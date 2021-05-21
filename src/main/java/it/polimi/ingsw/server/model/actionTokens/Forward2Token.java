package it.polimi.ingsw.server.model.actionTokens;

public class Forward2Token implements ActionToken {

    public boolean isRemover() {
        return false;
    }

    public boolean isForward2() {
        return true;
    }

    public boolean isForwardAndShuffle() {
        return false;
    }
}