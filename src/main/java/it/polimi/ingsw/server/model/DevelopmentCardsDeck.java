package it.polimi.ingsw.server.model;

import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_DevDeck;
import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
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
                        Map.of(Resource.COIN, 3, Resource.SHIELD, 1)),
                "resources/cardsFront/DFRONT (46).png", "resources/cardsBack/BACK (10)"
        );

        cards[0][0][1] = new DevelopmentCard(3, Color.GREEN, 11,
                Map.of(Resource.SHIELD, 7),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.COIN, 1, Resource.FAITH, 3)),
                "resources/cardsFront/DFRONT (42).png", "resources/cardsBack/BACK (10)"
        );

        cards[0][0][2] = new DevelopmentCard(3, Color.GREEN, 10,
                Map.of(Resource.SHIELD, 5, Resource.SERVANT, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.SERVANT, 1),
                        Map.of(Resource.SHIELD, 2, Resource.STONE, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (38).png", "resources/cardsBack/BACK (10)"
        );

        cards[0][0][3] = new DevelopmentCard(3, Color.GREEN, 9,
                Map.of(Resource.SHIELD, 6),
                new Power(Map.of(Resource.COIN, 2),
                        Map.of(Resource.STONE, 3, Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (34).png", "resources/cardsBack/BACK (10)"
        );

        cards[0][1][0] = new DevelopmentCard(3, Color.BLUE, 12,
                Map.of(Resource.COIN, 4, Resource.STONE, 4),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.COIN, 1, Resource.SHIELD, 3)),
                "resources/cardsFront/DFRONT (48).png", "resources/cardsBack/BACK (12)"
        );

        cards[0][1][1] = new DevelopmentCard(3, Color.BLUE, 11,
                Map.of(Resource.COIN, 7),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.SHIELD, 1, Resource.FAITH, 3)),
                "resources/cardsFront/DFRONT (44).png", "resources/cardsBack/BACK (12)"
        );

        cards[0][1][2] = new DevelopmentCard(3, Color.BLUE, 10,
                Map.of(Resource.COIN, 5, Resource.STONE, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.SHIELD, 1),
                        Map.of(Resource.SERVANT, 2, Resource.STONE, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (40).png", "resources/cardsBack/BACK (12)"
        );

        cards[0][1][3] = new DevelopmentCard(3, Color.BLUE, 9,
                Map.of(Resource.COIN, 6),
                new Power(Map.of(Resource.SERVANT, 2),
                        Map.of(Resource.SHIELD, 3, Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (36).png", "resources/cardsBack/BACK (12)"
        );

        cards[0][2][0] = new DevelopmentCard(3, Color.YELLOW, 12,
                Map.of(Resource.STONE, 4, Resource.SERVANT, 4),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.STONE, 1, Resource.SERVANT, 3)),
                "resources/cardsFront/DFRONT (1).png", "resources/cardsBack/BACK (13)"
        );

        cards[0][2][1] = new DevelopmentCard(3, Color.YELLOW, 11,
                Map.of(Resource.STONE, 7),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.SERVANT, 1, Resource.FAITH, 3)),
                "resources/cardsFront/DFRONT (45).png", "resources/cardsBack/BACK (13)"
        );

        cards[0][2][2] = new DevelopmentCard(3, Color.YELLOW, 10,
                Map.of(Resource.STONE, 5, Resource.SERVANT, 2),
                new Power(Map.of(Resource.STONE, 1, Resource.SERVANT, 1),
                        Map.of(Resource.COIN, 2, Resource.SHIELD, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (41).png", "resources/cardsBack/BACK (13)"
        );

        cards[0][2][3] = new DevelopmentCard(3, Color.YELLOW, 9,
                Map.of(Resource.STONE, 6),
                new Power(Map.of(Resource.SHIELD, 2),
                        Map.of(Resource.SERVANT, 3, Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (37).png", "resources/cardsBack/BACK (13)"
        );

        cards[0][3][0] = new DevelopmentCard(3, Color.PURPLE, 12,
                Map.of(Resource.SERVANT, 4, Resource.SHIELD, 4),
                new Power(Map.of(Resource.COIN, 1),
                        Map.of(Resource.STONE, 3, Resource.SERVANT, 1)),
                "resources/cardsFront/DFRONT (47).png", "resources/cardsBack/BACK (11)"
        );

        cards[0][3][1] = new DevelopmentCard(3, Color.PURPLE, 11,
                Map.of(Resource.SERVANT, 7),
                new Power(Map.of(Resource.COIN, 1),
                        Map.of(Resource.STONE, 1, Resource.FAITH, 3)),
                "resources/cardsFront/DFRONT (43).png", "resources/cardsBack/BACK (11)"
        );

        cards[0][3][2] = new DevelopmentCard(3, Color.PURPLE, 10,
                Map.of(Resource.SERVANT, 5, Resource.COIN, 2),
                new Power(Map.of(Resource.STONE, 1, Resource.SHIELD, 1),
                        Map.of(Resource.COIN, 2, Resource.SERVANT, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (39).png", "resources/cardsBack/BACK (11)"
        );

        cards[0][3][3] = new DevelopmentCard(3, Color.PURPLE, 9,
                Map.of(Resource.SERVANT, 6),
                new Power(Map.of(Resource.STONE, 2),
                        Map.of(Resource.COIN, 3, Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (35).png", "resources/cardsBack/BACK (11)"
        );

        cards[1][0][0] = new DevelopmentCard(2, Color.GREEN, 8,
                Map.of(Resource.SHIELD, 3, Resource.COIN, 3),
                new Power(Map.of(Resource.COIN, 1),
                        Map.of(Resource.SHIELD, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (30).png", "resources/cardsBack/BACK (6)"
        );

        cards[1][0][1] = new DevelopmentCard(2, Color.GREEN, 7,
                Map.of(Resource.SHIELD, 5),
                new Power(Map.of(Resource.COIN, 2),
                        Map.of(Resource.STONE, 2, Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (26).png", "resources/cardsBack/BACK (6)"
        );

        cards[1][0][2] = new DevelopmentCard(2, Color.GREEN, 6,
                Map.of(Resource.SHIELD, 3, Resource.SERVANT, 2),
                new Power(Map.of(Resource.SHIELD, 1, Resource.SERVANT, 1),
                        Map.of(Resource.STONE, 3)),
                "resources/cardsFront/DFRONT (22).png", "resources/cardsBack/BACK (6)"
        );

        cards[1][0][3] = new DevelopmentCard(2, Color.GREEN, 5,
                Map.of(Resource.SHIELD, 4),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (18).png", "resources/cardsBack/BACK (6)"
        );

        cards[1][1][0] = new DevelopmentCard(2, Color.BLUE, 8,
                Map.of(Resource.COIN, 3, Resource.STONE, 3),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.STONE, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (32).png", "resources/cardsBack/BACK (8)"
        );

        cards[1][1][1] = new DevelopmentCard(2, Color.BLUE, 7,
                Map.of(Resource.COIN, 5),
                new Power(Map.of(Resource.SERVANT, 2),
                        Map.of(Resource.SHIELD, 2, Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (28).png", "resources/cardsBack/BACK (8)"
        );

        cards[1][1][2] = new DevelopmentCard(2, Color.BLUE, 6,
                Map.of(Resource.COIN, 3, Resource.STONE, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 3)),
                "resources/cardsFront/DFRONT (24).png", "resources/cardsBack/BACK (8)"
        );

        cards[1][1][3] = new DevelopmentCard(2, Color.BLUE, 5,
                Map.of(Resource.COIN, 4),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (20).png", "resources/cardsBack/BACK (8)"
        );

        cards[1][2][0] = new DevelopmentCard(2, Color.YELLOW, 8,
                Map.of(Resource.STONE, 3, Resource.SERVANT, 3),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.COIN, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (33).png", "resources/cardsBack/BACK (9)"
        );

        cards[1][2][1] = new DevelopmentCard(2, Color.YELLOW, 7,
                Map.of(Resource.STONE, 5),
                new Power(Map.of(Resource.SHIELD, 2),
                        Map.of(Resource.SERVANT, 2, Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (29).png", "resources/cardsBack/BACK (9)"
        );

        cards[1][2][2] = new DevelopmentCard(2, Color.YELLOW, 6,
                Map.of(Resource.STONE, 3, Resource.SHIELD, 2),
                new Power(Map.of(Resource.STONE, 1, Resource.SHIELD, 1),
                        Map.of(Resource.COIN, 3)),
                "resources/cardsFront/DFRONT (25).png", "resources/cardsBack/BACK (9)"
        );

        cards[1][2][3] = new DevelopmentCard(2, Color.YELLOW, 5,
                Map.of(Resource.STONE, 4),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (21).png", "resources/cardsBack/BACK (9)"
        );

        cards[1][3][0] = new DevelopmentCard(2, Color.PURPLE, 8,
                Map.of(Resource.SERVANT, 3, Resource.SHIELD, 3),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (31).png", "resources/cardsBack/BACK (7)"
        );

        cards[1][3][1] = new DevelopmentCard(2, Color.PURPLE, 7,
                Map.of(Resource.SERVANT, 5),
                new Power(Map.of(Resource.STONE, 2),
                        Map.of(Resource.COIN, 2, Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (27).png", "resources/cardsBack/BACK (7)"
        );

        cards[1][3][2] = new DevelopmentCard(2, Color.PURPLE, 6,
                Map.of(Resource.SERVANT, 3, Resource.COIN, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.SERVANT, 1),
                        Map.of(Resource.SHIELD, 3)),
                "resources/cardsFront/DFRONT (23).png", "resources/cardsBack/BACK (7)"
        );

        cards[1][3][3] = new DevelopmentCard(2, Color.PURPLE, 5,
                Map.of(Resource.SERVANT, 4),
                new Power(Map.of(Resource.COIN, 1),
                        Map.of(Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (19).png", "resources/cardsBack/BACK (7)"
        );

        cards[2][0][0] = new DevelopmentCard(1, Color.GREEN, 4,
                Map.of(Resource.SHIELD, 2, Resource.COIN, 2),
                new Power(Map.of(Resource.STONE, 1, Resource.SERVANT, 1),
                        Map.of(Resource.COIN, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (14).png", "resources/cardsBack/BACK (2)"
        );

        cards[2][0][1] = new DevelopmentCard(1, Color.GREEN, 3,
                Map.of(Resource.SHIELD, 3),
                new Power(Map.of(Resource.SERVANT, 2),
                        Map.of(Resource.COIN, 1, Resource.SHIELD, 1, Resource.STONE, 1)),
                "resources/cardsFront/DFRONT (10).png", "resources/cardsBack/BACK (2)"
        );

        cards[2][0][2] = new DevelopmentCard(1, Color.GREEN, 2,
                Map.of(Resource.SHIELD, 1, Resource.SERVANT, 1, Resource.STONE, 1),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 1)),
                "resources/cardsFront/DFRONT (6).png", "resources/cardsBack/BACK (2)"
        );

        cards[2][0][3] = new DevelopmentCard(1, Color.GREEN, 1,
                Map.of(Resource.SHIELD, 2),
                new Power(Map.of(Resource.COIN, 1),
                        Map.of(Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (2).png", "resources/cardsBack/BACK (2)"
        );

        cards[2][1][0] = new DevelopmentCard(1, Color.BLUE, 4,
                Map.of(Resource.COIN, 2, Resource.SERVANT, 2),
                new Power(Map.of(Resource.SHIELD, 1, Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (16).png", "resources/cardsBack/BACK (4)"
        );

        cards[2][1][1] = new DevelopmentCard(1, Color.BLUE, 3,
                Map.of(Resource.COIN, 3),
                new Power(Map.of(Resource.STONE, 2),
                        Map.of(Resource.COIN, 1, Resource.SERVANT, 1, Resource.SHIELD, 1)),
                "resources/cardsFront/DFRONT (12).png", "resources/cardsBack/BACK (4)"
        );

        cards[2][1][2] = new DevelopmentCard(1, Color.BLUE, 2,
                Map.of(Resource.COIN, 1, Resource.SERVANT, 1, Resource.STONE, 1),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.STONE, 1)),
                "resources/cardsFront/DFRONT (8).png", "resources/cardsBack/BACK (4)"
        );

        cards[2][1][3] = new DevelopmentCard(1, Color.BLUE, 1,
                Map.of(Resource.COIN, 2),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (4).png", "resources/cardsBack/BACK (4)"
        );

        cards[2][2][0] = new DevelopmentCard(1, Color.YELLOW, 4,
                Map.of(Resource.STONE, 2, Resource.SHIELD, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.SERVANT, 1),
                        Map.of(Resource.SHIELD, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (17).png", "resources/cardsBack/BACK (5)"
        );

        cards[2][2][1] = new DevelopmentCard(1, Color.YELLOW, 3,
                Map.of(Resource.STONE, 3),
                new Power(Map.of(Resource.SHIELD, 2),
                        Map.of(Resource.COIN, 1, Resource.SERVANT, 1, Resource.STONE, 1)),
                "resources/cardsFront/DFRONT (13).png", "resources/cardsBack/BACK (5)"
        );

        cards[2][2][2] = new DevelopmentCard(1, Color.YELLOW, 2,
                Map.of(Resource.SHIELD, 1, Resource.STONE, 1, Resource.COIN, 1),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.COIN, 1)),
                "resources/cardsFront/DFRONT (9).png", "resources/cardsBack/BACK (5)"
        );

        cards[2][2][3] = new DevelopmentCard(1, Color.YELLOW, 1,
                Map.of(Resource.STONE, 2),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (5).png", "resources/cardsBack/BACK (5)"
        );

        cards[2][3][0] = new DevelopmentCard(1, Color.PURPLE, 4,
                Map.of(Resource.SERVANT, 2, Resource.STONE, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.SHIELD, 1),
                        Map.of(Resource.STONE, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (15).png", "resources/cardsBack/BACK (3)"
        );

        cards[2][3][1] = new DevelopmentCard(1, Color.PURPLE, 3,
                Map.of(Resource.SERVANT, 3),
                new Power(Map.of(Resource.COIN, 2),
                        Map.of(Resource.SERVANT, 1, Resource.SHIELD, 1, Resource.STONE, 1)),
                "resources/cardsFront/DFRONT (11).png", "resources/cardsBack/BACK (3)"
        );

        cards[2][3][2] = new DevelopmentCard(1, Color.PURPLE, 2,
                Map.of(Resource.SHIELD, 1, Resource.SERVANT, 1, Resource.COIN, 1),
                new Power(Map.of(Resource.COIN, 1),
                        Map.of(Resource.SHIELD, 1)),
                "resources/cardsFront/DFRONT (7).png", "resources/cardsBack/BACK (3)"
        );

        cards[2][3][3] = new DevelopmentCard(1, Color.PURPLE, 1,
                Map.of(Resource.SERVANT, 2),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (3).png", "resources/cardsBack/BACK (3)"
        );

        shuffle();
    }

    /**
     * Shuffle individually all the 12 small decks of 4 cards in the DevelopmentCardsDeck, so that it does not mix the colors nor the levels of the cards.
     * Note: it should not be called after a card is removed, because it would shuffle a null element.
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
     * Removes a specified Card from the DevelopmentCardsDeck and returns it.
     * Also notifies the observers.
     *
     * @param row    A row of the DevelopmentCardsDeck.
     * @param column A column of the DevelopmentCardsDeck.
     * @return The card that that is being removed, or null if the selected deck was empty or the row and column parameters were out of bounds.
     */
    public DevelopmentCard removeCard(int row, int column) {
        if (row < 0 || row > 2) return null;
        if (column < 0 || column > 3) return null;
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
     * It is used in the Solo mode with Lorenzo. It removes 2 cards of a column starting from the bottom (level 1 to level 3).
     * Also notifies the observers.
     *
     * @param column A column of the DevelopmentCardsDeck.
     * @return True if the cards are removed correctly, False if the column is < 0 or > 3.
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
     * Returns a DevelopmentCard matrix containing all the Visible Cards of the DevelopmentCardsDeck.
     *
     * @return A DevelopmentCard matrix containing all the Visible Cards of the DevelopmentCardsDeck.
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
     * Returns a DevelopmentCard array containing all the DevelopmentCards from a specified deck.
     *
     * @param row    A row of the DevelopmentCardsDeck.
     * @param column A column of the DevelopmentCardsDeck.
     * @return A DevelopmentCard[] stack of 4 cards in that position, or null if the parameters were out of bounds.
     */
    public DevelopmentCard[] getStack(int row, int column) {
        if (row < 0 || row > 2 || column < 0 || column > 3) return null;
        DevelopmentCard[] result = new DevelopmentCard[4];
        System.arraycopy(cards[row][column], 0, result, 0, 4);
        return result;
    }

    /**
     * Used in Solo mode, checks if a column is destroyed.
     *
     * @return True if the column has been destroyed, False otherwise.
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
     * Sets a custom grid.
     *
     * @param grid A grid of DevelopmentCards.
     */
    public void setGrid(DevelopmentCard[][][] grid) {
        this.cards = grid;
    }


    /**
     * Creates a message using generateMessage() and notifies observers.
     *
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_UPD_DevDeck representing the current state of the DevelopmentCardsDeck.
     *
     * @return A MSG_UPD_DevDeck representing the current state of the DevelopmentCardsDeck.
     */
    public MSG_UPD_DevDeck generateMessage() {
        return new MSG_UPD_DevDeck(
                this.getVisible()
        );
    }
}