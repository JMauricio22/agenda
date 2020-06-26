package com.example.agenda;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import com.example.agenda.fragments.NoteFragment;
import com.example.agenda.fragments.TaskFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;

    Fragment currentFragment;

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

                        Fragment fragment;

                        switch (menuItem.getItemId()){
                            case R.id.lista_tareas:

                                if(!(currentFragment instanceof TaskFragment)){

                                    currentFragment = new TaskFragment();

                                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                                    transaction.replace(R.id.fragment_container , currentFragment )
                                            .commit();
                                }

                                drawer.closeDrawer(GravityCompat.START);

                                return true;

                            case R.id.notas:

                                if(!(currentFragment instanceof NoteFragment)){

                                    currentFragment = new NoteFragment();

                                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                                    transaction.replace(R.id.fragment_container , currentFragment )
                                            .commit();
                                }

                                drawer.closeDrawer(GravityCompat.START);

                                return true;

                            case R.id.salir:
                                    finish();

                            default:
                                return true;
                        }
                    }
                });
    }

    public void loadTaskFragment(){
        fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        currentFragment = new TaskFragment();

        fragmentTransaction.add(R.id.fragment_container , currentFragment);

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
