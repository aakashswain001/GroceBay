package com.grocebay.grocebay;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    View mView;
    LinearLayout category1, category2, category3, category4;

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
}
