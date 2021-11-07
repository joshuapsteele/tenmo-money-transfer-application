package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class UserService implements UserServiceInterface {

    private static final String API_BASE_URL = "http://localhost:8080/api/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final AccountServiceInterface accountServiceInterface = new AccountService();
    private final TransferServiceInterface transferServiceInterface = new TransferService();

    private String authToken;

    @Override
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
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

    @Override
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

    @Override
    public String getUsernameByAccountId(Long accountId) {
        Account account = accountServiceInterface.getAccountByAccountId(accountId);
        Long userId = account.getUserId();
        User user = findUserById(userId);
        String username = user.getUsername();
        return username;
    }

    public HttpEntity<User> makeUserEntity(User user){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(user, headers);
    }

    public HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
