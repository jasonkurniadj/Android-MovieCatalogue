package com.jasonkurniadj.moviecatalogue.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.jasonkurniadj.moviecatalogue.R;
import com.jasonkurniadj.moviecatalogue.database.DatabaseHelper;
import com.jasonkurniadj.moviecatalogue.model.ItemModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = "JS_LOG";
    private final ArrayList<Bitmap> posterList = new ArrayList<>();
    private final Context context;

    public StackRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        DatabaseHelper db = new DatabaseHelper(context);
        ArrayList<ItemModel> modelList = db.getAllItems();

        int totalData = modelList.size();
        Log.i(LOG_TAG, "Favorite data on widget: " + totalData);

        if (totalData > 0) {
            for (int i=0; i<totalData; i++) {
                ItemModel model = modelList.get(i);
                Bitmap bitmap = null;

                try {
                    bitmap = Glide.with(context)
                            .asBitmap()
                            .load(model.getItemPoster())
                            .into(175, 250)
                            .get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                posterList.add(bitmap);
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return posterList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Log.i(LOG_TAG, "Widget getViewAt: " + i);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        remoteViews.setImageViewBitmap(R.id.img_widget, posterList.get(i));

        Bundle bundle = new Bundle();
        bundle.putInt(MovieCatalogueWidget.EXTRA_ITEM, i);
        Intent intent = new Intent();
        intent.putExtras(bundle);

        remoteViews.setOnClickFillInIntent(R.id.img_widget, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
