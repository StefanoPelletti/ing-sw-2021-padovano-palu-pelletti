package it.polimi.ingsw.client;


import it.polimi.ingsw.client.modelSimplified.GameSimplified;
import it.polimi.ingsw.client.modelSimplified.PlayerSimplified;
import it.polimi.ingsw.networking.message.*;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Full;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.specialAbilities.ExtraDepot;
import it.polimi.ingsw.server.model.specialAbilities.Production;
import it.polimi.ingsw.server.utils.A;

import java.io.*;
import java.net.Socket;
import java.util.*;

import static it.polimi.ingsw.server.model.enumerators.Resource.FAITH;


enum Phase {QUIT, MAIN_MENU, ONLINE_PHASE, LOCAL_PHASE, ERROR}


public class PhaseClient {

    public static void main(String[] args) {

        System.out.println(A.YELLOW + " <<>> Welcome player." + A.RESET);

        Phase phase = Phase.MAIN_MENU;

        while (true) {
            switch (phase) {
                case MAIN_MENU:
                    phase = (new MenuPhase()).run();
                    break;
                case ONLINE_PHASE:
                    phase = (new OnlinePhase()).run();
                    break;
                case LOCAL_PHASE:
                    phase = (new LocalPhase()).run();
                    break;
                case ERROR:
                    phase = (new ErrorPhase()).run();
                    break;
                case QUIT:
                    (new ClosingPhase()).run();
                    return;
            }
        }
    }
}

class Halo {
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
    //TODO action
    static boolean action = false;
    static boolean local;
    static boolean triedAction = false;
    static GameManager gameManager;
    static ActionManager actionManager;
    static Player myPlayerRefSRV;
    static Game gameSRV;
    private Halo() {
    }

    public static void closeStreams() {
        try {
            socket.close();
            outputStream.close();
            inputStream.close();
            objectOutputStream.close();
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sweep() {
        game = null;
        gameManager = null;
        actionManager = null;
        myPlayerRef = null;
        myPlayerRefSRV = null;
        myPlayerNumber = 0;
        myNickname = "";
        yourTurn = false;
        action = false;
        gameSRV = null;
    }

    private static LeaderCard getMyLeaderCard(int cardNumber) {
        if (Halo.local)
            return myPlayerRefSRV.getLeaderCards()[cardNumber];
        else
            return myPlayerRef.getLeaderCards()[cardNumber];
    }

    private static LeaderCard[] getBothMyLeaderCard() {
        if (Halo.local)
            return myPlayerRefSRV.getLeaderCards();
        else
            return myPlayerRef.getLeaderCards();
    }

    private static Map<DevelopmentCard, boolean[]> getVendorCards() {
        if (local)
            return gameSRV.getDevelopmentCardsVendor().getCards();
        else
            return game.getDevelopmentCardsVendor().getCards();
    }

    private static boolean[] getMarketChoices() {
        if (local)
            return gameSRV.getMarketHelper().getChoices();
        else
            return game.getMarketHelper().getChoices();
    }


    public static void printHelpMenu() {
        System.out.println(" >> List of commands! \n \n");
        System.out.println("=> " + A.CYAN + "quit" + A.RESET + "                            : kills the thread and exits the program");
        System.out.println("=> " + A.CYAN + "help" + A.RESET + "                            : displays the possible terminal commands");
        System.out.println("=> " + A.CYAN + "set" + A.RESET + " " + A.PURPLE + "<something> <value>" + A.RESET + "         : sets a new default port or address");
        System.out.println("                                     default values: " + A.GREEN + Halo.defaultAddress + ":" + Halo.defaultPort + A.RESET);
        System.out.println(A.PURPLE + "something" + A.RESET + " :>  'port'");
        System.out.println("          :>  'address' ");
        System.out.println("=> " + A.CYAN + "c" + A.RESET + " " + A.PURPLE + "<nickname> <capacity>" + A.RESET + "         : quickly creates a new lobby using default values");
        System.out.println("=> " + A.CYAN + "j" + A.RESET + " " + A.PURPLE + "<nickname> <lobbyNumber>" + A.RESET + "      : quickly joins a lobby using default values");
        System.out.println("=> " + A.CYAN + "create" + A.RESET + " " + A.PURPLE + "<ip> <port> <nickname> <capacity>" + A.RESET + "  ");
        System.out.println("                                   : creates a new lobby using custom port number \n" +
                "                                     and address values");
        System.out.println("=> " + A.CYAN + "join" + A.RESET + " " + A.PURPLE + "<ip> <port> <nickname> <lobbyNumber>" + A.RESET + "  ");
        System.out.println("                                   : joins an existing lobby using custom port number \n" +
                "                                     and address values");
        System.out.println("=> " + A.CYAN + "rejoin" + A.RESET + " " + A.PURPLE + "<ip> <port> <nickname> <lobbyNumber>" + A.RESET + "  ");
        System.out.println("                                   : gives the ability to reconnect to a lobby,  \n" +
                "                                     if connection was lost. Nickname must match \n" +
                "                                     the previously used one. ");
    }

    public static void printActions() {
        System.out.println(A.CYAN + "  LIST OF ACTIONS! " + A.RESET);
        System.out.println("  =>   0   : go back");
        System.out.println("  =>   1   : activate a leader card");
        System.out.println("  =>   2   : discard a leader card");
        System.out.println("  =>   3   : activate production");
        System.out.println("  =>   4   : change depot configuration");
        System.out.println("  =>   5   : buy development card");
        System.out.println("  =>   6   : get market resources");
        System.out.println("  =>   7   : end turn");
    }

    public static void printHelpGame() {
        System.out.println("  List of commands! \n \n");
        System.out.println("=> " + A.CYAN + "quit" + A.RESET + "                             : kills the thread and exits the program");
        System.out.println("=> " + A.CYAN + "help" + A.RESET + "                            : displays the possible terminal commands");
        System.out.println("=> " + A.CYAN + "show " + A.PURPLE + "<something>" + A.RESET + "                 : shows one of my Assets");
        System.out.println("=> " + A.CYAN + "action" + A.RESET + "                          : enables the action routine, if possible");
        System.out.println(A.PURPLE + "something" + A.RESET + " :>  'leaderCards'");
        System.out.println("          :>  'players' ");
        System.out.println("          :>  'market' ");
        System.out.println("          :>  'depot' ");
        System.out.println("          :>  'strongbox' ");
        System.out.println("          :>  'devslot' ");
        System.out.println("          :>  'devdeck' ");
        System.out.println("          :>  'faithtrack' ");
        System.out.println("          :>  'myvp' ");
        System.out.println("          :>  'turn' ");
        System.out.println("=> " + A.CYAN + "show" + A.PURPLE + " <nickname> <something>" + A.RESET + "     : shows one of the other players' assets.\n" +
                "                                     Specify the nickname of the player.        ");
        System.out.println("=> " + A.CYAN + "show player " + A.PURPLE + "<num> <something>" + A.RESET + "   : shows one of the other players' assets.\n" +
                "                                     Specify the player's number. ");
        System.out.println(A.PURPLE + "num" + A.RESET + "       :>  1, .. , maxLobbySize ");
        System.out.println(A.PURPLE + "something" + A.RESET + " :>  'leaderCards'");
        System.out.println("          :>  'vp'");
        System.out.println("          :>  'depot'");
        System.out.println("          :>  'strongbox'");
        System.out.println("          :>  'devslot'");
    }

    public static String printMarketHelper() {
        if (local)
            return gameSRV.getMarketHelper().toString();
        else
            return game.getMarketHelper().toString();
    }

    public static String printDevelopmentCardsVendor() {
        if (local)
            return gameSRV.getDevelopmentCardsVendor().toString();
        else
            return game.getDevelopmentCardsVendor().toString();
    }

    public static String printMarket() {
        if (Halo.local)
            return gameSRV.getMarket().toString();
        else
            return game.getMarket().toString();
    }

    public static String printMyWarehouseDepot() {
        if (local)
            return myPlayerRefSRV.getWarehouseDepot().toString();
        else
            return myPlayerRef.getWarehouseDepot().toString();
    }

    public static String printMyStrongbox() {
        if (local)
            return myPlayerRefSRV.getStrongbox().toString();
        else
            return myPlayerRef.getStrongbox().toString();
    }

    public static String printMyDevelopmentSlot() {
        if (local)
            return myPlayerRefSRV.getDevelopmentSlot().toString();
        else
            return myPlayerRef.getDevelopmentSlot().toString();
    }

    public static String printMyVP() {
        if (local)
            return " my VP : " + myPlayerRefSRV.getVp();
        else
            return " my VP : " + myPlayerRef.getVp();
    }

    public static String printPlayers() {
        StringBuilder result = new StringBuilder();
        result.append(A.GREEN + " > showing players  name - number" + A.RESET);
        if (local) {
            for (Player p : Halo.gameSRV.getPlayerList())
                result.append("  ").append(p.getNickname()).append(" - ").append(p.getPlayerNumber());
        } else {
            for (PlayerSimplified p : Halo.game.getPlayerSimplifiedList())
                result.append("  ").append(p.getNickname()).append(" - ").append(p.getPlayerNumber());
        }
        return result.toString();
    }

    public static String printDevelopmentCardsDeck() {
        if (local)
            return gameSRV.getDevelopmentCardsDeck().toString();
        else
            return game.getDevDeck().toString();
    }

    public static String printFaithTrack() {
        if (local)
            return gameSRV.getFaithTrack().toString(solo);
        else
            return game.getFaithTrack().toString(solo);
    }

    public static String printCurrentTurn() {
        if (local)
            return " current turn is : " + gameSRV.getTurn();
        else
            return " current turn is : " + game.getTurn();
    }

    public static String printMyLeaderCards() {
        LeaderCard[] cards = getBothMyLeaderCard();
        String result = "";
        if (cards[0] != null) {
            result += " Leader Card #1: \n" + cards[0];
        } else
            result += " Leader Card #1: none";
        if (cards[1] != null) {
            result += " Leader Card #2: \n" + cards[1];
        } else
            result += " Leader Card #2: none";
        return result;
    }

    public static void showMy(String key) {
        switch (key.toLowerCase()) {
            case "players":
                System.out.println(Halo.printPlayers());
                break;
            case "market":
                System.out.println(Halo.printMarket());
                break;
            case "depot":
                System.out.println(Halo.printMyWarehouseDepot());
                break;
            case "strongbox":
                System.out.println(Halo.printMyStrongbox());
                break;
            case "devslot":
                System.out.println(Halo.printMyDevelopmentSlot());
                break;
            case "devdeck":
                System.out.println(Halo.printDevelopmentCardsDeck());
                break;
            case "faithtrack":
                System.out.println(Halo.printFaithTrack());
                break;
            case "myvp":
                System.out.println(Halo.printMyVP());
                break;
            case "turn":
                System.out.println(Halo.printCurrentTurn());
                break;
            case "leadercards":
                System.out.println(Halo.printMyLeaderCards());
                break;
            default:
                System.out.println(A.RED + " Somehow I reached this default. Debug ." + A.RESET);
                System.out.println(key);
        }
    }

    private static Resource convertStringToResource(String resource) {
        if (resource.equals("servant")) return Resource.SERVANT;
        if (resource.equals("coin")) return Resource.COIN;
        if (resource.equals("shield")) return Resource.SHIELD;
        if (resource.equals("stone")) return Resource.STONE;
        if (resource.equals("none")) return Resource.NONE;
        return null;
    }

    public static boolean checkAction(List<String> textList) {
        if (textList.size() > 1) {
            System.out.println(A.RED + " > Please insert just a number" + A.RESET);
            return false;
        }
        try {
            int number = Integer.parseInt(textList.get(0));
            if (number < 0 || number > 8) {
                System.out.println(A.RED + " > Please pick a number between 0 and 7" + A.RESET);
                return false;
            }
//check very powerful action    already playedE
            if (Halo.action && (number == 3 || number == 5 || number == 6)) {
                System.out.println(A.RED + " > You already did a main move" + A.RESET);
                return false;
            }

            LeaderCard l1 = getMyLeaderCard(0);
            LeaderCard l2 = getMyLeaderCard(1);

//check action 1 and 2
            if (number == 1 || number == 2) {
                if (l1 == null && l2 == null) {
                    System.out.println(A.RED + " > You can't, because the cards are absent " + A.RESET);
                    return false;
                }
                if (l1 != null) {
                    if (l2 != null) {
                        if (l1.getEnable() && l2.getEnable()) {
                            System.out.println(A.RED + " > You can't, because the cards are already activated " + A.RESET);
                            return false;
                        }
                    } else {
                        if (l1.getEnable()) {
                            System.out.println(A.RED + " > You can't, because the first card is already enabled and the second one is absent " + A.RESET);
                            return false;
                        }
                    }
                } else {
                    if (l2.getEnable()) {
                        System.out.println(A.RED + " > You can't, the first card is absent and the second one is already enabled " + A.RESET);
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
            System.out.println(A.RED + " > Sorry, but that was not a number" + A.RESET);
            return false;
        }

        return true;
    }

    public static boolean checkShowCommand(List<String> textList) {
        String[] allowedKeySoloArr = {"players", "market", "depot", "strongbox", "devslot", "devdeck", "faithtrack", "myvp", "leadercards", "turn"};
        List<String> allowedKeySolo = new ArrayList<String>(Arrays.asList(allowedKeySoloArr));
        String[] allowedKeyArr = {"depot", "strongbox", "devslot", "vp", "leadercards"};
        List<String> allowedKey = new ArrayList<String>(Arrays.asList(allowedKeyArr));

        if (local) {
            if (textList.size() != 2) {
                System.out.println(A.RED + " > Error! You can only show your things!" + A.RESET);
                return false;
            }
        } else {
            if (textList.size() != 4 && textList.size() != 2 && textList.size() != 3) {
                System.out.println(A.RED + " > Error! The number of parameters is incorrect" + A.RESET);
                return false;
            }
        }

        if (textList.size() == 2) {
            if (allowedKeySolo.stream().anyMatch(s -> s.equalsIgnoreCase(textList.get(1))))
                return true;
            System.out.println(A.RED + " > Error! You can't show this. I don't even know what 'this' is!" + A.RESET);
            return false;
        }
        if (textList.size() == 4) {
            if (!textList.get(1).equalsIgnoreCase("player")) {
                System.out.println(A.RED + " > Sorry, but there should be more parameters." + A.RESET);
                return false;
            }
            //else
            try {
                if (Integer.parseInt(textList.get(2)) < 1) {
                    System.out.println(A.RED + " > Sorry, the player number is below the minimum." + A.RESET);
                    return false;
                }
                if (Integer.parseInt(textList.get(2)) > Halo.game.getPlayerSimplifiedList().size()) {
                    System.out.println(A.RED + " > Sorry, the player number is above the maximum." + A.RESET);
                    return false;
                }

                if (allowedKey.stream().anyMatch(s -> s.equalsIgnoreCase(textList.get(3))))
                    return true;
                System.out.println(A.RED + " > Error! You can't show this." + A.RESET);
                return false;
            } catch (NumberFormatException e) {
                System.out.println(A.RED + " > Sorry, the player number is not a number." + A.RESET);
                return false;
            }
        }
        if (allowedKey.stream().anyMatch(s -> s.equalsIgnoreCase(textList.get(2))))
            System.out.println(A.RED + " > Error! You can't show this. I don't even know what 'this' is!" + A.RESET);
        return false;
    }

    private static boolean checkNumber0_1_2(List<String> textList) {
        if (textList.size() == 1) {
            try {
                int number = Integer.parseInt(textList.get(0));
                if (number != 0 && number != 1 && number != 2) {
                    System.out.println(" > Please insert 0, 1 or 2");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println(" > That was not a number");
                return false;
            }
        } else {
            System.out.println(" > Too many parameters");
            return false;
        }
        return true;
    }

    private static boolean checkNumberN1_0_1_2(List<String> textList) {
        if (textList.size() == 1) {
            try {
                int number = Integer.parseInt(textList.get(0));
                if (number != 0 && number != 1 && number != 2 && number != -1) {
                    System.out.println(" > Please insert 0, 1 or 2");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println(" > That was not a number");
                return false;
            }
        } else {
            System.out.println(" > Too many parameters");
            return false;
        }
        return true;
    }

    private static boolean checkNumber1_2_3_4(List<String> textList) {
        if (textList.size() > 1) {
            System.out.println(A.RED + " > Please insert just a number" + A.RESET);
            return false;
        }
        try {
            int number = Integer.parseInt(textList.get(0));
            if (number < 1 || number > 4) {
                System.out.println(A.RED + " > Sorry, you have to choose between 1 and 4." + A.RESET);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(A.RED + " > Sorry, but that was not a number" + A.RESET);
            return false;
        }
        return true;
    }

    private static boolean checkLeaderCards() {
        LeaderCard l1 = getMyLeaderCard(0);
        LeaderCard l2 = getMyLeaderCard(1);

        if (l1 == null && l2 == null) {
            System.out.println(A.RED + " > Both cards are discarded!" + A.RESET);
            return false;
        }
        if (l1 != null) {
            if (l2 != null) {
                if (l1.getEnable() && l2.getEnable()) {
                    System.out.println(A.RED + " > Both cards are already activated!" + A.RESET);
                    return false;
                }
            } else {
                if (l1.getEnable()) {
                    System.out.println(A.RED + " > The only card present, the first, is already activated!" + A.RESET);
                    return false;
                }
            }
        } else {
            if (l2.getEnable()) {
                System.out.println(A.RED + " > The only card present, the second, is already activated!" + A.RESET);
                return false;
            }
        }
        return true;
    }

    private static boolean checkLeaderCardsNumber(List<String> textList) {
        if (textList.size() > 1) {
            System.out.println(A.RED + " > Please insert just a number" + A.RESET);
            return false;
        }
        try {
            int number = Integer.parseInt(textList.get(0));
            LeaderCard l1 = getMyLeaderCard(0);
            LeaderCard l2 = getMyLeaderCard(1);
            if (number < 0 || number > 2) {
                System.out.println(A.RED + " > Please choose 0, 1 or 2" + A.RESET);
                return false;
            }
            if (number == 1) {
                if (l1 == null) {
                    System.out.println(A.RED + " > Sorry, but that card is discarded" + A.RESET);
                    return false;
                } else if (l1.getEnable()) {
                    System.out.println(A.RED + " > Sorry, but that card is already activated" + A.RESET);
                    return false;
                }
            }
            if (number == 2) {
                if (l2 == null) {
                    System.out.println(A.RED + " > Sorry, but that card is discarded" + A.RESET);
                    return false;
                } else if (l2.getEnable()) {
                    System.out.println(A.RED + " > Sorry, but that card is already activated" + A.RESET);
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(A.RED + " > Sorry, but that was not a number" + A.RESET);
            return false;
        }

        return true;
    }

    private static boolean checkNumberDevSlot(List<String> textList) {
        if (textList.size() == 2) {
            try {
                int cardNum = Integer.parseInt(textList.get(0));
                int slotNum = Integer.parseInt(textList.get(1));
                Map<DevelopmentCard, boolean[]> cards;

                cards = Halo.getVendorCards();

                if (cardNum > cards.size() || cardNum < 1) {
                    System.out.println(A.RED + " > That's not a possible card!" + A.RESET);
                    return false;
                }
                if (slotNum < 0 || slotNum > 2) {
                    System.out.println(A.RED + " > That's not a proper slot!" + A.RESET);
                    return false;
                }
                Object[] dcards = cards.keySet().toArray();
                if (!cards.get((DevelopmentCard) dcards[cardNum - 1])[slotNum - 1]) {
                    System.out.println(A.RED + " > Error! You can't place that card in this slot" + A.RESET);
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println(A.RED + " > Sorry, that was not a number" + A.RESET);
                return false;
            }
        } else if (textList.size() == 1) {
            try {
                int quit = Integer.parseInt(textList.get(0));
                if (quit != 0) {
                    System.out.println("Invalid input, try again");
                    return false;
                }
                return true;
            } catch (NumberFormatException e) {
                System.out.println(A.RED + " > Sorry, that was not a number" + A.RESET);
                return false;
            }
        } else {
            System.out.println(A.RED + " > The number of parameters is different then expected." + A.RESET);
            return false;
        }
        return true;
    }

    private static boolean checkChoice(List<String> textList) {
        if (textList.size() > 1) {
            System.out.println(A.RED + " > Please insert just a number" + A.RESET);
            return false;
        }
        boolean[] choices = Halo.getMarketChoices();

        try {
            int number = Integer.parseInt(textList.get(0));
            if (number < 0 || number > 7)
                System.out.println(A.RED + " > Number must be between 0 and 7" + A.RESET);
            else if (!choices[number]) {
                System.out.println(A.RED + " > That wasn't a possible choice" + A.RESET);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(A.RED + " > Sorry, but that was not a number" + A.RESET);
            return false;
        }
        return true;
    }

    private static boolean checkLeaderCardObjectNumber(List<String> textList, int first) {
        if (textList.size() > 1) {
            System.out.println(A.RED + " > Please insert just a number" + A.RESET);
            return false;
        }
        try {
            int number = Integer.parseInt(textList.get(0));
            if (number < 1 || number > 4) {
                System.out.println(A.RED + " > Sorry, the player number must be between 1 and 4." + A.RESET);
                return false;
            }
            if (number == first) {
                System.out.println(A.RED + " > Sorry, but you inserted the same number as before." + A.RESET);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(A.RED + " > Sorry, but that was not a number" + A.RESET);
            return false;
        }
        return true;
    }

    private static boolean checkResource(List<String> textList, int numberOfResources, boolean resourceNonePermitted) {
        if (textList.size() != numberOfResources) return false;
        for (String resource : textList) {
            if ((resourceNonePermitted) && !(resource.equalsIgnoreCase("stone") || resource.equalsIgnoreCase("coin") || resource.equalsIgnoreCase("shield") || resource.equalsIgnoreCase("servant") || resource.equalsIgnoreCase("none")))
                return false;
            if (!resourceNonePermitted && !(resource.equalsIgnoreCase("stone") || resource.equalsIgnoreCase("coin") || resource.equalsIgnoreCase("shield") || resource.equalsIgnoreCase("servant")))
                return false;
        }
        return true;
    }

    private static boolean checkNumberMarket(List<String> textList, boolean column) {
        if (textList.size() > 1) {
            System.out.println(" > Too many parameters");
            return false;
        }
        try {
            int number = Integer.parseInt(textList.get(0));
            if (column) {
                if (number < 0 || number > 4) {
                    System.out.println(" > The column number must be 1, 2, 3, 4. Or 0 to quit.");
                    return false;
                }
            } else {
                if (number < 0 || number > 3) {
                    System.out.println(" > The row number must be 1, 2, 3. Or 0 to quit.");
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(" > That was not a number");
            return false;
        }
        return true;
    }

    private static boolean checkStandardProductionInput(List<String> input) {
        int n1;
        int n2 = -1;
        int n3;
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

    //MESSAGE GENERATORS
    public static MSG_ACTION_GET_MARKET_RESOURCES getMarketResources(List<String> textList) {
        String text;
        Halo.triedAction = true;
        int num;
        boolean column;
        System.out.println(" Here's the market, if this is not qol I don't know what could be then:");
        System.out.println(printMarket());
        System.out.println(A.UL + " Please choose Row or Column" + A.RESET);
        System.out.println(" Insert 1 for a row or 2 for a column, 0 to quit");

        while (true) {
            System.out.print(" Row ( 1 ) or column ( 2 ): ");
            text = Halo.input.nextLine();
            textList.clear();
            textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

            if (Halo.checkNumber0_1_2(textList)) {
                num = Integer.parseInt(textList.get(0));
                if (num == 0) {
                    System.out.println(A.GREEN + " > Going back to main menu" + A.RESET);
                    return null;
                }
                break;
            }
        }
        if (num == 1) {
            column = false;
            System.out.println(A.UL + " > Please choose the row " + A.RESET + "(must be between 1 and 3). 0 to quit");
        } else {
            System.out.println(A.UL + " > Please choose the column " + A.RESET + "(must be between 1 and 4). 0 to quit");
            column = true;
        }

        while (true) {
            text = Halo.input.nextLine();
            textList.clear();
            textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
            if (Halo.checkNumberMarket(textList, column)) {
                num = Integer.parseInt(textList.get(0));
                if (num == 0) {
                    System.out.println(A.GREEN + " > Going back to main menu" + A.RESET);
                    return null;
                }
                break;
            }
        }
        try {
            return new MSG_ACTION_GET_MARKET_RESOURCES(column, num);
        } catch (IllegalArgumentException e) {
            System.out.println(A.RED + " > We could not build that message" + A.RESET);
            return null;
        }
    }

    public static MSG_ACTION_CHANGE_DEPOT_CONFIG changeDepotConfig(List<String> textList) {
        Halo.triedAction = false;
        String text;
        //change depot
        System.out.println(A.UL + " >> Please insert the new configuration for your Warehouse Depot. Write'm as: stone none shield" + A.RESET);
        System.out.println(" Your actual depot: ");
        System.out.println(printMyWarehouseDepot());

        LeaderCard l1 = getMyLeaderCard(0);
        LeaderCard l2 = getMyLeaderCard(1);

        if (l1 != null && l1.getEnable() && l1.getSpecialAbility().isExtraDepot()) {
            System.out.println(" Extra depot of Card Number 1: " + l1.getSpecialAbility());
        }
        if (l2 != null && l2.getEnable() && l2.getSpecialAbility().isExtraDepot()) {
            System.out.println(" Extra depot of Card Number 2: " + l2.getSpecialAbility());
        }

        Resource shelf1;
        Resource[] shelf2 = new Resource[2];
        Resource[] shelf3 = new Resource[3];
        while (true) {
            System.out.print(" Shelf 1: ");
            text = Halo.input.nextLine();
            textList.clear();
            textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
            if (!checkResource(textList, 1, true))
                System.out.println(A.RED + " > Ohoh my friend, that's not possible in shelf 1. Try again." + A.RESET);
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
                System.out.println(A.RED + " > Ohoh my friend, that's not possible in shelf 2. Try again." + A.RESET);
            else {
                shelf2[0] = convertStringToResource(textList.get(0).toLowerCase());
                shelf2[1] = convertStringToResource(textList.get(1).toLowerCase());
                break;
            }
        }

        while (true) {
            System.out.print(" Shelf 3: ");
            text = input.nextLine();
            textList.clear();
            textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
            if (!Halo.checkResource(textList, 3, true))
                System.out.println(A.RED + " > Ohoh my friend, that's not possible in shelf 3. Try again." + A.RESET);
            else {
                shelf3[0] = convertStringToResource(textList.get(0).toLowerCase());
                shelf3[1] = convertStringToResource(textList.get(1).toLowerCase());
                shelf3[2] = convertStringToResource(textList.get(2).toLowerCase());
                break;
            }
        }

        int firstExtra = -1;
        int secondExtra = -1;


        if (l1 != null && l1.getEnable() && l1.getSpecialAbility().isExtraDepot()) {
            System.out.println(A.UL + " > What about the extra depot for the resource " + ((ExtraDepot) l1.getSpecialAbility()).getResourceType() + "." +
                    " \n Please tell me how much I have to Phil Heath." + A.RESET + " 0-1 to quit");
            while (true) {
                System.out.print(" Number of Resources: ");
                text = Halo.input.nextLine();
                textList.clear();
                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                if (checkNumberN1_0_1_2(textList)) {
                    firstExtra = Integer.parseInt(textList.get(0));
                    if (firstExtra == -1) {
                        System.out.println(A.GREEN + " > Going back to main menu" + A.RESET);
                        return null;
                    }
                    break;
                }
            }

        }
        if (l2 != null && l2.getEnable() && l2.getSpecialAbility().isExtraDepot()) {
            System.out.println(A.UL + " > What about the extra depot for the resource " + ((ExtraDepot) l2.getSpecialAbility()).getResourceType() + "." +
                    " \n Please tell me how much I have to Phil Heath" + A.RESET + " -1 to quit");
            while (true) {
                System.out.print(" Number of Resources: ");
                text = Halo.input.nextLine();
                textList.clear();
                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                if (checkNumberN1_0_1_2(textList)) {
                    secondExtra = Integer.parseInt(textList.get(0));
                    if (secondExtra == -1) {
                        System.out.println(A.GREEN + " > Going back to main menu" + A.RESET);
                        return null;
                    }
                    break;
                }
            }
        }

        try {
            return new MSG_ACTION_CHANGE_DEPOT_CONFIG(shelf1, shelf2, shelf3, firstExtra, secondExtra);
        } catch (IllegalArgumentException e) {
            System.out.println(A.RED + " We couldn't build the message like that." + A.RESET);
            return null;
        }

    }

    public static MSG_ACTION_ACTIVATE_PRODUCTION activateProduction(List<String> textList) {
        Halo.triedAction = true;
        String text;
        //production //all the system out must be reviewed by a standard that only stefano knows
        boolean basic = false;
        ArrayList<Resource> basicInput = new ArrayList<>();
        Resource basicOutput = null;
        boolean[] leader = {false, false};
        Resource leaderOutput1 = null;
        Resource leaderOutput2 = null;
        boolean std = false;
        boolean[] standard = {false, false, false};
        int input;
        LeaderCard l1 = getMyLeaderCard(0);
        LeaderCard l2 = getMyLeaderCard(1);

        System.out.println(" Here are your depot and strongbox");

        System.out.println(Halo.printMyWarehouseDepot());
        System.out.println(Halo.printMyStrongbox());
        if (l1 != null && l1.getEnable() && l1.getSpecialAbility().isExtraDepot()) {
            System.out.println(" Extra depot of Card Number 1: " + l1.getSpecialAbility());
        }
        if (l2 != null && l2.getEnable() && l2.getSpecialAbility().isExtraDepot()) {
            System.out.println(" Extra depot of Card Number 2: " + l2.getSpecialAbility());
        }

        System.out.println(" >> BASE PRODUCTION ");
        System.out.println(A.UL + " Do you want to activate it? " + A.RESET);
        System.out.println(" Press 0 to cancel action, 1 for yes, 2 for no");
        while (true) {
            System.out.print(" will: ");
            text = Halo.input.nextLine();
            textList.clear();
            textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
            if (checkNumber0_1_2(textList)) {
                input = Integer.parseInt(textList.get(0));
                if (input == 0) {
                    System.out.println(A.GREEN + " > Going back to main menu" + A.RESET);
                    return null;
                }
                if (input == 1)
                    basic = true;
                break;
            }
        }

        if (basic) {
            System.out.println(A.UL + " >> Which resources do you want as input for the basic production? Write'em as: stone shield" + A.RESET);
            while (true) {
                System.out.print(" will: ");
                text = Halo.input.nextLine();
                textList.clear();
                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                if (!checkResource(textList, 2, false))
                    System.out.println(A.RED + " > They are not a valid basic input!" + A.RESET);
                else {
                    basicInput.add(convertStringToResource(textList.get(0)));
                    basicInput.add(convertStringToResource(textList.get(1)));
                    break;
                }
            }
            System.out.println(A.UL + " >> Which resource do you want as output for the basic production? Write it as: stone" + A.RESET);
            while (true) {
                System.out.print(" will: ");
                text = Halo.input.nextLine();
                textList.clear();
                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                if (!checkResource(textList, 1, false))
                    System.out.println(A.RED + " > That's not a valid basic output!" + A.RESET);
                else {
                    basicOutput = Halo.convertStringToResource(textList.get(0));
                    break;
                }
            }
        }

        System.out.println(" >> STANDARD PRODUCTION ");
        System.out.println(A.UL + " Do you want to activate it? " + A.RESET);
        System.out.println(" Press 0 to cancel action, 1 for yes, 2 for no");
        while (true) {
            System.out.print(" will: ");
            text = Halo.input.nextLine();
            textList.clear();
            textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
            if (Halo.checkNumber0_1_2(textList)) {
                input = Integer.parseInt(textList.get(0));
                if (input == 0) {
                    System.out.println(A.GREEN + " > Going back to main menu" + A.RESET);
                    return null;
                }
                if (input == 1)
                    std = true;
                break;
            }
        }
        if (std) {
            System.out.println(printMyDevelopmentSlot());
            System.out.println(A.UL + " Which cards do you want to activate? write the numbers as: 1 2 3" + A.RESET);
            while (true) {
                System.out.print(" will: ");
                text = Halo.input.nextLine();
                textList.clear();
                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                if (Halo.checkStandardProductionInput(textList)) {
                    for (String s : textList) {
                        standard[Integer.parseInt(s) - 1] = true;
                    }
                    break;
                }
            }
        }
        if ((l1 != null && l1.getEnable()) || (l2 != null && l2.getEnable())) {
            if (Arrays.stream(getBothMyLeaderCard()).anyMatch(l -> l.getSpecialAbility().isProduction())) {
                System.out.println(" >> LEADER PRODUCTION");
                for (int i = 0; i < 2; i++) {
                    LeaderCard l = getMyLeaderCard(i);
                    if (l != null && l.getEnable() && l.getSpecialAbility().isProduction()) {
                        System.out.println(l);
                        System.out.println(A.UL + " Do you want to activate this card? " + A.RESET);
                        System.out.println(" Press 0 to cancel action, 1 for yes, 2 for no");
                        while (true) {
                            System.out.print(" will: ");
                            text = Halo.input.nextLine();
                            textList.clear();
                            textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                            if (checkNumber0_1_2(textList)) {
                                int number = Integer.parseInt(textList.get(0));
                                if (number == 0) {
                                    System.out.println(A.GREEN + " > Going back to main menu" + A.RESET);
                                    return null;
                                }
                                if (number == 1)
                                    leader[i] = true;
                                break;
                            } else
                                System.out.println(A.RED + " > Invalid input, try again" + A.RESET);
                        }
                    }
                }
            }
        }


        if (leader[0]) {
            System.out.println(A.UL + " Which resource do you want to produce with your leaderCard 1, along with a " + FAITH + A.UL + " point (input is" +
                    ((Production) l1.getSpecialAbility()).getInput() + ")?" + A.RESET);
            while (true) {
                System.out.print(" will: ");
                text = Halo.input.nextLine();
                textList.clear();
                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                if (checkResource(textList, 1, false)) {
                    leaderOutput1 = convertStringToResource(textList.get(0));
                    break;
                } else
                    System.out.println(A.RED + "Invalid input, try again" + A.RESET);
            }
        }

        if (leader[1]) {
            System.out.println(A.UL + " Which resource do you want to produce with your leaderCard 2, along with a " + FAITH + A.UL + " point (input is" +
                    ((Production) l2.getSpecialAbility()).getInput() + ")?" + A.RESET);
            while (true) {
                System.out.print(" will: ");
                text = Halo.input.nextLine();
                textList.clear();
                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
                if (Halo.checkResource(textList, 1, false)) {
                    leaderOutput2 = Halo.convertStringToResource(textList.get(0));
                    break;
                } else
                    System.out.println(A.RED + " > Invalid input, try again" + A.RESET);
            }
        }

        if (basic || std || leader[0] || leader[1]) {
            try {
                return new MSG_ACTION_ACTIVATE_PRODUCTION(standard, basic, leader, basicInput, basicOutput, leaderOutput1, leaderOutput2);

            } catch (IllegalArgumentException e) {
                System.out.println(A.RED + " We couldn't build the message like that." + A.RESET);
                return null;
            }
        } else {
            System.out.println(A.RED + " Produce you do not, result you do not get" + A.RESET);
            return null;
        }
    }

    public static MSG_ACTION_DISCARD_LEADERCARD discardLeaderCard(List<String> textList) {
        Halo.triedAction = false;
        int cardToDiscard;
        String text;
        LeaderCard l1 = getMyLeaderCard(0);
        LeaderCard l2 = getMyLeaderCard(1);

        if (Halo.checkLeaderCards()) {
            System.out.print(A.UL + " Please pick a number: " + A.RESET);
            System.out.println(" Press 0 to cancel the action ");
            if (l1 != null)
                System.out.println(" Press 1 to disable the first card and get a " + FAITH + " point ");
            if (l2 != null)
                System.out.println(" Press 2 to disable the second card and get a " + FAITH + " point");

            while (true) {
                System.out.print(" Card number: ");
                text = Halo.input.nextLine();
                textList.clear();
                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                if (checkLeaderCardsNumber(textList)) {
                    cardToDiscard = Integer.parseInt(textList.get(0));
                    if (cardToDiscard == 0) {
                        System.out.println(A.GREEN + " > Going back to main menu" + A.RESET);
                        return null;
                    }
                    break;
                }
            }
        } else return null;

        try {
            return new MSG_ACTION_DISCARD_LEADERCARD(cardToDiscard);
        } catch (IllegalArgumentException e) {
            System.out.println(A.RED + " > We could not build that message, please debug me: " + A.RESET);
            return null;
        }
    }

    public static MSG_ACTION_ACTIVATE_LEADERCARD activateLeaderCard(List<String> textList) {
        Halo.triedAction = false;

        String text;
        int cardToActivate;
        LeaderCard l1 = getMyLeaderCard(0);
        LeaderCard l2 = getMyLeaderCard(1);

        if (checkLeaderCards()) {
            System.out.println(A.UL + " Please select an option: " + A.RESET);
            System.out.println(" Press 0 to cancel the action ");
            if (l1 != null)
                System.out.println(" Press 1 to enable the first card ");
            if (l2 != null)
                System.out.println(" Press 2 to enable the second card ");

            while (true) {
                System.out.print(" Card number: ");
                text = Halo.input.nextLine();
                textList.clear();
                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                if (checkLeaderCardsNumber(textList)) {
                    cardToActivate = Integer.parseInt(textList.get(0));
                    if (cardToActivate == 0) {
                        System.out.println(A.GREEN + " > Going back to main menu" + A.RESET);
                        return null;
                    }
                    break;
                }
            }
        } else return null;

        try {
            return new MSG_ACTION_ACTIVATE_LEADERCARD(cardToActivate);
        } catch (IllegalArgumentException e) {
            System.out.println(A.RED + " > We could not build that message, please debug me: " + A.RESET);
            return null;
        }
    }

    public static MSG_ACTION_CHOOSE_DEVELOPMENT_CARD chooseDevCard(List<String> textList) {
        boolean quit = false;
        String text;
        int cardNum = -1;
        int slotNum = -1;
        while (true) {
            if (checkNumberDevSlot(textList)) {
                if (textList.size() == 1) quit = true;
                else {
                    cardNum = Integer.parseInt(textList.get(0));
                    slotNum = Integer.parseInt(textList.get(1));
                }
                break;
            } else {
                System.out.print(" Card | slot: (0 to quit the action) ");
                text = Halo.input.nextLine();
                textList.clear();
                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
            }
        }
        try {
            MSG_ACTION_CHOOSE_DEVELOPMENT_CARD msgToSend;
            if (quit) {
                Halo.triedAction = false;
                msgToSend = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(-1, -1);
            } else {
                Halo.triedAction = true;
                msgToSend = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(cardNum - 1, slotNum - 1);
            }
            return msgToSend;
        } catch (IllegalArgumentException e) {
            System.out.println(A.RED + " > We could not build that message, please debug me: " + A.RESET);
            return null;
        }
    }

    public static MSG_ACTION_MARKET_CHOICE marketChoice(List<String> textList) {
        int choice;
        String text;

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

        try {
            return new MSG_ACTION_MARKET_CHOICE(choice);
        } catch (IllegalArgumentException e) {
            System.out.println(A.RED + " > We could not build that message, please debug me: " + A.RESET);
            return null;
        }
    }

    public static MSG_INIT_CHOOSE_RESOURCE chooseResource(List<String> textList) {
        Resource resource = Resource.NONE;
        String text;
        while (true) {
            if (checkNumber1_2_3_4(textList)) {
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
                System.out.println(A.GREEN + " > Thanks, you choose a " + resource + A.RESET);
                break;
            } else {
                System.out.print(" Resource number: ");
                text = Halo.input.nextLine();
                textList.clear();
                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));
            }
        }
        try {
            return new MSG_INIT_CHOOSE_RESOURCE(resource);
        } catch (IllegalArgumentException e) {
            System.out.println(A.RED + " > We could not build that message, please debug me: " + A.RESET);
            return null;
        }
    }

    public static MSG_INIT_CHOOSE_LEADERCARDS chooseLeaderCards(List<String> textList) {
        int first;
        int second;
        String text;
        obtainNumberLoop:
        while (true) {
            if (checkNumber1_2_3_4(textList)) {
                first = Integer.parseInt(textList.get(0));

                while (true) {
                    System.out.println(A.UL + " > Please pick the second card: " + A.RESET);
                    System.out.print(" Card number: ");
                    text = Halo.input.nextLine();
                    textList.clear();
                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                    if (checkLeaderCardObjectNumber(textList, first)) {
                        second = Integer.parseInt(textList.get(0));
                        System.out.println(A.GREEN + " > Thanks, you choose " + A.RESET + first + A.GREEN + " and " + A.RESET + second);
                        break obtainNumberLoop;
                    }
                }
            }
        }

        try {
            return new MSG_INIT_CHOOSE_LEADERCARDS(first - 1, second - 1);
        } catch (IllegalArgumentException e) {
            System.out.println(A.RED + " > We could not build that message, please debug me: " + A.RESET);
            return null;
        }
    }


    //ONLY MULTIPLAYER METHODS
    public static void showPlayerAsset(String key, int playerNumber) {
        PlayerSimplified player = game.getPlayerRef(playerNumber);
        if (player == null)
            System.out.println(A.RED + " > There's no such player with that player number. " + A.RESET);
        else
            showPlayerAsset(key, player);

    }

    public static void showPlayerAsset(String key, String nickname) {
        PlayerSimplified player = game.getPlayerRef(nickname);
        if (player == null)
            System.out.println(A.RED + " > There's no such player with that name. " + A.RESET);
        else
            showPlayerAsset(key, player);
    }

    private static void showPlayerAsset(String key, PlayerSimplified player) {
        switch (key.toLowerCase()) {
            case "vp":
                System.out.println(" his/her VP : " + player.getVp());
                break;
            case "leadercards":
                LeaderCard[] cards = player.getLeaderCards();
                if (cards[0] != null) {
                    if (cards[0].getEnable())
                        System.out.println(" Leader Card #1: \n" + cards[0].toString());
                    else
                        System.out.println(" Leader Card #1: covered");
                } else
                    System.out.println(" Leader Card #1: none");
                if (cards[1] != null) {
                    if (cards[1].getEnable())
                        System.out.println(" Leader Card #2: \n" + cards[1].toString());
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
                System.out.println(key);
        }
    }
}

class ClosingPhase {
    public Phase run() {
        Halo.closeStreams();
        Halo.sweep();
        return Phase.MAIN_MENU;
    }
}

class MenuPhase {
    public Phase run() {
        Halo.sweep();
        System.out.println(A.UL + " <<>> Main Menu." + A.RESET);
        System.out.println(" >> write " + A.CYAN + "help" + A.RESET + " for a list of commands.");
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
                        System.out.println(A.GREEN + " > new ip: " + A.RESET + Halo.defaultAddress);
                    }
                    if (textList.get(1).equalsIgnoreCase("port")) {
                        try {
                            Halo.defaultPort = Integer.parseInt(textList.get(3));
                            System.out.println(A.GREEN + " > new port: " + A.RESET + Halo.defaultPort);
                        } catch (NumberFormatException e) {
                            System.out.println(A.RED + " > That was not a number" + A.RESET);
                        }
                    }

                    break;
                case "c": //c tom 3
                    int numberOfPlayers1 = Integer.parseInt(textList.get(2));
                    String nickname1 = textList.get(1);
                    boolean solo1 = Integer.parseInt(textList.get(2)) == 1;

                    return createRoutine(Halo.defaultAddress, Halo.defaultPort, numberOfPlayers1, nickname1, solo1);
                case "j": //j tom 343
                    int lobbyNumber2 = Integer.parseInt(textList.get(2));
                    String nickname2 = textList.get(1);

                    return joinRoutine(Halo.defaultAddress, Halo.defaultPort, lobbyNumber2, nickname2);
                case "r":
                    int lobbyNumber3 = Integer.parseInt(textList.get(2));
                    String nickname3 = textList.get(1);

                    return rejoinRoutine(Halo.defaultAddress, Halo.defaultPort, lobbyNumber3, nickname3);
                case "quit":
                    return Phase.QUIT;
                case "help":
                    Halo.printHelpMenu();
                    break;
                case "create":  // CREATE localhost 43210 Tommaso 4
                    if (!checkCreateCommand(textList)) break;

                    int numberOfPlayers4 = Integer.parseInt(textList.get(4));
                    String nickname4 = textList.get(3);
                    boolean solo4 = Integer.parseInt(textList.get(4)) == 1;
                    String customAddress4 = textList.get(1);
                    int customPort4 = Integer.parseInt(textList.get(2));

                    return createRoutine(customAddress4, customPort4, numberOfPlayers4, nickname4, solo4);
                case "join": // JOIN localhost 1337 Tommaso 256
                    if (!checkJoinCommand(textList)) break;

                    String customAddress5 = textList.get(1);
                    int customPort5 = Integer.parseInt(textList.get(2));
                    int lobbyNumber5 = Integer.parseInt(textList.get(4));
                    String nickname5 = textList.get(3);

                    return joinRoutine(customAddress5, customPort5, lobbyNumber5, nickname5);
                case "rejoin":
                    if (!checkJoinCommand(textList)) break;
                    String customAddress6 = textList.get(1);
                    int customPort6 = Integer.parseInt(textList.get(2));
                    int lobbyNumber6 = Integer.parseInt(textList.get(4));
                    String nickname6 = textList.get(3);

                    return rejoinRoutine(customAddress6, customPort6, lobbyNumber6, nickname6);
                case "halo":
                    new Thread(this::EE).start();
                    break;
                case "local":
                    return Phase.LOCAL_PHASE;
                default:
                    System.out.println(A.RED + " > Sorry, we didn't catch that" + A.RESET);
            }
        }
    }

    private Phase rejoinRoutine(String ip, int port, int lobbyNumber, String nickname) {
        Message message;
        try {
            openStreams(ip, port);

            try {
                MSG_REJOIN_LOBBY m = new MSG_REJOIN_LOBBY(nickname, lobbyNumber);
                Halo.objectOutputStream.writeObject(m);
                Halo.objectOutputStream.flush();
            } catch (IllegalArgumentException e) {
                System.out.println(A.RED + " > The parameters for the join are not correct!" + A.RESET);
                return Phase.MAIN_MENU;
            }

            message = (Message) Halo.objectInputStream.readObject();

            if (message.getMessageType() == MessageType.MSG_OK_REJOIN) {
                MSG_OK_REJOIN msg = (MSG_OK_REJOIN) message;
                System.out.println(A.GREEN + " <<>> RE - Connected! " + A.RESET);
                System.out.println(A.GREEN + " <> Your assigned nickname is " + msg.getNickname() + A.RESET);
                Halo.myNickname = msg.getNickname();
                Halo.solo = false;
                Halo.defaultAddress = ip;
                return Phase.ONLINE_PHASE;
            } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                MSG_ERROR msg = (MSG_ERROR) message;
                System.out.println(A.RED + " > Error! " + A.RESET);
                System.out.println(A.RED + " <> " + msg.getErrorMessage() + A.RESET);
                return Phase.ERROR;
            }
        } catch (IOException | ClassNotFoundException | NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return Phase.MAIN_MENU;

    }

    private Phase joinRoutine(String ip, int port, int lobbyNumber, String nickname) {
        Message message;
        try {
            openStreams(ip, port);

            try {
                MSG_JOIN_LOBBY m = new MSG_JOIN_LOBBY(nickname, lobbyNumber);
                Halo.objectOutputStream.writeObject(m);
                Halo.objectOutputStream.flush();
            } catch (IllegalArgumentException e) {
                System.out.println(A.RED + " > The parameters for the join are not correct!" + A.RESET);
                return Phase.MAIN_MENU;
            }

            message = (Message) Halo.objectInputStream.readObject();

            if (message.getMessageType() == MessageType.MSG_OK_JOIN) {
                MSG_OK_JOIN msg = (MSG_OK_JOIN) message;
                System.out.println(A.GREEN + " <<>> Connected! " + A.RESET);
                System.out.println(A.GREEN + " <> Your assigned nickname is " + msg.getAssignedNickname() + A.RESET);
                Halo.myNickname = msg.getAssignedNickname();
                Halo.solo = false;
                Halo.defaultAddress = ip;
                return Phase.ONLINE_PHASE;
            } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                MSG_ERROR msg = (MSG_ERROR) message;
                System.out.println(A.RED + " > Error! " + A.RESET);
                System.out.println(A.RED + " <> " + msg.getErrorMessage() + A.RESET);
                return Phase.ERROR;
            }
        } catch (IOException | ClassNotFoundException | NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return Phase.MAIN_MENU;
    }

    private Phase createRoutine(String ip, int port, int numberOfPlayers, String nickname, boolean solo) {
        Message message;
        try {
            openStreams(ip, port);

            try {
                MSG_CREATE_LOBBY m = new MSG_CREATE_LOBBY(numberOfPlayers, nickname);
                Halo.objectOutputStream.writeObject(m);
                Halo.objectOutputStream.flush();
            } catch (IllegalArgumentException e) {
                System.out.println(A.RED + " > The parameters for the creation are not correct!" + A.RESET);
                return Phase.MAIN_MENU;
            }

            message = (Message) Halo.objectInputStream.readObject();

            if (message.getMessageType() == MessageType.MSG_OK_CREATE) {
                MSG_OK_CREATE msg = (MSG_OK_CREATE) message;
                System.out.println(A.GREEN + " <<>> Connected! " + A.RESET);
                System.out.println(A.GREEN + " <> Your lobby number is " + msg.getLobbyNumber() + A.RESET);
                Halo.myNickname = nickname;
                Halo.solo = solo;
                Halo.defaultAddress = ip;
                return Phase.ONLINE_PHASE;
            } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                MSG_ERROR msg = (MSG_ERROR) message;
                System.out.println(A.RED + " > Error! " + A.RESET);
                System.out.println(A.RED + " <> " + msg.getErrorMessage() + A.RESET);
                return Phase.ERROR;
            }

        } catch (IOException | ClassNotFoundException | NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return Phase.MAIN_MENU;
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
            System.out.println(A.RED + " > Error! The number of parameters is incorrect!" + A.RESET);
            return false;
        }
        try {
            if (Integer.parseInt(textList.get(2)) >= 65536) {
                System.out.println(A.RED + " > Error! The port number is way too high!" + A.RESET);
                return false;
            }
            if (Integer.parseInt(textList.get(2)) <= 1023) {
                System.out.println(A.RED + " > Error! Your port number must be greater than 1023!" + A.RESET);
                return false;
            }
            if (Integer.parseInt(textList.get(4)) > 4) {
                System.out.println(A.RED + " > Error! The number of players must be < 5" + A.RESET);
                return false;
            }
            if (Integer.parseInt(textList.get(4)) < 1) {
                System.out.println(A.RED + " > Error! There must be at least one player!" + A.RESET);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(A.RED + " > Error! Could not parse the number!" + A.RESET);
            return false;
        }
        return true;
    }

    private boolean checkJoinCommand(List<String> textList) {
        if (textList.size() != 5) {
            System.out.println(A.RED + " > Error! The number of parameters is incorrect!" + A.RESET);
            return false;
        }
        try {
            if (Integer.parseInt(textList.get(2)) >= 65536) {
                System.out.println(A.RED + " > Error! The port number is way too high!" + A.RESET);
                return false;
            }
            if (Integer.parseInt(textList.get(2)) <= 1023) {
                System.out.println(A.RED + " > Error! Your port number must be greater than 1023!" + A.RESET);
                return false;
            }
            if (Integer.parseInt(textList.get(4)) <= -1 || Integer.parseInt(textList.get(4)) >= 500) {
                System.out.println(A.RED + " > Error! Lobby number must be hamburgered between 0 and 500!" + A.RESET);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(A.RED + " > Error! Could not parse the number!" + A.RESET);
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
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class OnlinePhase {

    public Phase run() {
        Message message;
        boolean execute;
        Halo.local = false;

        System.out.println(A.YELLOW + " >> Waiting for Initial update model." + A.RESET);
        System.out.println(A.RED + " >> Console is unresponsive. " + A.RESET + "Please wait.");
        try {

            message = (Message) Halo.objectInputStream.readObject();
            if (message.getMessageType() == MessageType.MSG_ERROR) {
                System.out.println(A.RED + " <> " + ((MSG_ERROR) message).getErrorMessage() + A.RESET);
                return Phase.ERROR;
            } else if (message.getMessageType() == MessageType.MSG_UPD_Full) {
                Halo.game = new GameSimplified();
                Halo.game.updateAll((MSG_UPD_Full) message);
                System.out.println(A.GREEN + " <> Model received.");
                System.out.println(" >> Console is responsive." + A.RESET);
            } else {
                System.out.println(A.RED + " <> Received unexpected message: " + message.getMessageType() + A.RESET);
                return Phase.ERROR;
            }

            Halo.yourTurn = Halo.game.isMyTurn(Halo.myPlayerNumber);
            Halo.myPlayerNumber = Halo.game.getMyPlayerNumber(Halo.myNickname);
            if (Halo.myPlayerNumber == 0) return Phase.ERROR;

            Halo.myPlayerRef = Halo.game.getPlayerRef(Halo.myPlayerNumber);

            System.out.println(A.YELLOW + " >> Remember " + A.CYAN + "help" + A.YELLOW + " for a list of commands." + A.RESET);
            List<String> textList = new ArrayList<>();
            String text;

            new Thread(new UpdateHandler()).start();

            while (true) {
                execute = true;
//phase 1: not found
//phase 2: gets input
                text = Halo.input.nextLine();

                textList.clear();
                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

//phase 3: input gets converted into specific action
                // must correct synchronization
                if (Halo.yourTurn) {
                    if (Halo.game.isMiddleActive()) {
//LEADERCARDS ROUTINE
                        if (Halo.game.isLeaderCardsObjectEnabled()) {
                            message = Halo.chooseLeaderCards(textList);
                            if (message != null) {
                                Halo.objectOutputStream.writeObject(message);
                                Halo.objectOutputStream.flush();
                            }
                        }
//RESOURCE ROUTINE
                        else if (Halo.game.isResourceObjectEnabled()) {
                            message = Halo.chooseResource(textList);
                            if (message != null) {
                                Halo.objectOutputStream.writeObject(message);
                                Halo.objectOutputStream.flush();
                            }
                        }
//MARKETHELPER ROUTINE
                        else if (Halo.game.isMarketHelperEnabled()) {
                            message = Halo.marketChoice(textList);
                            if (message != null) {
                                Halo.objectOutputStream.writeObject(message);
                                Halo.objectOutputStream.flush();
                            }
                        }
//VENDOR ROUTINE
                        else if (Halo.game.isDevelopmentCardsVendorEnabled()) {
                            message = Halo.chooseDevCard(textList);
                            if (message != null) {
                                Halo.objectOutputStream.writeObject(message);
                                Halo.objectOutputStream.flush();
                            }
                        }
                        execute = false;
                    }
                }

                if (execute) {
                    switch (textList.get(0).toLowerCase()) {
                        case "quit": {
                            return (new ClosingPhase()).run();
                        }
                        case "help": {
                            Halo.printHelpGame();
                        }
                        break;
                        case "show": {
                            if (!Halo.checkShowCommand(textList)) break;
                            synchronized (Halo.game) {
                                if (textList.size() == 2) // show depot
                                {
                                    Halo.showMy(textList.get(1));
                                } else if (textList.size() == 4) {
                                    Halo.showPlayerAsset(textList.get(3), Integer.parseInt(textList.get(2)));
                                } else //if(size==3)
                                {
                                    Halo.showPlayerAsset(textList.get(2), textList.get(1));
                                }
                            }
                        }
                        break;
                        case "action":
                            if (Halo.yourTurn) {
                                Halo.printActions();
                                actionLoop:
                                while (true) {
                                    System.out.println(A.UL + " Please pick a number: " + A.RESET);
                                    System.out.print(" Number: ");
                                    text = Halo.input.nextLine();
                                    textList.clear();
                                    textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                                    if (Halo.checkAction(textList)) {
                                        int choice = Integer.parseInt(textList.get(0));
                                        switch (choice) {
//ACTION ACTIVATE LEADERCARD
                                            case 1: {
                                                message = Halo.activateLeaderCard(textList);
                                                if (message != null) {
                                                    Halo.objectOutputStream.writeObject(message);
                                                    Halo.objectOutputStream.flush();
                                                }
                                                break actionLoop;
                                            }
//ACTION DISCARD LEADERCARD
                                            case 2: {
                                                message = Halo.discardLeaderCard(textList);
                                                if (message != null) {
                                                    Halo.objectOutputStream.writeObject(message);
                                                    Halo.objectOutputStream.flush();
                                                }
                                                break actionLoop;
                                            }
//ACTION ACTIVATE PRODUCTION
                                            case 3: {
                                                message = Halo.activateProduction(textList);
                                                if (message != null) {
                                                    Halo.objectOutputStream.writeObject(message);
                                                    Halo.objectOutputStream.flush();
                                                }
                                                break actionLoop;
                                            }
//ACTION CHANGE DEPOT CONFIG
                                            case 4: {
                                                message = Halo.changeDepotConfig(textList);
                                                if (message != null) {
                                                    Halo.objectOutputStream.writeObject(message);
                                                    Halo.objectOutputStream.flush();
                                                }
                                                break actionLoop;
                                            }
//ACTION BUY CARD
                                            case 5: {
                                                System.out.println(" > Asking the Vendor which cards we can buy...");
                                                MSG_ACTION_BUY_DEVELOPMENT_CARD msgToSend5 = new MSG_ACTION_BUY_DEVELOPMENT_CARD();
                                                Halo.objectOutputStream.writeObject(msgToSend5);
                                                Halo.objectOutputStream.flush();
                                                break actionLoop;
                                            }
//ACTION GET MARKET RESOURCES
                                            case 6: {
                                                message = Halo.getMarketResources(textList);
                                                if (message != null) {
                                                    Halo.objectOutputStream.writeObject(message);
                                                    Halo.objectOutputStream.flush();
                                                }
                                                break actionLoop;
                                            }
//ACTION BUY DEV CARDS
                                            case 7: {
                                                /*if(!Halo.action){
                                                    System.out.println("You must do a main action before ending the turn");
                                                    break actionLoop;
                                                }*/
                                                System.out.println(" > Ending the Turn...");
                                                MSG_ACTION_ENDTURN msgToSend7 = new MSG_ACTION_ENDTURN();
                                                Halo.objectOutputStream.writeObject(msgToSend7);
                                                Halo.objectOutputStream.flush();
                                                break actionLoop;
                                            }
//GO BACK
                                            case 0: {
                                                System.out.println(A.GREEN + " > Going back to main menu" + A.RESET);
                                                break actionLoop;
                                            }
                                        }
                                    }
                                }

                            } else {
                                System.out.println(A.RED + " > Hey, wait for your turn!" + A.RESET);
                            }
                            break;
                        default:
                            System.out.println(A.RED + " > Sorry, I didn't catch that" + A.RESET);
                    } //switch(text get(0) lowercase)
                } //if(execute)
            } //while(true)

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return Phase.ERROR;
        }
    }
}

class LocalPhase {

    public Phase run() {
        Message message;
        boolean execute;

        System.out.println(A.YELLOW + " <<>> Starting local game..." + A.RESET);
        System.out.println(A.RED + " >> Console is unresponsive. " + A.RESET + "Please wait.");

        init();

        List<String> textList = new ArrayList<>();
        String text;

        while (true) {
            execute = true;

            //phase 2: gets input
            text = Halo.input.nextLine();

            textList.clear();
            textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

            if (Halo.gameSRV.isMiddleActive() && !Halo.gameSRV.isLeaderBoardEnabled()) {
                if (Halo.gameSRV.isLeaderCardsObjectEnabled()) {
                    message = Halo.chooseLeaderCards(textList);
                    if (message != null) {
                        Halo.actionManager.onMessage(message);
                    }
                } else if (Halo.gameSRV.isMarketHelperEnabled()) {
                    message = Halo.marketChoice(textList);
                    if (message != null) {
                        Halo.actionManager.onMessage(message);
                    }

                } else if (Halo.gameSRV.isDevelopmentCardsVendorEnabled()) {
                    message = Halo.chooseDevCard(textList);
                    if (message != null) {
                        Halo.actionManager.onMessage(message);
                    }
                }
                execute = false;
            }

            if (execute) {
                switch (textList.get(0).toLowerCase()) {
                    case "quit":
                        return (new ClosingPhase()).run();
                    case "help": {
                        Halo.printHelpGame();
                    }
                    break;
                    case "show": {
                        if (!Halo.checkShowCommand(textList)) break;
                        Halo.showMy(textList.get(1));
                    }
                    break;
                    case "action":
                        if (Halo.yourTurn) {
                            Halo.printActions();
                            actionLoop:
                            while (true) {
                                System.out.println(A.UL + " Please pick a number: " + A.RESET);
                                System.out.print(" Number: ");
                                text = Halo.input.nextLine();
                                textList.clear();
                                textList = new ArrayList<>((Arrays.asList(text.split("\\s+"))));

                                if (Halo.checkAction(textList)) {
                                    int choice = Integer.parseInt(textList.get(0));
                                    switch (choice) {
//ACTION ACTIVATE LEADERCARD
                                        case 1: {
                                            message = Halo.activateLeaderCard(textList);
                                            if (message != null) {
                                                Halo.actionManager.onMessage(message);
                                            }
                                            break actionLoop;
                                        }
//ACTION DISCARD LEADERCARD
                                        case 2: {
                                            message = Halo.discardLeaderCard(textList);
                                            if (message != null) {
                                                Halo.actionManager.onMessage(message);
                                            }
                                            break actionLoop;
                                        }
//ACTION ACTIVATE PRODUCTION
                                        case 3: {
                                            message = Halo.activateProduction(textList);
                                            if (message != null) {
                                                Halo.actionManager.onMessage(message);
                                            }
                                            break actionLoop;
                                        }
//ACTION CHANGE DEPOT CONFIG
                                        case 4: {
                                            message = Halo.changeDepotConfig(textList);
                                            if (message != null) {
                                                Halo.actionManager.onMessage(message);
                                            }
                                            break actionLoop;
                                        }
//ACTION BUY CARD
                                        case 5: {
                                            System.out.println(" > Asking the Vendor which cards we can buy...");
                                            message = new MSG_ACTION_BUY_DEVELOPMENT_CARD();
                                            Halo.actionManager.onMessage(message);
                                            break actionLoop;
                                        }
//ACTION GET MARKET RESOURCES
                                        case 6: {
                                            message = Halo.getMarketResources(textList);
                                            if (message != null) {
                                                Halo.actionManager.onMessage(message);
                                            }
                                            break actionLoop;
                                        }
//ACTION BUY DEV CARDS
                                        case 7: {
                                            /*if(!Halo.action){
                                                System.out.println("You must do a main action before ending the turn");
                                                break actionLoop;
                                            }*/
                                            message = new MSG_ACTION_ENDTURN();
                                            Halo.actionManager.onMessage(message);
                                            break actionLoop;
                                        }
//GO BACK
                                        case 0: {
                                            System.out.println(A.GREEN + " > Going back to main menu" + A.RESET);
                                            break actionLoop;
                                        }

                                    }
                                }
                            }
                        } else
                            System.out.println(A.RED + " > Game is Over, you can only show your things and quit" + A.RESET);
                        break;
                    default:
                        System.out.println(A.RED + " > Sorry, I didn't catch that" + A.RESET);

                }
            }
        }
    }


    private void init() {
        Halo.local = true;
        Halo.solo = true;
        Halo.myPlayerNumber = 1;
        Halo.myNickname = "Player";
        Halo.gameManager = new GameManager(1);
        Halo.actionManager = Halo.gameManager.getActionManager();
        Halo.gameSRV = Halo.gameManager.getGame();
        Halo.gameSRV.addPlayer(Halo.myNickname, Halo.myPlayerNumber);
        Halo.myPlayerRefSRV = Halo.gameManager.currentPlayer();

        Halo.gameSRV.setLeaderCardsObjectCards(Halo.gameSRV.getCurrentPlayerStartingCards());
        Halo.gameSRV.setLeaderCardsObjectEnabled(true);

        UpdateHandlerLocal updateHandlerLocal = new UpdateHandlerLocal();
        Halo.gameManager.addAllObserver(updateHandlerLocal);

        Halo.yourTurn = true;

        System.out.println(" >> Game started!");

        System.out.println(" <> Two free Leader Cards!");
        System.out.println(Halo.gameSRV.getLeaderCardsObject().toString());
        System.out.println(" > Please pick the first card:");
        System.out.print(" Card number: ");
    }
}

class ErrorPhase {
    public Phase run() {
        System.out.println(A.RED + " > There was an accident and you will be returned to Main Menu." + A.RESET);
        Halo.closeStreams();
        return Phase.MAIN_MENU;
    }
}