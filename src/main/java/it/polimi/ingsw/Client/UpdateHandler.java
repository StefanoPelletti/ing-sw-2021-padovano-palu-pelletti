package it.polimi.ingsw.Client;

import java.io.*;
import java.net.*;

import it.polimi.ingsw.Client.ModelSimplified.GameSimplified;
import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.*;

public class UpdateHandler implements Runnable{

    private final Socket clientSocket;
    private final ObjectInputStream objectInputStream;
    private final GameSimplified game;

    public UpdateHandler(Socket clientSocket, ObjectInputStream objectInputStream, GameSimplified game){
        this.clientSocket = clientSocket;
        this.objectInputStream = objectInputStream;
        this.game = game;
    }

    @Override
    public void run() {
        Message message;
        while(true){
            try {
                message = (Message) objectInputStream.readObject();
                switch (message.getMessageType()){
                    case MSG_UPD_Full:
                        synchronized (game){ game.updateAll((MSG_UPD_Full) message);}
                    case MSG_UPD_Game:
                        synchronized (game){game.updateGame((MSG_UPD_Game) message);}
                    case MSG_UPD_Market:
                        synchronized (game){game.updateMarket((MSG_UPD_Market) message);}
                    case MSG_UPD_DevDeck:
                        synchronized (game){game.updateDevelopmentCardsDeck((MSG_UPD_DevDeck) message);}
                    case MSG_UPD_DevCardsVendor:
                        synchronized (game){game.updateDevelopmentCardsVendor((MSG_UPD_DevCardsVendor) message);}
                    case MSG_UPD_FaithTrack:
                        synchronized (game){game.updateFaithTrack((MSG_UPD_FaithTrack) message);}
                    case MSG_UPD_LeaderBoard: //who closes the connection?
                        synchronized (game){game.updateLeaderBoard((MSG_UPD_LeaderBoard) message);}
                        return;
                    case MSG_UPD_DevSlot:
                        synchronized (game){game.updateCurrentPlayer(message);}
                    case MSG_UPD_Extradepot:
                        synchronized (game){game.updateCurrentPlayer(message);}
                    case MSG_UPD_LeaderCards: //no method in gameSimplified?????
                        synchronized (game){game.updateCurrentPlayer(message);}
                    case MSG_UPD_WarehouseDepot:
                        synchronized (game){game.updateCurrentPlayer(message);}
                    case MSG_UPD_Strongbox:
                        synchronized (game){game.updateCurrentPlayer(message);}
                    case MSG_UPD_Player:
                        synchronized (game){game.updateCurrentPlayer(message);}
                    case MSG_UPD_LeaderCardsObject:
                        synchronized (game){game.updateLeaderCardsObject((MSG_UPD_LeaderCardsObject) message);}
                    case MSG_UPD_ResourceObject:
                        synchronized (game){game.updateResourceObject((MSG_UPD_ResourceObject) message);}
                    case MSG_UPD_MarketHelper:
                        synchronized (game){game.updateMarketHelper((MSG_UPD_MarketHelper) message);}
                    case MSG_UPD_End:
                        synchronized (game){

                        }
                }
            }
            catch(IOException | ClassNotFoundException e){}


        }
    }
}
