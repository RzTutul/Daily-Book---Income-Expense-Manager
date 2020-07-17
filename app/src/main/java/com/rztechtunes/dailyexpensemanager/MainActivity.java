package com.rztechtunes.dailyexpensemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.DairyPojo;
import com.rztechtunes.dailyexpensemanager.helper.NotificationWorker;
import com.rztechtunes.dailyexpensemanager.pref.UserActivityStorePref;

import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    BottomNavigationView bottomNav;
    NavController navController;
    boolean isExit = false, isBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //For Bottom Nevigation
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);



        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId()) {
                    case R.id.splashScreenFragment:
                        bottomNav.setVisibility(View.GONE);
                        break;
                    case R.id.expenseManagerFrag:
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                bottomNav.setVisibility(View.VISIBLE);
                                bottomNav.getMenu().findItem(R.id.home_menu).setChecked(true);

                            }
                        });
                        isExit = true;
                        break;
                    case R.id.dailyNoteFragment:
                        bottomNav.getMenu().findItem(R.id.dairy_menu).setChecked(true);
                        isBack = true;
                        isExit = false;
                        break;
                    case R.id.allCalculationFragment:
                        bottomNav.getMenu().findItem(R.id.calculate_menu).setChecked(true);
                        isBack = true;
                        isExit = false;
                        break;

                    default:
                        isExit = false;
                        isBack = false;
                        break;
                }
            }
        });

    }


    @Override
    public void onBackPressed() {

        if (isExit) {
            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("Rate this app?")
                    .setCustomImage(R.drawable.ratingimage)
                    .setContentText("if you enjoy this app,would you mind taking a moment to rate it? it won't take more than a minute.Thank you!")
                    .setConfirmText("Rate Now")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                            sDialog.dismissWithAnimation();

                        }
                    })
                    .setCancelButton("Later", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            MainActivity.this.finish();
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();


        } else if (isBack) {
            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.expenseManagerFrag);
        }
    /*    else if (isDairyFrg) {
            final Bundle bundle = new Bundle();
            bundle.putString("id", eventID);
            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragmnet).navigate(R.id.eventDairyListFragment,bundle);
        }
*/
        else {
            super.onBackPressed();
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home_menu:
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.expenseManagerFrag);
                            break;
                        case R.id.dashBoard_menu:
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.exIncomeDashBoardFrag);
                            break;
                        case R.id.calculate_menu:
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.allCalculationFragment);
                            break;
                        case R.id.dairy_menu:
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.dailyNoteFragment);
                            break;
                        case R.id.setting_menu:
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.settingFragment);
                            break;

                        default:
                            break;
                    }

                    return true;
                }
            };


}