package he.quanzhao.tinnews.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import he.quanzhao.tinnews.TinNewsApplication;
import he.quanzhao.tinnews.database.AppDatabase;
import he.quanzhao.tinnews.model.Article;
import he.quanzhao.tinnews.model.NewsResponse;
import he.quanzhao.tinnews.network.NewsApi;
import he.quanzhao.tinnews.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    private final NewsApi newsApi;
    private final AppDatabase database;
    private AsyncTask asyncTask;

    public NewsRepository(Context context) {
        //context is the current state the app is running
        this.newsApi = RetrofitClient.newInstance(context).create(NewsApi.class);
        database = TinNewsApplication.getDatabase();
    }

    public LiveData<NewsResponse> getTopHeadlines (String country) {
        MutableLiveData<NewsResponse> topHeadlinesLiveData = new MutableLiveData<>();

        newsApi.getTopHeadlines(country)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.isSuccessful()) {
                            topHeadlinesLiveData.setValue(response.body());
                        }
                        else {
                            topHeadlinesLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        topHeadlinesLiveData.setValue(null);
                    }
                });

        return topHeadlinesLiveData;
    }

    public LiveData<NewsResponse> searchNews(String query) {
        MutableLiveData<NewsResponse> everythingLiveData = new MutableLiveData<>();
        newsApi.getEverything(query, 40)
                .enqueue(
                        new Callback<NewsResponse>() {
                            @Override
                            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                                if (response.isSuccessful()) {
                                    everythingLiveData.setValue(response.body());
                                } else {
                                    everythingLiveData.setValue(null);
                                }
                            }

                            @Override
                            public void onFailure(Call<NewsResponse> call, Throwable t) {
                                everythingLiveData.setValue(null);
                            }
                        });
        return everythingLiveData;
    }

    public LiveData<Boolean> favoriteArticle(Article article) {
        MutableLiveData<Boolean> isSuccessLiveData = new MutableLiveData<>();

        asyncTask =
                new AsyncTask<Void, Void, Boolean> () {
            @Override
            protected Boolean doInBackground (Void... voids) {
                try {
                    database.dao().saveArticle(article);
                }
                catch (Exception e) {
                    Log.e("Test", e.getMessage());
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute (Boolean isSuccess) {
                article.favorite = isSuccess;
                isSuccessLiveData.setValue(isSuccess);
            }
        }.execute();
        return isSuccessLiveData;
    }

    public void onCancel() {
        if(asyncTask != null) {
            asyncTask.cancel(true);
        }
    }

}
