package com.example.kabinregistration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class KabinFormActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kabin_form);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        SessionManager session = new SessionManager(this);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser != null ? currentUser.getUid() : session.getUserUid();
        int userId = session.getUserId();

        // Check if user already submitted Kabin application in Firestore
        if (uid != null && !uid.isEmpty()) {
            firestore.collection("kabin_applications").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Toast.makeText(this,
                                    "You have already submitted an application. View status in profile.",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(this, ProfileActivity.class));
                            finish();
                        }
                    });
        }

        // Also check Room database fallback
        AppDatabase.databaseWriteExecutor.execute(() -> {
            KabinApplication existing = AppDatabase.get(this).kabinDao().getByUserId(userId);
            if (existing != null) {
                runOnUiThread(() -> {
                    Toast.makeText(this,
                            "You have already submitted an application. View status in profile.",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, ProfileActivity.class));
                    finish();
                });
            }
        });

        EditText etGroom = findViewById(R.id.etGroomName);
        EditText etBride = findViewById(R.id.etBrideName);
        EditText etGroomNid = findViewById(R.id.etGroomNid);
        EditText etBrideNid = findViewById(R.id.etBrideNid);
        EditText etDate = findViewById(R.id.etMarriageDate);
        EditText etDenmohor = findViewById(R.id.etDenmohor);
        EditText etAddress = findViewById(R.id.etAddress);
        EditText etWitness1 = findViewById(R.id.etWitness1);
        EditText etWitness2 = findViewById(R.id.etWitness2);
        CheckBox cbDeclaration = findViewById(R.id.cbDeclaration);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            EditText[] fields = {etGroom, etBride, etGroomNid, etBrideNid,
                    etDate, etDenmohor, etAddress, etWitness1, etWitness2};
            for (EditText field : fields) {
                if (field.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (!cbDeclaration.isChecked()) {
                Toast.makeText(this, "Please accept the declaration checkbox before submitting", Toast.LENGTH_SHORT).show();
                return;
            }

            String groom = etGroom.getText().toString().trim();
            String bride = etBride.getText().toString().trim();
            String groomNid = etGroomNid.getText().toString().trim();
            String brideNid = etBrideNid.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String denmohor = etDenmohor.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String witness1 = etWitness1.getText().toString().trim();
            String witness2 = etWitness2.getText().toString().trim();

            Map<String, Object> kabinData = new HashMap<>();
            kabinData.put("userUid", uid);
            kabinData.put("userId", userId);
            kabinData.put("groomName", groom);
            kabinData.put("brideName", bride);
            kabinData.put("groomNid", groomNid);
            kabinData.put("brideNid", brideNid);
            kabinData.put("marriageDate", date);
            kabinData.put("denmohor", denmohor);
            kabinData.put("address", address);
            kabinData.put("witness1", witness1);
            kabinData.put("witness2", witness2);
            kabinData.put("status", "PENDING");
            kabinData.put("submittedAt", System.currentTimeMillis());

            // Save to Firestore
            if (uid != null && !uid.isEmpty()) {
                firestore.collection("kabin_applications").document(uid).set(kabinData)
                        .addOnSuccessListener(aVoid -> {
                            // Save to Room as well
                            AppDatabase.databaseWriteExecutor.execute(() ->
                                    AppDatabase.get(this).kabinDao().insert(new KabinApplication(
                                            userId, groom, bride, groomNid, brideNid,
                                            date, denmohor, address, witness1, witness2
                                    )));

                            Toast.makeText(this, "Application submitted successfully (Pending)", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, ProfileActivity.class));
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed to submit: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                // Room Fallback if unauthenticated online
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    AppDatabase.get(this).kabinDao().insert(new KabinApplication(
                            userId, groom, bride, groomNid, brideNid,
                            date, denmohor, address, witness1, witness2
                    ));
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Application submitted successfully (Pending)", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, ProfileActivity.class));
                        finish();
                    });
                });
            }
        });
    }
}
