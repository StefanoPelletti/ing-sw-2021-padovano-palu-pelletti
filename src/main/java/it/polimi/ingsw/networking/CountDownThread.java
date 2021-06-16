package it.polimi.ingsw.networking;

/**
 * The CountDownThread class implements a Thread that will perform a check of existence after a specified amount of time.
 * <p>
 * This Thread is created just after a Lobby creation.
 * After a set amount of time, it checks if the started field of the specified Lobby is True. If so, the Thread kills itself.
 * If the Lobby is not started yet, proceeds to destroy it, by removing the reference from the static List of Lobbies,
 * setting the deleted field to true, and waking up all the idling Clienthandlers.
 */
public class CountDownThread implements Runnable {
    /**
     * the reference to the Lobby
     */
    private final Lobby lobby;
    /**
     * the time, in seconds, after the existence check is performed
     */
    private final int time;

    /**
     * Construct a object designed to destroy a specified Lobby after a specified amount of time, if such Lobby has not started yet.
     *
     * @param lobby The reference to the Lobby that will be checked.
     * @param time  The desired amount of time in seconds.
     */
    public CountDownThread(Lobby lobby, int time) {
        this.lobby = lobby;
        this.time = time;
    }

    public void run() {
        try {
            System.out.println("[CountDown " + Thread.currentThread().getName() + "] : " + time + " seconds left");
            Thread.sleep(1000L * time);

            synchronized (lobby) {
                if (lobby.isStarted()) {
                    System.out.println("[CountDown " + Thread.currentThread().getName() + "] : lobby " + lobby.getLobbyNumber() + " NOT deleted for timeout");
                } else {
                    lobby.setDeleted(true);
                    Lobby.removeLobby(lobby);
                    lobby.notifyAll();
                    System.out.println("[CountDown " + Thread.currentThread().getName() + "] : lobby " + lobby.getLobbyNumber() + " deleted for timeout");
                }
            }
        } catch (InterruptedException e) {
            System.out.println("[CountDown " + Thread.currentThread().getName() + "] : thread interrupted!");
            Thread.currentThread().interrupt();
        }
    }
}