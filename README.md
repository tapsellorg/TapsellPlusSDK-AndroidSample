

## <div dir="rtl">آموزش راه اندازی کتاب‌خانه TapsellPlus</div>

### <div dir="rtl">اضافه کردن کتابخانه به پروژه</div>


<div dir="rtl"></div>
<div dir="rtl">ابتدا ریپازیتوری تپسل را به فایل build.gradle پروژه اضافه کنید</div>

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

<div dir="rtl">در بخش dependencies فایل build.gradle اپلیکیشن خط زیر را اضافه کنید</div>

```gradle
dependencies {
    implementation 'ir.tapsell.plus:tapsell-plus-sdk-android:0.1.4'
}
```

<div dir="rtl">همچنین در قسمت android این قسمت وجود ندارد اضافه‌اش کنید</div>

```gradle
compileOptions {
  sourceCompatibility JavaVersion.VERSION_1_8
  targetCompatibility JavaVersion.VERSION_1_8
}
```

<div dir="rtl">تنظیمات پروگوارد را از  <a href="https://github.com/tapsellorg/TapsellPlusSDK-AndroidSample/blob/master/app/proguard-rules.pro">این فایل</a> دریافت کنید</div>

<div dir="rtl">کلید تپسل را از <a href="https://dashboard.tapsell.ir/">پنل</a> دریافت کنید</div>

<div dir="rtl">در اکتیویتی اولیه برنامه tapsell plus را به این شکل مقدار دهی کنید</div>

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

## <div dir="rtl">آموزش تبلیغات ویدیو جایزه‌ای</div>

<div dir="rtl">ابتدا از پنل یک تبلیغگاه (zone) ویدیو جایزه‌ای بسازید و zoneId رو زمان درخواست و نمایش تبلیغ استفاده کنید</div>

<div dir="rtl">مطابق کد زیر درخواست تبلیغ دهید</div>

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

<div dir="rtl">بعد از اجرای متد response تبلیغ آماده نمایش است و میتوانید مطابق روش زیر نمایش دهید</div>

```java
private void showAd() {
    TapsellPlus.showAd(activity, ZONE_ID_REWARDED_VIDEO);
}
```

<div dir="rtl">میتوانید به روش زیر از باز و بسته شدن تبلیغ و دادن جایزه به یوزر مطلع بشید</div>

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

## <div dir="rtl">آموزش تبلیغات آنی</div>
<div dir="rtl">مطابق تبلیغات جایزه‌ای پیش برید فقط زمان درخواست تبلیغ از متد TapsellPlus.requestInterstitial استفاده کنید</div>

## <div dir="rtl">آموزش تبلیغات بنر همسان</div>

<div dir="rtl">ابتدا از پنل یک تبلیغگاه (zone) بنر همسان بسازید و zoneId را زمان درخواست و نمایش تبلیغ استفاده کنید</div>

<div dir="rtl">در صفحه‌ای که قصد دارید بنر همسان نمایش بدهید باید یک  view اضافه کنید</div>

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

<div dir="rtl">باید یک layout دلخواه مطابق شکلی که قصد دارید تبلیغ نمایش داده بشود بسازید و id بخش‌های مختلف مطابق با جدول زیر باشد:</div>

|              |              id              |
|:------------:|:----------------------------:|
|     logo     |     tapsell_nativead_logo    |
|     title    |    tapsell_nativead_title    |
| ad indicator |  tapsell_nativead_sponsored  |
|  description | tapsell_nativead_description |
|    banner    |    tapsell_nativead_banner   |
|    button    |     tapsell_nativead_cta     |

<div dir="rtl">همچنین می‌توانید از view‌ای که برای این منظور از قبل آماده شده با id زیر استفاده کنید</div>

`tapsell_content_banner_ad_template`

<div dir="rtl">مطابق قطعه کد زیر adContainer و شناسه layout تبلیغ را به تپسل پلاس بدهید تا یک AdHolder بسازید</div>

```java
import ir.tapsell.plus.AdHolder;
import ir.tapsell.plus.TapsellPlus;
...
ViewGroup adContainer = findViewById(R.id.adContainer);
...
AdHolder adHolder = TapsellPlus.createAdHolder(
      context, adContainer, R.layout.tapsell_content_banner_ad_template);
```

<div dir="rtl">و مطابق این قطعه کد درخواست تبلیغ بدهید</div>

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

<div dir="rtl">بعد از اجرای متد response تبلیغ آماده نمایش است و می‌توانید مطابق روش زیر نمایش دهید</div>

```java
private void showAd() {
    TapsellPlus.showAd(activity, adHolder, ZONE_ID_NATIVE_BANNER);
}
```

## <div dir="rtl">آموزش تبلیغات بنر استاندارد</div>

<div dir="rtl">ابتدا از پنل یک تبلیغگاه (zone) بنر استاندارد بسازید و zoneId را زمان درخواست استفاده کنید</div>

<div dir="rtl">در صفحه‌ای که می‌خواهید تبلیغ نمایش داده شود یک viewGrop اضافه کنید</div>

```xml
<RelativeLayout
    android:id="@+id/standardBanner"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    android:gravity="center" />
```

<div dir="rtl">مطابق کد زیر zoneId و view‌ای که معرفی کردید را به تپسل‌پلاس بدهید</div>

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
