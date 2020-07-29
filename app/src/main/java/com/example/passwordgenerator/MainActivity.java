package com.example.passwordgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //ArrayList<String> pwdList=new ArrayList<String>();
    String pwdList;
    TextView outputTextView;
    EditText userinputEditText;
    EditText pwdreasonEditText;

    //private static final String TAG = "MainActivity";
    private String key;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button genButton = (Button) findViewById(R.id.genButton);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        outputTextView = (TextView) findViewById(R.id.outputTextView);
        mAuth = FirebaseAuth.getInstance();

        genButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userinputEditText = (EditText) findViewById(R.id.userinputEditText);
                TextView errormsgTextView = (TextView) findViewById(R.id.errormsgTextView);
                //pwdreasonEditText=(EditText)findViewById(R.id.pwdreasonEditText);

                int pwdchar;
                StringBuilder pwd = new StringBuilder();
                Random generator = new Random();

                pwdchar = Integer.parseInt(userinputEditText.getText().toString());
                char tempChar;
                if ((pwdchar > 8) && (pwdchar < 20)) {
                    for (int i = 0; i < pwdchar; i++) {
                        tempChar = (char) (generator.nextInt(92) + 33);
                        pwd.append(tempChar);
                    }
                } else {
                    errormsgTextView.setText(R.string.errorMsg);
                }

                String yourPwd = pwd.toString();
                outputTextView.setText(yourPwd);
            }
        });


        //todo: checking if output field empty; if no then save else toast

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getOutputText=outputTextView.getText().toString();
                if (TextUtils.isEmpty(outputTextView.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "You didn't generate a password !", Toast.LENGTH_LONG).show();
                } else {

                    //pwdList.add((String) outputTextView.getText());
                    pwdList = outputTextView.getText().toString();
                    pwdreasonEditText = (EditText) findViewById(R.id.pwdreasonEditText);
                    key = pwdreasonEditText.getText().toString();

                    //adding data to firestore


                    if (key == "" || key == null) {
                        Toast.makeText(getApplicationContext(), "For what do you need this password?", Toast.LENGTH_SHORT).show();
                    } else {
                        String currentUser = mAuth.getCurrentUser().getUid();
                        Map<String, Object> addData = new HashMap<>();
                        addData.put(key, (String) outputTextView.getText());

                        db.collection("passwords")
                                .document((String) currentUser)
                                .set(addData, SetOptions.merge());
                    }

                    Intent pwdIntent = new Intent(getApplicationContext(), PasswordList.class);
                    pwdIntent.putExtra("com.example.passwordgenerator.SAVE_PWD", pwdList);
                    pwdIntent.putExtra("com.example.passwordgenerator.SAVE_REASON", key);
                    pwdIntent.putExtra("com.example.passwordgenerator.SAVE_UID", mAuth.getCurrentUser().getUid());
                    startActivity(pwdIntent);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        userinputEditText = (EditText) findViewById(R.id.userinputEditText);
        outputTextView = (TextView) findViewById(R.id.outputTextView);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        Intent uid_to_list = new Intent(getApplicationContext(), PasswordList.class);


        switch (item.getItemId()) {
            case R.id.refresh:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.yourlist:
                uid_to_list.putExtra("com.example.passwordgenerator.SAVE_UID", uid);
                startActivity(uid_to_list);
                return true;
            case R.id.signout:
                if (uid != null) {
                    finishAffinity();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}