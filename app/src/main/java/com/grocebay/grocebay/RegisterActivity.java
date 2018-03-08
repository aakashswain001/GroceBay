package com.grocebay.grocebay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.grocebay.grocebay.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText name, phone, email, password, repassword;
    Button register;
    String fullname, ph, em, pass, pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.reg_fullname);
        phone = (EditText) findViewById(R.id.reg_phone);
        email = (EditText) findViewById(R.id.reg_email);
        password = (EditText) findViewById(R.id.reg_password);
        repassword = (EditText) findViewById(R.id.password2);
        register = (Button) findViewById(R.id.btnRegister);
        TextView signIn = (TextView) findViewById(R.id.link_to_login);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(i);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname = name.getText().toString();
                ph = phone.getText().toString();
                em = email.getText().toString();
                pass = password.getText().toString();
                pass2 = repassword.getText().toString();
                registerApi();
            }
        });

    }

    void registerApi() {
        HashMap<String, String> obj = new HashMap<String, String>();
        obj.put("name", fullname);
        obj.put("email", em);
        obj.put("phone", ph);
        obj.put("encrypted_password", pass);
        obj.put("location_id", "1");
        JSONObject object = new JSONObject(obj);
        String urlJsonObj = "http://shopptest.000webhostapp.com/api/user/register.php";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("message");
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}

