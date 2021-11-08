package com.techelevator.tenmo;

import com.techelevator.view.ApplicationService;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final ApplicationService applicationService;

    public App(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public static void main(String[] args) {
        App app = new App(new ApplicationService());
        app.run();
    }

    public void run() {

        System.out.println("***************************************************************");
        System.out.println();
        System.out.println("WELCOME TO");
        System.out.println("\n" +
                "$$$$$$$$\\ $$$$$$$$\\                                   \n" +
                "\\__$$  __|$$  _____|                                  \n" +
                "   $$ |   $$ |      $$$$$$$\\  $$$$$$\\$$$$\\   $$$$$$\\  \n" +
                "   $$ |   $$$$$\\    $$  __$$\\ $$  _$$  _$$\\ $$  __$$\\ \n" +
                "   $$ |   $$  __|   $$ |  $$ |$$ / $$ / $$ |$$ /  $$ |\n" +
                "   $$ |   $$ |      $$ |  $$ |$$ | $$ | $$ |$$ |  $$ |\n" +
                "   $$ |   $$$$$$$$\\ $$ |  $$ |$$ | $$ | $$ |\\$$$$$$  |\n" +
                "   \\__|   \\________|\\__|  \\__|\\__| \\__| \\__| \\______/ \n");
        System.out.println("****************************************************************");

        applicationService.registerAndLogin();
        applicationService.mainMenu();
    }
}