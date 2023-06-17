package ir.tapsell.plussample.android;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;

import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plus.VastRequestListener;
import ir.tapsell.sdk.preroll.TapsellPrerollAd;
import ir.tapsell.sdk.preroll.ima.ImaAdsLoader;


public class ExoPlayerVastActivity extends AppCompatActivity {

    private static final String TAG = "ExoPlayerVastActivity";
    private static final String SAMPLE_VIDEO_URL = "https://storage.backtory.com/tapsell-server/sdk/VASTContentVideo.mp4";

    private TextView tvLog;
    private StyledPlayerView playerView;
    private ExoPlayer player;
    private ViewGroup adUiContainer;
    private ViewGroup companionContainer;
    private TapsellPrerollAd tapsellPrerollAd;
    private ImaAdsLoader adsLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vast_exo_player);

        initUI();

    }

    private void initUI() {
        playerView = findViewById(R.id.exo_player);
        adUiContainer = findViewById(R.id.video_player_container);
        companionContainer = findViewById(R.id.companion_ad_slot);

        tvLog = findViewById(R.id.tvLog);
        Button requestButton = findViewById(R.id.btnRequest);
        requestButton.setOnClickListener(v -> requestAd());
    }

    private void initializePlayer() {
        if (adsLoader == null) {
            Log.w(TAG, "initializePlayer failed: adsLoader is null");
            return;
        }
        // Set up the factory for media sources, passing the ads loader and ad view providers.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSource.Factory(this,
                        new DefaultHttpDataSource.Factory()
                                .setUserAgent(Util.getUserAgent(this, getString(R.string.app_name))));

        MediaSource.Factory mediaSourceFactory =
                new DefaultMediaSourceFactory(dataSourceFactory)
                        .setLocalAdInsertionComponents(unusedAdTagUri -> adsLoader, playerView);

        // Create an ExoPlayer and set it as the player for content and ads.
        player = new ExoPlayer.Builder(this)
                .setMediaSourceFactory(mediaSourceFactory)
                .build();
        playerView.setPlayer(player);
        adsLoader.setPlayer(player);

        // Set PlayWhenReady. If true, content and ads will autoplay.
        player.setPlayWhenReady(true);

        prepareVideo();
    }

    private void prepareVideo() {

        // Create the MediaItem to play, specifying the content URI and ad tag URI.
        String tag = TapsellPlus.getVastTag(BuildConfig.TAPSELL_VAST_PREROLL);
        Uri contentUri = Uri.parse(SAMPLE_VIDEO_URL);
        Uri adTagUri = Uri.parse(tag);
        MediaItem.AdsConfiguration adsConfiguration = new MediaItem.AdsConfiguration.Builder(adTagUri).build();
        MediaItem mediaItem = new MediaItem.Builder().setUri(contentUri).setAdsConfiguration(adsConfiguration).build();

        // Prepare the content and ad to be played with the SimpleExoPlayer.
        player.setMediaItem(mediaItem);
        player.prepare();
    }

    private void requestAd() {
        tvLog.setText("");
        if (tapsellPrerollAd != null) {
            tapsellPrerollAd.destroyAd();
            releasePlayer();
        }
        tapsellPrerollAd = TapsellPlus.requestVastAd(
                this,
                playerView,
                SAMPLE_VIDEO_URL,
                adUiContainer,
                companionContainer,
                new VastRequestListener() {

                    @Override
                    public void onAdsLoaderCreated(ImaAdsLoader adsLoader) {
                        Log.d(TAG, "onAdsLoaderCreated");
                        adsLoader.release();
                        ExoPlayerVastActivity.this.adsLoader = adsLoader;
                        initializePlayer();
                    }

                    @Override
                    public void onAdEvent(AdEvent adEvent) {
                        Log.i(TAG, "onAdEvent: " + adEvent.getType());
                        tvLog.append(adEvent.getType().name() + "\n");
                    }

                    @Override
                    public void onAdError(AdErrorEvent adErrorEvent) {
                        Log.e(TAG, "onAdError: " + adErrorEvent.getError().getMessage());
                        tvLog.append(adErrorEvent.getError().getMessage() + "\n");
                    }
                });
    }

    private void releasePlayer() {
        Log.d(TAG, "releasePlayer");
        playerView.setPlayer(null);
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player == null) {
            initializePlayer();
        }
        if (playerView != null) {
            playerView.onResume();
        }
        if (tapsellPrerollAd != null) {
            tapsellPrerollAd.resumeAd();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        playerView.onPause();
        if (tapsellPrerollAd != null) {
            tapsellPrerollAd.pauseAd();
        }
    }

    @Override
    protected void onDestroy() {
        tapsellPrerollAd.destroyAd();
        releasePlayer();
        super.onDestroy();
    }
}