package com.example.moody.mybock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class allOut extends AppCompatActivity {

    ListView lv;
    TextView v;
    Button del;
    MyData dba;
    String M,password;
    long Summ;
    ArrayList<String> AD11 ;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    ListView leftDrawerList ;
    ArrayAdapter<String> navigationDrawerAdapter ;
    String[] leftSliderData ;
    int itemPosition ;long TT , TG ,S;
    private  AdView mAdView;
    SharedPreferences.Editor shE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_out);
        final SharedPreferences myPrefs = getSharedPreferences("myPrefs", this.MODE_PRIVATE);
        shE= myPrefs.edit();
        password=myPrefs.getString("password",null);

        MobileAds.initialize(this,getString(R.string.app_ID_ads));
        mAdView = (AdView) findViewById(R.id.allout);
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

        lv = (ListView) findViewById(R.id.listViewG);
        v = (TextView) findViewById(R.id.sumG);


        del = (Button) findViewById(R.id.alldelG);
        dba = new MyData(this);
        dba.open();
        TT = dba.totalSumT();
        TG = dba.totalSumG();
        S = TT - TG ;

        M = getIntent().getStringExtra("MG");
        AD11 = dba.getDb2G(M);
        ColoredList adapter = new ColoredList(this,AD11);
        lv.setAdapter(adapter);
        Summ = dba.getSumG(M);
        v.setText(getString(R.string.sum)+ Summ);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(allOut.this);
                ad.setTitle("data");
                ad.setMessage("all data will be deleted \n Password :");
                final EditText input = new EditText(allOut.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                ad.setView(input);
                ad.setIcon(android.R.drawable.ic_delete);
                ad.setNegativeButton("NO", null);
                ad.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pass = input.getText().toString();
                        if(pass.equals(password)) {
                            dba.del2G(M);
                            Intent i = new Intent(allOut.this , viewOut.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }else{
                            AlertDialog.Builder al = new AlertDialog.Builder(allOut.this);
                            al.setTitle("Error");
                            al.setMessage("Wrong password !.");
                            al.setPositiveButton("OK", null);
                            al.setIcon(android.R.drawable.ic_dialog_alert);
                            al.show();
                        }
                    }
                });
                ad.show();
            }
        });
        nitView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        leftSliderData = getResources().getStringArray(R.array.LSD);

        if (toolbar != null) {
            toolbar.setTitle("To : " +M);
            setSupportActionBar(toolbar);
        }
        initDrawer();
    }


    private void nitView() {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        leftSliderData = getResources().getStringArray(R.array.LSD);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter=new ArrayAdapter<>( allOut.this, android.R.layout.simple_list_item_1, leftSliderData);
        leftDrawerList.setAdapter(navigationDrawerAdapter);
        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                itemPosition = position;
                drawerLayout.closeDrawers();
                if (itemPosition == 1 ){
                    Intent i = new Intent(allOut.this , view.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else if (itemPosition == 2 ){
                    Intent i = new Intent(allOut.this , viewOut.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else if(itemPosition == 0){
                    Intent i = new Intent(allOut.this , MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else if(itemPosition == 5 ){
                    AlertDialog.Builder ad = new AlertDialog.Builder(allOut.this);
                    ad.setTitle("Change password");
                    final EditText current = new EditText(allOut.this);
                    current.setHint("current password");
                    final EditText New = new EditText(allOut.this);
                    New.setHint("New password");
                    final EditText New2 = new EditText(allOut.this);
                    New2.setHint("New password");
                    current.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    New.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    New2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    LinearLayout l = new LinearLayout(allOut.this);
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
                                    Toast.makeText(allOut.this,"Saved",Toast.LENGTH_LONG).show();
                                }else{
                                    AlertDialog.Builder al= new AlertDialog.Builder(allOut.this);
                                    al.setTitle("Error");
                                    al.setMessage("New passwords doesn't match");
                                    al.setPositiveButton("OK",null);
                                    al.show();
                                    New.setText("");
                                    New2.setText("");
                                }
                            }else{
                                AlertDialog.Builder al= new AlertDialog.Builder(allOut.this);
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
                    AlertDialog.Builder al = new AlertDialog.Builder(allOut.this);
                    al.setTitle("Total");
                    al.setMessage("total in : " + TT + "\n total out : " + TG + "\n you should have : " + S);
                    al.setPositiveButton("ok", null);al.setNegativeButton("Clear all data", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder ad = new AlertDialog.Builder(allOut.this);
                            ad.setTitle("data");
                            ad.setMessage("all data will be deleted");
                            ad.setNegativeButton("NO", null);
                            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dba.restdata();
                                    Intent i = new Intent(allOut.this,MainActivity.class);
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
                    Intent i = new Intent(allOut.this,Persons.class);
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
