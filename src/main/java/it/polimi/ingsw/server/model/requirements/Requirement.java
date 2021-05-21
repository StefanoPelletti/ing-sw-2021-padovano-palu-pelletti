package it.polimi.ingsw.server.model.requirements;

import java.io.Serializable;

public interface Requirement extends Serializable {
    boolean isCardRequirement();

    boolean isResourceRequirement();
}