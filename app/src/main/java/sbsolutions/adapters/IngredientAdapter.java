package sbsolutions.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marcorei.infinitefire.InfiniteFireArray;
import com.marcorei.infinitefire.InfiniteFireRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import sbsolutions.whatmeal.R;
import sbsolutions.dataclass.Ingredient;

/**
 * Created by Code Breaker on 3/19/2018.
 */

public class IngredientAdapter extends InfiniteFireRecyclerViewAdapter<Ingredient> {

    public static final int VIEW_TYPE_CONTENT = 1;
    public static final int VIEW_TYPE_FOOTER = 2;
    Context context;

    /**
     * This is the view holder for the chat messages.
     */
    public static class LetterHolder extends RecyclerView.ViewHolder {
        public TextView ingredientName;
        public ImageView ingredientPic;

        public LetterHolder(View itemView) {
            super(itemView);
            ingredientName = (TextView) itemView.findViewById(R.id.ingredient_name);
            ingredientPic = (ImageView) itemView.findViewById(R.id.ingredient_pic);

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
}
