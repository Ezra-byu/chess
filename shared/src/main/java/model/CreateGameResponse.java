package model;

public class CreateGameResponse extends BaseResponse{
    int gameID;
    public CreateGameResponse(int statuscode, int gameID) {
        super(statuscode);
        this.gameID = gameID;
    }
}
