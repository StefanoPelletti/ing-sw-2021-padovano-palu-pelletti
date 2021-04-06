package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerators.Color;
import it.polimi.ingsw.Model.Enumerators.Resource;

import java.util.*;

public class DevelopmentCardsDeck {
    private DevelopmentCard[][][] cards;
    private List<DevelopmentCard> internalList;

    public DevelopmentCardsDeck() {
        cards = new DevelopmentCard[3][4][4];
        internalList = new ArrayList<>();

        cards[0][0][0] = new DevelopmentCard(3, Color.GREEN, 12,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 4); put(Resource.COIN, 4); }},
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.COIN, 3); put(Resource.SHIELD, 1);}})
        );

        cards[0][0][1] = new DevelopmentCard(3, Color.GREEN, 11,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 7); }},
                new Power(new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); put(Resource.FAITH, 3);}})
        );

        cards[0][0][2] = new DevelopmentCard(3, Color.GREEN, 10,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 5); put(Resource.SERVANT, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); put(Resource.SERVANT, 1); }},
                           new HashMap<Resource, Integer>() {{ put(Resource.SHIELD,2); put(Resource.STONE,2); put(Resource.FAITH,1); }})
        );

        cards[0][0][3] = new DevelopmentCard(3, Color.GREEN, 9,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 6); }},
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 2); }},
                           new HashMap<Resource, Integer>() {{ put(Resource.STONE, 3); put(Resource.FAITH, 2); }})
        );

        cards[0][1][0] = new DevelopmentCard(3, Color.BLUE, 12,
                new HashMap<Resource,Integer>() {{ put(Resource.COIN, 4); put(Resource.STONE, 4); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 1); }},
                           new HashMap<Resource, Integer>() {{ put(Resource.COIN,1); put(Resource.SHIELD,3); }})
        );

        cards[0][1][1] = new DevelopmentCard(3, Color.BLUE, 11,
                new HashMap<Resource,Integer>() {{ put(Resource.COIN, 7); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SHIELD,1); put(Resource.FAITH,3); }})
        );

        cards[0][1][2] = new DevelopmentCard(3, Color.BLUE, 10,
                new HashMap<Resource,Integer>() {{ put(Resource.COIN, 5); put(Resource.STONE, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); put(Resource.SHIELD, 1); }},
                           new HashMap<Resource, Integer>() {{ put(Resource.SERVANT,2); put(Resource.STONE,2); put(Resource.FAITH,1); }})
        );

        cards[0][1][3] = new DevelopmentCard(3, Color.BLUE, 9,
                new HashMap<Resource,Integer>() {{ put(Resource.COIN, 6); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 2); }},
                           new HashMap<Resource, Integer>() {{ put(Resource.SHIELD,3); put(Resource.FAITH,2); }})
        );

        cards[0][2][0] = new DevelopmentCard(3, Color.YELLOW, 12,
                new HashMap<Resource,Integer>() {{ put(Resource.STONE, 4); put(Resource.SERVANT, 4); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 1); }},
                           new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); put(Resource.SERVANT, 3); }})
        );

        cards[0][2][1] = new DevelopmentCard(3, Color.YELLOW, 11,
                new HashMap<Resource,Integer>() {{ put(Resource.STONE, 7);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 1); }},
                           new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 1); put(Resource.FAITH, 3); }})
        );

        cards[0][2][2] = new DevelopmentCard(3, Color.YELLOW, 10,
                new HashMap<Resource,Integer>() {{ put(Resource.STONE, 5); put(Resource.SERVANT, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); put(Resource.SERVANT, 1); }},
                           new HashMap<Resource, Integer>() {{ put(Resource.COIN, 2); put(Resource.SHIELD, 2); put(Resource.FAITH, 1);}})
        );

        cards[0][2][3] = new DevelopmentCard(3, Color.YELLOW, 9,
                new HashMap<Resource,Integer>() {{ put(Resource.STONE, 6); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 2); }},
                           new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 3); put(Resource.FAITH, 2); }})
        );

        cards[0][3][0] = new DevelopmentCard(3, Color.PURPLE, 12,
                new HashMap<Resource,Integer>() {{ put(Resource.SERVANT, 4); put(Resource.SHIELD, 4); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.STONE, 3); put(Resource.SERVANT, 1); }})
        );

        cards[0][3][1] = new DevelopmentCard(3, Color.PURPLE, 11,
                new HashMap<Resource,Integer>() {{ put(Resource.SERVANT, 7);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); put(Resource.FAITH, 3); }})
        );

        cards[0][3][2] = new DevelopmentCard(3, Color.PURPLE, 10,
                new HashMap<Resource,Integer>() {{ put(Resource.SERVANT, 5); put(Resource.COIN, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); put(Resource.SHIELD, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.COIN, 2); put(Resource.SERVANT, 2); put(Resource.FAITH, 1);}})
        );

        cards[0][3][3] = new DevelopmentCard(3, Color.PURPLE, 9,
                new HashMap<Resource,Integer>() {{ put(Resource.SERVANT, 6); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.STONE, 2); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.COIN, 3); put(Resource.FAITH, 2); }})
        );

        cards[1][0][0] = new DevelopmentCard(2, Color.GREEN, 8,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 3); put(Resource.COIN, 3); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 2); put(Resource.FAITH, 1); }})
        );

        cards[1][0][1] = new DevelopmentCard(2, Color.GREEN, 7,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 5);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 2); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.STONE, 2); put(Resource.FAITH, 2); }})
        );

        cards[1][0][2] = new DevelopmentCard(2, Color.GREEN, 6,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 3); put(Resource.SERVANT, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 1); put(Resource.SERVANT, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.STONE, 3); }})
        );

        cards[1][0][3] = new DevelopmentCard(2, Color.GREEN, 5,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 4); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.FAITH, 2); }})
        );

        cards[1][1][0] = new DevelopmentCard(2, Color.BLUE, 8,
                new HashMap<Resource,Integer>() {{ put(Resource.COIN, 3); put(Resource.STONE, 3); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.STONE, 2); put(Resource.FAITH, 1); }})
        );

        cards[1][1][1] = new DevelopmentCard(2, Color.BLUE, 7,
                new HashMap<Resource,Integer>() {{ put(Resource.COIN, 5);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 2); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 2); put(Resource.FAITH, 2); }})
        );

        cards[1][1][2] = new DevelopmentCard(2, Color.BLUE, 6,
                new HashMap<Resource,Integer>() {{ put(Resource.COIN, 3); put(Resource.STONE, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); put(Resource.STONE, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 3); }})
        );

        cards[1][1][3] = new DevelopmentCard(2, Color.BLUE, 5,
                new HashMap<Resource,Integer>() {{ put(Resource.COIN, 4); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.FAITH, 2); }})
        );

        cards[1][2][0] = new DevelopmentCard(2, Color.YELLOW, 8,
                new HashMap<Resource,Integer>() {{ put(Resource.STONE, 3); put(Resource.SERVANT, 3); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.COIN, 2); put(Resource.FAITH, 1); }})
        );

        cards[1][2][1] = new DevelopmentCard(2, Color.YELLOW, 7,
                new HashMap<Resource,Integer>() {{ put(Resource.STONE, 5);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 2); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 2); put(Resource.FAITH, 2); }})
        );

        cards[1][2][2] = new DevelopmentCard(2, Color.YELLOW, 6,
                new HashMap<Resource,Integer>() {{ put(Resource.STONE, 3); put(Resource.SHIELD, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); put(Resource.SHIELD, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.COIN, 3); }})
        );

        cards[1][2][3] = new DevelopmentCard(2, Color.YELLOW, 5,
                new HashMap<Resource,Integer>() {{ put(Resource.STONE, 4); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.FAITH, 2); }})
        );

        cards[1][3][0] = new DevelopmentCard(2, Color.PURPLE, 8,
                new HashMap<Resource,Integer>() {{ put(Resource.SERVANT, 3); put(Resource.SHIELD, 3); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 2); put(Resource.FAITH, 1); }})
        );

        cards[1][3][1] = new DevelopmentCard(2, Color.PURPLE, 7,
                new HashMap<Resource,Integer>() {{ put(Resource.SERVANT, 5);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.STONE, 2); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.COIN, 2); put(Resource.FAITH, 2); }})
        );

        cards[1][3][2] = new DevelopmentCard(2, Color.PURPLE, 6,
                new HashMap<Resource,Integer>() {{ put(Resource.SERVANT, 3); put(Resource.COIN, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); put(Resource.SERVANT, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 3); }})
        );

        cards[1][3][3] = new DevelopmentCard(2, Color.PURPLE, 5,
                new HashMap<Resource,Integer>() {{ put(Resource.SERVANT, 4); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.FAITH, 2); }})
        );

        cards[2][0][0] = new DevelopmentCard(1, Color.GREEN, 4,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 2); put(Resource.COIN, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); put(Resource.SERVANT, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.COIN, 2); put(Resource.FAITH, 1); }})
        );

        cards[2][0][1] = new DevelopmentCard(1, Color.GREEN, 3,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 3);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 2); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); put(Resource.SHIELD, 1); put(Resource.STONE, 1);}})
        );

        cards[2][0][2] = new DevelopmentCard(1, Color.GREEN, 2,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 1); put(Resource.SERVANT, 1); put(Resource.STONE, 1);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 1); }})
        );

        cards[2][0][3] = new DevelopmentCard(1, Color.GREEN, 1,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.FAITH, 1); }})
        );

        cards[2][1][0] = new DevelopmentCard(1, Color.BLUE, 4,
                new HashMap<Resource,Integer>() {{ put(Resource.COIN, 2); put(Resource.SERVANT, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 1); put(Resource.STONE, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 2); put(Resource.FAITH, 1); }})
        );

        cards[2][1][1] = new DevelopmentCard(1, Color.BLUE, 3,
                new HashMap<Resource,Integer>() {{ put(Resource.COIN, 3);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.STONE, 2); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); put(Resource.SERVANT, 1); put(Resource.SHIELD, 1);}})
        );

        cards[2][1][2] = new DevelopmentCard(1, Color.BLUE, 2,
                new HashMap<Resource,Integer>() {{ put(Resource.COIN, 1); put(Resource.SERVANT, 1); put(Resource.STONE, 1);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); }})
        );

        cards[2][1][3] = new DevelopmentCard(1, Color.BLUE, 1,
                new HashMap<Resource,Integer>() {{ put(Resource.COIN, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.FAITH, 1); }})
        );

        cards[2][2][0] = new DevelopmentCard(1, Color.YELLOW, 4,
                new HashMap<Resource,Integer>() {{ put(Resource.STONE, 2); put(Resource.SHIELD, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); put(Resource.SERVANT, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 2); put(Resource.FAITH, 1); }})
        );

        cards[2][2][1] = new DevelopmentCard(1, Color.YELLOW, 3,
                new HashMap<Resource,Integer>() {{ put(Resource.STONE, 3);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 2); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); put(Resource.SERVANT, 1); put(Resource.STONE, 1);}})
        );

        cards[2][2][2] = new DevelopmentCard(1, Color.YELLOW, 2,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 1); put(Resource.STONE, 1); put(Resource.COIN, 1);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); }})
        );

        cards[2][2][3] = new DevelopmentCard(1, Color.YELLOW, 1,
                new HashMap<Resource,Integer>() {{ put(Resource.STONE, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.FAITH, 1); }})
        );

        cards[2][3][0] = new DevelopmentCard(1, Color.PURPLE, 4,
                new HashMap<Resource,Integer>() {{ put(Resource.SERVANT, 2); put(Resource.STONE, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); put(Resource.SHIELD, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.STONE, 2); put(Resource.FAITH, 1); }})
        );

        cards[2][3][1] = new DevelopmentCard(1, Color.PURPLE, 3,
                new HashMap<Resource,Integer>() {{ put(Resource.SERVANT, 3);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 2); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 1); put(Resource.SHIELD, 1); put(Resource.STONE, 1);}})
        );

        cards[2][3][2] = new DevelopmentCard(1, Color.PURPLE, 2,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 1); put(Resource.SERVANT, 1); put(Resource.COIN, 1);}} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.COIN, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 1); }})
        );

        cards[2][3][3] = new DevelopmentCard(1, Color.PURPLE, 1,
                new HashMap<Resource,Integer>() {{ put(Resource.SERVANT, 2); }} ,
                new Power( new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.FAITH, 1); }})
        );

        shuffle();
    }


//DO NOT CALL AFTER A REMOVE!
    public void shuffle() {
        for (int r = 0; r < 3; r++ )
        {
            for( int c = 0; c < 4; c++)
            {
                internalList.clear();
                internalList = new ArrayList<>(Arrays.asList(cards[r][c]));

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
                break;
            }
        }
        return card;
    }

    public boolean removeCard(int column) {
        if(column <0 || column>3) return false;

        int removed=0;
        for(int row=2; row>=0; row--){
            for(int i=3; i>=0; i--){
                if(cards[row][column][i]!=null){
                    cards[row][column][i]=null;
                    removed++;
                }
                if(removed==2) break;
            }
            if(removed==2) break;
        }
        return true;
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

    public DevelopmentCard[] getStack( int row, int column )
    {
        if( row<0 || row>2 || column < 0 || column > 3 ) return null;
        DevelopmentCard[] result = new DevelopmentCard[4];
        System.arraycopy(cards[row][column], 0, result, 0, 4);
        return result;
    }
}
