package com.ravisharma.messagetowhatsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            TextView tv = (TextView)findViewById(R.id.wel);
            Animation am = AnimationUtils.loadAnimation(this, R.anim.drop_up_to_down);
            tv.setAnimation(am);

            ImageView iv = (ImageView)findViewById(R.id.cb_logo);
            Animation am2 = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
            iv.startAnimation(am2);

            Animation am3 = AnimationUtils.loadAnimation(this, R.anim.slide_left_to_right);

            SwipeButton swipeButton = (SwipeButton)findViewById(R.id.swipe);

            swipeButton.setAnimation(am3);

            swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
                @Override
                public void onStateChange(boolean active) {
                    if(active)
                    {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();

                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            });

        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setMessage("No Internet Connection. Make sure that wi-fi or mobile data is turned on, then try again.");
            ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    WelcomeActivity.this.finish();
                }
            });

            AlertDialog dialog = ad.create();
            dialog.setCancelable(false);
            dialog.show();

        }
    }
}
