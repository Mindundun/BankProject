package com.bankproject.bankproject.domain.board.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bankproject.bankproject.domain.board.request.BoardInsertRequest;
import com.bankproject.bankproject.domain.board.request.BoardSearchRequest;
import com.bankproject.bankproject.domain.board.response.BoardResponse;
import com.bankproject.bankproject.domain.board.service.BoardService;
import com.bankproject.bankproject.global.dto.file.FileDTO;
import com.bankproject.bankproject.global.response.PagingResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;




@Slf4j
@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/fileUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> fileUpload(@RequestParam("file") List<MultipartFile> files) {
        log.info("fileUpload 실행 - 파일 개수: {}", files.size());
        Map<String, Object> result = boardService.fileUpload(files);
        log.info("fileUpload 종료 - 결과: {}", result);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<BoardResponse> insertBoard(@RequestBody BoardInsertRequest request) {
        BoardResponse entity = boardService.insertBoard(request);
        return ResponseEntity.ok(entity);
    }

    @GetMapping()
    public ResponseEntity<PagingResponse<BoardResponse>> getBoardList(@ModelAttribute BoardSearchRequest request) {
        PagingResponse<BoardResponse> returnData = boardService.getBoardList(request);
        return ResponseEntity.ok(returnData);
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> getFile(@RequestParam("boardId") Long boardId, @RequestParam("fileId") String fileId) {
        try {

            FileDTO fileDTO = boardService.getFileResource(boardId, fileId);

            // 파일 경로를 기반으로 Path 객체 생성
            Path path = Paths.get(fileDTO.getFilePath()).normalize(); // 경로 정규화
            Resource resource = new UrlResource(path.toUri()); // UrlResource로 감싸기

            // 파일이 존재하지 않으면 404 응답
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // 파일을 반환 (Content-Type은 적절히 설정)
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"") // inline으로 변경
                    .contentType(MediaType.IMAGE_JPEG) // 파일의 실제 MIME 타입으로 수정 (예: IMAGE_JPEG, IMAGE_PNG 등)
                    .body(resource);
            // return ResponseEntity.ok()
            //         .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            //         .contentType(MediaType.APPLICATION_OCTET_STREAM) // 필요에 따라 MIME 타입 수정
            //         .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build(); // 잘못된 URL 경로의 경우
        }
    }

}
