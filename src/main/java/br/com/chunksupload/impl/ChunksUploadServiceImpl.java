package br.com.chunksupload.impl;

import br.com.chunksupload.dto.ChunkUploadDTO;
import br.com.chunksupload.exception.FileUploadException;
import br.com.chunksupload.service.ChunksUploadService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ChunksUploadServiceImpl implements ChunksUploadService {
    @Value("${upload.dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new FileUploadException("Could not create upload directory!", e);
        }
    }

    private Path getChunkPath(ChunkUploadDTO chunkUploadDTO) {
        return Paths.get(uploadDir, chunkUploadDTO.getUuid() + "-" + chunkUploadDTO.getOriginalName() + "-" + chunkUploadDTO.getIndex());
    }

    private Path getFinalArchivePath(ChunkUploadDTO chunkUploadDTO) {
        return Paths.get(uploadDir, chunkUploadDTO.getUuid() + "-" + chunkUploadDTO.getOriginalName() + ".complete");
    }

    @Override
    public String uploadChunk(ChunkUploadDTO chunkUploadDTO, MultipartFile file) {
        try {
            Path chunkPath = getChunkPath(chunkUploadDTO);
            Files.write(chunkPath, file.getBytes());
            return "Chunk " + chunkUploadDTO.getIndex() + " uploaded successfully";
        } catch (IOException e) {
            deleteAllChunks(chunkUploadDTO);
            throw new FileUploadException("Failed to upload chunk " + chunkUploadDTO.getIndex(), e);
        }
    }

    private void deleteAllChunks(ChunkUploadDTO chunkUploadDTO) {
        try (Stream<Path> paths = Files.list(Paths.get(uploadDir))) {
            paths
                    .filter(path -> path.getFileName().toString().startsWith(chunkUploadDTO.getUuid() + "-" + chunkUploadDTO.getOriginalName()))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.out.println("Failed to delete chunk " + path.getFileName());
                            throw new FileUploadException("Failed to delete chunk " + path.getFileName(), e);
                        }
                    });
        } catch (IOException e) {
            System.out.println("Failed to delete chunks");
            throw new FileUploadException("Failed to delete chunks", e);
        }
    }

    @Override
    public String completeUpload(ChunkUploadDTO chunkUploadDTO) {
        Path finalPath = getFinalArchivePath(chunkUploadDTO);
        try (Stream<Path> paths = Files.list(Paths.get(uploadDir))) {
            List<Path> chunkPaths = paths
                    .filter(path -> path.getFileName().toString().startsWith(chunkUploadDTO.getUuid() + "-" + chunkUploadDTO.getOriginalName()))
                    .sorted(Comparator.comparingInt(path -> {
                        String[] parts = path.getFileName().toString().split("-");
                        return Integer.parseInt(parts[parts.length - 1]);
                    }))
                    .toList();

            for (Path chunkPath : chunkPaths) {
                Files.write(finalPath, Files.readAllBytes(chunkPath), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
                Files.delete(chunkPath);
            }
            return "Upload completed";
        } catch (IOException e) {
            try {
                Files.deleteIfExists(finalPath);
                deleteAllChunks(chunkUploadDTO);
            } catch (IOException ex) {
                System.out.println("Failed to clean up after failed upload");
                throw new FileUploadException("Failed to clean up after failed upload", ex);
            }
            throw new FileUploadException("Failed to complete upload", e);
        }
    }
}
