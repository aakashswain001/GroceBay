package com.grocebay.grocebay;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.grocebay.grocebay.utils.Order;
import com.grocebay.grocebay.utils.SharedPrefManager;
import com.grocebay.grocebay.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {


    List<Order> OrderHistoryList;
    RecyclerView recyclerView;


    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //getting the recycler view from xml
        recyclerView = getView().findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initializing the productlist
        OrderHistoryList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview
        showOrder();

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Orders");
    }

    private void showOrder() {

        String id = SharedPrefManager.getInstance(getContext()).getUser().getId();
        String url = URLs.GET_ORDERS.concat(id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            //converting the string to json array object
                            JSONArray array = object.getJSONArray("orders");

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject order = array.getJSONObject(i);

                                //adding the product to product list
                                OrderHistoryList.add(new Order(
                                        order.getInt("id"),
                                        order.getString("order_details"),
                                        order.getString("order_date"),
                                        order.getInt("price"),
                                        order.getString("address"),
                                        order.getString("order_status")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            Order_historyAdapter adapter = new Order_historyAdapter(getActivity(), OrderHistoryList);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

}
