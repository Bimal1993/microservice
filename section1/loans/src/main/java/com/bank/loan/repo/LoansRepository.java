package com.bank.loan.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.loan.model.Loans;
@Repository
public interface LoansRepository extends JpaRepository<Loans, Integer> {
	List<Loans> findByCustomerIdOrderByStartDtDesc(int customerId);
}
