package he.quanzhao.tinnews.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import he.quanzhao.tinnews.R;
import he.quanzhao.tinnews.model.Article;

public class SearchNewsAdapter extends RecyclerView.Adapter<SearchNewsAdapter.SearchNewsViewHolder> {
    private List<Article> articles = new ArrayList<>();
    private LikeListener likeListener;

    public void setLikeListener(LikeListener likeListener) {
        this.likeListener = likeListener;
    }

    public void setArticles(List<Article> articles) {
        this.articles.clear();
        this.articles.addAll(articles);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_news_item, parent, false);
        return new SearchNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchNewsViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.title.setText(article.title);

        if (article.urlToImage==null) {
            holder.newsImage.setImageResource(R.drawable.ic_empty_image);
        } else {
            Picasso.get().load(article.urlToImage).into(holder.newsImage);
        }

        if (article.favorite) {
            holder.favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
            holder.favorite.setOnClickListener(null); //to avoid duplicate favorites
        } else {
            holder.favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            holder.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    article.favorite = true;
                    likeListener.onLike(article);
                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                likeListener.onClick(article);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class SearchNewsViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        ImageView favorite;
        TextView title;


        public SearchNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.image);
            favorite = itemView.findViewById(R.id.favorite);
            title = itemView.findViewById(R.id.title);
        }
    }

    interface LikeListener {
        void onLike(Article article);
        void onClick (Article article);
    }
}
