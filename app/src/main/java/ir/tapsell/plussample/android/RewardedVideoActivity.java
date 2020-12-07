package ir.tapsell.plussample.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.AdShowListener;
import ir.tapsell.plus.TapsellPlus;

public class RewardedVideoActivity extends AppCompatActivity {

    private static final String TAG = "RewardActivity";

    private View btShow;
    private View btRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rewarded_video);
        init();
    }

    private void init() {
        btRequest = findViewById(R.id.btRequest);
        btShow = findViewById(R.id.btShow);
        btShow.setEnabled(false);
        btRequest.setOnClickListener(v -> requestAd());
        btShow.setOnClickListener(v -> showAd());
    }

    private void requestAd() {

        TapsellPlus.requestRewardedVideo(
                this, BuildConfig.TAPSELL_REWARDED_VIDEO,
                new AdRequestCallback() {
                    @Override
                    public void response() {

                        Log.d(TAG, "Ad Response");
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
        TapsellPlus.showAd(this, BuildConfig.TAPSELL_REWARDED_VIDEO,
                new AdShowListener() {
                    @Override
                    public void onOpened() {
                        Log.d(TAG, "Ad Opened");

                    }

                    @Override
                    public void onClosed() {
                        Log.d(TAG, "Ad Closed");

                    }

                    @Override
                    public void onRewarded() {
                        Log.d(TAG, "Reward");

                    }

                    @Override
                    public void onError(String s) {
                        Log.e(TAG, "Reward");

                    }
                });
        btShow.setEnabled(false);
    }
}
