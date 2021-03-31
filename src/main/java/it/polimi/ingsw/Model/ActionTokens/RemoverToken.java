package it.polimi.ingsw.Model.ActionTokens;

import it.polimi.ingsw.Model.ActionTokenStack;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.DevelopmentCardsDeck;
import it.polimi.ingsw.Model.FaithTrack;

public class RemoverToken implements ActionToken{
    private Color color;
    private int column;

    public RemoverToken(Color color){
        this.color=color;
        switch(color){
            case GREEN: this.column=0;
            case BLUE: this.column=1;
            case YELLOW: this.column=2;
            case PURPLE: this.column=3;
        }
    }

    @Override
    public void doAction(DevelopmentCardsDeck deck, FaithTrack faithTrack, ActionTokenStack stack){
        deck.removeCard(this.column);
    }

    public Color getColor(){
        return this.color;
    }
}
