package it.polimi.ingsw.Client;

import it.polimi.ingsw.Networking.Message.MSG_NOTIFICATION;
import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Server.Utils.A;
import it.polimi.ingsw.Server.Utils.ModelObserver;

public class UpdateHandlerLocal implements ModelObserver {

    @Override
    public void update(Message message) {

        switch (message.getMessageType()) {
//End update
            case MSG_UPD_End:
                if (Halo.gameSRV.isMiddleActive()) {
                    if (Halo.gameSRV.isMarketHelperEnabled()) {
                        System.out.println(Halo.gameSRV.getMarketHelper().toString());
                        System.out.println(Halo.gameSRV.getCurrentPlayer().getWarehouseDepot());
                        System.out.println(A.UL + " > Please select an option" + A.RESET);
                        System.out.print(" Choice: ");
                    } else if (Halo.gameSRV.isDevelopmentCardsVendorEnabled()) {
                        System.out.println(Halo.gameSRV.getDevelopmentCardsVendor().toString());
                        System.out.println(A.UL + " > Please select a card number and a slot number" + A.RESET);
                        System.out.print(" Card | Slot: (0 to quit the action)");
                    }
                } else {
                    System.out.println(A.GREEN + " <> Still your turn!" + A.RESET);
                }
                break;
//final update
            case MSG_UPD_LeaderBoard:
                System.out.println(A.YELLOW + " <> Game over : " + A.RESET + Halo.gameSRV.getLeaderBoard().toResult(Halo.myNickname, Halo.solo));
                System.out.println(A.UL + " > Please write " + A.CYAN + "quit" + A.UL + " to go back to the main menu" + A.RESET);
                Halo.yourTurn = false;
                return;
//Notification
            case MSG_NOTIFICATION:
                System.out.println(A.YELLOW + " <> " + ((MSG_NOTIFICATION) message).getMessage() + A.RESET);
                break;
            case MSG_ERROR:
                System.out.println(A.RED + " <> You got an error: " + A.RESET);
                break;
            default:
                //System.out.println("DEBUG handler : received : "+message.getMessageType());
                break;
        }
    }
}
