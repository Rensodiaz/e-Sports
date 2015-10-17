package news.esports.com.esports.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import news.esports.com.esports.Helpers.Environments;
import news.esports.com.esports.MainActivity;
import news.esports.com.esports.R;
import news.esports.com.esports.models.Collection1;

/**
 * Created by Renso on 9/12/2015.
 */
public class GridAdapter  extends RecyclerView.Adapter<GridAdapter.ViewHolder> implements Environments {
    private final String tag = this.getClass().getSimpleName();
    @VisibleForTesting
    private List<Collection1> mItems;
    private Context context;

    public GridAdapter(Context context, List<Collection1> mItems) {
        this.mItems = mItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final String thumbnail =  mItems.get(position).getImage().getSrc();
        //Font
        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/DroidSerif_Regular.ttf");
        viewHolder.newsTitle.setText(mItems.get(position).getTitle().getText());
        viewHolder.newsTitle.setTypeface(myTypeface);
        if (thumbnail.isEmpty()){
            viewHolder.imgThumbnail.setImageResource(R.drawable.headlines);
        }else {
            Picasso.with(context).load(thumbnail).into(viewHolder.imgThumbnail);
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imgThumbnail;
        public TextView newsTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            newsTitle = (TextView)itemView.findViewById(R.id.news_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //set the fragment of feeds
            if (context instanceof MainActivity) {
                final String title = mItems.get(getLayoutPosition()).getTitle().getText();
                final String link = mItems.get(getLayoutPosition()).getTitle().getHref();
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.switchFragment(title, link);
            }
        }
    }
}