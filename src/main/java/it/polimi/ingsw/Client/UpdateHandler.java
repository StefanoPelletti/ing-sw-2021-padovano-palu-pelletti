package it.polimi.ingsw.Client;

import java.io.*;

import it.polimi.ingsw.Networking.Message.MSG_NOTIFICATION;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.*;

public class UpdateHandler implements Runnable{

    @Override
    public void run() {

        Halo.handler = true;

        System.out.println("UpdateHandler avviato master: "+master);


        Message message;
        while(true){
            try {
                message = (Message) Halo.objectInputStream.readObject();
                switch (message.getMessageType()){
// Player updates
                    case MSG_UPD_Player:
                        synchronized (Halo.game){ Halo.game.updatePlayer(message);}
                        break;
// Current Player updates
                    case MSG_UPD_DevSlot:
                    case MSG_UPD_Extradepot:
                    case MSG_UPD_WarehouseDepot:
                    case MSG_UPD_Strongbox:
                        synchronized (Halo.game){ Halo.game.updateCurrentPlayer(message);}
                        break;
// Middle objects update
                    case MSG_UPD_DevCardsVendor:
                        synchronized (Halo.game){ Halo.game.updateDevelopmentCardsVendor((MSG_UPD_DevCardsVendor) message);}
                        break;
                    case MSG_UPD_LeaderCardsObject:
                        synchronized (Halo.game){Halo.game.updateLeaderCardsObject((MSG_UPD_LeaderCardsObject) message);}
                        break;
                    case MSG_UPD_ResourceObject:
                        synchronized (Halo.game){Halo.game.updateResourceObject((MSG_UPD_ResourceObject) message);}
                        break;
                    case MSG_UPD_MarketHelper:
                        synchronized (Halo.game){Halo.game.updateMarketHelper((MSG_UPD_MarketHelper) message);}
                        break;
// Shared objects update
                    case MSG_UPD_Game:
                        synchronized (Halo.game){ Halo.game.updateGame((MSG_UPD_Game) message);}
                        break;
                    case MSG_UPD_Market:
                        synchronized (Halo.game){ Halo.game.updateMarket((MSG_UPD_Market) message);}
                        break;
                    case MSG_UPD_DevDeck:
                        synchronized (Halo.game){ Halo.game.updateDevelopmentCardsDeck((MSG_UPD_DevDeck) message);}
                        break;
                    case MSG_UPD_FaithTrack:
                        synchronized (Halo.game){ Halo.game.updateFaithTrack((MSG_UPD_FaithTrack) message);}
                        break;
//End update
                    case MSG_UPD_End:
                        synchronized (Halo.game){
                            Halo.yourTurn = Halo.game.isMyTurn(Halo.myPlayerNumber);
                            if(Halo.yourTurn) {
                                if (Halo.game.isMiddleActive()) {
                                    if (Halo.game.isLeaderCardsObjectEnabled()) {
                                        System.out.println(" >> Two free Leader Cards!");
                                        System.out.println(Halo.game.getLeaderCardsObject().toString());
                                        System.out.println(" > Please pick the first card of your wish:");
                                    } else if (Halo.game.isResourceObjectEnabled()) {
                                        System.out.println(" >> Free resources!");
                                        System.out.println(Halo.game.getResourceObject().toString());
                                        System.out.println(" > Please pick the resource you want:");
                                    } else if (Halo.game.isMarketHelperEnabled()) {
                                        System.out.println(Halo.game.getMarketHelper().toString());
                                        System.out.println(" > Please select an option:");
                                    } else if (Halo.game.isDevelopmentCardsVendorEnabled()) {
                                        System.out.println(Halo.game.getDevelopmentCardsVendor().toString());
                                        System.out.println(" > Please select a card number and a slot number:");
                                    }
                                }
                                else {
                                    System.out.println(" >> Your Turn! You may use the <action> command!");
                                }

                            }
                            else{

                                System.out.println(" >> Turn of     "+ Halo.game.getCurrentPlayerRef().getNickname());
                            }
                        }
                        break;
//final update
                    case MSG_UPD_LeaderBoard: //who closes the connection?
                        synchronized (Halo.game){ Halo.game.updateLeaderBoard((MSG_UPD_LeaderBoard) message);}
                        return;
//Notification
                    case MSG_NOTIFICATION:
                        System.out.println(( (MSG_NOTIFICATION) message).getMessage());
                        break;

                    case MSG_ERROR:
                        System.out.println(" <> SRV: "+Halo.game.getCurrentPlayerRef().getNickname()+" got an error: ");
                        System.out.println(((MSG_ERROR) message).getErrorMessage());
                        break;
                }
            }
            catch(IOException | ClassNotFoundException | ClassCastException e){
                //e.printStackTrace();
                System.out.println(e.getMessage());
                return;
            }
        }
    }
}
