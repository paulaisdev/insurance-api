package com.insurance.api.service.csv;

import org.springframework.web.multipart.MultipartFile;

public interface CsvService {
    void processarCsv(MultipartFile file);
}
