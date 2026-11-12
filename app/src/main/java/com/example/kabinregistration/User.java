package com.example.kabinregistration;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * One row = one profile. email is UNIQUE, so one email can never
 * create a second profile. Room/SQLite enforces this rule.
 */
@Entity(tableName = "users",
        indices = {@Index(value = "email", unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String fullName;
    public String email;
    // Never store the plain password - only its SHA-256 hash.
    public String passwordHash;
    public String phone;
    public long createdAt;

    public User() {
    }

    public User(String fullName, String email, String passwordHash, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.createdAt = System.currentTimeMillis();
    }
}
