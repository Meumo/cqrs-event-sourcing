package sn.meum.digitalbanking.coreapi.events;

import lombok.Getter;
import sn.meum.digitalbanking.coreapi.commands.BaseCommand;

import java.math.BigDecimal;

public class AccountCreditedEvent extends BaseEvent<String> {
   @Getter private BigDecimal amount;
    @Getter private String currency;

    public AccountCreditedEvent(String id, BigDecimal amount, String currency) {
        super(id);
        this.amount = amount;
        this.currency = currency;
    }
}
