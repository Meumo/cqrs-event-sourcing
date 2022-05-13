package sn.meum.digitalbanking.coreapi.events;

import lombok.Getter;
import sn.meum.digitalbanking.coreapi.enums.AccountStatus;

import java.math.BigDecimal;

public class AccountActivatedEvent extends BaseEvent<String> {
    @Getter private AccountStatus status;

    public AccountActivatedEvent(String id, AccountStatus status) {
        super(id);
        this.status = status;
    }
}
