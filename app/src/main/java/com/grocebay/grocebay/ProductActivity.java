package com.grocebay.grocebay;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.load.model.ByteArrayLoader;
import com.grocebay.grocebay.interfaces.AddorRemoveCallbacks;
import com.grocebay.grocebay.model.Product;
import com.grocebay.grocebay.utils.Converter;
import com.grocebay.grocebay.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.grocebay.grocebay.utils.URLs.*;

public class ProductActivity extends AppCompatActivity implements AddorRemoveCallbacks {

    public ProductAdapter productAdapter;
    List<Product> productList;
    RecyclerView recyclerView;
    String category_id;
    ProgressBar pBar;
    TextView textView;
    FloatingActionButton fab;
    int cart_count = 0;
    CounterFab mCounterFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //fab = (FloatingActionButton) findViewById(R.id.fab);

        category_id = getIntent().getStringExtra("category_id");

        recyclerView = findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(ProductActivity.this, productList);
        recyclerView.setAdapter(productAdapter);
        pBar = findViewById(R.id.pBar);
        textView = findViewById(R.id.tvmenu);
        mCounterFab = (CounterFab) findViewById(R.id.counter_fab);
        mCounterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentCheckout();
            }
        });
        loadfood();
    }

    private void intentCheckout() {
        ArrayList<Product> checkoutList = new ArrayList<>();
        for (Product m : productList) {
            if (m.getCount() > 0) {
                checkoutList.add(m);
            }
        }
        if (checkoutList.isEmpty()) {
            Snackbar.make(findViewById(R.id.parentlayout), "No item in cart !!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        Intent intent = new Intent(ProductActivity.this, CheckoutActivity.class);
        intent.putExtra("productList", checkoutList);
        intent.putExtra("type", category_id);
        startActivity(intent);
    }

    public void loadfood() {
        setVisibility(false);
        textView.setVisibility(View.GONE);
        //String url = GET_PRODUCTS.concat("&category_id=" + category_id);
        String url = "http://sleepygamers.xyz/grocebay/api/products.php?apicall=get&category_id=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setVisibility(true);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("products");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject product = array.getJSONObject(i);
                        productList.add(new Product(
                                product.getInt("id"),
                                product.getString("name"),
                                product.getString("description"),
                                product.getString("category_id"),
                                product.getString("price"),
                                product.getString("vegtype"),
                                product.getString("image"))
                        );
                    }
                    productAdapter.notifyDataSetChanged();
                    if (productList.isEmpty()) {
                        textView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setVisibility(true);
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


    }

    private void setVisibility(boolean state) {
        if (state) {
            pBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            pBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(ProductActivity.this, cart_count, R.drawable.ic_shopping_cart_white_24dp));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.cart_action) {
            intentCheckout();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddProduct(int n) {
        cart_count = 0;
        for (Product product :
                productList) {
            cart_count += product.getCount();
        }
        mCounterFab.setCount(cart_count);
        invalidateOptionsMenu();
        Snackbar.make(findViewById(R.id.parentlayout), "Added to cart !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();


    }

    @Override
    public void onRemoveProduct(int n) {
        cart_count = 0;
        for (Product product :
                productList) {
            cart_count += product.getCount();
        }
        mCounterFab.setCount(cart_count);
        invalidateOptionsMenu();
        Snackbar.make(findViewById(R.id.parentlayout), "Removed from cart !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
}
