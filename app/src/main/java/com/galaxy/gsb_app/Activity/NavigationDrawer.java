package com.galaxy.gsb_app.Activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.galaxy.gsb_app.Fragments.AgendaFragment;
import com.galaxy.gsb_app.Fragments.CompteRendusFragment;
import com.galaxy.gsb_app.Fragments.MedicamentsFragment;
import com.galaxy.gsb_app.Fragments.PracticiensFragment;
import com.galaxy.gsb_app.Fragments.TicketsDIncidentsFragment;
import com.galaxy.gsb_app.Fragments.VehiculeFragment;
import com.galaxy.gsb_app.Fragments.VisiteursFragment;
import com.galaxy.gsb_app.R;


public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_agenda);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        displaySelectedScreen(item.getItemId());

        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        String tag ="";

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_agenda:
                fragment = new AgendaFragment();
                tag = "AgendaFragment";
                break;
            case R.id.nav_comptes_rendues:
                CompteRendusFragment myFrag = new CompteRendusFragment();
                String s = getIntent().getStringExtra("visiteurId");
                Bundle bundle = new Bundle();
                bundle.putString("visiteurId", s);
                myFrag.setArguments(bundle);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                tag = "CompteRendusFragment";
                ft.replace(R.id.content_frame, myFrag).addToBackStack("tag").commit();
                break;
            case R.id.nav_Medicaments:
                fragment = new MedicamentsFragment();
                tag = "MedicamentsFragment";
                break;
            case R.id.nav_practiciens:
                fragment = new PracticiensFragment();
                tag = "PracticiensFragment";
                break;
            case R.id.nav_visiteurs:
                fragment = new VisiteursFragment();
                tag = "VisiteursFragment";
                break;
            case R.id.nav_tickets_incidents:
                fragment = new TicketsDIncidentsFragment();
                tag = "TicketsDIncidentsFragment";
                break;
            case R.id.nav_vehicule:
                fragment = new VehiculeFragment();
                tag = "VehiculeFragment";
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(tag).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
