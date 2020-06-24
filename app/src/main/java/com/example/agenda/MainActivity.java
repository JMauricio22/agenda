package com.example.agenda;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import BD.model.TareaViewModel;
import BD.tareas.Tarea;

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

}
