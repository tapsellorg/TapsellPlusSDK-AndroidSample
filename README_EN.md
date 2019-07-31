

Adding TapsellPlus to your Android Studio Project
----

### Import TapsellPlus SDK

You can import the TapsellPlus SDK with a Gradle dependency that points to Tapsell's Maven repository. To use this repository, you need to reference it in the project-level `build.gradle` file.


```gradle
allprojects {  
    repositories {  
        google()  
        jcenter() 

        maven {  
            url 'https://dl.bintray.com/tapsellorg/maven'  
        }
    }  
}
```

Secondly, add the following dependency to the dependencies section of your app-level `build.gradle` file:

```gradle
dependencies {
    implementation 'ir.tapsell.plus:tapsell-plus-sdk-android:1.0.7'
}
```

Also you must add the following compile options if they do not exist in the android section:

```gradle
compileOptions {
  sourceCompatibility JavaVersion.VERSION_1_8
  targetCompatibility JavaVersion.VERSION_1_8
}
```

### Add other adNetworks
Add these items in the build.gradle file and dependencies section. For more information on each adNetwork, you can talk to our publisher's team.

```gradle
dependencies {
    .......
    //for adMob
    implementation 'com.google.android.gms:play-services-ads:17.2.1'

    //for unityAds
    implementation 'com.unity3d.ads:unity-ads:3.0.0'

    //for chartboost
    implementation 'ir.tapsell.sdk:chartboost-sdk-android:7.3.1'
    
    //for facebook
    implementation 'com.facebook.android:audience-network-sdk:5.3.0'
    implementation 'com.facebook.android:facebook-android-sdk:5.2.0'
    
    //for adcolony
    implementation 'com.adcolony:sdk:3.3.11'
    
    //for applovin
    implementation 'com.applovin:applovin-sdk:9.7.2'
    
    //for vungle
    implementation 'com.vungle:publisher-sdk-android:6.4.11'
    .....
}
```

For AdColony, add the following dependency to the dependencies section of your app-level `build.gradle` file:

```gradle
maven {
  url  "https://adcolony.bintray.com/AdColony"
}
```

### Proguard Configuration

Get `proguard.properties` file from this link [this link](https://github.com/tapsellorg/TapsellPlusSDK-AndroidSample/blob/master/app/proguard-rules.pro) and add it to proguard properties of your app module.

### Initialize TapsellPlus SDK

Get your app-key from [Tapsell Dashboard](https://dashboard.tapsell.ir/) and Initialize the SDK in the launcher (main) activity of your application as seen in the following code block:


```java
import ir.tapsell.plus.TapsellPlus;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TapsellPlus.initialize(this, TAPSELL_KEY);
    }
}
```

Where `TAPSELL_KEY` is the app-key copied from your tapsell dashboard.


### Implementing Rewarded Video Ads

First of all, you must create a new rewarded video ad-zone in your application dashboard and use the generated `zoneId` to show rewarded video ads.

Use the following code to request a new rewarde video ad using the TapsellPlus SDK:

```java
import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.TapsellPlus;
    .......
    private void requestAd() {
        TapsellPlus.requestRewardedVideo(context, ZONE_ID_REWARDED_VIDEO, new AdRequestCallback() {
            @Override
            public void response() {
                //ad is ready to show
            }

            @Override
            public void error(String message) {
            }

        });
    }
```

When `response` function is called, the ad is ready to be shown. You can start showing the video using the `showAd` method and the `zoneId` from you dashboard:

```java
private void showAd() {
    TapsellPlus.showAd(activity, ZONE_ID_REWARDED_VIDEO);
}
```


To get open, close, error and reward callbacks you can use overloaded `showAd` method with callbacks:


```java

import ir.tapsell.plus.AdShowListener;
    .......
    private void showAd() {
        TapsellPlus.showAd(this, ZONE_ID_REWARDED_VIDEO, new AdShowListener() {
            @Override
            public void onOpened() {
                //ad opened
            }

            @Override
            public void onClosed() {
                //ad closed
            }

            @Override
            public void onRewarded() {
                //reward
            }

            @Override
            public void onError(String message) {
                //error
            }
        });
    }
```

### Implementing Interstitial Ads

To implement interstitial ads in your application, follow the procedure describe in implementing rewarded ads but use `TapsellPlus.requestInterstitial` method instead of `requestRewardedVideo` method.  
The `zoneId` used in this method must have interstitial type in your dashboard.

### Implementing Native Banner Ads

You need to create a native banner ad-zone in your dashboard to use the generated `zoneId` for showing native banner ads.

Add an empty container in the page you want to show native banner. An example xml file is given below:

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <FrameLayout
        android:id="@+id/adContainer"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

    </FrameLayout>

</FrameLayout>
```
Create a template layout for showing native ad. This layout must contain components with ids given in table below and root view must be `com.google.android.gms.ads.formats.UnifiedNativeAdView`:

|       view       |              id              | type  |
|:------------:|:----------------------------:|:-:|
|     logo     |     `tapsell_nativead_logo`    | `ImageView`  |
|     title    |    `tapsell_nativead_title`    | `TextView`  |
| ad indicator |  `tapsell_nativead_sponsored`  | `View`  |
|  description | `tapsell_nativead_description` | `TextView`  |
|    banner    |    `tapsell_nativead_banner`   | `ir.tapsell.sdk.nativeads.views.RatioImageView`  |
|  media view  |`tapsell_nativead_banner_admob` | `com.google.android.gms.ads.formats.MediaView`  |
|    button    |     `tapsell_nativead_cta`     | `TextView`  |
|    clickable view    |     `tapsell_nativead_cta_view`     | `View`  |

* If in your design there is no button to click, you can use **clickable view**. 
* Type of views can be inherited from the given **types**.
* You must dedicate 2 views to show Ad images. One from `ir.tapsell.sdk.nativeads.views.RatioImageView` for Tapsell and another from `com.google.android.gms.ads.formats.MediaView` for AdMob. These 2 can exaclty overlay eachother. 
* You can also use the sample template which is included in the SDK with the following id:
`native_banner`

Create an `AdHolder` for showing native banner by using `TapsellPlus.createAdHolder` method with container and template layout as input parameters.

```java
import ir.tapsell.plus.AdHolder;
import ir.tapsell.plus.TapsellPlus;
...
ViewGroup adContainer = findViewById(R.id.adContainer);
...
AdHolder adHolder = TapsellPlus.createAdHolder(
      context, adContainer, R.layout.tapsell_content_banner_ad_template);
```
Use the following code to request for a native banner ad

```java
import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.TapsellPlus;
  .......
  private void requestAd() {
        TapsellPlus.requestNativeBanner(context, ZONE_ID_NATIVE_BANNER, new AdRequestCallback() {
            @Override
            public void response() {
                //ad is ready to show
            }

            @Override
            public void error(String message) {
            }

        });
    }
```
If `response` method is called, you can show the ad using overloaded `showAd` function with `adHolder` and `zoneId` as input variables.

```java
private void showAd() {
    TapsellPlus.showAd(activity, adHolder, ZONE_ID_NATIVE_BANNER);
}
```

### Implementing Standard Banner Ads

Create a standard banner ad-zone in your dashboard and add a `ViewGroup` container to the layout in which you want to display the banner.

Example container for showing standard banner ad

```xml
<RelativeLayout
    android:id="@+id/standardBanner"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    android:gravity="center" />
```

Show a standard banner ad by calling 'TapsellPlus.showBannerAd' method with the `ViewGroup`, your `zoneId` and banner size as input parameters:

```java
ViewGroup bannerContainer = findViewById(R.id.standardBanner);

TapsellPlus.showBannerAd(
        context, 
        bannerContainer,
        ZONE_ID_STANDARD_BANNER,
        TapsellPlusBannerType.BANNER_320x50,
        new AdRequestCallback() {
            @Override
            public void response() {
            }

            @Override
            public void error(String message) {
            }
});
```

### Testing AdNetworks
To ensure the correctness work of each adNetwrok, Use zoneId for each one. Each zoneId is related to an AdNetwork and an adType, and Test ad is displayed.
* Note: in test mode should use test appId.
* The application must be opened and closed once, for the correct operation of the test mode. Also, in the second request, the AdNetwork ad will be displayed.
* For facebook testing, the hash of the device on which test is performed should be given according to the method described in sdk.
* Run the test in release mode.

Use this appId to test.
```java
TapsellPlus.initialize(this, "alsoatsrtrotpqacegkehkaiieckldhrgsbspqtgqnbrrfccrtbdomgjtahflchkqtqosa");
```

Use the below zoneId to request and display ads for each AdNetwork and any advertise. Currently, only the following adType / adNetworks are usable.

|        Ad Network      |              Ad Type              |ZoneId|
|:------------:|:----------------------------:|:----------------------------:|
|     Tapsell     |     Rewarded Video    | `5cfaa802e8d17f0001ffb28e`|
|     Tapsell    |    Interstitial    |`5cfaa942e8d17f0001ffb292`|
| Tapsell |  Native  |`5cfaa9deaede570001d5553a`|
|  Tapsell | Standard |`5cfaaa30e8d17f0001ffb294`|
|    Admob    |    Rewarded Video   |`5cfaa8aee8d17f0001ffb28f`|
|    Admob    |     Interstitial     |`5cfaa9b0e8d17f0001ffb293`|
|    Admob    |     Standard     |`5cfaaa4ae8d17f0001ffb295`|
|    Admob    |     Native     |`5d123c9968287d00019e1a94`|
|    Admob    |     Native Video     |`5d123d6f68287d00019e1a95`|
|    Unity Ads    |     Rewarded Video     |`5cfaa8eae8d17f0001ffb291`|
|    Chartboost    |     Rewarded Video     |`5cfaa8cee8d17f0001ffb290`|
|    Facebook    |     Rewarded Video     |`5cfaa838aede570001d55538`|
|    Facebook    |     Interstitial     |`5cfaa975aede570001d55539`|
|    AdColony    |     Rewarded Video     |`5d3362766de9f600013662d5`|
|    AdColony    |     Interstitial     |`5d336289e985d50001427acf`|
|    AppLovin    |     Rewarded Video     |`5d3eb48c3aef7a0001406f84`|
|    AppLovin    |     Interstitial     |`5d3eb4fa3aef7a0001406f85`|
|    AppLovin    |     Standard     |`5d3eb5337a9b060001892441`|
|    Vungle    |     Rewarded Video     |`5d3eb55a7a9b060001892442`|
|    Vungle    |     Interstitial     |`5d3eb56d3aef7a0001406f86`|


When you use facebook, the following text is printed in logcat.
```
When testing your app with Facebook's ad units you must specify the device hashed ID to ensure the delivery of test ads, add the following code before loading an ad: AdSettings.addTestDevice("YOUR_DEVICE_HASH");
```

To see the Facebook test ad, give your device's hash value to the Tapsellplus library from this method.

```java
TapsellPlus.addFacebookTestDevice("YOUR_DEVICE_HASH");
```
