package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand{
    Integer gameID;
    public ResignCommand(String authToken) {
        super(authToken);
    }
    public Integer getGameID(){
        return gameID;
    }

}
