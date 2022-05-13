package sn.meum.digitalbanking.command.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data @AllArgsConstructor @NoArgsConstructor
public class CreateAccountRequestDTO {
    private BigDecimal initialBalance;
    private String currency;
}
