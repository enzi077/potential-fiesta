package com.example.passwordgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    DocumentReference upd_ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button delButton = (Button) findViewById(R.id.delButton);
        Button updButton = (Button) findViewById(R.id.updButton);

        Intent in = getIntent();
        final String pwd_at_i = in.getStringExtra("com.example.passwordgenerator.PWD_ITEM");
        final String rsn_at_i = in.getStringExtra("com.example.passwordgenerator.RSN_ITEM");
        final String uid = in.getStringExtra("com.example.passwordgenerator.SAVE_UID");


        TextView pwdselectedTextView = (TextView) findViewById(R.id.pwdselectedTextView);
        TextView rsnselectedTextView = (TextView) findViewById(R.id.rsnselectedTextView);

        pwdselectedTextView.setText(pwd_at_i);
        rsnselectedTextView.setText(rsn_at_i);

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder delAlert = new AlertDialog.Builder(UpdateActivity.this);
                delAlert.setTitle("Delete?");
                delAlert.setMessage("Are you aure you want to delete this password?");
                delAlert.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db = FirebaseFirestore.getInstance();

                        upd_ref = db.collection("passwords").document(uid);
                        Map<String, Object> del = new HashMap<>();
                        del.put(rsn_at_i, FieldValue.delete());
                        upd_ref.update(del).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                });
                delAlert.setNegativeButton("No", null);
                delAlert.show();
            }
        });

        updButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pass_to_upd = new Intent(getApplicationContext(), FinalUpdateActivity.class);
                pass_to_upd.putExtra("com.example.passwordgenerator.PWD_ITEM", pwd_at_i);
                pass_to_upd.putExtra("com.example.passwordgenerator.RSN_ITEM", rsn_at_i);
                pass_to_upd.putExtra("com.example.passwordgenerator.SAVE_UID", uid);
                startActivity(pass_to_upd);
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