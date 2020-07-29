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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FilterSearchActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    ItemAdapter adapter_filter;
    ListView myfilterlist_view;
    String rsntxtfilter;
    String uid;
    String password;
    ArrayList<String> pwd;
    ArrayList<String> rsn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        rsn = new ArrayList<>();

        Intent searchTxt = getIntent();
        rsntxtfilter = searchTxt.getStringExtra("com.example.passwordgenerator.SAVE_RSN_SEARCH");
        if (rsntxtfilter != null) {
            rsn.add(rsntxtfilter);
        } else {
            Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_LONG).show();
        }


        uid = mAuth.getCurrentUser().getUid();
        final DocumentReference documentReference = db.collection("passwords").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        pwd = new ArrayList<>();
                        pwd.add(documentSnapshot.getString(rsntxtfilter));

                        //displaying
                        myfilterlist_view = (ListView) findViewById(R.id.filterListView);
                        adapter_filter = new ItemAdapter(getApplicationContext(), pwd, rsn);
                        myfilterlist_view.setAdapter(adapter_filter);
                        adapter_filter.notifyDataSetChanged();

                        //passing values to updateActivity after tap
                        myfilterlist_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
                                intent.putExtra("com.example.passwordgenerator.PWD_ITEM", pwd.get(i));
                                intent.putExtra("com.example.passwordgenerator.RSN_ITEM", rsn.get(i));
                                intent.putExtra("com.example.passwordgenerator.SAVE_UID", uid);
                                startActivity(intent);
                            }
                        });
                    }
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
        String uid = mAuth.getCurrentUser().getUid();
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
                if (uid != null) {
                    finishAffinity();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}