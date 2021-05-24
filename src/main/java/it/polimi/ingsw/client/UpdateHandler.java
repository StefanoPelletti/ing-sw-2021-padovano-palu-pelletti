package it.polimi.ingsw.client;

import it.polimi.ingsw.networking.message.MSG_ERROR;
import it.polimi.ingsw.networking.message.MSG_NOTIFICATION;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_DevDeck;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_FaithTrack;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Game;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Market;
import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.*;
import it.polimi.ingsw.server.utils.A;

import java.io.IOException;

public class UpdateHandler implements Runnable {

    @Override
    public void run() {
        boolean still = false;

        Halo.yourTurn = Halo.game.isMyTurn(Halo.myPlayerNumber);

        if (Halo.yourTurn) { // && Halo.game.isLeaderCardsPickerEnabled()
            System.out.println(A.YELLOW + " > You are the First player! "+A.RESET);
            System.out.println(A.YELLOW + " <> Two free Leader Cards!" + A.RESET);
            System.out.println(Halo.game.getLeaderCardsPicker().toString());
            System.out.println(" > Please pick the first card:");
            System.out.print(" Card number: ");
        }
        else
        {
            System.out.println(A.YELLOW + " > You are the player number "+Halo.myPlayerRef.getPlayerNumber()+"!"+A.RESET);
            System.out.println(A.YELLOW + " > Please wait for your turn to choose the your Leader Cards");
            System.out.print(" > Because you are player number "+Halo.myPlayerRef.getPlayerNumber()+", you will also receive ");
            if(Halo.myPlayerRef.getPlayerNumber() == 2 || Halo.myPlayerRef.getPlayerNumber()==3)
                System.out.println(1+ " resource");
            else
                System.out.println(2+ " resources");
            if(Halo.myPlayerRef.getPlayerNumber() == 3 || Halo.myPlayerRef.getPlayerNumber() == 4)
                System.out.println(A.YELLOW + " > And lastly, you are starting in position 1");
        }

        Message message;
        while (true) {
            try {
                message = (Message) Halo.objectInputStream.readObject();

                switch (message.getMessageType()) {
// Player updates
                    case MSG_UPD_Player:
                        synchronized (Halo.game) {
                            Halo.game.updatePlayer(message);
                        }
                        break;
// Current Player updates
                    case MSG_UPD_DevSlot:
                    case MSG_UPD_Extradepot:
                    case MSG_UPD_WarehouseDepot:
                    case MSG_UPD_Strongbox:
                        synchronized (Halo.game) {
                            Halo.game.updateCurrentPlayer(message);
                        }
                        break;
// Middle objects update
                    case MSG_UPD_DevCardsVendor:
                        synchronized (Halo.game) {
                            Halo.game.updateDevelopmentCardsVendor((MSG_UPD_DevCardsVendor) message);
                        }
                        break;
                    case MSG_UPD_LeaderCardsPicker:
                        synchronized (Halo.game) {
                            Halo.game.updateLeaderCardsPicker((MSG_UPD_LeaderCardsPicker) message);
                        }
                        break;
                    case MSG_UPD_ResourcePicker:
                        synchronized (Halo.game) {
                            Halo.game.updateResourcePicker((MSG_UPD_ResourcePicker) message);
                        }
                        break;
                    case MSG_UPD_MarketHelper:
                        synchronized (Halo.game) {
                            Halo.game.updateMarketHelper((MSG_UPD_MarketHelper) message);
                        }
                        break;
// Shared objects update
                    case MSG_UPD_Game:
                        synchronized (Halo.game) {
                            if (Halo.solo) {
                                if (Halo.game.getTurn() + 1 == ((MSG_UPD_Game) message).getTurn()) Halo.action = false;
                            }
                            Halo.game.updateGame((MSG_UPD_Game) message);
                        }
                        break;
                    case MSG_UPD_Market:
                        synchronized (Halo.game) {
                            Halo.game.updateMarket((MSG_UPD_Market) message);
                        }
                        break;
                    case MSG_UPD_DevDeck:
                        synchronized (Halo.game) {
                            Halo.game.updateDevelopmentCardsDeck((MSG_UPD_DevDeck) message);
                        }
                        break;
                    case MSG_UPD_FaithTrack:
                        synchronized (Halo.game) {
                            Halo.game.updateFaithTrack((MSG_UPD_FaithTrack) message);
                        }
                        break;
//End update
                    case MSG_UPD_End:
                        synchronized (Halo.game) {
                            Halo.yourTurn = Halo.game.isMyTurn(Halo.myPlayerNumber);
                            if (Halo.yourTurn) {
                                if (Halo.game.isMiddleActive()) {
                                    if (Halo.game.isLeaderCardsPickerEnabled()) {
                                        System.out.println(A.YELLOW + " <> Two free Leader Cards!" + A.RESET);
                                        System.out.println(Halo.game.getLeaderCardsPicker().toString());
                                        System.out.println(A.UL + " > Please pick the first card" + A.RESET);
                                        System.out.print(" Card number: ");
                                    } else if (Halo.game.isResourcePickerEnabled()) {
                                        System.out.println(A.YELLOW + " <> Free resources!" + A.RESET);
                                        System.out.println(Halo.game.getResourcePicker().toString());
                                        System.out.println(A.UL + " > Please pick the resource you want" + A.RESET);
                                        System.out.print(" Resource number: ");
                                    } else if (Halo.game.isMarketHelperEnabled()) {
                                        System.out.println(Halo.game.getMarketHelper().toString());
                                        System.out.println(Halo.game.getCurrentPlayerRef().getWarehouseDepot());
                                        System.out.println(A.UL + " > Please select an option" + A.RESET);
                                        System.out.print(" Choice: ");
                                    } else if (Halo.game.isDevelopmentCardsVendorEnabled()) {
                                        System.out.println(Halo.game.getDevelopmentCardsVendor().toString());
                                        System.out.println(A.UL + " > Please select a card number and a slot number (0 to quit the action)" + A.RESET);
                                        System.out.print(" Card | Slot: ");
                                    }
                                } else {
                                    if (!still) {
                                        System.out.println(A.GREEN + " <> Your Turn! You may use the " + A.CYAN + "action" + A.GREEN + " command!" + A.RESET);
                                        Halo.action = false;
                                        still = true;
                                    } else {
                                        if (Halo.triedAction) {
                                            Halo.action = true;
                                            Halo.triedAction = false;
                                        }
                                        System.out.println(A.GREEN + " <> Still your turn!" + A.RESET);
                                    }
                                }

                            } else {
                                System.out.println(A.GREEN + " <> Turn of " + Halo.game.getCurrentPlayerRef().getNickname() + A.RESET);
                                still = false;
                            }
                        }
                        break;
//final update
                    case MSG_UPD_LeaderBoard: //who closes the connection?
                        synchronized (Halo.game) {
                            Halo.game.updateLeaderBoard((MSG_UPD_LeaderBoard) message);
                            Halo.yourTurn = false;
                            System.out.println(A.YELLOW + " <> Game over : " + A.RESET + Halo.game.getLeaderBoard().toResult(Halo.myNickname, Halo.solo));
                            System.out.println(A.UL + " > Please write quit to go back to the main menu" + A.RESET);
                        }
                        return;
//Notification
                    case MSG_NOTIFICATION:
                        synchronized (Halo.game) {
                            System.out.println();
                            System.out.println(A.YELLOW + " <> " + ((MSG_NOTIFICATION) message).getMessage() + A.RESET);
                            break;
                        }

                    case MSG_ERROR:
                        synchronized (Halo.game) {
                            if (Halo.yourTurn)
                                System.out.println(A.RED + " <> You got an error: " + A.RESET);
                            else
                                System.out.println(A.RED + " <> player " + Halo.game.getCurrentPlayerRef().getNickname() + " got an error: " + A.RESET);
                            System.out.println(A.RED + " <> " + ((MSG_ERROR) message).getErrorMessage() + A.RESET);
                        }
                        break;
                }
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                //e.printStackTrace();
                System.out.println(e.getMessage());
                return;
            }
        }
    }
}
