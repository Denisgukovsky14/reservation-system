package com.designhub.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/deals/temp/";


    @PostMapping
    public ResponseEntity<?> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
            Path uploadPath = Paths.get(System.getProperty("user.dir"), UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Создана директория: " + uploadPath.toAbsolutePath());
            }

            List<String> savedUrls = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                String originalName = file.getOriginalFilename();
                String extension = "";
                if (originalName != null && originalName.contains(".")) {
                    extension = originalName.substring(originalName.lastIndexOf("."));
                }
                String fileName = UUID.randomUUID().toString() + extension;

                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());

                String fileUrl = "/uploads/deals/temp/" + fileName;
                savedUrls.add(fileUrl);

                System.out.println("Файл сохранён: " + filePath.toAbsolutePath());
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "urls", savedUrls
            ));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Ошибка при сохранении файлов: " + e.getMessage()
            ));
        }
    }
}