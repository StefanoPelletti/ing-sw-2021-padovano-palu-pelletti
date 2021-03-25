package it.polimi.ingsw.Model;

public class DevelopmentCardsDeck {
    private DevelopmentCard[][][] cards;

    public DevelopmentCardsDeck() {
        cards = new DevelopmentCard[3][4][4];
    }

    public void shuffle() {
    }

    public DevelopmentCard removeCard(int Row, int Column) {
        DevelopmentCard card = null; //a caso
        return card;
    }

    public DevelopmentCard[][] getVisible() {
        //ci ho provato -Tom <3
        DevelopmentCard[][] temporaryDeck= null;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 4; j++) {
                temporaryDeck[i][j] = cards[i][j][0];
            }
        }
        return temporaryDeck;
    }
}
