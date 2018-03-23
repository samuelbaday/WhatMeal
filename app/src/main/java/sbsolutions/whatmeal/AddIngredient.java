package sbsolutions.whatmeal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.marcorei.infinitefire.InfiniteFireArray;

import butterknife.Bind;
import butterknife.ButterKnife;
import sbsolutions.adapters.IngredientAdapter;
import sbsolutions.dataclass.Ingredient;

public class AddIngredient extends AppCompatActivity {

    @Bind(R.id.ingredients_list_recycler)
    RecyclerView recyclerView;

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

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        query = databaseReference
                .child(getString(R.string.firebase_child_ingredients))
                .orderByChild(getString(R.string.firebase_ingredient_name));

        array = new InfiniteFireArray<>(Ingredient.class,query,10,10,false,false);
        IngredientAdapter ingredientListAdapter = new IngredientAdapter(getBaseContext(),array);

        recyclerView.setAdapter(ingredientListAdapter);
        recyclerView.addOnScrollListener(mScrollListener);


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
}
