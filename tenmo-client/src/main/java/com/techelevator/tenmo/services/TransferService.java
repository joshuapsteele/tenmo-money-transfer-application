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

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    // I think we should make this method a generic create Transfer instead of create SEND transfer,
    // because we can always specify the transfer type inside of the Transfer object itself.
    public Transfer createTransfer(Transfer newTransfer){
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "transfers",
                    HttpMethod.POST,
                    makeTransferEntity(newTransfer),
                    Transfer.class);
            transfer = response.getBody();
            return transfer;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Transfer failed. Try again.");
        }
        System.out.println("TransferService.sendTransfer() has not been implemented.");
        return null;
    }

    // I think this method is good. I just added in an actual Transfer object to return.
    public Transfer getTransferById(Long transferID) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(API_BASE_URL + "transfers/" + transferID,
                            HttpMethod.GET,
                            makeAuthEntity(),
                            Transfer.class);

            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            System.out.println("Transfer pull failed. ");
        }
        System.out.println("TransferService.getTransfer has not been implemented");
        return transfer;
    }

    public Transfer[] listTransfers(){
        Transfer[] userTransfers = null;
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfers",
                    HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            userTransfers = response.getBody();
        } catch (RestClientResponseException|ResourceAccessException e){
            System.out.println("Failed to retrieve transfers");
        }
        return userTransfers;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
