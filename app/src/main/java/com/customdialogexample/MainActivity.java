package com.customdialogexample;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProgressDialog.showDialog(MainActivity.this,null,false, MyProgressDialog.AnimPosition.SCREEN_CENTER);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //MyProgressDialog.hideDialog();
                MyProgressDialog.hideDialogWithActivity(SecondActivity.class,MainActivity.this);
            }
        },10000);
    }

}