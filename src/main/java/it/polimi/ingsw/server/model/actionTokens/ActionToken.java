package it.polimi.ingsw.server.model.actionTokens;

/**
 * ActionToken implements a Strategy Pattern.
 * The logic that implements the action is written in the action Manager.
 * These Tokens are similar to 'markers'.
 */
public interface ActionToken {

    /**
     * The Remover Token deletes 2 Development Cards from the Development Deck.
     * The Cards are deleted in the same column, defined by a Color contained in the Token.
     * The Cards are being deleted from the bottom, from level 1 to 3.
     *
     * @return True if the Token is a Remover Token, False otherwise.
     */
    boolean isRemover();

    /**
     * The Forward2 Token moves Lorenzo 2 times on the Faith Track.
     *
     * @return True if the Token is a Forward2 Token, False otherwise.
     */
    boolean isForward2();

    /**
     * The ForwardAndShuffle Token moves Lorenzo one time on the FaithTrack and shuffles the Action Token Stack.
     *
     * @return True if the Token is a ForwardAndShuffle Token, False otherwise.
     */
    boolean isForwardAndShuffle();
}