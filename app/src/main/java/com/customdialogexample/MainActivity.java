package com.customdialogexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    CircleRevealDialog c1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        c1=new CircleRevealDialog.Builder(this).setLayout(R.layout.dialog).setCloseButtonVisibility(true).build();
        c1.showDialog();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MyProgressDialog.showDialog(MainActivity.this,null,false, MyProgressDialog.AnimPosition.SCREEN_CENTER);
            }
        });
        ImageView imageView = (ImageView)c1.getDialogView().findViewById(R.id.closeDialogImg);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1.hideDialogWithActivity(SecondActivity.class);
            }
        });
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //MyProgressDialog.hideDialog();
                c1.hideDialogWithActivity(SecondActivity.class);
            }
        },10000);*/
    }

}