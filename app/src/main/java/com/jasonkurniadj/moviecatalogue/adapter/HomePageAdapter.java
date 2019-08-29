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

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {

    private ArrayList<ItemModel> modelList;

    public HomePageAdapter(ArrayList<ItemModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_horizontal_list, parent, false);
        return new HomePageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ItemModel model = modelList.get(position);

        if (model.getItemPoster().equals("")) {
            holder.imgPoster.setImageResource(R.drawable.dummy_img);
        }
        else {

            Glide.with(holder.itemView.getContext())
                    .load(model.getItemPoster())
                    .apply(new RequestOptions().override(185, 250))
                    .into(holder.imgPoster);
        }

        holder.tvName.setText(model.getItemName());
        holder.tvRating.setText(model.getItemRating());

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemModel itemModel = new ItemModel();
                itemModel.setItemExternalID(model.getItemExternalID());
                itemModel.setItemType(model.getItemType());
                itemModel.setItemPoster(model.getItemPoster());
                itemModel.setItemTrailer(model.getItemTrailer());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvItem;
        ImageView imgPoster;
        TextView tvName, tvRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvItem = itemView.findViewById(R.id.cv_now_playing_item);
            imgPoster = itemView.findViewById(R.id.img_now_playing_poster);
            tvName = itemView.findViewById(R.id.tv_now_playing_name);
            tvRating = itemView.findViewById(R.id.tv_now_playing_rating);
        }
    }
}
