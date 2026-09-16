package ir.tapsell.plussample.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plus.model.TapsellPlusAdModel;
import ir.tapsell.plussample.android.adapter.NativeBannerAdapter;
import ir.tapsell.plussample.android.enums.ListItemType;
import ir.tapsell.plussample.android.model.ItemList;
import ir.tapsell.plussample.android.model.Zone;
import ir.tapsell.plussample.android.model.ZoneType;
import ir.tapsell.plussample.android.util.ConfigProvider;
import ir.tapsell.plussample.android.util.ZoneSpinner;

public class NativeBannerInListActivity extends AppCompatActivity {

    private final int PAGE_SIZE = 20;
    private int currentPage = 0;
    private boolean isLoading = false;
    private ArrayList<ItemList> items = new ArrayList<>();

    private RecyclerView recyclerView;
    private NativeBannerAdapter adapter;
    private Spinner zoneSpinner;
    private List<Zone> zones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_banner_in_list);

        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);

        zoneSpinner = findViewById(R.id.zone_spinner);
        zones = ConfigProvider.zonesFor(this, ZoneType.NATIVE);
        ZoneSpinner.populate(zoneSpinner, zones);

        initList();
        getNativeBannerAd();
        generateItems(0);
    }

    private void initList() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NativeBannerAdapter(this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy < 0) {
                    return;
                }

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        isLoading = true;
                        generateItems(++currentPage);
                    }
                }
            }
        });
    }

    private void generateItems(int page) {
        for (int i = 0; i < PAGE_SIZE; i++) {
            ItemList item = new ItemList();
            item.title = "item " + (page * PAGE_SIZE + i);
            item.listItemType = ListItemType.ITEM;
            items.add(item);
        }

        isLoading = false;

        adapter.updateItem(items);
        getNativeBannerAd();
    }

    private void getNativeBannerAd() {
        Zone zone = ZoneSpinner.selectedZone(zoneSpinner, zones);
        if (zone == null) {
            Log.d("", "error: no zone selected");
            return;
        }
        TapsellPlus.requestNativeAd(
                this, zone.id, new AdRequestCallback() {

                    @Override
                    public void response(TapsellPlusAdModel tapsellPlusAdModel) {
                        super.response(tapsellPlusAdModel);
                        showAd(tapsellPlusAdModel.getResponseId());
                    }

                    @Override
                    public void error(String message) {
                        Log.d("", "error: ");
                    }
                });
    }

    private void showAd(String adIds) {
        ItemList item = new ItemList();
        item.id = adIds;
        item.listItemType = ListItemType.AD;
        items.add(item);
        adapter.updateItem(items);
    }
}
