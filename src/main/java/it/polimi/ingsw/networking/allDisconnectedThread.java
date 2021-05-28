package it.polimi.ingsw.networking;

public class allDisconnectedThread implements Runnable {
    private final Lobby lobby;

    /**
     * Construct a object designed to destroy a specified Lobby in which there are no more active players
     *  Wakes Up all pending-connection ClientHandlers, to interrupt them correctly
     * @param lobby the specified lobby to destroy
     */
    public allDisconnectedThread(Lobby lobby) {
        this.lobby = lobby;
    }

    public void run() {
        synchronized (lobby) {
            Lobby.removeLobby(lobby);
            this.lobby.setDeleted(true);
            lobby.wakeUpAllPendingClientHandlers();
            System.out.println("[CountDown " + Thread.currentThread().getName() + "] : lobby " + lobby.getLobbyNumber() + " deleted cause all players have disconnected");
        }
    }
}