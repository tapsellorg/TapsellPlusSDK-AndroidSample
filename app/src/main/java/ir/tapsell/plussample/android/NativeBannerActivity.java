package ir.tapsell.plussample.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ir.tapsell.plus.AdHolder;
import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.AdShowListener;
import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plus.model.TapsellPlusAdModel;
import ir.tapsell.plus.model.TapsellPlusErrorModel;
import ir.tapsell.plussample.android.model.Zone;
import ir.tapsell.plussample.android.model.ZoneType;
import ir.tapsell.plussample.android.util.ConfigProvider;
import ir.tapsell.plussample.android.util.ZoneSpinner;

public class NativeBannerActivity extends AppCompatActivity {

    private static final String TAG = "NativeBannerActivity";
    FrameLayout adContainer;
    private AdHolder adHolder;
    private Spinner zoneSpinner;
    private List<Zone> zones;
    private String responseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_native_banner);

        init();
    }

    private void init() {
        adContainer = findViewById(R.id.adContainer);

        adHolder = TapsellPlus.createAdHolder(
                this, adContainer, ir.tapsell.plus.R.layout.native_banner);

        Button requestButton = findViewById(R.id.request_button);
        Button showButton = findViewById(R.id.show_button);
        Button destroyButton = findViewById(R.id.destroy_button);
        requestButton.setOnClickListener(v -> requestSelectedZone());
        showButton.setOnClickListener(v -> showAd());
        destroyButton.setOnClickListener(v -> destroyAd());

        zoneSpinner = findViewById(R.id.zone_spinner);
        zones = ConfigProvider.zonesFor(this, ZoneType.NATIVE);
        ZoneSpinner.populate(zoneSpinner, zones);
    }

    private void requestSelectedZone() {
        Zone zone = ZoneSpinner.selectedZone(zoneSpinner, zones);
        if (zone != null) {
            requestAd(zone.id);
        }
    }

    private void requestAd(String zoneId) {
        TapsellPlus.requestNativeAd(
                this,
                zoneId,
                new AdRequestCallback() {
                    @Override
                    public void response(TapsellPlusAdModel tapsellPlusAdModel) {
                        super.response(tapsellPlusAdModel);
                        if (isDestroyed())
                            return;
                        Log.d(TAG, "Ad Response");
                        responseId = tapsellPlusAdModel.getResponseId();
                    }

                    @Override
                    public void error(@NonNull String message) {
                        if (isDestroyed())
                            return;

                        Log.e(TAG, "error: " + message);
                    }
                });
    }

    private void showAd() {
        TapsellPlus.showNativeAd(this, responseId, adHolder,
                new AdShowListener() {
                    @Override
                    public void onOpened(TapsellPlusAdModel tapsellPlusAdModel) {
                        super.onOpened(tapsellPlusAdModel);
                        Log.d(TAG, "Ad Open");
                    }

                    @Override
                    public void onError(TapsellPlusErrorModel tapsellPlusErrorModel) {
                        super.onError(tapsellPlusErrorModel);
                        Log.e("onError", tapsellPlusErrorModel.toString());
                    }
                });
    }

    private void destroyAd() {
        TapsellPlus.destroyNativeBanner(NativeBannerActivity.this, responseId);
    }

    @Override
    protected void onDestroy() {
        destroyAd();
        super.onDestroy();
    }
}
