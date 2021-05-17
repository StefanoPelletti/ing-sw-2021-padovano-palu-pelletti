package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.Requirements.CardRequirements;
import it.polimi.ingsw.Server.Model.Requirements.ResourceRequirements;
import it.polimi.ingsw.Server.Model.SpecialAbilities.DiscountResource;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;
import it.polimi.ingsw.Server.Model.SpecialAbilities.MarketResources;
import it.polimi.ingsw.Server.Model.SpecialAbilities.Production;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class LeaderCardsDeck {
    private ArrayList<LeaderCard> deck;
    int t_pos;

    public LeaderCardsDeck() {
        t_pos = 0;
        deck = new ArrayList<>();

        deck.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.YELLOW, new Integer[]{1, -1}, Color.GREEN, new Integer[]{1, -1})),
                new DiscountResource(Resource.SERVANT)));

        deck.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.BLUE, new Integer[]{1, -1}, Color.PURPLE, new Integer[]{1, -1})),
                new DiscountResource(Resource.SHIELD)));

        deck.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.GREEN, new Integer[]{1, -1}, Color.BLUE, new Integer[]{1, -1})),
                new DiscountResource(Resource.STONE)));

        deck.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.YELLOW, new Integer[]{1, -1}, Color.PURPLE, new Integer[]{1, -1})),
                new DiscountResource(Resource.COIN))
        );

        deck.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.COIN, 5)),
                new ExtraDepot(Resource.STONE))
        );

        deck.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SERVANT, 5)),
                new ExtraDepot(Resource.SHIELD))
        );

        deck.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.STONE, 5)),
                new ExtraDepot(Resource.SERVANT))
        );

        deck.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.COIN))
        );

        deck.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.YELLOW, new Integer[]{2, -1}, Color.BLUE, new Integer[]{1, -1})),
                new MarketResources(Resource.SERVANT))
        );

        deck.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.GREEN, new Integer[]{2, -1}, Color.PURPLE, new Integer[]{1, -1})),
                new MarketResources(Resource.SHIELD))
        );

        deck.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.BLUE, new Integer[]{2, -1}, Color.YELLOW, new Integer[]{1, -1})),
                new MarketResources(Resource.STONE))
        );

        deck.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.PURPLE, new Integer[]{2, -1}, Color.GREEN, new Integer[]{1, -1})),
                new MarketResources(Resource.COIN))
        );

        deck.add(new LeaderCard(4,
                new CardRequirements(Map.of(Color.YELLOW, new Integer[]{1, 2})),
                new Production(Resource.SHIELD))
        );

        deck.add(new LeaderCard(4,
                new CardRequirements(Map.of(Color.BLUE, new Integer[]{1, 2})),
                new Production(Resource.SERVANT))
        );

        deck.add(new LeaderCard(4,
                new CardRequirements(Map.of(Color.PURPLE, new Integer[]{1, 2})),
                new Production(Resource.STONE))
        );

        deck.add(new LeaderCard(4,
                new CardRequirements(Map.of(Color.GREEN, new Integer[]{1, 2})),
                new Production(Resource.COIN))
        );

        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(deck);
        t_pos = 0;
    }

    public ArrayList<LeaderCard> getCards() {
        return new ArrayList<>(deck);
    }

    // pickFourCards extracts the next 4 cards from a fixed stack.
// becomes cyclical if called more than 4 times without shuffling
    public synchronized ArrayList<LeaderCard> pickFourCards() {
        ArrayList<LeaderCard> cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (t_pos == 16) t_pos = 0;
            cards.add(deck.get(t_pos));
            t_pos++;
        }
        return cards;
    }
}