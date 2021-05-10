package it.polimi.ingsw.Client;


import it.polimi.ingsw.Client.ModelSimplified.GameSimplified;
import it.polimi.ingsw.Client.ModelSimplified.PlayerSimplified;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.*;
import it.polimi.ingsw.Server.Model.LeaderCard;


import java.io.*;
import java.net.Socket;
import java.util.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

enum Phase{ Quit, MainMenu, Game, Error }

public class PhaseClient  {

    public static void main( String[] args ) throws IOException {

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
                    System.out.println("Adios!");
                    return;
            }
        }
    }

}


class Halo
{

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
}

class MenuPhase
{
    public Phase run() {
        System.out.println("Welcome user. Remember the HELP command.");
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
                case "quit":
                    return Phase.Quit;
                case "help":
                    System.out.println("\t Commands: " +
                            "\n\"help\"" +
                            "\n\"quit\" " +
                            "\n\"create <ip> <port> <nickname> <capacity>\" " +
                            "\n\"join <ip> <port> <nickname> <lobbyNumber>\""+
                            "\n\"rejoin <ip> <port> <nickname> <lobbyNumber>\"");
                    break;
                case "create":  // CREATE localhost 43210 Tommaso 4
                    if (!checkCreateCommand(textList)) break;
                    try {
                        openStreams(textList);

                        MSG_CREATE_LOBBY m = new MSG_CREATE_LOBBY(Integer.parseInt(textList.get(4)), textList.get(3));
                        Halo.objectOutputStream.writeObject(m);
                        message = (Message) Halo.objectInputStream.readObject();

                        if (message.getMessageType() == MessageType.MSG_OK_CREATE) {
                            MSG_OK_CREATE msg = (MSG_OK_CREATE) message;
                            System.out.println("Your lobby number is " + msg.getLobbyNumber());
                            Halo.myNickname = textList.get(3);
                            if (Integer.parseInt(textList.get(4))==1)
                                Halo.solo = true;
                            else
                                Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            closeStreams(message);
                            return Phase.Error;
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        return Phase.MainMenu;
                    }
                    break;
                case "join": // JOIN localhost 1337 Tommaso 256
                    if (!checkJoinCommand(textList)) break;
                    try {
                        openStreams(textList);

                        MSG_JOIN_LOBBY m = new MSG_JOIN_LOBBY(textList.get(3), Integer.parseInt(textList.get(4)));
                        Halo.objectOutputStream.writeObject(m);
                        message = (Message) Halo.objectInputStream.readObject();
                        if (message.getMessageType() == MessageType.MSG_OK_JOIN) {
                            MSG_OK_JOIN msg = (MSG_OK_JOIN) message;
                            System.out.println("Your assigned nickname is " + msg.getAssignedNickname());
                            Halo.myNickname = msg.getAssignedNickname();
                            Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            closeStreams(message);
                            return Phase.Error;
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        return Phase.MainMenu;
                    }
                    break;
                case "rejoin":
                    if(!checkJoinCommand(textList)) break;
                    try
                    {
                        openStreams(textList);

                        MSG_REJOIN_LOBBY m = new MSG_REJOIN_LOBBY(textList.get(3), Integer.parseInt(textList.get(4)));
                        Halo.objectOutputStream.writeObject(m);
                        message = (Message) Halo.objectInputStream.readObject();
                        if (message.getMessageType() == MessageType.MSG_OK_REJOIN) {
                            MSG_OK_JOIN msg = (MSG_OK_JOIN) message;
                            System.out.println("Your assigned nickname is " + msg.getAssignedNickname());
                            Halo.myNickname = msg.getAssignedNickname();
                            Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            closeStreams(message);
                            return Phase.Error;
                        }
                    } catch(IOException | ClassNotFoundException e)
                    {
                        e.printStackTrace();
                        return Phase.MainMenu;
                    }

                case "halo": new Thread(this::EE).start();
                break;
                default:
                    System.out.println("Sorry, we didn't catch that");
            }
        }
    }

    private void openStreams(List<String> textList) throws IOException {
        Halo.socket = new Socket(textList.get(1), Integer.parseInt(textList.get(2)));

        Halo.outputStream = Halo.socket.getOutputStream();
        Halo.objectOutputStream = new ObjectOutputStream(Halo.outputStream);
        Halo.inputStream = Halo.socket.getInputStream();
        Halo.objectInputStream = new ObjectInputStream(Halo.inputStream);
    }
    private void closeStreams(Message message) throws IOException {
        MSG_ERROR msg = (MSG_ERROR) message;
        System.out.println(msg.getErrorMessage());
        Halo.socket.close();
        Halo.outputStream.close();
        Halo.inputStream.close();
        Halo.objectOutputStream.close();
        Halo.objectInputStream.close();
    }
    private boolean checkCreateCommand(List<String> textList) {
        if (textList.size() != 5) {
            System.out.println("Errore! mancano dei parametri oppure ce ne sono troppi!");
            return false;
        }
        if (Integer.parseInt(textList.get(2)) >= 65536) {
            System.out.println("Errore! il numero di porta è troppo potente!!");
            return false;
        }
        if (Integer.parseInt(textList.get(2)) <= 1023) {
            System.out.println("Errore! il numero di porta dev'essere maggiore di 1023!!");
            return false;
        }
        if (Integer.parseInt(textList.get(4)) > 4) {
            System.out.println("Errore! Il numero di giocatori deve essere minore di 5!!");
            return false;
        }
        if (Integer.parseInt(textList.get(4)) < 1)
        {
            System.out.println("Errore! il numero di giocatori dev'essere maggiore di 1!");
            return false;
        }
        return true;
    }

    private boolean checkJoinCommand(List<String> textList) {
        if (textList.size() != 5) {
            System.out.println("Error! The number of parameters is incorrect!");
            return false;
        }
        if (Integer.parseInt(textList.get(2)) >= 65536) {
            System.out.println("Error! The port number is way too high!");
            return false;
        }
        if (Integer.parseInt(textList.get(2)) <= 1023) {
            System.out.println("Error! Your port number must be greater than 1023!");
            return false;
        }
        if (Integer.parseInt(textList.get(4)) <= -1 || Integer.parseInt(textList.get(4)) >=500) {
            System.out.println("Error! Lobby number must be hamburgered between 0 and 500!");
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

        System.out.println(" Waiting for Initial update model ");
        try
        {
            message = (Message) Halo.objectInputStream.readObject();
            if(message.getMessageType()!=MessageType.MSG_UPD_Full) {
                return Phase.Error;
            }
            MSG_UPD_Full msg = (MSG_UPD_Full) message;

            Halo.game = new GameSimplified();
            Halo.game.updateAll(msg);
            System.out.println(" Model received ");

            Halo.myPlayerNumber = Halo.game.getMyPlayerNumber(Halo.myNickname);
            if ( Halo.myPlayerNumber == 0) return Phase.Error;
            Halo.myPlayerRef = Halo.game.getPlayerRef(Halo.myPlayerNumber);


            System.out.println("Remember the HELP command to show a list of commands.");
            List<String> textList = new ArrayList<>();
            String text;

            while(true)
            {
                System.out.print("> ");
                text = Halo.input.nextLine();

                textList.clear();
                textList = new ArrayList<>( (Arrays.asList(text.split("\\s+"))));

                switch(textList.get(0).toLowerCase())
                {
                    case "quit":
                        return Phase.Quit;
                    case "help":
                        System.out.println(" List of commands! ");
                        System.out.println("1. show <something>              : something can be");
                        System.out.println("                                'players' ");
                        System.out.println("                                'market' ");
                        System.out.println("                                'depot' ");
                        System.out.println("                                'strongbox' ");
                        System.out.println("                                'devslot' ");
                        System.out.println("                                'devdeck' ");
                        System.out.println("                                'faithtrack' ");
                        System.out.println("                                'myvp' ");
                        System.out.println("                                'leadercards' ");
                        System.out.println("2. show player <num> <something>  : something can be");
                        System.out.println("                                'vp'");
                        System.out.println("                                'leaderCards'");
                        System.out.println("                                'depot'");
                        System.out.println("                                'strongbox'");
                        System.out.println("                                'devslot'");
                        break;
                    case "show":
                        if (!checkShowCommand(textList)) break;
                        if ( textList.size() == 2)
                        {
                            switch (textList.get(1).toLowerCase())
                            {
                                case "players" :
                                    for ( PlayerSimplified p : Halo.game.getPlayerSimplifiedList())
                                        System.out.println(" "+p.getNickname()+ " - "+p.getPlayerNumber());
                                    break;
                                case "market" :
                                    System.out.println( Halo.game.getMarket().toString());
                                    break;
                                case "depot" :
                                    System.out.println( Halo.myPlayerRef.getWarehouseDepot().toString());
                                    break;
                                case "strongbox":
                                    System.out.println( Halo.myPlayerRef.getStrongbox().toString());
                                    break;
                                case "devslot":
                                    System.out.println( Halo.myPlayerRef.getDevelopmentSlot().toString());
                                    break;
                                case "devdeck":
                                    System.out.println( Halo.game.getDevDeck().toString());
                                    break;
                                case "faithtrack":
                                    System.out.println( Halo.game.getFaithTrack().toString());
                                    break;
                                case "myvp":
                                    System.out.println( " my VP : " + Halo.myPlayerRef.getVP());
                                    break;
                                case "leadercards":
                                    LeaderCard[] cards = Halo.myPlayerRef.getLeaderCards();
                                    if( cards[0] != null) {
                                        System.out.println(" Leader Card #1: "+ cards[0]);
                                    }
                                    else
                                        System.out.println(" Leader Card #1: discarded");
                                    if( cards[1] != null) {
                                        System.out.println(" Leader Card #2: "+ cards[1]);
                                    }
                                    else
                                        System.out.println(" Leader Card #2: discarded");
                                    break;
                                default: System.out.println(" Somehow I reached this default. Wow.");

                            }
                        }
                        else //if(textList.size() == 4)
                        {
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
                                        System.out.println(" Leader Card #1: discarded");
                                    if (cards[1] != null) {
                                        if (cards[1].getEnable())
                                            System.out.println(" Leader Card #2: " + cards[1].toString());
                                        else
                                            System.out.println(" Leader Card #2: covered");
                                    } else
                                        System.out.println(" Leader Card #2: discarded");
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
                                    System.out.println(" Somehow I reached this default. Wow.");
                            }
                        }
                        break;
                    default:
                        System.out.println("Sorry, I didn't catch that");
                }
            }




            /////////////

            /*
            System.out.println(Halo.game.getMarket());
            System.out.println(Halo.game.getDevDeck());
            System.out.println(Halo.game.getFaithTrack());
            System.out.println(Halo.game.getCurrentPlayer());
            System.out.println(Halo.game.getTurn());
            for (PlayerSimplified p : Halo.game.getPlayerSimplifiedList())
                System.out.println(p.getNickname());
            */








        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return Phase.Error;
        }

        //return Phase.MainMenu;
    }

    private boolean checkShowCommand(List<String> textList) {
        if (textList.size() != 4 && textList.size() != 2) {
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
            System.out.println("Error! You can't show this.");
            return false;
        }
        // if ( textList.size()==4 )
        if ( !textList.get(1).equalsIgnoreCase("player"))
        {
            System.out.println("Sorry, the second word is incomprehensible.");
            return false;
        }
        //else
        try {
            if (Integer.parseInt(textList.get(2)) < 1)
            {
                System.out.println("Sorry, the player number is below the minimum.");
                return false;
            }
            if(Integer.parseInt(textList.get(2)) > Halo.game.getPlayerSimplifiedList().size())
            {
                System.out.println("Sorry, the player number is above the maximum.");
                return false;
            }

            if( textList.get(3).equalsIgnoreCase("vp")) return true;
            if( textList.get(3).equalsIgnoreCase("leadercards")) return true;
            if( textList.get(3).equalsIgnoreCase("depot")) return true;
            if( textList.get(3).equalsIgnoreCase("strongbox")) return true;
            if( textList.get(3).equalsIgnoreCase("devslot")) return true;
            System.out.println("Error! You can't show this.");
            return false;
        }
        catch ( NumberFormatException e )
        {
            System.out.println("Sorry, the player number is not a number.");
            return false;
        }
    }

}

class ErrorPhase
{
    public Phase run() throws IOException {
        System.out.println(" There was an accident. You will be returned to Main Menu");

        Halo.socket.close();
        Halo.outputStream.close();
        Halo.inputStream.close();
        Halo.objectOutputStream.close();
        Halo.objectInputStream.close();

        return Phase.MainMenu;
    }
}
/*
class UpdateHandler implements Runnable{

    private final Socket clientSocket;
    private final ObjectInputStream objectInputStream;
    private final GameSimplified game;

    public UpdateHandler(Socket clientSocket, ObjectInputStream objectInputStream, GameSimplified game){
        this.clientSocket = clientSocket;
        this.objectInputStream = objectInputStream;
        this.game = game;
    }

    @Override
    public void run() {
        Message message;
        while(true){
            try {
                message = (Message) objectInputStream.readObject();
                switch (message.getMessageType()){
                    case MSG_UPD_Full:
                        synchronized (game){ game.updateAll((MSG_UPD_Full) message);}
                    case MSG_UPD_Game:
                        synchronized (game){game.updateGame((MSG_UPD_Game) message);}
                    case MSG_UPD_Market:
                        synchronized (game){game.updateMarket((MSG_UPD_Market) message);}
                    case MSG_UPD_DevDeck:
                        synchronized (game){game.updateDevelopmentCardsDeck((MSG_UPD_DevDeck) message);}
                    case MSG_UPD_DevCardsVendor:
                        synchronized (game){game.updateDevelopmentCardsVendor((MSG_UPD_DevCardsVendor) message);}
                    case MSG_UPD_FaithTrack:
                        synchronized (game){game.updateFaithTrack((MSG_UPD_FaithTrack) message);}
                    case MSG_UPD_LeaderBoard: //who closes the connection?
                        synchronized (game){game.updateLeaderBoard((MSG_UPD_LeaderBoard) message);}
                        return;
                    case MSG_UPD_DevSlot:
                        synchronized (game){game.updateCurrentPlayer(message);}
                    case MSG_UPD_Extradepot:
                        synchronized (game){game.updateCurrentPlayer(message);}
                    case MSG_UPD_WarehouseDepot:
                        synchronized (game){game.updateCurrentPlayer(message);}
                    case MSG_UPD_Strongbox:
                        synchronized (game){game.updateCurrentPlayer(message);}
                    case MSG_UPD_Player:
                        synchronized (game){game.updateCurrentPlayer(message);}
                    case MSG_UPD_LeaderCardsObject:
                        synchronized (game){game.updateLeaderCardsObject((MSG_UPD_LeaderCardsObject) message);}
                    case MSG_UPD_ResourceObject:
                        synchronized (game){game.updateResourceObject((MSG_UPD_ResourceObject) message);}
                    case MSG_UPD_MarketHelper:
                        synchronized (game){game.updateMarketHelper((MSG_UPD_MarketHelper) message);}
                    case MSG_UPD_End:
                        synchronized (game){

                        }
                }
            }
            catch(IOException | ClassNotFoundException e){}


        }
    }
}
*/