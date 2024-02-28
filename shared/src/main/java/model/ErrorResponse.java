package model;


public class ErrorResponse extends BaseResponse{
    String message;


    public ErrorResponse(int statuscode, String message) {
        super(statuscode);
        this.message = message;
    }
}
