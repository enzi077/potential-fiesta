package com.example.passwordgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    EditText loginemailEditText;
    EditText loginpwdEditText;
    Button loginButton;
    Button forgotpwdButton;
    TextView signupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        loginemailEditText = findViewById(R.id.loginemailEditText);
        loginpwdEditText = findViewById(R.id.loginpwdEditText);
        loginButton = findViewById(R.id.loginButton);
        forgotpwdButton=findViewById(R.id.forgotpwdButton);
        signupTextView = findViewById(R.id.signupTextView);


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    Intent login_tomain = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(login_tomain);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Log In", Toast.LENGTH_LONG).show();
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginemailEditText.getText().toString();
                String pwd = loginpwdEditText.getText().toString();

                if (email.isEmpty()) {
                    loginemailEditText.setError("Please enter your E-Mail ID");
                    loginemailEditText.requestFocus();
                } else if (pwd.isEmpty()) {
                    loginpwdEditText.setError("Please enter your password");
                    loginpwdEditText.requestFocus();
                } else if ((email.isEmpty()) && (pwd.isEmpty())) {
                    Toast.makeText(getApplicationContext(), "Please enter your credentials", Toast.LENGTH_LONG).show();
                } else if (!(email.isEmpty()) && !(pwd.isEmpty())) {
                    auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener
                            (new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!(task.isSuccessful())) {
                                        Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        //to get this sign up user below is the code
                                        /*FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user);*/
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Error occured while signing in!", Toast.LENGTH_LONG).show();
                }
            }
        });

        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_signup = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(goto_signup);
            }
        });

        forgotpwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class));
            }
        });
    }
}