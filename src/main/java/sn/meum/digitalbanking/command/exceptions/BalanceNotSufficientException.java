package sn.meum.digitalbanking.command.exceptions;

public class BalanceNotSufficientException extends RuntimeException {
    public BalanceNotSufficientException(String balance_not_sufficient_exception) {
        super(balance_not_sufficient_exception);
    }
}
