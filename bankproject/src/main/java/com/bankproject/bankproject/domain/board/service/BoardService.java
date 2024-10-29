package com.bankproject.bankproject.domain.board.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bankproject.bankproject.domain.board.entity.Board;
import com.bankproject.bankproject.domain.board.event.BoardCreateEvent;
import com.bankproject.bankproject.domain.board.event.BoardUpdateEvent;
import com.bankproject.bankproject.domain.board.repository.BoardRepository;
import com.bankproject.bankproject.domain.board.request.BoardInsertRequest;
import com.bankproject.bankproject.domain.board.request.BoardSearchRequest;
import com.bankproject.bankproject.domain.board.request.BoardUpdateRequest;
import com.bankproject.bankproject.domain.board.response.BoardResponse;
import com.bankproject.bankproject.entity.UserEntity;
import com.bankproject.bankproject.global.dto.file.FileDto;
import com.bankproject.bankproject.global.dto.file.FileDtoWrapper;
import com.bankproject.bankproject.global.response.PagingResponse;
import com.bankproject.bankproject.global.util.file.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final ApplicationEventPublisher publisher;

    @Value("${custom.fileTempDirPath}")
    private String fileTempDirPath;

    @Value("${custom.fileDirPath}")
    private String fileDirPath;

    public PagingResponse<BoardResponse> getBoardList(BoardSearchRequest request) {
        Integer page = Optional.ofNullable(request.getPage()).orElse(1);
        Integer size = Optional.ofNullable(request.getPageOfSize()).orElse(50);
        Pageable pageable = PageRequest.of(page - 1, size);

        List<Board> boardList = boardRepository.findBoardWithUser(request, pageable);
        Long totalCount = boardRepository.countBoard(request);
        List<BoardResponse> responseList = BoardResponse.ofList(boardList);

        PagingResponse<BoardResponse> response = PagingResponse.of(page, size, totalCount, responseList);
        return response;
    }

    public BoardResponse getBoardById(Long id) {
        Board board = boardRepository.findByIdWithUser(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return BoardResponse.of(board);
    }

    @Transactional
    public BoardResponse insertBoard(BoardInsertRequest request) {

        log.info("insertBoard 실행 request: {}", request);

        Board board = Board.builder()
            .type(request.getCategory())
            .title(request.getTitle())
            .content(request.getContent())
            .build();
        boardRepository.save(board);

        // 파일 이관
        String randomKey = request.getRandomKey();
        Path sourceDir = Paths.get(fileTempDirPath, randomKey);
        Path targetDir = Paths.get(fileDirPath, "/board", board.getId().toString());
        List<FileDto> files = CustomFileUtil.moveFilesInDirectory(sourceDir, targetDir);
        board.setFileDTOWrapper(new FileDtoWrapper(files));
        boardRepository.save(board);

        BoardResponse response = BoardResponse.of(board);
        log.info("insertBoard 종료");

        publisher.publishEvent(new BoardCreateEvent(this, board));

        return response;
    }

    @Transactional
    public BoardResponse updateBoard(Long id, BoardUpdateRequest request) {
        Board board = boardRepository.findByIdWithUser(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        UserEntity currentUser = getCurrentUser();
        if (!board.getCreateUser().equals(currentUser)) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }
        log.info("updateBoard 실행 request: {}", request);

        board.setTitle(request.getTitle());
        board.setContent(request.getContent());

        if(request.getIsPin() != null && request.getPinExpireDate() != null) {
            LocalDateTime pinExpireDate = LocalDateTime.parse(request.getPinExpireDate());
            LocalDateTime now = LocalDateTime.now();
            if(pinExpireDate.isBefore(now)) {
                throw new RuntimeException("유효하지 않은 고정기간입니다.");
            }
            board.setIsPin(request.getIsPin());
            board.setPinExpireDate(pinExpireDate);
        }

        FileDtoWrapper fileDtoWrapper = board.getFileDTOWrapper();

        // 파일 삭제
        List<String> deleteFileIds = request.getDeleteFileIds();
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            fileDtoWrapper.deleteFiles(deleteFileIds);
        }

        // 파일 이관
        String randomKey = request.getRandomKey();
        if(randomKey != null) {
            Path sourceDir = Paths.get(fileTempDirPath, randomKey);
            Path targetDir = Paths.get(fileDirPath, "/board", board.getId().toString());
            List<FileDto> files = CustomFileUtil.moveFilesInDirectory(sourceDir, targetDir);
            fileDtoWrapper.addFile(files);
        }
        board.setFileDTOWrapper(fileDtoWrapper);
        boardRepository.save(board);
        log.info("updateBoard 종료");

        publisher.publishEvent(new BoardUpdateEvent(this, board));

        return BoardResponse.of(board);
    }

    @Transactional
    public void deleteBoard(Long id) {
        Board board = boardRepository.findByIdWithUser(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        UserEntity currentUser = getCurrentUser();
        if (!board.getCreateUser().equals(currentUser)) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }

        board.delete();
        boardRepository.save(board);
    }

    // 파일 업로드
    public Map<String, Object> fileUpload(List<MultipartFile> files) {
        return CustomFileUtil.fileUpload(files, fileTempDirPath, "GS");
    }

    public FileDto getFileResource(Long boardId, String fileId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        
        return board.getFileDTOWrapper().getFileDTOById(fileId)
                .orElseThrow(() -> new RuntimeException("파일이 존재하지 않습니다."));
    }

    private UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (UserEntity) authentication.getPrincipal(); // 현재 사용자 반환
        }
        return null; // 인증되지 않은 경우 null 반환
    }

}
