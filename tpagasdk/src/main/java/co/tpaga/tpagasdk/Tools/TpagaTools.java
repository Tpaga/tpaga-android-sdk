package co.tpaga.tpagasdk.Tools;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import org.apache.commons.validator.routines.CreditCardValidator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TpagaTools {


    public static boolean isValidCardNumber(String carnum) {
        CreditCardValidator ccv = new CreditCardValidator(CreditCardValidator.MASTERCARD + CreditCardValidator.VISA +
                CreditCardValidator.AMEX + CreditCardValidator.DINERS);
        if (TextUtils.isEmpty(carnum)) return false;
        return ccv.isValid(carnum);
    }

    public static boolean isValidExpirationDate(String year, String month) {
        return isValidMonth(month);

    }

    public static boolean isValidMonth(String s) {
        if (TextUtils.isEmpty(s)) return false;
        int month = Integer.parseInt(s);
        return ((month > 0 && month < 13) ? true : false);
    }


    public static boolean isNameValid(CharSequence name) {
        if (TextUtils.isEmpty(name)) return false;
        Pattern pattern = Pattern.compile("[^0-9()*^|\\.,:;\"&@$~+_-]+");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static void showToast(Context context, int i) {
        int duration = Toast.LENGTH_LONG;
        Toast.makeText(context, i, duration).show();
    }

    public static void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static ArrayList<String> getMonths() {
        ArrayList<String> months = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            months.add(Integer.toString(i));
        }
        return months;
    }

    public static ArrayList<String> getYears() {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i <= thisYear + 20; i++) {
            years.add(Integer.toString(i));
        }
        return years;
    }
}
