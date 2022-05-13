package sn.meum.digitalbanking.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.meum.digitalbanking.coreapi.enums.AccountStatus;
import sn.meum.digitalbanking.query.entities.AccountOperation;

import java.math.BigDecimal;
import java.util.Collection;

@Data @AllArgsConstructor @NoArgsConstructor
public class AccountDTO {
    private String id;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
    private Collection<AccountOperation> accountOperations;
}
