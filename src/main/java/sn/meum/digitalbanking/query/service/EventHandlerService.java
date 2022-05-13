package sn.meum.digitalbanking.query.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.meum.digitalbanking.coreapi.enums.AccountStatus;
import sn.meum.digitalbanking.coreapi.events.AccountActivatedEvent;
import sn.meum.digitalbanking.coreapi.events.AccountCreatedEvent;
import sn.meum.digitalbanking.coreapi.events.AccountCreditedEvent;
import sn.meum.digitalbanking.coreapi.events.AccountDebitedEvent;
import sn.meum.digitalbanking.query.dto.AccountDTO;
import sn.meum.digitalbanking.query.entities.Account;
import sn.meum.digitalbanking.query.entities.AccountOperation;
import sn.meum.digitalbanking.query.enums.OperationType;
import sn.meum.digitalbanking.query.mappers.AccountMapper;
import sn.meum.digitalbanking.query.queries.GetAccountByIdQuery;
import sn.meum.digitalbanking.query.repository.AccountOperationRepository;
import sn.meum.digitalbanking.query.repository.AccountRepository;

import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
public class EventHandlerService {
    private AccountRepository accountRepository;
    private AccountOperationRepository accountOperationRepository;
    private AccountMapper accountMapper;
    private QueryUpdateEmitter queryUpdateEmitter;

    @ResetHandler
    public void resetDatabase() {
        log.info("Reset DataBase ..........");
        accountRepository.deleteAll();
        accountOperationRepository.deleteAll();
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.info("************* Query Side **********************");
        log.info(" AccountCreatedEvent Occurred");
        Account account = new Account();
        account.setId(event.getId());
        account.setBalance(event.getInitialBalance());
        account.setCurrency(event.getCurrency());
        account.setStatus(AccountStatus.CREATED);
        accountRepository.save(account);
    }

    @EventHandler
    @Transactional
    public void on(AccountActivatedEvent event) {
        log.info("************* Query Side **********************");
        log.info(" AccountActivatedEvent Occurred");
        Account account = accountRepository.findById(event.getId()).get();
        account.setStatus(AccountStatus.ACTIVATED);
        accountRepository.save(account);
    }

    @EventHandler
    @Transactional
    public void on(AccountCreditedEvent event) {
        log.info("************* Query Side **********************");
        log.info(" AccountCreditedEvent Occurred");

        Account account = accountRepository.findById(event.getId()).get();

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationDate(new Date());
        accountOperation.setAmount(event.getAmount());
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAccount(account);
        accountOperationRepository.save(accountOperation);
        account.setBalance(account.getBalance().add(event.getAmount()));
        Account savedAccount = accountRepository.save(account);
        AccountDTO accountDTO = accountMapper.fromAccount(savedAccount);
        queryUpdateEmitter.emit(message ->
                        ((GetAccountByIdQuery) message.getPayload()).getAccountId().equals(event.getId())
                , accountDTO);

    }

    @EventHandler
    @Transactional
    public void on(AccountDebitedEvent event) {
        log.info("************* Query Side **********************");
        log.info(" AccountDebitedEvent Occurred");

        Account account = accountRepository.findById(event.getId()).get();

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationDate(new Date()); // à ne pas faire
        accountOperation.setAmount(event.getAmount());
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAccount(account);
        accountOperationRepository.save(accountOperation);
        account.setBalance(account.getBalance().subtract(event.getAmount()));
        Account savedAccount = accountRepository.save(account);
        AccountDTO accountDTO = accountMapper.fromAccount(savedAccount);
        queryUpdateEmitter.emit(message ->
                        ((GetAccountByIdQuery) message.getPayload()).getAccountId().equals(event.getId())
                , accountDTO);
    }
}
