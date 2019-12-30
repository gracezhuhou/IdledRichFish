package com.sufe.idledrichfish.ui.tag;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.model.Tag;

import java.util.List;

public class ChipsAdapter extends RecyclerView.Adapter<ChipsAdapter.ViewHolder> {

    private List<String> tags;

    public ChipsAdapter(List<String> tags) {
        this.tags = tags;
    }

    public ChipsAdapter(List<String> tags, boolean isShowingPosition) {
        this.tags = tags;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String tag = tags.get(holder.getAdapterPosition());
        holder.text_tag_name.setText(tag);

        holder.button_close.setOnClickListener(v -> {
            tags.remove(holder.getAdapterPosition());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public List<String> getTags(){
        return tags;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView text_tag_name;
        private ImageButton button_close;

        ViewHolder(View itemView) {
            super(itemView);
            text_tag_name = itemView.findViewById(R.id.text_tag_name);
            button_close = itemView.findViewById(R.id.button_close);
        }
    }
}
