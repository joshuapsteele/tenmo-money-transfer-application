package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.view.ApplicationService;

public class App {

    private ApplicationService applicationService;

    public static void main(String[] args) {
        App app = new App(new ApplicationService());
        app.run();
    }

    public App(ApplicationService applicationService) {
        this.applicationService = applicationService;
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
