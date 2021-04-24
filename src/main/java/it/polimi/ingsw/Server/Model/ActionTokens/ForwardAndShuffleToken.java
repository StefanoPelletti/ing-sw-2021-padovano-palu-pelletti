package it.polimi.ingsw.Server.Model.ActionTokens;

import it.polimi.ingsw.Server.Model.ActionTokenStack;
import it.polimi.ingsw.Server.Model.DevelopmentCardsDeck;
import it.polimi.ingsw.Server.Controller.FaithTrackManager;


public class ForwardAndShuffleToken implements ActionToken{
    public boolean isRemover(){return false;}
    public boolean isForward2() { return false;};
    public boolean isForwardAndShuffle(){return true;};
}
