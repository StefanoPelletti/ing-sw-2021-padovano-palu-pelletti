package it.polimi.ingsw.Networking.Message.UpdateMessages;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.Marbles.MarketMarble;

import java.io.Serializable;

public class MSG_UPD_Market extends Message implements Serializable {

    private final MarketMarble[][] grid;
    private final MarketMarble slideMarble;

    public MSG_UPD_Market(MarketMarble slideMarble, MarketMarble[][] grid)
    {
        super(MessageType.MSG_UPD_Market);

        this.slideMarble = slideMarble;
        this.grid = new MarketMarble[3][4];
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<4; j++)
            {
                this.grid[i][j] = grid[i][j];
            }
        }
    }

    public MarketMarble[][] getGrid() {
        return grid;
    }
    public MarketMarble getSlideMarble() {
        return slideMarble;
    }

    public MessageType getMessageType() { return super.getMessageType();}
}
