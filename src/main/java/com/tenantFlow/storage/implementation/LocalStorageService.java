package com.tenantFlow.storage.implementation;

import com.tenantFlow.exception.StorageException;
import com.tenantFlow.storage.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.nio.file.StandardCopyOption;
@Service
@ConditionalOnProperty(
        name = "storage.driver",
        havingValue = "local",
        matchIfMissing = true
)
public class LocalStorageService implements StorageService {

    private final Path root;

    private final String baseUrl;

    public LocalStorageService(
            @Value("${storage.local.root}") String root,
            @Value("${storage.local.base-url}") String baseUrl
    ) {
        this.root = Path.of(root)
                .toAbsolutePath()
                .normalize();

        this.baseUrl = baseUrl;
    }

    @Override
    public String upload(MultipartFile file, String directory) {

        String filename = UUID.randomUUID()
                + getExtension(file);

        String key = directory + "/" + filename;

        Path destination = resolvePath(key);

        Path tempFile = null;

        try {

            Files.createDirectories(destination.getParent());

            tempFile = Files.createTempFile(
                    destination.getParent(),
                    "upload-",
                    ".tmp"
            );

            try (InputStream input = file.getInputStream()) {

                Files.copy(
                        input,
                        tempFile,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }

            Files.move(
                    tempFile,
                    destination,
                    StandardCopyOption.ATOMIC_MOVE
            );

            return key;

        } catch (IOException e) {

            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException cleanupException) {
                    e.addSuppressed(cleanupException);
                }
            }

            throw new StorageException(
                    "Failed to upload file", e
            );
        }
    }
    private Path resolvePath(String key) {

        Path path = root.resolve(key).normalize();

        if (!path.startsWith(root) || path.equals(root)) {
            throw new IllegalArgumentException(
                    "Invalid storage path"
            );
        }

        return path;
    }
    @Override
    public void delete(String key) {

        Path path = resolvePath(key);

        try {

            Files.deleteIfExists(path);

        } catch (IOException e) {

            throw new StorageException(
                    "Failed to delete file", e
            );
        }
    }

    @Override
    public String getUrl(String key) {

        return baseUrl + "/" + key;
    }
    private String getExtension(MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType == null) {
            throw new IllegalArgumentException(
                    "Invalid file type"
            );
        }

        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "image/webp" -> ".webp";
            default -> throw new IllegalArgumentException(
                    "Unsupported file type"
            );
        };
    }
}