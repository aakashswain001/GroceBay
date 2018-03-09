package com.grocebay.grocebay.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.grocebay.grocebay.Adapters.ProductsAdapter;
import com.grocebay.grocebay.CheckoutActivity;
import com.grocebay.grocebay.R;
import com.grocebay.grocebay.app.AppController;
import com.grocebay.grocebay.model.Category;
import com.grocebay.grocebay.model.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {

    View mView;
    ArrayList<Products> productsList;
    RecyclerView recyclerView;
    ProductsAdapter mAdapter;
    ProgressBar pBar;
    Button checkout;
    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_products, container, false);
        pBar = (ProgressBar) mView.findViewById(R.id.pbar);
        productsList = new ArrayList<>();
        recyclerView = (RecyclerView) mView.findViewById(R.id.products);

        mAdapter = new ProductsAdapter(getContext(), productsList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareProductData();
        checkout = (Button)mView.findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(getActivity(), CheckoutActivity.class);
                    startActivity(i);
            }
        });

        return mView;
    }

    public void prepareProductData(){
        recyclerView.setVisibility(View.GONE);
        pBar.setVisibility(View.VISIBLE);
        productsList.clear();
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";

        String url = "https://shopptest.000webhostapp.com/api/product/read.php";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("records");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        Products c = new Products();
                        c.setId(obj.getString("id"));
                        c.setName(obj.getString("name"));
                        c.setDescription(obj.getString("description"));
                        c.setPrice(obj.getString("price"));
                        c.setCategory_id(obj.getString("category_id"));
                        c.setImage_url(obj.getString("image_url"));
                        productsList.add(c);
                    }
                    mAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    pBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_arry);
    }

}
