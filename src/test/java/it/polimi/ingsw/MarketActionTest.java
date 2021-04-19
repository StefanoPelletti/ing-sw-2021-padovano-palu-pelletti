package it.polimi.ingsw;

import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.Middles.MarketHelper;
import it.polimi.ingsw.Server.Model.Requirements.CardRequirements;
import it.polimi.ingsw.Server.Model.Requirements.ResourceRequirements;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;
import it.polimi.ingsw.Server.Model.SpecialAbilities.MarketResources;
import org.junit.jupiter.api.*;

import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Controller.*;
import it.polimi.ingsw.Networking.Message.*;

import java.util.*;

public class MarketActionTest {
    @Test
    public void test(){
        Scanner myInput = new Scanner(System.in);
        GameManager gm = new GameManager(4);
        Game g = gm.getGame();
        FaithTrackManager ftm = gm.getFaithTrackManager();
        ActionManager am = gm.getActionManager();
        MarketHelper marketHelper = g.getMarketHelper();


        g.addPlayer("pino", 1);
        Player pino = g.getPlayer("pino");

        //if you want to try things, you can change the things of the player Pino here
        LeaderCard l1 = new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.BLUE, new Integer[] {2,-1}); put(Color.YELLOW, new Integer[] {1,-1}); }}),
                new MarketResources(Resource.STONE));
        LeaderCard l2 = new LeaderCard(3,
                new ResourceRequirements(new HashMap<Resource,Integer>() {{put(Resource.COIN, 5); }}),
                new ExtraDepot(Resource.STONE));
        LeaderCard l3 = new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.GREEN, new Integer[] {2,-1}); put(Color.PURPLE, new Integer[] {1,-1}); }}),
                new MarketResources(Resource.SHIELD));
        LeaderCard l4 = new LeaderCard(3,
                new ResourceRequirements(new HashMap<Resource,Integer>() {{put(Resource.SHIELD, 5); }}),
                new ExtraDepot(Resource.COIN));
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(l1);
        cards.add(l3);
        pino.associateLeaderCards(cards);
        WarehouseDepot d = pino.getWarehouseDepot();
        pino.getLeaderCards()[0].setEnable(true);
        pino.getLeaderCards()[1].setEnable(true);
        d.add(Resource.COIN);
        d.swapRow(1,2);
        d.add(Resource.COIN);

        System.out.println("INITIAL SITUATION:");
        System.out.println("\nYOUR DEPOT:\n"+d+"\n");
        for(LeaderCard c: pino.getCardsWithExtraDepotAbility()){
            ExtraDepot extraDepot =(ExtraDepot) c.getSpecialAbility();
            System.out.println("EXTRADEPOT: "+extraDepot.getResourceType()+", "+extraDepot.getNumber()+"\n");
        }
        System.out.println("\nHere's the market!\n"+g.getMarket());
        System.out.println("Do you want a row or a column? ");
        boolean column;
        boolean done = false;
        int y = 0;
        while (!done) {
            System.out.println("1: row");
            System.out.println("2: column");
            y = myInput.nextInt();
            if (y == 1 || y == 2) done = true;
            else System.out.println("Cosinovvabene");
        }
        if(y==1) column=false;
        else column=true;

        System.out.println("\nQuale?");
        done = false;
        y = 0;
        while (!done) {
            y = myInput.nextInt();
            if(column && (y>=1&&y<=4)) done =true;
            else if(!column && (y>=1&&y<=3)) done =true;
            else System.out.println("\nCosinovvabene");
        }

        MSG_ACTION_GET_MARKET_RESOURCES m = new MSG_ACTION_GET_MARKET_RESOURCES(column, y-1);
        am.getMarketResources(pino,m);
        MSG_ACTION_MARKET_CHOICE m2;


        while(marketHelper.getResources().size()!=0){
            System.out.println("\nYOUR DEPOT:\n"+d+"\n");
            for(LeaderCard c: pino.getCardsWithExtraDepotAbility()){
                ExtraDepot extraDepot =(ExtraDepot) c.getSpecialAbility();
                System.out.println("EXTRADEPOT: "+extraDepot.getResourceType()+", "+extraDepot.getNumber()+"\n");
            }

            System.out.println("Devi ancora mettere ste risorse: ");
            marketHelper.getResources().stream().forEach(System.out::println);
            Resource r = marketHelper.getCurrentResource();

            boolean[] choices = marketHelper.getChoices();
            done = false;
            int x = 0;
            if(r!=Resource.EXTRA) {
                while (!done) {
                    System.out.println("Current resource is: "+r+".\nWhat do you want to do?\n");
                    if (choices[0]) System.out.println("1: METTI NEL DEPOSITO");
                    if (choices[1]) System.out.println("2: PUT IN EXTRA DEPOSITO");
                    if (choices[2]) System.out.println("3: DISCARD RESOURCE");
                    if (choices[3]) System.out.println("4: SWAP RIGA 1-2 DEPOT");
                    if (choices[4]) System.out.println("5: SWAP RIGA 1-3 DEPOT");
                    if (choices[5]) System.out.println("6: SWAP RIGA 2-3 DEPOT");
                    if (choices[6]) System.out.println("7: SKIPPA THIS RISOURSA AVANTI");
                    if (choices[7]) System.out.println("8: SKIPPA THIS RISOURSA INDIETRO");
                    x = myInput.nextInt();
                    if ((x == 1 && choices[0]) || (x == 2 && choices[1]) || (x == 3 && choices[2]) || (x == 4 && choices[3]) || (x == 5 && choices[4]) || (x == 6 && choices[5]) || (x == 7) && choices[6] ||(x==8 && choices[7]))
                        done = true;
                    else System.out.println("\nCosinovvabene");
                }

                for (int i = 0; i < 8; i++) {
                    if (i == x-1) choices[i] = true;
                    else choices[i] = false;
                }

                m2 = new MSG_ACTION_MARKET_CHOICE(choices, true, null);
            }
            else{
                boolean redone = false;
                y = 0;
                Resource[] extraResources = marketHelper.getExtraResources();
                Resource extraChoice;
                while (!redone) {
                    System.out.println("Current resource is: "+r);
                    System.out.println("è una risorsa extra! Cosa vuoi fare?");
                    System.out.println("1: cambia in " + extraResources[0]);
                    System.out.println("2: cambia in " + extraResources[1]);
                    if (choices[3]) System.out.println("3: SWAP RIGA 1-2 DEPOT");
                    if (choices[4]) System.out.println("4: SWAP RIGA 1-3 DEPOT");
                    if (choices[5]) System.out.println("5: SWAP RIGA 2-3 DEPOT");
                    if (choices[6]) System.out.println("6: SKIPPA THIS RISOURSA AVANTI");
                    if (choices[7]) System.out.println("7: SKIPPA THIS RISOURSA INDIETRO");
                    y = myInput.nextInt();
                    if (y>=1&&y<=7) redone = true;
                    else System.out.println("\nCosinovvabene");
                }
                if(y==1) {
                    extraChoice = extraResources[0];
                    m2 = new MSG_ACTION_MARKET_CHOICE(null, false, extraChoice);
                }
                else if (y==2){
                    extraChoice = extraResources[1];
                    m2 = new MSG_ACTION_MARKET_CHOICE(null, false, extraChoice);
                }

                else{
                    for (int i = 0; i < 8; i++) {
                        if (i == y) choices[i] = true;
                        else choices[i] = false;
                    }
                    m2 = new MSG_ACTION_MARKET_CHOICE(choices, true, null);
                }

            }
            am.newChoiceMarket(pino, m2);

        }
        System.out.println("Risorse finite! Alla prossima!\n");
        System.out.println("END SITUATION:");
        System.out.println("\nYOUR DEPOT:\n"+d+"\n");
        for(LeaderCard c: pino.getCardsWithExtraDepotAbility()){
            ExtraDepot extraDepot =(ExtraDepot) c.getSpecialAbility();
            System.out.println("EXTRADEPOT: "+extraDepot.getResourceType()+", "+extraDepot.getNumber()+"\n");
        }
    }
}
