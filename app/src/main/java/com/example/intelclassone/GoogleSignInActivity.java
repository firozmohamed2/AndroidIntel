package com.example.intelclassone;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class GoogleSignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private TextView txtUserInfo;
    FirebaseFirestore db;
    FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        db = FirebaseFirestore.getInstance();



        txtUserInfo = findViewById(R.id.txtUserInfo);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))  // Firebase Web Client ID
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnGoogleSignIn).setOnClickListener(v -> signIn());

        // Check if user is already logged in
         currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            redirectToMainActivity();
            return; // Stop further execution
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    authenticateWithFirebase(account);
                }
            } catch (ApiException e) {
                Log.w("SignInActivity", "Google sign in failed", e);
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void authenticateWithFirebase(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(GoogleSignInActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String userInfo = "Name: " + user.getDisplayName() + "\n" +
                    "Email: " + user.getEmail() + "\n" +
                    "UID: " + user.getUid();
            txtUserInfo.setText(userInfo);
        } else {
            txtUserInfo.setText("No user signed in");
        }
    }

    private void signOut() {
        firebaseAuth.signOut();
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(GoogleSignInActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
            updateUI(null);
        });
    }

    private void redirectToMainActivity() {


        db.collection("intelUsers").document(currentUser.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.contains("option")) {
                            String optionValue = documentSnapshot.getString("option");
                            Log.d("FirestoreCheck", "Option exists: " + optionValue);

                            // You can now use the optionValue as needed
                            Intent intent = new Intent(GoogleSignInActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish(); // Close GoogleSignInActivity
                        } else {
                            Log.d("FirestoreCheck", "Option field does not exist.");
                            // Option field is missing
                            Intent intent = new Intent(GoogleSignInActivity.this, ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish(); // Close GoogleSignInActivity
                        }
                    } else {
                        Log.d("FirestoreCheck", "User document does not exist.");
                        Intent intent = new Intent(GoogleSignInActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish(); // Close GoogleSignInActivity
                    }
                })
                .addOnFailureListener(e -> Log.e("FirestoreCheck", "Error checking document", e));





    }

}
