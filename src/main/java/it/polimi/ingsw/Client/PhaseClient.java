package it.polimi.ingsw.Client;


import it.polimi.ingsw.Client.ModelSimplified.GameSimplified;
import it.polimi.ingsw.Client.ModelSimplified.PlayerSimplified;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.*;
import it.polimi.ingsw.Server.Model.DevelopmentCard;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.LeaderCard;
import it.polimi.ingsw.Server.Model.SpecialAbilities.*;


import java.io.*;
import java.net.Socket;
import java.util.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

enum Phase{ Quit, MainMenu, Game, Error }

public class PhaseClient  {

    public static void main( String[] args ) {

        Phase phase = Phase.MainMenu;

        while(true)
        {
            switch(phase)
            {
                case MainMenu:
                    phase = (new MenuPhase()).run();
                    break;
                case Game:
                    phase = (new GamePhase()).run();
                    break;
                case Error:
                    phase = (new ErrorPhase()).run();
                    break;
                case Quit:
                    (new ClosingPhase()).run();
                    return;
            }
        }
    }
}

class Halo
{
    static String defaultAddress = "localhost";
    static int defaultPort = 43210;

    static Socket socket;
    static OutputStream outputStream;
    static ObjectOutputStream objectOutputStream;
    static InputStream inputStream;
    static ObjectInputStream objectInputStream;
    static Scanner input = new Scanner(System.in);
    static GameSimplified game;
    static int myPlayerNumber;
    static PlayerSimplified myPlayerRef;
    static String myNickname;

    static boolean solo;
    static boolean yourTurn;
    static boolean action = false;
}

class ClosingPhase
{
    public void run()
    {
        try {
            Halo.socket.close();
            Halo.outputStream.close();
            Halo.inputStream.close();
            Halo.objectOutputStream.close();
            Halo.objectInputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class MenuPhase
{
    public Phase run() {
        System.out.println(" >> Welcome user.");
        System.out.println(" >> Remember the HELP command.");
        List<String> textList = new ArrayList<>();
        String text;
        Message message;

        while(true)
        {
            System.out.print("> ");
            text = Halo.input.nextLine();

            textList.clear();
            textList = new ArrayList<>( (Arrays.asList(text.split("\\s+"))));

            switch(textList.get(0).toLowerCase())
            {
                case "set":
                    if (textList.size()!=3 ) break;
                    if (textList.get(1).equalsIgnoreCase("ip"))
                        Halo.defaultAddress = textList.get(2);
                    if(textList.get(1).equalsIgnoreCase("port"))
                        Halo.defaultPort = Integer.parseInt(textList.get(3));
                    break;
                case "c": //c tom 3
                    try {
                        openStreams(Halo.defaultAddress, Halo.defaultPort);

                        int numberOfPlayers = Integer.parseInt(textList.get(2));
                        String nickname = textList.get(1);
                        MSG_CREATE_LOBBY m = new MSG_CREATE_LOBBY(numberOfPlayers, nickname);
                        Halo.objectOutputStream.writeObject(m);
                        message = (Message) Halo.objectInputStream.readObject();

                        if (message.getMessageType() == MessageType.MSG_OK_CREATE) {
                            MSG_OK_CREATE msg = (MSG_OK_CREATE) message;
                            System.out.println(" >> Connected! ");
                            System.out.println(" >> Your lobby number is "+ msg.getLobbyNumber());
                            Halo.myNickname = textList.get(1);
                            if (Integer.parseInt(textList.get(2))==1)
                                Halo.solo = true;
                            else
                                Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            MSG_ERROR msg = (MSG_ERROR) message;
                            System.out.println(msg.getErrorMessage());
                            closeStreams();
                            return Phase.Error;
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        return Phase.MainMenu;
                    }
                    break;
                case "j": //j tom 343
                    try {
                        openStreams(Halo.defaultAddress, Halo.defaultPort);

                        int lobbyNumber = Integer.parseInt(textList.get(2));
                        String nickname = textList.get(1);
                        MSG_JOIN_LOBBY m = new MSG_JOIN_LOBBY(nickname, lobbyNumber);
                        Halo.objectOutputStream.writeObject(m);

                        message = (Message) Halo.objectInputStream.readObject();

                        if (message.getMessageType() == MessageType.MSG_OK_JOIN) {
                            MSG_OK_JOIN msg = (MSG_OK_JOIN) message;
                            System.out.println(" >> Connected! ");
                            System.out.println(" >> Your assigned nickname is "+ msg.getAssignedNickname());
                            Halo.myNickname = msg.getAssignedNickname();
                            Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            MSG_ERROR msg = (MSG_ERROR) message;
                            System.out.println(msg.getErrorMessage());
                            closeStreams();
                            return Phase.Error;
                        }
                    } catch (IOException | ClassNotFoundException | NumberFormatException e) {
                        e.printStackTrace();
                        return Phase.MainMenu;
                    }
                    break;
                case "quit":
                    return Phase.Quit;
                case "help":
                    System.out.println(" >> List of commands! \n \n");
                    System.out.println("=> quit                            : kills the thread and exits the program");
                    System.out.println("=> help                            : displays the possible terminal commands");
                    System.out.println("=> set <something> <value>         : sets a new default port or address");
                    System.out.println("                                     default values: "+Halo.defaultAddress + ":"+Halo.defaultPort);
                    System.out.println("something :>  'port'");
                    System.out.println("          :>  'address' ");
                    System.out.println("=> c <nickname> <capacity>         : quickly creates a new lobby using default values");
                    System.out.println("=> j <nickname> <lobbyNumber>      : quickly joins a lobby using default values");
                    System.out.println("=> create <ip> <port> <nickname> <capacity>  ");
                    System.out.println("                                   : creates a new lobby using custom port number \n" +
                                       "                                     and address values");
                    System.out.println("=> join <ip> <port> <nickname> <lobbyNumber>  ");
                    System.out.println("                                   : joins an existing lobby using custom port number \n" +
                                       "                                     and address values");
                    System.out.println("=> rejoin <ip> <port> <nickname> <lobbyNumber>  ");
                    System.out.println("                                   : gives the ability to reconnect to a lobby,  \n" +
                                       "                                     if connection was lost. Nickname must match \n" +
                                       "                                     the previously used one. ");
                    break;
                case "create":  // CREATE localhost 43210 Tommaso 4
                    if (!checkCreateCommand(textList)) break;
                    try {
                        String customAddress = textList.get(1);
                        int customPort = Integer.parseInt(textList.get(2));
                        openStreams(customAddress, customPort);

                        int numberOfPlayers = Integer.parseInt(textList.get(4));
                        String nickname = textList.get(3);
                        MSG_CREATE_LOBBY m = new MSG_CREATE_LOBBY(numberOfPlayers, nickname);
                        Halo.objectOutputStream.writeObject(m);

                        message = (Message) Halo.objectInputStream.readObject();

                        if (message.getMessageType() == MessageType.MSG_OK_CREATE) {
                            MSG_OK_CREATE msg = (MSG_OK_CREATE) message;
                            System.out.println(" >> Connected! ");
                            System.out.println(" >> Your lobby number is "+ msg.getLobbyNumber());
                            Halo.myNickname = textList.get(3);
                            if (Integer.parseInt(textList.get(4))==1)
                                Halo.solo = true;
                            else
                                Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            MSG_ERROR msg = (MSG_ERROR) message;
                            System.out.println(msg.getErrorMessage());
                            closeStreams();
                            return Phase.Error;
                        }

                    } catch (IOException | ClassNotFoundException | NumberFormatException e) {
                        e.printStackTrace();
                        return Phase.MainMenu;
                    }
                    break;
                case "join": // JOIN localhost 1337 Tommaso 256
                    if (!checkJoinCommand(textList)) break;
                    try {
                        String customAddress = textList.get(1);
                        int customPort = Integer.parseInt(textList.get(2));
                        openStreams(customAddress, customPort);

                        int lobbyNumber = Integer.parseInt(textList.get(4));
                        String nickname = textList.get(3);
                        MSG_JOIN_LOBBY m = new MSG_JOIN_LOBBY(nickname, lobbyNumber);
                        Halo.objectOutputStream.writeObject(m);
                        message = (Message) Halo.objectInputStream.readObject();
                        if (message.getMessageType() == MessageType.MSG_OK_JOIN) {
                            MSG_OK_JOIN msg = (MSG_OK_JOIN) message;
                            System.out.println(" >> Connected! ");
                            System.out.println(" >> Your assigned nickname is "+ msg.getAssignedNickname());
                            Halo.myNickname = msg.getAssignedNickname();
                            Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            MSG_ERROR msg = (MSG_ERROR) message;
                            System.out.println(msg.getErrorMessage());
                            closeStreams();
                            return Phase.Error;
                        }
                    } catch (IOException | ClassNotFoundException | NumberFormatException e) {
                        e.printStackTrace();
                        return Phase.MainMenu;
                    }
                    break;
                case "rejoin":
                    if(!checkJoinCommand(textList)) break;
                    try
                    {
                        String customAddress = textList.get(1);
                        int customPort = Integer.parseInt(textList.get(2));
                        openStreams(customAddress, customPort);

                        int lobbyNumber = Integer.parseInt(textList.get(4));
                        String nickname = textList.get(3);
                        MSG_REJOIN_LOBBY m = new MSG_REJOIN_LOBBY(nickname, lobbyNumber);

                        Halo.objectOutputStream.writeObject(m);

                        message = (Message) Halo.objectInputStream.readObject();
                        if (message.getMessageType() == MessageType.MSG_OK_REJOIN) {
                            MSG_OK_JOIN msg = (MSG_OK_JOIN) message;
                            System.out.println(" >> Connected! ");
                            System.out.println(" >> Your assigned nickname is "+ msg.getAssignedNickname());
                            Halo.myNickname = msg.getAssignedNickname();
                            Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            MSG_ERROR msg = (MSG_ERROR) message;
                            System.out.println(msg.getErrorMessage());
                            closeStreams();
                            return Phase.Error;
                        }
                    } catch(IOException | ClassNotFoundException | NumberFormatException e)
                    {
                        e.printStackTrace();
                        return Phase.MainMenu;
                    }

                case "halo": new Thread(this::EE).start();
                break;
                default:
                    System.out.println(" > Sorry, we didn't catch that");
            }
        }
    }

    private void openStreams(String address, int port) throws IOException {
        Halo.socket = new Socket(address, port);

        Halo.outputStream = Halo.socket.getOutputStream();
        Halo.objectOutputStream = new ObjectOutputStream(Halo.outputStream);
        Halo.inputStream = Halo.socket.getInputStream();
        Halo.objectInputStream = new ObjectInputStream(Halo.inputStream);
    }

    private void closeStreams() throws IOException {
        Halo.socket.close();
        Halo.outputStream.close();
        Halo.inputStream.close();
        Halo.objectOutputStream.close();
        Halo.objectInputStream.close();
    }
    private boolean checkCreateCommand(List<String> textList) {
        if (textList.size() != 5) {
            System.out.println(" > Error! The number of parameters is incorrect!");
            return false;
        }
        try {
            if (Integer.parseInt(textList.get(2)) >= 65536) {
                System.out.println(" > Error! The port number is way too high!");
                return false;
            }
            if (Integer.parseInt(textList.get(2)) <= 1023) {
                System.out.println(" > Error! Your port number must be greater than 1023!");
                return false;
            }
            if (Integer.parseInt(textList.get(4)) > 4) {
                System.out.println(" > Error! The number of players must be < 5");
                return false;
            }
            if (Integer.parseInt(textList.get(4)) < 1) {
                System.out.println(" > Error! There must be at least one player!");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println(" > Error! Could not parse the number!");
            return false;
        }
        return true;
    }

    private boolean checkJoinCommand(List<String> textList) {
        if (textList.size() != 5) {
            System.out.println(" > Error! The number of parameters is incorrect!");
            return false;
        }
        try {
            if (Integer.parseInt(textList.get(2)) >= 65536) {
                System.out.println(" > Error! The port number is way too high!");
                return false;
            }
            if (Integer.parseInt(textList.get(2)) <= 1023) {
                System.out.println(" > Error! Your port number must be greater than 1023!");
                return false;
            }
            if (Integer.parseInt(textList.get(4)) <= -1 || Integer.parseInt(textList.get(4)) >= 500) {
                System.out.println(" > Error! Lobby number must be hamburgered between 0 and 500!");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println(" > Error! Could not parse the number!");
            return false;
        }
        return true;
    }

    private void EE() {
        List<String> list = new ArrayList<>();

        list.add("\n" );
        list.add("      ___           ___           ___       ___     " );
        list.add("     /\\__\\         /\\  \\         /\\__\\     /\\  \\    " );
        list.add("    /:/  /        /::\\  \\       /:/  /    /::\\  \\   " );
        list.add("   /:/__/        /:/\\:\\  \\     /:/  /    /:/\\:\\  \\  " );
        list.add("  /::\\  \\ ___   /::\\~\\:\\  \\   /:/  /    /:/  \\:\\  \\ " );
        list.add(" /:/\\:\\  /\\__\\ /:/\\:\\ \\:\\__\\ /:/__/    /:/__/ \\:\\__\\" );
        list.add(" \\/__\\:\\/:/  / \\/__\\:\\/:/  / \\:\\  \\    \\:\\  \\ /:/  /" );
        list.add("      \\::/  /       \\::/  /   \\:\\  \\    \\:\\  /:/  / " );
        list.add("      /:/  /        /:/  /     \\:\\  \\    \\:\\/:/  /  " );
        list.add("     /:/  /        /:/  /       \\:\\__\\    \\::/  /   " );
        list.add("     \\/__/         \\/__/         \\/__/     \\/__/ ");
        list.add(" ");
        /*
        list.add("░░░░░░░░░░░▓▓▓███████████████████████▓▓▓░░░░░░░░░░░");
        list.add("░░░░░░░░░▓▓▓░█░░░░░░░░▓░░░░░▓░░░░░░░░█░▓▓▓░░░░░░░░░");
        list.add("░░░░░░░▓▓▓░██░░░░░░░░▓░░░░░░░▓░░░░░░░░██░▓▓▓░░░░░░░");
        list.add("░░░░░░░▓░░█░░░░░░░░░▓▓░░░░░░░▓▓░░░░░░░░░█░░▓░░░░░░░");
        list.add("░░░░░░▓░░█░░░░░░░░░▓▓░░░░░░░░░▓▓░░░░░░░░░█░░▓░░░░░░");
        list.add("░░░░░▓▓▓█░░░░░░░░░▓▓░░░░░░░░░░░▓▓░░░░░░░░░█▓▓▓░░░░░");
        list.add("░░░░░▓░░█░░░░░░░░▓▓▓░░░░░░░░░░░▓▓▓░░░░░░░░█░░▓░░░░░");
        list.add("░░░░░▓░█████████████████████████████████████░▓░░░░░");
        list.add("░░░░▓░██░█░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█░██░▓░░░░");
        list.add("░░░░▓░░█░█░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█░█░░▓░░░░");
        list.add("░░░░▓░░█░█░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█░█░░▓░░░░");
        list.add("░░░░▓█░█▓░█░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█░▓█░█▓░░░░");
        list.add("░░░▓░██░░░█░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█░░░██░▓░░░");
        list.add("░░░▓█░█░░░░█░░░░░░░░░░░░░░░░░░░░░░░░░░░█░░░░█░█▓░░░");
        list.add("░░░▓█░█░░░░░█░░░░░░░░░░░░░░░░░░░░░░░░░█░░░░░█░█▓░░░");
        list.add("░░░░▓█░▓▓░░░░█░░░░░░░░▓▓▓▓▓▓▓░░░░░░░░█░░░░▓▓░█▓░░░░");
        list.add("░░░░░░█░░▓░░░░▓█████████████████████▓░░░░▓░░█░░░░░░");
        list.add("░░░░░░░█▓▓▓░░░░░░░░█░░▓▓▓▓▓▓▓░░█░░░░░░░░▓▓▓█░░░░░░░");
        list.add("░░░░░░░░░█▓▓▓░░░░░░▓█░░░░░░░░░█▓░░░░░░▓▓▓█░░░░░░░░░");
        list.add("░░░░░░░░░░░█▓▓░░▓▓▓░█░░░░░░░░░█░▓▓▓░░▓▓█░░░░░░░░░░░");
        list.add("░░░░░░░░░░░░░█▓▓░░░░█░░░░░░░░░█░░░░▓▓█░░░░░░░░░░░░░");
        list.add("░░░░░░░░░░░░░░░█░░░░█░░░░░░░░░█░░░░█░░░░░░░░░░░░░░░");
        list.add("░░░░░░░░░░░░░░░░██████▓▓▓▓▓▓▓██████░░░░░░░░░░░░░░░░");
        */


        for( String s : list)
        {
            try
            {
                Thread.sleep(333);
                System.out.println(s);
            }
            catch ( InterruptedException ignored)
            { }
        }
    }
}

class GamePhase
{

    public Phase run()
    {
        Message message;
        boolean execute = false;

        System.out.println("\u001B[35m >> Waiting for Initial update model.");
        System.out.println(" >> Console is unresponsive.");
        try
        {
            message = (Message) Halo.objectInputStream.readObject();
            if(message.getMessageType()!=MessageType.MSG_UPD_Full) {
                return Phase.Error;
            }
            MSG_UPD_Full msg = (MSG_UPD_Full) message;

            Halo.game = new GameSimplified();
            Halo.game.updateAll(msg);
            System.out.println(" >> Model received.");
            System.out.println(" >> Console is responsive.");

            Halo.myPlayerNumber = Halo.game.getMyPlayerNumber(Halo.myNickname);
            if ( Halo.myPlayerNumber == 0) return Phase.Error;
            Halo.myPlayerRef = Halo.game.getPlayerRef(Halo.myPlayerNumber);

            System.out.println(" >> Remember the HELP command to show a list of commands.");
            List<String> textList = new ArrayList<>();
            String text;

            new Thread(new UpdateHandler()).start();

            while(true)
            {
                execute=true;
//phase 2: gets input
                text = Halo.input.nextLine();

                textList.clear();
                textList = new ArrayList<>( (Arrays.asList(text.split("\\s+"))));

//phase 3: input gets converted into specific action??????????????
                // must correct synchronization
                if(Halo.yourTurn) {
                    if (Halo.game.isMiddleActive()) {
                        if (Halo.game.isLeaderCardsObjectEnabled()) {
                            int first = 0;
                            int second = 0;
                            obtainNumberLoop: while (true) {
                                if (check1_4Number(textList)) {
                                    first = Integer.parseInt(textList.get(0));

                                    while (true) {
                                        System.out.println(" > Please pick the second card of your wish:");
                                        text = Halo.input.nextLine();
                                        textList.clear();
                                        textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                                        if (checkLeaderCardObjectNumber(textList, first)) {
                                            second = Integer.parseInt(textList.get(0));
                                            System.out.println(" > Thanks, you choose " + first + " and " + second);
                                            break obtainNumberLoop;
                                        }
                                    }


                                } else {
                                    System.out.println(" > Please pick the first card of your wish:");
                                    text = Halo.input.nextLine();
                                    textList.clear();
                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                }
                            }

                            ArrayList<LeaderCard> list = new ArrayList<>();
                            list.add(Halo.game.getLeaderCardsObject().getCard(first));
                            list.add(Halo.game.getLeaderCardsObject().getCard(second));
                            MSG_INIT_CHOOSE_LEADERCARDS msgToSend = new MSG_INIT_CHOOSE_LEADERCARDS(list);
                            Halo.objectOutputStream.writeObject(msgToSend);
                        }
                        else if (Halo.game.isResourceObjectEnabled()) {
                            Resource resource = Resource.NONE;
                            while(true)
                            {
                                if (check1_4Number(textList))
                                {
                                    int number = Integer.parseInt(textList.get(0));
                                    switch (number)
                                    {
                                        case 1: resource = Resource.SHIELD;
                                        break;
                                        case 2: resource = Resource.COIN;
                                        break;
                                        case 3: resource = Resource.SERVANT;
                                        break;
                                        case 4: resource = Resource.STONE;
                                        break;
                                    }
                                    break;
                                }
                                else
                                {
                                    System.out.println(" > Please choose a number between 1 and 4: ");
                                    text = Halo.input.nextLine();
                                    textList.clear();
                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                }
                            }

                            MSG_INIT_CHOOSE_RESOURCE msgToSend = new MSG_INIT_CHOOSE_RESOURCE( resource );
                            Halo.objectOutputStream.writeObject(msgToSend);
                        }
                        else if (Halo.game.isMarketHelperEnabled()) {
                            int choice = 0;

                            while(true)
                            {
                                if(checkChoice(textList))
                                {
                                    choice = Integer.parseInt(textList.get(0));
                                    break;
                                }
                                else {
                                    System.out.println(" > Please just insert a number! ");
                                    text = Halo.input.nextLine();
                                    textList.clear();
                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                }
                            }

                            MSG_ACTION_MARKET_CHOICE msgToSend = new MSG_ACTION_MARKET_CHOICE(choice);
                            Halo.objectOutputStream.writeObject(msgToSend);
                        }
                        else if (Halo.game.isDevelopmentCardsVendorEnabled()) {
                            int cardNum;
                            int slotNum;
                            while(true) {
                                if(checkNumbers(textList)) {
                                    cardNum = Integer.parseInt(textList.get(0));
                                    slotNum = Integer.parseInt(textList.get(1));
                                    break;
                                } else {
                                    System.out.println(" > Please write the card you want to buy and to slot you want to put it in!  ");
                                    text = Halo.input.nextLine();
                                    textList.clear();
                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                }
                            }
                            MSG_ACTION_CHOOSE_DEVELOPMENT_CARD msgToSend = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(cardNum, slotNum);
                            Halo.objectOutputStream.writeObject(msgToSend);
                            (new UpdateHandler(true)).run();
                        }
                        else if (Halo.game.isLeaderBoardEnabled())
                        {
                            // something
                            return Phase.MainMenu;
                        }
                        execute = false;
                    }
                }

                if(execute) {
                    switch (textList.get(0).toLowerCase()) {
                        case "quit":
                            return Phase.Quit; //testing purposes
                        case "help": {
                            System.out.println("  List of commands! \n \n");
                            System.out.println("=> quit                            : kills the thread and exits the program");
                            System.out.println("=> help                            : displays the possible terminal commands");
                            System.out.println("=> show <something>                : shows one of my Assets");
                            System.out.println("=> action                          : enables the action routine, if possible");
                            System.out.println("something :>  'leaderCards'");
                            System.out.println("          :>  'players' ");
                            System.out.println("          :>  'market' ");
                            System.out.println("          :>  'depot' ");
                            System.out.println("          :>  'strongbox' ");
                            System.out.println("          :>  'devslot' ");
                            System.out.println("          :>  'devdeck' ");
                            System.out.println("          :>  'faithtrack' ");
                            System.out.println("          :>  'myvp' ");
                            System.out.println("=> show <nickname> <something>     : shows one of the other players' assets.\n" +
                                    "                                     Specify the nickname of the player.        ");
                            System.out.println("=> show player <num> <something>   : shows one of the other players' assets.\n" +
                                    "                                     Specify the player's number. ");
                            System.out.println("num       :>  1, .. , maxLobbySize ");
                            System.out.println("something :>  'leaderCards'");
                            System.out.println("          :>  'vp'");
                            System.out.println("          :>  'depot'");
                            System.out.println("          :>  'strongbox'");
                            System.out.println("          :>  'devslot'");
                        }
                        break;
                        case "show": {
                            if (!checkShowCommand(textList)) break;
                            synchronized (Halo.game) {
                                if (textList.size() == 2) // show depot
                                {
                                    switch (textList.get(1).toLowerCase()) {
                                        case "players":
                                            System.out.println(" > showing players  name - number");
                                            for (PlayerSimplified p : Halo.game.getPlayerSimplifiedList())
                                                System.out.println("  " + p.getNickname() + " - " + p.getPlayerNumber());
                                            break;
                                        case "market":
                                            System.out.println(Halo.game.getMarket().toString());
                                            break;
                                        case "depot":
                                            System.out.println(Halo.myPlayerRef.getWarehouseDepot().toString());
                                            break;
                                        case "strongbox":
                                            System.out.println(Halo.myPlayerRef.getStrongbox().toString());
                                            break;
                                        case "devslot":
                                            System.out.println(Halo.myPlayerRef.getDevelopmentSlot().toString());
                                            break;
                                        case "devdeck":
                                            System.out.println(Halo.game.getDevDeck().toString());
                                            break;
                                        case "faithtrack":
                                            System.out.println(Halo.game.getFaithTrack().toString());
                                            break;
                                        case "myvp":
                                            System.out.println(" my VP : " + Halo.myPlayerRef.getVP());
                                            break;
                                        case "leadercards":
                                            LeaderCard[] cards = Halo.myPlayerRef.getLeaderCards();
                                            if (cards[0] != null) {
                                                System.out.println(" Leader Card #1: " + cards[0]);
                                            } else
                                                System.out.println(" Leader Card #1: none");
                                            if (cards[1] != null) {
                                                System.out.println(" Leader Card #2: " + cards[1]);
                                            } else
                                                System.out.println(" Leader Card #2: none");
                                            break;
                                        default:
                                            System.out.println(" Somehow I reached this default. Wow.");

                                    }
                                } else if (textList.size() == 4) {
                                    PlayerSimplified player = Halo.game.getPlayerRef(Integer.parseInt(textList.get(2)));
                                    switch (textList.get(3).toLowerCase()) {
                                        case "vp":
                                            System.out.println(" his/her VP : " + player.getVP());
                                            break;
                                        case "leadercards":
                                            LeaderCard[] cards = player.getLeaderCards();
                                            if (cards[0] != null) {
                                                if (cards[0].getEnable())
                                                    System.out.println(" Leader Card #1: " + cards[0].toString());
                                                else
                                                    System.out.println(" Leader Card #1: covered");
                                            } else
                                                System.out.println(" Leader Card #1: none");
                                            if (cards[1] != null) {
                                                if (cards[1].getEnable())
                                                    System.out.println(" Leader Card #2: " + cards[1].toString());
                                                else
                                                    System.out.println(" Leader Card #2: covered");
                                            } else
                                                System.out.println(" Leader Card #2: none");
                                            break;
                                        case "depot":
                                            System.out.println(player.getWarehouseDepot());
                                            break;
                                        case "strongbox":
                                            System.out.println(player.getStrongbox());
                                            break;
                                        case "devslot":
                                            System.out.println(player.getDevelopmentSlot());
                                            break;
                                        default:
                                            System.out.println(" Somehow I reached this default. Check sequence.");
                                    }
                                } else //if(size==3)
                                {
                                    PlayerSimplified player = Halo.game.getPlayerRef(textList.get(1));
                                    if (player == null) {
                                        System.out.println(" There's no such player with that name. ");
                                        break;
                                    }
                                    switch (textList.get(2).toLowerCase()) {
                                        case "vp":
                                            System.out.println(" his/her VP : " + player.getVP());
                                            break;
                                        case "leadercards":
                                            LeaderCard[] cards = player.getLeaderCards();
                                            if (cards[0] != null) {
                                                if (cards[0].getEnable())
                                                    System.out.println(" Leader Card #1: " + cards[0].toString());
                                                else
                                                    System.out.println(" Leader Card #1: covered");
                                            } else
                                                System.out.println(" Leader Card #1: none");
                                            if (cards[1] != null) {
                                                if (cards[1].getEnable())
                                                    System.out.println(" Leader Card #2: " + cards[1].toString());
                                                else
                                                    System.out.println(" Leader Card #2: covered");
                                            } else
                                                System.out.println(" Leader Card #2: none");
                                            break;
                                        case "depot":
                                            System.out.println(player.getWarehouseDepot());
                                            break;
                                        case "strongbox":
                                            System.out.println(player.getStrongbox());
                                            break;
                                        case "devslot":
                                            System.out.println(player.getDevelopmentSlot());
                                            break;
                                        default:
                                            System.out.println(" Somehow I reached this default. Check sequence.");
                                    }
                                }
                            }
                        }
                        break;
                        case "action":
                            if (Halo.yourTurn) {
                                printActions();
                                loop: while(true)
                                {
                                    System.out.println("Please pick a number: ");
                                    System.out.print("> ");
                                    text = Halo.input.nextLine();
                                    textList.clear();
                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                                    if(checkAction(textList))
                                    {
                                        int choice = Integer.parseInt(textList.get(0));
                                        switch (choice)
                                        {
                                            case 1:
                                                int cardToActivate = 0;
                                                LeaderCard l1a = Halo.myPlayerRef.getLeaderCards()[0];
                                                LeaderCard l2a = Halo.myPlayerRef.getLeaderCards()[1];

                                                if(l1a!=null) System.out.println(" 1 to enable the first card ");
                                                if(l2a!=null) System.out.println(" 2 to enable the second card ");

                                                while (true)
                                                {
                                                    System.out.print(" card number: ");
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                                                    if(checkLeaderCardsNumber(textList))
                                                    {
                                                        cardToActivate = Integer.parseInt(textList.get(0));
                                                        break;
                                                    }
                                                }

                                                MSG_ACTION_ACTIVATE_LEADERCARD msgToSend1 = new MSG_ACTION_ACTIVATE_LEADERCARD(cardToActivate);
                                                Halo.objectOutputStream.writeObject(msgToSend1);
                                                break loop;

                                            case 2:
                                                int cardToDiscard = 0;
                                                LeaderCard l1d = Halo.myPlayerRef.getLeaderCards()[0];
                                                LeaderCard l2d = Halo.myPlayerRef.getLeaderCards()[1];

                                                if(l1d!=null) System.out.println(" 1 to disable the first card ");
                                                if(l2d!=null) System.out.println(" 2 to disable the second card ");

                                                while (true)
                                                {
                                                    System.out.print(" card number: ");
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                                                    if(checkLeaderCardsNumber(textList))
                                                    {
                                                        cardToDiscard = Integer.parseInt(textList.get(0)) -1;
                                                        break;
                                                    }
                                                }

                                                MSG_ACTION_DISCARD_LEADERCARD msgToSend2 = new MSG_ACTION_DISCARD_LEADERCARD(cardToDiscard);
                                                Halo.objectOutputStream.writeObject(msgToSend2);
                                                break loop;

                                            case 3:
                                                //production
                                                break loop;

                                            case 4:
                                                //change depot
                                                System.out.println(" >> My friend, please give me the new configuration of your Warehouse Depot.");
                                                Resource shelf1;
                                                Resource[] shelf2 = new Resource[2];
                                                Resource[] shelf3 = new Resource[3];
                                                while(true) {
                                                    System.out.print(" Shelf 1: ");
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                    if (!checkResourceDepot(textList, 1)) System.out.println(" > Ohoh my friend, that's not possible in shelf 1. Try again.");
                                                    else{
                                                        shelf1 = convertStringToResource(textList.get(0));
                                                        break;
                                                    }
                                                }

                                                while(true){
                                                    System.out.print(" Shelf 2: ");
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                    if(!checkResourceDepot(textList, 2)) System.out.println(" > Ohoh my friend, that's not possible in shelf 2. Try again.");
                                                    else {
                                                        shelf2[0] = convertStringToResource(textList.get(0));
                                                        shelf2[1] = convertStringToResource(textList.get(1));
                                                        break;
                                                    }
                                                }

                                                while(true){
                                                    System.out.print(" Shelf 3: ");
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                    if(!checkResourceDepot(textList, 3)) System.out.println(" > Ohoh my friend, that's not possible in shelf 3. Try again.");
                                                    else {
                                                        shelf3[0] = convertStringToResource(textList.get(0));
                                                        shelf3[1] = convertStringToResource(textList.get(1));
                                                        shelf3[2] = convertStringToResource(textList.get(2));
                                                        break;
                                                    }
                                                }

                                                int firstExtra = -1;
                                                int secondExtra = -1;

                                                if(Halo.myPlayerRef.getLeaderCards()[0]!=null) {
                                                    if (Halo.myPlayerRef.getLeaderCards()[0].getSpecialAbility().isExtraDepot()) {
                                                        if (Halo.myPlayerRef.getLeaderCards()[0].getEnable()) {
                                                            System.out.println(" > I noticed you have an extra depot for the resource " + ((ExtraDepot) Halo.myPlayerRef.getLeaderCards()[0].getSpecialAbility()).getResourceType() + ". Please tell me how much I have to fill it");

                                                            while (true) {
                                                                System.out.print(" Number of Resources: ");
                                                                text = Halo.input.nextLine();
                                                                textList.clear();
                                                                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                                int num = Integer.parseInt(textList.get(0));
                                                                if (num < 0 || num > 2)
                                                                    System.out.println(" > That's not correct for an extraDepot. Try again.");
                                                                else {
                                                                    firstExtra = num;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if(Halo.myPlayerRef.getLeaderCards()[1]!=null) {
                                                    if (Halo.myPlayerRef.getLeaderCards()[1].getSpecialAbility().isExtraDepot()) {
                                                        if (Halo.myPlayerRef.getLeaderCards()[1].getEnable()) {
                                                            System.out.println(" > I noticed you have an extra depot for the resource " + ((ExtraDepot) Halo.myPlayerRef.getLeaderCards()[1].getSpecialAbility()).getResourceType() + ". Please tell me how much I have to fill it");

                                                            while (true) {
                                                                System.out.print(" Number of Resources: ");
                                                                text = Halo.input.nextLine();
                                                                textList.clear();
                                                                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                                int num = Integer.parseInt(textList.get(0));
                                                                if (num < 0 || num > 2)
                                                                    System.out.println(" > That's not correct for an extraDepot. Try again.");
                                                                else {
                                                                    secondExtra = num;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                MSG_ACTION_CHANGE_DEPOT_CONFIG msgToSend4 = new MSG_ACTION_CHANGE_DEPOT_CONFIG(shelf1, shelf2, shelf3, firstExtra, secondExtra);
                                                break loop;

                                            case 5:
                                                MSG_ACTION_BUY_DEVELOPMENT_CARD msgToSend5 = new MSG_ACTION_BUY_DEVELOPMENT_CARD();
                                                Halo.objectOutputStream.writeObject(msgToSend5);
                                                break loop;
                                            case 6:
                                                int row;
                                                int col;
                                                System.out.println(" > Insert 1 for a row or 2 for a column");

                                                while (true) {
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                                                    if(checkRowOrColumn(textList)) {
                                                        //to be continued //nice tommaso
                                                        break;
                                                    }
                                                }
                                                break loop;
                                            case 7:
                                                MSG_ACTION_ENDTURN msgToSend7 = new MSG_ACTION_ENDTURN();
                                                Halo.objectOutputStream.writeObject(msgToSend7);
                                                break loop;
                                        }
                                    }
                                }

                            } else {
                                System.out.println(" >> Hey, wait for your turn!");
                            }
                            break;
                        default:
                            System.out.println(" > Sorry, I didn't catch that");

                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return Phase.Error;
        }

        //return Phase.MainMenu;
    }

    private boolean checkRowOrColumn(List<String> textList) {
        if(textList.size() == 1) {
            try {
                int RowOrCol = Integer.parseInt(textList.get(0));
                if (RowOrCol != 1 && RowOrCol != 2) {
                    System.out.println("The number must be 1 or 2!");
                    return false;
                }
            } catch(NumberFormatException e) {
                    System.out.println("It is not a number!");
                    return false;
            }
        } else {
            System.out.println("The number of parameters is different then expected.");
            return false;
        }
        return true;
    }

    private boolean checkResourceDepot(List<String> textList, int shelf) {

        if(textList.size()!=shelf) return false;

        String resource;
        for(int i = 0; i < textList.size(); i++){
            resource = textList.get(i);
            if(!(resource.equals("stone") || resource.equals("coin") || resource.equals("shield") || resource.equals("servant"))) return false;
        }

        return true;
    }

    private Resource convertStringToResource(String resource){
        if(resource.equals("servant")) return Resource.SERVANT;
        if(resource.equals("coin")) return Resource.COIN;
        if(resource.equals("shield")) return Resource.SHIELD;
        if(resource.equals("stone")) return Resource.STONE;
        return null;
    }

    private boolean checkLeaderCardsNumber(List<String> textList) {
        if(textList.size()>1)
        {
            System.out.println(" Please insert just a number");
            return false;
        }
        try
        {
            int number = Integer.parseInt(textList.get(0));
            LeaderCard l1 = Halo.myPlayerRef.getLeaderCards()[0];
            LeaderCard l2 = Halo.myPlayerRef.getLeaderCards()[1];
            if( number == 1)
            {
                if(l1==null)
                {
                    System.out.println("Sorry, but that card is discarded");
                    return false;
                }
                else if (l1.getEnable())
                {
                    System.out.println("Sorry, but that card is already activated");
                    return false;
                }
            }
            if( number == 2)
            {
                if(l2==null)
                {
                    System.out.println("Sorry, but that card is discarded");
                    return false;
                }
                else if (l2.getEnable())
                {
                    System.out.println("Sorry, but that card is already activated");
                    return false;
                }
            }
        }
        catch ( NumberFormatException e )
        {
            System.out.println("Sorry, but that was not a number");
            return false;
        }

        return true;
    }

    private boolean checkAction(List<String> textList) {
        if(textList.size()>1)
        {
            System.out.println(" Please insert just a number");
            return false;
        }
        try
        {
            int number = Integer.parseInt(textList.get(0));
            if( number < 1 || number >7)
            {
                System.out.println(" Please pick a number between 1 and 7");
                return false;
            }
//check very powerful action    already playedE
            if ( Halo.action && (number==3|| number==5|| number ==6))
            {
                System.out.println(" You already did a main move");
                return false;
            }

            LeaderCard l1 = Halo.myPlayerRef.getLeaderCards()[0];
            LeaderCard l2 = Halo.myPlayerRef.getLeaderCards()[1];
//check action 1 and 2
            if (number == 1 || number == 2)
            {
                if(l1 == null && l2 == null) {
                    System.out.println(" You can't, because the cards are absent ");
                    return false;
                }
                if(l1!=null) {
                    if(l2!=null) {
                        if (l1.getEnable() && l2.getEnable()) {
                            System.out.println(" You can't, because the cards are already activated ");
                            return false;
                        }
                    }
                    else {
                        if(l1.getEnable()) {
                            System.out.println(" You can't, because the first card is already enabled and the second one is absent ");
                            return false;
                        }
                    }
                }
                else {
                    if(l2.getEnable()) {
                        System.out.println(" You can't, the first card is absent and the second one is already enabled ");
                        return false;
                    }
                }
            }
//check activate production
            if ( number == 3)
            {
                //for giacomo (?)
            }
//check changeDepotConfig
//check buyDevelopmentCard
//getMarketResource
        }
        catch (NumberFormatException e)
        {
            System.out.println("Sorry, but that was not a number");
            return false;
        }

        return true;
    }

    private void printActions() {
        System.out.println("  List of actions! " +
                "         \n ----------------");
        System.out.println("=> 1                               : activate a leader card");
        System.out.println("=> 2                               : discard a leader card");
        System.out.println("=> 3                               : activate production");
        System.out.println("=> 4                               : change depot configuration");
        System.out.println("=> 5                               : buy development card");
        System.out.println("=> 6                               : get market resources");
        System.out.println("=> 7                               : end turn");
    }

    private boolean checkNumbers(List<String> textList) {
        if(textList.size() == 2) {
            try {
                int cardNum = Integer.parseInt(textList.get(0));
                int slotNum = Integer.parseInt(textList.get(1));
                Map<DevelopmentCard, boolean[]> cards;
                cards = Halo.game.getDevelopmentCardsVendor().getCards();

                if (cardNum > cards.size() || cardNum < 1) {
                    System.out.println("That's not a possible card!");
                    return false;
                }
                if (slotNum < 0 || slotNum > 2) {
                    System.out.println("That's not a proper slot!");
                    return false;
                }
                if (!cards.get(cardNum - 1)[slotNum - 1]) {
                    System.out.println("Error! You can't place that card in this slot");
                    return false;
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Sorry, that was not a number");
                return false;
            }
        } else {
            System.out.println("The number of parameters is different then expected.");
        }
        return true;
    }

    private boolean checkChoice(List<String> textList) {
        if(textList.size()>1)
        {
            System.out.println(" Please insert just a number");
            return false;
        }

        boolean[] choices = Halo.game.getMarketHelper().getChoices();
        try
        {
            int number = Integer.parseInt(textList.get(0));
            if ( !choices[number-1] )
            {
                System.out.println(" That wasn't a possible choice");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Sorry, but that was not a number");
            return false;
        }
        return true;
    }

    private boolean check1_4Number(List<String> textList) {
        if (textList.size()>1)
        {
            System.out.println(" Please insert just a number");
            return false;
        }
        try
        {
            int number = Integer.parseInt(textList.get(0));
            if (number < 1 || number > 4 ) {
                System.out.println("Sorry, you have to choose between 1 and 4.");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Sorry, but that was not a number");
            return false;
        }
        return true;
    }

    private boolean checkLeaderCardObjectNumber(List<String> textList, int first) {
        if (textList.size()>1)
        {
            System.out.println(" Please insert just a number");
            return false;
        }
        try
        {
            int number = Integer.parseInt(textList.get(0));
            if (number < 1 || number > 4) {
                System.out.println("Sorry, the player number must be between 1 and 4.");
                return false;
            }
            if(number == first) {
                System.out.println("Sorry, but you inserted the same number as before.");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Sorry, but that was not a number");
            return false;
        }
        return true;
    }

    private boolean checkShowCommand(List<String> textList) {
        if (textList.size() != 4 && textList.size() != 2 && textList.size() != 3) {
            System.out.println("Error! The number of parameters is incorrect");
            return false;
        }
        if( textList.size() == 2)
        {
            if( textList.get(1).equalsIgnoreCase("players")) return true;
            if( textList.get(1).equalsIgnoreCase("market")) return true;
            if( textList.get(1).equalsIgnoreCase("depot")) return true;
            if( textList.get(1).equalsIgnoreCase("strongbox")) return true;
            if( textList.get(1).equalsIgnoreCase("devslot")) return true;
            if( textList.get(1).equalsIgnoreCase("devdeck")) return true;
            if( textList.get(1).equalsIgnoreCase("faithtrack")) return true;
            if( textList.get(1).equalsIgnoreCase("myvp")) return true;
            if( textList.get(1).equalsIgnoreCase("leadercards")) return true;
            System.out.println("Error! You can't show this. I don't even know what 'this' is!");
            return false;
        }
        if(textList.size()==4) {
            if (!textList.get(1).equalsIgnoreCase("player")) {
                System.out.println("Sorry, the but the second word is not player.");
                return false;
            }
            //else
            try {
                if (Integer.parseInt(textList.get(2)) < 1) {
                    System.out.println("Sorry, the player number is below the minimum.");
                    return false;
                }
                if (Integer.parseInt(textList.get(2)) > Halo.game.getPlayerSimplifiedList().size()) {
                    System.out.println("Sorry, the player number is above the maximum.");
                    return false;
                }

                if (textList.get(3).equalsIgnoreCase("vp")) return true;
                if (textList.get(3).equalsIgnoreCase("leadercards")) return true;
                if (textList.get(3).equalsIgnoreCase("depot")) return true;
                if (textList.get(3).equalsIgnoreCase("strongbox")) return true;
                if (textList.get(3).equalsIgnoreCase("devslot")) return true;
                System.out.println("Error! You can't show this.");
                return false;
            } catch (NumberFormatException e) {
                System.out.println("Sorry, the player number is not a number.");
                return false;
            }
        }
        if (textList.size()==3 )
        {
            if (textList.get(2).equalsIgnoreCase("vp")) return true;
            if (textList.get(2).equalsIgnoreCase("leadercards")) return true;
            if (textList.get(2).equalsIgnoreCase("depot")) return true;
            if (textList.get(2).equalsIgnoreCase("strongbox")) return true;
            if (textList.get(2).equalsIgnoreCase("devslot")) return true;
            System.out.println("Error! You can't show this. I don't even know what 'this' is!");
            return false;
        }
        return true;
    }

}

class ErrorPhase
{
    public Phase run() {
        System.out.println(" There was an accident and you will be returned to Main Menu.");
        try {
            Halo.socket.close();
            Halo.outputStream.close();
            Halo.inputStream.close();
            Halo.objectOutputStream.close();
            Halo.objectInputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return Phase.MainMenu;
    }
}