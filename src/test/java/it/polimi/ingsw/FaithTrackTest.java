package it.polimi.ingsw;

import org.junit.jupiter.api.*;
import it.polimi.ingsw.Server.Model.*;

public class FaithTrackTest {

    @Test
    public void zonesInit()
    {
        FaithTrack faithTrack = new FaithTrack(new Game());

        assert (!faithTrack.getZones()[0]);
        assert (!faithTrack.getZones()[1]);
        assert (!faithTrack.getZones()[2]);
    }

    @Test
    public void zonesSet()
    {
        FaithTrack faithTrack = new FaithTrack(new Game());

        faithTrack.setZones(0, true);
        faithTrack.setZones(1, true);
        faithTrack.setZones(2, true);

        assert (faithTrack.getZones()[0]);
        assert (faithTrack.getZones()[1]);
        assert (faithTrack.getZones()[2]);

    }

    @Test
    public void entireTrip()
    {
        FaithTrack faithTrack = new FaithTrack(new Game());
        Player p = new Player("test", 1);

        //1
        assert( faithTrack.calculateVP(p) == 0);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //2
        assert( faithTrack.calculateVP(p) == 0);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //3
        assert( faithTrack.calculateVP(p) == 0);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //4
        assert( faithTrack.calculateVP(p) == 0);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //5
        assert( faithTrack.calculateVP(p) == 2);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //6
        assert( faithTrack.calculateVP(p) == 2);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //7
        assert( faithTrack.calculateVP(p) == 2);
        assert( faithTrack.doesActivateZone(p) == 1);

        assert( faithTrack.advance(p) ); //8
        assert( faithTrack.calculateVP(p) == 2);
        assert( faithTrack.doesActivateZone(p) == 0);
        faithTrack.setZones(0,true);
        assert( faithTrack.getZones()[0]);

        assert( faithTrack.advance(p) ); //9
        assert( faithTrack.calculateVP(p) == 0);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //10
        assert( faithTrack.calculateVP(p) == 0);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //11
        assert( faithTrack.calculateVP(p) == 0);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //12
        assert( faithTrack.calculateVP(p) == 3);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //13
        assert( faithTrack.calculateVP(p) == 3);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //14
        assert( faithTrack.calculateVP(p) == 3);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //15
        assert( faithTrack.calculateVP(p) == 3);
        assert( faithTrack.doesActivateZone(p) == 2);

        assert( faithTrack.advance(p) ); //16
        assert( faithTrack.calculateVP(p) == 3);
        assert( faithTrack.doesActivateZone(p) == 0);
        faithTrack.setZones(1,true);
        assert( faithTrack.getZones()[1]);

        assert( faithTrack.advance(p) ); //17
        assert( faithTrack.calculateVP(p) == 0);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //18
        assert( faithTrack.calculateVP(p) == 0);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //19
        assert( faithTrack.calculateVP(p) == 4);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //20
        assert( faithTrack.calculateVP(p) == 4);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //21
        assert( faithTrack.calculateVP(p) == 4);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //22
        assert( faithTrack.calculateVP(p) == 4);
        assert( faithTrack.doesActivateZone(p) == 0);

        assert( faithTrack.advance(p) ); //23
        assert( faithTrack.calculateVP(p) == 4);
        assert( faithTrack.doesActivateZone(p) == 3);

        assert( faithTrack.advance(p) ); //24
        assert( faithTrack.calculateVP(p) == 4);
        assert( faithTrack.doesActivateZone(p) == -1);
        faithTrack.setZones(2,true);
        assert( faithTrack.getZones()[2]);

        assert( !faithTrack.advance(p) ); //24
    }
}
