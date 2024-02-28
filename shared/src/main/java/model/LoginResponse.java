package model;

public class LoginResponse extends BaseResponse{
    //"authToken": "example_auth",
    //"username": "example_username"
    String username;
    String authToken;
    public LoginResponse(int statuscode, String username, String authToken) {
        super(statuscode);
        this.authToken = authToken;
        this.username = username;
    }
}
