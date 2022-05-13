package sn.meum.digitalbanking.coreapi.events;

import lombok.Getter;
import sn.meum.digitalbanking.coreapi.commands.BaseCommand;

import java.math.BigDecimal;

public class AccountDebitedEvent extends BaseEvent<String> {
   @Getter private BigDecimal amount;
    @Getter private String currency;

    public AccountDebitedEvent(String id, BigDecimal amount, String currency) {
        super(id);
        this.amount = amount;
        this.currency = currency;
    }
}
