package ir.tapsell.plussample.android;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import ir.tapsell.plus.TapsellPlus;

public class VastActivity extends AppCompatActivity implements AdEvent.AdEventListener,
        AdErrorEvent.AdErrorListener {

    private static final String TAG = "VastActivity";

    private TextView tvLog;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ImaAdsLoader adsLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vast);

        initUI();

        ImaSdkFactory sdkFactory = ImaSdkFactory.getInstance();
        ImaSdkSettings settings = sdkFactory.createImaSdkSettings();
        settings.setLanguage("fa");

        // Create an AdsLoader.
        adsLoader = new ImaAdsLoader.Builder(/* context= */ this)
                .setImaSdkSettings(settings)
                .setAdEventListener(this)
                .setAdErrorListener(this)
                .build();
    }

    private void initUI() {

        playerView = findViewById(R.id.player_view);
        tvLog = findViewById(R.id.tvLog);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    protected void onDestroy() {
        adsLoader.release();

        super.onDestroy();
    }

    private void releasePlayer() {
        adsLoader.setPlayer(null);
        playerView.setPlayer(null);
        player.release();
        player = null;
    }

    private void initializePlayer() {
        // Set up the factory for media sources, passing the ads loader and ad view providers.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)));

        MediaSourceFactory mediaSourceFactory =
                new DefaultMediaSourceFactory(dataSourceFactory)
                        .setAdsLoaderProvider(unusedAdTagUri -> adsLoader)
                        .setAdViewProvider(playerView);

        // Create a SimpleExoPlayer and set it as the player for content and ads.
        player = new SimpleExoPlayer.Builder(this).setMediaSourceFactory(mediaSourceFactory).build();
        playerView.setPlayer(player);
        adsLoader.setPlayer(player);

        // Create the MediaItem to play, specifying the content URI and ad tag URI.
        Uri contentUri = Uri.parse("https://storage.backtory.com/tapsell-server/sdk/VASTContentVideo.mp4");
        Uri adTagUri = Uri.parse(TapsellPlus.getVastTag(BuildConfig.TAPSELL_VAST_PREROLL));
        MediaItem mediaItem = new MediaItem.Builder().setUri(contentUri).setAdTagUri(adTagUri).build();

        // Prepare the content and ad to be played with the SimpleExoPlayer.
        player.setMediaItem(mediaItem);
        player.prepare();

        // Set PlayWhenReady. If true, content and ads will autoplay.
        player.setPlayWhenReady(false);
    }

    /**
     * AdErrorListener implementation
     **/
    @Override
    public void onAdError(AdErrorEvent event) {

        tvLog.append(event.getError().getMessage() + "\n");
        Log.e(TAG, "Ad Error: " + event.getError().getMessage());
    }

    /**
     * AdEventListener implementation
     **/
    @Override
    public void onAdEvent(AdEvent event) {
        switch (event.getType()) {
            case AD_PROGRESS:
                // Do nothing or else log are filled by these messages.
                break;
            default:
                tvLog.append(event.getType().name() + "\n");
                break;
        }
    }
}