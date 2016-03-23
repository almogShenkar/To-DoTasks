package com.almog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by almog on 3/11/16.
 */
public class Login extends AppCompatActivity {

    private TextView emailT;
    private TextView passwordT;
    private View mProgressView;
    private View mLoginFormView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPars();
        setContentView(R.layout.activity_login);
        View fab=(View)findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ParseUser user=ParseUser.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.loginForm);
        emailT = (TextView) findViewById(R.id.textEmaillog);
        passwordT = (TextView) findViewById(R.id.textPasslog);
    }

    //Activity click listeners
    public void onClickSign(View v) {
        startActivity(new Intent(this, SignUp.class));
        finish();
    }

    public void onClickLogin(View v){
        final Context context=this;
        String email = emailT.getText().toString();
        String password = passwordT.getText().toString();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            showProgress(true);
            ParseUser.logInInBackground(email, password, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    ContextThemeWrapper ctw = new ContextThemeWrapper(Login.this,null);
                    if (user != null)
                    {
                    new CustomDialog(ctw, "Logged-in successfully", "Welcome back " + user.get("name"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                            }
                        }
                    ).showDialog();

                    }
                    else
                    {
                    // login failed. Look at the ParseException to see what happened.
                        new CustomDialog(ctw,"Login failed","check your details",null).showDialog();
                        passwordT.setText("");
                        showProgress(false);
                    }
                }
            });
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void initPars() {
        try{
            Parse.enableLocalDatastore(this);
            Parse.initialize(this);
            //ParseUser.logOut();
        }
        catch (Exception e){
            //parse already initialized
        }

    }

}


