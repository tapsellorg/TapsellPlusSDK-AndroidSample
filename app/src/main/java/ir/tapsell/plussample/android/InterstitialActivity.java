package ir.tapsell.plussample.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.AdShowListener;
import ir.tapsell.plus.TapsellPlus;

public class InterstitialActivity extends AppCompatActivity {

    private View btShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rewarded_video);

        init();
    }

    private void init() {
        View btRequest = findViewById(R.id.btRequest);
        btShow = findViewById(R.id.btShow);
        btShow.setEnabled(false);
        btRequest.setOnClickListener(v -> requestAd());
        btShow.setOnClickListener(v -> showAd());
    }

    private void requestAd() {
        TapsellPlus.requestInterstitial(
                this, BuildConfig.TAPSELL_INTERSTITIAL, new AdRequestCallback() {
                    @Override
                    public void response() {
                        if (isDestroyed())
                            return;

                        btShow.setEnabled(true);
                    }

                    @Override
                    public void error(@NonNull String message) {
                        if (isDestroyed())
                            return;

                        Log.e("error", message);
                    }

                });
    }

    private void showAd() {
        TapsellPlus.showAd(this, BuildConfig.TAPSELL_INTERSTITIAL, new AdShowListener() {
            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {

            }

            @Override
            public void onRewarded() {

            }

            @Override
            public void onError(String a) {

            }
        });
        btShow.setEnabled(false);
    }
}
