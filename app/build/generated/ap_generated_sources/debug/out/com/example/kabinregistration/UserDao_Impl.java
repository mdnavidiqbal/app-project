package com.example.kabinregistration;

import android.database.Cursor;
import androidx.annotation.NonNull;
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
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<User> __insertionAdapterOfUser;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `users` (`id`,`fullName`,`email`,`passwordHash`,`phone`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final User entity) {
        statement.bindLong(1, entity.id);
        if (entity.fullName == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.fullName);
        }
        if (entity.email == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.email);
        }
        if (entity.passwordHash == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.passwordHash);
        }
        if (entity.phone == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.phone);
        }
        statement.bindLong(6, entity.createdAt);
      }
    };
  }

  @Override
  public long insert(final User user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfUser.insertAndReturnId(user);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public User getByEmail(final String email) {
    final String _sql = "SELECT * FROM users WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPasswordHash = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordHash");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final User _result;
      if (_cursor.moveToFirst()) {
        _result = new User();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfFullName)) {
          _result.fullName = null;
        } else {
          _result.fullName = _cursor.getString(_cursorIndexOfFullName);
        }
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _result.email = null;
        } else {
          _result.email = _cursor.getString(_cursorIndexOfEmail);
        }
        if (_cursor.isNull(_cursorIndexOfPasswordHash)) {
          _result.passwordHash = null;
        } else {
          _result.passwordHash = _cursor.getString(_cursorIndexOfPasswordHash);
        }
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _result.phone = null;
        } else {
          _result.phone = _cursor.getString(_cursorIndexOfPhone);
        }
        _result.createdAt = _cursor.getLong(_cursorIndexOfCreatedAt);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public User getById(final int id) {
    final String _sql = "SELECT * FROM users WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPasswordHash = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordHash");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final User _result;
      if (_cursor.moveToFirst()) {
        _result = new User();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfFullName)) {
          _result.fullName = null;
        } else {
          _result.fullName = _cursor.getString(_cursorIndexOfFullName);
        }
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _result.email = null;
        } else {
          _result.email = _cursor.getString(_cursorIndexOfEmail);
        }
        if (_cursor.isNull(_cursorIndexOfPasswordHash)) {
          _result.passwordHash = null;
        } else {
          _result.passwordHash = _cursor.getString(_cursorIndexOfPasswordHash);
        }
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _result.phone = null;
        } else {
          _result.phone = _cursor.getString(_cursorIndexOfPhone);
        }
        _result.createdAt = _cursor.getLong(_cursorIndexOfCreatedAt);
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
