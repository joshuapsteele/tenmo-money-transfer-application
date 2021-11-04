package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    // Our API BASE URL should be http://localhost:8080/api/
    private static final String API_BASE_URL = "http://localhost:8080/api/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null; //Do I need for transfers or just refer to authUser?
                                     //If I don't need an authToken for transfers, how to get?
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Transfer sendTransfer (Transfer newTransfer){
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "transfers",
                    HttpMethod.POST,
                    makeTransferEntity(newTransfer),
                    Transfer.class);

            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Transfer failed. Try again.");
        }
        System.out.println("TransferService.sendTransfer() has not been implemented.");
        return null;
    }

    public Transfer getTransfer(Long transferID) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(API_BASE_URL + "transfers/" + transferID,
                            HttpMethod.GET,
                            makeAuthEntity(), //I do realize this is an active error, I just want Walt's input
                            Transfer.class);

            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            System.out.println("Transfer pull failed. ");
        }
        System.out.println("TransferService.getTransfer has not been implemented");
        return transfer;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }
}
