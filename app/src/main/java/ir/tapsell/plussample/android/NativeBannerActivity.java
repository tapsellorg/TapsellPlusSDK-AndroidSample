package ir.tapsell.plussample.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ir.tapsell.plus.AdHolder;
import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.AdShowListener;
import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plus.model.TapsellPlusAdModel;
import ir.tapsell.plus.model.TapsellPlusErrorModel;

public class NativeBannerActivity extends AppCompatActivity {

    private static final String TAG = "NativeBannerActivity";

    private AdHolder adHolder;
    private String responseId;
    FrameLayout adContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_native_banner);

        init();
    }

    private void init() {
        adContainer = findViewById(R.id.adContainer);

        adHolder = TapsellPlus.createAdHolder(
                this, adContainer, R.layout.native_banner);

        requestAd();
    }

    private void requestAd() {
        TapsellPlus.requestNativeAd(
                this,
                BuildConfig.TAPSELL_NATIVE_BANNER,
                new AdRequestCallback() {
                    @Override
                    public void response(String s) {
                        super.response(s);
                        if (isDestroyed())
                            return;
                        Log.d(TAG, "Ad Response");
                        responseId = s;
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
        TapsellPlus.showNativeAd(this, responseId, adHolder,
                new AdShowListener() {
                    @Override
                    public void onOpened(TapsellPlusAdModel tapsellPlusAdModel) {
                        super.onOpened(tapsellPlusAdModel);
                        Log.d(TAG, "Ad Open");
                    }

                    @Override
                    public void onError(TapsellPlusErrorModel tapsellPlusErrorModel) {
                        super.onError(tapsellPlusErrorModel);
                        Log.e("onError", tapsellPlusErrorModel.toString());
                    }
                });
    }

    private void destroyAd() {
        TapsellPlus.destroyNativeBanner(NativeBannerActivity.this, responseId);
    }

    @Override
    protected void onDestroy() {
        destroyAd();
        super.onDestroy();
    }
}
