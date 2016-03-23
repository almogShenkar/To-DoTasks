package com.almog;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.almog.db.DataMgr;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by almog on 3/11/16.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TextView userNameMenu;
    private ParseUser user;
    private String type;
    private List<User> users;
    private String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user=ParseUser.getCurrentUser();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateEditTask.class));
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        userNameMenu = (TextView) headerView.findViewById(R.id.navUsername);
        Menu menu=navigationView.getMenu();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });
        if(!isConnected()) {
            new CustomDialog(this, "No internet connection", "you are allowed to watch your offline tasks", null).showDialog();
        }
        else {
            //sign in success
            userNameMenu.setText("To-Do " + user.getString("name").toUpperCase());
            type = ParseUser.getCurrentUser().get("type").toString();
            if (type.equals("Employee"))
            {
                fab.setVisibility(View.GONE);
            }
            }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
            if (id == R.id.view_members)
            {
                String strListEmp=null;
                DataMgr dataMgr = new DataMgr(getApplicationContext(), ParseUser.getCurrentUser(),false);
                dataMgr.syncFromParseFirstTime();
                users = dataMgr.getAllUsers();
                for(int i=0;i<users.size();i++)
                {
                    strListEmp+=(users.get(i).getUsername())+"\n";
                }
                new CustomDialog(this,"List of your employees",strListEmp+"\n",null)
                        .showDialog();
            }
            if (id == R.id.nav_add)
            {

                teamName=ParseUser.getCurrentUser().get("team").toString();
                Intent i = new Intent(this, CreateAndInvite.class);
                i.putExtra("teamName",teamName);
                startActivity(i);
            }


        if (id == R.id.nav_setting)
        {

        }
        else if (id == R.id.nav_logout)
        {
            ParseUser.logOut();
            startActivity(new Intent(this, Login.class));
            finish();
        } else if (id == R.id.nav_about)
        {
            new CustomDialog(this,"To-Do Tasks","This app was created as a final project of mobile course at Shenkar-College of Engineering and Design by Almog Assulin and Tavor Cohen.\nVersion:1.0",null)
                    .showDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            return true;
        }
        else
            return false;

    }




}


