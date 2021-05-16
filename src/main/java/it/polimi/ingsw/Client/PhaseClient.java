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

        System.out.println(" >> Welcome player.");

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
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

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

    public static void closeStreams() {
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

class MenuPhase {
    public Phase run() {
        System.out.println(" >> Main Menu.");
        System.out.println(" >> write " + Halo.ANSI_CYAN + "help" + Halo.ANSI_RESET + " for a list of commands.");
        List<String> textList = new ArrayList<>();
        String text;
        Message message;

        while (true) {
            System.out.print(" command: ");
            text = Halo.input.nextLine();

            textList.clear();
            textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

            switch (textList.get(0).toLowerCase()) {
                case "set":
                    if (textList.size() != 3) break;
                    if (textList.get(1).equalsIgnoreCase("ip")) {
                        Halo.defaultAddress = textList.get(2);
                        System.out.println(Halo.ANSI_GREEN + " > new ip: " + Halo.ANSI_RESET + Halo.defaultAddress);
                    }
                    if (textList.get(1).equalsIgnoreCase("port")) {
                        Halo.defaultPort = Integer.parseInt(textList.get(3));
                        System.out.println(Halo.ANSI_GREEN + " > new port: " + Halo.ANSI_RESET + Halo.defaultPort);
                    }

                    break;
                case "c": //c tom 3
                    try {
                        openStreams(Halo.defaultAddress, Halo.defaultPort);

                        int numberOfPlayers = Integer.parseInt(textList.get(2));
                        String nickname = textList.get(1);
                        try {
                            MSG_CREATE_LOBBY m = new MSG_CREATE_LOBBY(numberOfPlayers, nickname);
                            Halo.objectOutputStream.writeObject(m);
                        } catch (IllegalArgumentException e) {
                            System.out.println(" >> The parameters for the creation are not correct!");
                            return Phase.MainMenu;
                        }

                        message = (Message) Halo.objectInputStream.readObject();

                        if (message.getMessageType() == MessageType.MSG_OK_CREATE) {
                            MSG_OK_CREATE msg = (MSG_OK_CREATE) message;
                            System.out.println(Halo.ANSI_GREEN + " >> Connected! " + Halo.ANSI_RESET);
                            System.out.println(Halo.ANSI_GREEN + " <> Your lobby number is " + msg.getLobbyNumber() + Halo.ANSI_RESET);
                            Halo.myNickname = textList.get(1);
                            if (Integer.parseInt(textList.get(2)) == 1)
                                Halo.solo = true;
                            else
                                Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            MSG_ERROR msg = (MSG_ERROR) message;
                            System.out.println(Halo.ANSI_RED + " >> Error! " + Halo.ANSI_RESET);
                            System.out.println(Halo.ANSI_RED + " <> " + msg.getErrorMessage() + Halo.ANSI_RESET);
                            Halo.closeStreams();
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
                        try {
                            MSG_JOIN_LOBBY m = new MSG_JOIN_LOBBY(nickname, lobbyNumber);
                            Halo.objectOutputStream.writeObject(m);
                        } catch (IllegalArgumentException e) {
                            System.out.println("The parameters for the join are not correct!");
                            return Phase.MainMenu;
                        }

                        message = (Message) Halo.objectInputStream.readObject();

                        if (message.getMessageType() == MessageType.MSG_OK_JOIN) {
                            MSG_OK_JOIN msg = (MSG_OK_JOIN) message;
                            System.out.println(Halo.ANSI_GREEN + " >> Connected! " + Halo.ANSI_RESET);
                            System.out.println(Halo.ANSI_GREEN + " <> Your assigned nickname is " + msg.getAssignedNickname() + Halo.ANSI_RESET);
                            Halo.myNickname = msg.getAssignedNickname();
                            Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            MSG_ERROR msg = (MSG_ERROR) message;
                            System.out.println(Halo.ANSI_RED + " >> Error! " + Halo.ANSI_RESET);
                            System.out.println(Halo.ANSI_RED + " <> " + msg.getErrorMessage() + Halo.ANSI_RESET);
                            Halo.closeStreams();
                            return Phase.Error;
                        }
                    } catch (IOException | ClassNotFoundException | NumberFormatException e) {
                        e.printStackTrace();
                        return Phase.MainMenu;
                    }
                    break;
                case "r":
                    try {
                        openStreams(Halo.defaultAddress, Halo.defaultPort);

                        int lobbyNumber = Integer.parseInt(textList.get(2));
                        String nickname = textList.get(1);
                        try {
                            MSG_REJOIN_LOBBY m = new MSG_REJOIN_LOBBY(nickname, lobbyNumber);
                            Halo.objectOutputStream.writeObject(m);
                        } catch (IllegalArgumentException e) {
                            System.out.println("The parameters for the rejoin are not correct!");
                            return Phase.MainMenu;
                        }

                        message = (Message) Halo.objectInputStream.readObject();

                        if (message.getMessageType() == MessageType.MSG_OK_REJOIN) {
                            MSG_OK_REJOIN msg = (MSG_OK_REJOIN) message;
                            System.out.println(Halo.ANSI_GREEN + " >> RE - Connected! " + Halo.ANSI_RESET);
                            System.out.println(Halo.ANSI_GREEN + " <> Your assigned nickname is " + msg.getNickname() + Halo.ANSI_RESET);
                            Halo.myNickname = msg.getNickname();
                            Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            MSG_ERROR msg = (MSG_ERROR) message;
                            System.out.println(Halo.ANSI_RED + " >> Error! " + Halo.ANSI_RESET);
                            System.out.println(Halo.ANSI_RED + " <> " + msg.getErrorMessage() + Halo.ANSI_RESET);
                            Halo.closeStreams();
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
                    System.out.println("=> " + Halo.ANSI_CYAN + "quit" + Halo.ANSI_RESET + "                            : kills the thread and exits the program");
                    System.out.println("=> " + Halo.ANSI_CYAN + "help" + Halo.ANSI_RESET + "                            : displays the possible terminal commands");
                    System.out.println("=> " + Halo.ANSI_CYAN + "set" + Halo.ANSI_RESET + " " + Halo.ANSI_PURPLE + "<something> <value>" + Halo.ANSI_RESET + "         : sets a new default port or address");
                    System.out.println("                                     default values: " + Halo.ANSI_GREEN + Halo.defaultAddress + ":" + Halo.defaultPort + Halo.ANSI_RESET);
                    System.out.println(Halo.ANSI_PURPLE + "something" + Halo.ANSI_RESET + " :>  'port'");
                    System.out.println("          :>  'address' ");
                    System.out.println("=> " + Halo.ANSI_CYAN + "c" + Halo.ANSI_RESET + " " + Halo.ANSI_PURPLE + "<nickname> <capacity>" + Halo.ANSI_RESET + "         : quickly creates a new lobby using default values");
                    System.out.println("=> " + Halo.ANSI_CYAN + "j" + Halo.ANSI_RESET + " " + Halo.ANSI_PURPLE + "<nickname> <lobbyNumber>" + Halo.ANSI_RESET + "      : quickly joins a lobby using default values");
                    System.out.println("=> " + Halo.ANSI_CYAN + "create" + Halo.ANSI_RESET + " " + Halo.ANSI_PURPLE + "<ip> <port> <nickname> <capacity>" + Halo.ANSI_RESET + "  ");
                    System.out.println("                                   : creates a new lobby using custom port number \n" +
                            "                                     and address values");
                    System.out.println("=> " + Halo.ANSI_CYAN + "join" + Halo.ANSI_RESET + " " + Halo.ANSI_PURPLE + "<ip> <port> <nickname> <lobbyNumber>" + Halo.ANSI_RESET + "  ");
                    System.out.println("                                   : joins an existing lobby using custom port number \n" +
                            "                                     and address values");
                    System.out.println("=> " + Halo.ANSI_CYAN + "rejoin" + Halo.ANSI_RESET + " " + Halo.ANSI_PURPLE + "<ip> <port> <nickname> <lobbyNumber>" + Halo.ANSI_RESET + "  ");
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
                        try {
                            MSG_CREATE_LOBBY m = new MSG_CREATE_LOBBY(numberOfPlayers, nickname);
                            Halo.objectOutputStream.writeObject(m);
                        } catch (IllegalArgumentException e) {
                            System.out.println("The parameters for the creation are not correct!");
                            return Phase.MainMenu;
                        }

                        message = (Message) Halo.objectInputStream.readObject();

                        if (message.getMessageType() == MessageType.MSG_OK_CREATE) {
                            MSG_OK_CREATE msg = (MSG_OK_CREATE) message;
                            System.out.println(Halo.ANSI_GREEN + " >> Connected! " + Halo.ANSI_RESET);
                            System.out.println(Halo.ANSI_GREEN + " <> Your lobby number is " + msg.getLobbyNumber() + Halo.ANSI_RESET);
                            Halo.myNickname = textList.get(3);
                            if (Integer.parseInt(textList.get(4)) == 1)
                                Halo.solo = true;
                            else
                                Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            MSG_ERROR msg = (MSG_ERROR) message;
                            System.out.println(Halo.ANSI_RED + " >> Error! " + Halo.ANSI_RESET);
                            System.out.println(Halo.ANSI_RED + " <> " + msg.getErrorMessage() + Halo.ANSI_RESET);
                            Halo.closeStreams();
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
                        try {
                            MSG_JOIN_LOBBY m = new MSG_JOIN_LOBBY(nickname, lobbyNumber);
                            Halo.objectOutputStream.writeObject(m);
                        } catch (IllegalArgumentException e) {
                            System.out.println("The parameters for the join are not correct!");
                            return Phase.MainMenu;
                        }
                        message = (Message) Halo.objectInputStream.readObject();
                        if (message.getMessageType() == MessageType.MSG_OK_JOIN) {
                            MSG_OK_JOIN msg = (MSG_OK_JOIN) message;
                            System.out.println(Halo.ANSI_GREEN + " >> Connected! " + Halo.ANSI_RESET);
                            System.out.println(Halo.ANSI_GREEN + " <> Your assigned nickname is " + msg.getAssignedNickname() + Halo.ANSI_RESET);
                            Halo.myNickname = msg.getAssignedNickname();
                            Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            MSG_ERROR msg = (MSG_ERROR) message;
                            System.out.println(Halo.ANSI_RED + " >> Error! " + Halo.ANSI_RESET);
                            System.out.println(Halo.ANSI_RED + " <> " + msg.getErrorMessage() + Halo.ANSI_RESET);
                            Halo.closeStreams();
                            return Phase.Error;
                        }
                    } catch (IOException | ClassNotFoundException | NumberFormatException e) {
                        e.printStackTrace();
                        return Phase.MainMenu;
                    }
                    break;
                case "rejoin":
                    if (!checkJoinCommand(textList)) break;
                    try {
                        String customAddress = textList.get(1);
                        int customPort = Integer.parseInt(textList.get(2));
                        openStreams(customAddress, customPort);

                        int lobbyNumber = Integer.parseInt(textList.get(4));
                        String nickname = textList.get(3);
                        MSG_REJOIN_LOBBY m = new MSG_REJOIN_LOBBY(nickname, lobbyNumber);

                        Halo.objectOutputStream.writeObject(m);

                        message = (Message) Halo.objectInputStream.readObject();
                        if (message.getMessageType() == MessageType.MSG_OK_REJOIN) {
                            MSG_OK_REJOIN msg = (MSG_OK_REJOIN) message;
                            System.out.println(Halo.ANSI_GREEN + " >> RE - Connected! " + Halo.ANSI_RESET);
                            System.out.println(Halo.ANSI_GREEN + " <> Your assigned nickname is " + msg.getNickname() + Halo.ANSI_RESET);
                            Halo.myNickname = msg.getNickname();
                            Halo.solo = false;
                            return Phase.Game;
                        } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                            MSG_ERROR msg = (MSG_ERROR) message;
                            System.out.println(Halo.ANSI_RED + " >> Error! " + Halo.ANSI_RESET);
                            System.out.println(Halo.ANSI_RED + " <> " + msg.getErrorMessage() + Halo.ANSI_RESET);
                            Halo.closeStreams();
                            return Phase.Error;
                        }
                    } catch (IOException | ClassNotFoundException | NumberFormatException e) {
                        System.out.println(Halo.ANSI_RED + " >> There was an error" + Halo.ANSI_RESET);
                        e.printStackTrace();
                        return Phase.MainMenu;
                    }
                    break;
                case "halo":
                    new Thread(this::EE).start();
                    break;
                default:
                    System.out.println(Halo.ANSI_RED + " > Sorry, we didn't catch that" + Halo.ANSI_RESET);
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

    private boolean checkCreateCommand(List<String> textList) {
        if (textList.size() != 5) {
            System.out.println(Halo.ANSI_RED + " > Error! The number of parameters is incorrect!" + Halo.ANSI_RESET);
            return false;
        }
        try {
            if (Integer.parseInt(textList.get(2)) >= 65536) {
                System.out.println(Halo.ANSI_RED + " > Error! The port number is way too high!" + Halo.ANSI_RESET);
                return false;
            }
            if (Integer.parseInt(textList.get(2)) <= 1023) {
                System.out.println(Halo.ANSI_RED + " > Error! Your port number must be greater than 1023!" + Halo.ANSI_RESET);
                return false;
            }
            if (Integer.parseInt(textList.get(4)) > 4) {
                System.out.println(Halo.ANSI_RED + " > Error! The number of players must be < 5" + Halo.ANSI_RESET);
                return false;
            }
            if (Integer.parseInt(textList.get(4)) < 1) {
                System.out.println(Halo.ANSI_RED + " > Error! There must be at least one player!" + Halo.ANSI_RESET);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(Halo.ANSI_RED + " > Error! Could not parse the number!" + Halo.ANSI_RESET);
            return false;
        }
        return true;
    }

    private boolean checkJoinCommand(List<String> textList) {
        if (textList.size() != 5) {
            System.out.println(Halo.ANSI_RED + " > Error! The number of parameters is incorrect!" + Halo.ANSI_RESET);
            return false;
        }
        try {
            if (Integer.parseInt(textList.get(2)) >= 65536) {
                System.out.println(Halo.ANSI_RED + " > Error! The port number is way too high!" + Halo.ANSI_RESET);
                return false;
            }
            if (Integer.parseInt(textList.get(2)) <= 1023) {
                System.out.println(Halo.ANSI_RED + " > Error! Your port number must be greater than 1023!" + Halo.ANSI_RESET);
                return false;
            }
            if (Integer.parseInt(textList.get(4)) <= -1 || Integer.parseInt(textList.get(4)) >= 500) {
                System.out.println(Halo.ANSI_RED + " > Error! Lobby number must be hamburgered between 0 and 500!" + Halo.ANSI_RESET);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(Halo.ANSI_RED + " > Error! Could not parse the number!" + Halo.ANSI_RESET);
            return false;
        }
        return true;
    }

    private void EE() {
        List<String> list = new ArrayList<>();

        list.add("\n");
        list.add("      ___           ___           ___       ___     ");
        list.add("     /\\__\\         /\\  \\         /\\__\\     /\\  \\    ");
        list.add("    /:/  /        /::\\  \\       /:/  /    /::\\  \\   ");
        list.add("   /:/__/        /:/\\:\\  \\     /:/  /    /:/\\:\\  \\  ");
        list.add("  /::\\  \\ ___   /::\\~\\:\\  \\   /:/  /    /:/  \\:\\  \\ ");
        list.add(" /:/\\:\\  /\\__\\ /:/\\:\\ \\:\\__\\ /:/__/    /:/__/ \\:\\__\\");
        list.add(" \\/__\\:\\/:/  / \\/__\\:\\/:/  / \\:\\  \\    \\:\\  \\ /:/  /");
        list.add("      \\::/  /       \\::/  /   \\:\\  \\    \\:\\  /:/  / ");
        list.add("      /:/  /        /:/  /     \\:\\  \\    \\:\\/:/  /  ");
        list.add("     /:/  /        /:/  /       \\:\\__\\    \\::/  /   ");
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


        for (String s : list) {
            try {
                Thread.sleep(333);
                System.out.println(s);
            } catch (InterruptedException ignored) {
            }
        }
    }
}

class GamePhase {

    public Phase run() {
        Message message;
        boolean execute = false;

        System.out.println(" >> Waiting for Initial update model.");
        System.out.println(Halo.ANSI_RED + " >> Console is unresponsive. " + Halo.ANSI_RESET + "Please wait.");
        try {

            message = (Message) Halo.objectInputStream.readObject();
            if (message.getMessageType() == MessageType.MSG_ERROR) {
                System.out.println(Halo.ANSI_RED + " <> " + ((MSG_ERROR) message).getErrorMessage());
                return Phase.Error;
            } else if (message.getMessageType() == MessageType.MSG_UPD_Full) {
                Halo.game = new GameSimplified();
                synchronized (Halo.game) {
                    Halo.game.updateAll((MSG_UPD_Full) message);
                }
                System.out.println(Halo.ANSI_GREEN + " >> Model received.");
                System.out.println(" >> Console is responsive." + Halo.ANSI_RESET);
            } else {
                System.out.println(" Received unexpected message: " + message.getMessageType());
                return Phase.Error;
            }


            Halo.yourTurn = Halo.game.isMyTurn(Halo.myPlayerNumber);

            Halo.myPlayerNumber = Halo.game.getMyPlayerNumber(Halo.myNickname);
            if (Halo.myPlayerNumber == 0) return Phase.Error;
            Halo.myPlayerRef = Halo.game.getPlayerRef(Halo.myPlayerNumber);
            System.out.println(" >> Remember " + Halo.ANSI_CYAN + "help" + Halo.ANSI_RESET + " for a list of commands.");
            List<String> textList = new ArrayList<>();
            String text;

            new Thread(new UpdateHandler()).start();

            while (true) {
                execute = true;
//phase 2: gets input
                text = Halo.input.nextLine();

                textList.clear();
                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

//phase 3: input gets converted into specific action??????????????
                // must correct synchronization
                if (Halo.yourTurn) {
                    if (Halo.game.isMiddleActive()) {
                        if (Halo.game.isLeaderCardsObjectEnabled()) {
                            int first = 0;
                            int second = 0;
                            obtainNumberLoop:
                            while (true) {
                                if (check1_4Number(textList)) {
                                    first = Integer.parseInt(textList.get(0));

                                    while (true) {
                                        System.out.println(" > Please pick the second card: ");
                                        System.out.print(" Card number: ");
                                        text = Halo.input.nextLine();
                                        textList.clear();
                                        textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                                        if (checkLeaderCardObjectNumber(textList, first)) {
                                            second = Integer.parseInt(textList.get(0));
                                            System.out.println(Halo.ANSI_GREEN + " > Thanks, you choose " + Halo.ANSI_RESET + first + Halo.ANSI_GREEN + " and " + Halo.ANSI_RESET + second);
                                            break obtainNumberLoop;
                                        }
                                    }


                                } else {
                                    System.out.print(" Card number: ");
                                    text = Halo.input.nextLine();
                                    textList.clear();
                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                }
                            }

                            ArrayList<LeaderCard> list = new ArrayList<>();
                            list.add(Halo.game.getLeaderCardsObject().getCard(first - 1));
                            list.add(Halo.game.getLeaderCardsObject().getCard(second - 1));
                            MSG_INIT_CHOOSE_LEADERCARDS msgToSend = new MSG_INIT_CHOOSE_LEADERCARDS(list);
                            Halo.objectOutputStream.writeObject(msgToSend);
                        } else if (Halo.game.isResourceObjectEnabled()) {
                            Resource resource = Resource.NONE;
                            while (true) {
                                if (check1_4Number(textList)) {
                                    int number = Integer.parseInt(textList.get(0));
                                    switch (number) {
                                        case 1:
                                            resource = Resource.SHIELD;
                                            break;
                                        case 2:
                                            resource = Resource.COIN;
                                            break;
                                        case 3:
                                            resource = Resource.SERVANT;
                                            break;
                                        case 4:
                                            resource = Resource.STONE;
                                            break;
                                    }
                                    break;
                                } else {
                                    System.out.print(" Resource number: ");
                                    text = Halo.input.nextLine();
                                    textList.clear();
                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                }
                            }

                            MSG_INIT_CHOOSE_RESOURCE msgToSend = new MSG_INIT_CHOOSE_RESOURCE(resource);
                            Halo.objectOutputStream.writeObject(msgToSend);
                        } else if (Halo.game.isMarketHelperEnabled()) {
                            int choice = 0;

                            while (true) {
                                if (checkChoice(textList)) {
                                    choice = Integer.parseInt(textList.get(0));
                                    break;
                                } else {
                                    System.out.print(" Choice: ");
                                    text = Halo.input.nextLine();
                                    textList.clear();
                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                }
                            }

                            MSG_ACTION_MARKET_CHOICE msgToSend = new MSG_ACTION_MARKET_CHOICE(choice);
                            Halo.objectOutputStream.writeObject(msgToSend);
                        } else if (Halo.game.isDevelopmentCardsVendorEnabled()) {
                            int cardNum;
                            int slotNum;
                            while (true) {
                                if (checkNumbers(textList)) {
                                    cardNum = Integer.parseInt(textList.get(0));
                                    slotNum = Integer.parseInt(textList.get(1));
                                    break;
                                } else {
                                    System.out.println(" > Please write the card you want to buy and the slot you want to put it in!  ");
                                    text = Halo.input.nextLine();
                                    textList.clear();
                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                }
                            }
                            MSG_ACTION_CHOOSE_DEVELOPMENT_CARD msgToSend = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(cardNum, slotNum);
                            Halo.objectOutputStream.writeObject(msgToSend);
                        } else if (Halo.game.isLeaderBoardEnabled()) {
                            // something
                            return Phase.MainMenu;
                        }
                        execute = false;
                    }
                }

                if (execute) {
                    switch (textList.get(0).toLowerCase()) {
                        case "quit":
                            Halo.closeStreams();
                            return Phase.MainMenu; //testing purposes
                        case "help": {
                            System.out.println("  List of commands! \n \n");
                            System.out.println("=> " + Halo.ANSI_CYAN + "quit" + Halo.ANSI_RESET + "                             : kills the thread and exits the program");
                            System.out.println("=> " + Halo.ANSI_CYAN + "help" + Halo.ANSI_RESET + "                            : displays the possible terminal commands");
                            System.out.println("=> " + Halo.ANSI_CYAN + "show " + Halo.ANSI_PURPLE + "<something>" + Halo.ANSI_RESET + "                 : shows one of my Assets");
                            System.out.println("=> " + Halo.ANSI_CYAN + "action" + Halo.ANSI_RESET + "                          : enables the action routine, if possible");
                            System.out.println(Halo.ANSI_PURPLE + "something" + Halo.ANSI_RESET + " :>  'leaderCards'");
                            System.out.println("          :>  'players' ");
                            System.out.println("          :>  'market' ");
                            System.out.println("          :>  'depot' ");
                            System.out.println("          :>  'strongbox' ");
                            System.out.println("          :>  'devslot' ");
                            System.out.println("          :>  'devdeck' ");
                            System.out.println("          :>  'faithtrack' ");
                            System.out.println("          :>  'myvp' ");
                            System.out.println("=> " + Halo.ANSI_CYAN + "show" + Halo.ANSI_PURPLE + " <nickname> <something>" + Halo.ANSI_RESET + "     : shows one of the other players' assets.\n" +
                                    "                                     Specify the nickname of the player.        ");
                            System.out.println("=> " + Halo.ANSI_CYAN + "show player " + Halo.ANSI_PURPLE + "<num> <something>" + Halo.ANSI_RESET + "   : shows one of the other players' assets.\n" +
                                    "                                     Specify the player's number. ");
                            System.out.println(Halo.ANSI_PURPLE + "num" + Halo.ANSI_RESET + "       :>  1, .. , maxLobbySize ");
                            System.out.println(Halo.ANSI_PURPLE + "something" + Halo.ANSI_RESET + " :>  'leaderCards'");
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
                                            System.out.println(Halo.ANSI_GREEN + " > showing players  name - number" + Halo.ANSI_RESET);
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
                                            System.out.println(textList);
                                    }
                                } else //if(size==3)
                                {
                                    PlayerSimplified player = Halo.game.getPlayerRef(textList.get(1));
                                    if (player == null) {
                                        System.out.println(Halo.ANSI_RED + " > There's no such player with that name. " + Halo.ANSI_RESET);
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
                                            System.out.println(textList);
                                    }
                                }
                            }
                        }
                        break;
                        case "action":
                            if (Halo.yourTurn) {
                                printActions();
                                loop:
                                while (true) {
                                    System.out.println("Please pick a number: ");
                                    System.out.print("> ");
                                    text = Halo.input.nextLine();
                                    textList.clear();
                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                                    if (checkAction(textList)) {
                                        int choice = Integer.parseInt(textList.get(0));
                                        switch (choice) {
//ACTION ACTIVATE LEADERCARD
                                            case 1:
                                                int cardToActivate = -1;
                                                LeaderCard l1a = Halo.myPlayerRef.getLeaderCards()[0];
                                                LeaderCard l2a = Halo.myPlayerRef.getLeaderCards()[1];

                                                if (l1a != null)
                                                    System.out.println(" Press 1 to enable the first card ");
                                                if (l2a != null)
                                                    System.out.println(" Press 2 to enable the second card ");

                                                while (true) {
                                                    System.out.print(" Card number: ");
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                                                    if (checkLeaderCardsNumber(textList)) {
                                                        cardToActivate = Integer.parseInt(textList.get(0));
                                                        break;
                                                    }
                                                }

                                                MSG_ACTION_ACTIVATE_LEADERCARD msgToSend1 = new MSG_ACTION_ACTIVATE_LEADERCARD(cardToActivate - 1);
                                                Halo.objectOutputStream.writeObject(msgToSend1);
                                                break loop;
//ACTION DISCARD LEADERCARD
                                            case 2:
                                                int cardToDiscard;
                                                LeaderCard l1d = Halo.myPlayerRef.getLeaderCards()[0];
                                                LeaderCard l2d = Halo.myPlayerRef.getLeaderCards()[1];

                                                if (l1d != null)
                                                    System.out.println(" Press 1 to disable the first card ");
                                                if (l2d != null)
                                                    System.out.println(" Press 2 to disable the second card ");

                                                while (true) {
                                                    System.out.print(" Card number: ");
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                                                    if (checkLeaderCardsNumber(textList)) {
                                                        cardToDiscard = Integer.parseInt(textList.get(0)) - 1;
                                                        break;
                                                    }
                                                }

                                                MSG_ACTION_DISCARD_LEADERCARD msgToSend2 = new MSG_ACTION_DISCARD_LEADERCARD(cardToDiscard);
                                                Halo.objectOutputStream.writeObject(msgToSend2);
                                                break loop;
//ACTION ACTIVATE PRODUCTION
                                            case 3:
                                                //production //all the system out must be reviewed by a standard that only stefano knows
                                                boolean basic = false;
                                                boolean[] leader = {false, false};
                                                boolean std = false;
                                                boolean[] standard = {false, false, false};
                                                ArrayList<Resource> basicInput = new ArrayList<>();
                                                Resource basicOutput = null;
                                                boolean ldr = false;
                                                Resource leaderOutput1 = null;
                                                Resource leaderOutput2 = null;
                                                int input;
                                                System.out.println("Here's your depot and strongbox");
                                                System.out.println(Halo.myPlayerRef.getWarehouseDepot());
                                                System.out.println(Halo.myPlayerRef.getStrongbox());
                                                if (Halo.myPlayerRef.getLeaderCards()[0].getSpecialAbility().isExtraDepot()) {
                                                    System.out.println("Extra depot: " + Halo.myPlayerRef.getLeaderCards()[0].getSpecialAbility());
                                                }
                                                if (Halo.myPlayerRef.getLeaderCards()[1].getSpecialAbility().isExtraDepot()) {
                                                    System.out.println("Extra depot: " + Halo.myPlayerRef.getLeaderCards()[1].getSpecialAbility());
                                                }
                                                System.out.println(" >> BASE PRODUCTION\nDo you want to activate it? 1 for yes, 2 for no");
                                                while (true) {
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                    if (!check1Or2(textList))
                                                        System.out.println("Invalid input, try again");
                                                    else {
                                                        input = Integer.parseInt(textList.get(0));
                                                        if (input == 1) basic = true;
                                                        break;
                                                    }
                                                }

                                                if (basic) {
                                                    System.out.println("Which resources do you want as input for the basic production? (You must decide 2 resources)");
                                                    while (true) {
                                                        text = Halo.input.nextLine();
                                                        textList.clear();
                                                        textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                        if (!checkResource(textList, 2, false))
                                                            System.out.println("They are not a valid basic output!");
                                                        else {
                                                            basicInput.add(convertStringToResource(textList.get(0)));
                                                            basicInput.add(convertStringToResource(textList.get(1)));
                                                            break;
                                                        }
                                                    }
                                                    System.out.println("Which resource do you want as output for the basic production?");
                                                    while (true) {
                                                        text = Halo.input.nextLine();
                                                        textList.clear();
                                                        textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                        if (!checkResource(textList, 1, false))
                                                            System.out.println("That's not a valid basic input!");
                                                        else {
                                                            basicOutput = convertStringToResource(textList.get(0));
                                                            break;
                                                        }
                                                    }
                                                }

                                                System.out.println(">> STANDARD PRODUCTION.\nDo you want to activate it? 1 for yes, 2 for no");
                                                while (true) {
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                    if (!check1Or2(textList))
                                                        System.out.println("Invalid input, try again");
                                                    else {
                                                        input = Integer.parseInt(textList.get(0));
                                                        if (input == 1) std = true;
                                                        break;
                                                    }
                                                }
                                                if (std) {
                                                    System.out.println(Halo.myPlayerRef.getDevelopmentSlot());
                                                    System.out.println("Which cards do you want to activate?");
                                                    while (true) {
                                                        text = Halo.input.nextLine();
                                                        textList.clear();
                                                        textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                        if (checkStandardProductionInput(textList)) {
                                                            for (String s : textList) {
                                                                standard[Integer.parseInt(s) - 1] = true;
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }

                                                if (Arrays.stream(Halo.myPlayerRef.getLeaderCards()).anyMatch(l -> l.getSpecialAbility().isProduction())) {
                                                    System.out.println(" >> LEADER PRODUCTION");
                                                    for (int i = 0; i < 2; i++) {
                                                        LeaderCard l = Halo.myPlayerRef.getLeaderCards()[i];
                                                        if (l != null && l.getEnable() && l.getSpecialAbility().isProduction()) {
                                                            System.out.println(l);
                                                            System.out.println("Do you want to activate this card? (1 for yes, 2 for no)");
                                                            while (true) {
                                                                text = Halo.input.nextLine();
                                                                textList.clear();
                                                                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                                if (check1Or2(textList)) {
                                                                    if (Integer.parseInt(textList.get(0)) == 1)
                                                                        leader[i] = true;
                                                                    break;
                                                                } else System.out.println("Invalid input, try again");
                                                            }
                                                        }
                                                    }
                                                }

                                                if (leader[0]) {
                                                    System.out.println("Which resource do you want to produce with your leaderCard 1, along with a faith point (input is" + ((Production) Halo.myPlayerRef.getLeaderCards()[0].getSpecialAbility()).getInput() + ")?");
                                                    while (true) {
                                                        text = Halo.input.nextLine();
                                                        textList.clear();
                                                        textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                        if (checkResource(textList, 1, false)) {
                                                            leaderOutput1 = convertStringToResource(textList.get(0));
                                                            break;
                                                        } else System.out.println("Invalid input, try again");
                                                    }
                                                }

                                                if (leader[1]) {
                                                    System.out.println("Which resource do you want to produce with your leaderCard 2, along with a faith point (input is" + ((Production) Halo.myPlayerRef.getLeaderCards()[1].getSpecialAbility()).getInput() + ")?");
                                                    while (true) {
                                                        text = Halo.input.nextLine();
                                                        textList.clear();
                                                        textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                        if (checkResource(textList, 1, false)) {
                                                            leaderOutput2 = convertStringToResource(textList.get(0));
                                                            break;
                                                        } else System.out.println("Invalid input, try again");
                                                    }
                                                }
                                                MSG_ACTION_ACTIVATE_PRODUCTION msgToSend3 = new MSG_ACTION_ACTIVATE_PRODUCTION(standard, basic, leader, basicInput, basicOutput, leaderOutput1, leaderOutput2);
                                                Halo.objectOutputStream.writeObject(msgToSend3);
                                                break loop;
//ACTION CHANGE DEPOT CONFIG
                                            case 4:
                                                //change depot
                                                System.out.println(" >> My friend, please give me the new configuration of your Warehouse Depot.");
                                                Resource shelf1;
                                                Resource[] shelf2 = new Resource[2];
                                                Resource[] shelf3 = new Resource[3];
                                                while (true) {
                                                    System.out.print(" Shelf 1: ");
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                    if (!checkResource(textList, 1, true))
                                                        System.out.println(" > Ohoh my friend, that's not possible in shelf 1. Try again." + Halo.ANSI_RESET);
                                                    else {
                                                        shelf1 = convertStringToResource(textList.get(0).toLowerCase());
                                                        break;
                                                    }
                                                }

                                                while (true) {
                                                    System.out.print(" Shelf 2: ");
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                    if (!checkResource(textList, 2, true))
                                                        System.out.println(" > Ohoh my friend, that's not possible in shelf 2. Try again." + Halo.ANSI_RESET);
                                                    else {
                                                        shelf2[0] = convertStringToResource(textList.get(0).toLowerCase());
                                                        shelf2[1] = convertStringToResource(textList.get(1).toLowerCase());
                                                        break;
                                                    }
                                                }

                                                while (true) {
                                                    System.out.print(" Shelf 3: ");
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                    if (!checkResource(textList, 3, true))
                                                        System.out.println(" > Ohoh my friend, that's not possible in shelf 3. Try again." + Halo.ANSI_RESET);
                                                    else {
                                                        shelf3[0] = convertStringToResource(textList.get(0).toLowerCase());
                                                        shelf3[1] = convertStringToResource(textList.get(1).toLowerCase());
                                                        shelf3[2] = convertStringToResource(textList.get(2).toLowerCase());
                                                        break;
                                                    }
                                                }

                                                int firstExtra = -1;
                                                int secondExtra = -1;

                                                if (Halo.myPlayerRef.getLeaderCards()[0] != null) {
                                                    if (Halo.myPlayerRef.getLeaderCards()[0].getSpecialAbility().isExtraDepot()) {
                                                        if (Halo.myPlayerRef.getLeaderCards()[0].getEnable()) {
                                                            System.out.println(" > I noticed you have an extra depot for the resource " + ((ExtraDepot) Halo.myPlayerRef.getLeaderCards()[0].getSpecialAbility()).getResourceType() + ". Please tell me how much I have to Phil Heath");

                                                            while (true) {
                                                                System.out.print(" Number of Resources: ");
                                                                text = Halo.input.nextLine();
                                                                textList.clear();
                                                                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                                try {
                                                                    int num = Integer.parseInt(textList.get(0));
                                                                    if (num < 0 || num > 2)
                                                                        System.out.println(" > That's not correct for an extraDepot. Try again." + Halo.ANSI_RESET);
                                                                    else {
                                                                        firstExtra = num;
                                                                        break;
                                                                    }
                                                                } catch (NumberFormatException e) {
                                                                    System.out.println("EHIEHIEHI that's not a number! Wanna mess with me?");
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if (Halo.myPlayerRef.getLeaderCards()[1] != null) {
                                                    if (Halo.myPlayerRef.getLeaderCards()[1].getSpecialAbility().isExtraDepot()) {
                                                        if (Halo.myPlayerRef.getLeaderCards()[1].getEnable()) {
                                                            System.out.println(" > I noticed you have an extra depot for the resource " + ((ExtraDepot) Halo.myPlayerRef.getLeaderCards()[1].getSpecialAbility()).getResourceType() + ". Please tell me how much I have to Phil Heath");

                                                            while (true) {
                                                                System.out.print(" Number of Resources: ");
                                                                text = Halo.input.nextLine();
                                                                textList.clear();
                                                                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                                int num;
                                                                try {
                                                                    num = Integer.parseInt(textList.get(0));
                                                                    if (num < 0 || num > 2)
                                                                        System.out.println(" > That's not correct for an extraDepot. Try again." + Halo.ANSI_RESET);
                                                                    else {
                                                                        secondExtra = num;
                                                                        break;
                                                                    }
                                                                } catch (NumberFormatException e) {
                                                                    System.out.println("EHIEHIEHI that's not a number! Wanna mess with me?");
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                MSG_ACTION_CHANGE_DEPOT_CONFIG msgToSend4 = new MSG_ACTION_CHANGE_DEPOT_CONFIG(shelf1, shelf2, shelf3, firstExtra, secondExtra);
                                                Halo.objectOutputStream.writeObject(msgToSend4);
                                                break loop;
//ACTION BUY
                                            case 5:
                                                //buy card
                                                MSG_ACTION_BUY_DEVELOPMENT_CARD msgToSend5 = new MSG_ACTION_BUY_DEVELOPMENT_CARD();
                                                Halo.objectOutputStream.writeObject(msgToSend5);
                                                break loop;
//ACTION GET MARKET RESOURCES
                                            case 6:
                                                //market
                                                int num;
                                                boolean column;
                                                System.out.println(" >> Here's the market, if this is not qol I don't know what could be then:");
                                                System.out.println(Halo.game.getMarket());
                                                System.out.println(" > Insert 1 for a row or 2 for a column");

                                                while (true) {
                                                    System.out.print(" row or column: ");
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                                                    if (check1Or2(textList)) {
                                                        num = Integer.parseInt(textList.get(0));
                                                        break;
                                                    } else System.out.println(" > Invalid input");
                                                }
                                                if (num == 1) {
                                                    column = false;
                                                    System.out.println("\n > Please choose the row (must be between 1 and 3)");
                                                } else {
                                                    System.out.println("\n > Please choose the column (must be between 1 and 4)");
                                                    column = true;
                                                }

                                                while (true) {
                                                    text = Halo.input.nextLine();
                                                    textList.clear();
                                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                                                    try {
                                                        if (textList.size() != 1)
                                                            System.out.println(" > I said you have to choose a number, you don't need to write a book");
                                                        else {
                                                            num = Integer.parseInt(textList.get(0));
                                                            if (!column && (num < 1 || num > 3))
                                                                System.out.println(" > That isn't a correct row (must be between 1 and 3)");
                                                            else if (column && (num < 1 || num > 4))
                                                                System.out.println(" > That isn't a correct column (must be between 1 and 4)");
                                                            else break;
                                                        }
                                                    } catch (NumberFormatException e) {
                                                        System.out.println(" > That's not a number!");
                                                    }
                                                }
                                                MSG_ACTION_GET_MARKET_RESOURCES msgToSend6 = new MSG_ACTION_GET_MARKET_RESOURCES(column, num - 1);
                                                Halo.objectOutputStream.writeObject(msgToSend6);
                                                break loop;
//ACTION BUY DEV CARDS
                                            case 7:
                                                MSG_ACTION_ENDTURN msgToSend7 = new MSG_ACTION_ENDTURN();
                                                Halo.objectOutputStream.writeObject(msgToSend7);
                                                break loop;
//GO BACK
                                            case 8:
                                                break loop;
                                        }
                                    }
                                }

                            } else {
                                System.out.println(Halo.ANSI_RED + " >> Hey, wait for your turn!" + Halo.ANSI_RESET);
                            }
                            break;
                        default:
                            System.out.println(Halo.ANSI_RED + " > Sorry, I didn't catch that" + Halo.ANSI_RESET);

                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return Phase.Error;
        }

        //return Phase.MainMenu;
    }

    private boolean check1Or2(List<String> textList) {
        if (textList.size() == 1) {
            try {
                int RowOrCol = Integer.parseInt(textList.get(0));
                if (RowOrCol == 1 || RowOrCol == 2) {
                    return true;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private boolean checkStandardProductionInput(List<String> input) {
        int n1 = -2;
        int n2 = -1;
        int n3 = 0;
        if (input.size() > 3) {
            System.out.println("Please insert a maximum of 3 numbers");
            return false;
        }
        try {
            n1 = Integer.parseInt(input.get(0));
            if (n1 < 1 || n1 > 4) {
                System.out.println("The numbers you insert must be between 1 and 3");
                return false;
            }
            if (input.size() > 1) {
                n2 = Integer.parseInt(input.get(1));
                if (n2 < 1 || n2 > 4) {
                    System.out.println("The numbers you insert must be between 1 and 3");
                    return false;
                }
                if (n1 == n2) {
                    System.out.println("The numbers you insert must be different!");
                    return false;
                }
            }
            if (input.size() > 2) {
                n3 = Integer.parseInt(input.get(2));
                if (n3 < 1 || n3 > 4) {
                    System.out.println("The numbers you insert must be between 1 and 3");
                    return false;
                }
                if ((n1 == n3) || (n2 == n3)) {
                    System.out.println("The numbers you insert must be different!");
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("You didn't insert only numbers!");
            return false;
        }
        return true;
    }

    private boolean checkResource(List<String> textList, int num, boolean resourceNonePermitted) {

        if (textList.size() != num) return false;
        for (String resource : textList) {
            if ((resourceNonePermitted) && !(resource.equalsIgnoreCase("stone") || resource.equalsIgnoreCase("coin") || resource.equalsIgnoreCase("shield") || resource.equalsIgnoreCase("servant") || resource.equalsIgnoreCase("none")))
                return false;
            else if (!resourceNonePermitted && !(resource.equalsIgnoreCase("stone") || resource.equalsIgnoreCase("coin") || resource.equalsIgnoreCase("shield") || resource.equalsIgnoreCase("servant")))
                return false;
        }

        return true;
    }

    private Resource convertStringToResource(String resource) {
        if (resource.equals("servant")) return Resource.SERVANT;
        if (resource.equals("coin")) return Resource.COIN;
        if (resource.equals("shield")) return Resource.SHIELD;
        if (resource.equals("stone")) return Resource.STONE;
        if (resource.equals("none")) return Resource.NONE;
        return null;
    }

    private boolean checkLeaderCardsNumber(List<String> textList) {
        if (textList.size() > 1) {
            System.out.println(Halo.ANSI_RED + " > Please insert just a number" + Halo.ANSI_RESET);
            return false;
        }
        try {
            int number = Integer.parseInt(textList.get(0));
            LeaderCard l1 = Halo.myPlayerRef.getLeaderCards()[0];
            LeaderCard l2 = Halo.myPlayerRef.getLeaderCards()[1];
            if (number == 1) {
                if (l1 == null) {
                    System.out.println(Halo.ANSI_RED + " > Sorry, but that card is discarded" + Halo.ANSI_RESET);
                    return false;
                } else if (l1.getEnable()) {
                    System.out.println(Halo.ANSI_RED + " > Sorry, but that card is already activated" + Halo.ANSI_RESET);
                    return false;
                }
            }
            if (number == 2) {
                if (l2 == null) {
                    System.out.println(Halo.ANSI_RED + " > Sorry, but that card is discarded" + Halo.ANSI_RESET);
                    return false;
                } else if (l2.getEnable()) {
                    System.out.println(Halo.ANSI_RED + " > Sorry, but that card is already activated" + Halo.ANSI_RESET);
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(Halo.ANSI_RED + " > Sorry, but that was not a number" + Halo.ANSI_RESET);
            return false;
        }

        return true;
    }

    private boolean checkAction(List<String> textList) {
        if (textList.size() > 1) {
            System.out.println(Halo.ANSI_RED + " > Please insert just a number" + Halo.ANSI_RESET);
            return false;
        }
        try {
            int number = Integer.parseInt(textList.get(0));
            if (number < 1 || number > 8) {
                System.out.println(Halo.ANSI_RED + " > Please pick a number between 1 and 7" + Halo.ANSI_RESET);
                return false;
            }
//check very powerful action    already playedE
            if (Halo.action && (number == 3 || number == 5 || number == 6)) {
                System.out.println(Halo.ANSI_RED + " > You already did a main move" + Halo.ANSI_RESET);
                return false;
            }

            LeaderCard l1 = Halo.myPlayerRef.getLeaderCards()[0];
            LeaderCard l2 = Halo.myPlayerRef.getLeaderCards()[1];
//check action 1 and 2
            if (number == 1 || number == 2) {
                if (l1 == null && l2 == null) {
                    System.out.println(Halo.ANSI_RED + " > You can't, because the cards are absent " + Halo.ANSI_RESET);
                    return false;
                }
                if (l1 != null) {
                    if (l2 != null) {
                        if (l1.getEnable() && l2.getEnable()) {
                            System.out.println(Halo.ANSI_RED + " > You can't, because the cards are already activated " + Halo.ANSI_RESET);
                            return false;
                        }
                    } else {
                        if (l1.getEnable()) {
                            System.out.println(Halo.ANSI_RED + " > You can't, because the first card is already enabled and the second one is absent " + Halo.ANSI_RESET);
                            return false;
                        }
                    }
                } else {
                    if (l2.getEnable()) {
                        System.out.println(Halo.ANSI_RED + " > You can't, the first card is absent and the second one is already enabled " + Halo.ANSI_RESET);
                        return false;
                    }
                }
            }
//check activate production
            if (number == 3) {
                //for giacomo (?)
            }
//check changeDepotConfig
//check buyDevelopmentCard
//getMarketResource
        } catch (NumberFormatException e) {
            System.out.println(Halo.ANSI_RED + " > Sorry, but that was not a number" + Halo.ANSI_RESET);
            return false;
        }

        return true;
    }

    private void printActions() {
        System.out.println("\u001B[36m" + "  LIST OF ACTIONS! " + "\u001B[0m");
        System.out.println("  =>   1   : activate a leader card");
        System.out.println("  =>   2   : discard a leader card");
        System.out.println("  =>   3   : activate production");
        System.out.println("  =>   4   : change depot configuration");
        System.out.println("  =>   5   : buy development card");
        System.out.println("  =>   6   : get market resources");
        System.out.println("  =>   7   : end turn");
    }

    private boolean checkNumbers(List<String> textList) {
        if (textList.size() == 2) {
            try {
                int cardNum = Integer.parseInt(textList.get(0));
                int slotNum = Integer.parseInt(textList.get(1));
                Map<DevelopmentCard, boolean[]> cards;
                cards = Halo.game.getDevelopmentCardsVendor().getCards();

                if (cardNum > cards.size() || cardNum < 1) {
                    System.out.println(Halo.ANSI_RED + " > That's not a possible card!" + Halo.ANSI_RESET);
                    return false;
                }
                if (slotNum < 0 || slotNum > 2) {
                    System.out.println(Halo.ANSI_RED + " > That's not a proper slot!" + Halo.ANSI_RESET);
                    return false;
                }
                Object[] dcards = cards.keySet().toArray();
                if (!cards.get((DevelopmentCard) dcards[cardNum - 1])[slotNum - 1]) {
                    System.out.println(Halo.ANSI_RED + " > Error! You can't place that card in this slot" + Halo.ANSI_RESET);
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println(Halo.ANSI_RED + " > Sorry, that was not a number" + Halo.ANSI_RESET);
                return false;
            }
        } else {
            System.out.println(Halo.ANSI_RED + " > The number of parameters is different then expected." + Halo.ANSI_RESET);
        }
        return true;
    }

    private boolean checkChoice(List<String> textList) {
        if (textList.size() > 1) {
            System.out.println(Halo.ANSI_RED + " > Please insert just a number" + Halo.ANSI_RESET);
            return false;
        }

        boolean[] choices = Halo.game.getMarketHelper().getChoices();
        try {
            int number = Integer.parseInt(textList.get(0));
            if (number < 0 || number > 7) System.out.println("Invalid input");
            else if (!choices[number]) {
                System.out.println(Halo.ANSI_RED + " > That wasn't a possible choice" + Halo.ANSI_RESET);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(Halo.ANSI_RED + " > Sorry, but that was not a number" + Halo.ANSI_RESET);
            return false;
        }
        return true;
    }

    private boolean check1_4Number(List<String> textList) {
        if (textList.size() > 1) {
            System.out.println(Halo.ANSI_RED + " > Please insert just a number" + Halo.ANSI_RESET);
            return false;
        }
        try {
            int number = Integer.parseInt(textList.get(0));
            if (number < 1 || number > 4) {
                System.out.println(Halo.ANSI_RED + " > Sorry, you have to choose between 1 and 4." + Halo.ANSI_RESET);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(Halo.ANSI_RED + " > Sorry, but that was not a number" + Halo.ANSI_RESET);
            return false;
        }
        return true;
    }

    private boolean checkLeaderCardObjectNumber(List<String> textList, int first) {
        if (textList.size() > 1) {
            System.out.println(Halo.ANSI_RED + " > Please insert just a number" + Halo.ANSI_RESET);
            return false;
        }
        try {
            int number = Integer.parseInt(textList.get(0));
            if (number < 1 || number > 4) {
                System.out.println(Halo.ANSI_RED + " > Sorry, the player number must be between 1 and 4." + Halo.ANSI_RESET);
                return false;
            }
            if (number == first) {
                System.out.println(Halo.ANSI_RED + " > Sorry, but you inserted the same number as before." + Halo.ANSI_RESET);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(Halo.ANSI_RED + " > Sorry, but that was not a number" + Halo.ANSI_RESET);
            return false;
        }
        return true;
    }

    private boolean checkShowCommand(List<String> textList) {
        if (textList.size() != 4 && textList.size() != 2 && textList.size() != 3) {
            System.out.println(Halo.ANSI_RED + " > Error! The number of parameters is incorrect" + Halo.ANSI_RESET);
            return false;
        }
        if (textList.size() == 2) {
            if (textList.get(1).equalsIgnoreCase("players")) return true;
            if (textList.get(1).equalsIgnoreCase("market")) return true;
            if (textList.get(1).equalsIgnoreCase("depot")) return true;
            if (textList.get(1).equalsIgnoreCase("strongbox")) return true;
            if (textList.get(1).equalsIgnoreCase("devslot")) return true;
            if (textList.get(1).equalsIgnoreCase("devdeck")) return true;
            if (textList.get(1).equalsIgnoreCase("faithtrack")) return true;
            if (textList.get(1).equalsIgnoreCase("myvp")) return true;
            if (textList.get(1).equalsIgnoreCase("leadercards")) return true;
            System.out.println(Halo.ANSI_RED + " > Error! You can't show this. I don't even know what 'this' is!" + Halo.ANSI_RESET);
            return false;
        }
        if (textList.size() == 4) {
            if (!textList.get(1).equalsIgnoreCase("player")) {
                System.out.println(Halo.ANSI_RED + " > Sorry, but there should be more parameters." + Halo.ANSI_RESET);
                return false;
            }
            //else
            try {
                if (Integer.parseInt(textList.get(2)) < 1) {
                    System.out.println(Halo.ANSI_RED + " > Sorry, the player number is below the minimum." + Halo.ANSI_RESET);
                    return false;
                }
                if (Integer.parseInt(textList.get(2)) > Halo.game.getPlayerSimplifiedList().size()) {
                    System.out.println(Halo.ANSI_RED + " > Sorry, the player number is above the maximum." + Halo.ANSI_RESET);
                    return false;
                }

                if (textList.get(3).equalsIgnoreCase("vp")) return true;
                if (textList.get(3).equalsIgnoreCase("leadercards")) return true;
                if (textList.get(3).equalsIgnoreCase("depot")) return true;
                if (textList.get(3).equalsIgnoreCase("strongbox")) return true;
                if (textList.get(3).equalsIgnoreCase("devslot")) return true;
                System.out.println(Halo.ANSI_RED + " > Error! You can't show this." + Halo.ANSI_RESET);
                return false;
            } catch (NumberFormatException e) {
                System.out.println(Halo.ANSI_RED + " > Sorry, the player number is not a number." + Halo.ANSI_RESET);
                return false;
            }
        }
        if (textList.size() == 3) {
            if (textList.get(2).equalsIgnoreCase("vp")) return true;
            if (textList.get(2).equalsIgnoreCase("leadercards")) return true;
            if (textList.get(2).equalsIgnoreCase("depot")) return true;
            if (textList.get(2).equalsIgnoreCase("strongbox")) return true;
            if (textList.get(2).equalsIgnoreCase("devslot")) return true;
            System.out.println(Halo.ANSI_RED + " > Error! You can't show this. I don't even know what 'this' is!" + Halo.ANSI_RESET);
            return false;
        }
        return true;
    }
}

class ErrorPhase {
    public Phase run() {
        System.out.println(Halo.ANSI_RED + " > There was an accident and you will be returned to Main Menu." + Halo.ANSI_RESET);
        try {
            Halo.socket.close();
            Halo.outputStream.close();
            Halo.inputStream.close();
            Halo.objectOutputStream.close();
            Halo.objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Phase.MainMenu;
    }
}