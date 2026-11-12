package com.example.kabinregistration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        SessionManager session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser != null ? currentUser.getUid() : session.getUserUid();
        int userId = session.getUserId();

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvStatus = findViewById(R.id.tvStatus);
        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnKabin = findViewById(R.id.btnKabin);
        Button btnMyKabin = findViewById(R.id.btnMyKabin);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Fetch User & Application info from Firestore
        if (uid != null && !uid.isEmpty()) {
            firestore.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("fullName");
                            tvWelcome.setText("Welcome, " + (name != null ? name : "User"));
                        }
                    });

            firestore.collection("kabin_applications").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String status = documentSnapshot.getString("status");
                            if ("ACCEPTED".equals(status)) {
                                tvStatus.setText("Application Status: Accepted");
                            } else {
                                tvStatus.setText("Application Status: Pending");
                            }
                            btnKabin.setText("Marriage Registration (Submitted)");
                        } else {
                            tvStatus.setText("Application Status: Not Applied Yet");
                        }
                    });
        } else {
            // Local Room DB Fallback
            AppDatabase.databaseWriteExecutor.execute(() -> {
                AppDatabase db = AppDatabase.get(this);
                User user = db.userDao().getById(userId);
                KabinApplication application = db.kabinDao().getByUserId(userId);
                runOnUiThread(() -> {
                    tvWelcome.setText("Welcome, " + (user != null ? user.fullName : "User"));
                    if (application == null) {
                        tvStatus.setText("Application Status: Not Applied Yet");
                    } else if ("ACCEPTED".equals(application.status)) {
                        tvStatus.setText("Application Status: Accepted");
                        btnKabin.setText("Marriage Registration (Submitted)");
                    } else {
                        tvStatus.setText("Application Status: Pending");
                        btnKabin.setText("Marriage Registration (Submitted)");
                    }
                });
            });
        }

        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));

        btnKabin.setOnClickListener(v ->
                startActivity(new Intent(this, KabinFormActivity.class)));

        btnMyKabin.setOnClickListener(v -> {
            if (uid != null && !uid.isEmpty()) {
                firestore.collection("kabin_applications").document(uid).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                startActivity(new Intent(this, KabinDetailsActivity.class));
                            } else {
                                Toast.makeText(this, "You have not submitted a Kabin application yet.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    KabinApplication app = AppDatabase.get(this).kabinDao().getByUserId(userId);
                    runOnUiThread(() -> {
                        if (app == null) {
                            Toast.makeText(this, "You have not submitted a Kabin application yet.", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(this, KabinDetailsActivity.class));
                        }
                    });
                });
            }
        });

        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
