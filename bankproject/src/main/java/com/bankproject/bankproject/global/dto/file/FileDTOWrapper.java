package com.bankproject.bankproject.global.dto.file;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class FileDTOWrapper {

    private List<FileDTO> files;

    public FileDTOWrapper() {
        this.files = new ArrayList<>();
    }

    public FileDTOWrapper(List<FileDTO> files) {
        if (files == null) {
            this.files = new ArrayList<>();
            return;
        }
        this.files = files;
    }

    public long getActiveFileCount() {
        return files == null ? 0 : files.stream().filter(FileDTO::getUseYn).count();
    }

    public List<FileDTO> getActiveFiles() {
        return files == null ? new ArrayList<>() : files.stream().filter(FileDTO::getUseYn).collect(Collectors.toList());
    }

    public Optional<FileDTO> getFileDTOById(String fileId) {
        return files.stream().filter(file -> file.getFileId().equals(fileId) && file.getUseYn()).findFirst();
    }

}
