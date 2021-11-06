package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private static final String API_BASE_URL = "http://localhost:8080/api/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    // We need to determine where this method should be called from. Perhaps the App class?
    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    public Account getAccountByAccountId(Long accountId){
        Account account = null;
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + "accounts/" + accountId,
                            HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException| ResourceAccessException e){
            System.out.println("Failed to retrieve account");
        }
        return account;
    }

    public Account getAccountByUserId(Long userId) {
        Account account = null;
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + "accounts/user/" + userId,
                            HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException| ResourceAccessException e){
            System.out.println("Failed to retrieve account");
        }
        return account;
    }

    public String getUsernameByAccountId(Long accountId) {
        String username = null;
        try {
            ResponseEntity<String> response =
                    restTemplate.exchange(API_BASE_URL + "accounts/" + accountId + "/username",
                            HttpMethod.GET, makeAuthEntity(), String.class);
            username = response.getBody();
        } catch (RestClientResponseException| ResourceAccessException e){
            System.out.println("Failed to retrieve username");
        }
        return username;
    }

    // This way of retrieving the logged-in user's account balance makes use of the viewBalance() method
    // in the AccountController (URL = "/api/accounts/my-account-balance").
    public BigDecimal getUserAccountBalance(Long accountId) {
        BigDecimal userAccountBalance = null;
        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(API_BASE_URL + "accounts/my-account-balance",
                            HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            userAccountBalance = response.getBody();
        } catch (RestClientResponseException| ResourceAccessException e){
            System.out.println("Failed to retrieve account balance");
        }
        return userAccountBalance;
    }

    // JS: "Takes the above account"? Are you sure? Or just takes an ID? Let's discuss this.
//    public BigDecimal getAccountBalance(Long accountId) {
//        Account account = null;
//        try{
//            ResponseEntity<Account> response =
//                    restTemplate.exchange(API_BASE_URL + "accounts/" + accountId,
//                            HttpMethod.GET, makeAuthEntity(), Account.class);
//            account = response.getBody();
//        } catch (RestClientResponseException| ResourceAccessException e){
//            System.out.println("Failed to retrieve account");
//        }
//        if (account != null) {
//            return account.getBalance();
//        }
//        return null;
//    }

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

    // We still need this because, even though we don't have ADMINs, our users still need tokens to authenticate,
    // and this method is how the token gets bundled into the HTTP requests.
    // The reason why HttpHeaders wasn't working before was that you imported the wrong one
    // (java.net instead of the org.springframework one).
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }

}
