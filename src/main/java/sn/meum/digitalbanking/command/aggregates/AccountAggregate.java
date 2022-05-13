package sn.meum.digitalbanking.command.aggregates;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import sn.meum.digitalbanking.command.exceptions.AmountNegativeException;
import sn.meum.digitalbanking.command.exceptions.BalanceNotSufficientException;
import sn.meum.digitalbanking.command.exceptions.InitialBalanceNegativeException;
import sn.meum.digitalbanking.coreapi.commands.CreateAccountCommand;
import sn.meum.digitalbanking.coreapi.commands.CreditAccountCommand;
import sn.meum.digitalbanking.coreapi.commands.DebitAccountCommand;
import sn.meum.digitalbanking.coreapi.enums.AccountStatus;
import sn.meum.digitalbanking.coreapi.events.AccountActivatedEvent;
import sn.meum.digitalbanking.coreapi.events.AccountCreatedEvent;
import sn.meum.digitalbanking.coreapi.events.AccountCreditedEvent;
import sn.meum.digitalbanking.coreapi.events.AccountDebitedEvent;

import java.math.BigDecimal;

@Aggregate
@Slf4j
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate() {
        // Default Constructor required by Axon
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        log.info("CreateAccountCommand received ...");
        /* Business logic */
        if (command.getInitialBalance().doubleValue() < 0)
            throw new InitialBalanceNegativeException("Initial Balance should not be negative");
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getInitialBalance(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        log.info("AccountCreatedEvent occurred ...");
        this.accountId = event.getId();
        this.balance = event.getInitialBalance();
        this.currency = event.getCurrency();
        this.status = AccountStatus.CREATED;
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        log.info("AccountActivatedEvent occurred ...");
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command) {
        log.info("CreditAccountCommand received ...");
        /* Business logic */
        if (command.getAmount().doubleValue() < 0)
            throw new AmountNegativeException("Amount should not be negative");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        log.info("AccountCreditedEvent occurred ...");
        this.balance = this.balance.add(event.getAmount());
    }

    @CommandHandler
    public void handle(DebitAccountCommand command) {
        log.info("DebitAccountCommand received ...");
        /* Business logic */
        if (command.getAmount().doubleValue() < 0) throw new AmountNegativeException("Amount should not be negative");
        if (this.balance.subtract(command.getAmount()).doubleValue() < 0) {
            throw new BalanceNotSufficientException("Balance Not Sufficient Exception => " +balance);
        }
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        log.info("AccountDebitedEvent occurred ...");
        this.balance = this.balance.subtract(event.getAmount());
    }
}
