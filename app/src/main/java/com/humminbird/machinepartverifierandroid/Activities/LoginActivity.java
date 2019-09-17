package com.humminbird.machinepartverifierandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.google.android.material.card.MaterialCardView;
import com.humminbird.machinepartverifierandroid.DataClasses.Person;
import com.humminbird.machinepartverifierandroid.Interfaces.ApiService;
import com.humminbird.machinepartverifierandroid.R;
import com.humminbird.machinepartverifierandroid.Utilities.AnimationAssistant;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    AnimationAssistant assistant;

    MaterialCardView icon;
    MaterialCardView loginButton;
    MaterialCardView loginSubmitButton;
    MaterialCardView form;
    MaterialCardView decisionForm;
    MaterialCardView lineButton;
    MaterialCardView reelButton;

    RelativeLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        { //Load animations needed for this activity
            assistant = new AnimationAssistant(this);
        }
        { //Initialize Variables and Views
            icon = findViewById(R.id.mcvIcon);
            header = findViewById(R.id.relHeader);
            form = findViewById(R.id.mcvLoginForm);
            loginButton = findViewById(R.id.mcvLoginInit);
            loginSubmitButton = findViewById(R.id.mcvLoginButton);

            lineButton = findViewById(R.id.mcvFullLineButton);
            lineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
            });
            reelButton = findViewById(R.id.mcvSingleReelButton);
            reelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this,VerifyLineSetupActivity.class));
                }
            });
            decisionForm = findViewById(R.id.mcvDecisionForm);

//            // create an instance of the ApiService
//            ApiService apiService = retrofit.create(ApiService.class);
//            // make a request by calling the corresponding method
//            final CompositeDisposable compositeDisposable = new CompositeDisposable();
//
//            Single<Person> person = apiService.getPersonData(personId, API_KEY)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new SingleObserver<Person>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            // we'll come back to this in a moment
//                            compositeDisposable.add(d);
//                        }
//
//                        @Override
//                        public void onSuccess(Person person) {
//                            // data is ready and we can update the UI
//                        }
//                        @Override
//                        public void onError(Throwable e) {
//                            // oops, we best show some error message
//                        }
//                    });


//            retrofit = new Retrofit.Builder()
//                    .baseUrl("https://api.themoviedb.org/3/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();
        }

        epicEntrance();

        { //Setup Click Listeners
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    epicEntrance();
                }
            });
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showForm();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        epicEntrance();
    }

    private void epicEntrance(){
        header.setVisibility(View.VISIBLE);
        assistant.animateView(header, AnimationAssistant.AnimationGroup.Toast, AnimationAssistant.AnimationPhase.In);

        loginButton.setVisibility(View.VISIBLE);
        assistant.animateView(loginButton, AnimationAssistant.AnimationGroup.Entrance, AnimationAssistant.AnimationPhase.Grow);
    }

    private void showForm(){
        form.setVisibility(View.VISIBLE);
        assistant.animateView(form, AnimationAssistant.AnimationGroup.Entrance, AnimationAssistant.AnimationPhase.Grow);

        //gracefully transition
        loginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setVisibility(View.GONE);
                //form.startAnimation(shrinkOut);
                assistant.animateView(form, AnimationAssistant.AnimationGroup.Grow_Shrink, AnimationAssistant.AnimationPhase.Out, new Runnable() {
                    @Override
                    public void run() {
                        form.setVisibility(View.GONE);
                    }
                });
                assistant.animateView(header, AnimationAssistant.AnimationGroup.Toast, AnimationAssistant.AnimationPhase.Out,new Runnable() {
                    @Override
                    public void run() {
                        header.setVisibility(View.GONE);
                        decisionForm.setVisibility(View.VISIBLE);
                        assistant.animateView(decisionForm, AnimationAssistant.AnimationGroup.Toast, AnimationAssistant.AnimationPhase.In);
                    }
                });
            }
        });
    }
}
