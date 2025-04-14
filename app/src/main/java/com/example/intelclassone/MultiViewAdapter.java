package com.example.intelclassone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MultiViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MultiViewItem> itemList;

    public MultiViewAdapter(List<MultiViewItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == MultiViewItem.TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == MultiViewItem.TYPE_CONTENT) {
            View view = inflater.inflate(R.layout.item_content, parent, false);
            return new ContentViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_ad, parent, false);
            return new AdViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MultiViewItem item = itemList.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).textView.setText(item.text);
        } else if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).textView.setText(item.text);
        } else if (holder instanceof AdViewHolder) {
            ((AdViewHolder) holder).textView.setText("Ad: " + item.text);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        HeaderViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.headerText);
        }
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ContentViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.contentText);
        }
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        AdViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.adText);
        }
    }
}

