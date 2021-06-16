package it.polimi.ingsw.networking;


/**
 * The allDisconnectedThread class implements a Thread that will destroy a specified Lobby.
 * <p>
 * Three key points are necessary to understanding this method:
 * <ul>
 *     <li> this Thread is called after EVERY ClientHandler have gone disconnected, by design
 *     <li> this Thread deletes a Lobby from the static List of Lobbies, and sets its deleted field to true
 *     <li> this Thread wakes up all the waiting ClientHandlers. Doing so causes them to check if the Lobby has been deleted. They proceed to terminate their execution afterwards.
 */
public class AllDisconnectedThread implements Runnable {
    /**
     * the reference to the Lobby that will be deleted
     */
    private final Lobby lobby;

    /**
     * Constructor of the allDisconnectedThread.
     *
     * @param lobby The reference to the Lobby to destroy.
     */
    public AllDisconnectedThread(Lobby lobby) {
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