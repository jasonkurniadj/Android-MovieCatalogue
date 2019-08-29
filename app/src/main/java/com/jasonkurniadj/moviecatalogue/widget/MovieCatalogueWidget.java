package com.jasonkurniadj.moviecatalogue.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.jasonkurniadj.moviecatalogue.MainActivity;
import com.jasonkurniadj.moviecatalogue.R;

public class MovieCatalogueWidget extends AppWidgetProvider {

    private static final String LOG_TAG = "JS_LOG";
    public static final String EXTRA_ITEM = "EXTRA_ITEM";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.i(LOG_TAG, "Widget updateAppWidget");

        Intent serviceIntent = new Intent(context, StackWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        Intent toastIntent = new Intent(context, MainActivity.class);
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(
                context,
                3,
                toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.movie_catalogue_widget);
        views.setRemoteAdapter(R.id.stack_view_widget, serviceIntent);
        views.setPendingIntentTemplate(R.id.stack_view_widget, toastPendingIntent);

        Log.i(LOG_TAG, "Widget end of updateAppWidget");
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(LOG_TAG, "Widget onUpdate. widgetIds: " + appWidgetIds);

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

