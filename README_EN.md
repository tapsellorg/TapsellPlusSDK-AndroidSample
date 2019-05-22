

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
    implementation 'ir.tapsell.plus:tapsell-plus-sdk-android:0.2.6'
}
```

Also you must add the following compile options if they do not exist in the android section:

```gradle
compileOptions {
  sourceCompatibility JavaVersion.VERSION_1_8
  targetCompatibility JavaVersion.VERSION_1_8
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
Create an template layout for showing native ad. This layout must contain components with ids given in table below:

|              |              id              |
|:------------:|:----------------------------:|
|     logo     |     tapsell_nativead_logo    |
|     title    |    tapsell_nativead_title    |
| ad indicator |  tapsell_nativead_sponsored  |
|  description | tapsell_nativead_description |
|    banner    |    tapsell_nativead_banner   |
|    button    |     tapsell_nativead_cta     |

You can also use the sample template which is included in the SDK with the following id:

`tapsell_content_banner_ad_template`

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
