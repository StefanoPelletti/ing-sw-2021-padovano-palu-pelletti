package it.polimi.ingsw.server.model.actionTokens;

public class ForwardAndShuffleToken implements ActionToken {

    public boolean isRemover() {
        return false;
    }

    public boolean isForward2() {
        return false;
    }

    public boolean isForwardAndShuffle() {
        return true;
    }
}