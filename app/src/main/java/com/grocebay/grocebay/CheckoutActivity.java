package com.grocebay.grocebay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
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

public class CheckoutActivity extends AppCompatActivity {

    //  String type;
    RecyclerView recyclerView;
    CheckoutAdapter checkoutAdapter;
    String date, orderString = "";
    int tot_price = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ArrayList<Product> productList;
        //    productList = (ArrayList<Product>) getIntent().getSerializableExtra("productList");
        //    type = getIntent().getStringExtra("type");
        productList = SharedPrefManager.getInstance(this).getArrayList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cart (" + productList.size() + ")");


        recyclerView = findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        checkoutAdapter = new CheckoutAdapter(CheckoutActivity.this, productList);
        recyclerView.setAdapter(checkoutAdapter);

        for (Product m : productList) {
            tot_price += m.getCount() * Integer.parseInt(m.getPrice());
        }


        //getting the menu list as comma separated string
        for (int i = 0; i < productList.size(); i++) {
            String singleOrderString = productList.get(i).getName() + " " + productList.get(i).getPrice() + " " + Integer.toString(productList.get(i).getCount());
            if (i != productList.size() - 1)
                orderString += singleOrderString + ",";
            else
                orderString += singleOrderString;
        }

        TextView totPrice = findViewById(R.id.tv_totprice);
        totPrice.setText(Integer.toString(tot_price));
        Button checkout = findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //          placeOrder();
                Intent intent = new Intent(CheckoutActivity.this, AddressActivity.class);
                intent.putExtra("order_details", orderString);
                intent.putExtra("price", Integer.toString(tot_price));
                startActivity(intent);
            }
        });
    }

    //for putting into order database
    private void placeOrder() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ADD_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                //   finish();
                                Intent newIntent = new Intent(CheckoutActivity.this, MainActivity.class);
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
                params.put("order_details", orderString);
                params.put("price", Integer.toString(tot_price));
                params.put("address", "xyz");
                return params;
            }
        };

        //creating a request queue
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
