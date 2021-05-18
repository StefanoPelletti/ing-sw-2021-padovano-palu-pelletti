package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_DevDeck;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_Market;
import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Utils.A;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.*;

public class DevelopmentCardsDeck extends ModelObservable {
    private DevelopmentCard[][][] cards;
    private List<DevelopmentCard> internalList;

    public DevelopmentCardsDeck() {
        cards = new DevelopmentCard[3][4][4];
        internalList = new ArrayList<>();

        cards[0][0][0] = new DevelopmentCard(3, Color.GREEN, 12,
                Map.of(Resource.SHIELD, 4, Resource.COIN, 4),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.COIN, 3, Resource.SHIELD, 1))
        );

        cards[0][0][1] = new DevelopmentCard(3, Color.GREEN, 11,
                Map.of(Resource.SHIELD, 7),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.COIN, 1, Resource.FAITH, 3))
        );

        cards[0][0][2] = new DevelopmentCard(3, Color.GREEN, 10,
                Map.of(Resource.SHIELD, 5, Resource.SERVANT, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.SERVANT, 1),
                        Map.of(Resource.SHIELD, 2, Resource.STONE, 2, Resource.FAITH, 1))
        );

        cards[0][0][3] = new DevelopmentCard(3, Color.GREEN, 9,
                Map.of(Resource.SHIELD, 6),
                new Power(Map.of(Resource.COIN, 2),
                        Map.of(Resource.STONE, 3, Resource.FAITH, 2))
        );

        cards[0][1][0] = new DevelopmentCard(3, Color.BLUE, 12,
                Map.of(Resource.COIN, 4, Resource.STONE, 4),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.COIN, 1, Resource.SHIELD, 3))
        );

        cards[0][1][1] = new DevelopmentCard(3, Color.BLUE, 11,
                Map.of(Resource.COIN, 7),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.SHIELD, 1, Resource.FAITH, 3))
        );

        cards[0][1][2] = new DevelopmentCard(3, Color.BLUE, 10,
                Map.of(Resource.COIN, 5, Resource.STONE, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.SHIELD, 1),
                        Map.of(Resource.SERVANT, 2, Resource.STONE, 2, Resource.FAITH, 1))
        );

        cards[0][1][3] = new DevelopmentCard(3, Color.BLUE, 9,
                Map.of(Resource.COIN, 6),
                new Power(Map.of(Resource.SERVANT, 2),
                        Map.of(Resource.SHIELD, 3, Resource.FAITH, 2))
        );

        cards[0][2][0] = new DevelopmentCard(3, Color.YELLOW, 12,
                Map.of(Resource.STONE, 4, Resource.SERVANT, 4),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.STONE, 1, Resource.SERVANT, 3))
        );

        cards[0][2][1] = new DevelopmentCard(3, Color.YELLOW, 11,
                Map.of(Resource.STONE, 7),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.SERVANT, 1, Resource.FAITH, 3))
        );

        cards[0][2][2] = new DevelopmentCard(3, Color.YELLOW, 10,
                Map.of(Resource.STONE, 5, Resource.SERVANT, 2),
                new Power(Map.of(Resource.STONE, 1, Resource.SERVANT, 1),
                        Map.of(Resource.COIN, 2, Resource.SHIELD, 2, Resource.FAITH, 1))
        );

        cards[0][2][3] = new DevelopmentCard(3, Color.YELLOW, 9,
                Map.of(Resource.STONE, 6),
                new Power(Map.of(Resource.SHIELD, 2),
                        Map.of(Resource.SERVANT, 3, Resource.FAITH, 2))
        );

        cards[0][3][0] = new DevelopmentCard(3, Color.PURPLE, 12,
                Map.of(Resource.SERVANT, 4, Resource.SHIELD, 4),
                new Power(Map.of(Resource.COIN, 1),
                        Map.of(Resource.STONE, 3, Resource.SERVANT, 1))
        );

        cards[0][3][1] = new DevelopmentCard(3, Color.PURPLE, 11,
                Map.of(Resource.SERVANT, 7),
                new Power(Map.of(Resource.COIN, 1),
                        Map.of(Resource.STONE, 1, Resource.FAITH, 3))
        );

        cards[0][3][2] = new DevelopmentCard(3, Color.PURPLE, 10,
                Map.of(Resource.SERVANT, 5, Resource.COIN, 2),
                new Power(Map.of(Resource.STONE, 1, Resource.SHIELD, 1),
                        Map.of(Resource.COIN, 2, Resource.SERVANT, 2, Resource.FAITH, 1))
        );

        cards[0][3][3] = new DevelopmentCard(3, Color.PURPLE, 9,
                Map.of(Resource.SERVANT, 6),
                new Power(Map.of(Resource.STONE, 2),
                        Map.of(Resource.COIN, 3, Resource.FAITH, 2))
        );

        cards[1][0][0] = new DevelopmentCard(2, Color.GREEN, 8,
                Map.of(Resource.SHIELD, 3, Resource.COIN, 3),
                new Power(Map.of(Resource.COIN, 1),
                        Map.of(Resource.SHIELD, 2, Resource.FAITH, 1))
        );

        cards[1][0][1] = new DevelopmentCard(2, Color.GREEN, 7,
                Map.of(Resource.SHIELD, 5),
                new Power(Map.of(Resource.COIN, 2),
                        Map.of(Resource.STONE, 2, Resource.FAITH, 2))
        );

        cards[1][0][2] = new DevelopmentCard(2, Color.GREEN, 6,
                Map.of(Resource.SHIELD, 3, Resource.SERVANT, 2),
                new Power(Map.of(Resource.SHIELD, 1, Resource.SERVANT, 1),
                        Map.of(Resource.STONE, 3))
        );

        cards[1][0][3] = new DevelopmentCard(2, Color.GREEN, 5,
                Map.of(Resource.SHIELD, 4),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.FAITH, 2))
        );

        cards[1][1][0] = new DevelopmentCard(2, Color.BLUE, 8,
                Map.of(Resource.COIN, 3, Resource.STONE, 3),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.STONE, 2, Resource.FAITH, 1))
        );

        cards[1][1][1] = new DevelopmentCard(2, Color.BLUE, 7,
                Map.of(Resource.COIN, 5),
                new Power(Map.of(Resource.SERVANT, 2),
                        Map.of(Resource.SHIELD, 2, Resource.FAITH, 2))
        );

        cards[1][1][2] = new DevelopmentCard(2, Color.BLUE, 6,
                Map.of(Resource.COIN, 3, Resource.STONE, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 3))
        );

        cards[1][1][3] = new DevelopmentCard(2, Color.BLUE, 5,
                Map.of(Resource.COIN, 4),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.FAITH, 2))
        );

        cards[1][2][0] = new DevelopmentCard(2, Color.YELLOW, 8,
                Map.of(Resource.STONE, 3, Resource.SERVANT, 3),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.COIN, 2, Resource.FAITH, 1))
        );

        cards[1][2][1] = new DevelopmentCard(2, Color.YELLOW, 7,
                Map.of(Resource.STONE, 5),
                new Power(Map.of(Resource.SHIELD, 2),
                        Map.of(Resource.SERVANT, 2, Resource.FAITH, 2))
        );

        cards[1][2][2] = new DevelopmentCard(2, Color.YELLOW, 6,
                Map.of(Resource.STONE, 3, Resource.SHIELD, 2),
                new Power(Map.of(Resource.STONE, 1, Resource.SHIELD, 1),
                        Map.of(Resource.COIN, 3))
        );

        cards[1][2][3] = new DevelopmentCard(2, Color.YELLOW, 5,
                Map.of(Resource.STONE, 4),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.FAITH, 2))
        );

        cards[1][3][0] = new DevelopmentCard(2, Color.PURPLE, 8,
                Map.of(Resource.SERVANT, 3, Resource.SHIELD, 3),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 2, Resource.FAITH, 1))
        );

        cards[1][3][1] = new DevelopmentCard(2, Color.PURPLE, 7,
                Map.of(Resource.SERVANT, 5),
                new Power(Map.of(Resource.STONE, 2),
                        Map.of(Resource.COIN, 2, Resource.FAITH, 2))
        );

        cards[1][3][2] = new DevelopmentCard(2, Color.PURPLE, 6,
                Map.of(Resource.SERVANT, 3, Resource.COIN, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.SERVANT, 1),
                        Map.of(Resource.SHIELD, 3))
        );

        cards[1][3][3] = new DevelopmentCard(2, Color.PURPLE, 5,
                Map.of(Resource.SERVANT, 4),
                new Power(Map.of(Resource.COIN, 1),
                        Map.of(Resource.FAITH, 2))
        );

        cards[2][0][0] = new DevelopmentCard(1, Color.GREEN, 4,
                Map.of(Resource.SHIELD, 2, Resource.COIN, 2),
                new Power(Map.of(Resource.STONE, 1, Resource.SERVANT, 1),
                        Map.of(Resource.COIN, 2, Resource.FAITH, 1))
        );

        cards[2][0][1] = new DevelopmentCard(1, Color.GREEN, 3,
                Map.of(Resource.SHIELD, 3),
                new Power(Map.of(Resource.SERVANT, 2),
                        Map.of(Resource.COIN, 1, Resource.SHIELD, 1, Resource.STONE, 1))
        );

        cards[2][0][2] = new DevelopmentCard(1, Color.GREEN, 2,
                Map.of(Resource.SHIELD, 1, Resource.SERVANT, 1, Resource.STONE, 1),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 1))
        );

        cards[2][0][3] = new DevelopmentCard(1, Color.GREEN, 1,
                Map.of(Resource.SHIELD, 2),
                new Power(Map.of(Resource.COIN, 1),
                        Map.of(Resource.FAITH, 1))
        );

        cards[2][1][0] = new DevelopmentCard(1, Color.BLUE, 4,
                Map.of(Resource.COIN, 2, Resource.SERVANT, 2),
                new Power(Map.of(Resource.SHIELD, 1, Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 2, Resource.FAITH, 1))
        );

        cards[2][1][1] = new DevelopmentCard(1, Color.BLUE, 3,
                Map.of(Resource.COIN, 3),
                new Power(Map.of(Resource.STONE, 2),
                        Map.of(Resource.COIN, 1, Resource.SERVANT, 1, Resource.SHIELD, 1))
        );

        cards[2][1][2] = new DevelopmentCard(1, Color.BLUE, 2,
                Map.of(Resource.COIN, 1, Resource.SERVANT, 1, Resource.STONE, 1),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.STONE, 1))
        );

        cards[2][1][3] = new DevelopmentCard(1, Color.BLUE, 1,
                Map.of(Resource.COIN, 2),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.FAITH, 1))
        );

        cards[2][2][0] = new DevelopmentCard(1, Color.YELLOW, 4,
                Map.of(Resource.STONE, 2, Resource.SHIELD, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.SERVANT, 1),
                        Map.of(Resource.SHIELD, 2, Resource.FAITH, 1))
        );

        cards[2][2][1] = new DevelopmentCard(1, Color.YELLOW, 3,
                Map.of(Resource.STONE, 3),
                new Power(Map.of(Resource.SHIELD, 2),
                        Map.of(Resource.COIN, 1, Resource.SERVANT, 1, Resource.STONE, 1))
        );

        cards[2][2][2] = new DevelopmentCard(1, Color.YELLOW, 2,
                Map.of(Resource.SHIELD, 1, Resource.STONE, 1, Resource.COIN, 1),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.COIN, 1))
        );

        cards[2][2][3] = new DevelopmentCard(1, Color.YELLOW, 1,
                Map.of(Resource.STONE, 2),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.FAITH, 1))
        );

        cards[2][3][0] = new DevelopmentCard(1, Color.PURPLE, 4,
                Map.of(Resource.SERVANT, 2, Resource.STONE, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.SHIELD, 1),
                        Map.of(Resource.STONE, 2, Resource.FAITH, 1))
        );

        cards[2][3][1] = new DevelopmentCard(1, Color.PURPLE, 3,
                Map.of(Resource.SERVANT, 3),
                new Power(Map.of(Resource.COIN, 2),
                        Map.of(Resource.SERVANT, 1, Resource.SHIELD, 1, Resource.STONE, 1))
        );

        cards[2][3][2] = new DevelopmentCard(1, Color.PURPLE, 2,
                Map.of(Resource.SHIELD, 1, Resource.SERVANT, 1, Resource.COIN, 1),
                new Power(Map.of(Resource.COIN, 1),
                        Map.of(Resource.SHIELD, 1))
        );

        cards[2][3][3] = new DevelopmentCard(1, Color.PURPLE, 1,
                Map.of(Resource.SERVANT, 2),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.FAITH, 1))
        );

        shuffle();
    }


    //DO NOT CALL AFTER A REMOVE!
    public void shuffle() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                internalList.clear();
                internalList = new ArrayList<>(Arrays.asList(cards[r][c]));

                Collections.shuffle(internalList);
                for (int i = 0; i < 4; i++)
                    cards[r][c][i] = internalList.get(i);
            }
        }
    }

    public DevelopmentCard removeCard(int row, int column) {
        DevelopmentCard card = null;
        for (int i = 3; i >= 0; i--) {
            if (cards[row][column][i] != null) {
                card = cards[row][column][i];
                cards[row][column][i] = null;
                break;
            }
        }
        notifyObservers();
        return card;
    }

    public boolean removeCard(int column) {
        if (column < 0 || column > 3) return false;

        int removed = 0;
        for (int row = 2; row >= 0; row--) {
            for (int i = 3; i >= 0; i--) {
                if (cards[row][column][i] != null) {
                    cards[row][column][i] = null;
                    removed++;
                }
                if (removed == 2) break;
            }
            if (removed == 2) break;
        }
        notifyObservers();
        return true;
    }

    public DevelopmentCard[][] getVisible() {

        DevelopmentCard[][] temporaryDeck = new DevelopmentCard[3][4];

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                temporaryDeck[r][c] = null;
            }
        }

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                for (int i = 0; i < 4; i++) {
                    if (cards[r][c][i] != null)
                        temporaryDeck[r][c] = cards[r][c][i];
                }

            }
        }

        return temporaryDeck;
    }

    public DevelopmentCard[] getStack(int row, int column) {
        if (row < 0 || row > 2 || column < 0 || column > 3) return null;
        DevelopmentCard[] result = new DevelopmentCard[4];
        System.arraycopy(cards[row][column], 0, result, 0, 4);
        return result;
    }

    public boolean isOneColumnDestroyed() {
        boolean result = true;
        for (int c = 0; c < 4; c++) {
            result = true;
            loop:
            for (int r = 0; r < 3; r++) {
                for (int h = 0; h < 4; h++) {
                    if (cards[r][c][h] != null) {
                        result = false;
                        break loop;
                    }
                }
            }
            if (result) return true;
        }
        return false;
    }

    public void setGrid(DevelopmentCard[][][] grid) {
        this.cards = grid;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(" DEVELOPMENT DECK, ALL THE VISIBLE CARDS: ");

        DevelopmentCard[][] visible = this.getVisible();

        for (int i = 0; i < 3; i++) {
            result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET);
            result.append("\n").append(A.CYAN + "     I     I     I     I     I     I     I     " + A.RESET);
            result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
            result.append("\n").append(" Row ").append(i);
            for (int j = 0; j < 4; j++) {
                result.append("\n").append("  Column ").append(j);
                if (visible[i][j] == null)
                    result.append("\n").append(" X=====X Empty! X=====X");
                else
                    result.append("\n").append(visible[i][j].toString());
            }
        }
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET);
        result.append("\n").append(A.CYAN + "     I     I     I     I     I     I     I     " + A.RESET);
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_DevDeck generateMessage() {
        return new MSG_UPD_DevDeck(
                this.getVisible()
        );
    }
}