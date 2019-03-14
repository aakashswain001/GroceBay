package com.grocebay.grocebay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grocebay.grocebay.model.User;
import com.grocebay.grocebay.utils.SharedPrefManager;


public class ProfileFragment extends Fragment {

    View mView;
    TextView textViewId, textViewName, textViewEmail, textViewPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);


        textViewId = mView.findViewById(R.id.textViewId);
        textViewName = mView.findViewById(R.id.textViewUsername);
        textViewEmail = mView.findViewById(R.id.textViewEmail);
        textViewPhone = mView.findViewById(R.id.textViewPhone);


        //getting the current user
        User user = SharedPrefManager.getInstance(getContext()).getUser();

        //setting the values to the textviews
        textViewId.setText(String.valueOf(user.getId()));
        textViewName.setText(user.getName());
        textViewEmail.setText(user.getEmail());
        textViewPhone.setText(user.getPhone());

        //when the user presses logout button
        //calling the logout method
        mView.findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                SharedPrefManager.getInstance(getContext()).logout();
            }
        });
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Profile");
    }
}
