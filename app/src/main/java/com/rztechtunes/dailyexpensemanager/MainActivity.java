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
import android.content.pm.PackageManager;
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
import com.rztechtunes.dailyexpensemanager.helper.NotificationWorker;
import com.rztechtunes.dailyexpensemanager.pref.UserActivityStorePref;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final int STORAGE_REQUEST_CODE = 123;
    private static final String TAG = MainActivity.class.getSimpleName();
    BottomNavigationView bottomNav;
    NavController navController;
    boolean isExit=false,isBack=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isStorageRequestAccepted();

        //For Bottom Nevigation
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        navController = Navigation.findNavController(this,R.id.nav_host_fragment);

        ///Send Notification

        Constraints constraints =
                new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
                        .setConstraints(constraints).build();
        WorkManager.getInstance(this)
                .enqueue(periodicWorkRequest);

        UserActivityStorePref userActivityStorePref = new UserActivityStorePref(this);
        boolean notificationStatus = userActivityStorePref.getNotification();

        if (notificationStatus) {
            WorkManager.getInstance().cancelAllWorkByTag("notification");

        }

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId())
                {
                    case R.id.splashScreenFragment:
                        bottomNav.setVisibility(View.GONE);
                        break;
                    case R.id.expenseManagerFrag:
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                bottomNav.setVisibility(View.VISIBLE);

                            }
                        });
                        isExit = true;
                        break;
                    case R.id.dailyNoteFragment:
                        isBack = true;
                        isExit = false;
                        break;
                    case R.id.allCalculationFragment:
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else if (isBack) {
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

    private boolean isStorageRequestAccepted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissionList = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissionList, STORAGE_REQUEST_CODE);
                return false;
            }
        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {


            case STORAGE_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Stronge", Toast.LENGTH_SHORT).show();
                    //  getFromSdcard();
                    // getCaptureScreenShot();

                } else {
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                }
            }


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