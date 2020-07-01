package com.rztechtunes.dailyexpensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class MainActivity extends AppCompatActivity {
    private static final int STORAGE_REQUEST_CODE = 123;
    public MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isStorageRequestAccepted();

        bottomNavigation = findViewById(R.id.bottomNav);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_add_circle_outline_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_add_circle_outline_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_add_circle_outline_24));


        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // your codes
                switch (item.getId()) {
                    case 1:
                        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.expenseManagerFrag);
                        break;
                    case 2:
                        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.exIncomeDashBoardFrag);
                        break;
                    case 3:
                        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.expenseManagerFrag);
                        break;


                    default:
                        break;

                }
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        bottomNavigation.show(1, true);


        bottomNavigation.setCount(2, "History");

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

}