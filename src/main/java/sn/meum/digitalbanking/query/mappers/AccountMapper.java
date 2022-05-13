package sn.meum.digitalbanking.query.mappers;

import org.mapstruct.Mapper;
import sn.meum.digitalbanking.query.dto.AccountDTO;
import sn.meum.digitalbanking.query.dto.AccountOperationDTO;
import sn.meum.digitalbanking.query.entities.Account;
import sn.meum.digitalbanking.query.entities.AccountOperation;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO fromAccount(Account account);
    Account fromAccountDTO(AccountDTO accountDTO);
    AccountOperationDTO fromAccountOperation(AccountOperation accountOperation);
}
