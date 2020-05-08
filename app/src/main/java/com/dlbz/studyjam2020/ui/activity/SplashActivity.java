package com.dlbz.studyjam2020.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dlbz.studyjam2020.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActivity extends AppCompatActivity {

    private FirebaseRemoteConfig remoteConfig;

    private ProgressBar progress;
    private RelativeLayout lytBackground;
    private ImageView ivImage;
    private TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        remoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings remoteConfigSettings
                = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(true).build();


        remoteConfig.setConfigSettings(remoteConfigSettings);
        remoteConfig.setDefaults(R.xml.remote_config_default);

        syncronize();

        initComponent();
    }

    private void initComponent() {
        progress = findViewById(R.id.progress);
        lytBackground = findViewById(R.id.lytBackground);
        ivImage = findViewById(R.id.ivImage);
        tvMessage = findViewById(R.id.tvMessage);
    }

    private void loadComponent(){
        lytBackground.setVisibility(View.VISIBLE);

        lytBackground.setBackgroundColor(Color.parseColor(remoteConfig.getString("splash_background")));
        tvMessage.setText(remoteConfig.getString("splash_text"));

        if(remoteConfig.getString("splash_image").equalsIgnoreCase("image_default")){
            ivImage.setImageResource(R.drawable.image_default);
        }else if(remoteConfig.getString("splash_image").equalsIgnoreCase("image_christmas")){
            ivImage.setImageResource(R.drawable.image_christmas);
        }
    }

    private void syncronize(){
        long cache = 4000;

        if(remoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
            cache = 0;
        }

        remoteConfig.fetch(cache).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    remoteConfig.activateFetched();
                }

                loadComponent();

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    };
                }, 3000);
            }
        });
    }


}
