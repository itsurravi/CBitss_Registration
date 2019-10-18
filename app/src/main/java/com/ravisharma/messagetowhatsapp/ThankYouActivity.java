package com.ravisharma.messagetowhatsapp;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThankYouActivity extends AppCompatActivity {

    LinearLayout paytm, note;
    TextView ofc, txt_name, scratch;
    LayoutInflater li;
    View layout;
    AlertDialog a;
    EditText pin;
    Button pin_btn;

    String course, name, num1, num2, email, uri_upi;

    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        paytm = (LinearLayout) findViewById(R.id.paytm_layout);
        note = (LinearLayout) findViewById(R.id.note);

        ofc = (TextView) findViewById(R.id.ofeuse);
        txt_name = (TextView) findViewById(R.id.name);
        scratch = (TextView) findViewById(R.id.scratch);

        db = new DB(this);
        Bundle bundle = getIntent().getExtras();

        name = bundle.getString("name");
        num1 = bundle.getString("num1");
        num2 = bundle.getString("num2");
        course = bundle.getString("course");
        email = bundle.getString("email");
        uri_upi = bundle.getString("uri_upi");

        txt_name.setText(name);

        String message = "Dear " + name.toUpperCase()
                + ",%0a%0aThank you for showing your interest in our Professional " + course.toUpperCase()
                + " Course.%0a%0aWe Assure You to Deliver Quality Training.%0a%0aThank you.%0a%0a" +
                "For Queries,%0aPls Call on%0a9988741983";

        String url = "http://203.129.225.69/API/WebSMS/Http/v1.0a/index.php?username=cbitss&password=123456&sender=CBitss&to=91" + num1 + "&message=" + message + "&reqid=1&format={json|text}&route_id=7";

        StringRequest sr = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(ThankYouActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                sendMail(name, course, email);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThankYouActivity.this, "Message Not Sent Due to Some Server Issues", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(sr);


        Animation anim = AnimationUtils.loadAnimation(this, R.anim.scratch_anim);
        scratch.startAnimation(anim);

        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.paytm);
        paytm.startAnimation(anim2);

        Animation anim3 = AnimationUtils.loadAnimation(this, R.anim.scratch_note);
        note.startAnimation(anim3);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ofc.setVisibility(View.VISIBLE);
            }
        }, 10000);

        ofc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ThankYouActivity.this);

                builder.setTitle("Enter Pin for Office Use Only:-");
                li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                layout = li.inflate(R.layout.alert, null);

                builder.setView(layout);

                pin = (EditText) layout.findViewById(R.id.pin);
                pin_btn = (Button) layout.findViewById(R.id.button);
                pin_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pincheck = pin.getText().toString().trim();
                        try {
                            Cursor c = db.getPass(pincheck);
//                            Toast.makeText(ThankYouActivity.this, ""+c.getCount(), Toast.LENGTH_SHORT).show();
                            if (c.getCount() > 0) {
                                if (name.isEmpty() || num1.isEmpty() || course.isEmpty()) {

                                }
                                Intent i = new Intent(ThankYouActivity.this, Main2Activity.class);
                                i.putExtra("pin", pincheck);
                                i.putExtra("name", name);
                                i.putExtra("num1", num1);
                                i.putExtra("num2", num2);
                                i.putExtra("course", course);
                                i.putExtra("uri_upi", uri_upi);
                                startActivity(i);
                                finish();

                            }
                        } catch (Exception e) {
                            Toast.makeText(ThankYouActivity.this, "Enter Valid Pin\n", Toast.LENGTH_SHORT).show();
                            Log.d("ERROR", e.getMessage());
                        }
                        if (a.isShowing()) {
                            a.dismiss();
                        }
                    }
                });


                a = builder.create();
                a.setCancelable(false);
                a.show();
            }
        });
    }

    private void sendMail(final String name, final String course, final String email) {
        String api = "http://fossfoundation.com/InquiryApplication/mailsend.php";

        StringRequest sr = new StringRequest(Request.Method.POST, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ThankYouActivity.this, "Mail Not Sent", Toast.LENGTH_SHORT).show();
                Log.d("Eroro", String.valueOf(error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> m = new HashMap<>();
                m.put("email", email);
                m.put("name", name);
                m.put("course", course);
                return m;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(sr);
    }
}