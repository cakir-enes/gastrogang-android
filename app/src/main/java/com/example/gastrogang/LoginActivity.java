package com.example.gastrogang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {


    private Toolbar actionbarLogin;
    private EditText txtUsername, txtPassword;
    private Button btnLogin;

    public void init() {
        actionbarLogin = findViewById(R.id.actionbarLogin);
        setSupportActionBar(actionbarLogin);
        getSupportActionBar().setTitle("LOGIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtUsername = (EditText) findViewById(R.id.txtUsernameLogin);
        txtPassword = (EditText) findViewById(R.id.txtPasswordLogin);
        btnLogin = (Button) findViewById(R.id.buttonLogin);

    }


    private void loginUser() {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Enter a username!", Toast.LENGTH_LONG).show();

        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter a password!", Toast.LENGTH_LONG).show();
        }
        else {
            String url = "https://gastrogang.herokuapp.com/api/v1/login";
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                obj.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(LoginActivity.this, "Successfully logged in!", Toast.LENGTH_LONG).show();

                            String token = "";
                            try {
                                token = response.getString("token");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Intent viewIntent = new Intent(LoginActivity.this, ViewActivity.class);
                            viewIntent.putExtra("token", token);
                            startActivity(viewIntent);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse.statusCode == 400) {
                                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "An error occurred while logging in!", Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                            }
                        }
                    });
            queue.add(jsObjRequest);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }
}
