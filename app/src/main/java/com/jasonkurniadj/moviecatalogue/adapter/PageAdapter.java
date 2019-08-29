package com.jasonkurniadj.moviecatalogue.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jasonkurniadj.moviecatalogue.DetailActivity;
import com.jasonkurniadj.moviecatalogue.R;
import com.jasonkurniadj.moviecatalogue.model.ItemModel;

import java.util.ArrayList;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.ViewHolder> implements Filterable {

    private ArrayList<ItemModel> listModel;
    private ArrayList<ItemModel> listModelFilter;

    public PageAdapter(ArrayList<ItemModel> listModel) {
        this.listModel = listModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ItemModel model = listModel.get(i);

        if (model.getItemPoster().equals("")) {
            viewHolder.imgPoster.setImageResource(R.drawable.dummy_img);
        }
        else {
            Glide.with(viewHolder.itemView.getContext())
                    .load(model.getItemPoster())
                    .apply(new RequestOptions().override(350, 550))
                    .into(viewHolder.imgPoster);
        }

        viewHolder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemModel itemModel = new ItemModel();
                itemModel.setItemExternalID(model.getItemExternalID());
                itemModel.setItemType(model.getItemType());
                itemModel.setItemPoster(model.getItemPoster());
                itemModel.setItemName(model.getItemName());
                itemModel.setItemDescription(model.getItemDescription());
                itemModel.setItemRating(model.getItemRating());
                itemModel.setItemReleaseDate(model.getItemReleaseDate());

                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_DATA, itemModel);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listModel.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String inputSearch = charSequence.toString();

                if (inputSearch.isEmpty()) {
                    listModelFilter = listModel;
                }
                else {
                    ArrayList<ItemModel> filteredList = new ArrayList<>();

                    for (ItemModel row : listModel) {
                        if (row.getItemName().toLowerCase().contains(inputSearch.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    listModelFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listModelFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listModelFilter = (ArrayList<ItemModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvItem;
        ImageView imgPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvItem = itemView.findViewById(R.id.cv_item);
            imgPoster = itemView.findViewById(R.id.img_poster);
        }
    }
}
