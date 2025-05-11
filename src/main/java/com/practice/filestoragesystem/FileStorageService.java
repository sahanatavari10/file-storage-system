package com.practice.filestoragesystem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileRepository fileRepository;

    @Value("${file.storage.location}")
    private String storageLocation;

    public StoredFile store(MultipartFile file) throws IOException{
        String storedFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(storageLocation, storedFileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes(), StandardOpenOption.CREATE_NEW);

        StoredFile storedFile = StoredFile.builder()
                .originalFileName(file.getOriginalFilename())
                .storedFileName(storedFileName)
                .size(file.getSize())
                .uploadTime(LocalDateTime.now())
                .build();
        return fileRepository.save(storedFile);
    }

    public Optional<StoredFile> getFileMetaData(Long id){
        return fileRepository.findById(id);
    }

    public byte[] getFileData(Long id) throws IOException{
        StoredFile file = fileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("File not found"));
        Path path = Paths.get(storageLocation, file.getStoredFileName());
        return Files.readAllBytes(path);
    }
}
