package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{
    Integer gameID;
    public JoinObserverCommand(String authToken) {
        super(authToken);
    }
    public Integer getGameID(){
        return gameID;
    }
}
