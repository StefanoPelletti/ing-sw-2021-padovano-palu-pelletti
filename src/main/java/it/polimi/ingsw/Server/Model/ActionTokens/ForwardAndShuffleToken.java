package it.polimi.ingsw.Server.Model.ActionTokens;

import it.polimi.ingsw.Server.Model.ActionTokenStack;
import it.polimi.ingsw.Server.Model.DevelopmentCardsDeck;
import it.polimi.ingsw.Server.Controller.FaithTrackManager;


public class ForwardAndShuffleToken implements ActionToken{
    @Override
    public void doAction(DevelopmentCardsDeck deck, FaithTrackManager faithTrackManager, ActionTokenStack stack) {
        stack.shuffle();
    }
}
