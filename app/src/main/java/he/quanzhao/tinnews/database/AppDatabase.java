package he.quanzhao.tinnews.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import he.quanzhao.tinnews.model.Article;

@Database(entities = {Article.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RoomDao dao();
}