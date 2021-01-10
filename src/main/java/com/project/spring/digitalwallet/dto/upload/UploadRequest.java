package com.project.spring.digitalwallet.dto.upload;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UploadRequest {

    @NotNull
    private Long accountId;

    @NotNull
    private Long instrumentId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String currency;

    @NotNull
    private PaymentInstrumentType type;

}
