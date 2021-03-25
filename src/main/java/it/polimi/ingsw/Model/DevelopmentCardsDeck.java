package it.polimi.ingsw.Model;

import java.util.*;

public class DevelopmentCardsDeck {
    private DevelopmentCard[][][] cards;
    private List<DevelopmentCard> internalList;

    public DevelopmentCardsDeck() {
        cards = new DevelopmentCard[3][4][4];
        internalList = new ArrayList<DevelopmentCard>();


        cards[0][0][0] = new DevelopmentCard(3, Color.GREEN, 10,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 5); put(Resource.SERVANT, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); put(Resource.SERVANT, 1); }},
                           new HashMap<Resource, Integer>() {{ put(Resource.SHIELD,2); put(Resource.STONE,2); put(Resource.FAITH,1); }})

        );



        shuffle();
    }



    public void shuffle() {
        for (int r = 0; r < 3; r++ )
        {
            for( int c = 0; c < 4; c++)
            {
                internalList.clear();
                internalList = Arrays.asList(cards[r][c]);
                Collections.shuffle(internalList);
                for ( int i = 0 ; i < 4; i++ )
                    cards[r][c][i] = internalList.get(i);
            }
        }
    }

    public DevelopmentCard removeCard(int row, int column) {
        DevelopmentCard card = null;
        for( int i = 3; i >= 0; i-- )
        {
            if ( cards[row][column][i] != null ) {
                card = cards[row][column][i];
                cards[row][column][i] = null;
            }
        }
        return card;
    }

    public DevelopmentCard[][] getVisible() {

        DevelopmentCard[][] temporaryDeck = new DevelopmentCard[3][4];

        for( int r = 0; r < 3; r++ ) {
            for ( int c = 0; c < 4; c++ ) {
                temporaryDeck[r][c] = null;
            }
        }

        for( int r = 0; r < 3; r++ ) {
            for( int c = 0; c < 4; c++ ) {
                for( int i = 0; i < 4; i++ ) {
                    if(cards[r][c][i] != null)
                        temporaryDeck[r][c] = cards[r][c][i];
                }

            }
        }

        return temporaryDeck;
    }
}
