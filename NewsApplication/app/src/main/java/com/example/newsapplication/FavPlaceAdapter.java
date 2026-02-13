package com.example.newsapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FavPlaceAdapter extends RecyclerView.Adapter<FavPlaceAdapter.ViewHolder> {
    private List<Destination> destinations;

    public FavPlaceAdapter(List<Destination> destinations) {
        this.destinations = destinations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.destination_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Destination destination = destinations.get(position);
        holder.destinationName.setText(destination.getName() + " - " + destination.getRiskLevel());
        holder.destinationImage.setImageResource(R.drawable.star_filled);
    }


    @Override
    public int getItemCount() {
        return destinations.size();
    }


    public void updateData(List<Destination> newDestinations) {
        destinations.clear();
        if (newDestinations != null) {
            destinations.addAll(newDestinations);
        }
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView destinationName;
        ImageView destinationImage;

        public ViewHolder(View itemView) {
            super(itemView);
            destinationName = itemView.findViewById(R.id.destinationName);
            destinationImage = itemView.findViewById(R.id.destinationImage);
        }
    }
}
