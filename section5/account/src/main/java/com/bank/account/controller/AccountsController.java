package com.bank.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

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
	//@CircuitBreaker(name = "detailsforCustomerDetailsApp", fallbackMethod = "mycustomerDetailsFallback")
	@Retry(name = "retryforCustomerDetails",fallbackMethod = "mycustomerDetailsFallback") 
	public CustomerDetails myCustomerDetails(@RequestHeader("eazybank-correlation-id") String correlationid,@RequestBody Customer customer) { 
		System.out.println("------------------"+correlationid);
		Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoansDetails(customer);
		List<Cards> cards = cardsFeignClient.getCardDetails(customer);

		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		customerDetails.setCards(cards);

		return customerDetails;

	}
	
	private CustomerDetails mycustomerDetailsFallback(@RequestHeader("eazybank-correlation-id") String correlationid,Customer customer, Throwable t) {
		Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoansDetails(customer);
		CustomerDetails cd = new CustomerDetails();
		cd.setAccounts(accounts);
		cd.setLoans(loans);
		return cd;
	}
	
	@GetMapping("/sayHello")
	@RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
	public String sayHello() {
		return "Hello, Welcome";
	}

	private String sayHelloFallback(Throwable t) {
		return "Max time request";
	}

}
