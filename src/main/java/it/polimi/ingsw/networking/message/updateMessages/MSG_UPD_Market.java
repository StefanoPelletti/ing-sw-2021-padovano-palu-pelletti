package it.polimi.ingsw.networking.message.updateMessages;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.model.marbles.MarketMarble;

import java.io.Serializable;

public class MSG_UPD_Market extends Message implements Serializable {

    private final MarketMarble[][] grid;
    private final MarketMarble slideMarble;

    public MSG_UPD_Market(MarketMarble slideMarble, MarketMarble[][] grid) {
        super(MessageType.MSG_UPD_Market);

        this.slideMarble = slideMarble;
        this.grid = new MarketMarble[3][4];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(grid[i], 0, this.grid[i], 0, 4);
        }
    }

    public MarketMarble[][] getGrid() {
        return grid;
    }

    public MarketMarble getSlideMarble() {
        return slideMarble;
    }
}