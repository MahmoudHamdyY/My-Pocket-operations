package com.example.moody.mybock;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class password extends AppCompatActivity {

    Button pass;
    EditText pass1,pass2;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        final SharedPreferences myPrefs = getSharedPreferences("myPrefs", this.MODE_PRIVATE);
        final SharedPreferences.Editor e = myPrefs.edit();

        pass= (Button) findViewById(R.id.button2);
        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1=pass1.getText().toString(),s2=pass2.getText().toString();
                if(s1.equals(s2)&&!s1.equals("")){
                    e.putString("password",s1);
                    e.commit();
                    Intent i = new Intent(password.this,WelcomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else{
                    AlertDialog.Builder al= new AlertDialog.Builder(password.this);
                    al.setTitle("Error");
                    al.setMessage("Password doesn't match");
                    al.setPositiveButton("OK",null);
                    al.show();
                    pass1.setText("");
                    pass2.setText("");
                }
            }
        });
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(password.this, MainActivity.class));
        finish();
    }
}
