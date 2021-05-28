package it.polimi.ingsw.server.model;

import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_DevDeck;
import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.*;

public class DevelopmentCardsDeck extends ModelObservable {
    private DevelopmentCard[][][] cards;
    private List<DevelopmentCard> internalList;

    /**
     * Constructor of the DevelopmentCardsDeck. All the 48 cards are initialized here and then shuffled.
     */
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

    /**
     * Given:
     * @param cards a grid of the top cards (the ones the are visible) of the DevelopmentCardsDeck
     * @return a string the represents the current state of the DevelopmentCardsDeck.
     */
    public static String toString(DevelopmentCard[][] cards) {
        StringBuilder result = new StringBuilder(" DEVELOPMENT DECK, ALL THE VISIBLE CARDS: ");

        for (int i = 0; i < 3; i++) {
            result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET);
            result.append("\n").append(A.CYAN + "     I     I     I     I     I     I     I     " + A.RESET);
            result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
            result.append("\n").append(" Row of Cards level ");
            if(i==0) result.append(3);
            if(i==1) result.append(2);
            if(i==2) result.append(1);
            for (int j = 0; j < 4; j++) {
                result.append("\n").append("  Column of ");
                if(j==0) result.append(A.GREEN+"GREEN"+A.RESET+" cards");
                if(j==1) result.append(A.BLUE+"BLUE"+A.RESET+" cards");
                if(j==2) result.append(A.YELLOW+"YELLOW"+A.RESET+" cards");
                if(j==3) result.append(A.PURPLE +"PURPLE"+A.RESET+" cards");

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

    /**
     * shuffles individually all the 12 small decks of 4 cards in the DevelopmentCardsDeck, so that it does not mix the colors or the levels of the cards.
     * NOTE: it should not be called after a card is removed, because it would shuffle a null element.
     */
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

    /**
     * Given
     * @param row a row of the DevelopmentCardsDeck
     * @param column a column of the DevelopmentCardsDeck
     * @return the card that the user wants to buy, after removing it from the small deck in which it is in.
     */
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

    /**
     * It is used in the solo mode with Lorenzo. It removes 2 cards of a column starting from the bottom.
     * @param column a column of the DevelopmentCardsDeck
     * @return true if the cards are removed correctly, false if the column is < 0 or > 3
     */
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

    /**
     * @return all the cards on top (that the user can see) of the DevelopmentCardsDeck.
     */
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

    /**
     * Given:
     * @param row a row of the DevelopmentCardsDeck
     * @param column a column of the DevelopmentCardsDeck
     * @return the stack of 4 cards in that position.
     */
    public DevelopmentCard[] getStack(int row, int column) {
        if (row < 0 || row > 2 || column < 0 || column > 3) return null;
        DevelopmentCard[] result = new DevelopmentCard[4];
        System.arraycopy(cards[row][column], 0, result, 0, 4);
        return result;
    }

    /**
     * Used in solo mode, checks if a column is destroyed.
     * @return true if the column has been destroyed.
     */
    public boolean isOneColumnDestroyed() {
        boolean result;
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

    /**
     * sets the grid.
     * @param grid a grid of DevelopmentCards
     */
    public void setGrid(DevelopmentCard[][][] grid) {
        this.cards = grid;
    }

    @Override
    /**
     * Calls the toString() method above.
     */
    public String toString() {
        return DevelopmentCardsDeck.toString(getVisible());
    }

    /**
     * notifies the observers by sending a message that contains the actual internal status of the DevelopmentCardsDeck.
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     *
     * @return the actual message passed by the notifyObservers() method. The update message contains all the visible cards.
     */
    public MSG_UPD_DevDeck generateMessage() {
        return new MSG_UPD_DevDeck(
                this.getVisible()
        );
    }
}