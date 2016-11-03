package com.findmybusnj.findmybusnj.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.findmybusnj.findmybusnj.models.Favorite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidaghassi on 10/28/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "savedFavorites";
    private static final String TABLE_FAVORITES = "favorites";

    // Table variables
    private static final String KEY_PK = "stopAndRoute";
    private static final String KEY_STOP = "stop";
    private static final String KEY_ROUTE = "route";
    private static final String KEY_FREQ = "frequency";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Creates table for the given database
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + KEY_PK + " INTEGER PRIMARY KEY"
                + KEY_STOP + " TEXT," + KEY_ROUTE + " TEXT,"
                + KEY_FREQ + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);

        // Create tables again
        onCreate(db);
    }

    /** CRUD **/

    /**
     * Inserts a favorite object into the database
     * @param favorite The favorite object to be insterted.
     */
    public void addFavorite(Favorite favorite){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PK, favorite.generatePrimaryKey());
        values.put(KEY_STOP, favorite.getStop());
        values.put(KEY_ROUTE, favorite.getRoute());

        favorite.incrementFrequency();
        values.put(KEY_FREQ, favorite.getFrequency());

        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    /**
     * Removes the given favorite if it exists in the database
     * @param favorite
     */
    public void deleteFavorite(Favorite favorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, KEY_PK + " = ?",
                new String[] { String.valueOf(favorite.generatePrimaryKey())});
        db.close();
    }

    /**
     * Gets a favorite from the database given a key
     * @return Favorite fetched from the database
     */
    public Favorite getFavorite(int key) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITES, new String[] {KEY_PK, KEY_STOP, KEY_ROUTE, KEY_FREQ},
                KEY_PK + "=?", new String[]{ String.valueOf(key)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        String stop = cursor.getString(1);
        String route = cursor.getString(2);
        int frequency = Integer.parseInt(cursor.getString(3));
        Favorite favorite = new Favorite(stop, route, frequency);

        return favorite;
    }

    /**
     * Gets a list of all the favorites from the database
     * @return A list of all the favorites in the table, or an empty list none
     */
    public List<Favorite> getAllFavorites() {
        // Setting up query and return array
        List<Favorite> favoriteList = new ArrayList<Favorite>();
        String selectQuery = "SELECT * FROM " + TABLE_FAVORITES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all the rosw and add to the list
        if (cursor.moveToFirst()) {
            do {
                // Create the favorite
                Favorite favorite = new Favorite();
                favorite.setStop(cursor.getString(1));
                favorite.setRoute(cursor.getString(2));
                favorite.setFrequency(cursor.getInt(3));

                // add to the list
                favoriteList.add(favorite);
            } while (cursor.moveToNext());
        }

        return favoriteList;
    }

    /**
     * Returns the total number of favorite items in the database
     * @return The count of favorites
     */
    public int getFavoritesCount() {
        String selectQuery = "SELECT * FROM " + TABLE_FAVORITES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    /**
     * Updates the favorite if it already exists in the table
     * @param favorite Item to be updated
     * @return
     */
    public int updateFavorite(Favorite favorite) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PK, favorite.generatePrimaryKey());
        values.put(KEY_STOP, favorite.getStop());
        values.put(KEY_ROUTE, favorite.getRoute());

        favorite.incrementFrequency();
        values.put(KEY_FREQ, favorite.getFrequency());

        return db.update(TABLE_FAVORITES, values, KEY_PK + " = ?",
                new String[] { String.valueOf(favorite.generatePrimaryKey())});
    }
}
