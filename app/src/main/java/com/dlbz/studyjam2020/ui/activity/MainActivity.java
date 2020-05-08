package com.dlbz.studyjam2020.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dlbz.studyjam2020.R;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

public class MainActivity extends AppCompatActivity {

    private static final String PROYECT_ID = "https://studyjam2020.page.link";
    private static final String STUDYJAM_KEY = "studyjam";
    private static final String GDGSC_KEY = "gdgsc";
    private static final String DEEP_LINK = "http://studyjam2020/";

    private Button btnStudy;
    private Button btnGDG;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, onConnectionFailedListener)
                .addApi(AppInvite.API)
                .build();

        boolean launchDeepLink = false;
        AppInvite.AppInviteApi.getInvitation(googleApiClient, this, launchDeepLink).setResultCallback(new ResultCallback<AppInviteInvitationResult>() {
            @Override
            public void onResult(@NonNull AppInviteInvitationResult appInviteInvitationResult) {
                if(appInviteInvitationResult.getStatus().isSuccess()){
                    Intent intent = appInviteInvitationResult.getInvitationIntent();

                    String deepLink = AppInviteReferral.getDeepLink(intent); //http://studyjam2020/study
                    String[] str = deepLink.split(DEEP_LINK);
                    String id_post = str[1]; //study o gdgsc

                    Intent i = null;
                    if(id_post.equals(STUDYJAM_KEY+"/")){
                        i = new Intent(MainActivity.this, StudyJamActivity.class);
                    }

                    if(id_post.equals(GDGSC_KEY+"/")){
                        i = new Intent(MainActivity.this, GdgActivity.class);
                    }

                    startActivity(i);
                }
            }
        });

        initComponent();
    }

    private void initComponent() {
        btnStudy = findViewById(R.id.btnStudy);
        btnStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDeepLink(buildDeepLink(STUDYJAM_KEY));
            }
        });

        btnGDG = findViewById(R.id.btnGDG);
        btnGDG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDeepLink(buildDeepLink(GDGSC_KEY));
            }
        });
    }

    private String buildDeepLink(String key){
        return PROYECT_ID + "?link=http://studyjam2020/" + key + "/" + "&apn=" + getApplicationContext().getPackageName();
    }

    private void shareDeepLink(String deepLink){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Firebase Dynamic Link");
        intent.putExtra(Intent.EXTRA_TEXT, deepLink);
        startActivity(intent);

    }

    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }
    };
}
