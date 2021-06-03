package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.requirements.ReqValue;
import it.polimi.ingsw.server.model.requirements.CardRequirements;
import it.polimi.ingsw.server.model.requirements.ResourceRequirements;
import it.polimi.ingsw.server.model.specialAbilities.DiscountResource;
import it.polimi.ingsw.server.model.specialAbilities.ExtraDepot;
import it.polimi.ingsw.server.model.specialAbilities.MarketResources;
import it.polimi.ingsw.server.model.specialAbilities.Production;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LeaderCardsDeck {
    private final List<LeaderCard> deck;
    private int index;

    /**
     * Constructor of the LeaderCardsDeck.
     * All the 16 LeaderCards are initialized here. The deck is also shuffled at the end of the constructor.
     */
    public LeaderCardsDeck() {
        index = 0;
        deck = new ArrayList<>();

        deck.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(1, -1), Color.GREEN, new ReqValue(1, -1))),
                new DiscountResource(Resource.SERVANT)
                , "resources/cardsFront/LFRONT (1).png", "resources/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.BLUE, new ReqValue(1, -1), Color.PURPLE, new ReqValue(1, -1))),
                new DiscountResource(Resource.SHIELD)
                , "resources/cardsFront/LFRONT (2).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.GREEN, new ReqValue(1, -1), Color.BLUE, new ReqValue(1, -1))),
                new DiscountResource(Resource.STONE)
                , "resources/cardsFront/LFRONT (3).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(1, -1), Color.PURPLE, new ReqValue(1, -1))),
                new DiscountResource(Resource.COIN)
                , "resources/cardsFront/LFRONT (4).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.COIN, 5)),
                new ExtraDepot(Resource.STONE)
                , "resources/cardsFront/LFRONT (5).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SERVANT, 5)),
                new ExtraDepot(Resource.SHIELD)
                , "resources/cardsFront/LFRONT (6).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.STONE, 5)),
                new ExtraDepot(Resource.SERVANT)
                , "resources/cardsFront/LFRONT (7).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.COIN)
                , "resources/cardsFront/LFRONT (8).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(2, -1), Color.BLUE, new ReqValue(1, -1))),
                new MarketResources(Resource.SERVANT)
                , "resources/cardsFront/LFRONT (9).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.GREEN, new ReqValue(2, -1), Color.PURPLE, new ReqValue(1, -1))),
                new MarketResources(Resource.SHIELD)
                , "resources/cardsFront/LFRONT (10).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.BLUE, new ReqValue(2, -1), Color.YELLOW, new ReqValue(1, -1))),
                new MarketResources(Resource.STONE)
                , "resources/cardsFront/LFRONT (11).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.PURPLE, new ReqValue(2, -1), Color.GREEN, new ReqValue(1, -1))),
                new MarketResources(Resource.COIN)
                , "resources/cardsFront/LFRONT (12).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(4,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(1, 2))),
                new Production(Resource.SHIELD)
                , "resources/cardsFront/LFRONT (13).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(4,
                new CardRequirements(Map.of(Color.BLUE, new ReqValue(1, 2))),
                new Production(Resource.SERVANT)
                , "resources/cardsFront/LFRONT (14).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(4,
                new CardRequirements(Map.of(Color.PURPLE, new ReqValue(1, 2))),
                new Production(Resource.STONE)
                , "resources/cardsFront/LFRONT (15).png", "resource/cardsBack/BACK (1).png"));

        deck.add(new LeaderCard(4,
                new CardRequirements(Map.of(Color.GREEN, new ReqValue(1, 2))),
                new Production(Resource.COIN)
                , "resources/cardsFront/LFRONT (16).png", "resource/cardsBack/BACK (1).png"));

        shuffle();
    }

    /**
     * Shuffles all the LeaderCards.
     */
    public void shuffle() {
        Collections.shuffle(deck);
        index = 0;
    }

    public List<LeaderCard> getCards() {
        return new ArrayList<>(deck);
    }

    /**
     * Returns the next 4 cards available. Becomes cyclical if called more than 4 times without shuffling.
     * @return The next 4 cards available. Becomes cyclical if called more than 4 times without shuffling.
     */
    public synchronized List<LeaderCard> pickFourCards() {
        List<LeaderCard> cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (index == 16) index = 0;
            cards.add(deck.get(index));
            index++;
        }
        return cards;
    }
}