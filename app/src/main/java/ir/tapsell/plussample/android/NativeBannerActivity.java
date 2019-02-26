package ir.tapsell.plussample.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ir.tapsell.plus.AdHolder;
import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.TapsellPlus;

public class NativeBannerActivity extends AppCompatActivity {
    private FrameLayout adContainer;
    private AdHolder adHolder;

    private static final String TAG = "NativeBannerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_native_banner);

        init();
    }

    private void init() {
        adContainer = findViewById(R.id.adContainer);

        adHolder = TapsellPlus.createAdHolder(
                this, adContainer, R.layout.tapsell_content_banner_ad_template);

        requestAd();
    }

    private void requestAd() {
        TapsellPlus.requestNativeBanner(
                this,
                BuildConfig.TAPSELL_NATIVE_BANNER,
                new AdRequestCallback() {
                    @Override
                    public void response() {
                        if (isDestroyed())
                            return;

                        Log.d(TAG, "Ad Response");
                        showAd();
                    }

                    @Override
                    public void error(@NonNull String message) {
                        if (isDestroyed())
                            return;

                        Log.e(TAG, "error: " + message);
                    }
                });
    }

    private void showAd() {
        TapsellPlus.showAd(this, adHolder, BuildConfig.TAPSELL_NATIVE_BANNER);
    }
}
