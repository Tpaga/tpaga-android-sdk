package co.tpaga.tpagasdk.Entities;

import java.util.ArrayList;

/**
 * Created by TPaga on 8/30/16.
 */
public class CreditCardResponseTpaga {

    String token;

    boolean used;

    public class Error {
        public ArrayList<Error> errors;

        public String object;
        public String field;
        public String rejected_value;
        public String message;
    }

    public CreditCardWallet toCreditCardWallet() {
        return CreditCardWallet.create(token);
    }

}
