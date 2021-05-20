package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_Market;
import it.polimi.ingsw.Server.Model.Marbles.*;
import it.polimi.ingsw.Server.Utils.Displayer;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.ArrayList;
import java.util.Collections;

public class Market extends ModelObservable {
    private MarketMarble[][] grid;
    private MarketMarble slideMarble;

    private ArrayList<MarketMarble> internalList;

    //4 white, 2 blue, 2 grey, 2 yellow, 2 purple, 1 red

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

    public ArrayList<MarketMarble> pushRow(int row) //row must be between 0 and 2 (included) (or refactored)
    {
        if (row < 0 || row > 2) return null;
        ArrayList<MarketMarble> result = getRow(row);

        MarketMarble tmp = grid[row][0];
        grid[row][0] = grid[row][1];
        grid[row][1] = grid[row][2];
        grid[row][2] = grid[row][3];
        grid[row][3] = slideMarble;
        slideMarble = tmp;
        notifyObservers();
        return result;
    }

    public ArrayList<MarketMarble> getRow(int row) {
        if (row < 0 || row > 2) return null;
        ArrayList<MarketMarble> result = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            result.add(grid[row][i]);
        return result;
    }

    public ArrayList<MarketMarble> pushColumn(int column) {
        if (column < 0 || column > 3) return null;
        ArrayList<MarketMarble> result = getColumn(column);

        MarketMarble tmp = grid[0][column];
        grid[0][column] = grid[1][column];
        grid[1][column] = grid[2][column];
        grid[2][column] = slideMarble;
        slideMarble = tmp;

        notifyObservers();
        return result;
    }

    public ArrayList<MarketMarble> getColumn(int column) {
        if (column < 0 || column > 3) return null;
        ArrayList<MarketMarble> result = new ArrayList<>();
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

    //used only for testing, does not notify the observers
    public void setGrid(MarketMarble[][] grid, MarketMarble slideMarble) {
        this.grid = grid;
        this.slideMarble = slideMarble;
    }

    @Override
    public String toString() {
        return Displayer.marketToString(this.grid, this.slideMarble);
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_Market generateMessage() {
        return new MSG_UPD_Market(
                slideMarble,
                grid
        );
    }
}