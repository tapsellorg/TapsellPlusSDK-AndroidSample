package ir.tapsell.plussample.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plus.TapsellPlusInitListener;
import ir.tapsell.plus.model.AdNetworkError;
import ir.tapsell.plus.model.AdNetworks;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TapsellPlus.initialize(this, BuildConfig.TAPSELL_KEY, new TapsellPlusInitListener() {
            @Override
            public void onInitializeSuccess(AdNetworks adNetworks) {
                Log.d("onInitializeSuccess", adNetworks.name());
            }

            @Override
            public void onInitializeFailed(AdNetworks adNetworks, AdNetworkError adNetworkError) {
                Log.e("onInitializeFailed", "ad network: " + adNetworks.name() + ", error: " + adNetworkError.getErrorMessage());
            }
        });

        TapsellPlus.setGDPRConsent(this, true);

        init();
    }

    private void init() {
        View btRewardedVideo = findViewById(R.id.btRewardedVideo);
        View btInterstitial = findViewById(R.id.btInterstitial);
        View btNativeBanner = findViewById(R.id.btNativeBanner);
        View btStandardBanner = findViewById(R.id.btStandardBanner);
        View btNativeBannerInList = findViewById(R.id.btNativeBannerInList);

        btRewardedVideo.setOnClickListener(v -> startActivity(RewardedVideoActivity.class));
        btInterstitial.setOnClickListener(v -> startActivity(InterstitialActivity.class));
        btNativeBanner.setOnClickListener(v -> startActivity(NativeBannerActivity.class));
        btStandardBanner.setOnClickListener(v -> startActivity(StandardBannerActivity.class));
        btNativeBannerInList.setOnClickListener(v -> startActivity(NativeBannerInListActivity.class));
        findViewById(R.id.btNativeVideo).setOnClickListener(v -> startActivity(NativeVideoActivity.class));
    }

    private void startActivity(Class<?> cla) {
        Intent intent = new Intent(this, cla);
        startActivity(intent);
    }
}
