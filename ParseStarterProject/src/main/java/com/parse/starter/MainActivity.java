/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

  EditText passwordEditText;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    MenuInflater menuInflater = new MenuInflater(this);
    menuInflater.inflate(R.menu.main_menu,menu);

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if(item.getItemId()==R.id.signUp){

    }
    else if(item.getItemId()==R.id.help){

    }
    else if(item.getItemId()==R.id.about){

    }

    return super.onOptionsItemSelected(item);
  }

  public void redirectUser(){
    if(ParseUser.getCurrentUser()!=null){
      Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
      startActivity(intent);
    }
  }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==keyEvent.ACTION_DOWN){
      signupLogin(view);
    }

    return false;
  }

  @Override
  public void onClick(View view) {
    if(view.getId()==R.id.backgroundRelativeLayout||view.getId()==R.id.imageView){
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
  }


  public void signupLogin(View view){
    final EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);

    ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if( e == null){
          Log.i("Info","Logged In");
          Toast.makeText(MainActivity.this, "Logging in as "+ usernameEditText.getText().toString(), Toast.LENGTH_SHORT).show();
          redirectUser();
        }else{
          ParseUser parseUser = new ParseUser();
          parseUser.setUsername(usernameEditText.getText().toString());
          parseUser.setPassword(passwordEditText.getText().toString());

          parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
              if( e == null ){
                Log.i("Info","Signed Up");
                Toast.makeText(MainActivity.this, "Sign-up successful", Toast.LENGTH_SHORT).show();
                redirectUser();
              }else{
                Toast.makeText(MainActivity.this,e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_SHORT).show();
              }
            }
          });
        }
      }
    });

  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("Twitter");
    RelativeLayout backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);
    ImageView logoImageView = (ImageView) findViewById(R.id.imageView);
    backgroundRelativeLayout.setOnClickListener(this);
    logoImageView.setOnClickListener(this);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    passwordEditText.setOnKeyListener(this);
    redirectUser();
    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}