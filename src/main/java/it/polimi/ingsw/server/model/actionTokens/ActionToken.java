package it.polimi.ingsw.server.model.actionTokens;

public interface ActionToken {

    boolean isRemover();

    boolean isForward2();

    boolean isForwardAndShuffle();
}