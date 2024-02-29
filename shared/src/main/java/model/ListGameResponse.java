package model;

import java.util.Collection;

public class ListGameResponse extends BaseResponse{

    Collection <GameData> games;

    public ListGameResponse(int statuscode, Collection games) {
        super(statuscode);
        this.games = games;
    }

    public Collection<GameData> getGames(){
        return games;
    }
}
