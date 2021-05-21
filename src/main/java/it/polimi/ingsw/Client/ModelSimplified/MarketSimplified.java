package it.polimi.ingsw.Client.ModelSimplified;


import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_Market;
import it.polimi.ingsw.Server.Model.Marbles.MarketMarble;
import it.polimi.ingsw.Server.Utils.Displayer;

public class MarketSimplified {
    private final MarketMarble[][] grid;
    private MarketMarble slideMarble;

    public MarketSimplified() {
        grid = new MarketMarble[3][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                this.grid[i][j] = null;
            }
        }
        slideMarble = null;
    }

    public void update(MSG_UPD_Market message) {
        MarketMarble newSlideMarble = message.getSlideMarble();
        MarketMarble[][] newGrid = message.getGrid();

        this.slideMarble = newSlideMarble;
        for (int i = 0; i < 3; i++) {
            System.arraycopy(newGrid[i], 0, this.grid[i], 0, 4);
        }
    }

    @Override
    public String toString() {
        return Displayer.marketToString(this.grid, this.slideMarble);
    }
}
