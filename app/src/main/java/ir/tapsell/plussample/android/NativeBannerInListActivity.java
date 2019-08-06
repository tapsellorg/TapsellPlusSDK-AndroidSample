package ir.tapsell.plussample.android;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plussample.android.adapter.NativeBannerAdapter;
import ir.tapsell.plussample.android.enums.ListItemType;
import ir.tapsell.plussample.android.model.ItemList;

public class NativeBannerInListActivity extends AppCompatActivity {

    private final int PAGE_SIZE = 20;
    private int currentPage = 0;
    private boolean isLoading = false;
    private ArrayList<ItemList> items = new ArrayList<>();

    private RecyclerView recyclerView;
    private NativeBannerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_banner_in_list);

        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);

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
        TapsellPlus.requestNativeBanner(
                this, BuildConfig.TAPSELL_NATIVE_BANNER, new AdRequestCallback() {
                    @Override
                    public void response(String adId) {
                        showAd(adId);
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
