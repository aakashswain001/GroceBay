package com.grocebay.grocebay;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.grocebay.grocebay.model.Product;
import com.grocebay.grocebay.utils.MySingleton;
import com.grocebay.grocebay.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.grocebay.grocebay.utils.URLs.GET_PRODUCTS;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    View mView;
    LinearLayout category1, category2, category3, category4;
    FloatingActionButton fab;
    int cart_count = 0;
    CounterFab mCounterFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        category1 = mView.findViewById(R.id.category1);
        category2 = mView.findViewById(R.id.category2);
        category3 = mView.findViewById(R.id.category3);
        category4 = mView.findViewById(R.id.category4);

        category1.setOnClickListener(this);
        category2.setOnClickListener(this);
        category3.setOnClickListener(this);
        category4.setOnClickListener(this);


        mCounterFab = (CounterFab) mView.findViewById(R.id.counter_fab);
        int cc = SharedPrefManager.getInstance(getContext()).getCheckoutCount();
        if (cc > 0) {
            mCounterFab.setCount(cc);
        }
        mCounterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentCheckout();
            }
        });
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.category1:
                i = new Intent(getContext(), ProductActivity.class);
                i.putExtra("category_id", "1");
                startActivity(i);
                break;
            case R.id.category2:
                i = new Intent(getContext(), ProductActivity.class);
                i.putExtra("category_id", "2");
                startActivity(i);
                break;
            case R.id.category3:
                i = new Intent(getContext(), ProductActivity.class);
                i.putExtra("category_id", "3");
                startActivity(i);
                break;
            case R.id.category4:
                i = new Intent(getContext(), ProductActivity.class);
                i.putExtra("category_id", "4");
                startActivity(i);
                break;
        }
    }

    private void intentCheckout() {
        ArrayList<Product> checkoutList;
        int count = SharedPrefManager.getInstance(getContext()).getCheckoutCount();
        if (count == 0) {
            checkoutList = new ArrayList<>();
        } else {
            checkoutList = SharedPrefManager.getInstance(getContext()).getArrayList();
        }
        if (checkoutList.isEmpty()) {
            Snackbar.make(mView.findViewById(R.id.parentlayout), "No item in cart !!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        Intent intent = new Intent(getContext(), CheckoutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        int cc = SharedPrefManager.getInstance(getContext()).getCheckoutCount();
        mCounterFab.setCount(cc);
    }
}
