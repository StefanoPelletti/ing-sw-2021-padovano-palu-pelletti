package it.polimi.ingsw.Networking;

public class CountDownThread implements Runnable {
    private final Lobby lobby;
    private final int time;

    public CountDownThread(Lobby lobby, int time)
    {
        this.lobby = lobby;
        this.time = time;
    }

    public void run()
    {
        try
        {
            System.out.println("[CountDown "+Thread.currentThread().getName()+"] : "+time+" seconds left");
            Thread.sleep(1000L * time);

            synchronized (lobby) {
                if (lobby.isStarted()) {
                    System.out.println("[CountDown "+Thread.currentThread().getName()+"] : lobby "+lobby.getLobbyNumber()+" NOT deleted for timeout");
                }
                else {
                    lobby.setDeleted(true);
                    Lobby.removeLobby(lobby);
                    lobby.notifyAll();
                    System.out.println("[CountDown "+Thread.currentThread().getName()+"] : lobby "+lobby.getLobbyNumber()+" deleted for timeout");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
