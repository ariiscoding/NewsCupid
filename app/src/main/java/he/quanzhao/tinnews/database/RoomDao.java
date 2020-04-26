package he.quanzhao.tinnews.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import he.quanzhao.tinnews.model.Article;

@Dao
public interface RoomDao {

    @Insert
    void saveArticle (Article article);

    @Query("SELECT * FROM article")
    LiveData<List<Article>> getAllArticles();

    @Delete
    void deleteArticle(Article... article); //this means it can take multiple Article objects
}
