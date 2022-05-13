package sn.meum.digitalbanking.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.meum.digitalbanking.query.entities.Account;

public interface AccountRepository extends JpaRepository<Account,String> {
}
