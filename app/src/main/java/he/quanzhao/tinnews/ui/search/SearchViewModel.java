package he.quanzhao.tinnews.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import he.quanzhao.tinnews.model.Article;
import he.quanzhao.tinnews.model.NewsResponse;
import he.quanzhao.tinnews.repository.NewsRepository;

public class SearchViewModel extends ViewModel {
    private final NewsRepository repository;
    private final MutableLiveData<String> searchInput;
    private final MutableLiveData<Article> favoriteArticleInput;

    public SearchViewModel(NewsRepository repository) {
        this.repository = repository;
        searchInput = new MutableLiveData<>();
        favoriteArticleInput = new MutableLiveData<Article>();
    }

    public void setSearchInput(String query) {
        searchInput.setValue(query);
    }

    public LiveData<NewsResponse> searchNews() {
        return Transformations.switchMap(searchInput, repository::searchNews);
        //note: repository::searchNews is lambda method reference, equivalent to this:
        //return Transformation.switchMap(searchInput, new Function<String>, LiveData<NewsResponse>>() {
            //@Override
            //public LiveData<NewsResponse> apply (String input) {
            //  return repository.searchNews(input);
            //});
    }

    public LiveData<Boolean> onFavorite() {
        return Transformations.switchMap(favoriteArticleInput, repository::favoriteArticle);
    }

    public void onCancel() {
        repository.onCancel();
    }

    public void setFavoriteArticleInput (Article article) {
        favoriteArticleInput.setValue(article);
    }


}
