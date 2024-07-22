package br.com.chunksupload.dto;

import lombok.Data;

@Data
public class ChunkUploadDTO {
    private int index;
    private String originalName;
    private String uuid;
    private String mimetype;
}
