package com.bankproject.bankproject.domain.board.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bankproject.bankproject.domain.board.entity.Board;
import com.bankproject.bankproject.domain.board.enums.BoardType;
import com.bankproject.bankproject.domain.board.repository.BoardRepository;
import com.bankproject.bankproject.domain.board.request.BoardInsertRequest;
import com.bankproject.bankproject.domain.board.request.BoardSearchRequest;
import com.bankproject.bankproject.domain.board.response.BoardResponse;
import com.bankproject.bankproject.entity.UserEntity;
import com.bankproject.bankproject.global.dto.FileDTO;
import com.bankproject.bankproject.global.response.PagingResponse;
import com.bankproject.bankproject.global.util.file.CustomFileUtil;
import com.querydsl.core.Tuple;

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

    public PagingResponse<BoardResponse> getBoardList(BoardSearchRequest request) {
        List<Board> boardList = boardRepository.findBoard(request);
        Long totalCount = boardRepository.countBoard(request);
        List<BoardResponse> responseList = BoardResponse.ofList(boardList);

        PagingResponse<BoardResponse> response = PagingResponse.of(1, 30, totalCount, responseList);
        return response;
    }

    public BoardResponse getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return BoardResponse.of(board);
    }

    public BoardResponse getBoardWithUser(Long id) {
        Tuple tuple = boardRepository.findBoardWithUserById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return BoardResponse.of(tuple.get(0, Board.class), tuple.get(1, UserEntity.class));
    }

    @Transactional
    public BoardResponse insertBoard(BoardInsertRequest request) {

        log.info("insertBoard 실행 request: {}", request);

        Board board = Board.builder()
            .type(BoardType.guide)
            .title(request.getTitle())
            .content(request.getContent())
            .build();
        boardRepository.save(board);

        // 파일 이관
        String randomKey = request.getRandomKey();
        Path sourceDir = Paths.get(fileTempDirPath, randomKey);
        Path targetDir = Paths.get(fileDirPath, "/board", board.getId().toString());
        List<FileDTO> files = CustomFileUtil.moveFilesInDirectory(sourceDir, targetDir);

        board.setFiles(files);
        boardRepository.save(board);

        BoardResponse response = BoardResponse.of(board);
        log.info("insertBoard 종료");
        return response;
    }

    // 파일 업로드
    public Map<String, Object> fileUpload(List<MultipartFile> files) {
        return CustomFileUtil.fileUpload(files, fileTempDirPath, "GS");
    }
    
    public FileDTO getFileResource(Long boardId, String fileId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return board.getFiles().stream()
                .filter(file -> file.getFileId().equals(fileId) && file.getUseYn())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("파일이 존재하지 않습니다."));
    }
}
