package com.example.kabinregistration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class KabinDetailsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kabin_details);

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

        TextView tvRefNo = findViewById(R.id.tvRefNo);
        TextView tvDetailStatus = findViewById(R.id.tvDetailStatus);
        TextView tvDetailGroomName = findViewById(R.id.tvDetailGroomName);
        TextView tvDetailGroomNid = findViewById(R.id.tvDetailGroomNid);
        TextView tvDetailBrideName = findViewById(R.id.tvDetailBrideName);
        TextView tvDetailBrideNid = findViewById(R.id.tvDetailBrideNid);
        TextView tvDetailMarriageDate = findViewById(R.id.tvDetailMarriageDate);
        TextView tvDetailDenmohor = findViewById(R.id.tvDetailDenmohor);
        TextView tvDetailAddress = findViewById(R.id.tvDetailAddress);
        TextView tvDetailWitness1 = findViewById(R.id.tvDetailWitness1);
        TextView tvDetailWitness2 = findViewById(R.id.tvDetailWitness2);
        TextView tvDetailSubmittedAt = findViewById(R.id.tvDetailSubmittedAt);

        // Fetch from Firebase Firestore
        if (uid != null && !uid.isEmpty()) {
            firestore.collection("kabin_applications").document(uid).get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            String groom = doc.getString("groomName");
                            String bride = doc.getString("brideName");
                            String groomNid = doc.getString("groomNid");
                            String brideNid = doc.getString("brideNid");
                            String date = doc.getString("marriageDate");
                            String denmohor = doc.getString("denmohor");
                            String address = doc.getString("address");
                            String witness1 = doc.getString("witness1");
                            String witness2 = doc.getString("witness2");
                            String status = doc.getString("status");
                            Long submittedAt = doc.getLong("submittedAt");

                            String shortUid = uid.length() >= 6 ? uid.substring(0, 6).toUpperCase() : uid.toUpperCase();
                            tvRefNo.setText("REF: #KABIN-" + shortUid);

                            if ("ACCEPTED".equals(status)) {
                                tvDetailStatus.setText("OFFICIALLY ACCEPTED");
                                tvDetailStatus.setBackgroundColor(android.graphics.Color.parseColor("#D1FAE5"));
                                tvDetailStatus.setTextColor(android.graphics.Color.parseColor("#059669"));
                            } else {
                                tvDetailStatus.setText("PENDING VERIFICATION");
                                tvDetailStatus.setBackgroundColor(android.graphics.Color.parseColor("#FEF3C7"));
                                tvDetailStatus.setTextColor(android.graphics.Color.parseColor("#D97706"));
                            }

                            tvDetailGroomName.setText("Groom Full Name: " + (groom != null ? groom : ""));
                            tvDetailGroomNid.setText("Groom NID Number: " + (groomNid != null ? groomNid : ""));
                            tvDetailBrideName.setText("Bride Full Name: " + (bride != null ? bride : ""));
                            tvDetailBrideNid.setText("Bride NID Number: " + (brideNid != null ? brideNid : ""));
                            tvDetailMarriageDate.setText("Date of Marriage: " + (date != null ? date : ""));
                            tvDetailDenmohor.setText("Denmohor Amount: " + (denmohor != null ? denmohor : "") + " BDT");
                            tvDetailAddress.setText("Marriage Venue / Address: " + (address != null ? address : ""));
                            tvDetailWitness1.setText("Witness 1: " + (witness1 != null ? witness1 : ""));
                            tvDetailWitness2.setText("Witness 2: " + (witness2 != null ? witness2 : ""));

                            if (submittedAt != null) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                tvDetailSubmittedAt.setText("Submission Timestamp: " + sdf.format(new Date(submittedAt)));
                            }
                        } else {
                            Toast.makeText(this, "No Kabin application record found.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to load application details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            // Room Fallback
            AppDatabase.databaseWriteExecutor.execute(() -> {
                KabinApplication app = AppDatabase.get(this).kabinDao().getByUserId(userId);
                runOnUiThread(() -> {
                    if (app != null) {
                        tvRefNo.setText("REF: #KABIN-LOCAL-" + app.id);
                        if ("ACCEPTED".equals(app.status)) {
                            tvDetailStatus.setText("OFFICIALLY ACCEPTED");
                            tvDetailStatus.setBackgroundColor(android.graphics.Color.parseColor("#D1FAE5"));
                            tvDetailStatus.setTextColor(android.graphics.Color.parseColor("#059669"));
                        } else {
                            tvDetailStatus.setText("PENDING VERIFICATION");
                            tvDetailStatus.setBackgroundColor(android.graphics.Color.parseColor("#FEF3C7"));
                            tvDetailStatus.setTextColor(android.graphics.Color.parseColor("#D97706"));
                        }
                        tvDetailGroomName.setText("Groom Full Name: " + app.groomName);
                        tvDetailGroomNid.setText("Groom NID Number: " + app.groomNid);
                        tvDetailBrideName.setText("Bride Full Name: " + app.brideName);
                        tvDetailBrideNid.setText("Bride NID Number: " + app.brideNid);
                        tvDetailMarriageDate.setText("Date of Marriage: " + app.marriageDate);
                        tvDetailDenmohor.setText("Dower (Mahr) Amount: " + app.denmohor + " BDT");
                        tvDetailAddress.setText("Marriage Venue / Address: " + app.address);
                        tvDetailWitness1.setText("Witness 1: " + app.witness1);
                        tvDetailWitness2.setText("Witness 2: " + app.witness2);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        tvDetailSubmittedAt.setText("Submission Timestamp: " + sdf.format(new Date(app.submittedAt)));
                    } else {
                        Toast.makeText(this, "No Kabin application record found.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            });
        }
    }
}
