package it.polimi.ingsw.Server.Utils;

import it.polimi.ingsw.Client.ModelSimplified.GameSimplified;
import it.polimi.ingsw.Client.ModelSimplified.PlayerSimplified;
import it.polimi.ingsw.Server.Model.DevelopmentCard;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.LeaderCard;
import it.polimi.ingsw.Server.Model.Marbles.MarketMarble;
import it.polimi.ingsw.Server.Model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.Server.Model.Enumerators.Resource.*;
import static it.polimi.ingsw.Server.Model.Enumerators.Resource.STONE;

public abstract class Displayer {

    public static String warehouseDepotToString(Resource shelf1, Resource[] shelf2, Resource[] shelf3) {
        StringBuilder result = new StringBuilder();
        result.append("               WAREHOUSE DEPOT:").append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(A.CYAN + "  Shelf 1:  " + A.RESET).append(shelf1).append("\n");
        result.append(A.CYAN + "  Shelf 2:  " + A.RESET).append(shelf2[0]).append(" - ").append(shelf2[1]).append("\n");
        result.append(A.CYAN + "  Shelf 3:  " + A.RESET).append(shelf3[0]).append(" - ").append(shelf3[1]).append(" - ").append(shelf3[2]).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }

    public static String strongboxToString(Map<Resource, Integer> resources) {
        StringBuilder result = new StringBuilder();
        result.append("      STRONGBOX").append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        if (resources.isEmpty()) {
            result.append(" The Strongbox is empty. ").append("\n");
        } else {
            for (Resource r : resources.keySet()) {
                result.append(resources.get(r)).append(" of ").append(r.toString());
                result.append("\n");
            }
        }
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }

    public static String marketToString(MarketMarble[][] grid, MarketMarble slideMarble) {
        StringBuilder result = new StringBuilder();
        result.append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append("                   MARKET!                ").append("\n");
        result.append("      Marble on the Slide: ").append(slideMarble).append("\n").append("\n");
        result.append("             [ ").append(grid[0][0].toAbbreviation()).append(" | ").append(grid[0][1].toAbbreviation()).append(" | ").append(grid[0][2].toAbbreviation()).append(" | ").append(grid[0][3].toAbbreviation()).append(" ]").append("\n");
        result.append("             [ ").append(grid[1][0].toAbbreviation()).append(" | ").append(grid[1][1].toAbbreviation()).append(" | ").append(grid[1][2].toAbbreviation()).append(" | ").append(grid[1][3].toAbbreviation()).append(" ]").append("\n");
        result.append("             [ ").append(grid[2][0].toAbbreviation()).append(" | ").append(grid[2][1].toAbbreviation()).append(" | ").append(grid[2][2].toAbbreviation()).append(" | ").append(grid[2][3].toAbbreviation()).append(" ]").append("\n");
        result.append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }

    public static String faithTrackToString(boolean solo, boolean[] zones, boolean simplified, Game game, GameSimplified gameSimplified) {
        StringBuilder result = new StringBuilder();
        List<PlayerSimplified> playerListSimplified;
        List<Player> playerList;

        result.append("                  FAITH TRACK: ").append("\n");

        if (simplified) {
            playerListSimplified = gameSimplified.getPlayerList();
            for (PlayerSimplified player : playerListSimplified) {
                result.append("   ").append(player.getNickname()).append(" is at position: ").append(player.getPosition()).append("\n");
            }
        } else {
            playerList = game.getPlayerList();
            for (Player player : playerList) {
                result.append("   ").append(player.getNickname()).append(" is at position: ").append(player.getPosition()).append("\n");
            }
        }

        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");

        if (solo)
            result.append("\n").append(" Lorenzo is at position: ").append(game.getBlackCrossPosition()).append("\n");
        result.append("\n");
        if (zones[2])
            result.append("   Third and last zone has been activated!");
        else {
            if (zones[1])
                result.append("   Second zone has been activated!");
            else {
                if (zones[0])
                    result.append("   The First zone has been activated!");
                else
                    result.append("   No zone has been activated yet!");
            }
        }
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }

    public static String developmentSlotToString(DevelopmentCard[][] cards) {
        StringBuilder result = new StringBuilder();

        DevelopmentCard[] onTop = new DevelopmentCard[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 2; j >= 0; j--) {
                if (cards[i][j] != null) {
                    onTop[i] = cards[i][j];
                    break;
                }
            }
        }


        result.append("                  DEVELOPMENT SLOT:    \n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(A.CYAN + "Slot 1: \n" + A.RESET);
        if (onTop[0] != null) {
            result.append(onTop[0]);
        } else {
            result.append(" No card in slot number 1. \n");
        }
        for (int i = 0; i < 3; i++) {
            if (cards[0][i] != onTop[0]) {
                //FIXME check comment below
                assert cards[0][i] != null; //getVP was signaling possible nullPointerException, please check
                result.append("VP of underneath cards: ").append(cards[0][i].getVP()).append("\n");
            } else {
                break;
            }
        }

        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(A.CYAN + "Slot 2: \n" + A.RESET);
        if (onTop[1] != null) {
            result.append(onTop[1]);
        } else {
            result.append(" No card in slot number 2. \n");
        }
        for (int i = 0; i < 3; i++) {
            if (cards[1][i] != onTop[1]) {
                result.append("VP of underneath cards: ").append(cards[1][i].getVP()).append("\n");
            } else {
                break;
            }
        }

        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(A.CYAN + "Slot 3: \n" + A.RESET);
        if (onTop[2] != null) {
            result.append(onTop[2]);
        } else {
            result.append(" No card in slot number 3. \n");
        }
        for (int i = 0; i < 3; i++) {
            if (cards[2][i] != onTop[2]) {
                result.append("VP of underneath cards: ").append(cards[2][i].getVP()).append("\n");
            } else {
                break;
            }
        }
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");

        return result.toString();
    }

    public static String developmentCardsDeckToString(DevelopmentCard[][] cards) {
        StringBuilder result = new StringBuilder(" DEVELOPMENT DECK, ALL THE VISIBLE CARDS: ");

        for (int i = 0; i < 3; i++) {
            result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET);
            result.append("\n").append(A.CYAN + "     I     I     I     I     I     I     I     " + A.RESET);
            result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
            result.append("\n").append(" Row ").append(i);
            for (int j = 0; j < 4; j++) {
                result.append("\n").append("  Column ").append(j);
                if (cards[i][j] == null)
                    result.append("\n").append(" X=====X Empty! X=====X");
                else
                    result.append("\n").append(cards[i][j].toString());
            }
        }
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET);
        result.append("\n").append(A.CYAN + "     I     I     I     I     I     I     I     " + A.RESET);
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }

    public static String resourceObjectToString(boolean enabled, int numOfResources) {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append(A.CYAN + " RESOURCE OBJECT IS HERE TO HELP! " + A.RESET).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        if (enabled) {
            result.append("  Number of resource to get: ").append(numOfResources).append("\n");
            result.append("   1:  " + SHIELD);
            result.append("   2:  " + COIN);
            result.append("   3:  " + SERVANT);
            result.append("   4:  " + STONE);
        } else
            result.append("  ResourceObject is not enabled.");
        return result.toString();
    }

    public static String marketHelperToString(boolean enabled, ArrayList<Resource> resources, int currentResource, boolean[] choices, Resource[] extraResourceChoices) {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append(A.CYAN + " MARKETHELPER IS HERE TO HELP! " + A.RESET).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(" The Resources you gathered from the market are: ").append("\n");
        result.append(" ").append(resources).append("\n");
        result.append(" Currently selected resource is a ").append(resources.get(currentResource)).append(". What do you want to do with it?").append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(" Available options: ").append("\n");
        if (resources.get(currentResource) != Resource.EXTRA) {
            if (choices[0]) result.append("  0 : put in depot! ").append("\n");
            if (choices[1]) result.append("  1 : put in Extra depot!").append("\n");
        } else {
            if (choices[0]) result.append("  0 : convert in ").append(extraResourceChoices[0]).append("\n");
            if (choices[1]) result.append("  1 : convert in ").append(extraResourceChoices[1]).append("\n");
        }

        if (choices[2]) result.append("  2 : discard that resource! ").append("\n");
        if (choices[3]) result.append("  3 : swap the 1st and 2nd rows of your depot! ").append("\n");
        if (choices[4]) result.append("  4 : swap the 1st and 3rd rows of your depot! ").append("\n");
        if (choices[5]) result.append("  5 : swap the 2nd and 3rd rows of your depot! ").append("\n");
        if (choices[6]) result.append("  6 : hop to the next available resource! ").append("\n");
        if (choices[7]) result.append("  7 : hop back to the previous resource! ").append("\n");

        return result.toString();
    }

    public static String leaderCardsObjectToString(boolean enabled, ArrayList<LeaderCard> cards) {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append(A.CYAN + " LEADERCARD PICKER IS HERE TO HELP! " + A.RESET).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        if (enabled) {
            result.append(" These are the cards: ").append("\n");
            for (int i = 0; i < cards.size(); i++) {
                result.append("\n\n").append(" Card number #").append(i + 1).append("\n");
                result.append(cards.get(i).toString());
            }
        } else
            result.append("  LeaderCardsObject is not enabled.");
        return result.toString();
    }

    public static String leaderboardToResult(boolean enabled, Map<String, Integer> leaderboard, String thisPlayer, boolean solo) {
        StringBuilder result = new StringBuilder();
        if (!enabled) return result.append("Empty LeaderBoard.").toString();

        result.append(A.CYAN + "      LEADERBOARD" + A.RESET).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        boolean tie = false;
        Integer maxValue = 0;
        for (String nickname : leaderboard.keySet()) {
            if (leaderboard.get(nickname) > maxValue) maxValue = leaderboard.get(nickname);
        }
        if (solo) {
            String key = "Lorenzo";
            if (leaderboard.get(key) == 1) //lorenzo Lost
            {
                result.append(" You just won. Happy now? ").append("\n");
                result.append("\n").append(thisPlayer).append("    ");
                result.append(" - ").append(leaderboard.get(thisPlayer)).append(" points");
            }
            if (leaderboard.get(key) == 2) //lorenzo won
            {
                result.append(" I mean, you lost. GG").append("\n");
                result.append("\n").append(thisPlayer);
                result.append("\t").append(leaderboard.get(thisPlayer));
            }
        } else {
            if (leaderboard.get(thisPlayer).equals(maxValue)) {
                for (String nickname : leaderboard.keySet()) {
                    if (nickname.equals(thisPlayer))
                        continue;
                    if (maxValue.equals(leaderboard.get(nickname)))
                        tie = true;
                }
                if (tie)
                    result.append(" Extremely lucky guy, you Tied. ").append("\n");
                else
                    result.append(" You just won. Happy now? ").append("\n");
            } else
                result.append(" I mean, you lost. GG").append("\n");

            result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
            for (String nickname : leaderboard.keySet()) {
                result.append("\n").append(nickname);
                result.append("\t").append(leaderboard.get(nickname));
            }
        }
        return result.toString();
    }

    public static String developmentCardsVendorToString(Map<DevelopmentCard, boolean[]> cards) {
        StringBuilder result = new StringBuilder();
        int i = 1;
        result.append(A.CYAN + " THE VENDOR IS HERE TO HELP! " + A.RESET).append("\n").append("\n");
        if (cards != null) {
            for (DevelopmentCard dc : cards.keySet()) {
                result.append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
                result.append("  The card number: ").append(i).append("\n");
                result.append(dc).append("\n");
                int k = 1;
                for (boolean b : cards.get(dc)) {
                    if (b) {
                        result.append("  Can be placed in slot number: ").append(k).append("\n");
                    }
                    k++;
                }
                i++;
            }
        } else
            result.append(" No cards present");
        return result.toString();
    }
}