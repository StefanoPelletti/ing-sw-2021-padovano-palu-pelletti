package it.polimi.ingsw.Client;

import it.polimi.ingsw.Networking.Message.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

enum Phase{ Quit, MainMenu, Game }

public class PhaseClient {

    public static void main( String[] args)
    {
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
    static boolean solo;
}

class MenuPhase
{
    public Phase run()
    {
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
                            Halo.solo = true;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            closeStreams(message);
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
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
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            closeStreams(message);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
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
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            closeStreams(message);
                        }
                    } catch(IOException | ClassNotFoundException e)
                    {
                        e.printStackTrace();
                    }

                case "halo": EE(); break;
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
        if (Integer.parseInt(textList.get(4)) <= -1 || Integer.parseInt(textList.get(4)) >=500) {
            System.out.println("Errore! Il numero di lobby dev'essere compreso tra 0 e 500!!");
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


        return Phase.MainMenu;
    }
}