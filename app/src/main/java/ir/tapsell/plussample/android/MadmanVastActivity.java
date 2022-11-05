package ir.tapsell.plussample.android;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flipkart.madman.exo.extension.MadmanAdLoader;
import com.flipkart.madman.listener.AdErrorListener;
import com.flipkart.madman.listener.AdEventListener;
import com.flipkart.madman.okhttp.extension.DefaultNetworkLayer;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import ir.tapsell.plus.TapsellPlus;

public class MadmanVastActivity extends AppCompatActivity implements AdEventListener,
        AdErrorListener, AdsMediaSource.MediaSourceFactory {

    private static final String TAG = "VastActivity";

    private TextView tvLog;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private MadmanAdLoader adsLoader;
    private DataSource.Factory dataSourceFactory = null;
    private boolean isShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vast);

        initUI();

        // Create an AdsLoader.
        getAdsLoader();
    }

    private void getAdsLoader() {
        String tag = TapsellPlus.getVastTag(BuildConfig.TAPSELL_VAST_PREROLL);
        adsLoader = new MadmanAdLoader
                .Builder(this, new DefaultNetworkLayer.Builder().build(this))
                .setAdEventListener(this)
                .setAdErrorListener(this)
                .buildForAdUri(Uri.parse(tag));
    }

    private void initUI() {

        playerView = findViewById(R.id.player_view);
        tvLog = findViewById(R.id.tvLog);
        Button requestButton = findViewById(R.id.btnRequest);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isShowing) {
                    isShowing = true;
                    initializePlayer();

                } else {
                    Toast.makeText(MadmanVastActivity.this, "wait for complete ALL ADS", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        if (!isShowing) return;

        player = ExoPlayerFactory.newSimpleInstance(this);

        // Set up the factory for media sources, passing the ads loader and ad view providers.
        dataSourceFactory =
                new DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)));

        Uri contentUri = Uri.parse("https://storage.backtory.com/tapsell-server/sdk/VASTContentVideo.mp4");
        MediaSource contentMediaSource = buildMediaSource(contentUri);
        MediaSource mediaSourceWithAds = new AdsMediaSource(
                contentMediaSource, this, adsLoader, playerView
        );

        adsLoader.setPlayer(player);
        playerView.setPlayer(player);
        player.prepare(mediaSourceWithAds);
        player.setPlayWhenReady(true);
    }

    /**
     * AdErrorListener implementation
     **/
    @Override
    public void onAdError(@NonNull AdError adError) {

        tvLog.append(adError.getMessage() + "\n");
        Log.e(TAG, "Ad Error: " + adError.getMessage());
    }

    /**
     * AdEventListener implementation
     **/
    @Override
    public void onAdEvent(AdEvent event) {
        switch (event.getType()) {
            case STARTED:
                // Do nothing or else log are filled by these messages.
            case PROGRESS:
                // Do nothing or else log are filled by these messages.
                break;
            case COMPLETED:
            case SKIPPED:

                // To hide companion ads after the ads finished
                //companionViewGroup.setVisibility(View.GONE);
                break;
            case ALL_AD_COMPLETED:
                isShowing = false;
            default:
                tvLog.append(event.getType().name() + "\n");
                break;
        }
    }

    @Override
    public MediaSource createMediaSource(Uri uri) {
        return buildMediaSource(uri);
    }

    @Override
    public int[] getSupportedTypes() {
        return new int[]{
                C.TYPE_DASH,
                C.TYPE_HLS,
                C.TYPE_OTHER
        };
    }

    private MediaSource buildMediaSource(Uri uri) {
        @C.ContentType int type =
                Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        dataSourceFactory
                ).createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        dataSourceFactory
                ).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(
                        dataSourceFactory
                ).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(
                        dataSourceFactory
                ).createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: $type");
        }
    }
}