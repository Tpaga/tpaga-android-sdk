package co.tpaga.android.FragmentCreditCard;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import co.tpaga.android.Entities.CreditCard;
import co.tpaga.android.R;
import co.tpaga.android.Tools.ExpirationDatePickerDialog;
import co.tpaga.android.Tools.TpagaTools;
import co.tpaga.android.Tpaga;

public class AddCreditCardFragment extends Fragment implements AddCreditCardView.View, View.OnClickListener, TextWatcher, DatePickerDialog.OnDateSetListener {
    public static final String TAG = AddCreditCardFragment.class.getSimpleName();
    private static final String ccExpirationDateTag = "CC_EXPIRATION_DATE";

    private TextInputLayout ccNumberTil;
    private EditText ccNumber;

    private TextInputLayout expirationDateTil;
    private EditText expirationDate;

    private TextInputLayout cvvTil;
    private EditText cvv;

    private TextInputLayout ccHolderNameTil;
    private EditText ccHolderName;

    private ImageButton scanCreditCardButton;
    private Button saveCreditCardButton;

    private AddCreditCardView.UserActionsListener mUserActionListener;
    private ExpirationDatePickerDialog expirationDatePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_credit_card, container, false);
        initViewComponents(view);
        return view;
    }

    public void initViewComponents(View view) {
        ccNumberTil = (TextInputLayout) view.findViewById(R.id.et_cc_number_til);
        ccNumber = (EditText) view.findViewById(R.id.et_cc_number);
        ccNumber.addTextChangedListener(this);
        expirationDateTil = (TextInputLayout) view.findViewById(R.id.expiration_date_til);
        expirationDate = (EditText) view.findViewById(R.id.expiration_date);
        expirationDate.setClickable(false);
        expirationDate.setOnClickListener(this);
        expirationDate.setFocusable(false);
        expirationDate.setFocusableInTouchMode(false);

        cvvTil = (TextInputLayout) view.findViewById(R.id.et_cvv_number_til);
        cvv = (EditText) view.findViewById(R.id.et_cvv_number);
        ccHolderNameTil = (TextInputLayout) view.findViewById(R.id.et_name_in_card_til);
        ccHolderName = (EditText) view.findViewById(R.id.et_name_in_card);
        scanCreditCardButton = (ImageButton) view.findViewById(R.id.bt_scan_card);
        scanCreditCardButton.setOnClickListener(this);
        saveCreditCardButton = (Button) view.findViewById(R.id.bt_add_cc_request);
        saveCreditCardButton.setOnClickListener(this);
        expirationDatePickerDialog = new ExpirationDatePickerDialog();
        expirationDatePickerDialog.setListener(this);
        clearFields();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bt_scan_card) {
            Tpaga.startScanCreditCard(this);
        } else if (i == R.id.bt_add_cc_request) {
            Tpaga.validateAndTokenizeCreditCard(mUserActionListener, this, getCreditCard());
        } else if (i == R.id.expiration_date) {
            expirationDatePickerDialog.show(getActivity().getFragmentManager(), ccExpirationDateTag);
        }
    }

    public CreditCard getCreditCard() {
        String month = "";
        String year = "";
        if (!expirationDate.getText().toString().isEmpty()) {
            try {
                String[] date = expirationDate.getText().toString().split("/");
                month = date[0];
                year = date[1];
            } catch (Exception e) {
                month = "";
                year = "";
                e.printStackTrace();
            }
        }
        return CreditCard.create(
                ccNumber.getText().toString().replaceAll("\\s+", ""),
                year,
                month,
                cvv.getText().toString(),
                ccHolderName.getText().toString());
    }

    @Override
    public boolean validateFieldsCC() {
        return Tpaga.validateCreditCardData(getCreditCard());
    }

    @Override
    public void showValidateFieldsError() {
        View view = null;

        if (!TpagaTools.isNameValid(ccHolderName.getText().toString())) {
            ccHolderNameTil.setErrorEnabled(true);
            ccHolderNameTil.setError(getString(R.string.fail_validate_name_cc));
            ccHolderNameTil.requestFocus();
            view = ccHolderName;
        } else {
            ccHolderNameTil.setErrorEnabled(false);
        }

        if (cvv.getText().toString().isEmpty()) {
            cvvTil.setErrorEnabled(true);
            cvvTil.setError(getString(R.string.fail_validate_cvc_cc));
            view = cvv;
        } else {
            cvvTil.setErrorEnabled(false);
        }
        if (expirationDate.getText().toString().isEmpty()) {
            expirationDateTil.setErrorEnabled(true);
            expirationDateTil.setError(getString(R.string.fail_validate_expiration_date));
            expirationDateTil.requestFocus();
            view = expirationDate;
        } else {
            String[] date = expirationDate.getText().toString().split("/");
            if (!TpagaTools.isValidExpirationDate(date[1], date[0])) {
                expirationDateTil.setErrorEnabled(true);
                expirationDateTil.setError(getString(R.string.fail_validate_expiration_date));
                expirationDateTil.requestFocus();
                view = expirationDate;
            } else {
                expirationDateTil.setErrorEnabled(false);
            }
        }

        if (!TpagaTools.isValidCardNumber(ccNumber.getText().toString().replaceAll("\\s+", ""))) {
            ccNumberTil.setErrorEnabled(true);
            ccNumberTil.setError(getString(R.string.fail_validate_cc));
            view = ccNumber;
        } else {
            ccNumberTil.setErrorEnabled(false);
        }

        view.requestFocus();
    }

    @Override
    public void clearFields() {
        ccHolderName.setText("");
        cvv.setText("");
        ccNumber.setText("");
        expirationDate.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        char space = ' ';
        // Remove spacing char
        if (s.length() > 0 && (s.length() % 5) == 0) {
            final char c = s.charAt(s.length() - 1);
            if (space == c) {
                s.delete(s.length() - 1, s.length());
            }
        }
        // Insert char where needed.
        if (s.length() > 0 && (s.length() % 5) == 0) {
            char c = s.charAt(s.length() - 1);
            // Only if its a digit where there should be a space we insert a space
            if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 4) {
                s.insert(s.length() - 1, String.valueOf(space));
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mUserActionListener = (AddCreditCardView.UserActionsListener) getActivity();
        } catch (ClassCastException castException) {
            /** The activity does not implement the AddCreditCardView listener. */
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Tpaga.SCAN_CREDIT_CARD:
                if (resultCode == Tpaga.SCAN_CREDIT_CARD_OK) {
                    onResultScanCreditCard(Tpaga.onActivityResultScanCreditCard(data));
                }
                break;
        }
    }

    private void onResultScanCreditCard(CreditCard creditCard) {
        ccNumber.setText(creditCard.primaryAccountNumber);
        if (creditCard.expirationYear != null && !creditCard.expirationYear.isEmpty()
                && creditCard.expirationMonth != null && !creditCard.expirationMonth.isEmpty()) {
            expirationDate.setText(creditCard.expirationMonth + "/" + creditCard.expirationYear);
        }

        if (creditCard.cvc != null && !creditCard.cvc.isEmpty()) {
            cvv.setText(creditCard.cvc);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        expirationDate.setText(month + "/" + year);
    }
}