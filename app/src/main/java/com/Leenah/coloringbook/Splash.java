package com.Leenah.coloringbook;

import android.content.Intent;
import android.os.Handler;

import com.daimajia.androidanimations.library.Techniques;
import com.google.android.ump.ConsentInformation;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by Leenah on 15/5/2017.
 * leenah.apps@gmail.com
 */

public class Splash extends AwesomeSplash {

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!

    @Override
    public void initSplash(ConfigSplash configSplash) {

			/* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorAccent); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.small_logo); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.setTitleSplash(getString(R.string.app_name));
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.BounceInDown);
        configSplash.setTitleFont("fonts/snoopy.ttf"); //provide string to your font located in assets/fonts/


    }

    @Override
    public void animationsFinished() {

        //transit to another activity here
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(Splash.this, Categories.class);
                finish();
                startActivity(intent);
            }

        }, 1000);

    }
}
