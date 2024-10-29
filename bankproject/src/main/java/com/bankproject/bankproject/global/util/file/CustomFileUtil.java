package com.bankproject.bankproject.global.util.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.bankproject.bankproject.global.dto.file.FileDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomFileUtil {

    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".pdf", ".txt", ".ppt", ".pptx", ".doc", ".docx", ".xls", ".xlsx");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public static Map<String, Object> fileUpload(List<MultipartFile> files, String targetDir, String prefix) {
        try {

            Map<String, Object> result = new HashMap<>();
            String randomKey = prefix + System.currentTimeMillis();

            Path tempDir = Paths.get(targetDir, randomKey);
            List<String> uploadFilePaths = CustomFileUtil.saveFilesWithDefaultPath(files, tempDir);

            result.put("randomKey", randomKey);
            result.put("uploadedFilePaths", uploadFilePaths);
            return result;

        } catch (Exception e) {
            log.error("파일 업로드 중 일반 오류 발생: {}", e.getMessage());
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    public static List<String> saveFilesWithDefaultPath(List<MultipartFile> files, Path targetDir) throws IOException {
        try {
            // 1. 폴더 생성
            createDirectoryIfNotExists(targetDir);

            // 2. 파일 유효성 검사
            validateFiles(files);

            // 3. 파일 저장
            List<String> storedFilePaths = new ArrayList<>();
            List<String> existingFiles = Arrays.asList(targetDir.toFile().list());

            for(MultipartFile file : files) {
                String fileName = getUniqueFileName(file.getOriginalFilename(), existingFiles);
                log.debug("fileName: {}", fileName);
                Path targetPath = targetDir.resolve(fileName);
                log.debug("targetPath: {}", targetPath);
                String storedFilePath = saveFile(file, targetPath);
                log.debug("storedFilePath: {}", storedFilePath);
                storedFilePaths.add(storedFilePath);
            }

            // 4. 저장된 파일 경로 반환
            return storedFilePaths;

        } catch (IOException e) {
            log.error("파일 저장 중 오류 발생: {}", e);
            throw e;
        }
    }

    // 폴더 생성
    public static void createDirectoryIfNotExists(Path directory) throws IOException {
        try {
            if (Files.notExists(directory)) {
                Files.createDirectories(directory);
                log.info("폴더가 생성되었습니다: {}", directory.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("폴더 생성 중 오류 발생: {}", directory.toAbsolutePath(), e);
            throw e;
        }
    }

    // 파일 유효성 검사
    public static void validateFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new FileValidationException("파일이 전달되지 않았습니다.");
        }
        files.forEach(CustomFileUtil::validateFile);
    }

    public static void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileValidationException("파일이 전달되지 않았습니다.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileValidationException("파일 크기가 10MB를 초과했습니다.");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new FileValidationException("파일 이름이 존재하지 않습니다.");
        }

        String fileExt = getFileExtension(fileName);
        if (!SUPPORTED_EXTENSIONS.contains(fileExt.toLowerCase())) {
            throw new FileValidationException("지원하지 않는 파일 형식입니다.");
        }
    }

    // 파일 저장
    public static String saveFile(MultipartFile file, Path targetPath) throws IOException {
        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath.toString().replace("\\", "/");
        } catch (IOException e) {
            log.error("파일 저장 중 오류 발생: {}", file.getOriginalFilename(), e);
            throw e;
        }
    }

    // 파일 이동
    public static List<FileDto> moveFilesInDirectory(Path sourceDir, Path targetDir) throws RuntimeException {
        if(Files.notExists(sourceDir)) {
            throw new RuntimeException("파일이 존재하지 않습니다: " + sourceDir);
        }

        log.info("파일 이동 시작: {} -> {}", sourceDir, targetDir);
        Map<Path, Path> movedFiles = new HashMap<>();  // 원본 경로 -> 타겟 경로 저장
        List<FileDto> fileDTOs = new ArrayList<>();
        try {

            // 1. 타겟 폴더 생성
            createDirectoryIfNotExists(targetDir);

            // 2. sourceDir 안에 있는 모든 파일을 targetDir로 이동
            List<String> existingFileNames = new ArrayList<>(Files.list(targetDir)
                    .map(path -> path.getFileName().toString())
                    .toList());
            List<Path> paths = Files.list(sourceDir).toList();
            for(Path sourcePath : paths) {
                String originalFileName = sourcePath.getFileName().toString();
                String fileName = getUniqueFileName(sourcePath.getFileName().toString(), existingFileNames);
                Path targetPath = targetDir.resolve(fileName);
                Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("파일 이동 성공: {} -> {}", sourcePath, targetPath);

                movedFiles.put(sourcePath, targetPath);
                existingFileNames.add(fileName);
                fileDTOs.add(FileDto.builder()
                        .fileId(UUID.randomUUID().toString())
                        .fileName(originalFileName)
                        .filePath(targetPath.toString().replace("\\", "/"))
                        .fileExt(getFileExtension(fileName))
                        .fileSize(Files.size(targetPath))
                        .useYn(true)
                        .regDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build());
            }
            log.info("파일 이동 완료: {} -> {}", sourceDir, targetDir);

            // 3. sourceDir 삭제
            deleteFile(sourceDir);
            log.info("폴더 삭제 성공: {}", sourceDir);
            log.info("파일 이동 종료: {} -> {}", sourceDir, targetDir);

        } catch (IOException e) {
            log.error("파일 이동 중 오류 발생: 파일 원복 시도 중...");
            movedFiles.forEach((sourcePath, targetPath) -> {
                try {
                    Files.move(targetPath, sourcePath, StandardCopyOption.REPLACE_EXISTING);
                    log.info("파일 원복 성공: {} -> {}", targetPath, sourcePath);
                } catch (IOException e1) {
                    log.error("파일 원복 중 오류 발생: {} -> {}", targetPath, sourcePath, e1);
                }
            });
            throw new RuntimeException("파일 이동 중 오류 발생", e);
        }

        return fileDTOs;
    }

    // 파일 삭제
    public static void deleteFile(Path filePath) throws IOException {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("파일 삭제 중 오류 발생: {}", filePath, e);
            throw e;
        }
    }

    public static String getUniqueFileName(String originalFilename, List<String> existingFiles) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
        }
    
        int dotIndex = originalFilename.lastIndexOf(".");
        
        // 확장자가 없는 경우
        if (dotIndex == -1) {
            throw new IllegalArgumentException("확장자가 없는 파일 이름입니다: " + originalFilename);
        }

        String baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String extension = originalFilename.substring(dotIndex);

        String newFileName = originalFilename;
        int count = 1;
        while (existingFiles.contains(newFileName)) {
            newFileName = baseName + "(" + count++ + ")" + extension;
        }

        return newFileName;
    }

    public static String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            return "";
        }
        return fileName.substring(lastIndex);
    }

}
