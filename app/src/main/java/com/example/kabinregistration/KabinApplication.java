package com.example.kabinregistration;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * One kabin application per user (userId is UNIQUE).
 * status is either "PENDING" or "ACCEPTED".
 */
@Entity(tableName = "kabin_applications",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = "userId", unique = true)})
public class KabinApplication {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public String groomName;
    public String brideName;
    public String groomNid;
    public String brideNid;
    public String marriageDate;
    public String denmohor;
    public String address;
    public String witness1;
    public String witness2;
    public String status;
    public long submittedAt;

    public KabinApplication() {
    }

    public KabinApplication(int userId, String groomName, String brideName,
                            String groomNid, String brideNid, String marriageDate,
                            String denmohor, String address,
                            String witness1, String witness2) {
        this.userId = userId;
        this.groomName = groomName;
        this.brideName = brideName;
        this.groomNid = groomNid;
        this.brideNid = brideNid;
        this.marriageDate = marriageDate;
        this.denmohor = denmohor;
        this.address = address;
        this.witness1 = witness1;
        this.witness2 = witness2;
        this.status = "PENDING";
        this.submittedAt = System.currentTimeMillis();
    }
}
