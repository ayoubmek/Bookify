package com.example.formulairesae2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "formulairesae2.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_COMMENT = "Comment";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_BOOK_ID = "book_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_LIKES = "likes_count"; // <-- Add this line

    public static final String TABLE_BOOK = "Book";
    public static final String COLUMN_BOOK_ID_REF = "id";
    public static final String COLUMN_BOOK_NAME = "name";

    public static final String TABLE_USER = "User";
    public static final String COLUMN_USER_ID_REF = "id";
    public static final String COLUMN_USER_NAME = "name";

    private static final String TABLE_COMMENT_CREATE =
            "CREATE TABLE " + TABLE_COMMENT + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CONTENT + " TEXT NOT NULL, " +
                    COLUMN_AUTHOR + " TEXT NOT NULL, " +
                    COLUMN_BOOK_ID + " INTEGER, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_LIKES + " INTEGER DEFAULT 0, " +
                    "FOREIGN KEY(" + COLUMN_BOOK_ID + ") REFERENCES " + TABLE_BOOK + "(" + COLUMN_BOOK_ID_REF + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID_REF + ") ON DELETE CASCADE);";

    private static final String TABLE_BOOK_CREATE =
            "CREATE TABLE " + TABLE_BOOK + " (" +
                    COLUMN_BOOK_ID_REF + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BOOK_NAME + " TEXT NOT NULL);";

    private static final String TABLE_USER_CREATE =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_USER_ID_REF + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NAME + " TEXT NOT NULL);";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL(TABLE_BOOK_CREATE);
        db.execSQL(TABLE_USER_CREATE);
        db.execSQL(TABLE_COMMENT_CREATE);

        insertSampleBooks(db);
        insertSampleUsers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_COMMENT + " ADD COLUMN " + COLUMN_USER_ID + " INTEGER;");
        }
    }

    private void insertSampleBooks(SQLiteDatabase db) {
        String insertBook1 = "INSERT INTO " + TABLE_BOOK + " (" + COLUMN_BOOK_NAME + ") VALUES ('Betty 1');";
        String insertBook2 = "INSERT INTO " + TABLE_BOOK + " (" + COLUMN_BOOK_NAME + ") VALUES ('Book 2');";
        db.execSQL(insertBook1);
        db.execSQL(insertBook2);
    }

    private void insertSampleUsers(SQLiteDatabase db) {
        String insertUser1 = "INSERT INTO " + TABLE_USER + " (" + COLUMN_USER_NAME + ") VALUES ('Asma');";
        String insertUser2 = "INSERT INTO " + TABLE_USER + " (" + COLUMN_USER_NAME + ") VALUES ('Jane Smith');";
        db.execSQL(insertUser1);
        db.execSQL(insertUser2);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }
}
