package com.bankproject.bankproject.global.dto.file;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class FileDtoWrapper {

    private List<FileDto> files;

    public FileDtoWrapper() {
        this.files = new ArrayList<>();
    }

    public FileDtoWrapper(List<FileDto> files) {
        if (files == null) {
            this.files = new ArrayList<>();
            return;
        }
        this.files = files;
    }

    public long getActiveFileCount() {
        return files == null ? 0 : files.stream().filter(FileDto::getUseYn).count();
    }

    public List<FileDto> getActiveFiles() {
        return files == null ? new ArrayList<>() : files.stream().filter(FileDto::getUseYn).collect(Collectors.toList());
    }

    public Optional<FileDto> getFileDTOById(String fileId) {
        return files.stream().filter(file -> file.getFileId().equals(fileId) && file.getUseYn()).findFirst();
    }

    public void deleteFiles(List<String> deleteFileIds) {
        if (deleteFileIds == null || deleteFileIds.isEmpty()) {
            return;
        }
        files.forEach(file -> {
            if (deleteFileIds.contains(file.getFileId())) {
                file.setUseYn(false);
            }
        });
    }

    public void addFile(List<FileDto> fileList) {
        if (fileList == null) {
            return;
        }
        files.addAll(fileList); // 일단 뒤로만 붙이기!
    }

}
