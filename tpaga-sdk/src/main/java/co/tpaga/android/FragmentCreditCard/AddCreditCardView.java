package co.tpaga.android.FragmentCreditCard;

public interface AddCreditCardView {

    interface UserActionsListener {
        void onResponseSuccessTokenizeCreditCard(String creditCardToken);

        void showError(Throwable t);
    }

    interface View {

        boolean validateFieldsCC();

        void showValidateFieldsError();

        void clearFields();
    }


}
