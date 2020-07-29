package com.example.passwordgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PasswordList extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    ListView mylist_view;
    DocumentReference doc_ref;
    ArrayList<String> reasonlist;
    ArrayList<String> pwdlist;
    ItemAdapter itemAdapter;
    String searchTxt;

    public void delallpwd() {
        AlertDialog.Builder delAlert = new AlertDialog.Builder(PasswordList.this);
        delAlert.setTitle("Delete?");
        delAlert.setMessage("Are you aure you want to delete all your passwords?");
        delAlert.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mAuth = FirebaseAuth.getInstance();
                String uid = mAuth.getCurrentUser().getUid();
                db.collection("passwords")
                        .document(uid)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "All passwords deleted!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error in deleting!", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        delAlert.setNegativeButton("No", null);
        delAlert.show();
    }

    public void search(){
        AlertDialog.Builder searchBuilder=new AlertDialog.Builder(PasswordList.this, R.style.AlertDialog);
        searchBuilder.setTitle("Search your password");
        searchBuilder.setMessage("Enter the reason/description of the password...");

        final EditText input=new EditText(getApplicationContext());

        //specifying type of input
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setTextColor(getResources().getColor(R.color.txtColor));
        searchBuilder.setView(input);

        searchBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                searchTxt=input.getText().toString();
                Intent passtosearch=new Intent(getApplicationContext(), FilterSearchActivity.class);
                passtosearch.putExtra("com.example.passwordgenerator.SAVE_RSN_SEARCH",searchTxt);
                startActivity(passtosearch);
            }
        });
        searchBuilder.setNegativeButton("Cancel",null);
        searchBuilder.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String uid = getIntent().getStringExtra("com.example.passwordgenerator.SAVE_UID");

        if (uid != null) {
            doc_ref = db.collection("passwords").document(uid);

            if (!uid.isEmpty()) {
                doc_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                if (true) {
                                    pwdlist = new ArrayList<>();
                                    reasonlist = new ArrayList<>();
                                    Map<String, Object> map = document.getData();
                                    if (map != null) {
                                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                                            pwdlist.add(entry.getValue().toString());
                                            reasonlist.add(entry.getKey());
                                        }


                                        itemAdapter = new ItemAdapter
                                                (getApplicationContext(), pwdlist, reasonlist);

                                        mylist_view = (ListView) findViewById(R.id.mylistview);
                                        mylist_view.setAdapter(itemAdapter);
                                        itemAdapter.notifyDataSetChanged();


                                        //mylist_view = (ListView) findViewById(R.id.mylistview);
                                        mylist_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
                                                intent.putExtra("com.example.passwordgenerator.PWD_ITEM", pwdlist.get(i));
                                                intent.putExtra("com.example.passwordgenerator.RSN_ITEM", reasonlist.get(i));
                                                intent.putExtra("com.example.passwordgenerator.SAVE_UID", uid);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Your password list wasn't found!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        } else {
            Toast.makeText(getApplicationContext(), "User not found!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_no_ref, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        switch (item.getItemId()) {
            case R.id.search:
                search();
                return true;
            case R.id.genPwd:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.delall:
                delallpwd();
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

//do list sorting for display