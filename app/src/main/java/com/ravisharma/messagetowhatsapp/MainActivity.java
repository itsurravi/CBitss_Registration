package com.ravisharma.messagetowhatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static com.ravisharma.messagetowhatsapp.Main2Activity.getDate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String url = "https://script.google.com/macros/s/AKfycbzE15zANuanQFpd2W1MXxcOYxq9k6gID8UJH14cdUCsxExVcG4H/exec";
    private static String id = "1dSo2kv4_V6cleUhvlAR17nRPUO9Xzi1Pm7nSSynSF5E";

    TextInputEditText e1, e2, e3, e4;
    TextView tv, ty_message;
    Button b, pin_btn;
    CheckBox box;
    EditText pin;
    LayoutInflater li;
    View layout;
    AlertDialog a;

    String date, time, name, num1, num2, course;

    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        e1 = (TextInputEditText) findViewById(R.id.name);
        e2 = (TextInputEditText) findViewById(R.id.phone);
        e3 = (TextInputEditText) findViewById(R.id.whtsapp_number);
        e4 = (TextInputEditText) findViewById(R.id.course);
        tv = (TextView) findViewById(R.id.ofeuse);
        box = (CheckBox) findViewById(R.id.check);
        b = (Button) findViewById(R.id.click);

        tv.setOnClickListener(this);
        b.setOnClickListener(this);
        db = new DB(this);

        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);

        if (!sp.getBoolean("launch", false)) {
            db.AddData();
            SharedPreferences.Editor ed = sp.edit();
            ed.putBoolean("launch", true);
            ed.commit();
        }

        e3.setEnabled(false);

        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                e3.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    e3.setText(e2.getText().toString());
                    e3.setEnabled(false);
                    e4.requestFocus();
                } else {
                    e3.setText("");
                    e3.setEnabled(true);
                    e3.requestFocus();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == b) {
            name = e1.getText().toString().trim();
            num1 = e2.getText().toString().trim();
            num2 = e3.getText().toString().trim();
            course = e4.getText().toString().trim();

            Long timing = System.currentTimeMillis();

            date = getDate(timing, "dd/MM/yyyy");
            time = getDate(timing, "hh:mm:ss");

            if (name.isEmpty() || num1.isEmpty() || num2.isEmpty() || course.isEmpty()) {
                Snackbar.make(v, "Please Fill All Values", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (num1.length() < 10 || num2.length() < 10) {
                Snackbar.make(v, "Please enter Valid Number", Snackbar.LENGTH_LONG).show();
                return;
            }

            String message = "Hello " + name.toUpperCase() + ", Welcome to CBitss You are ensured for the course of " + course;

            String url = "http://203.129.225.69/API/WebSMS/Http/v1.0a/index.php?username=cbitss&password=123456&sender=CBitss&to=91" + num1 + "&message=" + message + "&reqid=1&format={json|text}&route_id=7";

            StringRequest sr = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    showThankYouMessage();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue r = Volley.newRequestQueue(this);
            r.add(sr);

        }
        if (v == tv) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Enter Pin for Office Use Only:-");
            li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            layout = li.inflate(R.layout.alert, null);

            builder.setView(layout);

            pin = (EditText) layout.findViewById(R.id.pin);
            pin_btn = (Button) layout.findViewById(R.id.button);
            pin_btn.setOnClickListener(this);


            a = builder.create();
            a.setCancelable(false);
            a.show();

        }
        if (v == pin_btn) {
            String pincheck = pin.getText().toString().trim();
            try {
                Cursor c = db.getPass(pincheck);
//                Toast.makeText(this, "" + c.getCount(), Toast.LENGTH_SHORT).show();
                if (c.getCount() > 0) {
                    if (name.isEmpty() || num1.isEmpty() || course.isEmpty()) {

                    }
                    Intent i = new Intent(MainActivity.this, Main2Activity.class);
                    i.putExtra("pin", pincheck);
                    i.putExtra("name", name);
                    i.putExtra("num1", num1);
                    i.putExtra("num2", num2);
                    i.putExtra("course", course);
                    startActivity(i);
                    e1.setText("");
                    e2.setText("");
                    e3.setText("");
                    e4.setText("");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Enter Valid Pin", Toast.LENGTH_SHORT).show();
            }

            if (a.isShowing()) {
                a.dismiss();
            }

        }
    }

    private void showThankYouMessage() {
        String text = name + "\n\n For Showing Your Interest In " + course;
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        LayoutInflater l = LayoutInflater.from(this);
        View v = l.inflate(R.layout.thankyou_alert, null);
        ty_message = (TextView) v.findViewById(R.id.msg);
        ty_message.setText(text);
        b.setView(v);
        final AlertDialog d = b.create();
        d.setCancelable(false);
        d.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (d.isShowing()) {
                    d.dismiss();
                    insertIntoSheet();
                }
            }
        }, 7000);
    }

    private void insertIntoSheet() {
        final ProgressDialog d = new ProgressDialog(this);
        d.setMessage("Please Wait...");
        d.setCancelable(false);
        d.show();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                        d.dismiss();
                        tv.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        d.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> m = new HashMap<>();
                m.put("id", id);
                m.put("date", date);
                m.put("time", time);
                m.put("name", name);
                m.put("mobile", num1);
                m.put("whatsapp", num2);
                m.put("course", course);
                return m;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(request);
    }


}
