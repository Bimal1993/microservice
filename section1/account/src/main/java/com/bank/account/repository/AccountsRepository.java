package com.bank.account.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.account.model.Accounts;
@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long>{
Accounts findByCustomerId(int customerId);
}
