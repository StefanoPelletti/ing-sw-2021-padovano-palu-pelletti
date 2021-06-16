package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.networking.message.MSG_ERROR;
import it.polimi.ingsw.networking.message.MSG_NOTIFICATION;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.updateMessages.*;
import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.*;
import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_DevSlot;
import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_Extradepot;
import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_Strongbox;
import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_WarehouseDepot;
import it.polimi.ingsw.server.utils.ModelObserver;

import java.io.IOException;

/**
 * The UpdateHandlerGUI updates a GameSimplified instance which is supposed to be already present in the Ark.
 * The UpdateHandlerGUI is also able to show real time messages, from both the Server and the Simplified Model which is updated by Update messages.
 * Instead of updating the sole Model, also updates the Board GUI.
 */
public class UpdateHandlerGUI implements Runnable, ModelObserver {

    /**
     * The reference to the Board that this UpdateHandlerGUI updates.
     */
    private final Board board;

    /**
     * Used to tell when it's "still" this Player turn in a multi-move turn
     */
    private boolean still;

    public UpdateHandlerGUI(Board boardRef) {
        this.board = boardRef;
    }

    /**
     * Main execution body of the UpdateHandlerGUI, is simply composed of two parts: <ul>
     * <li> on initialization, it could write some "welcome" messages for the player</li>
     * <li> later on, cycles indefinitely on the socket to capture UpdateMessages and executeGUI() them. This will terminate when a MSG_Stop is caught.</li>
     */
    @Override
    public void run() {
        Ark.yourTurn = Ark.game.isMyTurn(Ark.myPlayerNumber);

        if (Ark.yourTurn) { // && Ark.game.isLeaderCardsPickerEnabled()
            board.leftPanel.updateNotification("You are the First Player!");
        } else {
            if (!Ark.reconnected) {
                board.leftPanel.updateNotification("You are player number " + Ark.myPlayerRef.getPlayerNumber() + "!");
                String message = "Because you are player number " + Ark.myPlayerRef.getPlayerNumber() + ", you will also receive ";
                if (Ark.myPlayerRef.getPlayerNumber() == 2 || Ark.myPlayerRef.getPlayerNumber() == 3)
                    message += 1 + " resource";
                else
                    message += 2 + " resources";
                board.leftPanel.updateNotification(message);

                if (Ark.myPlayerRef.getPlayerNumber() == 3 || Ark.myPlayerRef.getPlayerNumber() == 4)
                    board.leftPanel.updateNotification("And lastly, you are starting in position 1");
            }
        }

        UpdateMessage message;
        while (true) {
            try {
                message = (UpdateMessage) Ark.objectInputStream.readObject();
                message.executeGUI(this);
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                board.leftPanel.updateNotification("Connection to server was lost. Please quit.");
                return;
            } catch (IllegalArgumentException e) {
                return;
            }
        }
    }


    /**
     * This method allows the bypassing of the Network Layer. Update messages from the model are directly caught and executedGUI() by the UpdateHandlerGUI.
     *
     * @param message the UpdateMessage generated from the Model
     */
    @Override
    public void update(Message message) {
        UpdateMessage msg = (UpdateMessage) message;
        msg.executeGUI(this);
    }


    /**
     * Passes the MSG_UPD_Player message to the Game instance, which performs the update.
     * Also updates the GUI to reflect the changes.
     *
     * @param message the MSG_UPD_Player update message
     */
    public void updatePlayer(MSG_UPD_Player message) {
        String name = message.getNickname();

        Ark.game.updatePlayer(message);
        board.topPanel.update();


        if (name.equals(Ark.nickname)) {
            board.leftPanel.update();
            board.activateLeaderCard_panel.update();
            board.discardLeaderCard_panel.update();
            board.production_panel.update();
        } else
            board.centralRightPanel.update(name);
    }

    /**
     * Passes the MSG_UPD_WarehouseDepot message to the Game instance, which performs the update on the Current Player.
     * Also updates the GUI to reflect the changes.
     *
     * @param message the MSG_UPD_WarehouseDepot update message
     */
    public void updateCurrentPlayerDepot(MSG_UPD_WarehouseDepot message) {
        Ark.game.updateCurrentPlayerDepot(message);

        String name = Ark.game.getCurrentPlayerName();

        board.centralLeftPanel.update(name);

        if (name.equals(Ark.nickname))
            board.changeDepotConfig_panel.update();
        else
            board.bottomPanel.update();
    }

    /**
     * Passes the MSG_UPD_DevSlot message to the Game instance, which performs the update on the Current Player.
     * Also updates the GUI to reflect the changes.
     *
     * @param message the MSG_UPD_DevSlot update message
     */
    public void updateCurrentPlayerDevSlot(MSG_UPD_DevSlot message) {
        Ark.game.updateCurrentPlayerDevSlot(message);

        String name = Ark.game.getCurrentPlayerName();
        board.centralRightPanel.update(name);
        if (name.equals(Ark.nickname)) {
            board.productionSelection_panel.update();
        }
    }

    /**
     * Passes the MSG_UPD_Extradepot message to the Game instance, which performs the update on the Current Player.
     * Also updates the GUI to reflect the changes.
     *
     * @param message the MSG_UPD_Extradepot update message
     */
    public void updateCurrentPlayerExtraDepot(MSG_UPD_Extradepot message) {
        Ark.game.updateCurrentPlayerExtraDepot(message);

        if (Ark.yourTurn) {
            board.leftPanel.update();
            board.changeDepotConfig_panel.update();
        }
    }

    /**
     * Passes the MSG_UPD_Strongbox message to the Game instance, which performs the update on the Current Player.
     * Also updates the GUI to reflect the changes.
     *
     * @param message the MSG_UPD_Strongbox update message
     */
    public void updateCurrentPlayerStrongbox(MSG_UPD_Strongbox message) {
        Ark.game.updateCurrentPlayerStrongbox(message);

        String name = Ark.game.getCurrentPlayerName();

        board.centralLeftPanel.update(name);

        if (name.equals(Ark.nickname))
            board.changeDepotConfig_panel.update();
        else
            board.bottomPanel.update();
    }

    /**
     * Passes the MSG_UPD_DevCardsVendor message to the Game instance, which performs the update.
     * Also updates the GUI to reflect the changes.
     *
     * @param message the MSG_UPD_DevCardsVendor update message
     */
    public void updateDevCardVendor(MSG_UPD_DevCardsVendor message) {
        Ark.game.updateDevelopmentCardsVendor(message);
        board.vendor_panel.update();
        if (Ark.yourTurn) {
            if (Ark.game.isDevelopmentCardsVendorEnabled()) {
                if (!board.lastRightCard.equals(Board.VENDOR)) {
                    board.lastRightCard = Board.VENDOR;
                    board.changeRightCard(Board.VENDOR);
                }
            } else {
                board.lastRightCard = Ark.nickname;
                board.changeRightCard(Ark.nickname);
            }
        }
    }

    /**
     * Passes the MSG_UPD_LeaderCardsPicker message to the Game instance, which performs the update.
     * Also updates the GUI to reflect the changes.
     *
     * @param message the MSG_UPD_LeaderCardsPicker update message
     */
    public void updateLeaderCardPicker(MSG_UPD_LeaderCardsPicker message) {
        Ark.game.updateLeaderCardsPicker(message);
        board.leaderCardsPicker_panel.update();
    }

    /**
     * Passes the MSG_UPD_ResourcePicker message to the Game instance, which performs the update.
     * Also updates the GUI to reflect the changes.
     *
     * @param message the MSG_UPD_ResourcePicker update message
     */
    public void updateResourcePicker(MSG_UPD_ResourcePicker message) {
        Ark.game.updateResourcePicker(message);
        board.resourcePicker_panel.update();
    }

    /**
     * Passes the MSG_UPD_MarketHelper message to the Game instance, which performs the update.
     * Also updates the GUI to reflect the changes.
     * If this is the turn of this Player, switches the Cards to the MarketHelper and forth, depending on the MarketHelper enabled status.
     *
     * @param message the MSG_UPD_MarketHelper update message
     */
    public void updateMarketHelper(MSG_UPD_MarketHelper message) {
        Ark.game.updateMarketHelper(message);
        board.marketHelper_panel.update();
        if (Ark.yourTurn) {
            if (Ark.game.isMarketHelperEnabled()) {
                if (!board.lastRightCard.equals(Board.MARKETHELPER)) {
                    board.lastRightCard = Board.MARKETHELPER;
                    board.changeRightCard(Board.MARKETHELPER);
                }
            } else {
                board.lastRightCard = Ark.nickname;
                board.changeRightCard(Ark.nickname);
            }
        }
    }

    /**
     * Passes the MSG_UPD_Game message to the Game instance, which performs the update.
     * <p> In solo mode, it automatically resets the Ark.action field if a new Turn has passed. </p>
     * Also updates the GUI to reflect the changes.
     *
     * @param message the MSG_UPD_Game update message
     */
    public void updateGame(MSG_UPD_Game message) {

        if (Ark.solo) {
            if (Ark.game.getTurn() + 1 == message.getTurn())
                Ark.action = false;
        }

        Ark.game.updateGame(message);

        board.leftPanel.updateTurnOf();
        board.leftPanel.updateCurrentTurn();
        if (Ark.solo)
            board.topPanel.update();
    }

    /**
     * Passes the MSG_UPD_Market message to the Game instance, which performs the update.
     * Also updates the GUI to reflect the changes.
     *
     * @param message the MSG_UPD_Market update message
     */
    public void updateMarket(MSG_UPD_Market message) {
        Ark.game.updateMarket(message);
        board.market_panel.update();
        board.getMarketResource_panel.update();
    }

    /**
     * Passes the MSG_UPD_DevDeck message to the Game instance, which performs the update.
     * Also updates the GUI to reflect the changes.
     *
     * @param message the MSG_UPD_DevDeck update message
     */
    public void updateDevDeck(MSG_UPD_DevDeck message) {
        Ark.game.updateDevelopmentCardsDeck(message);
        board.devDeck_panel.update();
    }

    /**
     * Passes the MSG_UPD_FaithTrack message to the Game instance, which performs the update.
     * Also updates the GUI to reflect the changes.
     *
     * @param message the MSG_UPD_FaithTrack update message
     */
    public void updateFaithTrack(MSG_UPD_FaithTrack message) {
        Ark.game.updateFaithTrack(message);
        board.topPanel.update();
    }

    /**
     * When a MSG_UPD_End message gets executes, a new Player may be the Current Player.
     * <p> In that case, the UpdateHandler can switch to a specific Card if a middle-object is enabled. </p>
     *
     * @param message the MSG_UPD_End update message, which is ignored because it contains nothing interesting.
     */
    public void updateEnd(MSG_UPD_End message) {
        Ark.yourTurn = Ark.game.isMyTurn(Ark.myPlayerNumber);
        if (Ark.yourTurn) {
            if (Ark.game.isMiddleActive()) {
                board.disableBottomButtons();
                if (Ark.game.isLeaderCardsPickerEnabled()) {
                    board.changeRightCard(Board.LEADERCARDSPICKER);
                    board.changeLeftCard(Ark.nickname);
                    board.lastLeftCard = Ark.nickname;
                    board.lastRightCard = Board.LEADERCARDSPICKER;
                } else if (Ark.game.isResourcePickerEnabled()) {
                    board.changeRightCard(Board.RESOURCEPICKER);
                    board.changeLeftCard(Ark.nickname);
                    board.lastLeftCard = Ark.nickname;
                    board.lastRightCard = Board.RESOURCEPICKER;
                } else if (Ark.game.isMarketHelperEnabled()) {
                    board.changeRightCard(Board.MARKETHELPER);
                    board.changeLeftCard(Ark.nickname);
                    board.lastLeftCard = Ark.nickname;
                    board.lastRightCard = Board.MARKETHELPER;
                } else if (Ark.game.isDevelopmentCardsVendorEnabled()) {
                    board.changeRightCard(Board.VENDOR);
                    board.changeLeftCard(Ark.nickname);
                    board.lastLeftCard = Ark.nickname;
                    board.lastRightCard = Board.VENDOR;
                }
            } else {
                if (!still) {
                    board.leftPanel.updateNotification("Your Turn!");
                    Ark.action = false;
                    still = true;
                } else {
                    if (Ark.triedAction) {
                        Ark.action = true;
                        Ark.triedAction = false;
                    }
                    board.leftPanel.updateNotification("Still your Turn!");
                }
                board.enableActionBottomButtons();
            }
        } else {
            still = false;
            if (board.lastRightCard.equals(Board.LEADERCARDSPICKER)) {
                board.lastRightCard = Ark.nickname;
                board.changeRightCard(Ark.nickname);
            } else if (board.lastRightCard.equals(Board.RESOURCEPICKER)) {
                board.lastRightCard = Ark.nickname;
                board.changeRightCard(Ark.nickname);
            }
        }
    }

    /**
     * Passes the MSG_UPD_LeaderBoard message to the Game instance, which performs the update. <p>
     * Also sets the yourTurn field to false, denying the player of furthers actions.
     * Switches to the LeaderBoard Card.
     *
     * @param message the MSG_UPD_LeaderBoard update message
     */
    public void updateLeaderBoard(MSG_UPD_LeaderBoard message) {

        Ark.game.updateLeaderBoard(message);
        Ark.yourTurn = false;

        board.disableBottomButtons();
        board.leaderboard_panel.update();
        board.changeRightCard(Board.LEADERBOARD);
        throw new IllegalArgumentException();
    }

    /**
     * Writes on the notification zone the content of the Notification message.
     *
     * @param message the MSG_NOTIFICATION update message
     */
    public void notify(MSG_NOTIFICATION message) {
        board.leftPanel.updateNotification(message.getMessage());
    }

    /**
     * Writes on the notification zone the content of the Error message.
     *
     * @param message the MSG_ERROR update message
     */
    public void printError(MSG_ERROR message) {
        String error = "";
        if (Ark.yourTurn)
            error += "You got an error: ";
        else
            error += "player " + Ark.game.getCurrentPlayerRef().getNickname() + " got an error: ";
        error += message.getErrorMessage();

        board.leftPanel.updateNotification(error);
    }
}
