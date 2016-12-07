package co.tpaga.tpagasdk.Entities;

/**
 * Created by TPaga on 8/30/16.
 */
public class CreditCardTpaga {

    public String primaryAccountNumber;

    public String expirationMonths;

    public String expirationYear;

    public String cvc;

    public String cardHolderName;

    public static CreditCardTpaga create(String number, String year, String month, String cvv, String name) {
        CreditCardTpaga creditCardTpaga = new CreditCardTpaga();
        creditCardTpaga.primaryAccountNumber = number.replaceAll("\\s+", "");
        creditCardTpaga.expirationMonths = month;
        creditCardTpaga.expirationYear = year;
        creditCardTpaga.cvc = cvv;
        creditCardTpaga.cardHolderName = name;
        return creditCardTpaga;
    }
}
