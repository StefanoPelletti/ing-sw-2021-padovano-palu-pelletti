package it.polimi.ingsw;

import it.polimi.ingsw.Server.Controller.FaithTrackInfo;
import org.junit.jupiter.api.*;

public class FaithTrackInfoTest {
    FaithTrackInfo f;

    @BeforeEach
    public void init()
    {
        f = new FaithTrackInfo();
    }

    @Test
    public void testCopyInvalidConfig1()
    {
        boolean[] config = { false, false, true};
        assert (!f.setZones(config));
    }
    @Test
    public void testCopyInvalidConfig2()
    {
        boolean[] config = { false, true, false};
        assert (!f.setZones(config));
    }
    @Test
    public void testCopyInvalidConfig3()
    {
        boolean[] config = { false, true, true};
        assert (!f.setZones(config));
    }
    @Test
    public void testCopyInvalidConfig4()
    {
        boolean[] config = { true, false, true};
        assert (!f.setZones(config));
    }
    @Test
    public void testCopyValidConfig1()
    {
        boolean[] config = { false, false, false};
        assert (f.setZones(config));
    }
    @Test
    public void testCopyValidConfig2()
    {
        boolean[] config = { true, false, false};
        assert (f.setZones(config));
    }
    @Test
    public void testCopyValidConfig3()
    {
        boolean[] config = { true, true, false};
        assert (f.setZones(config));
    }
    @Test
    public void testCopyValidConfig4()
    {
        boolean[] config = { true, true, true};
        assert (f.setZones(config));
    }

    @Test
    public void settingAllZones()
    {
        assert ( !f.setZones(-1));
        assert ( !f.setZones(-1));
    }
}
