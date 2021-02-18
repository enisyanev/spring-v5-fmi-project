package com.project.spring.digitalwallet.dto.transaction;

import com.project.spring.digitalwallet.model.transaction.Direction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDto {

    private Long id;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime createdTime;
    private String description;
    private Long accountId;

    public TransactionDto(Long id, BigDecimal amount, LocalDateTime createdTime, Long accountId, Direction direction,
                          String currency) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.createdTime = createdTime;
        this.accountId = accountId;
        this.description = buildDescription(direction);
    }

    private String buildDescription(Direction direction) {
        String directionString = "added to";
        if (direction == Direction.W) {
            directionString = "subtracted from";

        }
        StringBuilder builder = new StringBuilder();
        builder.append("Transaction ").append(id.longValue()).append(": ").append(amount + " ")
            .append(currency + " ").append("were " + directionString + " ").append("account ").append(accountId);
        return builder.toString();

    }

}
