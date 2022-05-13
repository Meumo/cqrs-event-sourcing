package sn.meum.digitalbanking.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.meum.digitalbanking.query.entities.Account;
import sn.meum.digitalbanking.query.enums.OperationType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class AccountOperationDTO {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    private BigDecimal amount;
    private OperationType type;

}
