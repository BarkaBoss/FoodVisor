package xyz.nokt.btf.foodadvisor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>{

    private Activity activity;
    private List<RestaurantObj> restLists;
    private Context context;
    Bitmap mBitmap;

    FirebaseDatabase fireDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference dbRef = fireDatabase.getReference("restaurants");

    public RestaurantListAdapter(Activity activity, Context context, List<RestaurantObj> restLists)
    {
        this.activity = activity;
        this.context = context;
        this.restLists = restLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rest_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final RestaurantObj restaurantObj = restLists.get(position);

        holder.tvRestPhone.setText(restaurantObj.getRest_phone());
        holder.tvRestAddress.setText(restaurantObj.getRest_address());
        holder.tvRestName.setText(restaurantObj.getRest_name());

        Picasso.get().load(restaurantObj.rest_imgUrl).into(holder.restBanner);

        /*Picasso.get().load(restaurantObj.rest_imgUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.restBanner.setImageBitmap(bitmap);
                        mBitmap = bitmap;
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Toast.makeText(context, "Network too slow",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });*/

        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();*/

                Intent goMapsIntent = new Intent(activity, DetailedRestaurantActivity.class);
                goMapsIntent.putExtra("phone", restaurantObj.getRest_phone());
                goMapsIntent.putExtra("restName", restaurantObj.getRest_name());
                goMapsIntent.putExtra("restAddress", restaurantObj.getRest_address());
                goMapsIntent.putExtra("imageBanner", restaurantObj.getRest_imgUrl());

                context.startActivity(goMapsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restLists.size();
    }

    public void filter(ArrayList<RestaurantObj> listItem)
    {
        restLists = new ArrayList<>();
        restLists.addAll(listItem);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rel;
        TextView tvRestName, tvRestPhone, tvRestAddress;
        ImageView restBanner;
        public ViewHolder(View itemView) {
            super(itemView);

            rel = itemView.findViewById(R.id.relaBody);
            tvRestName = itemView.findViewById(R.id.tvRestName);
            tvRestAddress = itemView.findViewById(R.id.tvRestAddress);
            tvRestPhone = itemView.findViewById(R.id.tvRestPhone);
            restBanner = itemView.findViewById(R.id.restImg);
        }
    }
}
