package com.jasonkurniadj.moviecatalogue.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jasonkurniadj.moviecatalogue.DetailActivity;
import com.jasonkurniadj.moviecatalogue.R;
import com.jasonkurniadj.moviecatalogue.model.ItemModel;

import java.util.ArrayList;

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.FavoriteViewHolder> {

    private ArrayList<ItemModel> modelList;

    public FavoriteRecyclerViewAdapter(ArrayList<ItemModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public FavoriteRecyclerViewAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav_list, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRecyclerViewAdapter.FavoriteViewHolder holder, int position) {
        final ItemModel model = modelList.get(position);

        if (model.getItemPoster().equals("")) {
            holder.imgPoster.setImageResource(R.drawable.dummy_img);
        }
        else {
            Glide.with(holder.itemView.getContext())
                    .load(model.getItemPoster())
                    .apply(new RequestOptions().override(115, 200))
                    .into(holder.imgPoster);
        }

        holder.tvName.setText(model.getItemName());
        holder.tvOverview.setText(model.getItemDescription());

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemModel itemModel = new ItemModel();
                itemModel.setItemExternalID(model.getItemExternalID());
                itemModel.setItemPoster(model.getItemPoster());
                itemModel.setItemName(model.getItemName());
                itemModel.setItemDescription(model.getItemDescription());
                itemModel.setItemRating(model.getItemRating());
                itemModel.setItemReleaseDate(model.getItemReleaseDate());

                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_DATA, itemModel);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        CardView cvItem;
        ImageView imgPoster;
        TextView tvName, tvOverview;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            cvItem = itemView.findViewById(R.id.cv_fav_item);
            imgPoster = itemView.findViewById(R.id.img_fav_poster);
            tvName = itemView.findViewById(R.id.tv_fav_name);
            tvOverview = itemView.findViewById(R.id.tv_fav_overview);
        }
    }
}
