package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Server.ClientHandler;
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
    List<ClientHandler> threadsList;

    int lobbyNumber;
    int lobbyMaxPlayers;



    public Lobby(String nickname, Socket socket, int lobbyNumber, int lobbyMaxPlayers)
    {
        this.socketList = new LinkedList<>();
        this.nicknameList = new LinkedList<>();
        this.threadsList = new LinkedList<>();
        this.socketList.add(socket);

        this.lobbyNumber = lobbyNumber;
        this.lobbyMaxPlayers = lobbyMaxPlayers;

    }

    public void init() {
        GameManager gameManager = new GameManager(this.lobbyMaxPlayers);
        Thread thread;
        List<Integer> playerNumbers = new LinkedList<>();

        for(int i = 0; i<lobbyMaxPlayers; i++) {
            playerNumbers.add(i);
        }

        Collections.shuffle(playerNumbers);
        Game game = gameManager.getGame();

        for(int i = 0; i<lobbyMaxPlayers; i++) {
            game.addPlayer(nicknameList.get(i), playerNumbers.get(i));
            thread = new Thread(new ClientHandler(socketList.get(i),gameManager, this, nicknameList.get(i)));
            thread.start();
        }
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
