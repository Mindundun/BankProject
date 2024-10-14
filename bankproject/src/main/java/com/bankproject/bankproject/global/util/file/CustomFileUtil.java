package com.bankproject.bankproject.global.util.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.bankproject.bankproject.domain.board.dto.FileDTO;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomFileUtil {

    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".pdf", ".txt", ".ppt", ".pptx", ".doc", ".docx", ".xls", ".xlsx");
    
    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_FILE_SIZE_STR;

    @Value("${spring.servlet.multipart.max-request-size}")
    private String MAX_REQUEST_SIZE_STR;

    private static long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static long MAX_REQUEST_SIZE = 10 * 1024 * 1024; // 10MB

    @PostConstruct
    public void init() {
        log.debug("MAX_FILE_SIZE: {}", MAX_FILE_SIZE_STR);
        log.debug("MAX_REQUEST_SIZE: {}", MAX_REQUEST_SIZE_STR);
        MAX_FILE_SIZE = Long.parseLong(MAX_FILE_SIZE_STR.replace("MB", "")) * 1024 * 1024;
        MAX_REQUEST_SIZE = Long.parseLong(MAX_REQUEST_SIZE_STR.replace("MB", "")) * 1024 * 1024;
        log.debug("MAX_FILE_SIZE: {}", MAX_FILE_SIZE);
        log.debug("MAX_REQUEST_SIZE: {}", MAX_REQUEST_SIZE);
    }

    // 파일 저장
    public static Map<String, List<String>> saveFilesWithDefaultPath(List<MultipartFile> files, Path targetDir) throws IOException {
        try {
            // 1. 폴더 생성
            createDirectoryIfNotExists(targetDir);

            // 2. 파일 유효성 검사
            validateFiles(files);

            // 3. 파일 저장
            List<String> storedFilePaths = new ArrayList<>();
            List<String> fileNames = new ArrayList<>();
            List<String> existingFiles = Arrays.asList(targetDir.toFile().list());

            for(MultipartFile file : files) {
                String fileName = getUniqueFileName(file.getOriginalFilename(), existingFiles);
                log.debug("fileName: {}", fileName);

                Path targetPath = targetDir.resolve(fileName);
                log.debug("targetPath: {}", targetPath);

                String storedFilePath = saveFile(file, targetPath);
                log.debug("storedFilePath: {}", storedFilePath);

                storedFilePaths.add(storedFilePath);
                fileNames.add(fileName);
            }

            // 리턴
            Map<String, List<String>> result = Map.of("fileNames", fileNames, "storedFilePaths", storedFilePaths);
            return result;
            
        } catch (IOException e) {
            log.error("=== ERROR LINE : {}", e.getStackTrace()[0].getLineNumber());
            log.error("==== ERROR ====", e);
            throw e;
        }
    }

    // 폴더 생성
    public static void createDirectoryIfNotExists(Path directory) throws IOException {
        try {
            if (Files.notExists(directory)) {
                Files.createDirectories(directory);
                log.debug("폴더가 생성되었습니다: {}", directory.toAbsolutePath());
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

        // 파일 유효성 검사
        files.forEach(CustomFileUtil::validateFile);

        // 요청 크기 검사
        long totalFileSize = files.stream()
                .map(MultipartFile::getSize)
                .reduce(0L, Long::sum);

        if (totalFileSize > MAX_REQUEST_SIZE) {
            throw new FileValidationException("요청 크기가 10MB를 초과했습니다.");
        }
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
    public static List<FileDTO> moveFilesInDirectory(Path sourceDir, Path targetDir) throws IOException {
        Map<Path, Path> movedFiles = new HashMap<>();  // 원본 경로 -> 타겟 경로 저장
        List<FileDTO> fileDTOList = new ArrayList<>();
        log.info("파일 이동 시작: {} -> {}", sourceDir, targetDir);
        try {
            // 1. 타겟 폴더 생성
            createDirectoryIfNotExists(targetDir);

            // 2. sourceDir 안에 있는 모든 파일을 targetDir로 이동
            List<Path> paths = Files.list(sourceDir).toList();
            List<String> existingFiles = Arrays.asList(targetDir.toFile().list());
            for(Path sourcePath : paths) {
                String saveFileName = getUniqueFileName(sourcePath.getFileName().toString(), existingFiles);
                Path targetPath = targetDir.resolve(saveFileName);
                Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                log.debug("파일 이동 성공: {} -> {}", sourcePath, targetPath);
                movedFiles.put(sourcePath, targetPath);

                // FileDTO 생성
                FileDTO fileDTO = FileDTO.builder()
                        .fileId(UUID.randomUUID().toString())
                        .fileName(sourcePath.getFileName().toString())
                        .filePath(targetPath.toString().replace("\\", "/"))
                        .build();

                fileDTOList.add(fileDTO);
            }

            // 3. sourceDir 삭제
            deleteFile(sourceDir);
            log.info("폴더 삭제 성공: {}", sourceDir);
            log.info("파일 이동 완료: {} -> {}", sourceDir, targetDir);

        } catch (IOException e) {
            log.error("오류 발생: 파일 원복 시도 중...");
            movedFiles.forEach((sourcePath, targetPath) -> {
                try {
                    Files.move(targetPath, sourcePath, StandardCopyOption.REPLACE_EXISTING);
                    log.info("파일 원복 성공: {} -> {}", targetPath, sourcePath);
                } catch (IOException e1) {
                    log.error("파일 원복 중 오류 발생: {} -> {}", targetPath, sourcePath, e1);
                }
            });
            throw e;
        }

        return fileDTOList;
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
        int lastIndex = originalFilename.lastIndexOf('.');
        String baseName;
        String extension;
        String newFileName = originalFilename;
        if (lastIndex == -1) {
            baseName = originalFilename;
            extension = "";
        } else {
            baseName = originalFilename.substring(0, lastIndex);
            extension = originalFilename.substring(lastIndex);
        }

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
