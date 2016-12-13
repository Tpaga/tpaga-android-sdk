package co.tpaga.android.FragmentCreditCard;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import co.tpaga.android.Entities.CreditCard;
import co.tpaga.android.Network.MockService;
import co.tpaga.android.Tools.TpagaException;
import co.tpaga.android.Tpaga;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddCreditCardPresenterTest {

    private MockService api;

    @Mock
    private AddCreditCardView.UserActionsListener userActionsListener;
    @Mock
    private AddCreditCardView.View view;

    private AddCreditCardPresenter creditCardPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        api = new MockService("", Tpaga.SANDBOX);

        creditCardPresenter = new AddCreditCardPresenter(userActionsListener, api);
        creditCardPresenter.addCreditCardView(view);
    }

    @Test
    public void onClickAddCC_validationSuccess() {
        when(view.validateFieldsCC()).thenReturn(true);

        creditCardPresenter.onClickAddCC(CreditCard.create("4242424242424242", "2020", "11", "123", "nombre"));

        verify(view, Mockito.times(1)).validateFieldsCC();
        verify(view, Mockito.times(0)).showValidateFieldsError();
    }

    @Test
    public void onClickAddCC_validationFailure() {
        when(view.validateFieldsCC()).thenReturn(false);

        creditCardPresenter.onClickAddCC(CreditCard.create("4242424242424242", "2020", "11", "123", "nombre"));

        verify(view, Mockito.times(1)).validateFieldsCC();
        verify(view, Mockito.times(1)).showValidateFieldsError();
    }

    @Test
    public void tokenizeCreditCard_onResponseSuccessfulOfAddCreditCard() throws InterruptedException, IOException {
        api.setCodeResponse(200);

        creditCardPresenter.tokenizeCreditCard(CreditCard.create("4242424242424242", "2020", "11", "123", "nombre"));

        Thread.sleep(10000);

        verify(userActionsListener, Mockito.times(1)).onResponseSuccessTokenizeCreditCard(any(String.class));
    }

    @Test
    public void tokenizeCreditCard_showError() throws InterruptedException, IOException {
        api.setCodeResponse(500);

        creditCardPresenter.tokenizeCreditCard(CreditCard.create("4242424242424242", "2020", "11", "123", "nombre"));

        Thread.sleep(10000);

        verify(userActionsListener, Mockito.times(1)).showError(any(TpagaException.class));
    }

}