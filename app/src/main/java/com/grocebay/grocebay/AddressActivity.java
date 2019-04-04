package com.grocebay.grocebay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.grocebay.grocebay.model.Product;
import com.grocebay.grocebay.utils.MySingleton;
import com.grocebay.grocebay.utils.SharedPrefManager;
import com.grocebay.grocebay.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {
    EditText EtName, EtPhone, EtLane1, EtLane2, EtLandmark, EtPin;
    Button BtnPlaceOrder;
    String orderDetails, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);


        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //get intent values
        orderDetails = getIntent().getExtras().getString("order_details");
        price = getIntent().getExtras().getString("price");

        // initialize et and btn
        EtName = (EditText) findViewById(R.id.et_name);
        EtName.setText(SharedPrefManager.getInstance(getApplicationContext()).getUser().getName());
        EtPhone = (EditText) findViewById(R.id.et_phone);
        EtPhone.setText(SharedPrefManager.getInstance(getApplicationContext()).getUser().getPhone());
        EtLane1 = (EditText) findViewById(R.id.et_line1);
        EtLane2 = (EditText) findViewById(R.id.et_line2);
        EtLandmark = (EditText) findViewById(R.id.et_landmark);
        EtPin = (EditText) findViewById(R.id.et_pin);
        BtnPlaceOrder = (Button) findViewById(R.id.placeorder);
        BtnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateOrder();
            }
        });

    }

    private void validateOrder() {
        final String name = EtName.getText().toString().trim();
        final String phone = EtPhone.getText().toString().trim();
        final String line1 = EtLane1.getText().toString().trim();
        final String line2 = EtLane2.getText().toString().trim();
        final String landmark = EtLandmark.getText().toString().trim();
        final String pincode = EtPin.getText().toString().trim();

        //first we will do the validations

        if (TextUtils.isEmpty(name)) {
            EtName.setError("Please enter name");
            EtName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            EtPhone.setError("Enter phone no");
            EtPhone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(line1)) {
            EtLane1.setError("Please enter Line1");
            EtLane1.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(landmark)) {
            EtLandmark.setError("Enter the nearest landmark");
            EtLandmark.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(pincode)) {
            EtPin.setError("Please enter pincode");
            EtPin.requestFocus();
            return;
        }
        String address = line1.concat(" ").concat(line2).concat(" landmark ").concat(landmark).concat(" pin ").concat(pincode);
        placeOrder(address);
    }

    //for putting into order database
    private void placeOrder(final String address) {
        final ProgressBar progressBar = findViewById(R.id.pBar);
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ADD_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                //   finish();
                                SharedPrefManager.getInstance(getApplicationContext()).setCheckoutCount(0);
                                SharedPrefManager.getInstance(getApplicationContext()).saveArrayList(new ArrayList<Product>());
                                Intent newIntent = new Intent(AddressActivity.this, MainActivity.class);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(newIntent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                params.put("order_details", orderDetails);
                params.put("price", price);
                params.put("address", address);
                return params;
            }
        };

        //creating a request queue
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
