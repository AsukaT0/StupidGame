package io.github.asuka_neko.slimejump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;

import java.util.ArrayList;

public class DatabaseTest {

    private static Database dbHandler;

    public static final String TABLE_LEADERBOARD = "leaderboard";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SCORE = "score";

    private static final String DATABASE_NAME = "leaderboard.db";
    private static final int DATABASE_VERSION = 1;

    // SQL для создания таблицы
    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
        + TABLE_LEADERBOARD + " ("
        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COLUMN_NAME + " TEXT NOT NULL, "
        + COLUMN_SCORE + " INTEGER NOT NULL);";

    public DatabaseTest() {
        Gdx.app.log("DatabaseTest", "Database creation started");
        dbHandler = DatabaseFactory.getNewDatabase(DATABASE_NAME, DATABASE_VERSION, DATABASE_CREATE, null);

        dbHandler.setupDatabase();
        try {
            dbHandler.openOrCreateDatabase();
            dbHandler.execSQL(DATABASE_CREATE);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }

        Gdx.app.log("DatabaseTest", "Database created successfully");
    }

    public static void saveScore(String name, int score) {
        try {
            String query = "INSERT INTO " + TABLE_LEADERBOARD + " (" + COLUMN_NAME + ", " + COLUMN_SCORE + ") VALUES ('"
                + name + "', " + score + ")";
            dbHandler.execSQL(query);
            Gdx.app.log("DatabaseTest", "Score saved: " + name + " - " + score);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getLeaderboard() {
        ArrayList<String> leaderboard = new ArrayList<>();
        DatabaseCursor cursor = null;

        try {
            String query = "SELECT " + COLUMN_NAME + ", " + COLUMN_SCORE + " FROM " + TABLE_LEADERBOARD
                + " ORDER BY " + COLUMN_SCORE + " DESC LIMIT 5";
            cursor = dbHandler.rawQuery(query);

            while (cursor.next()) {
                String name = cursor.getString(0);
                int score = cursor.getInt(1);
                leaderboard.add(name + ": " + score);
            }
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return leaderboard;
    }

    public static void closeDatabase() {
        try {
            if (dbHandler != null) {
                dbHandler.closeDatabase();
                dbHandler = null;
                Gdx.app.log("DatabaseTest", "Database closed");
            }
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }
}
