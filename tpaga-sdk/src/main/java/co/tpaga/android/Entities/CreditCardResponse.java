package co.tpaga.android.Entities;

import java.util.ArrayList;

public class CreditCardResponse {

    private String token;

    boolean used;

    public class Error {
        public ArrayList<Error> errors;

        public String object;
        public String field;
        public String rejected_value;
        public String message;
    }

    public String getToken() {
        return token;
    }
}
