package sn.meum.digitalbanking.coreapi.events;

import lombok.Getter;
import sn.meum.digitalbanking.coreapi.commands.BaseCommand;

import java.math.BigDecimal;

public class AccountCreatedEvent extends BaseEvent<String> {
   @Getter private BigDecimal initialBalance;
    @Getter private String currency;

    public AccountCreatedEvent(String id, BigDecimal initialBalance, String currency) {
        super(id);
        this.initialBalance = initialBalance;
        this.currency = currency;
    }
}
