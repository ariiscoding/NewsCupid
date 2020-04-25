package he.quanzhao.tinnews.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import he.quanzhao.tinnews.model.NewsResponse;
import he.quanzhao.tinnews.repository.NewsRepository;

public class HomeViewModel extends ViewModel {
    private final NewsRepository repository;
    private final MutableLiveData<String> countryInput;

    public HomeViewModel (NewsRepository repository) {
        this.repository = repository;
        countryInput = new MutableLiveData<>();
    }

    public void setCountryInput (String country) {
        countryInput.setValue(country);
    }

    public LiveData<NewsResponse> getTopHeadlines() {
        return Transformations.switchMap(countryInput, repository::getTopHeadlines);
    }
}
