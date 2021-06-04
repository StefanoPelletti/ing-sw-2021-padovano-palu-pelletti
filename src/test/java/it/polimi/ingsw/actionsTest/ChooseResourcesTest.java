package it.polimi.ingsw.actionsTest;

import it.polimi.ingsw.Catcher;
import it.polimi.ingsw.networking.message.actionMessages.MSG_INIT_CHOOSE_RESOURCE;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.WarehouseDepot;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.enumerators.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChooseResourcesTest {

    GameManager gm;
    Game g;
    ActionManager am;
    Player p;
    Catcher c;

    @BeforeEach
    public void reset() {
        gm = new GameManager(4);
        am = gm.getActionManager();
        g = gm.getGame();

        c = new Catcher();
        g.addPlayer("Primo", 1);
        g.addPlayer("Secondo", 2);
        g.addPlayer("Terzo", 3);
        g.addPlayer("Quarto", 4);
        gm.addAllObserver(c);
        c.emptyQueue();
        p = g.getPlayer(1);
    }

    @Test
    public void errorState() {
        MSG_INIT_CHOOSE_RESOURCE message = new MSG_INIT_CHOOSE_RESOURCE(Resource.STONE);

        WarehouseDepot depot = p.getWarehouseDepot();
        assertEquals(0, depot.getTotal());

        assertFalse(am.chooseResource(p, message));
        assertEquals(0, depot.getTotal());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

    @Test
    public void standardTurn() {
        MSG_INIT_CHOOSE_RESOURCE message = new MSG_INIT_CHOOSE_RESOURCE(Resource.STONE);
        g.setCurrentPlayer(2);
        p = g.getCurrentPlayer();
        g.setResourcePickerEnabled(true);
        g.setResourcePickerNumOfResources(p.getStartingResources());
        g.changeStatus(Status.STANDARD_TURN);
        c.emptyQueue();

        assertTrue(am.chooseResource(p, message));
        WarehouseDepot depot = p.getWarehouseDepot();
        assertEquals(1, depot.getTotal());
        assertEquals(1, (int) depot.getResources().get(Resource.STONE));
        assertSame(depot.getShelf1(), Resource.STONE);
        assertEquals(0, p.getStartingResources());
        assertFalse(g.isResourcePickerEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_ResourcePicker).count());
        assertEquals(3, c.messages.size());
    }

    @Test
    public void standardTurnLastPlayer() {
        MSG_INIT_CHOOSE_RESOURCE message = new MSG_INIT_CHOOSE_RESOURCE(Resource.STONE);
        g.setCurrentPlayer(4);
        p = g.getCurrentPlayer();
        g.setResourcePickerEnabled(true);
        g.setResourcePickerNumOfResources(p.getStartingResources());
        g.changeStatus(Status.STANDARD_TURN);
        c.emptyQueue();

        assertTrue(am.chooseResource(p, message));
        WarehouseDepot depot = p.getWarehouseDepot();
        assertEquals(1, depot.getTotal());
        assertEquals(1, (int) depot.getResources().get(Resource.STONE));
        assertSame(depot.getShelf2()[0], Resource.STONE);

        assertTrue(g.isResourcePickerEnabled());
        assertEquals(1, g.getResourcePicker().getNumOfResources());
        assertEquals(1, p.getStartingResources());

        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_ResourcePicker).count());
        assertEquals(4, c.messages.size());

        c.emptyQueue();

        assertTrue(am.chooseResource(p, message));
        assertEquals(2, depot.getTotal());
        assertEquals(2, (int) depot.getResources().get(Resource.STONE));
        assertSame(depot.getShelf2()[0], Resource.STONE);
        assertSame(depot.getShelf2()[1], Resource.STONE);
        assertEquals(0, p.getStartingResources());
        assertFalse(g.isResourcePickerEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_ResourcePicker).count());
        assertEquals(3, c.messages.size());
    }

    @Test
    public void goingIntoStandardTurnFromINIT_2() {
        MSG_INIT_CHOOSE_RESOURCE message = new MSG_INIT_CHOOSE_RESOURCE(Resource.STONE);
        g.setCurrentPlayer(4);
        p = g.getCurrentPlayer();
        g.setResourcePickerEnabled(true);
        g.setResourcePickerNumOfResources(p.getStartingResources());
        g.changeStatus(Status.INIT_2);
        c.emptyQueue();

        assertTrue(am.chooseResource(p, message));
        WarehouseDepot depot = p.getWarehouseDepot();
        assertTrue(g.isResourcePickerEnabled());
        assertSame(Status.INIT_2, g.getStatus());
        assertEquals(4, g.getCurrentPlayerInt());
        c.emptyQueue();

        assertTrue(am.chooseResource(p, message));
        assertEquals(2, depot.getTotal());
        assertEquals(2, (int) depot.getResources().get(Resource.STONE));
        assertEquals(0, p.getStartingResources());
        assertFalse(g.isResourcePickerEnabled());
        assertEquals(1, g.getCurrentPlayerInt());
        assertSame(g.getStatus(), Status.STANDARD_TURN);
        assertEquals(false, p.isDisconnectedBeforeResource());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Game).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_ResourcePicker).count());
        assertEquals(5, c.messages.size());
    }

}
