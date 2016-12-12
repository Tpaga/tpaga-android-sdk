package co.tpaga.android.Tools;

import android.support.annotation.StringRes;

import co.tpaga.android.Entities.CreditCardResponse;

public class StatusResponse {

    public int responseCode;

    public String responseMessage;


    public static StatusResponse create(CreditCardResponse.Error error) {
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.responseCode = 1;
        statusResponse.responseMessage = error.errors.get(0).message;
        return statusResponse;
    }

    public static StatusResponse create(@StringRes int s) {
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.responseCode = 1;
        statusResponse.responseMessage = "";
        return statusResponse;
    }

    public static StatusResponse create(int code, String s) {
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.responseCode = code;
        statusResponse.responseMessage = s;
        return statusResponse;
    }
}
