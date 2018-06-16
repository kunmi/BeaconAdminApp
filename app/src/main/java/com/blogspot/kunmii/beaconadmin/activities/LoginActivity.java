package com.blogspot.kunmii.beaconadmin.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.blogspot.kunmii.beaconadmin.Config;
import com.blogspot.kunmii.beaconadmin.Helpers.Helpers;
import com.blogspot.kunmii.beaconadmin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity{

    AppCompatButton loginButton;
    EditText usernameView;
    EditText passwordView;

    LinearLayout loginLayout;
    LinearLayout loadingLayout;
    ProgressBar progressBar;

    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         client = new OkHttpClient();


        loginButton =  findViewById(R.id.btn_login);
        usernameView =  findViewById(R.id.input_username);
        passwordView =  findViewById(R.id.input_password);

        loginLayout = findViewById(R.id.login_layout);
        loadingLayout =  findViewById(R.id.progress_layout);
        progressBar = findViewById(R.id.progressBar1);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usernameView.getText().toString().length()<1)
                {
                    Helpers.showDialog(LoginActivity.this, "Invalid Username", "Please enter a valid username", "Okay",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    return;
                }

                if(passwordView.getText().toString().length()<1)
                {
                    Helpers.showDialog(LoginActivity.this, "Invalid Password", "Please enter a valid password", "Okay",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    return;
                }


                loginLayout.setVisibility(GONE);
                loadingLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);

                attemptLogin(usernameView.getText().toString(), passwordView.getText().toString());

            }
        });

    }

    void attemptLogin(final String username, final String password){


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("username", username);
                    jsonBody.put("password", password);

                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody.toString());

                    Request request = new Request.Builder()
                            .url(Config.SERVER_URL + Config.LOGIN_URL)
                            .post(requestBody)
                            .build();


                    Response response = client.newCall(request).execute();
                    String val = response.body().string();

                    final JSONObject jsonResult = new JSONObject(val);
                    if(jsonResult.getBoolean("success"))
                    {
                        Helpers.storeUserToken(jsonResult.getString("token"), LoginActivity.this.getApplication());


                        if(Helpers.StoreUserData(jsonResult.getJSONObject("user"), LoginActivity.this.getApplication()))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            });
                        }
                        else
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Helpers.showDialog(LoginActivity.this, "An Error Occured",
                                            "Error Occured Storing Your Information, Please contact support",
                                            "Okay", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }
                                    );
                                }
                            });
                        }



                    }
                    else
                    {
                        final String message = jsonResult.getString("msg");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Helpers.showDialog(LoginActivity.this, "Invalid Credentials",
                                        message
                                        , "Okay",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                loadingLayout.setVisibility(GONE);
                                                loginLayout.setVisibility(View.VISIBLE);
                                            }
                                        });
                            }
                        });
                    }


                    // Do something with the response.
                } catch (final IOException e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Helpers.showDialog(LoginActivity.this, "Error Occured", "Error occurred processing your request, please try again later.\n "
                                            +e.getMessage(), "Okay",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            loadingLayout.setVisibility(GONE);
                                            loginLayout.setVisibility(View.VISIBLE);
                                        }
                                    });
                        }
                    });
                } catch (final JSONException e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Helpers.showDialog(LoginActivity.this, "Error Occured", "Error occurred processing your request, please try again later.\n "
                                            +e.getMessage(), "Okay",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            loadingLayout.setVisibility(GONE);
                                            loginLayout.setVisibility(View.VISIBLE);
                                        }
                                    });

                        }
                    });

                }

            }
        }).start();

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setTitle("Exiting");
        exitDialog.setMessage("Are you sure you want to Exit the app?");
        exitDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginActivity.this.finishAffinity();
            }
        });

        exitDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        exitDialog.show();
    }
}
