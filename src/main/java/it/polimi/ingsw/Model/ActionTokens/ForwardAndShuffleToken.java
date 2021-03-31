package it.polimi.ingsw.Model.ActionTokens;

import it.polimi.ingsw.Model.ActionTokenStack;
import it.polimi.ingsw.Model.DevelopmentCardsDeck;
import it.polimi.ingsw.Model.FaithTrack;


public class ForwardAndShuffleToken implements ActionToken{
    @Override
    public void doAction(DevelopmentCardsDeck deck, FaithTrack faithTrack, ActionTokenStack stack) {
        stack.shuffle();
    }
}
