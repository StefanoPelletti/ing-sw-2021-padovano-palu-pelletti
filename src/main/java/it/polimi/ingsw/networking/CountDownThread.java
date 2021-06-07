package it.polimi.ingsw.networking;

public class CountDownThread implements Runnable {
    private final Lobby lobby;
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