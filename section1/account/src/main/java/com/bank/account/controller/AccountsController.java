package com.bank.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.account.model.Accounts;
import com.bank.account.model.Customer;
import com.bank.account.repository.AccountsRepository;

@RestController
public class AccountsController {
	@Autowired 
	private AccountsRepository accountRepository;
    
	@GetMapping("/test") 
	public String test() { 
		return "test";   
	} 
	
	@PostMapping("/myaccount")
	public Accounts getAccountDetails(@RequestBody Customer customer) {
		Accounts account = accountRepository.findByCustomerId(customer.getCustomerId());
		if (account != null) {
			return account;
		} else {
			return null;
		}
	}
}
