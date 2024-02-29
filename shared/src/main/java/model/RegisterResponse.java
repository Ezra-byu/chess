package model;

import com.google.gson.Gson;

public class RegisterResponse extends BaseResponse {
    String username;
    String authToken;

    public RegisterResponse(int statuscode, String username, String authToken) {
        super(statuscode);
        this.username = username;
        this.authToken = authToken;
    }
    public String getAuthToken(){
        return authToken;
    }
    public String toString() {
        return new Gson().toJson(this);
    }
}
