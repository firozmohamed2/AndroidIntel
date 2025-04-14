package com.example.intelclassone;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    EditText editTextName, editTextPhone;
    Spinner spinnerOptions;
    Button buttonSubmit;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;

    String[] options = {"Option 1", "Option 2"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        spinnerOptions = findViewById(R.id.spinnerOptions);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Firebase initialization
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Spinner setup
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOptions.setAdapter(adapter);




        if (currentUser != null) {
            loadUserData(currentUser.getEmail());
            Toast.makeText(this, currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    Toast.makeText(ProfileActivity.this, "Not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String selectedOption = spinnerOptions.getSelectedItem().toString();

                Map<String, Object> userData = new HashMap<>();
                userData.put("name", name);
                userData.put("phone", phone);
                userData.put("option", selectedOption);

                db.collection("intelUsers").document(currentUser.getEmail())
                        .set(userData)
                        .addOnSuccessListener(aVoid -> Toast.makeText(ProfileActivity.this, "Data saved", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void loadUserData(String email) {
        db.collection("intelUsers").document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        editTextName.setText(documentSnapshot.getString("name"));
                        editTextPhone.setText(documentSnapshot.getString("phone"));

                        String option = documentSnapshot.getString("option");
                        int index = 0;
                        for (int i = 0; i < options.length; i++) {
                            if (options[i].equals(option)) {
                                index = i;
                                break;
                            }
                        }
                        spinnerOptions.setSelection(index);
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));
    }
}
