package com.pipitliandani.android.facebox;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
    TextInputEditText email, password;
    Button signin;
    ProgressDialog pbDialog;
    private FirebaseAuth mAuth;
    public static FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.sign_in_button);
        pbDialog = new ProgressDialog(this);

        signin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("SignIn", "TEST");
                if (email.getText().toString().isEmpty()){
                    email.setError("Required");
                    return;
                }
                if (password.getText().toString().isEmpty()){
                    password.setError("Required");
                    return;
                }
                pbDialog.setMessage("Please Wait");
                pbDialog.setIndeterminate(true);
                pbDialog.show();
                loginProcess();
            }
        });

    }
    public void loginProcess(){
        final String emailKey = email.getText().toString();
        final String passKey = password.getText().toString();
        mAuth = FirebaseAuth.getInstance();
        Log.d("SignIn", emailKey);
        Log.d("SignIn", passKey);
        mAuth.signInWithEmailAndPassword(emailKey, passKey)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            pbDialog.dismiss();
                            Log.d("SignIn", "success");
                            finish();
                            Intent intent = new Intent(LoginActivity.this, NavigationDrawer.class);
                            intent.setFlags(intent.getFlags()|Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                        } else {
                            Log.w("failed", "Failure", task.getException());
                            new AlertDialog.Builder(getApplicationContext())
                                    .setTitle("Failed Login")
                                    .setMessage("Email or password doesn't exist")
                                    .setNegativeButton("Close", null);
                            pbDialog.dismiss();
                        }
                    }
                });
    }
}

