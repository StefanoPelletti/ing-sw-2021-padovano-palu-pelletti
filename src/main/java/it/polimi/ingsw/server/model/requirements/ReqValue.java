package it.polimi.ingsw.server.model.requirements;

import java.io.Serializable;
import java.util.Objects;

public class ReqValue implements Serializable {
    private final int reqNumCard;
    private final int reqLvlCard;

    public ReqValue(int reqNumCard, int reqLvlCard) {
        this.reqNumCard = reqNumCard;
        this.reqLvlCard = reqLvlCard;
    }

    public int getReqNumCard() {
        return this.reqNumCard;
    }

    public int getReqLvlCard() {
        return this.reqLvlCard;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ReqValue)) return false;
        ReqValue o = (ReqValue) obj;
        return (this.reqNumCard == o.reqNumCard && this.reqLvlCard == o.reqLvlCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.reqNumCard, this.reqLvlCard);
    }
}
