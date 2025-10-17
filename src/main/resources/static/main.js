// Основной JavaScript для галереи изображений
let currentImageId = null;

// Обработка клика по изображению для просмотра
$(document).ready(function() {
    $('#imageModal').on('show.bs.modal', function (event) {
        const button = $(event.relatedTarget);
        const imgSrc = button.data('img');
        const imgTitle = button.data('title');

        const modal = $(this);
        modal.find('#modalImage').attr('src', imgSrc);
        modal.find('#imageModalLabel').text(imgTitle);
    });

    // Обработка формы загрузки
    $('#uploadForm').on('submit', function(e) {
        const fileInput = $(this).find('input[type="file"]');
        if (fileInput[0].files.length === 0) {
            e.preventDefault();
            showAlert('Пожалуйста, выберите файл для загрузки', 'warning');
            return false;
        }
    });

    // Обработка нажатия Enter в поле редактирования названия
    $('#editNameInput').on('keypress', function(e) {
        if (e.which === 13) { // Enter key
            e.preventDefault();
            saveImageName();
        }
    });

    // Обработка клика по кнопке редактирования названия
    $(document).on('click', '.edit-name-btn', function() {
        const imageId = $(this).data('image-id');
        editImageName(imageId);
    });

    // Обработка клика по кнопке "Сохранить"
    $('#saveNameBtn').on('click', function() {
        saveImageName();
    });
});

// Показать уведомление
function showAlert(message, type = 'info') {
    const alertContainer = $('#alertContainer');
    const alertClass = `alert-${type}`;
    
    const alertHtml = `
        <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="close" data-dismiss="alert" aria-label="Закрыть">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    `;
    
    alertContainer.html(alertHtml);
    
    // Автоматически скрыть через 5 секунд
    setTimeout(function() {
        alertContainer.find('.alert').fadeOut();
    }, 5000);
}

// Редактирование названия изображения
function editImageName(imageId) {
    console.log('editImageName called with ID:', imageId);
    currentImageId = imageId;
    
    // Получить текущее название изображения
    fetch(`/api/images/${imageId}`)
        .then(response => {
            console.log('Response status:', response.status);
            return response.json();
        })
        .then(data => {
            console.log('Response data:', data);
            if (data.image) {
                $('#editNameInput').val(data.image.name);
                $('#editNameModal').modal('show');
            } else {
                showAlert('Ошибка при загрузке данных изображения', 'danger');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showAlert('Ошибка при загрузке данных изображения', 'danger');
        });
}

// Сохранение нового названия
function saveImageName() {
    const newName = $('#editNameInput').val().trim();
    
    if (!newName) {
        showAlert('Название не может быть пустым', 'warning');
        $('#editNameInput').focus();
        return;
    }
    
    if (newName.length > 255) {
        showAlert('Название не может превышать 255 символов', 'warning');
        $('#editNameInput').focus();
        return;
    }
    
    // Показать индикатор загрузки
    const saveBtn = $('#saveNameBtn');
    const originalText = saveBtn.text();
    saveBtn.prop('disabled', true).text('Сохранение...');
    
    fetch(`/api/images/${currentImageId}/name`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name: newName })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            $('#editNameModal').modal('hide');
            showAlert('Название успешно обновлено', 'success');
            // Обновить страницу через 1 секунду
            setTimeout(() => location.reload(), 1000);
        } else {
            showAlert(data.error || 'Ошибка при обновлении названия', 'danger');
            saveBtn.prop('disabled', false).text(originalText);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('Ошибка при обновлении названия', 'danger');
        saveBtn.prop('disabled', false).text(originalText);
    });
}

// Обработка изменения сортировки
function changeSorting() {
    const sortSelect = document.getElementById('sortSelect');
    const selectedValue = sortSelect.value;
    window.location.href = '?sort=' + selectedValue;
}