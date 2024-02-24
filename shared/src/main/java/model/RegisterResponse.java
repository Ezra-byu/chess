package model;

import com.google.gson.Gson;

public record RegisterResponse(int responsecode, String username, AuthData auth) {
    public String toString() {
        return new Gson().toJson(this);
    }
}
