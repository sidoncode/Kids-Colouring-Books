package com.Leenah.coloringbook;

import static com.google.android.gms.ads.RequestConfiguration.MAX_AD_CONTENT_RATING_G;
import static com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


/**
 * Created by Leenah on 15/5/2017.
 * leenah.apps@gmail.com
 */

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 1;
    public static ArrayList<Point> drawnPoints = new ArrayList<Point>();
    public static boolean readyForReview = false;
    static String randomNumber;
    Paint paint;
    ImageView iv;
    int position, code;
    Button White, Black, Gray, lightOrange, Brown, Yellow, deepBlue, lightBlue, deepPurple, lightPurple,
            deepGreen, lightGreen, deepPink, lightPink, Red, deepOrange, select_color;
    int h, w;
    AdView mAdView;
    SharedPreferences preferences;
    MediaPlayer mPlayer, clickPlayer;
    int counter = 0;
    Point aaa;
    private RelativeLayout drawingLayout;
    private MyView myView;
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        randomNumber = String.valueOf(new Random().nextInt(5) + 1);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

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


        iv = (ImageView) findViewById(R.id.coringImage);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // code of category
            //position of image in the category array
            position = extras.getInt("position");
            code = extras.getInt("code");
        }

        myView = new MyView(this);
        drawingLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        drawingLayout.addView(myView);

        White = (Button) findViewById(R.id.white);
        Black = (Button) findViewById(R.id.black);
        Gray = (Button) findViewById(R.id.gray);
        lightOrange = (Button) findViewById(R.id.light_orange);
        Brown = (Button) findViewById(R.id.brown);
        Yellow = (Button) findViewById(R.id.yellow);
        deepBlue = (Button) findViewById(R.id.deep_blue);
        lightBlue = (Button) findViewById(R.id.light_blue);
        deepPurple = (Button) findViewById(R.id.deep_purple);
        lightPurple = (Button) findViewById(R.id.light_purple);
        deepGreen = (Button) findViewById(R.id.deep_green);
        lightGreen = (Button) findViewById(R.id.light_green);
        deepPink = (Button) findViewById(R.id.deep_pink);
        lightPink = (Button) findViewById(R.id.light_pink);
        Red = (Button) findViewById(R.id.red);
        deepOrange = (Button) findViewById(R.id.deep_orange);
        select_color = (Button) findViewById(R.id.select_color);

        White.setOnClickListener(this);
        Black.setOnClickListener(this);
        Gray.setOnClickListener(this);
        lightOrange.setOnClickListener(this);
        Brown.setOnClickListener(this);
        Yellow.setOnClickListener(this);
        deepBlue.setOnClickListener(this);
        lightBlue.setOnClickListener(this);
        deepPurple.setOnClickListener(this);
        lightPurple.setOnClickListener(this);
        deepGreen.setOnClickListener(this);
        lightGreen.setOnClickListener(this);
        deepPink.setOnClickListener(this);
        lightPink.setOnClickListener(this);
        Red.setOnClickListener(this);
        deepOrange.setOnClickListener(this);
        select_color.setOnClickListener(this);


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                onShareImageItem();
                break;
            case R.id.action_save:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        save(myView);

                    } else {
                        requestPermission(); // Code for permission
                    }
                } else {
                    // Code for Below 23 API Oriented Device
                    save(myView);
                }
                break;
            case R.id.action_mute:
                if (mPlayer != null) {
                    if (!mPlayer.isPlaying()) {
                        mPlayer.start();
                        mPlayer.setLooping(true);
                        item.setIcon(R.drawable.sound_on);
                        Snackbar snackbar = Snackbar.make(drawingLayout, getString(R.string.soundon), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                        item.setIcon(R.drawable.sound_off);
                        Snackbar snackbar = Snackbar.make(drawingLayout, getString(R.string.soundoff), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
                break;

            case R.id.action_undo:
                myView.undoMethod();
                break;
            case android.R.id.home:
                Intent i = new Intent(MainActivity.this, CategoryItems.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("code", code);
                startActivity(i);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    //Share the Image
    public void onShareImageItem() {
        String package_name = getPackageName();

        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(iv);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + "\n" + "https://play.google.com/store/apps/details?id=" + package_name);

            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, getString(R.string.action_share)));
        } else {
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView iv) {
        iv.setImageBitmap(myView.scaledBitmap);
        // Extract Bitmap from ImageView drawable
        Drawable drawable = iv.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        File file;

        try {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/" + getString(R.string.app_name) + "/");
            dir.mkdirs();
            String fileName = "/" + System.currentTimeMillis() + "share_image.png";

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                file = new File(dir, fileName);
            } else {
                file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            }

            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void save(View view) {
        File filename;
        iv.setImageBitmap(myView.scaledBitmap);

        try {


            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/" + getString(R.string.app_name) + "/");
            dir.mkdirs();
            String fileName = "/" + System.currentTimeMillis() + "image.jpg";

            filename = new File(dir, fileName);
            FileOutputStream fos = new FileOutputStream(filename);
            myView.scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(getApplicationContext(), getString(R.string.save), Toast.LENGTH_LONG).show();
            MediaScannerConnection.scanFile(MainActivity.this,
                    new String[]{filename.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        enable();
        switch (view.getId()) {
            case R.id.white:
                paint.setColor(getResources().getColor(R.color.white));
                White.setSelected(true);
                break;
            case R.id.black:
                paint.setColor(getResources().getColor(R.color.black));
                Black.setSelected(true);
                break;
            case R.id.gray:
                paint.setColor(getResources().getColor(R.color.gray));
                Gray.setSelected(true);
                break;
            case R.id.light_orange:
                paint.setColor(getResources().getColor(R.color.light_orange));
                lightOrange.setSelected(true);
                break;
            case R.id.brown:
                paint.setColor(getResources().getColor(R.color.brown));
                Brown.setSelected(true);
                break;
            case R.id.yellow:
                paint.setColor(getResources().getColor(R.color.yellow));
                Yellow.setSelected(true);
                break;
            case R.id.deep_blue:
                paint.setColor(getResources().getColor(R.color.deep_blue));
                deepBlue.setSelected(true);
                break;
            case R.id.light_blue:
                paint.setColor(getResources().getColor(R.color.light_blue));
                lightBlue.setSelected(true);
                break;
            case R.id.deep_purple:
                paint.setColor(getResources().getColor(R.color.deep_purple));
                deepPurple.setSelected(true);
                break;
            case R.id.light_purple:
                paint.setColor(getResources().getColor(R.color.light_purple));
                lightPurple.setSelected(true);
                break;
            case R.id.deep_green:
                paint.setColor(getResources().getColor(R.color.deep_green));
                deepGreen.setSelected(true);
                break;
            case R.id.light_green:
                paint.setColor(getResources().getColor(R.color.light_green));
                lightGreen.setSelected(true);
                break;
            case R.id.deep_pink:
                paint.setColor(getResources().getColor(R.color.deep_pink));
                deepPink.setSelected(true);
                break;
            case R.id.light_pink:
                paint.setColor(getResources().getColor(R.color.light_pink));
                lightPink.setSelected(true);
                break;
            case R.id.red:
                paint.setColor(getResources().getColor(R.color.red));
                Red.setSelected(true);
                break;
            case R.id.deep_orange:
                paint.setColor(getResources().getColor(R.color.deep_orange));
                deepOrange.setSelected(true);
                break;

            case R.id.select_color:
                ColorPickerDialogBuilder
                        .with(this)
                        .setTitle(getString(R.string.chooseColor))
//                        .initialColor(0xffe53935)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .showLightnessSlider(false)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {


                            }
                        })
                        .setPositiveButton(getString(R.string.ok), new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                paint.setColor(selectedColor);

                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
                break;
        }

    }

    public void enable() {
        White.setSelected(false);
        Gray.setSelected(false);
        Black.setSelected(false);
        lightOrange.setSelected(false);
        Brown.setSelected(false);
        Yellow.setSelected(false);
        deepBlue.setSelected(false);
        lightBlue.setSelected(false);
        deepPurple.setSelected(false);
        lightPurple.setSelected(false);
        deepGreen.setSelected(false);
        lightGreen.setSelected(false);
        deepPink.setSelected(false);
        lightPink.setSelected(false);
        Red.setSelected(false);
        deepOrange.setSelected(false);

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= 33) {
            result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_IMAGES);
        }
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_MEDIA_IMAGES)) {
                Toast.makeText(MainActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
            }
        }else{
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(MainActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
            mPlayer.setLooping(true);
        } else {
            int backgroundResourceId = this.getResources().getIdentifier("background_" + randomNumber, "raw", this.getPackageName());
            mPlayer = MediaPlayer.create(MainActivity.this, backgroundResourceId);
            mPlayer.start();
            mPlayer.setLooping(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        readyForReview = true;
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MainActivity.this, CategoryItems.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("code", code);
        startActivity(i);
    }

    // flood fill
    public class MyView extends View {

        Point p1 = new Point();
        Bitmap mBitmap, scaledBitmap;

        ProgressDialog pd;
        Canvas canvas;
        private Path path;

        private ScaleGestureDetector scaleDetector;
        private float scaleFactor = 1.f;

        private float mPositionX, mPositionY;
        private float refX, refY;
        int currentPointerCount = 0;

        public MyView(Context context) {
            super(context);
            this.path = new Path();
            paint = new Paint();
            paint.setAntiAlias(true);
            pd = new ProgressDialog(context);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(5f);
            int id = getResources().getIdentifier("gp" + code + "_" + position, "drawable", getPackageName());
            mBitmap = BitmapFactory.decodeResource(getResources(),
                    id).copy(Bitmap.Config.ARGB_8888, true);

// Get the screen dimension
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            Display getOrient = getWindowManager().getDefaultDisplay();
            int orientation = Configuration.ORIENTATION_UNDEFINED;
            if (getOrient.getWidth() == getOrient.getHeight()) {
                orientation = Configuration.ORIENTATION_SQUARE;
            } else {
                if (getOrient.getWidth() < getOrient.getHeight()) {
                    orientation = Configuration.ORIENTATION_PORTRAIT;
                } else {
                    orientation = Configuration.ORIENTATION_LANDSCAPE;
                }

            }

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {

                h = displaymetrics.heightPixels / 2 + 100;
                w = displaymetrics.widthPixels;
            }
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //for mdpi screen 760×1024 tablet
                Display display = getWindowManager().getDefaultDisplay();
                float density = context.getResources().getDisplayMetrics().density;
                if (density >= 1.0 && display.getWidth() == 1024) {
                    // Toast.makeText(context, "mdpi", Toast.LENGTH_SHORT).show();
                    h = displaymetrics.heightPixels - 200;
                    w = displaymetrics.widthPixels - 100;
                } else {
                    h = displaymetrics.heightPixels - 500;
                    w = displaymetrics.widthPixels - 700;
                }
            }
            scaledBitmap = Bitmap.createScaledBitmap(mBitmap, w, h, false);
            scaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        }

        public void undoMethod() {
//            if (paths.size() > 0) {
//                undonePaths.add(paths.remove(paths.size() - 1));
//                invalidate();
//            } else {
//                Toast.makeText(getContext(), getString(R.string.nomore), Toast.LENGTH_SHORT).show();
//            }
            if (paths.size() > 0) {
                counter = counter + 1;

                int length = drawnPoints.size();
                Log.e("Total_Drawn_Points", " " + length);
                if (paths.size() > 0) {

                    undonePaths.add(paths.remove(paths.size() - 1));


                    invalidate();
                    Log.e("Counter", " " + counter);
                    aaa = drawnPoints.get(drawnPoints.size() - counter);
                    int xxxx = aaa.x;
                    int yyyy = aaa.y;
                    if (yyyy >= 0 && yyyy < myView.scaledBitmap.getHeight() && xxxx >= 0) {
                        final int sourceColor = myView.scaledBitmap.getPixel((int) xxxx, (int) yyyy);
                        final int targetColor = Color.TRANSPARENT;

                        new MainActivity.MyView.TheTask(scaledBitmap, aaa, sourceColor, targetColor).execute();
                    }
                    //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                }
            } else {
                drawnPoints.clear();
                paths.clear();
                undonePaths.clear();
                counter = 0;
                Toast.makeText(MainActivity.this, getString(R.string.nomore), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("position", position);
                intent.putExtra("code", code);
                startActivity(intent);

            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.save();
            this.canvas = canvas;
            canvas.translate(mPositionX, mPositionY);
            canvas.scale(scaleFactor, scaleFactor);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            canvas.drawBitmap(scaledBitmap, 0, 0, paint);
            for (Path p : paths) {
                canvas.drawPath(p, paint);
            }
            canvas.restore();

        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            scaleDetector.onTouchEvent(event);
            //============//
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int screenHight = displaymetrics.heightPixels;
            int screenWidth = displaymetrics.widthPixels;
            float newX, newY;
            float dX = 0, dY = 0;
            int lastAction = 0;

            //============//
            float x = event.getX();
            float y = event.getY();
//            currentPointerCount = event.getPointerCount();
            Log.d("testttetsestestes", event.getPointerCount() + ">"+currentPointerCount);

            if (event.getAction() == MotionEvent.ACTION_UP) {


//                currentPointerCount = event.getPointerCount();
                if (currentPointerCount == 1) {

                    try {
                        //Play click sound
                        if (clickPlayer != null && !clickPlayer.isPlaying()) {
                            clickPlayer.start();
                        } else {
                            clickPlayer = MediaPlayer.create(MainActivity.this, R.raw.click);
                            clickPlayer.start();
                        }
                        p1 = new Point();
                        p1.x = (int) ((refX - mPositionX) / scaleFactor);
                        p1.y = (int) ((refY - mPositionY) / scaleFactor);
                        drawnPoints.add(p1);
                        final int sourceColor = scaledBitmap.getPixel((int) ((refX - mPositionX) / scaleFactor), (int) ((refY - mPositionY) / scaleFactor));
                        final int targetColor = paint.getColor();
                        new TheTask(scaledBitmap, p1, sourceColor, targetColor).execute();
                        paths.add(path);
                        invalidate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                currentPointerCount = event.getPointerCount();

                return true;

            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                currentPointerCount = event.getPointerCount();

                refX = event.getX();
                refY = event.getY();
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

                if (scaleFactor > 1.f) {
                    float nX = event.getX();
                    float nY = event.getY();

                    mPositionX += nX - refX;
                    mPositionY += nY - refY;
                    refX = nX;
                    refY = nY;
                    invalidate();
                    return true;

                } else {
                    return false;
                }

            } else if (event.getActionMasked() == MotionEvent.ACTION_CANCEL) {

                return true;


            }
            else if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {

                currentPointerCount = 2;
                return true;


            }
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_UP:
//                    Log.d("testttetsestestes", MotionEvent.ACTION_UP + "1");
//
//
//                        currentPointerCount = event.getPointerCount();
//                        if (currentPointerCount == 1 ) {
//
//                            try {
//                                //Play click sound
//                                if (clickPlayer != null && !clickPlayer.isPlaying()) {
//                                    clickPlayer.start();
//                                } else {
//                                    clickPlayer = MediaPlayer.create(MainActivity.this, R.raw.click);
//                                    clickPlayer.start();
//                                }
//                                p1 = new Point();
//                                p1.x = (int) ((refX - mPositionX) / scaleFactor);
//                                p1.y = (int) ((refY - mPositionY) / scaleFactor);
//                                drawnPoints.add(p1);
//                                final int sourceColor = scaledBitmap.getPixel((int) ((refX - mPositionX) / scaleFactor), (int) ((refY - mPositionY) / scaleFactor));
//                                final int targetColor = paint.getColor();
//                                new TheTask(scaledBitmap, p1, sourceColor, targetColor).execute();
//                                paths.add(path);
//                                invalidate();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                    break;
//                case MotionEvent.ACTION_DOWN:
//                    Log.d("testttetsestestes", MotionEvent.ACTION_DOWN + "0");
//
//                    currentPointerCount = event.getPointerCount();
//
//                    refX = event.getX();
//                    refY = event.getY();
//
//                    break;
//
//                case MotionEvent.ACTION_MOVE:
//                    Log.d("testttetsestestes", MotionEvent.ACTION_MOVE + "2");
//
//                    if (scaleFactor > 1.f) {
//                        float nX = event.getX();
//                        float nY = event.getY();
//
//                        mPositionX += nX - refX;
//                        mPositionY += nY - refY;
//                        refX = nX;
//                        refY = nY;
//                        invalidate();
//                    }
//
//                    break;
//
//
//                case MotionEvent.ACTION_POINTER_UP:
//                    currentPointerCount = 2;
//
//
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//
//
//                    break;
//            }

            return super.onTouchEvent(event);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            w = widthMeasureSpec - MeasureSpec.EXACTLY;
            h = heightMeasureSpec - MeasureSpec.EXACTLY;

            Log.d("Dim", Integer.toString(w) + " | " + Integer.toString(h));
        }

        private class ScaleListener extends
                ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(1.f, Math.min(scaleFactor, 5.0f));
                invalidate();
                return true;
            }
        }

        class TheTask extends AsyncTask<Void, Integer, Void> {

            Bitmap bmp;
            Point pt;
            int replacementColor, targetColor;

            public TheTask(Bitmap bm, Point p, int sc, int tc) {
                this.bmp = bm;
                this.pt = p;
                this.replacementColor = tc;
                this.targetColor = sc;
                //To show the progress dialog while coloring
                //pd.show();
            }

            @Override
            protected void onPreExecute() {
                //To show the progress dialog while coloring
                //pd.show();

            }

            @Override
            protected void onProgressUpdate(Integer... values) {

            }

            @Override
            protected Void doInBackground(Void... params) {
                FloodFill f = new FloodFill();
                f.floodFill(bmp, pt, targetColor, replacementColor);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                pd.dismiss();
                invalidate();
            }
        }
    }

    public class FloodFill {
        public void floodFill(Bitmap image, Point node, int targetColor,
                              int replacementColor) {
            int width = image.getWidth();
            int height = image.getHeight();
            int target = targetColor;
            int replacement = replacementColor;
            if (target != replacement) {
                Queue<Point> queue = new LinkedList<Point>();
                do {

                    int x = node.x;
                    int y = node.y;
                    while (x > 0 && image.getPixel(x - 1, y) == target) {
                        x--;

                    }
                    boolean spanUp = false;
                    boolean spanDown = false;
                    while (x < width && image.getPixel(x, y) == target) {
                        image.setPixel(x, y, replacement);
                        if (!spanUp && y > 0
                                && image.getPixel(x, y - 1) == target) {
                            queue.add(new Point(x, y - 1));
                            spanUp = true;
                        } else if (spanUp && y > 0
                                && image.getPixel(x, y - 1) != target) {
                            spanUp = false;
                        }
                        if (!spanDown && y < height - 1
                                && image.getPixel(x, y + 1) == target) {
                            queue.add(new Point(x, y + 1));
                            spanDown = true;
                        } else if (spanDown && y < height - 1
                                && image.getPixel(x, y + 1) != target) {
                            spanDown = false;
                        }
                        x++;
                    }
                } while ((node = queue.poll()) != null);
            }
        }
    }
}