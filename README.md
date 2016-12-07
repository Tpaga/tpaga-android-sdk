## Tpaga android-sdk

Tpaga SDK allows your application connect with the Tpaga API to create new tokens which represents a credit cards. Have two options to generate credit card tokens. The first include a fragment where you can to write the credit card data or scan it. 
The other option is to make the request to the Tpaga API. Both return a credit card token. You can show a implementation example here [Example Tpaga SDK](https://bitbucket.org/tpaga/tpaga-sdk-android-sample-app)  

### Latest release

The most recent release is tpagasdk 1.0.9, released December 7, 2016 

### Get Started

1. Add depencency: 

- Using Maven add the following
```
<dependency>
  <groupId>co.tpaga</groupId>
  <artifactId>tpagasdk</artifactId>
  <version>1.0.5</version>
  <type>pom</type>
</dependency>
```

- Using Gradle
```
dependencies{
  compile 'co.tpaga:tpagasdk:1.0.4'
}
```

- Using Ivy
```
<dependency org='co.tpaga' name='tpagasdk' rev='1.0.5'>
  <artifact name='tpagasdk' ext='pom' />
</dependency>
```

>1. Other option is download and import the module [tpagasdk](https://github.com/AdelaTpaga/android-sdk/tree/master/SampleTpagaSdk/tpagasdk) into your project.
2. Add `compile project(":tpagasdk")` in your app build.gradle
```
dependencies {
    compile project(":tpagasdk")
 }
```

2. Add permissions in the manifest
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

3. Initialize Tpaga SDK

In the Activity or Application file initialize tpagasdk. Set your public key and select environment.
Use your `public_api_key` to initialize. This key is in your dashboard [sandbox](https://sandbox.tpaga.co)/[production](https://api.tpaga.co/). The enviroment can be `TpagaAPI.SANDBOX` OR `TpagaAPI.PRODUCTION`. You must check that the added public_api_key matches the selected environment.

```
Tpaga.initialize("public_api_key"), TpagaAPI.SANDBOX);
```

### Add Credit Card

First option is use AddCreditCardFragment to add credit cards

- Add `AddCreditCardFragment` fragment in your activity
```
FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
AddCreditCardFragment mAddCreditCardFragment = new AddCreditCardFragment();
ft.add(R.id.content_default, mAddCreditCardFragment, fragmentTag);
ft.commitAllowingStateLoss();
```

- Your activity must implement `AddCreditCardView.UserActionsListener` and override methods.
```
//This method is execute if fields in fragment are correct 
@Override
public void onResponseSuccessfulOfAddCreditCard(CreditCardWallet creditCardWallet) {
    /**
     * you must send the creditCardWallet.tempCcToken to your server to use in payments
     */
}

@Override
public void showError(Throwable t) {
    Toast.makeText(this, "credit card Throwable error", Toast.LENGTH_LONG).show();
    t.printStackTrace();
}

@Override
public void showError(GenericResponse genericResponse) {
    showToastError(genericResponse.status);
}

@Override
public void showToastError(StatusResponse response) {
    if (response != null && !response.responseMessage.isEmpty()) {
        Toast.makeText(this, response.responseMessage, Toast.LENGTH_LONG).show();
    }
}

@Override
public CreditCardTpaga getCreditCard() {
    return mAddCreditCardFragment.getCC();
}
```

- Add `mAddCreditCardFragment.onActivityResult(requestCode, resultCode, data);` In the onActivityResult, this to update fragment when scan card.
```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    mAddCreditCardFragment.onActivityResult(requestCode, resultCode, data);
}
```

To customize AddCreditCardFragment you can overwrite the next styles
```
<style name="button_red" parent="Theme.AppCompat.Light"></style>
<style name="title_style"></style>
<style name="default_edit_text_style" parent="@style/Base.Widget.AppCompat.EditText"></style>
```

Second option without AddCreditCardFragment

- In your activity create a `AddCreditCardPresenter` instance;
```
AddCreditCardPresenter mAddCreditCardPresenter = new AddCreditCardPresenter(this, Tpaga.tpagaApi);
```

- Implements `AddCreditCardView.UserActionsListener` and override methods
```
@Override
public CreditCardTpaga getCreditCard() {
    return CreditCardTpaga.create("number", "year", "month", "cvv", "name");
}
```

- Call `mAddCreditCardPresenter.tokenizeCreditCard();` to request card token

- Optionaly you must use `Tpaga.scanCard(this)` to start scan credit card intent and in `onActivityResult` method add the next lines. Where `Tpaga.onActivityResultScanCreditCard(data)` return a `CreditCardTpaga` object
```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
        case Tpaga.SCAN_CREDIT_CARD:
            if (resultCode == 13274388) {
                onResultScanCreditCard(Tpaga.onActivityResultScanCreditCard(data));
            }
            break;
    }
}
```