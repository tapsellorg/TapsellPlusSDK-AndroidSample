package ir.tapsell.plussample.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.AdShowListener;
import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plus.TapsellPlusBannerType;
import ir.tapsell.plus.model.TapsellPlusAdModel;
import ir.tapsell.plus.model.TapsellPlusErrorModel;

public class StandardBannerActivity extends AppCompatActivity {

    private static final String TAG = "StandardActivity";
    private Button showButton;
    private String responseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_standard_banner);

        init();
    }

    private void init() {
        Button requestButton = findViewById(R.id.request_button);
        showButton = findViewById(R.id.show_button);
        Button destroyButton = findViewById(R.id.destroy_button);
        showButton.setEnabled(false);
        requestButton.setOnClickListener(v -> requestAd());
        showButton.setOnClickListener(v -> showAd());
        destroyButton.setOnClickListener(v -> destroyAd());
    }

    private void requestAd() {
        TapsellPlus.requestStandardBannerAd(
                this, BuildConfig.TAPSELL_STANDARD_BANNER,
                TapsellPlusBannerType.BANNER_320x50,
                new AdRequestCallback() {
                    @Override
                    public void response(String s) {
                        super.response(s);
                        if (isDestroyed())
                            return;

                        Log.d(TAG, "response");
                        responseId = s;
                        showButton.setEnabled(true);
                    }

                    @Override
                    public void error(@NonNull String message) {
                        if (isDestroyed())
                            return;

                        Log.e(TAG, "error " + message);
                    }
                });
    }

    private void showAd() {
        TapsellPlus.showStandardBannerAd(this, responseId,
                findViewById(R.id.standardBanner),
                new AdShowListener() {
                    @Override
                    public void onOpened(TapsellPlusAdModel tapsellPlusAdModel) {
                        super.onOpened(tapsellPlusAdModel);
                        Log.d(TAG, "Ad Opened");
                    }

                    @Override
                    public void onError(TapsellPlusErrorModel tapsellPlusErrorModel) {
                        super.onError(tapsellPlusErrorModel);
                        Log.e(TAG, tapsellPlusErrorModel.toString());
                    }
                });
        showButton.setEnabled(false);
    }

    private void destroyAd() {
        TapsellPlus.destroyStandardBanner(this, responseId, findViewById(R.id.standardBanner));
    }

    @Override
    protected void onDestroy() {
        destroyAd();
        super.onDestroy();
    }
}
