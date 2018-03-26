package sbsolutions.whatmeal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.marcorei.infinitefire.InfiniteFireArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import sbsolutions.adapters.IngredientListAdapter;
import sbsolutions.dataclass.Ingredient;

public class WhatMeanMain extends AppCompatActivity implements IngredientListAdapter.ItemClickListener{

    @Bind(R.id.add_card)
    CardView addCardView;
    @Bind(R.id.main_ingredient_list)
    RecyclerView ingredientListRecycler;
    @Bind(R.id.search_recipe)
    TextView searchRecipe;

    IngredientListAdapter ingredientListAdapter;

    LinearLayoutManager mLayoutManager;
    List<Ingredient> ingredientList = new ArrayList<>();

    private final int INGREDIENT_TAG = 2018;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_mean_main);
        ButterKnife.bind(this);

        addCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(WhatMeanMain.this,AddIngredient.class),INGREDIENT_TAG);
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        ingredientListRecycler.setLayoutManager(mLayoutManager);


        ingredientListAdapter = new IngredientListAdapter(WhatMeanMain.this,ingredientList);
        ingredientListRecycler.setAdapter(ingredientListAdapter);
        ingredientListAdapter.setClickListener(this);

        searchRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipeList = "";
                for(Ingredient ingredient: ingredientList){
                    recipeList += ingredient.getIngredient_name() + " & ";
                }
                recipeList = recipeList.substring(0,recipeList.length() - 3);

                String escapedQuery = null;
                try {
                    escapedQuery = URLEncoder.encode("Recipes with " + recipeList, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_serach:
               ingredientList.clear();
               ingredientListAdapter.notifyDataSetChanged();
               searchRecipe.setVisibility(View.GONE);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INGREDIENT_TAG && resultCode == RESULT_OK){
            final String name = data.getStringExtra("ingredient_name");
            final String pic = data.getStringExtra("ingredient_pic");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean addIngredient = true;
                    for(Ingredient ingredient: ingredientList){
                        if(ingredient.getIngredient_name().equals(name)){
                            addIngredient = false;
                        }
                    }
                    if(addIngredient){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ingredientList.add(new Ingredient(name,pic));
                                ingredientListAdapter.notifyDataSetChanged();
                                searchRecipe.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            }).start();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ingredientList.remove(position);
        ingredientListAdapter.notifyDataSetChanged();
        if(ingredientList.size() == 0){
            searchRecipe.setVisibility(View.GONE);
        }
    }
}
