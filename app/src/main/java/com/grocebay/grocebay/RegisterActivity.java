package com.grocebay.grocebay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.grocebay.grocebay.model.User;
import com.grocebay.grocebay.utils.MySingleton;
import com.grocebay.grocebay.utils.SharedPrefManager;
import com.grocebay.grocebay.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText EtName, EtPhone, EtEmail, EtPassword, EtRepassword;
    Button BtnRegister;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EtName = (EditText) findViewById(R.id.et_reg_name);
        EtPhone = (EditText) findViewById(R.id.et_reg_phone);
        EtEmail = (EditText) findViewById(R.id.et_reg_email);
        EtPassword = (EditText) findViewById(R.id.et_reg_pass);
        EtRepassword = (EditText) findViewById(R.id.et_reg_pass2);
        BtnRegister = (Button) findViewById(R.id.btn_reg);
        TextView signIn = (TextView) findViewById(R.id.tv_link_login);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on login
                //we will open the login screen
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private void registerUser() {

        final String name = EtName.getText().toString().trim();
        final String email = EtEmail.getText().toString().trim();
        final String password = EtPassword.getText().toString().trim();
        final String phone = EtPhone.getText().toString().trim();
        final String password2 = EtRepassword.getText().toString().trim();

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
        if (TextUtils.isEmpty(email)) {
            EtEmail.setError("Please enter your email");
            EtEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EtEmail.setError("Enter a valid email");
            EtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            EtPassword.setError("Enter a password");
            EtRepassword.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(password2)) {
            EtRepassword.setError("Please re-enter password");
            EtRepassword.requestFocus();
            return;
        }
        if (!password2.equals(password)) {
            EtRepassword.setError("Please enter the same password");
            EtRepassword.requestFocus();
            return;
        }

        //displaying the progress bar while user registers on the server
        progressBar = (ProgressBar) findViewById(R.id.pbar_reg);
        progressBar.setVisibility(View.VISIBLE);

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        progressBar.setVisibility(View.GONE);
                        try {
                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            //if no error in response
                            if (!obj.getBoolean("error")) {

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object
                                User user = new User(
                                        userJson.getString("id"),
                                        userJson.getString("name"),
                                        userJson.getString("email"),
                                        userJson.getString("phone")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                params.put("name", name);
                params.put("password", password);
                params.put("email", email);
                params.put("phone", phone);
                return params;
            }
        };

        //creating a request queue
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


}

