package it.polimi.ingsw.Server.Model.ActionTokens;

import it.polimi.ingsw.Server.Model.ActionTokenStack;
import it.polimi.ingsw.Server.Model.DevelopmentCardsDeck;
import it.polimi.ingsw.Server.Controller.FaithTrackManager;

public class Forward2Token implements ActionToken{

    public boolean isRemover(){return false;};
    public boolean isForward2() { return true;};
    public boolean isForwardAndShuffle(){return false;};

}
