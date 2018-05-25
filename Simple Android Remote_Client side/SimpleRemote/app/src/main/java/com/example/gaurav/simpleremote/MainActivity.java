package com.example.gaurav.simpleremote;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity  {

    Toolbar toolbar;
    private CharSequence mTitle;
    public static Socket clientSocket = null;
    DrawerLayout drawerLayout;
    private static ActionBarActivity thisActivity;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    public static ObjectInputStream objectInputStream = null;
    public static ObjectOutputStream objectOutputStream = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container,new ConnectFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Connect");


        navigationView=(NavigationView)findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.connect:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new ConnectFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Connect");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.touchpad:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new TouchpadFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("TouchPad");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.keyboard:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new KeyBoardFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("KeyBoard");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_power:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new PowerFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Power Option");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.presentation:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new PresentaionFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Presentation");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_share:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        Intent myintent=new Intent(Intent.ACTION_SEND);
                        myintent.setType("text/plain");
                        String sharebody="Hey Cool Application Here:-";
                        String ShareSub="file:///F:/Android%20Projects/SimpleRemote/app/src/main/assets/www/index.html";
                        myintent.putExtra(Intent.EXTRA_SUBJECT,ShareSub);
                        myintent.putExtra(Intent.EXTRA_TEXT,sharebody);
                        startActivity(Intent.createChooser(myintent,"Share Using"));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_webview:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new WebView());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("About Us");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                }


                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    //this method is called from fragments to send message to server (Desktop)
    public static void sendMessageToServer(String message) {
        if (MainActivity.clientSocket != null) {
            try {
                MainActivity.objectOutputStream.writeObject(message);
                MainActivity.objectOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
                socketException();
            }
        }
    }

    public static void sendMessageToServer(int message) {
        if (MainActivity.clientSocket != null) {
            try {
                MainActivity.objectOutputStream.writeObject(message);
                MainActivity.objectOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
                socketException();
            }
        }
    }
    private static void socketException() {
        Toast.makeText(thisActivity, "Connection Closed", Toast.LENGTH_LONG).show();
        if (MainActivity.clientSocket != null) {
            try {
                MainActivity.clientSocket.close();
                MainActivity.objectOutputStream.close();
                MainActivity.clientSocket = null;
            } catch(Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {  AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.myremote);
        builder.setMessage("Do You Want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();    }
}
