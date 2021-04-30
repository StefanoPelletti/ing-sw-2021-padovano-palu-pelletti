package it.polimi.ingsw.Server.Model.ActionTokens;

public class ForwardAndShuffleToken implements ActionToken{
    public boolean isRemover(){return false;}
    public boolean isForward2() { return false;};
    public boolean isForwardAndShuffle(){return true;};
}
