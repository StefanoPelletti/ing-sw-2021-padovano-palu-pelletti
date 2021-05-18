package it.polimi.ingsw.Client.ModelSimplified;


import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_Market;
import it.polimi.ingsw.Server.Model.Marbles.MarketMarble;

public class MarketSimplified {
    private MarketMarble[][] grid;
    private MarketMarble slideMarble;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";

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
        StringBuilder result = new StringBuilder();
        result.append(ANSI_CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + ANSI_RESET).append("\n");
        result.append("                   MARKET!                ").append("\n");
        result.append("      Marble on the Slide: ").append(slideMarble).append("\n").append("\n");
        result.append("             [ ").append(grid[0][0].toAbbreviation()).append(" | ").append(grid[0][1].toAbbreviation()).append(" | ").append(grid[0][2].toAbbreviation()).append(" | ").append(grid[0][3].toAbbreviation()).append(" ]").append("\n");
        result.append("             [ ").append(grid[1][0].toAbbreviation()).append(" | ").append(grid[1][1].toAbbreviation()).append(" | ").append(grid[1][2].toAbbreviation()).append(" | ").append(grid[1][3].toAbbreviation()).append(" ]").append("\n");
        result.append("             [ ").append(grid[2][0].toAbbreviation()).append(" | ").append(grid[2][1].toAbbreviation()).append(" | ").append(grid[2][2].toAbbreviation()).append(" | ").append(grid[2][3].toAbbreviation()).append(" ]").append("\n");
        result.append(ANSI_CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + ANSI_RESET).append("\n");
        return result.toString();
    }
}
