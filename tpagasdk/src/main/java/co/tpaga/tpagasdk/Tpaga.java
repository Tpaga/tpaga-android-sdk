package co.tpaga.tpagasdk;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import co.tpaga.tpagasdk.Entities.CreditCardTpaga;
import co.tpaga.tpagasdk.FragmentCreditCard.AddCreditCardPresenter;
import co.tpaga.tpagasdk.FragmentCreditCard.AddCreditCardView;
import co.tpaga.tpagasdk.Network.TpagaAPI;
import co.tpaga.tpagasdk.Tools.GenericResponse;
import co.tpaga.tpagasdk.Tools.TpagaTools;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import okhttp3.ResponseBody;

import static com.google.common.base.Preconditions.checkNotNull;

public class Tpaga {

    /*request code for scan card*/
    public static final int SCAN_CREDIT_CARD = 1126;

    @NonNull
    private static TpagaAPI tpagaApi;

    public static void initialize(@NonNull String tpagaPublicApiKey, @NonNull int environment) {
        checkNotNull(tpagaPublicApiKey, "Tpaga public_api_key cannot be null");
        checkNotNull(environment, "Tpaga environment cannot be null");
        tpagaApi = new TpagaAPI(tpagaPublicApiKey, environment);
    }

    public static void startScanCreditCard(Activity context) {
        Intent scanIntent = new Intent(context, CardIOActivity.class);
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true);
        context.startActivityForResult(scanIntent, SCAN_CREDIT_CARD);
    }

    public static boolean validateCreditCardData(CreditCardTpaga creditCard) {
        return TpagaTools.isValidCardNumber(creditCard.primaryAccountNumber.replaceAll("\\s+", ""))
                && TpagaTools.isValidExpirationDate(creditCard.expirationYear, creditCard.expirationMonths)
                && !creditCard.cvc.isEmpty()
                && TpagaTools.isNameValid(creditCard.cardHolderName);
    }

    public static CreditCardTpaga onActivityResultScanCreditCard(Intent data) {
        try {
            if (data != null || data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                CreditCardTpaga creditCardTpaga = CreditCardTpaga.create(
                        scanResult.getFormattedCardNumber(),
                        (scanResult.isExpiryValid()) ? scanResult.expiryYear + "" : "",
                        (scanResult.isExpiryValid()) ? scanResult.expiryMonth + "" : "",
                        (scanResult.cvv != null) ? scanResult.cvv : "",
                        "");

                return creditCardTpaga;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void tokenizeCreditCard(AddCreditCardView.UserActionsListener userActionsListener) {
        checkNotNull(tpagaApi, "Tpaga SDK has not been properly initialized. Please, read the documentation in https://bitbucket.org/tpaga/tpaga-android-sdk/overview");
        AddCreditCardPresenter mAddCreditCardPresenter = new AddCreditCardPresenter(userActionsListener, tpagaApi);
        mAddCreditCardPresenter.tokenizeCreditCard();
    }

    public static void validateAndTokenizeCreditCard(AddCreditCardView.UserActionsListener userActionsListener, AddCreditCardView.View view) {
        checkNotNull(tpagaApi, "Tpaga SDK has not been properly initialized. Please, read the documentation in https://bitbucket.org/tpaga/tpaga-android-sdk/overview");
        AddCreditCardPresenter mAddCreditCardPresenter = new AddCreditCardPresenter(userActionsListener, tpagaApi);
        mAddCreditCardPresenter.addCreditCardView(view);
        mAddCreditCardPresenter.onClickAddCC();
    }

    public static GenericResponse errorResponse(ResponseBody responseBody) {
        return tpagaApi.errorResponse(responseBody);
    }
}
