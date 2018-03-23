package sbsolutions.whatmeal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.marcorei.infinitefire.InfiniteFireArray;

import butterknife.Bind;
import butterknife.ButterKnife;
import sbsolutions.adapters.IngredientAdapter;
import sbsolutions.dataclass.Ingredient;

public class AddIngredient extends AppCompatActivity implements IngredientAdapter.ItemClickListener{

    @Bind(R.id.ingredients_list_recycler)
    RecyclerView recyclerView;

    @Bind(R.id.progressBarHolder)
    ViewGroup progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    LinearLayoutManager mLayoutManager;
    InfiniteFireArray<Ingredient> array;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);
        ButterKnife.bind(this);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        showLoadingNoDelay();

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        query = databaseReference
                .child(getString(R.string.firebase_child_ingredients))
                .orderByChild(getString(R.string.firebase_ingredient_name));

        array = new InfiniteFireArray<>(Ingredient.class,query,10,10,true,false);
        IngredientAdapter ingredientListAdapter = new IngredientAdapter(getBaseContext(),array);

        recyclerView.setAdapter(ingredientListAdapter);
        ingredientListAdapter.setClickListener(this);
        recyclerView.addOnScrollListener(mScrollListener);

        array.addOnLoadingStatusListener(new InfiniteFireArray.OnLoadingStatusListener() {
            @Override
            public void onChanged(EventType eventType) {
                if(eventType.equals(EventType.Done)){
                    hideLoadingNoDelay();
                }
            }
        });
    }

    RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (array.isLoading())
                return;
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
            if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                //End of list
                Log.i("END_OF_LINE","END_OF_LINE " + array.hasMoreData());
                if(array.hasMoreData()) {
                    Log.i("HAS_MORE_YOW","END_OF_LINE " + array.hasMoreData());
                    array.more();
                } else {
                    Log.i("NO_MORE_JOSE","END_OF_LINE " + array.hasMoreData());
                }

                Log.i("LOADING_STUFF", String.valueOf(array.isLoading()));
            }
        }
    };

    public void showLoadingNoDelay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inAnimation = new AlphaAnimation(0f, 1f);
                        inAnimation.setDuration(200);
                        progressBarHolder.setAnimation(inAnimation);
                        progressBarHolder.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();

    }

    public void hideLoadingNoDelay() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onItemClick(View view, String ingredient) {
        Log.i("INGREDIENT_DET",ingredient);
    }
}
