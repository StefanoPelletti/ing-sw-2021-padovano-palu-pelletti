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
            System.out.println("                      - server <port>       : set a specific port number");
            return;
        }

        switch(args[0]) {
            case "cli":
                (new PhaseClient()).run();
                break;
            case "gui":
                (new MainMenu()).run();
                break;
            case "server":
                if(args.length==1)
                    (new Server(43210)).run();
                else
                {
                    try {
                        int portNumber = Integer.parseInt(args[1]);
                        (new Server(portNumber)).run();
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Invalid parameter specified");
                    }
                }
                break;
            default:
                System.out.println("Invalid parameter specified");
        }
    }
}
