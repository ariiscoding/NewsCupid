package he.quanzhao.tinnews.ui.save;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import he.quanzhao.tinnews.model.Article;
import he.quanzhao.tinnews.repository.NewsRepository;

public class SaveViewModel extends ViewModel {
    private final NewsRepository repository;

    public SaveViewModel (NewsRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Article>> getAllSavedArticles() {
        return repository.getAllSavedArticles();
    }

    public void deleteSavedArticle (Article article) {
        repository.deleteSavedArticle(article);
    }

    public void onCancel() {
        repository.onCancel();
    }
}
