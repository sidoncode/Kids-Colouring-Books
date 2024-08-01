package com.Leenah.coloringbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;

import java.util.ArrayList;

import static com.Leenah.coloringbook.MainActivity.readyForReview;
import static com.google.android.gms.ads.RequestConfiguration.MAX_AD_CONTENT_RATING_G;
import static com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE;

/**
 * Created by Leenah on 15/5/2017.
 * leenah.apps@gmail.com
 */

public class CategoryItems extends BaseActivity {
    int code;
    int imagesArray;
    String title;
    AdView mAdView;
    SharedPreferences preferences;
    int pos;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "AdMob ads consent";
    ReviewInfo reviewInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);
        preferences = PreferenceManager.getDefaultSharedPreferences(CategoryItems.this);

        //AdMob Banner Ad
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

        requestNewInterstitial();
        //============In-App Review API==================//
        if (readyForReview == true) {
            final ReviewManager manager = ReviewManagerFactory.create(CategoryItems.this);
            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
                @Override
                public void onComplete(@NonNull Task<ReviewInfo> task) {
                    if (task.isSuccessful()) {
                        reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(CategoryItems.this, reviewInfo);
                        flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task2) {
                                // do nothing
                            }
                        });


                    }
                }
            });
        }
        //============================================//



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            code = extras.getInt("code");
        }
        switch (code) {
            case 1:
                imagesArray = getResources().getIdentifier("Thumbnails1", "array", getPackageName());
                title = getString(R.string.flowers);
                break;
            case 2:
                imagesArray = getResources().getIdentifier("Thumbnails2", "array", getPackageName());
                title = getString(R.string.cartoons);
                break;
            case 3:
                imagesArray = getResources().getIdentifier("Thumbnails3", "array", getPackageName());
                title = getString(R.string.animals);
                break;
            case 4:
                imagesArray = getResources().getIdentifier("Thumbnails4", "array", getPackageName());
                title = getString(R.string.foods);
                break;
            case 5:
                imagesArray = getResources().getIdentifier("Thumbnails5", "array", getPackageName());
                title = getString(R.string.transport);
                break;
            case 6:
                imagesArray = getResources().getIdentifier("Thumbnails6", "array", getPackageName());
                title = getString(R.string.nature);
                break;

        }
        getSupportActionBar().setTitle(title);
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

        //onClick
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                pos = position + 1;
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(CategoryItems.this);
                } else {
                    Intent intent = new Intent(CategoryItems.this, MainActivity.class);
                    intent.putExtra("position", pos);
                    intent.putExtra("code", code);
                    startActivity(intent);
                }
            }
        });
    }

    // Prepare data for gridview according to the code variable
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(imagesArray);

        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap));
        }
        return imageItems;
    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();


        InterstitialAd.load(
                this,
                getString(R.string.interstitial_unit_id),
                adRequest,
                new InterstitialAdLoadCallback() {

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                        requestNewInterstitial();

                                        requestNewInterstitial();
                                        Intent intent = new Intent(CategoryItems.this, MainActivity.class);
                                        intent.putExtra("position", pos);
                                        intent.putExtra("code", code);
                                        startActivity(intent);

                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                        requestNewInterstitial();

                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                        requestNewInterstitial();
                                    }


                                });


                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;

                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());

                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CategoryItems.this, Categories.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
