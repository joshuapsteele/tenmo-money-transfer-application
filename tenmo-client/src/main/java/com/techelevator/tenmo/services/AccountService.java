package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService implements AccountServiceInterface {
    private static final String API_BASE_URL = "http://localhost:8080/api/";
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken;

    @Override
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public Account getAccountByAccountId(Long accountId) {
        Account account = null;
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + "accounts/" + accountId,
                            HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to retrieve account");
        }
        return account;
    }

    @Override
    public Account getAccountByUserId(Long userId) {
        Account account = null;
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + "accounts/user/" + userId,
                            HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to retrieve account");
        }
        return account;
    }

    @Override
    public String getUsernameByAccountId(Long accountId) {
        String username = null;
        try {
            ResponseEntity<String> response =
                    restTemplate.exchange(API_BASE_URL + "accounts/" + accountId + "/username",
                            HttpMethod.GET, makeAuthEntity(), String.class);
            username = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to retrieve username");
        }
        return username;
    }

    @Override
    public BigDecimal getAccountBalanceByAccountId(Long accountId) {
        BigDecimal accountBalance = null;
        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(API_BASE_URL + "accounts/" + accountId + "/balance",
                            HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            accountBalance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to retrieve account balance.");
        }
        return accountBalance;
    }

    @Override
    public BigDecimal getCurrentUserAccountBalance(Long userId) {
        BigDecimal userAccountBalance = null;
        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(API_BASE_URL + "accounts/my-account-balance",
                            HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            userAccountBalance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to retrieve account balance");
        }
        return userAccountBalance;
    }

    @Override
    public boolean update(Account updatedAccount) {
        HttpEntity<Account> entity = makeAccountEntity(updatedAccount);

        boolean success = false;
        try {
            restTemplate.put(API_BASE_URL + "accounts/" + updatedAccount.getAccountId(), entity);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to update account.");
        }
        return success;
    }

    public HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    public HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }

}
