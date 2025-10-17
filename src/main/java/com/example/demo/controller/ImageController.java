package com.example.demo.controller;

import com.example.demo.model.Image;
import com.example.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Controller
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Главная страница с галереей изображений
     */
    @GetMapping("/")
    public String list(@RequestParam(value = "sort", defaultValue = "date") String sortBy,
                       @RequestParam(value = "dir", defaultValue = "desc") String direction,
                       Model model) {
        List<Image> images = imageService.getSortedImages(sortBy, direction);
        
        model.addAttribute("images", images);
        model.addAttribute("currentSort", sortBy);
        model.addAttribute("currentDir", direction);
        return "list";
    }

    /**
     * Загрузка нового изображения
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            imageService.upload(file);
            model.addAttribute("success", "Файл успешно загружен");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при загрузке: " + e.getMessage());
        }
        model.addAttribute("images", imageService.getSortedImages("date", "desc"));
        return "redirect:/";
    }

    /**
     * Получение изображения для отображения
     */
    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        Path file = Paths.get("uploads").resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok().body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * API endpoint для обновления названия изображения
     */
    @PutMapping("/api/images/{id}/name")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateImageName(@PathVariable Long id, 
                                                               @RequestBody Map<String, String> request) {
        try {
            String newName = request.get("name");
            if (newName == null || newName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Название не может быть пустым"));
            }
            
            Image updatedImage = imageService.updateImageName(id, newName.trim());
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Название успешно обновлено",
                "image", updatedImage
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Произошла ошибка при обновлении"));
        }
    }



    /**
     * API endpoint для получения информации об изображении
     */
    @GetMapping("/api/images/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getImage(@PathVariable Long id) {
        return imageService.findById(id)
            .map(image -> {
                Map<String, Object> response = new java.util.HashMap<>();
                response.put("image", image);
                return ResponseEntity.ok(response);
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Изображение не найдено")));
    }
}
