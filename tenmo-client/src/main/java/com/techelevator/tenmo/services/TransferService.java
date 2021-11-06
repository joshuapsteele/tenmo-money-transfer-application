package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
public class TransferService implements TransferServiceInterface {

    private static final String API_BASE_URL = "http://localhost:8080/api/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    @Override
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
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

    @Override
    public Transfer getCurrentUserTransferById(Long transferId) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(API_BASE_URL + "transfers/my-transfers/" + transferId,
                            HttpMethod.GET,
                            makeAuthEntity(),
                            Transfer.class);

            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Transfer pull failed. ");
        }
        return transfer;
    }

    @Override
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

    @Override
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

    @Override
    public boolean updateTransfer(Transfer transfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        boolean success = false;
        try {
            restTemplate.put(API_BASE_URL + "transfers/" + transfer.getTransferId(), entity);
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
