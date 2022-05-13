package sn.meum.digitalbanking.query.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import sn.meum.digitalbanking.query.dto.AccountDTO;
import sn.meum.digitalbanking.query.dto.AccountHistoryDTO;
import sn.meum.digitalbanking.query.dto.AccountOperationDTO;
import sn.meum.digitalbanking.query.entities.Account;
import sn.meum.digitalbanking.query.entities.AccountOperation;
import sn.meum.digitalbanking.query.mappers.AccountMapper;
import sn.meum.digitalbanking.query.queries.GetAccountByIdQuery;
import sn.meum.digitalbanking.query.queries.GetAccountHistoryQuery;
import sn.meum.digitalbanking.query.queries.GetAccountOperationsQuery;
import sn.meum.digitalbanking.query.queries.GetAllAccountsQuery;
import sn.meum.digitalbanking.query.repository.AccountOperationRepository;
import sn.meum.digitalbanking.query.repository.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class QueryHandlerService {
    private AccountRepository accountRepository;
    private AccountOperationRepository accountOperationRepository;
    private AccountMapper accountMapper;

    @QueryHandler
    public List<AccountDTO> handle(GetAllAccountsQuery query) {
        List<Account> allAccounts = accountRepository.findAll();
        List<AccountDTO> allAccountsDTOS = allAccounts.stream().map(account -> accountMapper.fromAccount(account)).collect(Collectors.toList());
        return allAccountsDTOS;
    }

    @QueryHandler
    public AccountDTO handle(GetAccountByIdQuery query) {
        Account account = accountRepository.findById(query.getAccountId()).get();

        /**
         AccountDTO accountDTO=new AccountDTO();
         accountDTO.setId(account.getId());
         accountDTO.setBalance(account.getBalance());
         accountDTO.setCurrency(account.getCurrency());
         accountDTO.setStatus(account.getStatus());
         */
        AccountDTO accountDTO = accountMapper.fromAccount(account);
        return accountDTO;
    }

    @QueryHandler
    public List<AccountOperationDTO> handle(GetAccountOperationsQuery query) {
        List<AccountOperation> operationList = accountOperationRepository.findByAccountId(query.getAccountId());
        List<AccountOperationDTO> operationDTOS = operationList
                .stream()
                .map(accOp -> accountMapper.fromAccountOperation(accOp))
                .collect(Collectors.toList());
        return operationDTOS;
    }

    @QueryHandler
    public AccountHistoryDTO handle(GetAccountHistoryQuery query) {
        Account account = accountRepository.findById(query.getAccountId()).get();
        AccountDTO accountDTO = accountMapper.fromAccount(account);
        List<AccountOperation> operationList = accountOperationRepository.findByAccountId(query.getAccountId());
        List<AccountOperationDTO> operationDTOS = operationList
                .stream()
                .map(accOp -> accountMapper.fromAccountOperation(accOp))
                .collect(Collectors.toList());
        return new AccountHistoryDTO(accountDTO, operationDTOS);
    }
}
