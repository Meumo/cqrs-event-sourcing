package sn.meum.digitalbanking.command.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditAccountRequestDTO {
    private String accountId;
    private BigDecimal amount;
    private String currency;
}
