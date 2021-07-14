package ir.tapsell.plussample.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.AdShowListener;
import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plus.TapsellPlusVideoAdHolder;
import ir.tapsell.plus.model.TapsellPlusAdModel;
import ir.tapsell.plus.model.TapsellPlusErrorModel;

public class NativeVideoActivity extends AppCompatActivity {

    private String responseId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_video);

        findViewById(R.id.request_button).setOnClickListener(v -> requestAd());
        findViewById(R.id.show_button).setOnClickListener(v -> showTheRequestedAd());

    }

    private void requestAd() {
        TapsellPlus.requestNativeVideo(this, BuildConfig.TAPSELL_NATIVE_VIDEO, new AdRequestCallback() {
            @Override
            public void response(TapsellPlusAdModel tapsellPlusAdModel) {
                super.response(tapsellPlusAdModel);
                Toast.makeText(NativeVideoActivity.this, "Ad is ready", Toast.LENGTH_SHORT).show();
                responseId = tapsellPlusAdModel.getResponseId();
            }

            @Override
            public void error(String s) {
                super.error(s);
                Toast.makeText(NativeVideoActivity.this, "Oops! " + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showTheRequestedAd() {
        TapsellPlus.showNativeVideo(this, responseId, new TapsellPlusVideoAdHolder.Builder()
                .setContentViewTemplate(ir.tapsell.sdk.R.layout.tapsell_content_video_ad_template)
                .setAdContainer(findViewById(R.id.adContainer))
                .build(), new AdShowListener() {
            @Override
            public void onOpened(TapsellPlusAdModel tapsellPlusAdModel) {
                super.onOpened(tapsellPlusAdModel);
                Toast.makeText(NativeVideoActivity.this, "Native Video is opened", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onError(TapsellPlusErrorModel tapsellPlusErrorModel) {
                super.onError(tapsellPlusErrorModel);
                Toast.makeText(NativeVideoActivity.this, "Oops! " + tapsellPlusErrorModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}