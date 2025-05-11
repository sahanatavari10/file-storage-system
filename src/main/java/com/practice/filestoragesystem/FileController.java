package com.practice.filestoragesystem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<StoredFile> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        StoredFile storedFile = fileStorageService.store(file);
        return ResponseEntity.ok(storedFile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) throws IOException {
        byte[] fileData = fileStorageService.getFileData(id);
        StoredFile metadata = fileStorageService.getFileMetaData(id).orElseThrow();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getOriginalFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileData);
    }

    @GetMapping("/meta/{id}")
    public ResponseEntity<StoredFile> getFileMetadata(@PathVariable Long id) throws IOException {
        return fileStorageService.getFileMetaData(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
