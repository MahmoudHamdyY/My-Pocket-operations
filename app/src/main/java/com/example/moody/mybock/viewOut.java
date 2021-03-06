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
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Comparator;

public class viewOut extends AppCompatActivity {

    ListView v ;
    MyData dba;
    String z,password;
    int itemPosition;
    ArrayList<String> ADG , ANG ,VANG;
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
        setContentView(R.layout.activity_view_out);
        final SharedPreferences myPrefs = getSharedPreferences("myPrefs", this.MODE_PRIVATE);
        shE= myPrefs.edit();
        password=myPrefs.getString("password",null);

        MobileAds.initialize(this,getString(R.string.app_ID_ads));
        mAdView = (AdView) findViewById(R.id.ViewOut);
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


        v = (ListView) findViewById(R.id.listView);
        final Intent i = new Intent(this , allOut.class);
        dba = new MyData(this);
        dba.open();
        TT = dba.totalSumT();
        TG = dba.totalSumG();
        S = TT - TG ;

        loadListG();
        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                itemPosition   = position;
                z = String.valueOf(itemPosition + 1);
                final String a = parent.getAdapter().getItem(itemPosition).toString();
                TextView v = (TextView) view.findViewById(R.id.datta);
                final String F = v.getText().toString();

                i.putExtra("MG" , F);

                String aa = dba.getDbG(a);
                Toast.makeText(viewOut.this ,F , Toast.LENGTH_LONG).show();
                AlertDialog.Builder ad = new AlertDialog.Builder(viewOut.this);
                ad.setTitle("data");
                ad.setMessage(aa);
                ad.setPositiveButton("ok", null);
                ad.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder DELL = new AlertDialog.Builder(viewOut.this);
                        DELL.setTitle("delete");
                        DELL.setMessage("are you sure you want to delete this data \n Password :");
                        final EditText input = new EditText(viewOut.this);
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
                                    dba.delG(a);
                                    loadListG();
                                }else{
                                    AlertDialog.Builder al = new AlertDialog.Builder(viewOut.this);
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
                        /*ArrayList<String> M = new ArrayList<String>( );
                        M = dba.getDb2(a);*/

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
            toolbar.setTitle("Out");
            setSupportActionBar(toolbar);
        }
        initDrawer();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    Intent i = new Intent(viewOut.this , MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void loadListG() {
        ADG = dba.SADG();
        ANG = dba.SANG();
        VANG = dba.VANG();
        //--------------------------------------------------------------
        CustomList adapter = new CustomList(this ,ADG , ANG ,VANG);
        adapter.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);   //or whatever your sorting algorithm
            }
        });

        v.setAdapter(adapter);

    }

    private void nitView() {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        leftSliderData = getResources().getStringArray(R.array.LSD);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter=new ArrayAdapter<>( viewOut.this, android.R.layout.simple_list_item_1, leftSliderData);
        leftDrawerList.setAdapter(navigationDrawerAdapter);
        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                itemPosition2 = position;
                drawerLayout.closeDrawers();
                if (itemPosition2 == 1 ){
                    Intent i = new Intent(viewOut.this , view.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else if (itemPosition2 == 2 ){
                    Intent i = new Intent(viewOut.this , viewOut.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else if(itemPosition2 == 0){
                    Intent i = new Intent(viewOut.this , MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else if(itemPosition2 == 5 ){
                    AlertDialog.Builder ad = new AlertDialog.Builder(viewOut.this);
                    ad.setTitle("Change password");
                    final EditText current = new EditText(viewOut.this);
                    current.setHint("current password");
                    final EditText New = new EditText(viewOut.this);
                    New.setHint("New password");
                    final EditText New2 = new EditText(viewOut.this);
                    New2.setHint("New password");
                    current.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    New.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    New2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    LinearLayout l = new LinearLayout(viewOut.this);
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
                                    Toast.makeText(viewOut.this,"Saved",Toast.LENGTH_LONG).show();
                                }else{
                                    AlertDialog.Builder al= new AlertDialog.Builder(viewOut.this);
                                    al.setTitle("Error");
                                    al.setMessage("New passwords doesn't match");
                                    al.setPositiveButton("OK",null);
                                    al.show();
                                    New.setText("");
                                    New2.setText("");
                                }
                            }else{
                                AlertDialog.Builder al= new AlertDialog.Builder(viewOut.this);
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
                    AlertDialog.Builder al = new AlertDialog.Builder(viewOut.this);
                    al.setTitle("Total");
                    al.setMessage("total in : " + TT + "\n total out : " + TG + "\n you should have : " + S);
                    al.setPositiveButton("ok", null);al.setNegativeButton("Clear all data", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder ad = new AlertDialog.Builder(viewOut.this);
                            ad.setTitle("data");
                            ad.setMessage("all data will be deleted");
                            ad.setNegativeButton("NO", null);
                            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dba.restdata();
                                    Intent i = new Intent(viewOut.this,MainActivity.class);
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
                    Intent i = new Intent(viewOut.this,Persons.class);
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
