package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/api/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean createTransfer(Transfer newTransfer) {
        boolean wasCreated = false;
        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(API_BASE_URL + "transfers/",
                    HttpMethod.POST,
                    makeTransferEntity(newTransfer),
                    Boolean.class);
            wasCreated = response.getBody();
            return wasCreated;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Transfer failed. Try again." + e.getMessage());
        }
        return false;
    }

    public Transfer getTransferById(Long transferID) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(API_BASE_URL + "transfers/" + transferID,
                            HttpMethod.GET,
                            makeAuthEntity(),
                            Transfer.class);

            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Transfer pull failed. ");
        }
        return transfer;
    }

    public Transfer[] listAllTransfers() {
        Transfer[] allTransfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfers/",
                    HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            allTransfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to retrieve transfers" + e.getMessage());
        }
        return allTransfers;
    }

    public Transfer[] listAllTransfersCurrentUser() {
        Transfer[] currentUserTransfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfers/my-transfers",
                    HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            currentUserTransfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to retrieve transfers" + e.getMessage());
        }
        return currentUserTransfers;
    }

    public boolean updateTransfer(Transfer transfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        boolean success = false;
        try {
            restTemplate.exchange(API_BASE_URL + "transfers/" + transfer.getTransferId(), HttpMethod.PUT, entity, Transfer.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to update transfer.");
        }
        return success;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
