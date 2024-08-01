package com.Leenah.coloringbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.LayoutRes;
import com.google.android.material.navigation.NavigationView;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.UserMessagingPlatform;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;



/**
 * Created by Leenah on 15/5/2017.
 * leenah.apps@gmail.com
 */
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    int code;
    private NavigationView navigationView;
    private DrawerLayout fullLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedNavItemId;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        /**
         * This is going to be our actual root layout.
         */
        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        /**
         * {@link FrameLayout} to inflate the child's view. We could also use a {@link android.view.ViewStub}
         */
        FrameLayout activityContainer = (FrameLayout) fullLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        /**
         * Note that we don't_title pass the child's layoutId to the parent,
         * instead we pass it our inflated layout.
         */
        super.setContentView(fullLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        if (useToolbar()) {
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }

        setUpNavView();

    }

    /**
     * Helper method that can be used by child classes to
     * specify that they don't_title want a {@link Toolbar}
     *
     * @return true
     */
    protected boolean useToolbar() {
        return true;
    }

    protected void setUpNavView() {
        navigationView.setNavigationItemSelectedListener(this);

        if (useDrawerToggle()) {
            drawerToggle = new ActionBarDrawerToggle(this, fullLayout, toolbar,
                    R.string.nav_drawer_opened,
                    R.string.nav_drawer_closed);

            fullLayout.setDrawerListener(drawerToggle);
            drawerToggle.syncState();
        } else if (useToolbar() && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources()
                    .getDrawable(R.drawable.abc_ic_ab_back_material));
        }

//
    }

    /**
     * Helper method to allow child classes to opt-out of having the
     * hamburger menu.
     *
     * @return
     */
    protected boolean useDrawerToggle() {
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        fullLayout.closeDrawer(GravityCompat.START);
        selectedNavItemId = menuItem.getItemId();
        return onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mainmenu:
                code = 1;
                Intent i = new Intent(this, Categories.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("code", code);
                startActivity(i);
                return true;
            case R.id.ads:
                adsSettings();
                return true;

            case R.id.share:
                shareTextUrl();
                return true;

            case R.id.rate:
                String package_name = getPackageName();
                Intent r = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + package_name));
                startActivity(r);
                return true;

            case R.id.about:
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this);
                alertDialogBuilder.setTitle(getString(R.string.app_name));
                alertDialogBuilder
                        .setMessage(getString(R.string.about_text))
                        .setIcon(R.drawable.small_logo)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            case R.id.privacyPolicy:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.gdpr_privacypolicy)));
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_container);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void shareTextUrl() {
        String package_name = getPackageName();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + package_name);
        startActivity(Intent.createChooser(share, getString(R.string.Share_The_App)));
    }
    public void adsSettings() {
        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(this);

        consentInformation.reset();
        Intent i4 = new Intent(this, Splash.class);
        i4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i4);
    }
}
