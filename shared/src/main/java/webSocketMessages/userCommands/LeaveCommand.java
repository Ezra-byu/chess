package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand{
    Integer gameID;
    public LeaveCommand(String authToken) {
        super(authToken);
    }
    public Integer getGameID(){
        return gameID;
    }

}
