package it.polimi.ingsw.Client;

import java.io.*;

import it.polimi.ingsw.Networking.Message.MSG_NOTIFICATION;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.*;
import it.polimi.ingsw.Server.Utils.A;

public class UpdateHandler implements Runnable {

    @Override
    public void run() {
        boolean still = false;

        Halo.yourTurn = Halo.game.isMyTurn(Halo.myPlayerNumber);

        if (Halo.yourTurn) { // && Halo.game.isLeaderCardsObjectEnabled()
            System.out.println(A.YELLOW+ " <> Two free Leader Cards!"+A.RESET);
            System.out.println(Halo.game.getLeaderCardsObject().toString());
            System.out.println(" > Please pick the first card:");
            System.out.print(" Card number: ");
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
                    case MSG_UPD_LeaderCardsObject:
                        synchronized (Halo.game) {
                            Halo.game.updateLeaderCardsObject((MSG_UPD_LeaderCardsObject) message);
                        }
                        break;
                    case MSG_UPD_ResourceObject:
                        synchronized (Halo.game) {
                            Halo.game.updateResourceObject((MSG_UPD_ResourceObject) message);
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
                                if(Halo.game.getTurn()+1==((MSG_UPD_Game) message).getTurn()) Halo.action= false;
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
                                    if (Halo.game.isLeaderCardsObjectEnabled()) {
                                        System.out.println(A.YELLOW+" <> Two free Leader Cards!"+A.RESET);
                                        System.out.println(Halo.game.getLeaderCardsObject().toString());
                                        System.out.println(A.UL+" > Please pick the first card"+A.RESET);
                                        System.out.print(" Card number: ");
                                    } else if (Halo.game.isResourceObjectEnabled()) {
                                        System.out.println(A.YELLOW+" <> Free resources!"+A.RESET);
                                        System.out.println(Halo.game.getResourceObject().toString());
                                        System.out.println(A.UL+" > Please pick the resource you want"+A.RESET);
                                        System.out.print(" Resource number: ");
                                    } else if (Halo.game.isMarketHelperEnabled()) {
                                        System.out.println(Halo.game.getMarketHelper().toString());
                                        System.out.println(Halo.game.getCurrentPlayerRef().getWarehouseDepot());
                                        System.out.println(A.UL+" > Please select an option"+A.RESET);
                                        System.out.print(" Choice: ");
                                    } else if (Halo.game.isDevelopmentCardsVendorEnabled()) {
                                        System.out.println(Halo.game.getDevelopmentCardsVendor().toString());
                                        System.out.println(A.UL+" > Please select a card number and a slot number (0 to quit the action)"+A.RESET);
                                        System.out.print(" Card | Slot: ");
                                    }
                                } else {
                                    if (!still) {
                                        System.out.println(A.GREEN + " <> Your Turn! You may use the "+A.CYAN+"action"+A.GREEN+" command!" + A.RESET);
                                        Halo.action=false;
                                        still = true;
                                    } else {
                                        if(Halo.triedAction) {
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
                            Halo.yourTurn=false;
                            System.out.println(A.YELLOW+" <> Game over : " +A.RESET+ Halo.game.getLeaderBoard().toResult(Halo.myNickname, Halo.solo));
                            System.out.println(A.UL+" > Please write quit to go back to the main menu"+A.RESET);
                        }
                        return;
//Notification
                    case MSG_NOTIFICATION:
                        synchronized (Halo.game) {
                            System.out.println();
                            System.out.println(A.YELLOW+" <> " + ((MSG_NOTIFICATION) message).getMessage()+A.RESET);
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
