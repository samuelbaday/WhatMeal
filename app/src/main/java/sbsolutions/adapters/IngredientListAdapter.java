package sbsolutions.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import sbsolutions.whatmeal.R;
import sbsolutions.dataclass.Ingredient;
/**
 * Created by Code Breaker on 3/22/2018.
 */

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.ViewHolder> {

    private List<Ingredient> mData = Collections.emptyList();
    private LayoutInflater mInflater;
    private Context context;

    public IngredientListAdapter(Context context, List<Ingredient> mData){
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.context = context;

    }

    @Override
    public IngredientListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
//        if(isIngredient){
//            view = mInflater.inflate(R.layout.card_ingredient, parent, false);
//        } else {
//
//        }
        view = mInflater.inflate(R.layout.card_add_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientListAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = mData.get(position);

//        if(isIngredient){
//            holder.ingredientName.setText(ingredient.getIngredient_name());
//            Glide.with(context)
//                    .load(ingredient.getPic_url())
//                    .into(holder.ingredientPic);
//        } else {
//            holder.ingredientName.setText(R.string.add_ingredient);
//        }
        holder.ingredientName.setText(ingredient.getIngredient_name());
//        Glide.with(context)
//                .load(ingredient.getPic_url())
//                .into(holder.ingredientPic);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ingredientPic;
        public TextView ingredientName;

        public ViewHolder(View itemView) {
            super(itemView);

            ingredientPic = (ImageView) itemView.findViewById(R.id.ingredient_pic);
            ingredientName = (TextView) itemView.findViewById(R.id.ingredient_name);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
