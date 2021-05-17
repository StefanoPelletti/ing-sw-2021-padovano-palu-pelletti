package it.polimi.ingsw.Networking;

public class allDisconnectedThread implements Runnable{
    private final Lobby lobby;

    public allDisconnectedThread(Lobby lobby)
    {
        this.lobby=lobby;
    }

    public void run() {
        synchronized (lobby)
        {
            Lobby.removeLobby(lobby);
            this.lobby.setDeleted(true);
            lobby.wakeUpAllPendingClientHandlers();
            System.out.println("[CountDown "+Thread.currentThread().getName()+"] : lobby "+lobby.getLobbyNumber()+" deleted cause all players have disconnected");

        }
    }
}
