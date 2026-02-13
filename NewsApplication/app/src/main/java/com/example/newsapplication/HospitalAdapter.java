package com.example.newsapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {
    private List<Hospitals> hospitals;
    private Context context; // To use for Intent

    // Constructor with Context parameter
    public HospitalAdapter(List<Hospitals> hospitals, Context context) {
        this.hospitals = hospitals;
        this.context = context;
    }

    public void setData(List<Hospitals> hospitals) {
        this.hospitals = hospitals;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hospitals hospital = hospitals.get(position);
        holder.nameTextView.setText(hospital.getName());
        holder.addressTextView.setText(hospital.getAddress());
        holder.addressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse(hospital.getGoogleMapsLink());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                } else {
                    Toast.makeText(context, "Google Maps app not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.numberOfBedsTextView.setText("Number of Beds: " + hospital.getNumberOfBeds());
        holder.vaccineAvailableTextView.setText("Vaccine Available: " + (hospital.isVaccineAvailable() ? "Yes" : "No"));
        holder.openStatusTextView.setText("Hours: " + hospital.getopenStatus());

        Glide.with(holder.itemView.getContext())
                .load(hospital.getImageUrl())
                .into(holder.hospitalImageView);
    }

    @Override
    public int getItemCount() {
        return hospitals != null ? hospitals.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, addressTextView, numberOfBedsTextView, vaccineAvailableTextView, openStatusTextView;
        public ImageView hospitalImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            hospitalImageView = itemView.findViewById(R.id.hospitalImage);
            nameTextView = itemView.findViewById(R.id.name);
            addressTextView = itemView.findViewById(R.id.address);
            numberOfBedsTextView = itemView.findViewById(R.id.numberOfBeds);
            vaccineAvailableTextView = itemView.findViewById(R.id.vaccineAvailable);
            openStatusTextView = itemView.findViewById(R.id.openStatus);
        }
    }
}
