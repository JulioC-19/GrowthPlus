package com.GrowthPlus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.GrowthPlus.dataAccessLayer.Language.LanguageSchema;
import com.GrowthPlus.dataAccessLayer.Language.LanguageSchemaService;
import com.GrowthPlus.dataAccessLayer.parent.ParentSchema;
import com.GrowthPlus.dataAccessLayer.parent.ParentSchemaService;

import io.realm.Realm;

public class ParentLogin extends AppCompatActivity implements View.OnClickListener{

    Realm realm;
    Resources resources;
    private Button loginButton;
    private Button loginBackButton;
    private EditText loginPinInput;
    private TextView forgotPin;
    private ParentSchemaService loginParentService;
    private ParentSchema loginParent;
    private Integer parentSignupPIN;
    Integer loginPinInputInteger;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        init();

        loginButton.setOnClickListener(this);
        loginBackButton.setOnClickListener(this);
        forgotPin.setOnClickListener(this);
    }

    private void init(){
        realm = Realm.getDefaultInstance();
        resources = getResources();
        loginButton = findViewById(R.id.parentLoginBtn);
        loginBackButton = findViewById(R.id.backParentLogin);

        loginPinInput = findViewById(R.id.loginPinInput);
        loginPinInput.setOnFocusChangeListener(this::hideKeyboard);

        forgotPin = findViewById(R.id.forgotPinText);
        loginParentService = new ParentSchemaService(realm);
        loginParent = loginParentService.getAllParentSchemas().get(0); //gets the parent
        assert loginParent != null;
        parentSignupPIN = loginParent.getPIN(); //and their PIN
    }

    @Override
    protected void onResume() {
        super.onResume();

        // create instance of shared preferences
        SharedPreferences langPrefs = getSharedPreferences("LangPreferences", MODE_PRIVATE);
        // create language schema service and set strings
        LanguageSchemaService langSchemaService = new LanguageSchemaService(realm, langPrefs.getString("languageId", "frenchZero"));
        LanguageSchema lang = langSchemaService.getLanguageSchemaById();

        loginPinInput.setHint(lang.getPin());
        forgotPin.setText(lang.getForgotPin());
    }

    @Override
    public void onClick(View view) {
        view.startAnimation(buttonClick);
        int id = view.getId();

        //the user clicked the login button
        if(id == R.id.parentLoginBtn){

            boolean inputValid = validInput(loginPinInput);//checks for null and blank input

            if(inputValid){
                loginPinInputInteger = Integer.parseInt(loginPinInput.getText().toString());

                //if the PIN's match and the input was valid, start the parent portal activity
                if(confirmPinMatch(loginPinInputInteger, parentSignupPIN)){
                    //change the input box back to grey (in case it has been earlier changed to red)
                    loginPinInput.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(204, 204, 204)));
                    startParentPortalActivity();
                }

                else{ //PIN's don't match -> change input box to red to indicate the PIN is incorrect
                    loginPinInput.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(221, 97, 87)));
                    loginPinInput.setText("");
                }
            }

            else {//input was not valid -> change input box to red to indicate the PIN is incorrect
                loginPinInput.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(221, 97, 87)));
                loginPinInput.setText("");
            }
        }

        //the user clicked the back button
        if(id == R.id.backParentLogin){
            //if a parent already exists, then clicking the back arrow should send you back to the landing page
            if(loginParent != null){
                startLandingPageActivity();
            }

            //if a parent doesn't exist, then go to the signup page.....but this never is the case, right?
            else {
                startParentSignupActivity();
            }
        }

        if(id == R.id.forgotPinText){
            startForgotPasswordActivity();
        }
        loginPinInput.setText("");//clears the EditText
    }

    private boolean validInput(EditText input) {
        String inputString = input.getText().toString();
        return inputString.length() == 4;
    }
    private boolean confirmPinMatch(Integer pin1, Integer pin2){
        return pin1.equals(pin2);
    }

    public void startParentSignupActivity(){
        Intent parentSignup = new Intent(ParentLogin.this, ParentSignup.class);
        startActivity(parentSignup);
        this.finish();
    }

    public void startParentPortalActivity(){
        Intent parentPortal = new Intent(ParentLogin.this, ParentPortal.class);
        startActivity(parentPortal);
        this.finish();
    }

    //moves to the MainActivity page
    public void startLandingPageActivity(){
        Intent landingPageActivity = new Intent(ParentLogin.this, MainActivity.class);
        startActivity(landingPageActivity);
        this.finish();
    }

    public void startForgotPasswordActivity(){
        Intent forgotPasswordActivity = new Intent(ParentLogin.this, ParentForgotPassword.class);
        startActivity(forgotPasswordActivity);
        this.finish();
    }

    private void hideKeyboard(View view, boolean hasFocus) {
        if (!hasFocus) {
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the Realm instance.
        realm.close();
    }
}