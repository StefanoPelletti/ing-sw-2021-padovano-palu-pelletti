package it.polimi.ingsw.client.modelSimplified;


import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Market;
import it.polimi.ingsw.server.model.marbles.MarketMarble;
import it.polimi.ingsw.server.utils.A;

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

    public MarketMarble[][] getGrid() {
        return this.grid;
    }

    public MarketMarble getSlideMarble() {
        return this.slideMarble;
    }

    /**
     * Returns the representation of the current state of a given Market.
     *
     * @return A String representing the current state of the Market.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append("                   MARKET!                ").append("\n");
        result.append("      Marble on the Slide: ").append(slideMarble.toStringColored()).append("\n").append("\n");
        result.append("             [ ").append(grid[0][0].toAbbreviation()).append(" | ").append(grid[0][1].toAbbreviation()).append(" | ").append(grid[0][2].toAbbreviation()).append(" | ").append(grid[0][3].toAbbreviation()).append(" ]").append("\n");
        result.append("             [ ").append(grid[1][0].toAbbreviation()).append(" | ").append(grid[1][1].toAbbreviation()).append(" | ").append(grid[1][2].toAbbreviation()).append(" | ").append(grid[1][3].toAbbreviation()).append(" ]").append("\n");
        result.append("             [ ").append(grid[2][0].toAbbreviation()).append(" | ").append(grid[2][1].toAbbreviation()).append(" | ").append(grid[2][2].toAbbreviation()).append(" | ").append(grid[2][3].toAbbreviation()).append(" ]").append("\n");
        result.append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }
}
