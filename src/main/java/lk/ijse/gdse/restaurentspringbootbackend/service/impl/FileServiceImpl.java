package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl {
    private final String uploadDir = "uploads/";

    public String saveFile(MultipartFile file) {
        try {
            // Create uploads folder if not exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save file
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return URL
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("File upload failed!", e);
        }
    }
}
