package ir.tapsell.plussample.android.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.tapsell.plus.AdHolder;
import ir.tapsell.plus.AdShowListener;
import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plussample.android.R;
import ir.tapsell.plussample.android.enums.ListItemType;
import ir.tapsell.plussample.android.model.ItemList;

public class NativeBannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_AD = 1;
    private final Activity activity;
    private final LayoutInflater inflater;
    private List<ItemList> items;

    public NativeBannerAdapter(Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        items = new ArrayList<>();
    }

    public void updateItem(List<ItemList> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_AD) {
            return new TapsellListItemAdHolder(
                    inflater.inflate(R.layout.list_ad_item, parent, false), activity);
        } else {
            return new ItemHolder(inflater.inflate(R.layout.list_item, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).listItemType == ListItemType.ITEM) {
            return VIEW_TYPE_ITEM;
        }

        return VIEW_TYPE_AD;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            ((ItemHolder) holder).bindView(position);
            return;
        }

        ((TapsellListItemAdHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvTitle;

        ItemHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        void bindView(int position) {
            tvTitle.setText(items.get(position).title);
        }
    }

    public class TapsellListItemAdHolder extends RecyclerView.ViewHolder {
        private FrameLayout adContainer;
        private AdHolder adHolder;

        TapsellListItemAdHolder(View itemView, Activity activity) {
            super(itemView);
            adContainer = itemView.findViewById(R.id.adContainer);
            adHolder = TapsellPlus.createAdHolder(
                    activity, adContainer, R.layout.native_banner);
        }

        void bindView(int position) {
            TapsellPlus.showNativeAd(
                    activity,
                    items.get(position).id,
                    adHolder,
                    new AdShowListener() {
                    });
        }
    }
}
