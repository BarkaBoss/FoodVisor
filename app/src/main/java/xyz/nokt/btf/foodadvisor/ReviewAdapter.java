package xyz.nokt.btf.foodadvisor;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private Activity activity;
    private List<Comments> reviewList;
    private Context context;

    public ReviewAdapter(Activity activity, List<Comments> reviewList, Context context) {
        this.activity = activity;
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Comments comment = reviewList.get(position);

        holder.rateTitle.setText(comment.getTitle());
        holder.rateBody.setText(comment.getMessage());
    }

    @Override
    public int getItemCount() {
         return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout relativeLayout;
        TextView rateTitle, rateBody;

        public ViewHolder(View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.relaBody);
            rateTitle = itemView.findViewById(R.id.tvRateTitle);
            rateBody = itemView.findViewById(R.id.tvRateRating);
        }
    }
}