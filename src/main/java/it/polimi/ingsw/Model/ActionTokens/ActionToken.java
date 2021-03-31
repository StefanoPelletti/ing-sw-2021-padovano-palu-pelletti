package it.polimi.ingsw.Model.ActionTokens;

import it.polimi.ingsw.Model.ActionTokenStack;
import it.polimi.ingsw.Model.DevelopmentCardsDeck;
import it.polimi.ingsw.Model.FaithTrack;
import java.util.function.Consumer;

public interface ActionToken {
    public void doAction(DevelopmentCardsDeck deck, FaithTrack faithTrack, ActionTokenStack stack);
}
