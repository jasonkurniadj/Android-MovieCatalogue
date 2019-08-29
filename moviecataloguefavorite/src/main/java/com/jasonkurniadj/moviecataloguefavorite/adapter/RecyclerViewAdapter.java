package com.jasonkurniadj.moviecataloguefavorite.adapter;

import android.content.Intent;
import android.util.Log;
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
import com.jasonkurniadj.moviecataloguefavorite.DetailActivity;
import com.jasonkurniadj.moviecataloguefavorite.R;
import com.jasonkurniadj.moviecataloguefavorite.model.ItemModel;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<ItemModel> modelList;

    public RecyclerViewAdapter(ArrayList<ItemModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
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
                    .apply(new RequestOptions().override(115, 200))
                    .into(holder.imgPoster);

            Log.i("JS_LOG", "Poster URL: " + model.getItemPoster());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvItem;
        ImageView imgPoster;
        TextView tvName, tvOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvItem = itemView.findViewById(R.id.cv_item);
            imgPoster = itemView.findViewById(R.id.img_poster);
            tvName = itemView.findViewById(R.id.tv_name);
            tvOverview = itemView.findViewById(R.id.tv_overview);
        }
    }
}
