package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.http.HttpHeaders;

public class AccountService {
    private static final String API_BASE_URL = "http://localhost:8080/api/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    public Account getAccount(Long accountId){
        Account account = null;
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + "account" + accountId,
                            HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException| ResourceAccessException e){
            System.out.println("Failed to retrieve account");
        }
        return account;
    }

    public BigDecimal getAccountBalance(){ //Takes the above account
        Account account = null;
        try{
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + "account" + accountId,
                            HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException| ResourceAccessException e){
            System.out.println("Failed to retrieve account");
        }
        return account.getBalance();
    }

    //Need to consult with Walt about this. Do we even need it since we don't need ADMIN users?
    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    // TODO Make sure that AccountService here on the client side matches up with AccountController on the server.

}
