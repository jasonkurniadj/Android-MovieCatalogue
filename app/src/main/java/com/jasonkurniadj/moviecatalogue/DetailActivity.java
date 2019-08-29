package com.jasonkurniadj.moviecatalogue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.jasonkurniadj.moviecatalogue.database.DatabaseHelper;
import com.jasonkurniadj.moviecatalogue.model.ItemModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "EXTRA_DATA";
    private static final String LOG_TAG = "JS_LOG";
    private ImageButton ibFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imgPoster;
        final TextView tvName, tvOverview, tvRating, tvReleaseDate;

        imgPoster = findViewById(R.id.img_poster);
        ibFavorite = findViewById(R.id.ib_favorite);
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

        final DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        final Boolean isSaved = helper.checkItem(model.getItemExternalID());

        if (isSaved) {
            ibFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isTempSaved = helper.checkItem(model.getItemExternalID());

                if (isTempSaved) {
                    try {
                        helper.deleteItem(model);
                        ibFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        showSnackbarMessage(getResources().getString(R.string.remove_succeed));
                        Log.i("JS_LOG", "Delete success.");
                    } catch (Exception ex) {
                        showSnackbarMessage(getResources().getString(R.string.remove_failed));
                        Log.i("JS_LOG", "Delete failed.");
                    }
                }
                else {
                    try {
                        Boolean isNotEmpty = helper.insertItem(model);
                        Log.i(LOG_TAG, "OnClick Save to Favorite.");

                        if (isNotEmpty) {
                            ibFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                            showSnackbarMessage(getResources().getString(R.string.add_succeed));
                            Log.i(LOG_TAG, "Insert success.");
                        } else {
                            showSnackbarMessage(getResources().getString(R.string.add_failed));
                            Log.i("JS_LOG", "Insert failed.");
                        }
                    } catch (Exception ex) {
                        showSnackbarMessage("Catch: " + getResources().getString(R.string.add_failed));
                        Log.i("JS_LOG", "Insert failed. SaveException: " + ex.toString());
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void showSnackbarMessage (String message) {
        Snackbar.make(ibFavorite, message, Snackbar.LENGTH_SHORT).show();
    }
}
