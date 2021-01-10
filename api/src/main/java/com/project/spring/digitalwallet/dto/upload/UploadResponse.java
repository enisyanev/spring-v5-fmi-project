package com.project.spring.digitalwallet.dto.upload;

import com.project.spring.digitalwallet.model.transaction.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {

    private Long accountId;
    private TransactionStatus status;
    private Long slipId;

}
