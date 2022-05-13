package sn.meum.digitalbanking.query.controllers;

import lombok.AllArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import sn.meum.digitalbanking.query.dto.AccountDTO;
import sn.meum.digitalbanking.query.dto.AccountHistoryDTO;
import sn.meum.digitalbanking.query.dto.AccountOperationDTO;
import sn.meum.digitalbanking.query.queries.GetAccountByIdQuery;
import sn.meum.digitalbanking.query.queries.GetAccountHistoryQuery;
import sn.meum.digitalbanking.query.queries.GetAccountOperationsQuery;
import sn.meum.digitalbanking.query.queries.GetAllAccountsQuery;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/query/accounts")
@AllArgsConstructor
public class AccountQueryRestAPI {
    private QueryGateway queryGateway;

    @GetMapping(path = "/allAccounts")
    public List<AccountDTO> allAccounts(){
        List<AccountDTO> response=queryGateway.query(new GetAllAccountsQuery(),ResponseTypes.multipleInstancesOf(AccountDTO.class)).join();
        return response;
    }

    @GetMapping(path = "/{accountId}")
    public CompletableFuture<AccountDTO> getAccount(@PathVariable String accountId){
        CompletableFuture<AccountDTO> query = queryGateway.query(
                new GetAccountByIdQuery(accountId),
                AccountDTO.class
        );
        return query;
    }

    @GetMapping(path = "/{accountId}/operations")
    public CompletableFuture<List<AccountOperationDTO>> getOperations(@PathVariable String accountId){
        CompletableFuture<List<AccountOperationDTO>> query = queryGateway.query(
                new GetAccountOperationsQuery(accountId),
                ResponseTypes.multipleInstancesOf(AccountOperationDTO.class)
        );
        return query;
    }

    @GetMapping(path = "/{accountId}/history")
    public CompletableFuture<AccountHistoryDTO> getHistory(@PathVariable String accountId){
        CompletableFuture<AccountHistoryDTO> query = queryGateway.query(
                new GetAccountHistoryQuery(accountId),
                ResponseTypes.instanceOf(AccountHistoryDTO.class)
        );
        return query;
    }

    @GetMapping(path = "{accountId}/watch",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountDTO>  subscribeToAccount(@PathVariable String accountId){
        SubscriptionQueryResult<AccountDTO,AccountDTO> result=queryGateway.subscriptionQuery(
                new GetAccountByIdQuery(accountId),
                ResponseTypes.instanceOf(AccountDTO.class),
                ResponseTypes.instanceOf(AccountDTO.class)
        );
        return result.initialResult().concatWith(result.updates());
    }
}
