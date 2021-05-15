package it.polimi.ingsw.Server.Model;

public class main {
    public static void main(String[] args) {
        DevelopmentCardsDeck d = new DevelopmentCardsDeck();
        DevelopmentSlot ds = new DevelopmentSlot();

        ds.addCard(d.removeCard(2,0), 0);

        ds.addCard(d.removeCard(2,1), 1);
        ds.addCard(d.removeCard(1,0), 1);

        ds.addCard(d.removeCard(2,2), 2);
        ds.addCard(d.removeCard(1,1), 2);
        ds.addCard(d.removeCard(0,0), 2);

        System.out.println(ds);
    }
}
