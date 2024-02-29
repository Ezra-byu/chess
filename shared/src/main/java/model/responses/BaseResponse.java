package model.responses;

public abstract class BaseResponse {
    int statusCode;

    public BaseResponse(int statuscode) {
        this.statusCode = statuscode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
