package co.tpaga.android.FragmentCreditCard;

import co.tpaga.android.Entities.CreditCard;

public interface AddCreditCardView {

    interface UserActionsListener {
        void onResponseSuccessTokenizeCreditCard(String creditCardToken);

        void showError(Throwable t);

        CreditCard getCreditCard();
    }

    interface View {

        boolean validateFieldsCC();

        void showValidateFieldsError();

        void clearFields();

        CreditCard getCreditCard();
    }


}
