package it.polimi.ingsw.server.model;

import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Market;
import it.polimi.ingsw.server.model.marbles.*;
import it.polimi.ingsw.server.utils.A;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Market extends ModelObservable {
    private final ArrayList<MarketMarble> internalList;
    private MarketMarble[][] grid;
    private MarketMarble slideMarble;

    /**
     * CONSTRUCTOR
     * Creates randomly a matrix of MarketMarbles and the slideMarble:
     *     - 4 white marbles
     *     - 2 blue marbles
     *     - 2 grey marbles
     *     - 2 yellow marbles
     *     - 2 purple marbles
     *     - 1 red marble
     */
    public Market() {
        grid = new MarketMarble[3][4];

        internalList = new ArrayList<>();

        internalList.add(new WhiteMarble());
        internalList.add(new WhiteMarble());
        internalList.add(new WhiteMarble());
        internalList.add(new WhiteMarble());

        internalList.add(new BlueMarble());
        internalList.add(new BlueMarble());

        internalList.add(new GreyMarble());
        internalList.add(new GreyMarble());

        internalList.add(new YellowMarble());
        internalList.add(new YellowMarble());

        internalList.add(new PurpleMarble());
        internalList.add(new PurpleMarble());

        internalList.add(new RedMarble());

        shuffle();
    }

    /**
     *
     * @param grid the grid of the Market
     * @param slideMarble the slideMarble of the market
     * @return a String representing the current state of the Market
     */
    public static String toString(MarketMarble[][] grid, MarketMarble slideMarble) {
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

    /**
     * Shuffles the MarketMarbles of the Market, slideMarble included
     * Also notifies observers
     */
    public void shuffle() {
        Collections.shuffle(internalList);
        int tmp = 1;
        slideMarble = internalList.get(0);
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                grid[r][c] = internalList.get(tmp);
                tmp++;
            }
        }
        notifyObservers();
    }

    /**
     * Pushes a row of the grid:
     *     - SlideMarble replaces the last position of the row (starting from the right)
     *     - Every Marble of the row is shifted left
     *     - The first Marble in the row becomes the new SlideMarble
     * Also notifies the observers
     * @param row the row of the Market to push
     * @return a List of the Marbles in the row (before the push)
     */
    public List<MarketMarble> pushRow(int row) //row must be between 0 and 2 (included) (or refactored)
    {
        if (row < 0 || row > 2) return null;
        List<MarketMarble> result = getRow(row);

        MarketMarble tmp = grid[row][0];
        grid[row][0] = grid[row][1];
        grid[row][1] = grid[row][2];
        grid[row][2] = grid[row][3];
        grid[row][3] = slideMarble;
        slideMarble = tmp;
        notifyObservers();
        return result;
    }

    /**
     *
     * @param row the row of Market to get
     * @return a List of the Marbles in the row
     */
    public List<MarketMarble> getRow(int row) {
        if (row < 0 || row > 2) return null;
        List<MarketMarble> result = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            result.add(grid[row][i]);
        return result;
    }

    /**
     * Pushes a column of the grid:
     *     - SlideMarble replaces the last position of the column (starting from the bottom)
     *     - Every Marble of the column is shifted up
     *     - The first Marble in the column becomes the new SlideMarble
     * Also notifies the observers
     * @param column the column of the Market to push
     * @return a List of the Marbles in the column (before the push)
     */
    public List<MarketMarble> pushColumn(int column) {
        if (column < 0 || column > 3) return null;
        List<MarketMarble> result = getColumn(column);

        MarketMarble tmp = grid[0][column];
        grid[0][column] = grid[1][column];
        grid[1][column] = grid[2][column];
        grid[2][column] = slideMarble;
        slideMarble = tmp;

        notifyObservers();
        return result;
    }

    /**
     *
     * @param column the column of Market to get
     * @return a List of the Marbles in the column
     */
    public List<MarketMarble> getColumn(int column) {
        if (column < 0 || column > 3) return null;
        List<MarketMarble> result = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            result.add(grid[i][column]);
        return result;
    }

    public MarketMarble getSlideMarble() {
        return this.slideMarble;
    }

    public MarketMarble[][] getGrid() {
        MarketMarble[][] result = new MarketMarble[3][4];
        for (int i = 0; i < 3; i++) {
            result[i] = grid[i].clone();
        }
        return result;
    }

    public void setGrid(MarketMarble[][] grid, MarketMarble slideMarble) {
        this.grid = grid;
        this.slideMarble = slideMarble;
    }

    @Override
    public String toString() {
        return Market.toString(this.grid, this.slideMarble);
    }

    /**
     * creates a message and notifies observers
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     *
     * @return a MSG_UPD_Market containing the current state of the Market
     */
    public MSG_UPD_Market generateMessage() {
        return new MSG_UPD_Market(
                slideMarble,
                grid
        );
    }
}