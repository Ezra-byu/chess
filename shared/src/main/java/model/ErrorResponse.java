package model;


public class ErrorResponse extends BaseResponse{
    String errormessage;


    public ErrorResponse(int statuscode, String message) {
        super(statuscode);
        this.errormessage = message;
    }
}
