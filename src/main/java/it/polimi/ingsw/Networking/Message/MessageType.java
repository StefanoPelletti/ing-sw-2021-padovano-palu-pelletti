package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public enum MessageType implements Serializable {
    MSG_CREATE_LOBBY,
    MSG_OK_CREATE,
    MSG_JOIN_LOBBY,
    MSG_OK_JOIN,

    MSG_ERROR,
    MSG_OK_GENERIC,

    MSG_INIT_CHOOSE_LEADERCARDS,
    MSG_INIT_CHOOSE_RESOURCE,
    MSG_INIT_LEADERCARDS_REQUEST,
    MSG_INIT_RESOURCE_REQUEST,

    MSG_INIT_START,

    MSG_GAME_YOUR_TURN,
    MSG_ACTION_GET_MARKET_RESOURCES,
    MSG_ACTION_MARKET_CHOICE,
    MSG_ACTION_MARKET_REQUEST,
    MSG_GAME_MARKET_RESOURCE_REQUEST,
    MSG_GAME_MARKET_RESOURCE_REPLY,
    MSG_ACTION_ACTIVATE_LEADERCARD,
    MSG_ACTION_DISCARD_LEADERCARD,
    MSG_ACTION_CHANGE_DEPOT_CONFIG,
    MSG_ACTION_BUY_DEVELOPMENT_CARD,
    MSG_ACTION_CHOOSE_DEVELOPMENT_CARD,
    MSG_GAME_BUY_DEVELOPMENT_CARD_REQUEST,
    MSG_GAME_BUY_DEVELOPMENT_CARD_REPLY,
    MSG_ACTION_ACTIVATE_PRODUCTION,
    MSG_ACTION_ENDTURN,
    MSG_GAME_END_TURN,

    MSG_UPD_Extradepot,
    MSG_UPD_DevSlot,
    MSG_UPD_LeaderCards,
    MSG_UPD_Strongbox,
    MSG_UPD_WarehouseDepot,
    MSG_UPD_DevDeck,
    MSG_UPD_FaithTrack,
    MSG_UPD_Game,
    MSG_UPD_Market,
    MSG_UPD_Player,
    MSG_UPD_End,

    MSG_UPD_DevCardsVendor,
    MSG_UPD_LeaderCardsObject,
    MSG_UPD_MarketHelper,
    MSG_UPD_ResourceObject,
    MSG_UPD_LeaderBoard,

    MSG_UPDATE_MODEL,
    MSG_NOTIFICATION,

    MSG_GAME_OVER,

}
