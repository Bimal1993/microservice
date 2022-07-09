package com.bank.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.bank.account.config.AccountsServiceConfig;
import com.bank.account.model.Accounts;
import com.bank.account.model.Cards;
import com.bank.account.model.Customer;
import com.bank.account.model.CustomerDetails;
import com.bank.account.model.Loans;
import com.bank.account.model.Properties;
import com.bank.account.repository.AccountsRepository;
import com.bank.account.service.client.CardsFeignClient;
import com.bank.account.service.client.LoansFeignClient;

@RestController
public class AccountsController {
	@Autowired 
	private AccountsRepository accountRepository;
	
	@Autowired
	LoansFeignClient loansFeignClient;

	@Autowired
	CardsFeignClient cardsFeignClient;
	
	
	@Autowired
	AccountsServiceConfig accountsConfig;
    
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
	
	
	@GetMapping("/account/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(),
				accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr; 
	}
	
	@PostMapping("/myCustomerDetails")
	public CustomerDetails myCustomerDetails(@RequestBody Customer customer) {
		Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoansDetails(customer);
		List<Cards> cards = cardsFeignClient.getCardDetails(customer);

		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		customerDetails.setCards(cards);
		
		return customerDetails;

	}

}
