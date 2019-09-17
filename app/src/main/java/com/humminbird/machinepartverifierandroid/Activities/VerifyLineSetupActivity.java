package com.humminbird.machinepartverifierandroid.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.humminbird.machinepartverifierandroid.Adapter.DataAdapter;
import com.humminbird.machinepartverifierandroid.DataClasses.PartReel;
import com.humminbird.machinepartverifierandroid.Interfaces.CustomGridLayoutManager;
import com.humminbird.machinepartverifierandroid.R;
import com.humminbird.machinepartverifierandroid.Utilities.AnimationAssistant;

import java.util.ArrayList;

public class VerifyLineSetupActivity extends AppCompatActivity {
    public final ArrayList<PartReel> parts = new ArrayList<>();
    public int position = 1;

    public static AnimationAssistant assistant;

    BroadcastReceiver br = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_line_setup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assistant = new AnimationAssistant(this);

        if(br == null){
            br = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(intent.getAction() != null && intent.getAction().equals("GetBcde")){
                        IntentIntegrator BarcodeScanner = new IntentIntegrator(VerifyLineSetupActivity.this);
                        BarcodeScanner.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                        BarcodeScanner.setPrompt("Scan Reel ID Barcode...");
                        BarcodeScanner.setCameraId(0);
                        BarcodeScanner.setBeepEnabled(true);
                        BarcodeScanner.initiateScan();
                    }
                }
            };
            registerReceiver(br,new IntentFilter("GetBcde"));
        }

        loadParts();

        MaterialCardView captureButon = findViewById(R.id.mcvCapture);
        assistant.animateView(captureButon, AnimationAssistant.AnimationGroup.Rotate, AnimationAssistant.AnimationPhase.In);
        captureButon.setVisibility(View.VISIBLE);
        View clickable = findViewById(R.id.clickable);
        clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Capture the next barcode.
                sendBroadcast(new Intent("GetBcde"));
            }
        });
        captureButon.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(parts.size() == 0){


            //TODO: Replace with the logic
            PartReel reel = new PartReel("Verified parts will appear here.","");
            reel.setVerified(true);
            parts.add(reel);

            parts.add(new PartReel("F-5","640073-1"));
            parts.add(new PartReel("F-7","668003-000"));
            parts.add(new PartReel("F-8","668032-1203"));
            parts.add(new PartReel("F-9","668032-1695"));
            parts.add(new PartReel("F-11","668032-3012"));
            parts.add(new PartReel("F-12","664199-010"));
            parts.add(new PartReel("F-13","664206-224"));
            parts.add(new PartReel("F-14","664152-470"));
            parts.add(new PartReel("F-15","668032-3302"));
            parts.add(new PartReel("F-16","668032-5111"));


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    CustomGridLayoutManager llmMain;

    private void loadParts() {


        DataAdapter adt = new DataAdapter(parts);
        RecyclerView rvContentRecycler = findViewById(R.id.rvPartReelRecycler);
        rvContentRecycler.setAdapter(adt);

        llmMain = new CustomGridLayoutManager(this){
        };
        rvContentRecycler.setLayoutManager(llmMain);
        rvContentRecycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //parts.get(0).setVerified(true);
            }
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(scanResult != null) {
            if(scanResult.getContents() == null) {
                Log.d("MainActivity","Scan failure...");
            } else {
                Log.d("MainActivity", "Scan Success!!!");
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (v != null) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                } else {
                    //deprecated in API 26
                    if (v != null) {
                        v.vibrate(500);
                    }
                }

                PartReel reel = parts.get(position);
                if(reel.getFeederPositionNum().equals(scanResult.getContents())){
                    position++;
                    moveToPos(position);
                } else {
                    //Error
                    reel.setVerified(false);
                    Toast.makeText(this, "Validation failure!", Toast.LENGTH_SHORT).show();
                    Log.e("Validation Failure","Expected: "+reel.getFeederPositionNum()+" but got: "+scanResult.getContents());
                }
            }
        }
    }

    private void moveToPos(int position){
        int offset = (position-1);
        parts.get(offset).setVerified(true);
        Toast.makeText(this, "Validated position "+offset, Toast.LENGTH_SHORT).show();
        llmMain.setScrollEnabled(true);
        llmMain.scrollToPosition(offset+2);
        llmMain.setScrollEnabled(false);
    }

}
