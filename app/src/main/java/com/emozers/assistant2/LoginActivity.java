package com.emozers.assistant2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity
{

    // TODO: Add member variables here:
    // UI references.
    private FirebaseAuth mAuth;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // TODO: Grab an instance of FirebaseAuth
        mAuth=FirebaseAuth.getInstance();
    }

    // Executed when Sign in button pressed
    public void signInExistingUser(View v)
    {
        // TODO: Call attemptLogin() here
        attemptLogin();

    }

    // Executed when Register button pressed
    public void registerNewUser(View v)
    {
        Intent intent = new Intent(this, com.emozers.assistant2.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    // TODO: Complete the attemptLogin() method
    private void attemptLogin()
    {

        final String email=mEmailView.getText().toString();
        String password=mPasswordView.getText().toString();
        if(email.equals("")||password.equals(""))
            return;
        Toast.makeText(this,"Login in progress",Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("xyz1234","signing with email "+task.isSuccessful());
                if(!task.isSuccessful()){
                    showErrorDialog(task.getException().getMessage());
                }
                else{
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("Email",email);
                    String temp="";
                    for(int i=0;i<email.length();i++)
                    {
                        if(email.charAt(i)!='@')
                            temp+=email.charAt(i);
                        else
                            break;
                    }
                    final String username=temp;
                    intent.putExtra("Username",username);
                    //intent.putExtra("")
                    finish();
                    startActivity(intent);
                }
            }
        });
        // TODO: Use FirebaseAuth to sign in with email & password

    }

    // TODO: Show error on screen with an alert dialog
    void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }



}