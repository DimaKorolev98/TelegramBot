package org.example.rest;


import org.example.dto.PhotoDto;
import org.example.entity.Photo;
import org.example.repository.PhotoRepository;
import org.example.servises.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api")
public class CrudRestController {
@Autowired
    PhotoService photoService;

    @PostMapping("/photos")
    public ResponseEntity<byte[]> getPhoto(@RequestBody PhotoDto photoDto) throws IOException {
        // Получение фотографии из базы данных по ее названию
        Photo photo = photoService.getPhoto(photoDto.getName());
        if (photo == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] photoData = photo.getData();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type if needed
        headers.setContentLength(photoData.length);

        return new ResponseEntity<>(photoData, headers, HttpStatus.OK);
    }
}
