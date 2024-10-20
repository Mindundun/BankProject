package com.bankproject.bankproject.domain.account.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bankproject.bankproject.domain.account.request.AccountItemInsertRequest;
import com.bankproject.bankproject.domain.account.request.AccountItemSearchRequest;
import com.bankproject.bankproject.domain.account.request.AccountItemUpdateRequest;
import com.bankproject.bankproject.domain.account.response.AccountItemResponse;
import com.bankproject.bankproject.domain.account.service.AccountItemService;
import com.bankproject.bankproject.global.response.PagingResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/account-item")
@RequiredArgsConstructor
public class AccountItemController {

    private final AccountItemService accountItemService;

    // 1. 계좌 상품 등록
    @PostMapping
    public ResponseEntity<AccountItemResponse> insertAccountItem(@RequestBody AccountItemInsertRequest request) {
        AccountItemResponse response = accountItemService.insertAccountItem(request);
        return ResponseEntity.ok(response);
    }

    // 2. 계좌 상품 수정
    @PutMapping("/{id}")
    public ResponseEntity<AccountItemResponse> updateAccountItem(@PathVariable("id") Long id, @RequestBody AccountItemUpdateRequest request) {
        request.setId(id);
        AccountItemResponse response = accountItemService.updateAccountItem(request);
        return ResponseEntity.ok(response);
    }

    // 3. 계좌 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountItem(@PathVariable("id") Long id) {
        accountItemService.deleteAccountItem(id);
        return ResponseEntity.ok().build();
    }

    // 4. 계좌 상품 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<AccountItemResponse> getAccountItem(@PathVariable("id") Long id) {
        AccountItemResponse response = accountItemService.selectAccountItem(id);
        return ResponseEntity.ok(response);
    }

    // 5. 계좌 상품 목록 조회
    @GetMapping
    public ResponseEntity<PagingResponse<AccountItemResponse>> getAccountItemList(@ModelAttribute AccountItemSearchRequest request) {
        PagingResponse<AccountItemResponse> response = accountItemService.selectAccountItemList(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> fileUpload(@RequestPart("file") List<MultipartFile> files) {
        Map<String, Object> result = accountItemService.fileUpload(files);
        return ResponseEntity.ok(result);
    }

}
