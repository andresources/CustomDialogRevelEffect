package com.customdialogexample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

public class CircleRevealDialog {

    public enum AnimPosition {
        TARGET_VIEW,SCREEN_CENTER,FROM_TOP_LEFT,FROM_TOP_RIGHT,FROM_BOTTOM_LEFT,FROM_BOTTOM_RIGHT
    }

    private CircleRevealDialog(Builder builder){
        mContext = builder.mContext;
        mAnimPos = builder.mAnimPos;
        mTargetView = builder.mTargetView;
        fgCloseButton = builder.mfgCloseButton;
        mResLayout = builder.mResLayout;
    }
    private final Context mContext;
    private final AnimPosition mAnimPos;
    private final View mTargetView;
    private View mDialogView;
    private Dialog mDialog;
    private final boolean fgCloseButton;
    private final int mResLayout;
    public static class Builder{
        private Context mContext;
        private AnimPosition mAnimPos= AnimPosition.SCREEN_CENTER;
        private View mTargetView=null;
        private boolean mfgCloseButton=false;
        private int mResLayout;
        public Builder(Context context){
            mContext = context;
        }
        public Builder setAnimPosition(AnimPosition animPos){
            mAnimPos = animPos;
            return this;
        }
        public Builder setTargetView(View targetView){
            mTargetView = targetView;
            return this;
        }
        public Builder setLayout(int resLayout){
            mResLayout = resLayout;
            return this;
        }
        public Builder setCloseButtonVisibility(boolean mfgCloseButton){
            mfgCloseButton = mfgCloseButton;
            return this;
        }
        public CircleRevealDialog build(){
            return new CircleRevealDialog(this);
        }
    }
    public  void hideDialog(){
        revealShowHide(mTargetView,mDialogView, false, mDialog);
    }
    public  void hideDialogWithActivity(Class<?> cls){
        revealShowWithAnotherActivity(mTargetView,mDialogView,  mDialog,cls,mContext);
    }
    public Dialog getDialogView(){
        return mDialog;
    }
    public void showDialog() {
        mDialogView = View.inflate(mContext,mResLayout,null);
        mDialog = new Dialog(mContext,android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(mDialogView);
        /*ImageView imageView = (ImageView)mDialog.findViewById(R.id.closeDialogImg);
        if(fgCloseButton){
            imageView.setVisibility(View.VISIBLE);
        }else{
            imageView.setVisibility(View.GONE);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealShowHide(mTargetView,mDialogView, false, mDialog);
            }
        });*/

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                revealShowHide(mTargetView,mDialogView, true, null);
            }
        });
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK){
                    revealShowHide(mTargetView,mDialogView, false, mDialog);
                    return true;
                }
                return false;
            }
        });
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
    }
    int cx,cy;
    public void revealShowHide(View target,View dialogView, boolean b,  Dialog dialog) {
        View view = dialogView.findViewById(R.id.dialog);
        int w = view.getWidth();
        int h = view.getHeight();
        int endRadius = (int) Math.hypot(w, h);
        if(target!=null){
            cx = (int) (target.getX() + (target.getWidth()/2));
            cy = (int) (target.getY())+ target.getHeight() + 56;
        }else{
            switch (mAnimPos){
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
    public void revealShowWithAnotherActivity(View target,View dialogView, Dialog dialog,Class<?> cls,Context cnt) {
        View view = dialogView.findViewById(R.id.dialog);
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
