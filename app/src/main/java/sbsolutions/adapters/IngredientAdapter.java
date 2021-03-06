package sbsolutions.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marcorei.infinitefire.InfiniteFireArray;
import com.marcorei.infinitefire.InfiniteFireRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import sbsolutions.whatmeal.R;
import sbsolutions.dataclass.Ingredient;

/**
 * Created by Code Breaker on 3/19/2018.
 */

public class IngredientAdapter extends InfiniteFireRecyclerViewAdapter<Ingredient> {

    public static final int VIEW_TYPE_CONTENT = 1;
    public static final int VIEW_TYPE_FOOTER = 2;
    Context context;
    private ItemClickListener mClickListener;
    Ingredient ingredient;
    InfiniteFireArray snapshots2;

    /**
     * This is the view holder for the chat messages.
     */
    public class LetterHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public TextView ingredientName;
        public ImageView ingredientPic;
        public CardView cardView;

        public LetterHolder(View itemView) {
            super(itemView);
            ingredientName = (TextView) itemView.findViewById(R.id.ingredient_name);
            ingredientPic = (ImageView) itemView.findViewById(R.id.ingredient_pic);
            cardView = (CardView) itemView.findViewById(R.id.card_ingredient);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {
            Log.i("INGRED_T",":0" + getAdapterPosition());
//            if (mClickListener != null) mClickListener.onItemClick(view, ingredientName.getText().toString());
        }
    }

    /**
     * This is the view holder for the simple header and footer of this example.
     */
    public static class LoadingHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        }
    }

    private boolean loadingMore = false;

    /**
     * @param snapshots data source for this adapter.
     */
    public IngredientAdapter(Context context, InfiniteFireArray snapshots) {
        super(snapshots, 0, 1);
        this.context = context;
        this.snapshots2 = snapshots;
    }

    /**
     * @return status of load-more loading procedures
     */
    public boolean isLoadingMore() {
        return loadingMore;
    }

    /**
     * This loading status has nothing to do with firebase real-time functionality.
     * It reflects the loading procedure of the first "fresh" data set after a change to the query.
     *
     * @param loadingMore adjust the status of additional loading procedures.
     */
    public void setLoadingMore(boolean loadingMore) {
        if(loadingMore == this.isLoadingMore()) return;
        this.loadingMore = loadingMore;
        notifyItemChanged(getItemCount() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount() - 1) {
            return VIEW_TYPE_FOOTER;
        }
        return VIEW_TYPE_CONTENT;
    }



    public int getSpanSize(int position) {
        if(position == getItemCount() - 1) {
            return 3;
        }
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_CONTENT:
                view = inflater.inflate(R.layout.card_ingredient, parent, false);
                viewHolder = new LetterHolder(view);
                break;
            case VIEW_TYPE_FOOTER:
                view = inflater.inflate(R.layout.list_item_loading, parent, false);
                viewHolder = new LoadingHolder(view);
                break;
            default: throw new IllegalArgumentException("Unknown type");
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch(viewType) {
            case VIEW_TYPE_CONTENT:
                Ingredient ingredient = snapshots.getItem(position - indexOffset).getValue();
                if(ingredient == null) {
                    ingredient = new Ingredient("-","-");
                }
                LetterHolder contentHolder = (LetterHolder) holder;
                String text = ingredient.getIngredient_name();
                if(text.length() == 0) {
                    text = "-";
                }
//                text = text.substring(0,1);
                if(text.equals(" ")) {
                    text = "-";
                }
                contentHolder.ingredientName.setText(text);
                Picasso.with(context).load(ingredient.getPic_url())
                        .error(R.drawable.web_hi_res_512_cook)
                        .placeholder(R.drawable.web_hi_res_512_cook)
                        .into(((LetterHolder) holder).ingredientPic);
                break;
            case VIEW_TYPE_FOOTER:
                LoadingHolder footerHolder = (LoadingHolder) holder;
                footerHolder.progressBar.setVisibility((isLoadingMore()) ? View.VISIBLE : View.GONE);
                break;
            default:
                throw new IllegalArgumentException("Unknown type");
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view,int position);
    }
}
