package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public enum MessageType implements Serializable {
    MSG_CREATE_LOBBY,
    MSG_OK_CREATE,
    MSG_JOIN_LOBBY,
    MSG_OK_JOIN,
    MSG_REJOIN_LOBBY,
    MSG_OK_REJOIN,

    MSG_ERROR,

    MSG_INIT_CHOOSE_LEADERCARDS,
    MSG_INIT_CHOOSE_RESOURCE,
    MSG_ACTION_GET_MARKET_RESOURCES,
    MSG_ACTION_MARKET_CHOICE,
    MSG_ACTION_ACTIVATE_LEADERCARD,
    MSG_ACTION_DISCARD_LEADERCARD,
    MSG_ACTION_CHANGE_DEPOT_CONFIG,
    MSG_ACTION_BUY_DEVELOPMENT_CARD,
    MSG_ACTION_CHOOSE_DEVELOPMENT_CARD,
    MSG_ACTION_ACTIVATE_PRODUCTION,
    MSG_ACTION_ENDTURN,

    MSG_UPD_Extradepot,
    MSG_UPD_DevSlot,
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

    MSG_UPD_Full,
    MSG_NOTIFICATION,

    Ping, MSG_Stop

}
