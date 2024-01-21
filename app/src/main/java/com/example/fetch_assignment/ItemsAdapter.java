package com.example.fetch_assignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private List<Item> items = new ArrayList<>();
    private List<Item> itemsFiltered = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = itemsFiltered.get(position);
        holder.tvListId.setText("List ID: " + item.getListId());
        holder.tvId.setText("ID: " + item.getId());
        holder.tvName.setText("Name: " + item.getName());
    }

    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }

    public void filterData(int listId) {
        if (listId == -1) {
            itemsFiltered = new ArrayList<>(items);
        } else {
            itemsFiltered.clear();
            for (Item item : items) {
                if (item.getListId() == listId) {
                    itemsFiltered.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvListId, tvId, tvName;

        ViewHolder(View view) {
            super(view);
            tvListId = view.findViewById(R.id.tvListId);
            tvId = view.findViewById(R.id.tvId);
            tvName = view.findViewById(R.id.tvName);
        }
    }

    public void setItems(List<Item> items) {
        this.items = items;
        filterData(-1);
    }
}
