package com.example.agenda;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.view.MenuItem;
import android.widget.Filter;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private MainActivity self = this;

    private View[] list = new View[6];

    private Filter[] filters = new Filter[list.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupDrawerContent((NavigationView) findViewById(R.id.navigation));

        loadTaskFragment();

        createNotificationChannel(getString(R.string.channel_id));

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);

                        drawer.closeDrawer(GravityCompat.START);

                        Toast.makeText(self, "Testing", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                });
    }

    public void loadTaskFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment taskFragment = new TaskFragment();

        fragmentTransaction.add(R.id.fragment_container , taskFragment);

        fragmentTransaction.commit();
    }

    private void createNotificationChannel(String CHANNEL_ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);

            String description = getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
        }
    }

}
