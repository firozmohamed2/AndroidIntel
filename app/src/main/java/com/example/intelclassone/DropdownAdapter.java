package com.example.intelclassone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DropdownAdapter extends RecyclerView.Adapter<DropdownAdapter.ViewHolder> {

    private List<String> itemList;  // List of items

    public DropdownAdapter(List<String> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dropdown, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = itemList.get(position);
        holder.titleText.setText(title);

        // Toggle dropdown visibility
        holder.dropdownToggle.setOnClickListener(v -> {
            if (holder.dropdownContent.getVisibility() == View.GONE) {
                holder.dropdownContent.setVisibility(View.VISIBLE);
                holder.dropdownToggle.setRotation(180);
            } else {
                holder.dropdownContent.setVisibility(View.GONE);
                holder.dropdownToggle.setRotation(0);
            }
        });

        // Dropdown item click listeners
        holder.dropdownItem1.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "Clicked Option 1 of " + title, Toast.LENGTH_SHORT).show()
        );

        holder.dropdownItem2.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "Clicked Option 2 of " + title, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        ImageView dropdownToggle;
        LinearLayout dropdownContent;
        Button dropdownItem1, dropdownItem2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            dropdownToggle = itemView.findViewById(R.id.dropdownToggle);
            dropdownContent = itemView.findViewById(R.id.dropdownContent);
            dropdownItem1 = itemView.findViewById(R.id.dropdownItem1);
            dropdownItem2 = itemView.findViewById(R.id.dropdownItem2);
        }
    }
}

