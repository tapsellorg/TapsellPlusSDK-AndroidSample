package ir.tapsell.plussample.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.AdShowListener;
import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plus.model.TapsellPlusAdModel;
import ir.tapsell.plus.model.TapsellPlusErrorModel;

public class InterstitialActivity extends AppCompatActivity {

    private static final String TAG = "RewardActivity";

    private Button showButton;
    private TextView logTextView;
    private String responseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rewarded_video);

        init();
    }

    private void init() {
        logTextView = findViewById(R.id.log_text_view);
        Button requestButton = findViewById(R.id.request_button);
        showButton = findViewById(R.id.show_button);
        showButton.setEnabled(false);
        requestButton.setOnClickListener(v -> requestAd());
        showButton.setOnClickListener(v -> showAd());
    }

    private void requestAd() {
        TapsellPlus.requestInterstitialAd(
                this, BuildConfig.TAPSELL_INTERSTITIAL,
                new AdRequestCallback() {
                    @Override
                    public void response(TapsellPlusAdModel tapsellPlusAdModel) {
                        super.response(tapsellPlusAdModel);
                        if (isDestroyed())
                            return;

                        responseId = tapsellPlusAdModel.getResponseId();

                        showLogToDeveloper("response", Log.DEBUG);

                        showButton.setEnabled(true);
                    }

                    @Override
                    public void error(@NonNull String message) {
                        if (isDestroyed())
                            return;

                        showLogToDeveloper(message, Log.ERROR);
                    }
                });
    }

    private void showAd() {
        TapsellPlus.showInterstitialAd(this, responseId,
                new AdShowListener() {
                    @Override
                    public void onOpened(TapsellPlusAdModel tapsellPlusAdModel) {
                        super.onOpened(tapsellPlusAdModel);
                        showLogToDeveloper("onOpened", Log.DEBUG);
                    }

                    @Override
                    public void onClosed(TapsellPlusAdModel tapsellPlusAdModel) {
                        super.onClosed(tapsellPlusAdModel);
                        showLogToDeveloper("onClosed", Log.DEBUG);
                    }

                    @Override
                    public void onError(TapsellPlusErrorModel tapsellPlusErrorModel) {
                        super.onError(tapsellPlusErrorModel);
                        showLogToDeveloper(tapsellPlusErrorModel.toString(), Log.ERROR);
                    }
                });

        showButton.setEnabled(false);
    }

    private void showLogToDeveloper(String message, int logLevel) {

        switch (logLevel) {
            case Log.DEBUG:

                Log.d(TAG, message);
                break;

            case Log.ERROR:

                Log.e(TAG, message);
                break;
        }

        logTextView.append("\n".concat(message));
    }
}
