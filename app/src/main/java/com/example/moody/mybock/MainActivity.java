package com.example.moody.mybock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    ListView leftDrawerList ;
    ArrayAdapter<String> navigationDrawerAdapter ;
    String[] leftSliderData ;
    EditText takedNum , takedFrom , takedNots , giveNum , giveTo , giveNots ;
    Button add , give ;
    TextView tv,t1,t2,t3,t4,t5,t6,t7,t8 ;
    Typeface tf;
    MyData dba ;
    int itemPosition ;long TT ,TG ,S;
    ProgressBar progressBar;
    String password;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    SharedPreferences.Editor shE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences myPrefs = getSharedPreferences("myPrefs", this.MODE_PRIVATE);
        shE= myPrefs.edit();
        password=myPrefs.getString("password",null);

        //--------------ADS---
            MobileAds.initialize(this,getString(R.string.app_ID_ads));
            mAdView = (AdView) findViewById(R.id.ko);
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


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.inter_on_Add_UID));


        //--------------------
        tf = Typeface.createFromAsset(getAssets(),"fonts/F1.ttf");
        fonts();
        takedNum = (EditText) findViewById(R.id.takeNum);
        takedFrom = (EditText) findViewById(R.id.takeFrom);
        takedNots = (EditText) findViewById(R.id.takeNotes);
        giveNum = (EditText) findViewById(R.id.giveNum);
        giveTo = (EditText) findViewById(R.id.giveTo);
        giveNots = (EditText) findViewById(R.id.giveNotes);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        tv = (TextView) findViewById(R.id.MyMoney);
        tv.setTypeface(tf);
        dba = new MyData(this);
        dba.open();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Home");
            setSupportActionBar(toolbar);
        }

        total();

        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(takedNum.getText().toString().length() == 0 ||takedFrom.getText().toString().length() == 0 ){
                    AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
                    al.setTitle("Error");
                    al.setMessage("Please Enter data");
                    al.setPositiveButton("ok", null);
                    al.show();
                }else {
                    long Tnum = Long.parseLong(takedNum.getText().toString());
                    String Tname = takedFrom.getText().toString();
                    String Tnots = takedNots.getText().toString();
                    dba.insertTData(Tnum , getDateTime() , Tname , Tnots);
                    Toast.makeText(MainActivity.this , "Saved" + getDateTime() , Toast.LENGTH_LONG).show();
                    takedNum.setText("");
                    takedNots.setText("");
                    takedFrom.setText("");
                    TT = 0; TG = 0; S =0;
                    total();

                }
                add();

            }
        });
        give = (Button) findViewById(R.id.give);
        give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(giveNum.getText().toString().length() == 0 ||giveTo.getText().toString().length() == 0 ){
                    AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
                    al.setTitle("Error");
                    al.setMessage("Please Enter data");
                    al.setPositiveButton("ok", null);
                    al.show();
                }else {
                    long Gnum = Long.parseLong(giveNum.getText().toString());
                    if(Gnum>S){
                        AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
                        al.setTitle("Note...");
                        al.setMessage("you are going to give out more than that you have !");
                        al.setNegativeButton("Cancel",null);
                        al.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                long gnum = Long.parseLong(giveNum.getText().toString());
                                String Gname = giveTo.getText().toString();
                                String Gnots = giveNots.getText().toString();
                                dba.insertGData(gnum , getDateTime() ,Gname , Gnots);
                                Toast.makeText(MainActivity.this , "Saved" , Toast.LENGTH_LONG).show();
                                giveNum.setText("");
                                giveNots.setText("");
                                giveTo.setText("");
                                TT = 0; TG = 0; S =0;
                                total();
                            }
                        });
                        al.show();
                    }else {
                        String Gname = giveTo.getText().toString();
                        String Gnots = giveNots.getText().toString();
                        dba.insertGData(Gnum, getDateTime(), Gname, Gnots);
                        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_LONG).show();
                        giveNum.setText("");
                        giveNots.setText("");
                        giveTo.setText("");
                        TT = 0;
                        TG = 0;
                        S = 0;
                        total();
                    }
                }
                add();
            }
        });
        nitView();

        leftSliderData = getResources().getStringArray(R.array.LSD);


        initDrawer();



    }
    public void add(){
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mInterstitialAd.show();
            }
        });
    }
    public void fonts(){
        t1= (TextView) findViewById(R.id.textView);
        t2= (TextView) findViewById(R.id.textView3);
        t3= (TextView) findViewById(R.id.textView2);
        t4= (TextView) findViewById(R.id.textView4);
        t5= (TextView) findViewById(R.id.textView5);
        t6= (TextView) findViewById(R.id.textView6);
        t7= (TextView) findViewById(R.id.textView7);
        t8= (TextView) findViewById(R.id.textView8);
        t1.setTypeface(tf);
        t2.setTypeface(tf);
        t3.setTypeface(tf);
        t4.setTypeface(tf);
        t5.setTypeface(tf);
        t6.setTypeface(tf);
        t7.setTypeface(tf);
        t8.setTypeface(tf);
    }
    public void total(){
        TT = 0 ; TG =0; S = 0;
        dba = new MyData(this);
        dba.open();
        TT = dba.totalSumT();
        TG = dba.totalSumG();
        S = TT - TG ;
        double pro=(double)S/TT;
        pro*=100;
        progressBar.setMax(100);
        if(S>=0)
            progressBar.setProgress((int) pro);
        else
            progressBar.setProgress(100);
        String st =getString(R.string.have_in_my_pocket);
        st+=Long.toString(S);
        tv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv.setText(st);
        tv.setTypeface(tf);
        if(pro >=50){
            progressBar.getProgressDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        }else if(pro >=25){
            progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#fff400"), android.graphics.PorterDuff.Mode.SRC_IN);
        }else if(pro >=10){
            progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#ff7400"), android.graphics.PorterDuff.Mode.SRC_IN);
        }else {
            progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#ff0000"), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MMM-yyyy      hh:mm:ss a", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void nitView() {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        leftSliderData = getResources().getStringArray(R.array.LSD);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter=new ArrayAdapter<>( MainActivity.this, android.R.layout.simple_list_item_1, leftSliderData);
        leftDrawerList.setAdapter(navigationDrawerAdapter);
        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                itemPosition = position;
                drawerLayout.closeDrawers();
                 if (itemPosition == 1 ){
                    Intent i = new Intent(MainActivity.this , view.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     add();
                    startActivity(i);
                }else if (itemPosition == 2 ){
                     Intent i = new Intent(MainActivity.this , viewOut.class);
                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     add();
                     startActivity(i);
                 }else if(itemPosition == 0){
                     Intent i = new Intent(MainActivity.this , MainActivity.class);
                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     add();
                     startActivity(i);
                 }else if(itemPosition == 4 ){
                     AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
                     al.setTitle("Total");
                     al.setMessage("total in : " + TT + "\n total out : " + TG + "\n you should have : " + S);
                     al.setPositiveButton("ok", null);
                     al.setNegativeButton("Clear all data", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                             ad.setTitle("data");
                             ad.setMessage("all data will be deleted");
                             ad.setNegativeButton("NO", null);
                             ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     dba.restdata();
                                     Intent i = new Intent(MainActivity.this,MainActivity.class);
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
                     Intent i = new Intent(MainActivity.this,Persons.class);
                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     add();
                     startActivity(i);
                 }else if(itemPosition == 5){
                     AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                     ad.setTitle("Change password");
                     final EditText current = new EditText(MainActivity.this);
                     current.setHint("current password");
                     final EditText New = new EditText(MainActivity.this);
                     New.setHint("New password");
                     final EditText New2 = new EditText(MainActivity.this);
                     New2.setHint("New password");
                     current.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                     New.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                     New2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                     LinearLayout l = new LinearLayout(MainActivity.this);
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
                                     Toast.makeText(MainActivity.this,"Saved",Toast.LENGTH_LONG).show();
                                 }else{
                                     AlertDialog.Builder al= new AlertDialog.Builder(MainActivity.this);
                                     al.setTitle("Error");
                                     al.setMessage("New passwords doesn't match");
                                     al.setPositiveButton("OK",null);
                                     al.show();
                                     New.setText("");
                                     New2.setText("");
                                 }
                             }else{
                                 AlertDialog.Builder al= new AlertDialog.Builder(MainActivity.this);
                                 al.setTitle("Error");
                                 al.setMessage("Wrong password");
                                 al.setPositiveButton("OK",null);
                                 al.show();
                                 current.setText("");
                             }

                         }
                     });
                     ad.show();
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
               /* getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );*/
               hideKeyboardFrom(MainActivity.this,drawerView);
                super.onDrawerOpened(drawerView);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    finishAffinity();
            }

        }
        return super.onKeyDown(keyCode, event);
    }



    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
