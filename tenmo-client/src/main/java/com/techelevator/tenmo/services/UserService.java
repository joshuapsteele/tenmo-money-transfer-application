package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class UserService {

    private static final String API_BASE_URL = "http://localhost:8080/api/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User[] findAllUsers() {
        User[] allUsers = new User[0];
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(API_BASE_URL + "users/",
                            HttpMethod.GET, makeAuthEntity(), User[].class);
            allUsers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Users not found.");
        }
        return allUsers;
    }

    public User findUserById(Long id) {
        User user = null;
        try {
            ResponseEntity<User> response =
                    restTemplate.exchange(API_BASE_URL + "users/" + id,
                            HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("User not found.");
        }
        return user;
    }

    public String getUsernameByAccountId(Long accountId) {
        Account account = accountService.getAccountByAccountId(accountId);
        Long userId = account.getUserId();
        User user = findUserById(userId);
        String username = user.getUsername();
        return username;
    }

    private HttpEntity<User> makeUserEntity(User user){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(user, headers);
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
