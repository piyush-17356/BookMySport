package com.iiitd.bookmysport.other;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.iiitd.bookmysport.R;
import com.iiitd.bookmysport.User;
import com.iiitd.bookmysport.bottomnavbar.RecyclerViewSports;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "tag";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String email_ID;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        Intent intent = getIntent();
        int sign_out = intent.getIntExtra("signOut", 0);

        String y = Integer.toString(sign_out);

        if (sign_out == 1) {
            signOut();
        } else {
            sign_out = 0;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String email_address = account.getEmail();
                if (email_address.contains("@iiitd.ac.in")) {
                    int index = email_address.indexOf("@iiitd.ac.in");
                    email_ID = email_address;
                    String rollno = email_address.substring(0, index);
                    firebaseAuthWithGoogle(account);


                } else {
                    mGoogleSignInClient.signOut();
                    Toast.makeText(Login.this, "Please log in through your IIITD account", Toast.LENGTH_LONG).show();
                }

            } catch (ApiException e) {
                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final User userDB = new User(email_ID, new ArrayList<String>());
                            db = FirebaseFirestore.getInstance();

                            DocumentReference docRef = db.collection("Users")
                                    .document(email_ID);

                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                    } else {
                                        db.collection("Users")
                                                .document(email_ID)
                                                .set(userDB, SetOptions.merge())
                                                .addOnSuccessListener(
                                                        new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(Login.this, "User Added To Database", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                );

                                    }
                                }
                            });


                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent inte = new Intent(Login.this, RecyclerViewSports.class);
                            startActivity(inte);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "SignIn Failed", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Login.this, "You have signed out!!", Toast.LENGTH_LONG).show();
            }
        });
    }


}
