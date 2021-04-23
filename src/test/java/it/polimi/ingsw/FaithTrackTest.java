package it.polimi.ingsw;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.Server.Model.*;

public class FaithTrackTest {

    FaithTrack faithTrack;

    @BeforeEach
    public void reset()
    {
        faithTrack = new FaithTrack(new Game());
    }

    @Test
    public void zonesInit()
    {
        assertFalse (faithTrack.getZones()[0]);
        assertFalse (faithTrack.getZones()[1]);
        assertFalse (faithTrack.getZones()[2]);
    }

    @Test
    public void zonesSet()
    {
        faithTrack.setZones(0, true);
        faithTrack.setZones(1, true);
        faithTrack.setZones(2, true);

        assertTrue (faithTrack.getZones()[0]);
        assertTrue (faithTrack.getZones()[1]);
        assertTrue (faithTrack.getZones()[2]);
    }

    @Test
    public void testReset()
    {
        faithTrack.setZones(0, true);
        faithTrack.setZones(1,true);
        faithTrack.setZones(2, true);
        assertTrue (faithTrack.getZones()[0]);
        assertTrue (faithTrack.getZones()[1]);
        assertTrue (faithTrack.getZones()[2]);

        faithTrack.resetZones();
        assertFalse (faithTrack.getZones()[0]);
        assertFalse (faithTrack.getZones()[1]);
        assertFalse (faithTrack.getZones()[2]);

    }



    @Test
    public void pointsBounds()
    {
        Player p = new Player("tommaso", 1);
        p.setPosition(8);
        assertSame ( faithTrack.calculateVP(p), 2);
        p.setPosition(16);
        assertSame ( faithTrack.calculateVP(p), 3);
        p.setPosition(24);
        assertSame ( faithTrack.calculateVP(p), 4);

    }

    @Test
    public void get()
    {
        boolean[] newZones = faithTrack.getZones();
        assertNotSame ( newZones , faithTrack.getZones());

        newZones[0] = true;
        newZones[1] = true;
        newZones[2] = true;

        assertTrue (newZones[0] && !faithTrack.getZones()[0]);
        assertTrue (newZones[1] && !faithTrack.getZones()[1]);
        assertTrue (newZones[2] && !faithTrack.getZones()[2]);
    }

    @Test
    public void entireTrip()
    {
        Player p = new Player("test", 1);

        //1
        assertSame( faithTrack.calculateVP(p), 0);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //2
        assertSame( faithTrack.calculateVP(p), 0);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //3
        assertSame( faithTrack.calculateVP(p), 0);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //4
        assertSame( faithTrack.calculateVP(p), 0);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //5
        assertSame( faithTrack.calculateVP(p), 2);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //6
        assertSame( faithTrack.calculateVP(p), 2);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //7
        assertSame( faithTrack.calculateVP(p), 2);
        assertSame( faithTrack.doesActivateZone(p), 1);

        assertTrue( faithTrack.advance(p) ); //8
        assertSame( faithTrack.calculateVP(p), 2);
        assertSame( faithTrack.doesActivateZone(p), 0);
        faithTrack.setZones(0,true);
        assertTrue( faithTrack.getZones()[0]);

        assertTrue( faithTrack.advance(p) ); //9
        assertSame( faithTrack.calculateVP(p), 0);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //10
        assertSame( faithTrack.calculateVP(p), 0);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //11
        assertSame( faithTrack.calculateVP(p), 0);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //12
        assertSame( faithTrack.calculateVP(p), 3);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //13
        assertSame( faithTrack.calculateVP(p), 3);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //14
        assertSame( faithTrack.calculateVP(p), 3);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //15
        assertSame( faithTrack.calculateVP(p), 3);
        assertSame( faithTrack.doesActivateZone(p), 2);

        assertTrue( faithTrack.advance(p) ); //16
        assertSame( faithTrack.calculateVP(p), 3);
        assertSame( faithTrack.doesActivateZone(p), 0);
        faithTrack.setZones(1,true);
        assertTrue( faithTrack.getZones()[1]);

        assertTrue( faithTrack.advance(p) ); //17
        assertSame( faithTrack.calculateVP(p), 0);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //18
        assertSame( faithTrack.calculateVP(p), 0);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //19
        assertSame( faithTrack.calculateVP(p), 4);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //20
        assertSame( faithTrack.calculateVP(p), 4);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //21
        assertSame( faithTrack.calculateVP(p), 4);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //22
        assertSame( faithTrack.calculateVP(p), 4);
        assertSame( faithTrack.doesActivateZone(p), 0);

        assertTrue( faithTrack.advance(p) ); //23
        assertSame( faithTrack.calculateVP(p), 4);
        assertSame( faithTrack.doesActivateZone(p), 3);

        assertTrue( faithTrack.advance(p) ); //24
        assertSame( faithTrack.calculateVP(p), 4);
        assertSame( faithTrack.doesActivateZone(p), -1);
        faithTrack.setZones(2,true);
        assertTrue( faithTrack.getZones()[2]);

        assertFalse( faithTrack.advance(p) ); //24
    }
    @Test
    public void blackCrossAdvance()
    {
        Game game = new Game();
        faithTrack = new FaithTrack(game);
        int i = game.getBlackCrossPosition();

        assertTrue ( faithTrack.advanceLorenzo() );
        assertSame ( game.getBlackCrossPosition() , i+1);

        game.setBlackCrossPosition(23);
        assertTrue ( faithTrack.advanceLorenzo() );
        assertFalse ( faithTrack.advanceLorenzo() );
        assertSame ( game.getBlackCrossPosition() , 24);

    }
}


