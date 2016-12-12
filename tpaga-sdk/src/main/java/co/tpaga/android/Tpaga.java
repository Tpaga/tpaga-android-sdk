package co.tpaga.android;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import co.tpaga.android.Entities.CreditCard;
import co.tpaga.android.FragmentCreditCard.AddCreditCardPresenter;
import co.tpaga.android.FragmentCreditCard.AddCreditCardView;
import co.tpaga.android.Network.TpagaAPI;
import co.tpaga.android.Tools.GenericResponse;
import co.tpaga.android.Tools.TpagaTools;
import io.card.payment.CardIOActivity;
import okhttp3.ResponseBody;

import static com.google.common.base.Preconditions.checkNotNull;

public class Tpaga {

    /*to select environment*/
    public static final int SANDBOX = 1000;
    public static final int PRODUCTION = 2000;

    /*request code for scan card*/
    public static final int SCAN_CREDIT_CARD = 1126;

    @NonNull
    private static TpagaAPI tpagaApi;

    public static void initialize(@NonNull String tpagaPublicApiKey, @NonNull int environment) {
        checkNotNull(tpagaPublicApiKey, "Tpaga public_api_key cannot be null");
        checkNotNull(environment, "Tpaga environment cannot be null");
        tpagaApi = new TpagaAPI(tpagaPublicApiKey, environment);
    }

    public static void startScanCreditCard(Activity activity) {
        Intent scanIntent = new Intent(activity, CardIOActivity.class);
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true);
        activity.startActivityForResult(scanIntent, SCAN_CREDIT_CARD);
    }

    public static void startScanCreditCard(Fragment fragment) {
        Intent scanIntent = new Intent(fragment.getActivity(), CardIOActivity.class);
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true);
        fragment.startActivityForResult(scanIntent, SCAN_CREDIT_CARD);
    }

    public static boolean validateCreditCardData(CreditCard creditCard) {
        return TpagaTools.isValidCardNumber(creditCard.primaryAccountNumber.replaceAll("\\s+", ""))
                && TpagaTools.isValidExpirationDate(creditCard.expirationYear, creditCard.expirationMonth)
                && !creditCard.cvc.isEmpty()
                && TpagaTools.isNameValid(creditCard.cardHolderName);
    }

    public static CreditCard onActivityResultScanCreditCard(Intent data) {
        try {
            if (data != null || data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                io.card.payment.CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                CreditCard creditCard = CreditCard.create(
                        scanResult.getFormattedCardNumber(),
                        (scanResult.isExpiryValid()) ? scanResult.expiryYear + "" : "",
                        (scanResult.isExpiryValid()) ? scanResult.expiryMonth + "" : "",
                        (scanResult.cvv != null) ? scanResult.cvv : "",
                        "");

                return creditCard;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void tokenizeCreditCard(AddCreditCardView.UserActionsListener userActionsListener, CreditCard creditCard) {
        checkNotNull(tpagaApi, "Tpaga SDK has not been properly initialized. Please, read the documentation in https://bitbucket.org/tpaga/tpaga-android-sdk/overview");
        AddCreditCardPresenter mAddCreditCardPresenter = new AddCreditCardPresenter(userActionsListener, tpagaApi);
        mAddCreditCardPresenter.tokenizeCreditCard(creditCard);
    }

    public static void validateAndTokenizeCreditCard(AddCreditCardView.UserActionsListener userActionsListener, AddCreditCardView.View view, CreditCard creditCard) {
        checkNotNull(tpagaApi, "Tpaga SDK has not been properly initialized. Please, read the documentation in https://bitbucket.org/tpaga/tpaga-android-sdk/overview");
        AddCreditCardPresenter mAddCreditCardPresenter = new AddCreditCardPresenter(userActionsListener, tpagaApi);
        mAddCreditCardPresenter.addCreditCardView(view);
        mAddCreditCardPresenter.onClickAddCC(creditCard);
    }

    public static GenericResponse errorResponse(ResponseBody responseBody) {
        return tpagaApi.errorResponse(responseBody);
    }
}
