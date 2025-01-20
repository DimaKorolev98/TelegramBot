package org.example.servises;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.example.entity.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.example.repository.PhotoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class PhotoService   {
    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private ResourceLoader resourceLoader;

//    @Transactional
//    @PostConstruct
//    public void savePhotosFromResources() throws IOException {
//        List<Photo> photos = new ArrayList<>();
//
//        // Загрузка ресурсов из папки resources
//        Resource resourceFolder = resourceLoader.getResource("classpath:images");
//        Path folderPath = Paths.get(resourceFolder.getURI());
//
//        // Поиск всех JPG файлов и сохранение их в базу данных
//        Files.walk(folderPath)
//                .filter(path -> path.toString().endsWith(".jpg"))
//                .forEach(path -> {
//                    try {
//                        String filename = path.getFileName().toString();
//                        byte[] data = Files.readAllBytes(path);
//                        Photo photo = new Photo();
//                        photo.setName(filename);
//                        photo.setData(data);
//                        photos.add(photo);
//                    } catch (IOException e) {
//                        // Обработка ошибок чтения файла
//                        e.printStackTrace();
//                    }
//                });
//
//        // Сохранение всех фотографий в базу данных
//        photoRepository.saveAll(photos);
//    }
    public List<Photo> findAll() {
        return photoRepository.findAll();
    }

    @Cacheable(value = "photos", key = "#name")
    public Photo getPhoto(String name) {
        return photoRepository.findByName(name);
    }

    @CacheEvict(value = "photos", key = "#name")
    public Photo savePhoto(String name, byte[] data) {
        Photo photo = new Photo();
        photo.setName(name);
        photo.setData(data);
        return photoRepository.save(photo);
    }
}