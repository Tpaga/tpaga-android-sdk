package co.tpaga.tpagasdk.FragmentCreditCard;

import co.tpaga.tpagasdk.Entities.CreditCardTpaga;
import co.tpaga.tpagasdk.Entities.CreditCardWallet;

public interface AddCreditCardView {

    interface UserActionsListener {
        void onResponseSuccessfulOfAddCreditCard(CreditCardWallet creditCardWallet);

        void showError(Throwable t);

        CreditCardTpaga getCreditCard();
    }

    interface View {

        boolean validateFieldsCC();

        void showToastMsg();

        void clear();

        CreditCardTpaga getCC();
    }


}
