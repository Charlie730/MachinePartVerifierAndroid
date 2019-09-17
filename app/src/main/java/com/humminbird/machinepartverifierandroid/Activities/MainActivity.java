package com.humminbird.machinepartverifierandroid.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.humminbird.machinepartverifierandroid.Activities.Camera.CameraActivity;
import com.humminbird.machinepartverifierandroid.DataAndPreferences.MainSettings;
import com.humminbird.machinepartverifierandroid.R;
import com.humminbird.machinepartverifierandroid.Utilities.AnimationAssistant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_CONTACT_REQUEST = 5;

    MaterialCardView reset;
    Toolbar toolbar;
    LinearLayout lltContent;
    public static String machineId = null;
    public static boolean hasCameraStarted = false;
    public static String removedPart = null;
    public static String addedPart = null;

    AnimationAssistant assistant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assistant = new AnimationAssistant(this);

        {//Auto Generated Code
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }

        {
            reset = findViewById(R.id.mcvResetButton);
            lltContent = findViewById(R.id.lltContent);
        }

        {//finish transition.
            toolbar.setVisibility(View.VISIBLE);
            reset.setVisibility(View.VISIBLE);
            lltContent.setVisibility(View.VISIBLE);

            assistant.animateView(toolbar, AnimationAssistant.AnimationGroup.Toast, AnimationAssistant.AnimationPhase.In);
            assistant.animateView(reset, AnimationAssistant.AnimationGroup.Entrance, AnimationAssistant.AnimationPhase.Grow);
            assistant.animateView(lltContent, AnimationAssistant.AnimationGroup.Toast, AnimationAssistant.AnimationPhase.In);
        }

        {//begin automation sequence
            if(machineId == null && !hasCameraStarted){
//                Intent i = new Intent(this, CameraActivity.class);
//                i.putExtra("part","Machine ID");
//                startActivityForResult(i,PICK_CONTACT_REQUEST);
//                Toast.makeText(this, "Scan the Machine ID barcode now.", Toast.LENGTH_SHORT).show();

                IntentIntegrator BarcodeScanner = new IntentIntegrator(this);
                BarcodeScanner.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                BarcodeScanner.setPrompt("Scan Machine ID Barcode...");
                BarcodeScanner.setCameraId(0);
                BarcodeScanner.setBeepEnabled(true);
                BarcodeScanner.initiateScan();
                hasCameraStarted = true;
            } else {
                TextView v = findViewById(R.id.mid);
                v.setText(machineId);

                TextView y = findViewById(R.id.pid);
                y.setText(removedPart);

                TextView z = findViewById(R.id.tvPartAdded);
                z.setText(addedPart);
            }

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentIntegrator BarcodeScanner = new IntentIntegrator(MainActivity.this);
                    BarcodeScanner.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                    BarcodeScanner.setPrompt("Scan Machine ID Barcode...");
                    BarcodeScanner.setCameraId(0);
                    BarcodeScanner.setBeepEnabled(true);
                    BarcodeScanner.initiateScan();
                    hasCameraStarted = true;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, MainSettings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
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

                if(machineId == null){
                    //This is the machine id.
                    TextView vi = findViewById(R.id.mid);
                    vi.setText(scanResult.getContents());
                    machineId = scanResult.getContents();

                    IntentIntegrator BarcodeScanner = new IntentIntegrator(this);
                    BarcodeScanner.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                    BarcodeScanner.setPrompt("Scan Part Number Removed Barcode...");
                    BarcodeScanner.setCameraId(0);
                    BarcodeScanner.setBeepEnabled(true);
                    BarcodeScanner.initiateScan();
                } else if(removedPart == null){

                    removedPart = scanResult.getContents();
                    TextView vie = findViewById(R.id.pid);
                    vie.setText(removedPart);

                    IntentIntegrator BarcodeScanner = new IntentIntegrator(this);
                    BarcodeScanner.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                    BarcodeScanner.setPrompt("Scan Part Number Removed Barcode...");
                    BarcodeScanner.setCameraId(0);
                    BarcodeScanner.setBeepEnabled(true);
                    BarcodeScanner.initiateScan();
                } else if(addedPart == null) {
                    addedPart = scanResult.getContents();
                    TextView vie = findViewById(R.id.tvPartAdded);
                    vie.setText(addedPart);
                }
            }
        }

//        if (requestCode == PICK_CONTACT_REQUEST) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                // The barcode has been read.
//
//                if(data.getStringExtra("result")!= null && data.getStringExtra("part")!= null){
//                    if(data.getStringExtra("part").equals("Machine ID")){
//                        //put the result in the right view.
//                        TextView v = findViewById(R.id.mid);
//                        v.setText(data.getStringExtra("result"));
//                        machineId = data.getStringExtra("result");
//                        Toast.makeText(this, "Returned to main with value: "+machineId, Toast.LENGTH_SHORT).show();
//
//                        Intent i = new Intent(this, CameraActivity.class);
//                        i.putExtra("part","Removed Number");
//                        startActivityForResult(i,PICK_CONTACT_REQUEST);
//                        Toast.makeText(this, "Scan the removed # barcode now.", Toast.LENGTH_SHORT).show();
//                    } else if(data.getStringExtra("part").equals("Removed Number")){
//                        TextView v = findViewById(R.id.pid);
//                        v.setText(data.getStringExtra("result"));
//                        removedPart = data.getStringExtra("result");
//                    }
//                }
//            }
//        }
    }
}
