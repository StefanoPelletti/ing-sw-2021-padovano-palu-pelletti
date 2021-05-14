package it.polimi.ingsw.Client;

import java.io.*;

import it.polimi.ingsw.Networking.Message.MSG_NOTIFICATION;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.*;

public class UpdateHandler implements Runnable{

    @Override
    public void run() {
        boolean still = false;
        Halo.yourTurn = Halo.game.isMyTurn(Halo.myPlayerNumber);

        if(Halo.yourTurn) { // && Halo.game.isLeaderCardsObjectEnabled()
            System.out.println(" <> Two free Leader Cards!");
            System.out.println(Halo.game.getLeaderCardsObject().toString());
            System.out.println(" > Please pick the first card of your wish:");
            System.out.print(" card number: ");
        }

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
                                        System.out.println(" <> Two free Leader Cards!");
                                        System.out.println(Halo.game.getLeaderCardsObject().toString());
                                        System.out.println(" > Please pick the first card of your wish:");
                                        System.out.print(" card number: ");
                                    } else if (Halo.game.isResourceObjectEnabled()) {
                                        System.out.println(" <> Free resources!");
                                        System.out.println(Halo.game.getResourceObject().toString());
                                        System.out.println(" > Please pick the resource you want:");
                                        System.out.print(" resource number: ");
                                    } else if (Halo.game.isMarketHelperEnabled()) {
                                        System.out.println(Halo.game.getMarketHelper().toString());
                                        System.out.println(Halo.game.getCurrentPlayerRef().getWarehouseDepot());
                                        System.out.println(" > Please select an option:");
                                        System.out.print(" choice: ");
                                    } else if (Halo.game.isDevelopmentCardsVendorEnabled()) {
                                        System.out.println(Halo.game.getDevelopmentCardsVendor().toString());
                                        System.out.println(" > Please select a card number and a slot number:");
                                        System.out.print(" card | slot: ");
                                    }
                                }
                                else {
                                    if(still == false)
                                    {
                                        System.out.println(Halo.ANSI_GREEN+ " <> Your Turn! You may use the <action> command!"+Halo.ANSI_RESET);
                                        still = true;
                                    }
                                    else
                                    {
                                        System.out.println(Halo.ANSI_GREEN+ " <> Still your turn!"+Halo.ANSI_RESET);
                                    }
                                }

                            }
                            else{
                                System.out.println(Halo.ANSI_GREEN+" <> Turn of "+ Halo.game.getCurrentPlayerRef().getNickname()+Halo.ANSI_RESET);
                                still=false;
                            }
                        }
                        break;
//final update
                    case MSG_UPD_LeaderBoard: //who closes the connection?
                        synchronized (Halo.game){
                            Halo.game.updateLeaderBoard((MSG_UPD_LeaderBoard) message);
                            System.out.println(" <> Game over : "+Halo.game.getLeaderBoard().toResult(Halo.myNickname));
                            System.out.println(" > press any key to go back to the main menu");
                        }
                        return;
//Notification
                    case MSG_NOTIFICATION:
                        System.out.println(( (MSG_NOTIFICATION) message).getMessage());
                        break;

                    case MSG_ERROR:
                        if(Halo.yourTurn)
                            System.out.println(Halo.ANSI_RED+" <> You got an error: "+Halo.ANSI_RESET);
                        else
                            System.out.println(Halo.ANSI_RED+" <> player "+Halo.game.getCurrentPlayerRef().getNickname()+" got an error: "+Halo.ANSI_RESET);
                        System.out.println(Halo.ANSI_RED+" <> "+((MSG_ERROR) message).getErrorMessage()+Halo.ANSI_RESET);
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
