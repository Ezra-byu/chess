package serverFacade;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameRequest;
import model.UserData;
import model.responses.ListGameResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public AuthData register(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public AuthData login(UserData user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public GameData[] listGames(String authToken) throws ResponseException {
        var path = "/game";
        Collection<GameData> gameList;
        var response = this.makeRequest("GET", path, null, ListGameResponse.class, authToken);
        return response.getGames().toArray(new GameData[0]);

    }
    public GameData createGame(GameData game, String authToken) throws ResponseException{
        var path = "/game";
        return this.makeRequest("POST", path, game, GameData.class, authToken);
    }

    public void joinGame(JoinGameRequest gameRequest, String authToken) throws ResponseException{
        var path = "/game";
        this.makeRequest("PUT", path, gameRequest, GameData.class, authToken);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setRequestProperty("authorization", authToken);

            if(method.equals("GET")){
                http.setDoOutput(false);
            }
            else{http.setDoOutput(true); }
            writeBody(request, http);//serialization and sends
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
