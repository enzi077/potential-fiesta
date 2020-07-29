package com.example.passwordgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText forgotemailEditText;
    Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotemailEditText=findViewById(R.id.forgotemailEditText);
        resetButton=findViewById(R.id.resetButton);
        mAuth=FirebaseAuth.getInstance();

        String exp_mail=mAuth.getCurrentUser().getEmail();

        forgotemailEditText.setText(exp_mail);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr_email=forgotemailEditText.getText().toString();
                mAuth.sendPasswordResetEmail(usr_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Check your e-mail",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }else{
                            Toast.makeText(getApplicationContext(), "Try again!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class));
                        }
                    }
                });
            }
        });
    }
}