package com.example.demo.repository;

import com.example.demo.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    
    /**
     * Получить все изображения с сортировкой по имени (по возрастанию)
     */
    List<Image> findAllByOrderByNameAsc();
    
    /**
     * Получить все изображения с сортировкой по имени (по убыванию)
     */
    List<Image> findAllByOrderByNameDesc();
    
    /**
     * Получить все изображения с сортировкой по размеру (по возрастанию)
     */
    List<Image> findAllByOrderBySizeAsc();
    
    /**
     * Получить все изображения с сортировкой по размеру (по убыванию)
     */
    List<Image> findAllByOrderBySizeDesc();
    
    /**
     * Получить все изображения с сортировкой по дате загрузки (по возрастанию)
     */
    List<Image> findAllByOrderByUploadDateAsc();
    
    /**
     * Получить все изображения с сортировкой по дате загрузки (по убыванию)
     */
    List<Image> findAllByOrderByUploadDateDesc();
    
    
}