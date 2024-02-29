package model.responses;


import model.responses.BaseResponse;

public class ErrorResponse extends BaseResponse {
    String message;


    public ErrorResponse(int statuscode, String message) {
        super(statuscode);
        this.message = message;
    }
}
