package co.tpaga.android.Entities;

import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CreditCardTest {

    @Test
    public void testCreditCard() {
        CreditCard creditCard = Mockito.mock(CreditCard.class);
        when(creditCard.primaryAccountNumber).thenReturn("4242424242424242");
        assertEquals(creditCard.primaryAccountNumber, "4242424242424242");
    }

    @Test
    public void testNewCreditCard() {
        CreditCard creditCard = CreditCard.create("number", "year", "month", "cvv", "name");
        assertEquals(creditCard.primaryAccountNumber, "number");
        assertEquals(creditCard.expirationYear, "year");
        assertEquals(creditCard.expirationMonth, "month");
        assertEquals(creditCard.cvc, "cvv");
        assertEquals(creditCard.cardHolderName, "name");
    }

}