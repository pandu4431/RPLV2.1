package com.androidbelieve.drawerwithswipetabs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidbelieve.drawerwithswipetabs.navigationbar.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pandu on 25/03/17.
 */

public class Login extends AppCompatActivity {
    public static final String LOGIN_URL = "http://pandumalik.esy.es/UserRegistration/login.php";
    public static final String KEY_EMAIL = "email";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    public static final String LOGIN_SUCCESS = "success";
    public static String KEY_PASSWORD = "password";
    public static String SHARED_PREF_NAME;
    public static String EMAIL_SHARED_PREF;
    public static String ID_USER;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView signUp;
    private Button BtnLogin;
    private Button BtnClear;
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        BtnLogin = (Button) findViewById(R.id.btn_login);
        BtnClear = (Button) findViewById(R.id.btn_clear);
        signUp = (TextView) findViewById(R.id.signupaccess);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Login.this, Signup.class);
                startActivity(myIntent);
            }
        });

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        BtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextEmail.setText("");
                editTextPassword.setText("");
            }
        });
    }

    private void login() {
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        final BackgroundTask bt = new BackgroundTask();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equalsIgnoreCase(LOGIN_SUCCESS)) {
                            SharedPreferences sharedPreferences = Login.this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(LOGGEDIN_SHARED_PREF, true);
                            editor.putString(EMAIL_SHARED_PREF, email);
                            bt.execute();
                            editor.commit();
                            //userPrev.setText(editTextEmail.toString());
                            //Intent intent = new Intent(Login.this, MainActivity.class);
                            //startActivity(intent);
                        } else {
                            Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> prams = new HashMap<>();
                prams.put(KEY_EMAIL, email);
                prams.put(KEY_PASSWORD, password);

                return prams;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(LOGGEDIN_SHARED_PREF, false);
        if (loggedIn) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Apakah Anda Yakin Ingin Keluar");
        builder.setCancelable(true);
        builder.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                System.exit(1);
            }
        });
        builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    class BackgroundTask extends AsyncTask<Void, UserData, Void> {
        String username = editTextEmail.getText().toString();
        String URLdata = "http://pandumalik.esy.es/UserRegistration/getUser.php?username="+username;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(URLdata);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                httpURLConnection.disconnect();
                String json_string = stringBuilder.toString().trim();

                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.optJSONArray("server_response");
                int count = 0;
                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    count++;
                    UserData UD = new UserData(JO.getString("nim"), JO.getString("name"), JO.getString("email"), JO.getString("password"));
                    publishProgress(UD);
                    ID_USER = JO.getString("nim");
                    EMAIL_SHARED_PREF = JO.getString("email");
                    SHARED_PREF_NAME = JO.getString("name");
                    KEY_PASSWORD = JO.getString("password");
                }
                finish();
                Log.d("JSON STRING", json_string);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
