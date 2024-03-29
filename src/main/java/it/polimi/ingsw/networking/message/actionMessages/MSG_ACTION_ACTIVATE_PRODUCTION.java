package it.polimi.ingsw.networking.message.actionMessages;

import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.middles.MessageHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MSG_ACTION_ACTIVATE_PRODUCTION extends ActionMessage implements Serializable {

    private final boolean[] standardProduction; //refers to the DevelopmentSlot
    private final boolean basicProduction; //refers to the basic Development skill
    private final boolean[] leaderProduction; //refers to the choices made for the leaderCards with a Production ability
    private final List<Resource> basicInput;
    private final Resource basicOutput;
    private final Resource leaderOutput1;
    private final Resource leaderOutput2;

    /**
     * MSG_ACTION_ACTIVATE_PRODUCTION is sent by the Client to the ClientHandler.
     * It requests the ClientHandler to perform the Controller activateProduction() method.
     * Contains all the resources chosen by the player that must be processed by the Controller.
     *
     * @param standardProduction A 3-cell boolean array representing which Development Card the player has chose to activate.
     * @param basicProduction    A boolean that is true if the player has chosen to use the basic production.
     * @param leaderProduction   A 2-cell boolean array representing which Leader Card the player has chosen to use.
     * @param basicInput         A List of 2 Resources representing the input of the basic production. Should be null if basicProduction is false.
     * @param basicOutput        A Resource representing the output of the basic production. Not used if basicProduction is false.
     * @param leaderOutput1      A Resource representing the output of the first Leader Card. Not used if leaderProduction[0] is false.
     * @param leaderOutput2      A Resource representing the output of the second Leader Card. Not used if leaderProduction[1] is false.
     * @throws IllegalArgumentException If the message is built with: <ul>
     *                                  <li> standardProduction array is null or leaderProduction is null
     *                                  <li> if basicProduction is true and ( basicInput is null or it does not contain 2 resources )
     *                                  <li> if leaderProduction[0] is true and leaderOutput1 is null
     *                                  <li> if leaderProduction[1] is true and leaderOutput2 is null.
     */
    public MSG_ACTION_ACTIVATE_PRODUCTION(boolean[] standardProduction, boolean basicProduction, boolean[] leaderProduction,
                                          List<Resource> basicInput,
                                          Resource basicOutput,
                                          Resource leaderOutput1,
                                          Resource leaderOutput2) {
        super(MessageType.MSG_ACTION_ACTIVATE_PRODUCTION);

        if (standardProduction == null || leaderProduction == null)
            throw new IllegalArgumentException();
        if (basicProduction && (basicInput == null || basicInput.size() != 2))
            throw new IllegalArgumentException();
        if (leaderProduction[0] && leaderOutput1 == null)
            throw new IllegalArgumentException();
        if (leaderProduction[1] && leaderOutput2 == null)
            throw new IllegalArgumentException();

        this.standardProduction = standardProduction.clone();
        this.basicProduction = basicProduction;
        this.leaderProduction = leaderProduction.clone();

        this.basicInput = basicInput != null ? new ArrayList<>(basicInput) : null;
        this.basicOutput = basicOutput;
        this.leaderOutput1 = leaderOutput1;
        this.leaderOutput2 = leaderOutput2;
    }

    public boolean[] getStandardProduction() {
        return standardProduction;
    }

    public boolean isBasicProduction() {
        return basicProduction;
    }

    public boolean[] getLeaderProduction() {
        return leaderProduction;
    }

    public List<Resource> getBasicInput() {
        return this.basicInput;
    }

    public Resource getBasicOutput() {
        return this.basicOutput;
    }

    public Resource getLeaderOutput1() {
        return this.leaderOutput1;
    }

    public Resource getLeaderOutput2() {
        return this.leaderOutput2;
    }

    @Override
    public boolean execute(ActionManager actionManager) {
        return actionManager.activateProduction(actionManager.getGame().getCurrentPlayer(), this);
    }

    @Override
    public String notifyAction(String nickname, MessageHelper messageHelper) {
        return messageHelper.action_activateProduction(nickname, this);
    }
}