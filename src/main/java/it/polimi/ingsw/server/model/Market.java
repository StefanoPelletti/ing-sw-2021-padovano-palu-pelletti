package it.polimi.ingsw.server.model;

import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Market;
import it.polimi.ingsw.server.model.marbles.*;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Market extends ModelObservable {
    private final List<MarketMarble> internalList;
    private MarketMarble[][] grid;
    private MarketMarble slideMarble;

    /**
     * CONSTRUCTOR
     * Creates randomly a matrix of MarketMarbles and the slideMarble:
     * <ul>
     * <li> 4 white marbles
     * <li> 2 blue marbles
     * <li> 2 grey marbles
     * <li> 2 yellow marbles
     * <li> 2 purple marbles
     * <li> 1 red marble
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
     * Shuffles the MarketMarbles of the Market, slideMarble included.
     * Also notifies observers.
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
     * <ul>
     * <li> SlideMarble replaces the last position of the row (starting from the right)
     * <li> Every Marble of the row is shifted left
     * <li> The first Marble in the row becomes the new SlideMarble.
     * </ul>
     * Also notifies the observers.
     *
     * @param row The row of the Market to push.
     * @return A List of the Marbles in the row (before the push), or null if an incorrect row value was inserted.
     * @see #getRow(int)
     */
    public List<MarketMarble> pushRow(int row) {
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
     * Returns A List of the Marbles in a specified row.
     *
     * @param row The row of Market to get.
     * @return A List of the Marbles in the specified row, or null if an incorrect row value was inserted.
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
     * <ul>
     * <li> SlideMarble replaces the last position of the column (starting from the bottom)
     * <li> Every Marble of the column is shifted up
     * <li> The first Marble in the column becomes the new SlideMarble
     * </ul>
     * Also notifies the observers.
     *
     * @param column The column of the Market to push.
     * @return A List of the Marbles in the column (before the push), or null if an incorrect column value was inserted.
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
     * Returns A List of the Marbles in a specified column.
     *
     * @param column The column of Market to get.
     * @return A List of the Marbles in the specified column, or null if an incorrect column value was inserted.
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

    /**
     * Replaces the current grid and SlideMarble with custom ones.
     * May break game rules, willingly.
     *
     * @param grid        The new MarketMarble[][] matrix.
     * @param slideMarble The new SlideMarble.
     */
    public void setGrid(MarketMarble[][] grid, MarketMarble slideMarble) {
        this.grid = grid;
        this.slideMarble = slideMarble;
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
     * Returns a MSG_UPD_Market representing the current state of the Market.
     *
     * @return A MSG_UPD_Market representing the current state of the Market.
     */
    public MSG_UPD_Market generateMessage() {
        return new MSG_UPD_Market(
                slideMarble,
                grid
        );
    }
}