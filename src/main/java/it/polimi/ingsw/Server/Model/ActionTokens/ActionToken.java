package it.polimi.ingsw.Server.Model.ActionTokens;

import it.polimi.ingsw.Server.Model.ActionTokenStack;
import it.polimi.ingsw.Server.Model.DevelopmentCardsDeck;
import it.polimi.ingsw.Server.Model.FaithTrack;

public interface ActionToken {
    public void doAction(DevelopmentCardsDeck deck, FaithTrack faithTrack, ActionTokenStack stack);
}
