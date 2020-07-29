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

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText emailEditText;
    EditText signuppwdEditText;
    Button signupButton;
    //Button loginButton;
    TextView existingusrTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth=FirebaseAuth.getInstance();
        emailEditText=findViewById(R.id.emailEditText);
        signuppwdEditText=findViewById(R.id.signuppwdEditText);
        signupButton=findViewById(R.id.signupButton);
        existingusrTextView=findViewById(R.id.existingusrTextView);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailEditText.getText().toString();
                String pwd=signuppwdEditText.getText().toString();

                if (email.isEmpty()){
                    emailEditText.setError("Please enter your E-Mail ID");
                    emailEditText.requestFocus();
                }
                else if(pwd.isEmpty()){
                    signuppwdEditText.setError("Please enter your password");
                    signuppwdEditText.requestFocus();
                }
                else if ((email.isEmpty()) && (pwd.isEmpty())){
                    Toast.makeText(getApplicationContext(),"Please enter your credentials", Toast.LENGTH_LONG).show();
                }
                else if (!(email.isEmpty()) && !(pwd.isEmpty())){
                    auth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener
                            (new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!(task.isSuccessful())){
                                Toast.makeText(getApplicationContext(),"Already an existing user. Please Log In.",Toast.LENGTH_LONG).show();
                            }
                            else{
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                //to get this sign up user below is the code
                                /*FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);*/
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error occured while signing up!", Toast.LENGTH_LONG).show();
                }
            }
        });


        existingusrTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log_inIntent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(log_inIntent);
            }
        });
    }

}