package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Название не может быть пустым")
    @Size(max = 255, message = "Название не может превышать 255 символов")
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String filename;
    
    @Column(nullable = false)
    private Long size;
    
    @Column(name = "upload_date", nullable = false)
    private Date uploadDate;
    
    @Column(name = "content_type")
    private String contentType;
    

    public Image() {}

    public Image(String name, String filename, Long size, Date uploadDate, String contentType) {
        this.name = name;
        this.filename = filename;
        this.size = size;
        this.uploadDate = uploadDate;
        this.contentType = contentType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", filename='" + filename + '\'' +
                ", size=" + size +
                ", uploadDate=" + uploadDate +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
