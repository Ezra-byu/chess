package model;


public class ErrorResponse extends BaseResponse{
    String errormessage;


    public ErrorResponse(int statuscode, String errormessage) {
        super(statuscode);
        this.errormessage = errormessage;
    }
}
