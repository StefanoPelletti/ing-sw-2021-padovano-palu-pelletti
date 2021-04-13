package it.polimi.ingsw.Server.Model.ActionTokens;

import it.polimi.ingsw.Server.Model.ActionTokenStack;
import it.polimi.ingsw.Server.Model.DevelopmentCardsDeck;
import it.polimi.ingsw.Server.Controller.FaithTrackManager;

public interface ActionToken {
    public void doAction(DevelopmentCardsDeck deck, FaithTrackManager faithTrackManger, ActionTokenStack stack);
}
