package com.jasonkurniadj.moviecataloguefavorite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jasonkurniadj.moviecataloguefavorite.model.ItemModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "EXTRA_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imgPoster;
        final TextView tvName, tvOverview, tvRating, tvReleaseDate;

        imgPoster = findViewById(R.id.img_poster);
        tvName = findViewById(R.id.tv_name);
        tvOverview = findViewById(R.id.tv_overview_content);
        tvRating = findViewById(R.id.tv_rating);
        tvReleaseDate = findViewById(R.id.tv_date_content);

        final ItemModel model = getIntent().getParcelableExtra(EXTRA_DATA);

        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MMMM, yyyy");
        String formattedDate = "";
        try {
            formattedDate = myFormat.format(fromUser.parse(model.getItemReleaseDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (model.getItemPoster().equals("")) {
            imgPoster.setImageResource(R.drawable.dummy_img);
        }
        else {
            Glide.with(this)
                    .load(model.getItemPoster())
                    .apply(new RequestOptions().override(145, 200))
                    .into(imgPoster);
        }

        String overview;
        if (model.getItemDescription().equals("")) {
            overview = getResources().getString(R.string.no_overview);
        }
        else {
            overview = model.getItemDescription();
        }

        tvName.setText(model.getItemName());
        tvOverview.setText(overview);
        tvRating.setText(model.getItemRating());
        tvReleaseDate.setText(formattedDate);
    }
}
