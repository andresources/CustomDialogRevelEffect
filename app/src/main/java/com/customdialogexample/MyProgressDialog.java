package com.customdialogexample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.gauravbhola.ripplepulsebackground.RipplePulseLayout;

public class MyProgressDialog {
    public enum AnimPosition {
        TARGET_VIEW,SCREEN_CENTER,FROM_TOP_LEFT,FROM_TOP_RIGHT,FROM_BOTTOM_LEFT,FROM_BOTTOM_RIGHT
    }
    static View target,dialogView;
    static Dialog dialog;
    static AnimPosition anim_position;
    public static void showDialog(Context cnt,View target1,boolean close,AnimPosition anim_position1) {
        target = target1;
        anim_position=anim_position1;
        dialogView = View.inflate(cnt,R.layout.dialog,null);
        dialog = new Dialog(cnt,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        ImageView imageView = (ImageView)dialog.findViewById(R.id.closeDialogImg);
        if(close){
            imageView.setVisibility(View.VISIBLE);
        }else{
            imageView.setVisibility(View.GONE);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealShowHide(target,dialogView, false, dialog);
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                revealShowHide(target,dialogView, true, null);
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK){
                    revealShowHide(target,dialogView, false, dialog);
                    return true;
                }
                return false;
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    public static void hideDialog(){
        revealShowHide(target,dialogView, false, dialog);
    }
    public static void hideDialogWithActivity(Class<?> cls,Context cnt){
        revealShowWithAnotherActivity(target,dialogView,  dialog,cls,cnt);
    }
    static int cx,cy;
    private static void revealShowHide(View target,View dialogView, boolean b, final Dialog dialog) {
        final View view = dialogView.findViewById(R.id.dialog);
        int w = view.getWidth();
        int h = view.getHeight();
        int endRadius = (int) Math.hypot(w, h);
        if(target!=null){
             cx = (int) (target.getX() + (target.getWidth()/2));
             cy = (int) (target.getY())+ target.getHeight() + 56;
        }else{
            switch (anim_position){
                case SCREEN_CENTER:
                    cy = view.getHeight()/2;
                    cx = view.getWidth()/2;
                    break;
                case FROM_TOP_LEFT:
                    cy = 0;
                    cx = 0;
                    break;
                case FROM_TOP_RIGHT:
                    cy = 0;
                    cx = view.getWidth()/2;
                    break;
                case FROM_BOTTOM_LEFT:
                    cy = view.getHeight();
                    cx = 0;
                    break;
                case FROM_BOTTOM_RIGHT:
                    cy = view.getHeight();
                    cx = view.getWidth();
                    break;
            }

        }
        if(b){
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx,cy, 0, endRadius);
            revealAnimator.setInterpolator(new AccelerateInterpolator());
            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(400);
            revealAnimator.start();
        } else {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);
            anim.setInterpolator(new AccelerateInterpolator());
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);
                }
            });
            anim.setDuration(400);
            anim.start();
        }
    }
    private static void revealShowWithAnotherActivity(View target,View dialogView, final Dialog dialog,Class<?> cls,Context cnt) {
        final View view = dialogView.findViewById(R.id.dialog);
        int cx,cy;
        int w = view.getWidth();
        int h = view.getHeight();
        int endRadius = (int) Math.hypot(w, h);
        if(target!=null){
            cx = (int) (target.getX() + (target.getWidth()/2));
            cy = (int) (target.getY())+ target.getHeight() + 56;
        }else{
            cy = view.getHeight()/2;
            cx = view.getWidth()/2;
        }
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);
            anim.setInterpolator(new AccelerateInterpolator());
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);
                    cnt.startActivity(new Intent(cnt,cls));
                }
            });
            anim.setDuration(400);
            anim.start();
    }
}
