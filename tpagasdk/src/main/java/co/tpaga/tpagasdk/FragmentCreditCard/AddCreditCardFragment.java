package co.tpaga.tpagasdk.FragmentCreditCard;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import co.tpaga.tpagasdk.Entities.CreditCardTpaga;
import co.tpaga.tpagasdk.R;
import co.tpaga.tpagasdk.Tools.TpagaTools;
import co.tpaga.tpagasdk.Tpaga;

public class AddCreditCardFragment extends Fragment implements AddCreditCardView.View, View.OnClickListener, TextWatcher {
    public static final String TAG = AddCreditCardFragment.class.getSimpleName();

    private TextInputLayout ccNumberTil;
    private EditText cc_number;

    private MaterialBetterSpinner month;

    private MaterialBetterSpinner year;

    private TextInputLayout cvvTil;
    private EditText cvv;

    private TextInputLayout nameTil;
    private EditText name;

    private ImageButton scanButton;
    private Button requestButton;

    private AddCreditCardView.UserActionsListener mAddCreditCardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_credit_card, container, false);
        initDependences(view);
        setSpinnerYears();
        setSpinnerMonths();
        return view;
    }

    public void initDependences(View view) {
        ccNumberTil = (TextInputLayout) view.findViewById(R.id.et_cc_number_til);
        cc_number = (EditText) view.findViewById(R.id.et_cc_number);
        cc_number.addTextChangedListener(this);
        month = (MaterialBetterSpinner) view.findViewById(R.id.et_month);
        year = (MaterialBetterSpinner) view.findViewById(R.id.et_year);
        cvvTil = (TextInputLayout) view.findViewById(R.id.et_cvv_number_til);
        cvv = (EditText) view.findViewById(R.id.et_cvv_number);
        nameTil = (TextInputLayout) view.findViewById(R.id.et_name_in_card_til);
        name = (EditText) view.findViewById(R.id.et_name_in_card);
        scanButton = (ImageButton) view.findViewById(R.id.bt_scan_card);
        scanButton.setOnClickListener(this);
        requestButton = (Button) view.findViewById(R.id.bt_add_cc_request);
        requestButton.setOnClickListener(this);
    }

    private void setSpinnerMonths() {
        ArrayAdapter<String> adapterMonths = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, TpagaTools.getMonths());
        month.setAdapter(adapterMonths);
        adapterMonths.notifyDataSetChanged();

    }

    private void setSpinnerYears() {
        ArrayAdapter<String> adapterYears = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, TpagaTools.getYears());
        year.setAdapter(adapterYears);
        adapterYears.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        name.setText("");
        cvv.setText("");
        cc_number.setText("");
        year.setSelection(0);
        month.setSelection(0);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bt_scan_card) {
            Tpaga.startScanCreditCard(getActivity());
        } else if (i == R.id.bt_add_cc_request) {
            Tpaga.validateAndTokenizeCreditCard(mAddCreditCardView, this);
//            mAddCreditCardPresenter.onClickAddCC();
        }
    }

    @Override
    public CreditCardTpaga getCC() {
        return CreditCardTpaga.create(
                cc_number.getText().toString().replaceAll("\\s+", ""),
                year.getText().toString(),
                month.getText().toString(),
                cvv.getText().toString(),
                name.getText().toString());
    }

    @Override
    public boolean validateFieldsCC() {
        return Tpaga.validateCreditCardData(getCC());
    }

    @Override
    public void showToastMsg() {
        if (!TpagaTools.isNameValid(name.getText().toString())) {
            nameTil.setErrorEnabled(true);
            nameTil.setError(getString(R.string.fail_validate_name_cc));
            nameTil.requestFocus();
        } else {
            nameTil.setErrorEnabled(false);
        }

        if (cvv.getText().toString().isEmpty()) {
            cvvTil.setErrorEnabled(true);
            cvvTil.setError(getString(R.string.fail_validate_cvc_cc));
            cvvTil.requestFocus();
        } else {
            cvvTil.setErrorEnabled(false);
        }

        if (!TpagaTools.isValidCardNumber(cc_number.getText().toString().replaceAll("\\s+", ""))) {
            ccNumberTil.setErrorEnabled(true);
            ccNumberTil.setError(getString(R.string.fail_validate_cc));
            ccNumberTil.requestFocus();
        } else {
            ccNumberTil.setErrorEnabled(false);
        }

        if (!TpagaTools.isValidMonth(month.getText().toString())) {
            TpagaTools.showToast(getActivity(), R.string.fail_validate_month_cc);
        } else if (year.getText().toString().isEmpty()) {
            TpagaTools.showToast(getActivity(), R.string.fail_validate_year_cc);
        }
    }

    @Override
    public void clear() {
        name.setText("");
        cvv.setText("");
        cc_number.setText("");
        year.setSelection(0);
        month.setSelection(0);
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
            mAddCreditCardView = (AddCreditCardView.UserActionsListener) getActivity();
        } catch (ClassCastException castException) {
            /** The activity does not implement the AddCreditCardView listener. */
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Tpaga.SCAN_CREDIT_CARD:
                if (resultCode == 13274388) {
                    onResultScanCreditCard(Tpaga.onActivityResultScanCreditCard(data));
                }
                break;
        }
    }

    public void onResultScanCreditCard(CreditCardTpaga creditCardTpaga) {
        cc_number.setText(creditCardTpaga.primaryAccountNumber);
        if (creditCardTpaga.expirationYear != null && !creditCardTpaga.expirationYear.isEmpty()) {
            year.setText(creditCardTpaga.expirationYear);
        }
        if (creditCardTpaga.expirationMonths != null && !creditCardTpaga.expirationMonths.isEmpty()) {
            month.setText(creditCardTpaga.expirationMonths);
        }
        if (creditCardTpaga.cvc != null && !creditCardTpaga.cvc.isEmpty()) {
            cvv.setText(creditCardTpaga.cvc);
        }
    }
}