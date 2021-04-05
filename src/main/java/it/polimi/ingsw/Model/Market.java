package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Marbles.*;

import java.util.ArrayList;
import java.util.Collections;

public class Market {
    private MarketMarble[][] grid;
    private MarketMarble slideMarble;

    private ArrayList<MarketMarble> internalList;

    //4 white, 2 blue, 2 grey, 2 yellow, 2 purple, 1 red

    public Market()
    {
        grid = new MarketMarble[3][4];

        internalList = new ArrayList<MarketMarble>();

        internalList.add( new WhiteMarble() );
        internalList.add( new WhiteMarble() );
        internalList.add( new WhiteMarble() );
        internalList.add( new WhiteMarble() );

        internalList.add( new BlueMarble() );
        internalList.add( new BlueMarble() );

        internalList.add( new GreyMarble() ) ;
        internalList.add( new GreyMarble() );

        internalList.add( new YellowMarble() );
        internalList.add( new YellowMarble() );

        internalList.add( new PurpleMarble() );
        internalList.add( new PurpleMarble() );

        internalList.add( new RedMarble() );

        shuffle();
    }

    public void shuffle()
    {
        Collections.shuffle( internalList );
        int tmp = 1;
        slideMarble = internalList.get( 0 );
        for( int r = 0; r < 3; r++ )
        {
            for( int c = 0; c < 4; c++ )
            {
                grid[r][c] = internalList.get( tmp );
                tmp++;
            }
        }
    }

    public ArrayList<MarketMarble> pushRow( int row ) //row must be between 0 and 2 (included) (or refactored)
    {
        ArrayList<MarketMarble> result = new ArrayList<MarketMarble>();
        for( int i = 0; i < 4; i++ )
            result.add( grid[row][i] );

        MarketMarble tmp = grid[row][0];
        grid[row][0] = grid[row][1];
        grid[row][1] = grid[row][2];
        grid[row][2] = grid[row][3];
        grid[row][3] = slideMarble;
        slideMarble = tmp;

        return result;
    }

    public ArrayList<MarketMarble> getRow( int row )
    {
        ArrayList<MarketMarble> result = new ArrayList<MarketMarble>();
        for( int i = 0; i < 4; i++ )
            result.add( grid[row][i] );
        return result;
    }

    public ArrayList<MarketMarble> pushColumn( int column )
    {
        ArrayList<MarketMarble> result = new ArrayList<MarketMarble>();
        for( int i = 0; i < 3; i++ )
            result.add( grid[i][column] );

        MarketMarble tmp = grid[0][column];
        grid[0][column] = grid[1][column];
        grid[1][column] = grid[2][column];
        grid[2][column] = slideMarble;
        slideMarble = tmp;

        return result;
    }

    public ArrayList<MarketMarble> getColumn( int column )
    {
        ArrayList<MarketMarble> result = new ArrayList<MarketMarble>();
        for( int i = 0; i < 3; i++ )
            result.add( grid[i][column] );
        return result;
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
