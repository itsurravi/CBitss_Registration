package com.ravisharma.messagetowhatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import static com.ravisharma.messagetowhatsapp.Main2Activity.getDate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String url = "https://script.google.com/macros/s/AKfycbzE15zANuanQFpd2W1MXxcOYxq9k6gID8UJH14cdUCsxExVcG4H/exec";
    private static String id = "1dSo2kv4_V6cleUhvlAR17nRPUO9Xzi1Pm7nSSynSF5E";
    boolean radio = true;
    int card_no = 0;

    int images[] = {
            R.drawable.so,
            R.drawable.qc,
            R.drawable.sotm,
            R.drawable.transe,
            R.drawable.wmi,
            R.drawable.placement,
            R.drawable.redhat,
            R.drawable.t3,
            R.drawable.we,
            R.drawable.youtube,
            R.drawable.gc
    };

    TextInputEditText name_edit, phone_edit, whPhone_edit, course_edit, email_edit;
    Button submit, next;
    RadioGroup rgroup;

    ImageView enqiury;
    TextInputLayout card_name, card_course, card_phone, card_whatsapp, card_email;
    LinearLayout card_check;
    ImageView f1, f2/*, f3*/;

    ProgressDialog d;

    String date, time, name, num1, num2, course, email;

    DB db;

    RadioButton r1, r2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_tab);
        d = new ProgressDialog(this);
        //card id
        card_name = (TextInputLayout) findViewById(R.id.card_name);
        card_course = (TextInputLayout) findViewById(R.id.card_course);
        card_phone = (TextInputLayout) findViewById(R.id.card_phone);
        card_check = (LinearLayout) findViewById(R.id.card_check);
        card_whatsapp = (TextInputLayout) findViewById(R.id.card_whatsapp);
        card_email = (TextInputLayout) findViewById(R.id.card_email);

        //imageview id
        f1 = (ImageView) findViewById(R.id.first);
        f2 = (ImageView) findViewById(R.id.sec);
//        f3 = (ImageView) findViewById(R.id.third);
        enqiury = (ImageView) findViewById(R.id.enquiry_stages);

        //radiobutton id
        r1 = (RadioButton) findViewById(R.id.yes);
        r2 = (RadioButton) findViewById(R.id.no);

        //TextInputEditText id
        name_edit = (TextInputEditText) findViewById(R.id.name);
        phone_edit = (TextInputEditText) findViewById(R.id.phone);
        whPhone_edit = (TextInputEditText) findViewById(R.id.whtsapp_number);
        course_edit = (TextInputEditText) findViewById(R.id.course);
        email_edit = (TextInputEditText) findViewById(R.id.email);

        //RadioGroup id
        rgroup = (RadioGroup) findViewById(R.id.rGroup);

        //Button id
        submit = (Button) findViewById(R.id.click);
        next = (Button) findViewById(R.id.next);

        next.setEnabled(false);

        //ClickListener
        submit.setOnClickListener(this);
        next.setOnClickListener(this);
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
                    }
                }
            }
        });


        f1.setImageResource(images[0]);
        f2.setImageResource(images[1]);

        ImageView learn = (ImageView) findViewById(R.id.learn_image);
        Animation one = AnimationUtils.loadAnimation(this, R.anim.anim_one);
        learn.startAnimation(one);

        Animation two = AnimationUtils.loadAnimation(this, R.anim.anim_two);
        enqiury.startAnimation(two);

        Animation three = AnimationUtils.loadAnimation(this, R.anim.anim_three);
        f1.startAnimation(three);

        Animation four = AnimationUtils.loadAnimation(this, R.anim.anim_four);
        f2.startAnimation(four);

//        Animation five = AnimationUtils.loadAnimation(this, R.anim.anim_five);
//        f3.startAnimation(five);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.enter_zoom1);
        card_name.startAnimation(anim);
        next.startAnimation(anim);
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
            if (num1.length() < 10) {
                phone_edit.setError("Please Enter Valid Number");
                phone_edit.requestFocus();
                return;
            }
            if (num2.length() < 10) {
                whPhone_edit.setError("Please Enter Valid Number");
                whPhone_edit.requestFocus();
                return;
            }

            if (!email.isEmpty()) {
                if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                    email_edit.setError("Please Enter Valid Email Id");
                    email_edit.requestFocus();
                    return;
                }

            }

            Long timing = System.currentTimeMillis();

            date = getDate(timing, "dd/MM/yyyy");
            time = getDate(timing, "hh:mm:ss");

            d.setMessage("Please Wait...");
            d.setCancelable(false);
            d.show();
//9988741983

            insertIntoSheet();
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
//                    next.setBackground(getDrawable(R.drawable.button_design_enable));
                } else {
                    next.setEnabled(false);
//                    next.setBackground(getDrawable(R.drawable.button_design_disable));
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
//                    next.setBackground(getDrawable(R.drawable.button_design_enable));
                } else {
                    next.setEnabled(false);
//                    next.setBackground(getDrawable(R.drawable.button_design_disable));
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
//                        next.setBackground(getDrawable(R.drawable.button_design_enable));
                    } else {
                        next.setEnabled(false);
//                        next.setBackground(getDrawable(R.drawable.button_design_disable));
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
//                    next.setBackground(getDrawable(R.drawable.button_design_enable));
                } else {
                    next.setEnabled(false);
//                    next.setBackground(getDrawable(R.drawable.button_design_disable));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        whPhone_edit.setEnabled(false);
    }

    int delay = 1600;

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

        if (card_no == 2) {
            String number = phone_edit.getText().toString().trim();
            if (number.length() < 10) {
                phone_edit.setError("Please Enter Valid Number");
                phone_edit.requestFocus();
                return;
            }
        }
        if (card_no == 4) {
            String number = whPhone_edit.getText().toString().trim();
            if (number.length() < 10) {
                whPhone_edit.setError("Please Enter Valid Number");
                whPhone_edit.requestFocus();
                return;
            }
        }

        switch (card_no) {
            case 0:
                card_no++;
                f1.setImageResource(images[2]);
                f2.setImageResource(images[3]);
                image_animation_start();
                next.setEnabled(false);
//                next.setBackground(getDrawable(R.drawable.button_design_disable));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        card_course.setVisibility(View.VISIBLE);
                        course_edit.requestFocus();
                        card_course.startAnimation(setZoomAnimation());
                        enqiury.setImageResource(R.drawable.two);
                    }
                }, delay);
                break;
            case 1:
                f1.setImageResource(images[4]);
                f2.setImageResource(images[5]);
//                f3.setImageResource(images[2]);
                card_no++;
                image_animation_start();
                next.setEnabled(false);
//                next.setBackground(getDrawable(R.drawable.button_design_disable));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        card_phone.setVisibility(View.VISIBLE);
                        phone_edit.requestFocus();
                        card_phone.startAnimation(setZoomAnimation());
                        enqiury.setImageResource(R.drawable.three);
                    }
                }, delay);
                break;
            case 2:

                f1.setImageResource(images[6]);
                f2.setImageResource(images[7]);
//                f3.setImageResource(images[5]);
                card_no++;
                image_animation_start();
                next.setEnabled(false);
//                next.setBackground(getDrawable(R.drawable.button_design_disable));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        card_check.setVisibility(View.VISIBLE);
                        card_check.startAnimation(setZoomAnimation());
                        enqiury.setImageResource(R.drawable.four);
                    }
                }, delay);
                break;
            case 3:
                f1.setImageResource(images[10]);
                f2.setImageResource(images[1]);
//                f3.setImageResource(images[8]);
                card_no++;
                image_animation_start();
                next.setEnabled(false);
//                next.setBackground(getDrawable(R.drawable.button_design_disable));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        whPhone_edit.requestFocus();
                        card_whatsapp.setVisibility(View.VISIBLE);
                        card_whatsapp.startAnimation(setZoomAnimation());
                        enqiury.setImageResource(R.drawable.five);
                    }
                }, delay);
                break;
            case 4:
                f1.setImageResource(images[8]);
                f2.setImageResource(images[9]);
//                f3.setImageResource(images[2]);
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
                        enqiury.setImageResource(R.drawable.six);
                    }
                }, delay);
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

        f1.setVisibility(View.VISIBLE);
        f2.setVisibility(View.VISIBLE);
//        f3.setVisibility(View.VISIBLE);

        f1.startAnimation(img_2);
        f2.startAnimation(img_1);
//        f3.startAnimation(img_1);

    }

    private void insertIntoSheet() {
        if (email.isEmpty()) {
            email = "Not Available";
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        d.dismiss();
                        Intent i = new Intent(MainActivity.this, ThankYouActivity.class);
                        i.putExtra("name", name);
                        i.putExtra("num1", num1);
                        i.putExtra("num2", num2);
                        i.putExtra("course", course);
                        i.putExtra("email", email);
                        startActivity(i);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        d.dismiss();
                        Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                m.put("email", email);
                return m;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(request);
    }

}
