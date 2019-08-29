package com.jasonkurniadj.moviecatalogue.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jasonkurniadj.moviecatalogue.R;
import com.jasonkurniadj.moviecatalogue.adapter.HomePageAdapter;
import com.jasonkurniadj.moviecatalogue.fragment.HomeFragment;
import com.jasonkurniadj.moviecatalogue.model.ItemModel;
import com.jasonkurniadj.moviecatalogue.notification.NotificationService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GetHomeListTask extends AsyncTask<Integer, String, ArrayList<String>> {

    private static final String LOG_TAG = "JS_LOG";

    private static final String API_METHOD = "GET";
    private static final String API_KEY = "75103a65bd5c254a1120b467ed4ca64b";
    private static final String API_LANGUAGE = "en-US";
    private static final int API_TIMEOUT = 9500;
    private static final String URL_POSTER_PATH = "https://image.tmdb.org/t/p/w300";

    private int taskID;
    private Context context;
    private ProgressDialog progress;

    public GetHomeListTask(Context context, int taskID) {
        this.context = context;
        this.taskID = taskID;
    }

    public GetHomeListTask(int taskID) {
        this.taskID = taskID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (taskID == 0) {
            progress = new ProgressDialog(context);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
        }
    }

    @Override
    protected ArrayList<String> doInBackground(Integer... integers) {
        try {
            final String API_URL_NOW_PLAYING = "https://api.themoviedb.org/3/movie/now_playing?";
            final String API_URL_RELEASE = "https://api.themoviedb.org/3/discover/movie?";
            final String API_URL_POPULAR_TV_SHOW = "https://api.themoviedb.org/3/tv/popular?";

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());
            Log.i(LOG_TAG, "AsyncTask Get date: " + currentDate);

            String urlNowPlaying = API_URL_NOW_PLAYING + "api_key=" + API_KEY + "&language=" + API_LANGUAGE;
            String urlRelease = API_URL_RELEASE + "api_key=" + API_KEY + "&primary_release_date.gte=" + currentDate + "&primary_release_date.lte=" + currentDate;
            String urlPopularTVShow = API_URL_POPULAR_TV_SHOW + "api_key=" + API_KEY + "&language=" + API_LANGUAGE;

            ArrayList<String> resultList = new ArrayList<>();
            ArrayList<String> urlList = new ArrayList<>();
            urlList.add(urlNowPlaying);
            urlList.add(urlRelease);
//            urlList.add(urlPopularTVShow);

            for (int i=0; i<urlList.size(); i++) {
                URL url = new URL(urlList.get(i));

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(API_METHOD);
                urlConnection.setReadTimeout(API_TIMEOUT);
                urlConnection.setConnectTimeout(API_TIMEOUT);
                urlConnection.connect();

                StringBuilder result = new StringBuilder();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }

                    bufferedReader.close();
                    inputStreamReader.close();
                }

                resultList.add(result.toString());
            }

            return resultList;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            Log.i(LOG_TAG, "Home AsyncTask Exception: " + ex.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<String> resultList) {
        super.onPostExecute(resultList);

        try {
            Log.i(LOG_TAG, "Total result list: " + resultList.size());
            HomeFragment homeFragment = new HomeFragment();

            for (int i=0; i<resultList.size(); i++) {
                ArrayList<ItemModel> modelList = new ArrayList<>();

                try {
                    JSONObject parentObject = new JSONObject(resultList.get(i));
                    JSONArray jsonArray = parentObject.getJSONArray("results");

                    for (int j=0; j<jsonArray.length(); j++) {
                        JSONObject object = jsonArray.getJSONObject(j);

                        ItemModel model = new ItemModel();
                        if (i==0 || i==1) {
                            model.setItemName(object.getString("title"));
                            model.setItemReleaseDate(object.getString("release_date"));
                            model.setItemType("Movie");
                        } else if (i == 2) {
                            model.setItemName(object.getString("name"));
                            model.setItemReleaseDate(object.getString("first_air_date"));
                            model.setItemType("TV Show");
                        }

                        model.setItemExternalID(object.getString("id"));
                        model.setItemPoster(URL_POSTER_PATH + object.getString("poster_path"));
                        model.setItemDescription(object.getString("overview"));
                        model.setItemRating(object.getString("vote_average"));

                        modelList.add(model);

                        if (taskID == 0) {
                            if (i == 0) {
                                homeFragment.setNowPlayingInstance(modelList);
                            } else if (i == 1) {
                                homeFragment.setReleaseInstance(modelList);
                            }
                        }
                        else if (taskID == 1) {
                            homeFragment.setReleaseInstance(modelList);

                            NotificationService service = new NotificationService();
                            service.setReleaseNotification(modelList);
                        }
                    }

                    if (taskID == 0) {
                        RecyclerView recyclerView = null;
                        if (i == 0) {
                            recyclerView = ((Activity) context).findViewById(R.id.rv_now_playing);
                        } else if (i == 1) {
                            recyclerView = ((Activity) context).findViewById(R.id.rv_release);
                        } else if (i == 2) {
                            recyclerView = ((Activity) context).findViewById(R.id.rv_release);
                        }

                        if (i == 0 || i == 1) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        } else {
                            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        }

                        recyclerView.setHasFixedSize(true);

                        if (i == 0 || i == 1) {
                            HomePageAdapter adapter = new HomePageAdapter(modelList);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (taskID == 0) {
                progress.dismiss();
            }
        } catch (Exception ex) {
            if (taskID == 0) {
                progress.dismiss();

                Toast toast = Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
