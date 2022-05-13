package sn.meum.digitalbanking.command.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.meum.digitalbanking.command.dto.CreateAccountRequestDTO;
import sn.meum.digitalbanking.command.dto.CreditAccountRequestDTO;
import sn.meum.digitalbanking.command.dto.DebitAccountRequestDTO;
import sn.meum.digitalbanking.coreapi.commands.CreateAccountCommand;
import sn.meum.digitalbanking.coreapi.commands.CreditAccountCommand;
import sn.meum.digitalbanking.coreapi.commands.DebitAccountCommand;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/commands/accounts")
@Slf4j
@AllArgsConstructor
public class AccountCommandRestAPI {
    private CommandGateway commandGateway;
    private EventStore eventStore;

    @PostMapping(path = "/create")
    public CompletableFuture<String> newAccount(@RequestBody CreateAccountRequestDTO requestDTO) {
        log.info("CreateAccountRequestDTO => " + requestDTO.getInitialBalance().toString());
        CompletableFuture<String> response = commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                requestDTO.getInitialBalance(),
                requestDTO.getCurrency()
        ));
        return response;
    }

    @PutMapping(path = "/credit")
    public CompletableFuture<String> credit(@RequestBody CreditAccountRequestDTO requestDTO) {
        log.info("CreditAccountRequestDTO => ");
        CompletableFuture<String> response = commandGateway.send(new CreditAccountCommand(
                requestDTO.getAccountId(),
                requestDTO.getAmount(),
                requestDTO.getCurrency()
        ));
        return response;
    }

    @PutMapping(path = "/debit")
    public CompletableFuture<String> debit(@RequestBody DebitAccountRequestDTO requestDTO) {
        log.info("DebitAccountRequestDTO => ");
        CompletableFuture<String> response = commandGateway.send(new DebitAccountCommand(
                requestDTO.getAccountId(),
                requestDTO.getAmount(),
                requestDTO.getCurrency()
        ));
        return response;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/events/{accountId}")
    public Stream getAccountEvents(@PathVariable String accountId) {
        return eventStore.readEvents(accountId).asStream();
    }

}
