package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;
import com.techelevator.view.ConsoleService;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private String currentUserToken = currentUser.getToken();
    private ConsoleService console;
    private AuthenticationService authenticationService;

    private AccountService accountService = new AccountService();
    private TransferService transferService = new TransferService();
    private UserService userService = new UserService();

    public static void main(String[] args) {
    	App app = new App(
    			new ConsoleService(System.in, System.out),
				new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
    	BigDecimal currentBalance = accountService.getUserAccountBalance(currentUser.getUser().getId());
		System.out.println("Your current balance is $" + currentBalance);
	}

	private void viewTransferHistory() {
		Transfer[] transfers = transferService.listTransfers();
		String prompt = "For further details on a transfer, enter its ID. " +
				"Otherwise, press '0'"; //Prompt to pass into the console service's getUII method.
		System.out.println("-------------------------------------------");
		System.out.println("\t\t\t\tTransfers");
		System.out.println("\t\tID\t\tFrom/to\t\tAmount");
		for (Transfer transfer : transfers) {
			System.out.println(transfer.getTransferId() + "\t\t" + transfer.getAccountFrom() + "\t/\t" + transfer.getAccountTo() + "\t\t" + transfer.getAmount());
		}
		System.out.println("-------------------------------------------");

		Long request = Long.valueOf(console.getUserInputInteger(prompt));
		Transfer requestedTransfer = transferService.getTransferById(request);
		requestedTransfer.toString();

		/*
		while (requestedTransfer != 0) { //Above gets the requested transfer, below loops until it
			for (int i = 0; i < transfers.length; i++){ //finds the correct transfer, then displays its details, and breaks from loop.
				if (transfers[i].getTransferId() == requestedTransfer){
					transfers[i].toString();
					break;
				}
			}
			requestedTransfer = Long.valueOf(console.getUserInputInteger(prompt));
		}
		 */

		// FIGURE OUT HOW/WHERE TO ASK USER FOR INPUT HERE
		// ASK THE USER WHICH TRANSFER THEY WANT MORE INFORMATION ABOUT
		// GIVE THEM THE OPTION TO CANCEL WITH 0
		// "Please enter transfer ID to view details (0 to cancel): " (CONSOLESERVICE CAN ASK FOR INPUT AND RECORD INPUT)
		// After you get number from user (might need to parse String into integer and then cast to a long, or parse to a long), use the TransferService to getTransferById(Long id);
		// After you get that Transfer, use the Transfer.toString() method;
		
	}

	private void sendBucks() {

		// UserService findAllUsers();
    	// Take User[] and print it pretty.

    	/* LIST OF ALL USERS
		-------------------------------------------
				Users
		ID          Name
		-------------------------------------------
		313         Bernice
		54          Larry
		---------

		Enter ID of user you are sending to (0 to cancel):
		Enter amount:

		DM: If what I have for getting a specific transfer (above method), then we can replicate it
		here for the Users. Then use the getUI method from console and parse the String it
		returns into a BigDecimal. Then we can check appropriate IDs and balances. */

		// Check account from and to IDs to make sure they're valid.
		// Check account FROM balance (YOUR balance, if you're sending money) and make sure that it's greater than or equal to the transfer amount.
		// IF NOT, DON'T ACTUALLY CREATE A TRANSFER OBJECT!

		// After validation, CREATE TRANSFER OBJECT
		// Use TransferService to tell the SERVER to create a new transfer in the database
		// Use AccountService to tell the Server to update the balances of 2 separate accounts

	}

	private void requestBucks() {
		// Same structure, more or less, as above. Validate the numbers on the CLIENT SIDE before creating a Transfer Object.
		// Transfer Object will have a DIFFERENT transfer_type_id and transfer_status than the sendBucks transfer.
		// ASK user to approve or reject the transfer.
		// DON'T tell the server to update account balances until the transfer is approved.

	}

	private void viewPendingRequests() {
		/* Get a Transfer[], but JUST Transfers that have a certain transfer_status_id.
		System.out.println("-------------------------------------------");
		System.out.println("\t\t\tPending Transfers");
		System.out.println("\t\tID\t\tFrom/to\t\tAmount");
		for (Transfer transfer : transfers) {
			if(transfer.getTransferStatus == 2) {
			System.out.println(transfer.getTransferId() + "\t\t" + transfer.getAccountFrom() + "\t/\t" + transfer.getAccountTo() + "\t\t" + transfer.getAmount());
			}
		}
		System.out.println("-------------------------------------------");
		 */
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		    if (currentUserToken != null) {
		    	accountService.setAuthToken(currentUserToken);
		    	transferService.setAuthToken(currentUserToken);
		    	userService.setAuthToken(currentUserToken);
			} else {
				System.out.println("USER AUTHENTICATION ERROR: Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
