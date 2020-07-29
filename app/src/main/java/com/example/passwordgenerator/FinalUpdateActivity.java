package com.example.passwordgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FinalUpdateActivity extends AppCompatActivity {
    private DocumentReference doc_ref;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String uid;
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        in = getIntent();

        final TextView reason_txt = (TextView) findViewById(R.id.rsnTextView);
        final EditText pwd_txt = (EditText) findViewById(R.id.pwdEditText);

        pwd_txt.setText(in.getStringExtra("com.example.passwordgenerator.PWD_ITEM"));
        reason_txt.setText(in.getStringExtra("com.example.passwordgenerator.RSN_ITEM"));
        pwd_txt.requestFocus();

        Button button = (Button) findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uid = in.getStringExtra("com.example.passwordgenerator.SAVE_UID");
                if ((uid != null) && !uid.isEmpty()) {
                    db = FirebaseFirestore.getInstance();

                    doc_ref = db.collection("passwords").document(uid);
                    String chng_pwd = pwd_txt.getText().toString();
                    Map<String, Object> updates = new HashMap<>();
                    updates.put(reason_txt.getText().toString(), chng_pwd);
                    doc_ref.set(updates, SetOptions.merge());

                    doc_ref.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(), PasswordList.class);
                                intent.putExtra("com.example.passwordgenerator.SAVE_UID", uid);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_no_ref_no_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mAuth = FirebaseAuth.getInstance();
        String uid=mAuth.getCurrentUser().getUid();
        Intent uid_to_list = new Intent(getApplicationContext(), PasswordList.class);

        switch (item.getItemId()) {
            case R.id.genPwd:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.yourlist:
                uid_to_list.putExtra("com.example.passwordgenerator.SAVE_UID", uid);
                startActivity(uid_to_list);
                return true;
            case R.id.signout:
                if (uid!=null){
                    finishAffinity();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}