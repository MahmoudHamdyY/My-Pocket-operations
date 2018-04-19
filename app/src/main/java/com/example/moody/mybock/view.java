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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Comparator;

public class view extends AppCompatActivity {

    private ListView lv;
    MyData dba;
    String z,password;
    int itemPosition;
    ArrayList<String> AD , AN , AV;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    ListView leftDrawerList ;
    ArrayAdapter<String> navigationDrawerAdapter ;
    String[] leftSliderData ;
    int itemPosition2 ;long TT , TG ,S;
    private AdView mAdView;
    SharedPreferences.Editor shE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        final SharedPreferences myPrefs = getSharedPreferences("myPrefs", this.MODE_PRIVATE);
        shE= myPrefs.edit();
        password=myPrefs.getString("password",null);

        //-------ads

        mAdView = (AdView) findViewById(R.id.M2);
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
        //setSupportActionBar(toolbar);
        final Intent i = new Intent(this , allIN.class);
        lv = (ListView) findViewById(R.id.listView3);
        dba = new MyData(this);
        dba.open();
        TT = dba.totalSumT();
        TG = dba.totalSumG();
        S = TT - TG ;

        loadList();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                itemPosition   = position;
                z = String.valueOf(itemPosition + 1);
                final String a = parent.getAdapter().getItem(itemPosition).toString();
                TextView v = (TextView) view.findViewById(R.id.datta);
                TextView val = (TextView) view.findViewById(R.id.VALL);
                String V=val.getText().toString().substring(2);
                final long vall = Long.parseLong(V);
                final String F = v.getText().toString();
                i.putExtra("M" , F);
                String aa = dba.getDb(a);
                Toast.makeText(view.this ,F , Toast.LENGTH_LONG).show();
                AlertDialog.Builder ad = new AlertDialog.Builder(view.this);
                ad.setTitle("data");
                ad.setMessage(aa);
                ad.setPositiveButton("ok", null);
                ad.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder DELL = new AlertDialog.Builder(view.this);
                        DELL.setTitle("delete");
                        DELL.setMessage("are you sure you want to delete this data \n Password :");
                        final EditText input = new EditText(view.this);
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        DELL.setView(input);
                        DELL.setIcon(android.R.drawable.ic_delete);
                        DELL.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            String pass = input.getText().toString();
                            if(pass.equals(password)) {
                                if(vall>S) {
                                    AlertDialog.Builder al = new AlertDialog.Builder(view.this);
                                    al.setTitle("Note...");
                                    al.setMessage("you are going to remove money and your pocket haven't enough for that !.");
                                    al.setNegativeButton("Cancel", null);
                                    al.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dba.del(a);
                                            loadList();
                                        }
                                    });
                                    al.show();
                                }else {
                                    dba.del(a);
                                    loadList();
                                }
                            }else{
                                AlertDialog.Builder al = new AlertDialog.Builder(view.this);
                                al.setTitle("Error");
                                al.setMessage("Wrong password !.");
                                al.setPositiveButton("OK", null);
                                al.setIcon(android.R.drawable.ic_dialog_alert);
                                al.show();
                            }

                            }
                        });
                        DELL.setNegativeButton("NO",null);
                        DELL.show();
                    }
                });
                ad.setNeutralButton("See All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
            toolbar.setTitle("In");
            setSupportActionBar(toolbar);
        }
        initDrawer();

    }

    public void loadList() {
        AD = dba.SAD();
        AN = dba.SAN();
        AV = dba.VAN();
        //--------------------------------------------------------------
        CustomList adapter = new CustomList(this ,AD , AN ,AV);
        adapter.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);   //or whatever your sorting algorithm
            }
        });

        lv.setAdapter(adapter);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    Intent i = new Intent(view.this , MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
    private void nitView() {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        leftSliderData = getResources().getStringArray(R.array.LSD);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter=new ArrayAdapter<>( view.this, android.R.layout.simple_list_item_1, leftSliderData);
        leftDrawerList.setAdapter(navigationDrawerAdapter);
        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                itemPosition2 = position;
                drawerLayout.closeDrawers();
                if (itemPosition2 == 1 ){
                    Intent i = new Intent(view.this , view.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else if (itemPosition2 == 2 ){
                    Intent i = new Intent(view.this , viewOut.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else if(itemPosition2 == 0){
                    Intent i = new Intent(view.this , MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else if(itemPosition2 == 5 ){
                    AlertDialog.Builder ad = new AlertDialog.Builder(view.this);
                    ad.setTitle("Change password");
                    final EditText current = new EditText(view.this);
                    current.setHint("current password");
                    final EditText New = new EditText(view.this);
                    New.setHint("New password");
                    final EditText New2 = new EditText(view.this);
                    New2.setHint("New password");
                    current.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    New.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    New2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    LinearLayout l = new LinearLayout(view.this);
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
                                    Toast.makeText(view.this,"Saved",Toast.LENGTH_LONG).show();
                                }else{
                                    AlertDialog.Builder al= new AlertDialog.Builder(view.this);
                                    al.setTitle("Error");
                                    al.setMessage("New passwords doesn't match");
                                    al.setPositiveButton("OK",null);
                                    al.show();
                                    New.setText("");
                                    New2.setText("");
                                }
                            }else{
                                AlertDialog.Builder al= new AlertDialog.Builder(view.this);
                                al.setTitle("Error");
                                al.setMessage("Wrong password");
                                al.setPositiveButton("OK",null);
                                al.show();
                                current.setText("");
                            }

                        }
                    });
                    ad.show();
                }else if(itemPosition2 == 4 ){
                    AlertDialog.Builder al = new AlertDialog.Builder(view.this);
                    al.setTitle("Total");
                    al.setMessage("total in : " + TT + "\n total out : " + TG + "\n you should have : " + S);
                    al.setPositiveButton("ok", null);al.setNegativeButton("Clear all data", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder ad = new AlertDialog.Builder(view.this);
                            ad.setTitle("data");
                            ad.setMessage("all data will be deleted");
                            ad.setNegativeButton("NO", null);
                            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dba.restdata();
                                    Intent i = new Intent(view.this,MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                            });
                            ad.show();
                        }
                    });
                    al.show();
                }else if(itemPosition2 == 3){
                    Intent i = new Intent(view.this,Persons.class);
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
