package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.PhaseClient;
import it.polimi.ingsw.client.gui.MainMenu;
import it.polimi.ingsw.networking.Server;


public class StartingPoint {

    public static void main(String[] args) {

        if(args.length == 0) {
            System.out.println("Insert a parameter:");
            System.out.println("                      - gui                 : starts the graphic user interface");
            System.out.println("                      - cli                 : starts the command line interface");
            System.out.println("                      - server              : starts the server (mandatory for online games)");
            return;
        }

        switch(args[0]) {
            case "cli":
                new PhaseClient().run();
                break;
            case "gui":
                new MainMenu();
                break;
            case "server":
                new Server(43210);
                break;
        }
    }
}
