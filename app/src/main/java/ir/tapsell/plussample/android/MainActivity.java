package ir.tapsell.plussample.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plus.TapsellPlusBannerType;

public class MainActivity extends AppCompatActivity {
    private View btRewardedVideo;
    private View btNativeBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TapsellPlus.initialize(this, BuildConfig.TAPSELL_KEY);

        init();

        TapsellPlus.showBannerAd(
                this, findViewById(R.id.standardBanner),
                BuildConfig.TAPSELL_STANDARD_BANNER,
                TapsellPlusBannerType.BANNER_320x50);
    }

    private void init() {
        btRewardedVideo = findViewById(R.id.btRewardedVideo);
        btNativeBanner = findViewById(R.id.btNativeBanner);

        btRewardedVideo.setOnClickListener(v -> startActivity(RewardedVideoActivity.class));
        btNativeBanner.setOnClickListener(v -> startActivity(NativeBannerActivity.class));
    }

    private void startActivity(Class cla) {
        Intent intent = new Intent(this, cla);
        startActivity(intent);
    }
}
