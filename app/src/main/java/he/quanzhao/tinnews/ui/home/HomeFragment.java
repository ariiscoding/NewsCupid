package he.quanzhao.tinnews.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mindorks.placeholderview.SwipeDecor;

import he.quanzhao.tinnews.R;
import he.quanzhao.tinnews.databinding.FragmentHomeBinding;
import he.quanzhao.tinnews.model.Article;
import he.quanzhao.tinnews.repository.NewsRepository;
import he.quanzhao.tinnews.repository.NewsViewModelFactory;

import static android.widget.Toast.LENGTH_SHORT;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements TinNewsCard.OnSwipeListener {

    private HomeViewModel viewModel;
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }


    //as view is being created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    //after view is created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.swipeView
                .getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor().setPaddingTop(20)
                .setRelativeScale(0.01f));

        binding.rejectBtn.setOnClickListener(v -> binding.swipeView.doSwipe(false));
        binding.acceptBtn.setOnClickListener(v -> binding.swipeView.doSwipe(true));

        NewsRepository repository = new NewsRepository(getContext());
        viewModel = new ViewModelProvider(this, new NewsViewModelFactory(repository)).get(HomeViewModel.class);
        //we use ViewModelProvider (factory pattern) to manage the lifecycle of the object

        viewModel.setCountryInput("us");

        viewModel.getTopHeadlines()
                .observe(getViewLifecycleOwner(),
                        newsResponse -> {
                    if (newsResponse != null) {
                        Log.d("HomeFragment", newsResponse.toString());
                        for(Article article : newsResponse.articles) {
                            TinNewsCard tinNewsCard = new TinNewsCard(article, this);
                            binding.swipeView.addView(tinNewsCard);
                        }
                    }
                        });


            viewModel.onFavorite().observe(getViewLifecycleOwner(), isSuccess -> {
                if (isSuccess) {
                    Toast.makeText(getContext(), "Success", LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "You might have like this before", LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void onLike(Article article) {
        viewModel.setFavoriteArticleInput(article);
    }

    @Override
    public void onDislike(Article article) {
        if(binding.swipeView.getChildCount() < 3) { //if the view has fewer than 3 elements, call the API again.
            viewModel.setCountryInput("us");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.onCancel();
    }
}
