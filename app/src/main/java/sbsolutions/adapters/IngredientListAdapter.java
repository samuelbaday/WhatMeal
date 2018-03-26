package sbsolutions.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    private ItemClickListener mClickListener;

    public IngredientListAdapter(Context context, List<Ingredient> mData){
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.context = context;

    }

    @Override
    public IngredientListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = mInflater.inflate(R.layout.card_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientListAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = mData.get(position);

        holder.ingredientName.setText(ingredient.getIngredient_name());
        Picasso.with(context).load(ingredient.getPic_url())
                .error(R.drawable.web_hi_res_512_cook)
                .placeholder(R.drawable.web_hi_res_512_cook)
                .into(holder.ingredientPic);

        holder.deleteButton.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ingredientPic;
        public TextView ingredientName;
        public Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            ingredientPic = (ImageView) itemView.findViewById(R.id.ingredient_pic);
            ingredientName = (TextView) itemView.findViewById(R.id.ingredient_name);
            deleteButton = (Button) itemView.findViewById(R.id.delete_recipe);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(view,getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view,int position);
    }
}
