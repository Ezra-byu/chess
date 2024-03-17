import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameRequest;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public AuthData addUser(UserData user) {
        try {
            var path = "/user";
            return this.makeRequest("POST", path, user, AuthData.class);
        } catch (Exception e) {
            System.out.println("500" + "Something went wrong." + e);
        }
        return null;
    }

    public AuthData login(UserData user) {
        try {
            var path = "/session";
            return this.makeRequest("POST", path, user, AuthData.class);
        } catch (Exception e) {
            System.out.println("500" + "Something went wrong." + e);
        }
        return null;
    }

    public void logout(UserData user) {
        try {
            var path = "/user";
            this.makeRequest("DELETE", path, user, null);
        } catch (Exception e) {
            System.out.println("500" + "Something went wrong." + e);
        }
    }

    public GameData[] listGames() {
        try {
            var path = "/game";
            record listPetResponse(GameData[] gameRecord) {
            }
            var response = this.makeRequest("GET", path, null, listPetResponse.class);
            return response.gameRecord();
        } catch (Exception e) {
            System.out.println("500" + "Something went wrong." + e);
        }
        return null;
    }
    public GameData createGame(GameData game) {
        try {
            var path = "/game";
            return this.makeRequest("POST", path, game, GameData.class);
        } catch (Exception e) {
            System.out.println("500" + "Something went wrong." + e);
        }
        return null;
    }

    public void joinGame(JoinGameRequest gameRequest) {
        try {
            var path = "/game";
            this.makeRequest("PUT", path, gameRequest, GameData.class);
        } catch (Exception e) {
            System.out.println("500" + "Something went wrong." + e);
        }
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

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
