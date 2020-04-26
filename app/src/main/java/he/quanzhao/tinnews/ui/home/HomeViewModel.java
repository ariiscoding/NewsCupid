package he.quanzhao.tinnews.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import he.quanzhao.tinnews.model.Article;
import he.quanzhao.tinnews.model.NewsResponse;
import he.quanzhao.tinnews.repository.NewsRepository;

public class HomeViewModel extends ViewModel {
    private final NewsRepository repository;
    private final MutableLiveData<String> countryInput;
    private final MutableLiveData<Article> favoriteArticleInput;

    public HomeViewModel (NewsRepository repository) {
        this.repository = repository;
        countryInput = new MutableLiveData<>();
        favoriteArticleInput = new MutableLiveData<Article>();
    }

    public void setCountryInput (String country) {
        countryInput.setValue(country);
    }

    public LiveData<NewsResponse> getTopHeadlines() {
        return Transformations.switchMap(countryInput, repository::getTopHeadlines);
    }

    public void setFavoriteArticleInput (Article article) {
        favoriteArticleInput.setValue(article);
    }

    public LiveData<Boolean> onFavorite() {
        return Transformations.switchMap(favoriteArticleInput, repository::favoriteArticle);
    }

    public void onCancel() {
        repository.onCancel();
    }
}
