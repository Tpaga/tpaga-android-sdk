package co.tpaga.tpagasdk.Entities;

public class CreditCardWallet {

    public String tempCcToken;

    public CreditCardWallet(String token) {
        tempCcToken = token;
    }

    public static CreditCardWallet create(String token) {
        return new CreditCardWallet(token);
    }
}
