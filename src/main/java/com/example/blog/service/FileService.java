package com.example.blog.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    // 프로젝트 루트 경로에 'uploads' 폴더 자동 생성
    private final String uploadDir = "uploads/";

    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("빈 파일입니다.");
            }

            // 폴더 없으면 생성
            File folder = new File(uploadDir);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // 원본 파일명에서 확장자 추출 (ex: .jpg)
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 저장할 파일명: UUID + 확장자 (ex: abc-123.jpg)
            String storeFilename = UUID.randomUUID().toString() + extension;

            // 파일 저장
            Path path = Paths.get(uploadDir + storeFilename);
            Files.write(path, file.getBytes());

            return storeFilename;

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + e.getMessage());
        }
    }
    
    // 파일 경로 가져오기 (다운로드용)
    public Path getFilePath(String filename) {
        return Paths.get(uploadDir + filename);
    }
}