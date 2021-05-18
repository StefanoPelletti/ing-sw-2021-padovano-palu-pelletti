package it.polimi.ingsw.Client;

import it.polimi.ingsw.Networking.Message.MSG_ERROR;
import it.polimi.ingsw.Networking.Message.MSG_NOTIFICATION;
import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_DevDeck;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_FaithTrack;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_Game;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_Market;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.*;
import it.polimi.ingsw.Server.Utils.ModelObserver;

public class UpdateHandlerLocal implements ModelObserver {

    @Override
    public void update(Message message) {

        switch (message.getMessageType()) {
//End update
            case MSG_UPD_End:
                synchronized (Halo.gameSRV) {
                    Halo.yourTurn = true;
                    if (Halo.gameSRV.isMiddleActive()) {
                        if (Halo.gameSRV.isMarketHelperEnabled()) {
                            System.out.println(Halo.gameSRV.getMarketHelper().toString());
                            System.out.println(Halo.gameSRV.getCurrentPlayer().getWarehouseDepot());
                            System.out.println(" > Please select an option:");
                            System.out.print(" Choice: ");
                        } else if (Halo.gameSRV.isDevelopmentCardsVendorEnabled()) {
                            System.out.println(Halo.gameSRV.getDevelopmentCardsVendor().toString());
                            System.out.println(" > Please select a card number and a slot number:");
                            System.out.print(" card | slot: ");
                        }
                    } else {
                        System.out.println(Halo.ANSI_GREEN + " <> Still your turn!" + Halo.ANSI_RESET);
                    }
                }
                break;
//final update
            case MSG_UPD_LeaderBoard:
                synchronized (Halo.gameSRV) {
                    System.out.println(" <> Game over : " + Halo.gameSRV.getLeaderBoard().toResult(Halo.myNickname, Halo.solo));
                    System.out.println(" > Please write quit to go back to the main menu");
                }
                return;
//Notification
            case MSG_NOTIFICATION:
                System.out.println(" <> " + ((MSG_NOTIFICATION) message).getMessage());
                break;
            case MSG_ERROR:
                System.out.println(Halo.ANSI_RED + " <> You got an error: " + Halo.ANSI_RESET);
                break;
            default:
                System.out.println("DEBUG handler : received : "+message.getMessageType());
                break;
        }
    }
}
