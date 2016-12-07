package co.tpaga.tpagasdk.FragmentCreditCard;

import android.support.annotation.NonNull;

import co.tpaga.tpagasdk.Entities.CreditCardResponseTpaga;
import co.tpaga.tpagasdk.Network.TpagaAPI;
import co.tpaga.tpagasdk.Tools.TpagaException;
import co.tpaga.tpagasdk.Tpaga;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddCreditCardPresenter {

    @NonNull
    private AddCreditCardView.UserActionsListener userActionsListener;
    @NonNull
    private AddCreditCardView.View view;
    @NonNull
    private TpagaAPI tpagaApi;

    public AddCreditCardPresenter(@NonNull AddCreditCardView.UserActionsListener userActionsListener, @NonNull TpagaAPI tpagaApi) {
        this.userActionsListener = checkNotNull(userActionsListener, "UserActionListener cannot be null");
        this.tpagaApi = checkNotNull(tpagaApi, "TpagaApi cannot be null");
    }

    public void addCreditCardView(@NonNull AddCreditCardView.View view) {
        this.view = checkNotNull(view);
    }

    public void onClickAddCC() {
        if (!view.validateFieldsCC()) {
            view.showToastMsg();
            return;
        }
        tokenizeCreditCard();
    }

    public void tokenizeCreditCard() {
        tpagaApi.addCreditCard(checkNotNull(userActionsListener.getCreditCard(), "Credit card data cannot be null")).enqueue(new Callback<CreditCardResponseTpaga>() {
            @Override
            public void onResponse(Call<CreditCardResponseTpaga> call, Response<CreditCardResponseTpaga> response) {
                if (response.isSuccessful()) {
                    userActionsListener.onResponseSuccessfulOfAddCreditCard(response.body().toCreditCardWallet());
                    return;
                }
                if (response.code() >= 500) {
                    userActionsListener.showError(new TpagaException("Error conectando con el servidor"));
                } else {
                    userActionsListener.showError(new TpagaException(Tpaga.errorResponse(response.errorBody()).status.responseMessage, response.code()));
                }
            }

            @Override
            public void onFailure(Call<CreditCardResponseTpaga> call, Throwable t) {
                userActionsListener.showError(t);
            }
        });
    }
}
