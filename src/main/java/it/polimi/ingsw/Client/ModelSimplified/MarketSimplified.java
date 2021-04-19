package it.polimi.ingsw.Client.ModelSimplified;


import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_Market;
import it.polimi.ingsw.Server.Model.Marbles.MarketMarble;

public class MarketSimplified {
    private MarketMarble[][] grid;
    private MarketMarble slideMarble;

    public MarketSimplified()
    {
        grid = new MarketMarble[3][4];
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<4; j++)
            {
                this.grid[i][j] = null;
            }
        }
        slideMarble = null;
    }

    public void update(MSG_UPD_Market message){
        slideMarble = message.getSlideMarble();
        MarketMarble[][] newGrid = message.getGrid();
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<4; j++)
            {
                this.grid[i][j] = newGrid[i][j];
            }
        }
    }

    @Override
    public String toString() {
        return " -- Market: -- " +
                "\n SlideMarble : " + slideMarble.toString() +
                "\n [ "+ grid[0][0].toAbbreviation()+ " | "+grid[0][1].toAbbreviation()+ " | "
                +grid[0][2].toAbbreviation()+ " | "+grid[0][3].toAbbreviation()+ " ]"+
                "\n [ "+ grid[1][0].toAbbreviation()+ " | "+grid[1][1].toAbbreviation()+ " | "
                +grid[1][2].toAbbreviation()+ " | "+grid[1][3].toAbbreviation()+ " ]"+
                "\n [ "+ grid[2][0].toAbbreviation()+ " | "+grid[2][1].toAbbreviation()+ " | "
                +grid[2][2].toAbbreviation()+ " | "+grid[2][3].toAbbreviation()+ " ]"+"\n";
    }
}
