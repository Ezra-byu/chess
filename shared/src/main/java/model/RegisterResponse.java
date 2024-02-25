package model;

import com.google.gson.Gson;

public class RegisterResponse extends BaseResponse {
    String username;
    AuthData auth;

    public RegisterResponse(int statuscode, String username, AuthData auth) {
        super(statuscode);
        this.username = username;
        this.auth = auth;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
