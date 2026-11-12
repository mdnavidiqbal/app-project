package com.example.kabinregistration;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class KabinDao_Impl implements KabinDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<KabinApplication> __insertionAdapterOfKabinApplication;

  private final EntityDeletionOrUpdateAdapter<KabinApplication> __updateAdapterOfKabinApplication;

  public KabinDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfKabinApplication = new EntityInsertionAdapter<KabinApplication>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `kabin_applications` (`id`,`userId`,`groomName`,`brideName`,`groomNid`,`brideNid`,`marriageDate`,`denmohor`,`address`,`witness1`,`witness2`,`status`,`submittedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final KabinApplication entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.userId);
        if (entity.groomName == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.groomName);
        }
        if (entity.brideName == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.brideName);
        }
        if (entity.groomNid == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.groomNid);
        }
        if (entity.brideNid == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.brideNid);
        }
        if (entity.marriageDate == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.marriageDate);
        }
        if (entity.denmohor == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.denmohor);
        }
        if (entity.address == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.address);
        }
        if (entity.witness1 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.witness1);
        }
        if (entity.witness2 == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.witness2);
        }
        if (entity.status == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.status);
        }
        statement.bindLong(13, entity.submittedAt);
      }
    };
    this.__updateAdapterOfKabinApplication = new EntityDeletionOrUpdateAdapter<KabinApplication>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `kabin_applications` SET `id` = ?,`userId` = ?,`groomName` = ?,`brideName` = ?,`groomNid` = ?,`brideNid` = ?,`marriageDate` = ?,`denmohor` = ?,`address` = ?,`witness1` = ?,`witness2` = ?,`status` = ?,`submittedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final KabinApplication entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.userId);
        if (entity.groomName == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.groomName);
        }
        if (entity.brideName == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.brideName);
        }
        if (entity.groomNid == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.groomNid);
        }
        if (entity.brideNid == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.brideNid);
        }
        if (entity.marriageDate == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.marriageDate);
        }
        if (entity.denmohor == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.denmohor);
        }
        if (entity.address == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.address);
        }
        if (entity.witness1 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.witness1);
        }
        if (entity.witness2 == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.witness2);
        }
        if (entity.status == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.status);
        }
        statement.bindLong(13, entity.submittedAt);
        statement.bindLong(14, entity.id);
      }
    };
  }

  @Override
  public long insert(final KabinApplication application) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfKabinApplication.insertAndReturnId(application);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final KabinApplication application) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfKabinApplication.handle(application);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public KabinApplication getByUserId(final int userId) {
    final String _sql = "SELECT * FROM kabin_applications WHERE userId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfGroomName = CursorUtil.getColumnIndexOrThrow(_cursor, "groomName");
      final int _cursorIndexOfBrideName = CursorUtil.getColumnIndexOrThrow(_cursor, "brideName");
      final int _cursorIndexOfGroomNid = CursorUtil.getColumnIndexOrThrow(_cursor, "groomNid");
      final int _cursorIndexOfBrideNid = CursorUtil.getColumnIndexOrThrow(_cursor, "brideNid");
      final int _cursorIndexOfMarriageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "marriageDate");
      final int _cursorIndexOfDenmohor = CursorUtil.getColumnIndexOrThrow(_cursor, "denmohor");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfWitness1 = CursorUtil.getColumnIndexOrThrow(_cursor, "witness1");
      final int _cursorIndexOfWitness2 = CursorUtil.getColumnIndexOrThrow(_cursor, "witness2");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfSubmittedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "submittedAt");
      final KabinApplication _result;
      if (_cursor.moveToFirst()) {
        _result = new KabinApplication();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        _result.userId = _cursor.getInt(_cursorIndexOfUserId);
        if (_cursor.isNull(_cursorIndexOfGroomName)) {
          _result.groomName = null;
        } else {
          _result.groomName = _cursor.getString(_cursorIndexOfGroomName);
        }
        if (_cursor.isNull(_cursorIndexOfBrideName)) {
          _result.brideName = null;
        } else {
          _result.brideName = _cursor.getString(_cursorIndexOfBrideName);
        }
        if (_cursor.isNull(_cursorIndexOfGroomNid)) {
          _result.groomNid = null;
        } else {
          _result.groomNid = _cursor.getString(_cursorIndexOfGroomNid);
        }
        if (_cursor.isNull(_cursorIndexOfBrideNid)) {
          _result.brideNid = null;
        } else {
          _result.brideNid = _cursor.getString(_cursorIndexOfBrideNid);
        }
        if (_cursor.isNull(_cursorIndexOfMarriageDate)) {
          _result.marriageDate = null;
        } else {
          _result.marriageDate = _cursor.getString(_cursorIndexOfMarriageDate);
        }
        if (_cursor.isNull(_cursorIndexOfDenmohor)) {
          _result.denmohor = null;
        } else {
          _result.denmohor = _cursor.getString(_cursorIndexOfDenmohor);
        }
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _result.address = null;
        } else {
          _result.address = _cursor.getString(_cursorIndexOfAddress);
        }
        if (_cursor.isNull(_cursorIndexOfWitness1)) {
          _result.witness1 = null;
        } else {
          _result.witness1 = _cursor.getString(_cursorIndexOfWitness1);
        }
        if (_cursor.isNull(_cursorIndexOfWitness2)) {
          _result.witness2 = null;
        } else {
          _result.witness2 = _cursor.getString(_cursorIndexOfWitness2);
        }
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _result.status = null;
        } else {
          _result.status = _cursor.getString(_cursorIndexOfStatus);
        }
        _result.submittedAt = _cursor.getLong(_cursorIndexOfSubmittedAt);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
