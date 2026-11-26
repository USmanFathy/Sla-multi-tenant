package com.tenantFlow.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String upload(MultipartFile file, String directory);

    void delete(String key);

    String getUrl(String key);
}