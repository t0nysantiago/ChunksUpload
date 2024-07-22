package br.com.chunksupload.controller;

import br.com.chunksupload.dto.ChunkUploadDTO;
import br.com.chunksupload.service.ChunksUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/chunksupload")
@CrossOrigin(origins = "*")
public class ChunksUploadController {

    private final ChunksUploadService chunksUploadService;

    @Autowired
    public ChunksUploadController(ChunksUploadService chunksUploadService) {
        this.chunksUploadService = chunksUploadService;
    }

    @PostMapping("/chunk")
    public ResponseEntity<String> uploadChunk(@ModelAttribute ChunkUploadDTO chunkUploadDto, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(chunksUploadService.uploadChunk(chunkUploadDto, file));
    }

    @PostMapping("/complete")
    public ResponseEntity<String> completeUpload(@ModelAttribute ChunkUploadDTO chunkUploadDto) {
        return ResponseEntity.ok(chunksUploadService.completeUpload(chunkUploadDto));
    }
}