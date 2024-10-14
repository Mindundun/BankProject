package com.bankproject.bankproject.domain.board.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bankproject.bankproject.domain.board.dto.FileDTO;
import com.bankproject.bankproject.domain.board.entity.Board;
import com.bankproject.bankproject.domain.board.enums.BoardType;
import com.bankproject.bankproject.domain.board.repository.BoardRepository;
import com.bankproject.bankproject.domain.board.request.BoardInsertRequest;
import com.bankproject.bankproject.domain.board.request.BoardUpdateRequest;
import com.bankproject.bankproject.domain.board.response.BoardResponse;
import com.bankproject.bankproject.entity.UserEntity;
import com.bankproject.bankproject.global.exception.ServiceSystemException;
import com.bankproject.bankproject.global.util.file.CustomFileUtil;

import jakarta.persistence.EntityNotFoundException;
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

    private final String FILE_DIR_PATH = "/board";

    // 게시글 목록 조회
    public List<BoardResponse> getBoardList() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardResponse> boardResponseList = new ArrayList<>(boardList.size());
        for (Board board : boardList) {
            boardResponseList.add(BoardResponse.of(board));
        }
        return boardResponseList;
    }

    // 게시글 상세 조회
    public BoardResponse getBoardDetail(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ServiceSystemException("게시글이 존재하지 않습니다."));
        return BoardResponse.of(board);
    }

    // 게시글 등록
    @Transactional
    public BoardResponse insertBoard(HttpServletRequest req, BoardInsertRequest request) {
        try {
            int userId = 1;
            UserEntity user = new UserEntity();
            user.setId(userId);

            Board board = Board.builder()
                    .type(BoardType.guide)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .user(user)
                    .build();

            boardRepository.save(board);

            // 파일이 있을 경우 파일 이동
            if(request.getRandomKey() != null) {
                Long boardId = board.getId();
                Path sourceDir = Paths.get(fileTempDirPath, request.getRandomKey());
                Path targetDir = Paths.get(fileDirPath, FILE_DIR_PATH + "/" + boardId);

                List<FileDTO> fileList = CustomFileUtil.moveFilesInDirectory(sourceDir, targetDir);
                board.setFileList(fileList);
                boardRepository.save(board);
            }

            // 게시글 저장
            BoardResponse response = BoardResponse.of(board);
            return response;

        } catch (Exception e) {
            log.error("==== ERROR ===", e);
            log.error("=== ERROR METHOD : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            log.error("=== ERROR LINE : {}", e.getStackTrace()[0].getLineNumber());
            throw new ServiceSystemException("게시글 등록 중 오류가 발생했습니다.", e);
        }
    }

    // 게시글 수정
    @Transactional
    public BoardResponse updateBoard(HttpServletRequest req, Long boardId, BoardUpdateRequest request) {
        try {
            // 유효성 체크
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
            if(board.getUser().getId() != 1) {
                throw new ServiceSystemException("접근 권한이 없습니다.");
            }

            // 로직 처리
            log.info("updateBoard 실행");
            log.debug("boardId: {}, BoardUpdateRequest : {}", boardId, request);

            board.setTitle(request.getTitle());
            board.setContent(request.getContent());

            // 파일 처리
            List<FileDTO> exsistingFiles = board.getFileList();

            // 1. 새로운 파일이 있을 경우 이동
            if(request.getRandomKey() != null) {
                Path sourceDir = Paths.get(fileTempDirPath, request.getRandomKey());
                Path targetDir = Paths.get(fileDirPath, FILE_DIR_PATH + "/" + boardId);
                log.debug("sourceDir: {}", sourceDir);
                log.debug("targetDir: {}", targetDir);
                List<FileDTO> fileList = CustomFileUtil.moveFilesInDirectory(sourceDir, targetDir);
                exsistingFiles.addAll(fileList);
            }

            // 2. 삭제할 파일이 있을 경우 삭제
            List<String> deleteFileIds = request.getDeleteFileId();
            if(deleteFileIds != null && !deleteFileIds.isEmpty()) {
                List<FileDTO> deleteFileList = handelDeleteFiles(exsistingFiles, deleteFileIds);
                exsistingFiles.removeAll(deleteFileList);
            }

            // 게시글 저장
            board.setFileList(exsistingFiles);
            log.debug("board: {}", board);
            boardRepository.save(board);
            log.debug("DB update 완료");

            // 리턴
            BoardResponse response = BoardResponse.of(board);
            log.info("updateBoard 종료");
            return response;

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("==== ERROR ===", e);
            log.error("=== ERROR METHOD : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            log.error("=== ERROR LINE : {}", e.getStackTrace()[0].getLineNumber());
            throw new ServiceSystemException("게시글 수정 중 오류가 발생했습니다.", e);
        }
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long boardId) {
        try {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

            board.delete();
            boardRepository.save(board);

            board.getFileList().forEach(file -> {
                try {
                    CustomFileUtil.deleteFile(Paths.get(fileDirPath, FILE_DIR_PATH, file.getFileName()));
                } catch (IOException e) {
                    log.error("파일 삭제 중 오류 발생: {}", e.getMessage());
                    // TODO 파일 삭제중 오류가 발생시 추가적으로 처리할수 있게 해야함
                }
            });

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("==== ERROR ===", e);
            log.error("=== ERROR METHOD : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            log.error("=== ERROR LINE : {}", e.getStackTrace()[0].getLineNumber());
            throw new ServiceSystemException("게시글 삭제 중 오류가 발생했습니다.", e);
        }
    }

    // 파일 업로드 로직
    public Map<String, Object> fileUpload(List<MultipartFile> files) {
        try {
            Map<String, Object> result = new HashMap<>();
            String randomKey = "V" + System.currentTimeMillis();
            
            Path tempDir = Paths.get(fileTempDirPath, randomKey);
            Map<String, List<String>> uploadResult = CustomFileUtil.saveFilesWithDefaultPath(files, tempDir);

            result.put("randomKey", randomKey);
            result.put("uploadedFileName", uploadResult.get("fileNames"));
            return result;

        } catch (Exception e) {
            log.error("파일 업로드 중 일반 오류 발생: {}", e.getMessage());
            throw new ServiceSystemException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }


    private List<FileDTO> handelDeleteFiles(List<FileDTO> exsistingFiles, List<String> deleteFileIds){
        List<FileDTO> deleteFileList = exsistingFiles.stream()
                .filter(file -> deleteFileIds.contains(file.getFileId()))
                .collect(Collectors.toList());

        deleteFileList.forEach(file -> {
            try {
                CustomFileUtil.deleteFile(Paths.get(fileDirPath, FILE_DIR_PATH, file.getFileName()));
            } catch (IOException e) {
                log.error("파일 삭제 중 오류 발생: {}", e.getMessage());
                // TODO 파일 삭제중 오류가 발생시 추가적으로 처리할수 있게 해야함
            }
        });

        return deleteFileList;
    }

}
