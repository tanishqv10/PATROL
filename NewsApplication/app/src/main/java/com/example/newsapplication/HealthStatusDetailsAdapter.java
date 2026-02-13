package com.example.newsapplication;

//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.ApImpl.getResources;

import android.content.Context;
//import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class HealthStatusDetailsAdapter extends RecyclerView.Adapter<HealthStatusDetailsAdapter.ViewHolder> {
    private ArrayList<HealthStatusDetails> healthStatusDetails;
    private Context context;
    private static String HEALTHY_HEADING = "You were healthy on ";
    private static String UNHEALTHY_HEADING = "You were unhealthy on ";
    private static String NO_ENTRY_HEADING = "No entry on ";

    public HealthStatusDetailsAdapter(ArrayList<HealthStatusDetails> healthStatusDetails, Context context) {
        this.healthStatusDetails = healthStatusDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public HealthStatusDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkin_rv_item, parent, false);
        return new HealthStatusDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthStatusDetailsAdapter.ViewHolder holder, int position) {
        HealthStatusDetails details = healthStatusDetails.get(position);
        holder.checkinHeadingTV.setText(getHeadingText(details));

        if (details.isHealthy()) {
            holder.checkInIcon.setImageResource(R.drawable.baseline_check_circle_24);
        } else {
            holder.checkInIcon.setImageResource(R.drawable.baseline_cancel_24);
        }
    }

    private String getHeadingText(HealthStatusDetails details) {
        boolean isHealthy = details.isHealthy();
        if (isHealthy) {
            return HEALTHY_HEADING + getDate(details.getTs());
        } else {
            return UNHEALTHY_HEADING + getDate(details.getTs());
        }
    }

    private String getDate(String ts) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set the timezone to UTC because of 'Z' in the timestamp

        try {
            Date date = inputFormat.parse(ts);
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
            outputFormat.setTimeZone(TimeZone.getDefault()); // Set to your local time zone or any specific time zone

            String formattedDate = outputFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public int getItemCount() {
        System.out.println("getItemCount(): " + healthStatusDetails.size());
        return healthStatusDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView checkinHeadingTV;
        private ImageView checkInIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkinHeadingTV = itemView.findViewById(R.id.idTVCheckingHeading);
            checkInIcon = itemView.findViewById(R.id.checkinIcon);
        }
    }
}
