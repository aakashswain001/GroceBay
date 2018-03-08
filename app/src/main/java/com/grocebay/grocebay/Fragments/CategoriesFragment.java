package com.grocebay.grocebay.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.grocebay.grocebay.Adapters.CategoryAdapter;
import com.grocebay.grocebay.MainActivity;
import com.grocebay.grocebay.R;
import com.grocebay.grocebay.RegisterActivity;
import com.grocebay.grocebay.app.AppController;
import com.grocebay.grocebay.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    View view;
    ArrayList<Category> categoryList;
    RecyclerView recyclerView;
    CategoryAdapter mAdapter;
    ProgressBar pBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_categories, container, false);
        pBar = (ProgressBar) view.findViewById(R.id.pbar);
        categoryList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.categories);

        mAdapter = new CategoryAdapter(categoryList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareRestaurantData();

        return view;
    }


    void prepareRestaurantData() {

        recyclerView.setVisibility(View.GONE);
        pBar.setVisibility(View.VISIBLE);
        categoryList.clear();
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";

        String url = "https://shopptest.000webhostapp.com/api/category/read.php";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("records");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        Category c = new Category();
                        c.setId(obj.getString("id"));
                        c.setName(obj.getString("name"));
                        c.setDescription(obj.getString("description"));
                        categoryList.add(c);
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
