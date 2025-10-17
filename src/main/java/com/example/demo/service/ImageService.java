package com.example.demo.service;

import com.example.demo.model.Image;
import com.example.demo.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Загрузка нового изображения
     */
    public Image upload(MultipartFile file) throws IOException {
        validateFile(file);
        
        // Создаём папку uploads, если её нет
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Уникальное имя файла
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Сохраняем файл в папку uploads
        File dest = new File(uploadDir + filename);
        file.transferTo(dest);

        // Сохраняем запись в БД
        Image image = new Image(
            file.getOriginalFilename(), 
            filename, 
            file.getSize(), 
            new Date(),
            file.getContentType()
        );
        
        return imageRepository.save(image);
    }


    /**
     * Получить изображения с сортировкой
     */
    public List<Image> getSortedImages(String sortBy, String direction) {
        boolean isAscending = "asc".equalsIgnoreCase(direction);
        
        return switch (sortBy.toLowerCase()) {
            case "name" -> isAscending ? 
                imageRepository.findAllByOrderByNameAsc() : 
                imageRepository.findAllByOrderByNameDesc();
            case "size" -> isAscending ? 
                imageRepository.findAllByOrderBySizeAsc() : 
                imageRepository.findAllByOrderBySizeDesc();
            case "date" -> isAscending ? 
                imageRepository.findAllByOrderByUploadDateAsc() : 
                imageRepository.findAllByOrderByUploadDateDesc();
            default -> imageRepository.findAllByOrderByUploadDateDesc();
        };
    }

    /**
     * Получить изображение по ID
     */
    public Optional<Image> findById(Long id) {
        return imageRepository.findById(id);
    }

    /**
     * Обновить название изображения
     */
    public Image updateImageName(Long id, String newName) {
        Image image = imageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Изображение не найдено"));
        
        image.setName(newName);
        return imageRepository.save(image);
    }





    /**
     * Валидация файла
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть пустым");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Поддерживаются только изображения");
        }
        
        // Максимальный размер файла (50MB)
        if (file.getSize() > 50 * 1024 * 1024) {
            throw new IllegalArgumentException("Размер файла не должен превышать 50MB");
        }
    }
}