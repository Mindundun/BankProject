package com.bankproject.bankproject.domain.board.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bankproject.bankproject.domain.board.dto.FileDTO;
import com.bankproject.bankproject.domain.board.entity.Board;
import com.bankproject.bankproject.domain.board.enums.BoardType;
import com.bankproject.bankproject.domain.board.repository.BoardRepository;
import com.bankproject.bankproject.domain.board.request.BoardInsertRequest;
import com.bankproject.bankproject.domain.board.response.BoardResponse;
import com.bankproject.bankproject.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Value("${custom.fileTempDirPath}")
    private String fileTempDirPath;

    @Value("${custom.fileDirPath}")
    private String fileDirPath;

    // 파일 업로드 로직
    public Map<String, Object> fileUpload(List<MultipartFile> files) {
        Map<String, Object> result = new HashMap<>();
        List<String> uploadedFiles = new ArrayList<>();
        List<String> uploadFilePaths = new ArrayList<>();
        String randomKey = "V" + System.currentTimeMillis();

        // 임시 폴더 생성
        Path tempDir = Paths.get(fileTempDirPath, randomKey);
        if (!Files.exists(tempDir)) {
            try {
                Files.createDirectories(tempDir);  // 디렉토리 생성
            } catch (IOException e) {
                log.error("임시 디렉토리 생성 실패: {}", e.getMessage());
                throw new RuntimeException("임시 디렉토리 생성에 실패했습니다.");
            }
        }

        // 파일 업로드 처리
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                log.warn("빈 파일이 전달됨: {}", file.getOriginalFilename());
                continue;
            }

            // 파일 확장자 검증 (ex: .jpg, .png, .pdf 등만 허용)
            String originalFilename = file.getOriginalFilename();
            if (!isValidExtension(originalFilename)) {
                log.warn("허용되지 않는 파일 형식: {}", originalFilename);
                continue;
            }

            // 파일 저장 경로
            String newFileName = getUniqueFileName(originalFilename, uploadedFiles);
            Path tempFilePath = tempDir.resolve(newFileName);

            try {
                // 임시 폴더에 파일 저장
                Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);
                uploadedFiles.add(newFileName);  // 저장된 파일 이름을 리스트에 추가
                uploadFilePaths.add(tempFilePath.toString().replace("\\", "/"));  // 파일 경로를 리스트에 추가
            } catch (IOException e) {
                log.error("파일 업로드 실패: {}", originalFilename);
                throw new RuntimeException("파일 업로드에 실패했습니다.");
            }
        }

        result.put("randomKey", randomKey);
        result.put("uploadedFilePaths", uploadFilePaths);
        return result;
    }

    // 파일 확장자 검증
    private boolean isValidExtension(String fileName) {
        List<String> validExtensions = Arrays.asList(
            ".jpg", ".jpeg", ".png",
            ".pdf", ".txt", ".ppt", ".pptx",
            ".doc", ".docx", ".xls", ".xlsx"
        );
        
        if (fileName != null) {
            String lowerCaseFileName = fileName.toLowerCase();
            return validExtensions.stream().anyMatch(lowerCaseFileName::endsWith);
        }
    
        return false;
    }

    // 중복 파일명을 처리하는 메서드
    private String getUniqueFileName(String originalFilename, List<String> uploadedFiles) {
        String newFileName = originalFilename;
        int count = 1;

        // uploadedFiles에 파일이 이미 존재하는지 확인
        while (uploadedFiles.contains(newFileName)) {
            String fileNameWithoutExt = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            newFileName = fileNameWithoutExt + "_" + count + extension;
            count++;
        }

        return newFileName;
    }

    @Transactional
    public BoardResponse insertBoard(HttpServletRequest req, BoardInsertRequest request) {
        int userId = 1;
        UserEntity user = new UserEntity();
        user.setId(userId);

        Board board = Board.builder()
            .type(BoardType.guide)
            .title(request.getTitle())
            .content(request.getContent())
            .user(user)
            .build();

        Path uploadDir = Paths.get(fileDirPath, "/board");
        if (!Files.exists(uploadDir)) {
            try {
                Files.createDirectories(uploadDir);
            } catch (IOException e) {
                log.error("업로드 디렉토리 생성 실패: {}", e.getMessage());
                throw new RuntimeException("업로드 디렉토리 생성에 실패했습니다.");
            }
        }

        if(request.getRandomKey() != null) {
            List<String> randomKey = request.getRandomKey();
            List<FileDTO> fileDTOList = new ArrayList<>();
            for (String key : randomKey) {
                Path tempDir = Paths.get(fileTempDirPath, key);
                if (!Files.exists(tempDir)) {
                    log.warn("임시 디렉토리가 존재하지 않습니다: {}", key);
                    continue;
                }

                // tempDir에 있는 파일을 uploadDir로 이동
                try {
                    // 이동할 파일의 이름
                    Files.list(tempDir).forEach(file -> {
                        String originalFileName = file.getFileName().toString();
                        String newFileName = UUID.randomUUID().toString() + "_" + originalFileName; // 새로운 파일 이름 생성
                        Path targetFilePath = uploadDir.resolve(newFileName);

                        try {
                            Files.move(file, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
                            // FileDTO 객체 생성 및 추가
                            fileDTOList.add(new FileDTO(originalFileName, targetFilePath.toString().replace("\\", "/")));
                        } catch (IOException e) {
                            log.error("파일 이동 실패: {}", e.getMessage());
                            throw new RuntimeException("파일 이동에 실패했습니다.");
                        }
                    });

                } catch (IOException e) {
                    log.error("파일 목록 가져오기 실패: {}", e.getMessage());
                    throw new RuntimeException("파일 목록 가져오기에 실패했습니다.");
                }
            }
            board.setFiles(fileDTOList);
        }
        boardRepository.save(board);
        BoardResponse response = BoardResponse.of(board);
        return response;
    }

    public List<BoardResponse> getBoardList() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardResponse> boardResponseList = new ArrayList<>(boardList.size());
        for (Board board : boardList) {
            boardResponseList.add(BoardResponse.of(board));
        }
        return boardResponseList;
    }

}
