package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.PhaseClient;
import it.polimi.ingsw.client.gui.MainMenu;
import it.polimi.ingsw.networking.Server;


/**
 * This is the Starting Point of the program. <p>
 * This class should be called by the Java Console, followed by a parameter.
 * Calling this class without parameters will print them.
 * Parameters can be: <ul>
 * <li> gui : which executes the GUI</li>
 * <li> cli : which executes the CLI</li>
 * <li> server : which executes the server process</li>
 * <li> server "port" : which can specify a port for the server process </li>
 */
public class StartingPoint {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Insert a parameter:");
            System.out.println("                      - gui                 : starts the graphic user interface");
            System.out.println("                      - cli                 : starts the command line interface");
            System.out.println("                      - server              : starts the server (mandatory for online games)");
            System.out.println("                      - server <port>       : set a specific port number");
            return;
        }

        switch (args[0]) {
            case "cli":
                (new PhaseClient()).run();
                break;
            case "gui":
                (new MainMenu()).run();
                break;
            case "server":
                if (args.length == 1)
                    (new Server()).run();
                else {
                    try {
                        int portNumber = Integer.parseInt(args[1]);
                        (new Server(portNumber)).run();
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid parameter specified");
                    }
                }
                break;
            default:
                System.out.println("Invalid parameter specified");
        }
    }
}
