package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Server.Controller.ActionManager;
import it.polimi.ingsw.Server.Controller.FaithTrackManager;
import it.polimi.ingsw.Server.Controller.GameManager;
import it.polimi.ingsw.Server.Model.*;

import java.net.*;
import java.io.*;
import java.util.*;


public class Lobby {
    List<Socket> socketList;
    List<String> nicknameList;
    int lobbyNumber;
    int lobbyMaxPlayers;
    Game game;
    ActionManager actionManager;
    FaithTrackManager faithTrackManager;
    GameManager gameManager;

    public Lobby(String nickname, Socket socket, int lobbyNumber, int lobbyMaxPlayers)
    {
        this.socketList = new LinkedList<>();
        this.nicknameList = new LinkedList<>();
        this.socketList.add(socket);
        this.game = null;
        this.actionManager = null;
        this.faithTrackManager = null;
        this.gameManager = null;
        this.lobbyNumber = lobbyNumber;
        this.lobbyMaxPlayers = lobbyMaxPlayers;
    }

    public void init()
    {

    }
    public void onInit(Game game, ActionManager actionManager, FaithTrackManager faithTrackManager, GameManager gameManager){
        this.game = game;
        this.actionManager = actionManager;
        this.faithTrackManager = faithTrackManager;
        this.gameManager = gameManager;
    }

    public String add(String nickname, Socket socket){
        socketList.add(socket);

        if(nicknameList.stream().anyMatch(n-> n.equals(nickname))){
            if(nicknameList.stream().anyMatch(n-> n.equals(nickname+" (1)")))
            {
               if(nicknameList.stream().anyMatch(n-> n.equals(nickname+" (2)")))
               {
                   nicknameList.add(nickname+" (3)");
                   return nickname +" (3)";
               }
               else
               {
                   nicknameList.add(nickname+" (2)");
                   return nickname +" (2)";
               }
            }
            else
            {
                nicknameList.add(nickname+" (1)");
                return nickname +" (1)";
            }
        }
        else
        {
            nicknameList.add(nickname);
            return nickname;
        }
    }


    public int getLobbyMaxPlayers() { return this.lobbyMaxPlayers; }

    public int getLobbyNumber() { return this.lobbyNumber; }

    public int getNumberOfPresentPlayers() {
        assert ( this.socketList.size() == this.nicknameList.size());
        return this.socketList.size();
    }
}
