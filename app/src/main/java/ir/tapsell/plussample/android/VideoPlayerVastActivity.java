package ir.tapsell.plussample.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdsLoader;

import java.util.List;

import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plus.VastRequestListener;
import ir.tapsell.plussample.android.model.Zone;
import ir.tapsell.plussample.android.model.ZoneType;
import ir.tapsell.plussample.android.util.ConfigProvider;
import ir.tapsell.plussample.android.util.ZoneSpinner;
import ir.tapsell.sdk.preroll.TapsellPrerollAd;

public class VideoPlayerVastActivity extends AppCompatActivity {

    private static final String TAG = "VideoPlayerVastActivity";
    private static final String SAMPLE_VIDEO_URL = "https://storage.backtory.com/tapsell-server/sdk/VASTContentVideo.mp4";

    private TapsellPrerollAd tapsellPrerollAd;

    private VideoView videoView;
    private TextView tvLog;
    private Spinner zoneSpinner;
    private List<Zone> zones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vast_video_player);

        videoView = findViewById(R.id.video_player);
        tvLog = findViewById(R.id.tvLog);

        findViewById(R.id.btnRequest).setOnClickListener(v -> requestSelectedZone());

        zoneSpinner = findViewById(R.id.zone_spinner);
        zones = ConfigProvider.zonesFor(this, ZoneType.PRE_ROLL);
        ZoneSpinner.populate(zoneSpinner, zones);
    }

    private void requestSelectedZone() {
        Zone zone = ZoneSpinner.selectedZone(zoneSpinner, zones);
        if (zone != null) {
            requestAd(zone.id);
        }
    }

    private void requestAd(String zoneId) {
        videoView.setVideoPath(SAMPLE_VIDEO_URL);
        if (tapsellPrerollAd != null) {
            tapsellPrerollAd.destroyAd();
        }
        String tag = TapsellPlus.getVastTag(zoneId);
        tapsellPrerollAd = TapsellPlus.requestVastAd(
                this,
                tag,
                videoView,
                SAMPLE_VIDEO_URL,
                findViewById(R.id.video_player_container),
                findViewById(R.id.companion_ad_slot),
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
        tapsellPrerollAd.requestAd(tag);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tapsellPrerollAd != null) {
            tapsellPrerollAd.resumeAd();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tapsellPrerollAd != null) {
            tapsellPrerollAd.pauseAd();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tapsellPrerollAd != null) {
            tapsellPrerollAd.destroyAd();
        }
    }
}
