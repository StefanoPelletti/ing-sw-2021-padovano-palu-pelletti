package it.polimi.ingsw.client.cli;

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

import java.io.IOException;
import java.net.ProtocolException;

public class UpdateHandler implements Runnable {

    boolean still = false;
    /**
     * The UpdateHandler updates a GameSimplified instance which is supposed to be already present in the Halo.
     * The UpdateHandler is also able to show real time messages, from both the Server and the Simplified Model which is updated by Update messages.
     * Also treats the Halo.action field, which indicates if the user has already done a main move.
     */
    @Override
    public void run() {


        Halo.yourTurn = Halo.game.isMyTurn(Halo.myPlayerNumber);

        if (Halo.yourTurn) { // && Halo.game.isLeaderCardsPickerEnabled()
            System.out.println(A.YELLOW + " > You are the First player! "+A.RESET);
            System.out.println(A.YELLOW + " <> Two free Leader Cards!" + A.RESET);
            System.out.println(Halo.game.getLeaderCardsPicker().toString());
            System.out.println(" > "+ A.UL + "Please pick the first card:" + A.RESET);
            System.out.print(" Card number: ");
        }
        else
        {
            if(!Halo.reconnected) {
                System.out.println(A.YELLOW + " > You are the player number " + Halo.myPlayerRef.getPlayerNumber() + "!" + A.RESET);
                System.out.println(A.YELLOW + " > Please wait for your turn to choose the your Leader Cards");
                System.out.print(" > Because you are player number " + Halo.myPlayerRef.getPlayerNumber() + ", you will also receive ");
                if (Halo.myPlayerRef.getPlayerNumber() == 2 || Halo.myPlayerRef.getPlayerNumber() == 3)
                    System.out.println(1 + " resource");
                else
                    System.out.println(2 + " resources");
                if (Halo.myPlayerRef.getPlayerNumber() == 3 || Halo.myPlayerRef.getPlayerNumber() == 4)
                    System.out.println(A.YELLOW + " > And lastly, you are starting in position 1");
            }
        }

        UpdateMessage message;
        while (true) {
            try {
                message = (UpdateMessage) Halo.objectInputStream.readObject();
                message.executeCLI(this);
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                return;
            } catch (IllegalArgumentException e){
                return;
            }
        }
    }

    public void updatePlayer(MSG_UPD_Player message){
        synchronized (Halo.game) {
            Halo.game.updatePlayer(message);
        }
    }

    public void updateCurrentPlayerDepot(MSG_UPD_WarehouseDepot message){
        Halo.game.updateCurrentPlayerDepot(message);
    }

    public void updateCurrentPlayerDevSlot(MSG_UPD_DevSlot message){
        Halo.game.updateCurrentPlayerDevSlot(message);
    }

    public void updateCurrentPlayerExtraDepot(MSG_UPD_Extradepot message){
        Halo.game.updateCurrentPlayerExtraDepot(message);
    }

    public void updateCurrentPlayerStrongbox(MSG_UPD_Strongbox message){
        Halo.game.updateCurrentPlayerStrongbox(message);
    }

    public void updateDevCardVendor(MSG_UPD_DevCardsVendor message){
        synchronized (Halo.game) {
            Halo.game.updateDevelopmentCardsVendor(message);
        }
    }

    public void updateLeaderCardPicker(MSG_UPD_LeaderCardsPicker message){
        synchronized (Halo.game) {
            Halo.game.updateLeaderCardsPicker(message);
        }
    }

    public void updateResourcePicker(MSG_UPD_ResourcePicker message){
        synchronized (Halo.game) {
            Halo.game.updateResourcePicker(message);
        }
    }

    public void updateMarketHelper(MSG_UPD_MarketHelper message){
        synchronized (Halo.game) {
            Halo.game.updateMarketHelper(message);
        }
    }

    public void updateGame(MSG_UPD_Game message){
        synchronized (Halo.game) {
            if (Halo.solo) {
                if (Halo.game.getTurn() + 1 == message.getTurn()) Halo.action = false;
            }
            Halo.game.updateGame(message);
        }
    }

    public void updateMarket(MSG_UPD_Market message){
        synchronized (Halo.game) {
            Halo.game.updateMarket(message);
        }
    }

    public void updateDevDeck(MSG_UPD_DevDeck message){
        synchronized (Halo.game) {
            Halo.game.updateDevelopmentCardsDeck(message);
        }
    }

    public void updateFaithTrack(MSG_UPD_FaithTrack message){
        synchronized (Halo.game) {
            Halo.game.updateFaithTrack(message);
        }
    }

    public void updateEnd(MSG_UPD_End message){
        synchronized (Halo.game) {
            Halo.yourTurn = Halo.game.isMyTurn(Halo.myPlayerNumber);
            if (Halo.yourTurn) {
                if (Halo.game.isMiddleActive()) {
                    if (Halo.game.isLeaderCardsPickerEnabled()) {
                        System.out.println(A.YELLOW + " <> Two free Leader Cards!" + A.RESET);
                        System.out.println(Halo.game.getLeaderCardsPicker().toString());
                        System.out.println(" > " + A.UL + "Please pick the first card:" + A.RESET);
                        System.out.print(" Card number: ");
                    } else if (Halo.game.isResourcePickerEnabled()) {
                        System.out.println(A.YELLOW + " <> Free resources!" + A.RESET);
                        System.out.println(Halo.game.getResourcePicker().toString());
                        System.out.println(" > " + A.UL + "Please pick the resource you want:" + A.RESET);
                        System.out.print(" Resource number: ");
                    } else if (Halo.game.isMarketHelperEnabled()) {
                        System.out.println(Halo.game.getMarketHelper().toString());
                        System.out.println(Halo.game.getCurrentPlayerRef().getWarehouseDepot());
                        System.out.println(" > " + A.UL + "Please select an option:" + A.RESET);
                        System.out.print(" Choice: ");
                    } else if (Halo.game.isDevelopmentCardsVendorEnabled()) {
                        System.out.println(Halo.game.getDevelopmentCardsVendor().toString());
                        System.out.println(" >" + A.UL + "Please select a card number and a slot number (0 to quit the action):" + A.RESET);
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
    }

    public void updateLeaderBoard (MSG_UPD_LeaderBoard message){
        synchronized (Halo.game) {
            Halo.game.updateLeaderBoard(message);
            Halo.yourTurn = false;
            System.out.println(A.YELLOW + " <> Game over : " + A.RESET + Halo.game.getLeaderBoard().toResult(Halo.myNickname, Halo.solo));
            System.out.println(" > " + A.UL + "Please write quit to go back to the Main Menu." + A.RESET);
        }
        throw new IllegalArgumentException();
    }

    public void notify (MSG_NOTIFICATION message){
        synchronized (Halo.game) {
            System.out.println();
            System.out.println(A.YELLOW + " <> " + (message).getMessage() + A.RESET);
        }
    }

    public void printError(MSG_ERROR message){
        synchronized (Halo.game) {
            if (Halo.yourTurn)
                System.out.println(A.RED + " <> You got an error: " + A.RESET);
            else
                System.out.println(A.RED + " <> player " + Halo.game.getCurrentPlayerRef().getNickname() + " got an error: " + A.RESET);
            System.out.println(A.RED + " <> " + ((MSG_ERROR) message).getErrorMessage() + A.RESET);
        }
    }



}
