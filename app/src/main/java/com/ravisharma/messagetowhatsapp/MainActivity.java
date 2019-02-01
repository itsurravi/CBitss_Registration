package com.ravisharma.messagetowhatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.PatternMatcher;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.regex.Pattern;

import static com.ravisharma.messagetowhatsapp.Main2Activity.getDate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String url = "https://script.google.com/macros/s/AKfycbzE15zANuanQFpd2W1MXxcOYxq9k6gID8UJH14cdUCsxExVcG4H/exec";
    private static String id = "1dSo2kv4_V6cleUhvlAR17nRPUO9Xzi1Pm7nSSynSF5E";
    boolean radio = true;
    int card_no = 0;

    EditText name_edit, phone_edit, whPhone_edit, course_edit, email_edit;
    TextView tv, ty_message, c1, c2, c3;
    Button submit, pin_btn, next;
    RadioGroup rgroup;
    EditText pin;

    CardView card_name, card_course, card_phone, card_check, card_whatsapp, card_email;
    ImageView f1, f2, f3;

    LayoutInflater li;
    View layout;
    AlertDialog a;

    String date, time, name, num1, num2, course, email;

    DB db;

    RadioButton r1, r2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //card id
        card_name = (CardView) findViewById(R.id.card_name);
        card_course = (CardView) findViewById(R.id.card_course);
        card_phone = (CardView) findViewById(R.id.card_phone);
        card_check = (CardView) findViewById(R.id.card_check);
        card_whatsapp = (CardView) findViewById(R.id.card_whatsapp);
        card_email = (CardView) findViewById(R.id.card_email);

        //imageview id
        f1 = (ImageView) findViewById(R.id.first);
        f2 = (ImageView) findViewById(R.id.sec);
        f3 = (ImageView) findViewById(R.id.third);

        //textview id
        c1 = (TextView) findViewById(R.id.tfirst);
        c2 = (TextView) findViewById(R.id.tsec);
        c3 = (TextView) findViewById(R.id.tthird);
        tv = (TextView) findViewById(R.id.ofeuse);

        //radiobutton id
        r1 = (RadioButton) findViewById(R.id.yes);
        r2 = (RadioButton) findViewById(R.id.no);

        //EditText id
        name_edit = (EditText) findViewById(R.id.name);
        phone_edit = (EditText) findViewById(R.id.phone);
        whPhone_edit = (EditText) findViewById(R.id.whtsapp_number);
        course_edit = (EditText) findViewById(R.id.course);
        email_edit = (EditText) findViewById(R.id.email);

        //RadioGroup id
        rgroup = (RadioGroup) findViewById(R.id.rGroup);

        //Button id
        submit = (Button) findViewById(R.id.click);
        next = (Button) findViewById(R.id.next);

        next.setEnabled(false);
        next.setBackground(getDrawable(R.drawable.button_design_disable));

        //ClickListener
        tv.setOnClickListener(this);
        submit.setOnClickListener(this);
        next.setOnClickListener(this);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.enter_zoom1);
        card_name.startAnimation(anim);
        next.startAnimation(anim);

        db = new DB(this);

        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);

        if (!sp.getBoolean("launch", false)) {
            db.AddData();
            SharedPreferences.Editor ed = sp.edit();
            ed.putBoolean("launch", true);
            ed.commit();
        }

        textchangeListener();

        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (!next.isEnabled()) {
                    int select = group.getCheckedRadioButtonId();
                    if (select > -1) {
                        next.setEnabled(true);
                        next.setBackground(getDrawable(R.drawable.button_design_enable));
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == submit) {
            name = name_edit.getText().toString().trim();
            num1 = phone_edit.getText().toString().trim();
            num2 = whPhone_edit.getText().toString().trim();
            course = course_edit.getText().toString().trim();
            email = email_edit.getText().toString().trim();

            if (name.isEmpty() || num1.isEmpty() || num2.isEmpty() || course.isEmpty()) {
                Toast.makeText(this, "Please Fill All Values", Toast.LENGTH_SHORT).show();
                return;
            }
            if (num1.length()<10)
            {
                phone_edit.setError("Please Enter Valid Number");
                phone_edit.requestFocus();
                return;
            }
            if (num2.length()<10)
            {
                whPhone_edit.setError("Please Enter Valid Number");
                whPhone_edit.requestFocus();
                return;
            }

            if(!email.isEmpty())
            {
                if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                    email_edit.setError("Please Enter Valid Email Id");
                    email_edit.requestFocus();
                }
                return;
            }

            Long timing = System.currentTimeMillis();

            date = getDate(timing, "dd/MM/yyyy");
            time = getDate(timing, "hh:mm:ss");

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
                    name_edit.setText("");
                    phone_edit.setText("");
                    whPhone_edit.setText("");
                    course_edit.setText("");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Enter Valid Pin", Toast.LENGTH_SHORT).show();
            }
            if (a.isShowing()) {
                a.dismiss();
            }
        }

        if (v == next) {
            showCard();
        }
    }

    private void textchangeListener() {
        name_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    next.setEnabled(true);
                    next.setBackground(getDrawable(R.drawable.button_design_enable));
                } else {
                    next.setEnabled(false);
                    next.setBackground(getDrawable(R.drawable.button_design_disable));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                whPhone_edit.setText(s.toString());
                if (count > 0) {
                    next.setEnabled(true);
                    next.setBackground(getDrawable(R.drawable.button_design_enable));
                } else {
                    next.setEnabled(false);
                    next.setBackground(getDrawable(R.drawable.button_design_disable));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        whPhone_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (whPhone_edit.isEnabled()) {
                    if (count > 0) {
                        next.setEnabled(true);
                        next.setBackground(getDrawable(R.drawable.button_design_enable));
                    } else {
                        next.setEnabled(false);
                        next.setBackground(getDrawable(R.drawable.button_design_disable));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        course_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    next.setEnabled(true);
                    next.setBackground(getDrawable(R.drawable.button_design_enable));
                } else {
                    next.setEnabled(false);
                    next.setBackground(getDrawable(R.drawable.button_design_disable));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        whPhone_edit.setEnabled(false);
    }

    private void showCard() {
        if (card_check.getVisibility() == View.VISIBLE) {
            if (radio) {
                int selectedId = rgroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(selectedId);
                if (selectedId == -1) {
                    Toast.makeText(this, "Please Choose an Option", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (rb.getText().toString().equals("Yes")) {
                        card_no++;
                        Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.exit_to_right);
                        next.startAnimation(anim);
                        next.setVisibility(View.GONE);

                    } else if (rb.getText().toString().equals("No")) {
                        whPhone_edit.setText("");
                        whPhone_edit.setEnabled(true);
                        whPhone_edit.requestFocus();
                    }
                    radio = false;
                    rgroup.setEnabled(false);
                    r1.setEnabled(false);
                    r2.setEnabled(false);

                }
            }
        }

        if(card_no==2)
        {
            String number = phone_edit.getText().toString().trim();
            if(number.length()<10)
            {
                phone_edit.setError("Please Enter Valid Number");
                phone_edit.requestFocus();
                return;
            }
        }
        if(card_no==4)
        {
            String number = whPhone_edit.getText().toString().trim();
            if(number.length()<10)
            {
                whPhone_edit.setError("Please Enter Valid Number");
                whPhone_edit.requestFocus();
                return;
            }
        }

        switch (card_no) {
            case 0:
                card_no++;
                image_animation_start();
                next.setEnabled(false);
                next.setBackground(getDrawable(R.drawable.button_design_disable));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        card_course.setVisibility(View.VISIBLE);
                        course_edit.requestFocus();
                        card_course.startAnimation(setZoomAnimation());
                    }
                }, 3400);
                break;
            case 1:
                card_no++;
                image_animation_start();
                next.setEnabled(false);
                next.setBackground(getDrawable(R.drawable.button_design_disable));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        card_phone.setVisibility(View.VISIBLE);
                        phone_edit.requestFocus();
                        card_phone.startAnimation(setZoomAnimation());
                    }
                },3400);
                break;
            case 2:
                card_no++;
                image_animation_start();
                next.setEnabled(false);
                next.setBackground(getDrawable(R.drawable.button_design_disable));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        card_check.setVisibility(View.VISIBLE);
                        card_check.startAnimation(setZoomAnimation());
                    }
                }, 3400);
                break;
            case 3:
                card_no++;
                image_animation_start();
                next.setEnabled(false);
                next.setBackground(getDrawable(R.drawable.button_design_disable));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        whPhone_edit.requestFocus();
                        card_whatsapp.setVisibility(View.VISIBLE);
                        card_whatsapp.startAnimation(setZoomAnimation());
                    }
                }, 3400);
                break;
            case 4:
                card_no++;
                image_animation_start();
                Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.exit_to_right);
                next.startAnimation(anim);
                next.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        email_edit.requestFocus();
                        submit.setVisibility(View.VISIBLE);
                        card_email.setVisibility(View.VISIBLE);
                        submit.startAnimation(setZoomAnimation());
                        card_email.startAnimation(setZoomAnimation());
                    }
                }, 3400);
                break;
        }
    }

    private Animation setZoomAnimation() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.enter_zoom);
        return anim;
    }

    private void image_animation_start() {

        Animation img_3 = AnimationUtils.loadAnimation(this, R.anim.img_3_start);
        Animation img_2 = AnimationUtils.loadAnimation(this, R.anim.img_2_start);
        Animation img_1 = AnimationUtils.loadAnimation(this, R.anim.img_1_start);

        Animation anim_1 = AnimationUtils.loadAnimation(this, R.anim.text1);
        Animation anim_2 = AnimationUtils.loadAnimation(this, R.anim.text2);
        Animation anim_3 = AnimationUtils.loadAnimation(this, R.anim.text3);

        c1.setVisibility(View.VISIBLE);
        c2.setVisibility(View.VISIBLE);
        c3.setVisibility(View.VISIBLE);

        f1.setVisibility(View.VISIBLE);
        f2.setVisibility(View.VISIBLE);
        f3.setVisibility(View.VISIBLE);

        f1.startAnimation(img_3);
        f2.startAnimation(img_2);
        f3.startAnimation(img_1);

        c1.startAnimation(anim_3);
        c2.startAnimation(anim_2);
        c3.startAnimation(anim_1);
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
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
        }, 5000);
    }

    private void insertIntoSheet() {
        if (email.isEmpty()) {
            email = "Not Available";
        }
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
                        Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
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
                m.put("email", email);
                return m;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(request);
    }

}
