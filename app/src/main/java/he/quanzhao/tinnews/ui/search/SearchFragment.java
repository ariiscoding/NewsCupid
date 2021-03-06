package he.quanzhao.tinnews.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import he.quanzhao.tinnews.R;
import he.quanzhao.tinnews.databinding.FragmentSearchBinding;
import he.quanzhao.tinnews.model.Article;
import he.quanzhao.tinnews.repository.NewsRepository;
import he.quanzhao.tinnews.repository.NewsViewModelFactory;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private SearchViewModel viewModel;
    private FragmentSearchBinding binding;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //specifying layout & initializing the view
        SearchNewsAdapter newsAdapter = new SearchNewsAdapter();
        newsAdapter.setLikeListener(new SearchNewsAdapter.LikeListener() {
            @Override
            public void onLike(Article article) {
                viewModel.setFavoriteArticleInput(article);
            }

            @Override
            public void onClick(Article article) {
                //NavHostFragment.findNavController(SearchFragment.this).navigate(R.id.action_title_search_to_detail);
                SearchFragmentDirections.ActionTitleSearchToDetail action = SearchFragmentDirections.actionTitleSearchToDetail();
                action.setArticle(article);
                NavHostFragment.findNavController(SearchFragment.this).navigate(action);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize (int position) {
                return position == 0 ? 2 : 1;
            }
        });
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        binding.recyclerView.setAdapter(newsAdapter);

        binding.searchView.setOnEditorActionListener((v, actionId, event) -> {
            String searchText = binding.searchView.getText().toString();
            if (actionId == EditorInfo.IME_ACTION_DONE && !searchText.isEmpty()) {
                viewModel.setSearchInput(searchText);
                return true;
            } else {
                return false;
            }
        });

        NewsRepository repository = new NewsRepository(getContext());
        viewModel = new ViewModelProvider(this, new NewsViewModelFactory(repository))
                .get(SearchViewModel.class);
        viewModel
                .searchNews()
                .observe(
                        getViewLifecycleOwner(),
                        newsResponse -> {
                            if (newsResponse != null) {
                                newsAdapter.setArticles(newsResponse.articles); //notify the view adapter that data is ready
                                Log.d("SearchFragment", newsResponse.toString());
                            }
                        });
        viewModel.onFavorite().observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                Toast.makeText(requireActivity(), "Success", LENGTH_SHORT).show();
                newsAdapter.notifyDataSetChanged(); //update view
            } else {
                Toast.makeText(requireActivity(), "You might have liked this before", LENGTH_SHORT).show();
            }
        });

    }
}
