package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.dto.upload.UploadRequest;
import com.project.spring.digitalwallet.dto.upload.UploadResponse;
import com.project.spring.digitalwallet.service.UploadService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/upload")
public class UploadController {

    private UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping
    public UploadResponse uploadMoney(@Valid @RequestBody UploadRequest request) {
        return uploadService.upload(request);
    }

}
