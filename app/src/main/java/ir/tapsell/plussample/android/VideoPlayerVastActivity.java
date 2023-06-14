package ir.tapsell.plussample.android;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdsLoader;

import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plus.VastRequestListener;
import ir.tapsell.sdk.preroll.TapsellPrerollAd;

public class VideoPlayerVastActivity extends AppCompatActivity {

    private static final String TAG = "VideoPlayerVastActivity";
    private static final String SAMPLE_VIDEO_URL = "https://storage.backtory.com/tapsell-server/sdk/VASTContentVideo.mp4";

    private TapsellPrerollAd tapsellPrerollAd;

    private VideoView videoView;
    private TextView tvLog;

    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vast_video_player);

        ViewGroup adUiContainer = findViewById(R.id.video_player_container);
        ViewGroup companionContainer = findViewById(R.id.companion_ad_slot);
        videoView = findViewById(R.id.video_player);

        tvLog = findViewById(R.id.tvLog);
        Button btnRequest = findViewById(R.id.btnRequest);

        tag = TapsellPlus.getVastTag(BuildConfig.TAPSELL_VAST_PREROLL);


        tapsellPrerollAd = TapsellPlus.requestVastAd(
                this,
                tag,
                videoView,
                SAMPLE_VIDEO_URL,
                adUiContainer,
                companionContainer,
                new VastRequestListener() {
                    @Override
                    public void onAdsLoaderCreated(AdsLoader adsLoader) {
                        Log.d(TAG, "onAdsLoaderCreated");
                    }

                    @Override
                    public void onAdEvent(AdEvent adEvent) {
                        Log.d(TAG, "onAdEvent: " + adEvent.getType());
                        tvLog.append("\n" + adEvent.getType());
                    }

                    @Override
                    public void onAdError(AdErrorEvent adErrorEvent) {
                        Log.e(TAG, "onAdError: " + adErrorEvent.getError().getMessage());
                        tvLog.append("\n" + adErrorEvent.getError().getMessage());
                    }
                }
        );

        btnRequest.setOnClickListener(v -> requestAd());
    }


    private void requestAd() {
        videoView.setVideoPath(SAMPLE_VIDEO_URL);
        tapsellPrerollAd.requestAd(tag);
    }

    @Override
    public void onResume() {
        super.onResume();
        tapsellPrerollAd.resumeAd();
    }

    @Override
    public void onPause() {
        super.onPause();
        tapsellPrerollAd.pauseAd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tapsellPrerollAd.destroyAd();
    }
}
