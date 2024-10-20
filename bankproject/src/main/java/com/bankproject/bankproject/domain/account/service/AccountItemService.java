package com.bankproject.bankproject.domain.account.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bankproject.bankproject.domain.account.entity.AccountItem;
import com.bankproject.bankproject.domain.account.repository.AccountItemRepository;
import com.bankproject.bankproject.domain.account.request.AccountItemInsertRequest;
import com.bankproject.bankproject.domain.account.request.AccountItemSearchRequest;
import com.bankproject.bankproject.domain.account.request.AccountItemUpdateRequest;
import com.bankproject.bankproject.domain.account.response.AccountItemResponse;
import com.bankproject.bankproject.global.dto.FileDTO;
import com.bankproject.bankproject.global.response.PagingResponse;
import com.bankproject.bankproject.global.util.file.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountItemService {

    private final AccountItemRepository accountItemRepository;

    private final String DEFAULT_FILE_PATH = "/account";

    @Value("${custom.fileTempDirPath}")
    private String fileTempDirPath;

    @Value("${custom.fileDirPath}")
    private String fileDirPath;

    // 1. 계좌 상품 등록
    @Transactional
    public AccountItemResponse insertAccountItem(AccountItemInsertRequest request) {
        AccountItem accountItem = AccountItem.builder()
                .itemCode(request.getItemCode())
                .itemName(request.getItemName())
                .itemDesc(request.getItemDesc())
                .itemDetail(request.getItemDetail())
                .isUsed(true)
                .build();
        accountItemRepository.save(accountItem);

        // 파일 이관
        String randomKey = request.getRamdomKey();
        Path sourceDir = Paths.get(fileTempDirPath, randomKey);
        Path targetDir = Paths.get(fileDirPath, DEFAULT_FILE_PATH, accountItem.getId().toString());
        List<FileDTO> files = CustomFileUtil.moveFilesInDirectory(sourceDir, targetDir);

        // 파일 정보 저장
        accountItem = accountItem.toBuilder()
                .itemFiles(files)
                .build();
        accountItemRepository.save(accountItem);

        return AccountItemResponse.of(accountItem);
    }

    // 2. 계좌 상품 수정
    @Transactional
    public AccountItemResponse updateAccountItem(AccountItemUpdateRequest request) {
        AccountItem accountItem = accountItemRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("해당 계좌 상품이 존재하지 않습니다."));

        List<String> deleteFileIds = request.getDeleteFileIds();
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            List<FileDTO> files = accountItem.getItemFiles();
            for (FileDTO file : files) {
                if (deleteFileIds.contains(file.getFileId())) {
                    file.onUpdate("admin"); // 현재 사용자 ID를 전달
                }
            }
            accountItem = accountItem.toBuilder()
                    .itemFiles(files)
                    .build();
        }

        String ramdomKey = request.getRamdomKey();
        if (ramdomKey != null) {
            Path sourceDir = Paths.get(fileTempDirPath, ramdomKey);
            Path targetDir = Paths.get(fileDirPath, DEFAULT_FILE_PATH, accountItem.getId().toString());
            List<FileDTO> newFiles = CustomFileUtil.moveFilesInDirectory(sourceDir, targetDir);
            List<FileDTO> existingFiles = accountItem.getItemFiles();
            existingFiles.addAll(newFiles);
            // 파일 정보 저장
            accountItem = accountItem.toBuilder()
                    .itemFiles(existingFiles)
                    .build();
        }

        accountItem = accountItem.toBuilder()
                .itemName(request.getItemName())
                .itemDesc(request.getItemDesc())
                .build();
        accountItemRepository.save(accountItem);
        return AccountItemResponse.of(accountItem);
    }

    // 3. 계좌 상품 삭제
    @Transactional
    public void deleteAccountItem(Long AccountItemId) {
        AccountItem accountItem = accountItemRepository.findById(AccountItemId)
                .orElseThrow(() -> new RuntimeException("해당 계좌 상품이 존재하지 않습니다."));

        accountItem = accountItem.toBuilder()
                .isUsed(false)
                .build();

        accountItemRepository.save(accountItem);
    }

    // 4. 계좌 상품 조회
    public AccountItemResponse selectAccountItem(Long AccountItemId) {
        AccountItem accountItem = accountItemRepository.findById(AccountItemId)
                .orElseThrow(() -> new RuntimeException("해당 계좌 상품이 존재하지 않습니다."));
        return AccountItemResponse.of(accountItem);
    }

    // 5. 계좌 상품 리스트 조회
    public PagingResponse<AccountItemResponse> selectAccountItemList(AccountItemSearchRequest request) {
        List<AccountItem> accountItems = accountItemRepository.findAccountItemList(request);
        long totalCount = accountItemRepository.countAccountItems(request);
        List<AccountItemResponse> accountItemResponses = AccountItemResponse.of(accountItems);
        return PagingResponse.of(request.getPage(), request.getSize(), totalCount, accountItemResponses);
    }

    // 6. 계좌 상품 파일 업로드
    public Map<String, Object> fileUpload(List<MultipartFile> files) {
        return CustomFileUtil.fileUpload(files, fileTempDirPath, "AC");
    }
}
