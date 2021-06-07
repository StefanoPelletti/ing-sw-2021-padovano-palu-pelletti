package it.polimi.ingsw.server.model.requirements;

import java.io.Serializable;

public interface Requirement extends Serializable {
    /**
     * Returns True if this Requirement implies owning some Development Cards.
     *
     * @return True if this Requirement implies owning some Development Cards, False otherwise.
     */
    boolean isCardRequirement();

    /**
     * Returns True if this Requirement implies owning some Resources.
     *
     * @return True if this Requirement implies owning some Resources, False otherwise.
     */
    boolean isResourceRequirement();
}