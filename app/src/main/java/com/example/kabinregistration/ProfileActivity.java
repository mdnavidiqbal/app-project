package com.example.kabinregistration;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        SessionManager session = new SessionManager(this);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser != null ? currentUser.getUid() : session.getUserUid();
        int userId = session.getUserId();

        TextView tvName = findViewById(R.id.tvName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvPhone = findViewById(R.id.tvPhone);
        TextView tvApplication = findViewById(R.id.tvApplication);
        TextView tvStatus = findViewById(R.id.tvStatus);
        Button btnAcceptDemo = findViewById(R.id.btnAcceptDemo);

        // Fetch user profile and application from Firestore
        if (uid != null && !uid.isEmpty()) {
            firestore.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("fullName");
                            String email = documentSnapshot.getString("email");
                            String phone = documentSnapshot.getString("phone");

                            tvName.setText("Name: " + (name != null ? name : ""));
                            tvEmail.setText("Email: " + (email != null ? email : ""));
                            tvPhone.setText("Phone: " + (phone != null && !phone.isEmpty() ? phone : "Not provided"));
                        }
                    });

            firestore.collection("kabin_applications").document(uid).get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            String groom = doc.getString("groomName");
                            String bride = doc.getString("brideName");
                            String date = doc.getString("marriageDate");
                            String denmohor = doc.getString("denmohor");
                            String status = doc.getString("status");

                            tvApplication.setText("Marriage Application: Groom " + groom
                                    + ", Bride " + bride
                                    + "\nMarriage Date: " + date
                                    + "\nDower (Mahr): " + denmohor + " BDT");

                            if ("ACCEPTED".equals(status)) {
                                tvStatus.setText("Status: Accepted");
                                btnAcceptDemo.setVisibility(View.GONE);
                            } else {
                                tvStatus.setText("Status: Pending");
                                btnAcceptDemo.setVisibility(View.VISIBLE);
                                btnAcceptDemo.setOnClickListener(v ->
                                        firestore.collection("kabin_applications").document(uid)
                                                .update("status", "ACCEPTED")
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(this, "Demo: Application has been Accepted", Toast.LENGTH_SHORT).show();
                                                    recreate();
                                                }));
                            }
                        } else {
                            tvApplication.setText("Marriage Application: Not Applied Yet");
                            tvStatus.setText("Status: N/A");
                            btnAcceptDemo.setVisibility(View.GONE);
                        }
                    });
        } else {
            // Local Room DB Fallback
            AppDatabase.databaseWriteExecutor.execute(() -> {
                AppDatabase db = AppDatabase.get(this);
                User user = db.userDao().getById(userId);
                KabinApplication application = db.kabinDao().getByUserId(userId);
                runOnUiThread(() -> {
                    tvName.setText("Name: " + (user != null ? user.fullName : ""));
                    tvEmail.setText("Email: " + (user != null ? user.email : ""));
                    String phone = (user != null && user.phone != null && !user.phone.isEmpty())
                            ? user.phone : "Not provided";
                    tvPhone.setText("Phone: " + phone);

                    if (application == null) {
                        tvApplication.setText("Marriage Application: Not Applied Yet");
                        tvStatus.setText("Status: N/A");
                        btnAcceptDemo.setVisibility(View.GONE);
                    } else {
                        tvApplication.setText("Marriage Application: Groom " + application.groomName
                                + ", Bride " + application.brideName
                                + "\nMarriage Date: " + application.marriageDate
                                + "\nDower (Mahr): " + application.denmohor + " BDT");
                        if ("ACCEPTED".equals(application.status)) {
                            tvStatus.setText("Status: Accepted");
                            btnAcceptDemo.setVisibility(View.GONE);
                        } else {
                            tvStatus.setText("Status: Pending");
                            btnAcceptDemo.setVisibility(View.VISIBLE);
                            btnAcceptDemo.setOnClickListener(v ->
                                    AppDatabase.databaseWriteExecutor.execute(() -> {
                                        application.status = "ACCEPTED";
                                        db.kabinDao().update(application);
                                        runOnUiThread(() -> {
                                            Toast.makeText(this,
                                                    "Demo: Application has been Accepted",
                                                    Toast.LENGTH_SHORT).show();
                                            recreate();
                                        });
                                    }));
                        }
                    }
                });
            });
        }
    }
}
