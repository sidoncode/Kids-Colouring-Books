package com.Leenah.coloringbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;


import static com.google.android.gms.ads.RequestConfiguration.MAX_AD_CONTENT_RATING_G;
import static com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

/**
 * Created by Leenah on 15/5/2017.
 * leenah.apps@gmail.com
 */
public class Categories extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AdMob ads consent";
    public boolean mShowNonPersonalizedAdRequests = false;
    ImageView c1, c2, c3, c4, c5, c6;
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    int code;
    AdView mAdView;
    SharedPreferences preferences;
    SharedPreferences sharedPreferences;
    private ConsentInformation consentInformation;
    private ConsentForm consentForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //**Ads EU conset**//
        checkForConsent();

        getSupportActionBar().setTitle(getString(R.string.app_name));
        preferences = PreferenceManager.getDefaultSharedPreferences(Categories.this);

        //AdMob Ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //=======Designed for children==========//
        RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                .toBuilder().setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                .setMaxAdContentRating(MAX_AD_CONTENT_RATING_G)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
        //=======================================//

        mAdView = (AdView) findViewById(R.id.adView);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);


        c1 = (ImageView) findViewById(R.id.c_1);
        c2 = (ImageView) findViewById(R.id.c_2);
        c3 = (ImageView) findViewById(R.id.c_3);
        c4 = (ImageView) findViewById(R.id.c_4);
        c5 = (ImageView) findViewById(R.id.c_5);
        c6 = (ImageView) findViewById(R.id.c_6);


        tv1 = (TextView) findViewById(R.id.text_card_name1);
        tv2 = (TextView) findViewById(R.id.text_card_name2);
        tv3 = (TextView) findViewById(R.id.text_card_name3);
        tv4 = (TextView) findViewById(R.id.text_card_name4);
        tv5 = (TextView) findViewById(R.id.text_card_name5);
        tv6 = (TextView) findViewById(R.id.text_card_name6);


        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        c5.setOnClickListener(this);
        c6.setOnClickListener(this);

        //Set font
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/snoopy.ttf");
        tv1.setTypeface(tf);
        tv2.setTypeface(tf);
        tv3.setTypeface(tf);
        tv4.setTypeface(tf);
        tv5.setTypeface(tf);
        tv6.setTypeface(tf);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.c_1:
                code = 1;
                break;
            case R.id.c_2:
                code = 2;
                break;
            case R.id.c_3:
                code = 3;
                break;
            case R.id.c_4:
                code = 4;
                break;
            case R.id.c_5:
                code = 5;
                break;
            case R.id.c_6:
                code = 6;
                break;
        }

            Intent i = new Intent(Categories.this, CategoryItems.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("code", code);
            startActivity(i);
    }

    /*******************************************/
    private void checkForConsent() {
        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this)
                .setDebugGeography(ConsentDebugSettings
                        .DebugGeography
                        .DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("")

                .build();



        // Set tag for underage of consent. false means users are not underage.
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setTagForUnderAgeOfConsent(false)
                //TODO Remove this line before production
//                .setConsentDebugSettings(debugSettings)
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
                    @Override
                    public void onConsentInfoUpdateSuccess() {
                        // The consent information state was updated.
                        // You are now ready to check if a form is available.
                        if (consentInformation.isConsentFormAvailable()) {
                            loadForm();
                        }
                    }
                },
                new ConsentInformation.OnConsentInfoUpdateFailureListener() {
                    @Override
                    public void onConsentInfoUpdateFailure(FormError formError) {
                        // Handle the error.
                    }
                });
    }

    public void loadForm(){
        UserMessagingPlatform.loadConsentForm(
                this,
                new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
                    @Override
                    public void onConsentFormLoadSuccess(ConsentForm consentForm) {
                        Categories.this.consentForm = consentForm;
                        if(consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
                            consentForm.show(
                                    Categories.this,
                                    new ConsentForm.OnConsentFormDismissedListener() {
                                        @Override
                                        public void onConsentFormDismissed(@Nullable FormError formError) {
                                            // Handle dismissal by reloading form.
                                            loadForm();
                                        }
                                    });

                        }

                    }
                },
                new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
                    @Override
                    public void onConsentFormLoadFailure(FormError formError) {
                        /// Handle Error.
                    }
                }
        );
    }

    /*******************************************/


}
