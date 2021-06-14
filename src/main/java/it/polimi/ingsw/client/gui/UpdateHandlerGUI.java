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
import it.polimi.ingsw.server.utils.A;
import it.polimi.ingsw.server.utils.ModelObserver;

import java.io.IOException;

public class UpdateHandlerGUI implements Runnable, ModelObserver {

    private final Board board;
    private boolean still;


    static final String DEVDECK                 = "Development Cards Deck";
    static final String MARKET                  = "Market";
    static final String GETMARKETRESOURCES      = "Get Market Resources";
    static final String LEADERCARDSPICKER       = "Leader Cards Picker";
    static final String RESOURCEPICKER          = "Resource Picker";
    static final String CHANGEDEPOTCONFIG       = "Change Depot Configuration";
    static final String DISCARDLEADERCARD       = "Discard LeaderCard";
    static final String ACTIVATELEADERCARD      = "Activate Leader Card";
    static final String MARKETHELPER            = "Market Helper";
    static final String VENDOR                  = "Development Cards Vendor";
    static final String PRODUCTION              = "Production";
    static final String PRODSELECTION           = "Production Selection";

    public UpdateHandlerGUI(Board boardRef) {
        this.board = boardRef;
    }

    @Override
    public void run() {
        Ark.yourTurn = Ark.game.isMyTurn(Ark.myPlayerNumber);

        if (Ark.yourTurn) { // && Ark.game.isLeaderCardsPickerEnabled()
            board.leftPanel.updateNotification("You are the First Player!");
        } else {
            if(!Ark.reconnected) {
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


    @Override
    public void update(Message message) {
        UpdateMessage msg = (UpdateMessage) message;
        msg.executeGUI(this);
    }


    public void updatePlayer(MSG_UPD_Player message) {
        String name = message.getNickname();

        synchronized (Ark.game) {
            Ark.game.updatePlayer(message);
            board.centralLeftPanel.update(name);
            board.centralRightPanel.update(name);
            board.bottomPanel.update();
            board.topPanel.update();
            if(name.equals(Ark.nickname)) {
                board.leftPanel.update();
                board.activateLeaderCard_panel.update();
                board.discardLeaderCard_panel.update();
            }
        }
    }

    public void updateCurrentPlayerDepot(MSG_UPD_WarehouseDepot message) {
        Ark.game.updateCurrentPlayerDepot(message);

        String name = Ark.game.getCurrentPlayerName();
        board.centralLeftPanel.update(name);
    }

    public void updateCurrentPlayerDevSlot(MSG_UPD_DevSlot message) {
        Ark.game.updateCurrentPlayerDevSlot(message);

        String name = Ark.game.getCurrentPlayerName();
        board.centralRightPanel.update(name);
    }

    public void updateCurrentPlayerExtraDepot(MSG_UPD_Extradepot message) {
        Ark.game.updateCurrentPlayerExtraDepot(message);

        if(Ark.yourTurn)
            board.leftPanel.update();
    }

    public void updateCurrentPlayerStrongbox(MSG_UPD_Strongbox message) {
        Ark.game.updateCurrentPlayerStrongbox(message);

        String name = Ark.game.getCurrentPlayerName();
        board.centralLeftPanel.update(name);
    }

    public void updateDevCardVendor(MSG_UPD_DevCardsVendor message) {
        synchronized (Ark.game) {
            Ark.game.updateDevelopmentCardsVendor(message);
            board.vendor_panel.update();
        }
    }

    public void updateLeaderCardPicker(MSG_UPD_LeaderCardsPicker message) {
        synchronized (Ark.game) {
            Ark.game.updateLeaderCardsPicker(message);
            board.leaderCardsPicker_panel.update();
        }
    }

    public void updateResourcePicker(MSG_UPD_ResourcePicker message) {
        synchronized (Ark.game) {
            Ark.game.updateResourcePicker(message);
            board.resourcePicker_panel.update();
        }
    }

    public void updateMarketHelper(MSG_UPD_MarketHelper message) {
        synchronized (Ark.game) {
            Ark.game.updateMarketHelper(message);
            board.marketHelper_panel.update();
        }
    }

    public void updateGame(MSG_UPD_Game message) {
        synchronized (Ark.game) {
            if (Ark.solo) {
                if (Ark.game.getTurn() + 1 == message.getTurn())
                    Ark.action = false;
            }
            Ark.game.updateGame(message);
            board.leftPanel.updateTurnOf();
            board.leftPanel.updateCurrentTurn();
        }
    }

    public void updateMarket(MSG_UPD_Market message) {
        synchronized (Ark.game) {
            Ark.game.updateMarket(message);
            board.market_panel.update();
            board.getMarketResource_panel.update();
        }
    }

    public void updateDevDeck(MSG_UPD_DevDeck message) {
        synchronized (Ark.game) {
            Ark.game.updateDevelopmentCardsDeck(message);
            board.devDeck_panel.update();
        }
    }

    public void updateFaithTrack(MSG_UPD_FaithTrack message) {
        synchronized (Ark.game) {
            Ark.game.updateFaithTrack(message);
            board.topPanel.update();
        }
    }

    public void updateEnd(MSG_UPD_End message) {
        synchronized (Ark.game) {
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
                    board.enableActionBottomButtons();
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
                }
            } else {
                still = false;
            }
        }
    }


    public void updateLeaderBoard(MSG_UPD_LeaderBoard message) {
        synchronized (Ark.game) {
            Ark.game.updateLeaderBoard(message);
            Ark.yourTurn = false;

            board.disableBottomButtons();

            //TODO
            /*
            System.out.println(A.YELLOW + " <> Game over : " + A.RESET + Ark.game.getLeaderBoard().toResult(Ark.myNickname, Ark.solo));
            System.out.println(" > " + A.UL + "Please write quit to go back to the Main Menu." + A.RESET);

             */
        }
        throw new IllegalArgumentException();
    }

    public void notify(MSG_NOTIFICATION message) {
        board.leftPanel.updateNotification(message.getMessage());
    }

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
