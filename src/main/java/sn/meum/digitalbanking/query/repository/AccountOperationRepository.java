package sn.meum.digitalbanking.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.meum.digitalbanking.query.entities.AccountOperation;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByAccountId(String accountId);
}
