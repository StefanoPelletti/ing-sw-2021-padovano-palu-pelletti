package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.server.model.Market;
import it.polimi.ingsw.server.model.marbles.MarketMarble;
import it.polimi.ingsw.server.model.marbles.WhiteMarble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MarketTest {

    Market market;

    @BeforeEach
    public void reset() {
        market = new Market();
    }

    //tests that slideMarble is never null
    @Test
    public void slideMarbleNotNull() {
        assertNotNull(market.getSlideMarble());
    }

    //verifies that methods getColumn(column) and getRow(row) are not null if column/row are valid
    @Test
    public void getValidRowColumn() {
        assertNotNull(market.getColumn(0));
        assertNotNull(market.getRow(0));
    }

    //verifies that methods pushColumn(column) and pushRow(row) are not null if column/row are valid
    @Test
    public void pushValidRowColumn() {
        assertNotNull(market.pushColumn(0));
        assertNotNull(market.pushRow(0));
    }

    //verifies the method getColumn(column) returns null if column is invalid
    //(minor than 0 or greater than 3)
    @Test
    public void getNotValidColumn() {
        assertNull(market.getColumn(-1));
        assertNull(market.getColumn(40));
    }

    //verifies the method getRow(row) returns null if row is invalid
    //(minor than 0 or greater than 2)
    @Test
    public void getNotValidRow() {
        assertNull(market.getRow(-1));
        assertNull(market.getRow(3));
    }

    //verifies the method pushColumn(column) returns null if column is invalid
    //(minor than 0 or greater than 3)
    @Test
    public void pushNotValidColumn() {
        assertNull(market.pushColumn(-1));
        assertNull(market.pushColumn(4));
    }

    //verifies the method pushRow(row) returns null if row is invalid
    //(minor than 0 or greater than 2)
    @Test
    public void pushNotValidRow() {
        assertNull(market.pushRow(-1));
        assertNull(market.pushRow(3));
    }

    //verifies that methods getColumn(column) and pushColumn(column) return the same marbles
    @Test
    public void getAndPushColumn() {
        List<MarketMarble> gMarbles = market.getColumn(1);
        List<MarketMarble> pMarbles = market.pushColumn(1);
        assertArrayEquals(gMarbles.toArray(), pMarbles.toArray());
    }

    //verifies that methods getRow(column) and pushRow(column) return the same marbles
    @Test
    public void getAndPushRow() {
        List<MarketMarble> gMarbles = market.getRow(1);
        List<MarketMarble> pMarbles = market.pushRow(1);
        assertArrayEquals(gMarbles.toArray(), pMarbles.toArray());
    }

    //tests that the method pushColumn(column) works as intended
    //(pushes the slide marble as last place in the column, shifts the other marbles,
    //first marble in the column is the new slideMarble)
    @Test
    public void pushAndGetColumn() {
        MarketMarble priorSlideMarble = market.getSlideMarble();
        List<MarketMarble> priorMarbles = market.getColumn(2);
        market.pushColumn(2);
        List<MarketMarble> postMarbles = market.getColumn(2);
        MarketMarble postSlideMarble = market.getSlideMarble();

        priorMarbles.add(3, priorSlideMarble);
        postMarbles.add(0, postSlideMarble);
        assertArrayEquals(priorMarbles.toArray(), postMarbles.toArray());
    }

    //tests that the method pushRow(row) works as intended
    //(pushes the slide marble as last place in the row, shifts the other marbles,
    //first marble in the row is the new slideMarble)
    @Test
    public void pushAndGetRow() {
        MarketMarble priorSlideMarble = market.getSlideMarble();
        List<MarketMarble> priorMarbles = market.getRow(1);
        market.pushRow(1);
        List<MarketMarble> postMarbles = market.getRow(1);
        MarketMarble postSlideMarble = market.getSlideMarble();

        priorMarbles.add(4, priorSlideMarble);
        postMarbles.add(0, postSlideMarble);
        assertArrayEquals(priorMarbles.toArray(), postMarbles.toArray());
    }

    @Test
    public void testSet() {
        MarketMarble[][] re = {{new WhiteMarble(), new WhiteMarble(), new WhiteMarble(), new WhiteMarble()},
                {new WhiteMarble(), new WhiteMarble(), new WhiteMarble(), new WhiteMarble()},
                {new WhiteMarble(), new WhiteMarble(), new WhiteMarble(), new WhiteMarble()}};

        market.setGrid(re, new WhiteMarble());

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                assertTrue(market.getGrid()[r][c] instanceof WhiteMarble);
            }
        }
    }

    @Test
    public void toStringFormat() {
        System.out.println(market.toString());
        assertTrue(true);
    }
}