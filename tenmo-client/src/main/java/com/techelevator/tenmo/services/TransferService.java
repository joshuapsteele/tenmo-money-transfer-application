package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    // TODO Make sure that TransferService here on the client side matches up with TransferController on the server.

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

//    Commented this out for now.

//    public Transfer getTransfer(Long transferID) {
//        Transfer transfer = null;
//        try {
//            ResponseEntity<Transfer> response =
//                    restTemplate.exchange(API_BASE_URL + "transfers/" + transferID,
//                            HttpMethod.GET,
//                            makeAuthEntity(),
//                            Transfer.class);
//
//            transfer = response.getBody();
//        } catch (RestClientResponseException | ResourceAccessException e){
//            System.out.println("Transfer pull failed. ");
//        }
//        System.out.println("TransferService.getTransfer has not been implemented");
//        return transfer;
//    }

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

    //Need to consult with Walt about this
    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
