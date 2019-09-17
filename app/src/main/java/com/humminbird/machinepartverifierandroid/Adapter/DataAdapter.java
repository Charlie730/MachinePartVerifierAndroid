package com.humminbird.machinepartverifierandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.humminbird.machinepartverifierandroid.Activities.VerifyLineSetupActivity;
import com.humminbird.machinepartverifierandroid.DataClasses.PartReel;
import com.humminbird.machinepartverifierandroid.R;
import com.humminbird.machinepartverifierandroid.Utilities.AnimationAssistant;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by cwh73 on 8/28/2019
 */


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<PartReel> Objects;
    ViewHolder vh;
    Bitmap success;
    Bitmap error;
    Bitmap camera;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public Context context;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            context = itemView.getContext();
            success = BitmapFactory.decodeResource(context.getApplicationContext().getResources(),R.drawable.ic_check_circle_black_24dp);
            error = BitmapFactory.decodeResource(context.getApplicationContext().getResources(),R.drawable.ic_cancel_black_24dp);
            camera = BitmapFactory.decodeResource(context.getApplicationContext().getResources(),R.drawable.ic_camera_black_24dp);

        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public DataAdapter(ArrayList<PartReel> DataSet) {
        Objects = DataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.recycle_data, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        vh = viewHolder;
        return viewHolder;

    }

    // Replace the contents of a view (invoked by the layout manager)
    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Context context = viewHolder.context;

        final PartReel me = Objects.get(position);
        final AnimationAssistant assistant = VerifyLineSetupActivity.assistant;


        final String name = me.getExpectedNumber();
        String posn = me.getFeederPositionNum();


        me.onStatusChanged(new Runnable() {
            @Override
            public void run() {
                final ImageView status = viewHolder.itemView.findViewById(R.id.imgLarge);
                assistant.animateView(status, AnimationAssistant.AnimationGroup.Rotate, AnimationAssistant.AnimationPhase.Out, new Runnable() {
                    @Override
                    public void run() {
                        if(me.isVerified()){
                            status.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            status.setColorFilter(context.getResources().getColor(android.R.color.holo_green_light));
                        }else {
                            status.setImageResource(R.drawable.ic_cancel_black_24dp);
                            status.setColorFilter(context.getResources().getColor(android.R.color.holo_red_light));
                        }
                        status.setVisibility(View.VISIBLE);
                        assistant.animateView(status, AnimationAssistant.AnimationGroup.Rotate, AnimationAssistant.AnimationPhase.In);
                    }
                });
            }
        });

        //Views
        TextView numberView = viewHolder.itemView.findViewById(R.id.txtSubtitle);
        TextView nameView = viewHolder.itemView.findViewById(R.id.txtHeader);

        numberView.setText(posn);
        nameView.setText(name);

        ImageView status = viewHolder.itemView.findViewById(R.id.imgLarge);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Capture the next barcode.
                context.sendBroadcast(new Intent("GetBcde"));
            }
        });
        if(me.isVerified()){
            status.setImageResource(R.drawable.ic_check_circle_black_24dp);
            status.setColorFilter(context.getResources().getColor(android.R.color.holo_green_light));
        }else {
            status.setImageResource(R.drawable.ic_camera_black_24dp);
            status.setColorFilter(context.getResources().getColor(R.color.colorAccent));
        }
        assistant.animateView(status, AnimationAssistant.AnimationGroup.Rotate, AnimationAssistant.AnimationPhase.In);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return Objects.size();
    }

    public static Bitmap openPhotoImageFromDevice(Uri uri, Context context) {
        File imgFile = new File(String.valueOf(uri));

        if(imgFile.exists()){
            try {
                return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            } catch (OutOfMemoryError d){
                d.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
