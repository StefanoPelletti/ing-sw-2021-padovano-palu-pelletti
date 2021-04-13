package it.polimi.ingsw.Server.Model.ActionTokens;

import it.polimi.ingsw.Server.Model.ActionTokenStack;
import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.DevelopmentCardsDeck;
import it.polimi.ingsw.Server.Controller.FaithTrackManager;

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
    public void doAction(DevelopmentCardsDeck deck, FaithTrackManager faithTrackManager, ActionTokenStack stack){
        deck.removeCard(this.column);
    }

    public Color getColor(){
        return this.color;
    }
}
