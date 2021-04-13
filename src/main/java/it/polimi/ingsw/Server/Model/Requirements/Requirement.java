package it.polimi.ingsw.Server.Model.Requirements;

import java.io.Serializable;

public interface Requirement extends Serializable {
    public boolean isCardRequirement();
    public boolean isResourceRequirement();
}
