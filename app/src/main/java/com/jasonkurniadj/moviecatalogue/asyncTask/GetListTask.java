package com.jasonkurniadj.moviecatalogue.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jasonkurniadj.moviecatalogue.R;
import com.jasonkurniadj.moviecatalogue.fragment.TvShowFragment;
import com.jasonkurniadj.moviecatalogue.fragment.*;
import com.jasonkurniadj.moviecatalogue.adapter.PageAdapter;
import com.jasonkurniadj.moviecatalogue.model.ItemModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetListTask extends AsyncTask<Integer, String, String> {

    private static final String LOG_TAG = "JS_LOG";

    private static final String API_METHOD = "GET";
    private static final String API_KEY = "75103a65bd5c254a1120b467ed4ca64b";
    private static final String API_LANGUAGE = "en-US";
    private static final int API_TIMEOUT = 9500;
    private static final String URL_POSTER_PATH = "https://image.tmdb.org/t/p/w300";

    private Context context;
    private int fragmentIdx;
    private ProgressDialog progress;

    public GetListTask(Context context, int fragmentIdx) {
        this.context = context;
        this.fragmentIdx = fragmentIdx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = new ProgressDialog(context);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected String doInBackground(Integer... integers) {
        try {
            String API_URL = "";

            if (integers[0] == 1) {
                API_URL = "https://api.themoviedb.org/3/discover/movie?";
            }
            else if (integers[0] == 2) {
                API_URL = "https://api.themoviedb.org/3/discover/tv?";
            }

            String _url = API_URL + "api_key=" + API_KEY + "&language=" + API_LANGUAGE ;
            URL url = new URL(_url);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(API_METHOD);
            urlConnection.setReadTimeout(API_TIMEOUT);
            urlConnection.setConnectTimeout(API_TIMEOUT);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder result = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }

                bufferedReader.close();
                inputStreamReader.close();

                return result.toString();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            Log.i(LOG_TAG, "AsyncTask Exception: " + ex.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ArrayList<ItemModel> modelList = new ArrayList<>();

        try {
            JSONObject parentObject = new JSONObject(result);
            JSONArray jsonArray = parentObject.getJSONArray("results");

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                ItemModel model = new ItemModel();
                if (fragmentIdx == 1) {
                    model.setItemName(object.getString("title"));
                    model.setItemReleaseDate(object.getString("release_date"));
                    model.setItemType("Movie");
                }
                else if (fragmentIdx == 2) {
                    model.setItemName(object.getString("name"));
                    model.setItemReleaseDate(object.getString("first_air_date"));
                    model.setItemType("TV Show");
                }

                model.setItemExternalID(object.getString("id"));
                model.setItemPoster(URL_POSTER_PATH + object.getString("poster_path"));
                model.setItemDescription(object.getString("overview"));
                model.setItemRating(object.getString("vote_average"));

                modelList.add(model);

                if (fragmentIdx == 1) {
                    MovieFragment movieFragment = new MovieFragment();
                    movieFragment.setMovieInstance(modelList);
                }
                else if (fragmentIdx == 2) {
                    TvShowFragment tvShowFragment = new TvShowFragment();
                    tvShowFragment.setTvShowInstance(modelList);
                }
            }

            RecyclerView recyclerView = null;
            if (fragmentIdx == 1) {
                recyclerView = ((Activity) context).findViewById(R.id.rv_movie);
            }
            else if (fragmentIdx == 2) {
                recyclerView = ((Activity) context).findViewById(R.id.rv_tv_show);
            }

            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
            }

            recyclerView.setHasFixedSize(true);

            PageAdapter adapter = new PageAdapter(modelList);
            recyclerView.setAdapter(adapter);

            progress.dismiss();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            progress.dismiss();

            Toast toast = Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
