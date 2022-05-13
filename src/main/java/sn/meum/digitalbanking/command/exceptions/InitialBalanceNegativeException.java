package sn.meum.digitalbanking.command.exceptions;

public class InitialBalanceNegativeException extends RuntimeException {
    public InitialBalanceNegativeException(String message) {
        super(message);
    }
}
