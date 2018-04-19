package com.example.moody.mybock;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Persons extends AppCompatActivity {

    ListView lv;
    ArrayList<String>arr;
    MyData dba;
    Toolbar toolbar;
    String z,password;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    ListView leftDrawerList ;
    ArrayAdapter<String> navigationDrawerAdapter ;
    String[] leftSliderData ;
    int itemPosition ,itemPosition2;long TT , TG ,S;
    SharedPreferences.Editor shE;

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons);

        final SharedPreferences myPrefs = getSharedPreferences("myPrefs", this.MODE_PRIVATE);
        shE= myPrefs.edit();
        password=myPrefs.getString("password",null);

        MobileAds.initialize(this,getString(R.string.app_ID_ads));
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mAdView.setVisibility(View.GONE);
            }
        });

        lv = (ListView) findViewById(R.id.personsList);
        dba = new MyData(this);
        dba.open();
        TT = dba.totalSumT();
        TG = dba.totalSumG();
        S = TT - TG ;
        arr = dba.names();
        Set<String> st = new HashSet<>();
        st.addAll(arr);
        arr.clear();
        arr.addAll(st);
        ColoredList adapter1 = new ColoredList(this,arr);
        lv.setAdapter(adapter1);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemPosition2   = position;
                z = String.valueOf(itemPosition2 + 1);
                TextView v = (TextView) view.findViewById(R.id.lestCo);
                final String F = v.getText().toString();
                AlertDialog.Builder ad = new AlertDialog.Builder(Persons.this);
                ad.setTitle("get all data");
                ad.setMessage("For what you want to view");
                ad.setPositiveButton("Total IN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i =new Intent(Persons.this,allIN.class);
                        i.putExtra("M",F);
                        startActivity(i);
                    }
                });
                ad.setNegativeButton("Total OUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i =new Intent(Persons.this,allOut.class);
                        i.putExtra("MG",F);
                        startActivity(i);
                    }
                });
                ad.show();
            }
        });
        nitView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        leftSliderData = getResources().getStringArray(R.array.LSD);

        if (toolbar != null) {
            toolbar.setTitle("Persons ");
            setSupportActionBar(toolbar);
        }
        initDrawer();
    }

    private void nitView() {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        leftSliderData = getResources().getStringArray(R.array.LSD);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter=new ArrayAdapter<>( Persons.this, android.R.layout.simple_list_item_1, leftSliderData);
        leftDrawerList.setAdapter(navigationDrawerAdapter);
        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                itemPosition = position;
                drawerLayout.closeDrawers();
                if (itemPosition == 1 ){
                    Intent i = new Intent(Persons.this , view.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else if (itemPosition == 2 ){
                    Intent i = new Intent(Persons.this , viewOut.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else if(itemPosition == 0){
                    Intent i = new Intent(Persons.this , MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else if(itemPosition == 5 ){
                    AlertDialog.Builder ad = new AlertDialog.Builder(Persons.this);
                    ad.setTitle("Change password");
                    final EditText current = new EditText(Persons.this);
                    current.setHint("current password");
                    final EditText New = new EditText(Persons.this);
                    New.setHint("New password");
                    final EditText New2 = new EditText(Persons.this);
                    New2.setHint("New password");
                    current.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    New.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    New2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    LinearLayout l = new LinearLayout(Persons.this);
                    l.setOrientation(LinearLayout.VERTICAL);
                    l.addView(current);
                    l.addView(New);
                    l.addView(New2);
                    ad.setView(l);
                    ad.setIcon(android.R.drawable.ic_dialog_info);
                    ad.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String a,b,c;
                            a=current.getText().toString();
                            b=New.getText().toString();
                            c=New2.getText().toString();
                            if(a.equals(password)){
                                if(b.equals(c)&&!b.equals("")){
                                    shE.putString("password",b);
                                    shE.commit();
                                    Toast.makeText(Persons.this,"Saved",Toast.LENGTH_LONG).show();
                                }else{
                                    AlertDialog.Builder al= new AlertDialog.Builder(Persons.this);
                                    al.setTitle("Error");
                                    al.setMessage("New passwords doesn't match");
                                    al.setPositiveButton("OK",null);
                                    al.show();
                                    New.setText("");
                                    New2.setText("");
                                }
                            }else{
                                AlertDialog.Builder al= new AlertDialog.Builder(Persons.this);
                                al.setTitle("Error");
                                al.setMessage("Wrong password");
                                al.setPositiveButton("OK",null);
                                al.show();
                                current.setText("");
                            }

                        }
                    });
                    ad.show();
                }else if(itemPosition == 4 ){
                    AlertDialog.Builder al = new AlertDialog.Builder(Persons.this);
                    al.setTitle("Total");
                    al.setMessage("total in : " + TT + "\n total out : " + TG + "\n you should have : " + S);
                    al.setPositiveButton("ok", null);al.setNegativeButton("Clear all data", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder ad = new AlertDialog.Builder(Persons.this);
                            ad.setTitle("data");
                            ad.setMessage("all data will be deleted");
                            ad.setNegativeButton("NO", null);
                            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dba.restdata();
                                    Intent i = new Intent(Persons.this,MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                            });
                            ad.show();
                        }
                    });
                    al.show();
                }else if(itemPosition == 3){
                    Intent i = new Intent(Persons.this,Persons.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }



            }
        });

    }

    private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
