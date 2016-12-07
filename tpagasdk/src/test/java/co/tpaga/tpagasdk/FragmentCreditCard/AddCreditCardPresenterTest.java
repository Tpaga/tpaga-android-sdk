package co.tpaga.tpagasdk.FragmentCreditCard;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import co.tpaga.tpagasdk.Entities.CreditCardTpaga;
import co.tpaga.tpagasdk.Entities.CreditCardWallet;
import co.tpaga.tpagasdk.Tools.GenericResponse;
import co.tpaga.tpagasdk.Network.MockService;
import co.tpaga.tpagasdk.Network.TpagaAPI;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddCreditCardPresenterTest {

    private MockService api;

    @Mock
    private AddCreditCardView.UserActionsListener view;

    private AddCreditCardPresenter creditCardPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        api = new MockService("", TpagaAPI.SANDBOX);

        creditCardPresenter = new AddCreditCardPresenter(view, api);
    }

    @Test
    public void tokenizeCreditCard_onResponseSuccessfulOfAddCreditCard() throws InterruptedException, IOException {
        api.setCodeResponse(200);
        when(view.getCreditCard()).thenReturn(CreditCardTpaga.create("4242424242424242", "2020", "11", "123", "nombre"));

        creditCardPresenter.tokenizeCreditCard();

        Thread.sleep(10000);

        verify(view, Mockito.times(1)).getCreditCard();
        verify(view, Mockito.times(1)).onResponseSuccessfulOfAddCreditCard(any(CreditCardWallet.class));
    }

    @Test
    public void tokenizeCreditCard_showError() throws InterruptedException, IOException {
        api.setCodeResponse(500);
        when(view.getCreditCard()).thenReturn(CreditCardTpaga.create("4242424242424242", "2020", "11", "123", "nombre"));

        creditCardPresenter.tokenizeCreditCard();

        Thread.sleep(10000);

        verify(view, Mockito.times(1)).getCreditCard();
        verify(view, Mockito.times(1)).showError(any(GenericResponse.class));
    }

}