package com.example.codeplayer.rahat_cfd;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SelfHelpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<HelpModel> list;


    public SelfHelpAdapter(List<HelpModel> list) {
        this.list = list;
    }

    @Override
    public RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.selfhelpitem, parent, false);
        return new RecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final HelpModel helpModel  = list.get(i);

//        viewHolder = (RecViewHolder)viewHolder;

    ((RecViewHolder) viewHolder).bind(helpModel);

        viewHolder.itemView.setOnClickListener(v -> {
            boolean expanded = helpModel.isExpanded();
            helpModel.setExpanded(!expanded);
            notifyItemChanged(i);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class RecViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView genre;
        private TextView year;
        private View subItem;

        public RecViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.disaster_title);
            genre = itemView.findViewById(R.id.sub_item_information);
            year = itemView.findViewById(R.id.sub_item_other_information);
            subItem = itemView.findViewById(R.id.sub_item);
        }

        private void bind(HelpModel helpModel) {
            boolean expanded = helpModel.isExpanded();

            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

            title.setText(helpModel.getTitle());
            genre.setText("" + helpModel.getInformation());
        }
    }

}
