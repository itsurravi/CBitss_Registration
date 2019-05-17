package com.ravisharma.messagetowhatsapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {

    String name, number1, number2, course, pincheck, detail_by;
    DB db;
    CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        cb = findViewById(R.id.check);

        db = new DB(this);

        Bundle b = getIntent().getExtras();

        pincheck = b.getString("pin");
        name = b.getString("name");
        number1 = b.getString("num1");
        number2 = b.getString("num2");
        course = b.getString("course");


        Cursor c = db.getPass(pincheck);
        if (c.getCount() > 0) {
            c.moveToFirst();
            detail_by = c.getString(c.getColumnIndex(DB.USER));
        }

        findViewById(R.id.group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goto_whatsapp();
            }
        });
        findViewById(R.id.client).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_to_client(number2);
            }
        });

    }

    private void goto_whatsapp() {

        Long timing = System.currentTimeMillis();

        String date = getDate(timing, "dd/MM/yyyy");
        String time = getDate(timing, "hh:mm:ss");
        PackageManager pm = getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            String text="";

            if(cb.isChecked()) {
                text = "Date: " + date +
                        "\nTime: " + time +
                        "\n\nName: " + name +
                        "\nPhone No.: " + number1 +
                        "\nWhatsapp No.: " + number2 +
                        "\nCourse: " + course +
                        "\n\nBy:- " + detail_by;

            }
            else
            {
                text = "Date: " + date +
                        "\nTime: " + time +
                        "\n\nName: " + name +
                        "\nPhone No.: " + number1 +
                        "\nWhatsapp No.: " + number2 +
                        "\nCourse: " + course  +
                        "\n\nNote:- This Number is not on WhatsApp." +
                        "\n\nBy:- " + detail_by;
            }
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    /*
    * Dear Ravi,

    Thank you for showing your interest in our Professional Linux Course.

    We Assure You to Deliver Quality Training.

    Thank you.
    *
    *
    * */
    public void send_to_client(String phone) {


            String message = "Dear *" + name + "*,\n\nThank you for showing your interest in our Professional *"
                    + course + "* Course. \n\nWe Assure You to Deliver Quality Training.\n\n"
                    + "Thank you. \n\n CBitss Technologies";

            PackageManager packageManager = getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            try {
                String url="";
                if(phone.equals(number1)) {
                    url = "https://api.whatsapp.com/send?phone=+91" + phone + "&text=" + URLEncoder.encode(message, "UTF-8");
                }
                else {
                    url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode(message, "UTF-8");
                }
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i);
                }
            } catch (Exception e) {
                Toast.makeText(this, "Whatsapp not Installed", Toast.LENGTH_SHORT).show();
            }
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void startAgain(View view) {
        Intent intent = new Intent(Main2Activity.this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //this will always start your activity as a new task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
