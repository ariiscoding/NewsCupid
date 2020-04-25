package he.quanzhao.tinnews.network;

import he.quanzhao.tinnews.model.NewsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    @GET("top-headlines") //get the parameter (in HTTP) "top-headlines"
    Call<NewsResponse> getTopHeadlines(@Query("Country") String country); //extract the values in query "country=..."

    @GET("everything")
    Call<NewsResponse> getEverything (@Query("q") String query, @Query("pageSize") int pageSize);

}
