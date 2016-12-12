package co.tpaga.android.Entities;

public class CreditCard {

    public String primaryAccountNumber;

    public String expirationMonth;

    public String expirationYear;

    public String cvc;

    public String cardHolderName;

    public static CreditCard create(String number, String year, String month, String cvv, String name) {
        CreditCard creditCard = new CreditCard();
        creditCard.primaryAccountNumber = number.replaceAll("\\s+", "");
        creditCard.expirationMonth = month;
        creditCard.expirationYear = year;
        creditCard.cvc = cvv;
        creditCard.cardHolderName = name;
        return creditCard;
    }
}
