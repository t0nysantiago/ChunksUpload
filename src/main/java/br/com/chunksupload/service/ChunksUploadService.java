package br.com.chunksupload.service;

import br.com.chunksupload.dto.ChunkUploadDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ChunksUploadService {
    String uploadChunk(ChunkUploadDTO uploadDto, MultipartFile file);
    String completeUpload(ChunkUploadDTO uploadDto);
}
