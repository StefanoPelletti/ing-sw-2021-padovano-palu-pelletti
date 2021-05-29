package it.polimi.ingsw.server.model.actionTokens;

/**
 * ActionToken implements a Strategy Pattern.
 * The logic that implements the action is written in the action Manager.
 * These Tokens are similar to 'markers'.
 */
public interface ActionToken {

    /**
     * @return True if the Token is a Remover Token, False otherwise.
     */
    boolean isRemover();

    /**
     * @return True if the Token is a Forward2 Token, False otherwise.
     */
    boolean isForward2();

    /**
     * @return True if the Token is a ForwardAndShuffle Token, False otherwise.
     */
    boolean isForwardAndShuffle();
}