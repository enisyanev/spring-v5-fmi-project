package com.project.spring.digitalwallet.dto.fxrates;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FxRatesApiResponse {

    private String base;
    private LocalDate date;
    private Map<String, BigDecimal> rates;

}
