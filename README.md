## tpaga-android-sdk

Tpaga SDK allows your application connect with the Tpaga API to create new tokens which represents a credit cards. Have two options to generate credit card tokens. The first include a fragment where you can to write the credit card data or scan it. 
The other option is to make the request to the Tpaga API. Both return a credit card token. You can show a implementation example here [Example Tpaga SDK](https://bitbucket.org/tpaga/tpaga-sdk-android-sample-app)  

### Latest release

The most recent release is tpaga-sdk 1.0.0, released December 13, 2016 

### Get Started

1. Add depencency: 

- Using Maven add the following
```
<dependency>
  <groupId>co.tpaga</groupId>
  <artifactId>android</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

- Using Gradle

```
allprojects {
    repositories {
        maven {
            url "https://raw.github.com/Tpaga/tpaga-android-sdk/releases"
        }
    }
}

dependencies{
  compile 'co.tpaga:android:1.0.0'
}
```

- Using Ivy
```
<dependency org='co.tpaga' name='android' rev='1.0.0'>
  <artifact name='android' ext='pom' />
</dependency>
```

>1. Other option is download and import the module [tpaga-sdk](https://bitbucket.org/tpaga/tpaga-android-sdk) into your project.
2. Add `compile project(":tpaga-sdk")` in your app build.gradle
```
dependencies {
    compile project(":tpaga-sdk")
 }
```

2. Add permissions in the manifest
```
<uses-permission android:name="android.permission.INTERNET" />
```

3. Initialize Tpaga SDK

In the Activity or Application file initialize tpagasdk. Set your public key and select environment.
Use your `public_api_key` to initialize. This key is in your dashboard [sandbox](https://sandbox.tpaga.co)/[production](https://api.tpaga.co/). The enviroment can be `TpagaAPI.SANDBOX` OR `TpagaAPI.PRODUCTION`. You must check that the added public_api_key matches the selected environment.

```
Tpaga.initialize("public_api_key", Tpaga.SANDBOX);
```

## Usage

### Add Credit Card (tokenize)

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
//This method is execute if fields in fragment are correct and tokenize credit card is successful
@Override
public void onResponseSuccessTokenizeCreditCard(String creditCardToken) {
    /**
     * you must send the creditCardToken to your server to use in payments
     */
}

//This method is called when the request presents a error
@Override
public void showError(Throwable t) {
    if (t instanceof TpagaException) { 
        //.getStatusCode() return the http error
        ((TpagaException) t).getStatusCode();
    }
}
```

To customize AddCreditCardFragment you can overwrite the next styles

```
<style name="Base.Button" parent="Theme.AppCompat.Light"></style>
<style name="Base.TextInputLayout" parent="Theme.AppCompat.Light"></style>
<style name="Base.EditText" parent="@style/Base.Widget.AppCompat.EditText"></style>
<style name="Tittle"></style>
```

Second option without AddCreditCardFragment

- Implements `AddCreditCardView.UserActionsListener` and overwrite methods as show above.

- Call `Tpaga.tokenizeCreditCard(this, CreditCard.create("number", "year", "month", "cvv", "name"));` to request card token

- Optionaly you must use `Tpaga.startScanCreditCard(this);` to start scan credit card intent and in `onActivityResult` method add the next lines. Where `Tpaga.onActivityResultScanCreditCard(data)` return a `CreditCard` object
```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
        case Tpaga.SCAN_CREDIT_CARD:
            if (resultCode == Tpaga.SCAN_CREDIT_CARD_OK)) {
                onResultScanCreditCard(Tpaga.onActivityResultScanCreditCard(data));
            }
            break;
    }
}
```
## Contact

Tpaga Support / soporte@tpaga.co - for personal support at any phase of integration

## License

The Tpaga Android SDK is open source and available under the Apache License. See the LICENSE file for more info.


