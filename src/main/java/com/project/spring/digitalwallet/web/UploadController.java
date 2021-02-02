package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.dto.upload.MoneyRequest;
import com.project.spring.digitalwallet.dto.upload.MoneyResponse;
import com.project.spring.digitalwallet.service.UploadService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping
    public MoneyResponse uploadMoney(@Valid @RequestBody MoneyRequest request) {
        return uploadService.upload(request);
    }

}
