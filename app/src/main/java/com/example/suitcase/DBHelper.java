package com.example.suitcase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.mindrot.jbcrypt.BCrypt;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "database", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Userdata(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, password TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Itemdata(id INTEGER PRIMARY KEY AUTOINCREMENT, itemName TEXT, itemDescription TEXT,price TEXT, purchased INTEGER, status TEXT, userId INTEGER, FOREIGN KEY(userId) REFERENCES Userdata(id))");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists Itemdata");
        sqLiteDatabase.execSQL("drop table if exists Userdata");
    }

    public Boolean saveitemdata(String itemName, String itemDescription, String price, Integer purchased, Integer userId){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("itemName", itemName);
        contentValues.put("itemDescription", itemDescription);
        contentValues.put("price", price);
        contentValues.put("purchased", purchased);
        contentValues.put("status", "active");
        contentValues.put("userId", userId);
        long result = sqLiteDatabase.insert("Itemdata", null, contentValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public Boolean signupUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("email", email);
            values.put("password", hashedPassword);

            long result = db.insert("Userdata", null, values);
            if(result == -1){
                return false;
            }else {
                return true;
            }
    }

    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean isAuthenticated = false;
            Cursor cursor = db.rawQuery("SELECT password FROM Userdata WHERE email=?", new String[]{email});

            if (cursor.moveToFirst()) {
                String hashedPasswordFromDB = cursor.getString(0);

                isAuthenticated = BCrypt.checkpw(password, hashedPasswordFromDB);
            }

            cursor.close();

        return isAuthenticated;
    }

    public Boolean updateitemdata(String id, String itemName, String itemDescription, String price, Integer purchased){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("itemName", itemName);
        contentValues.put("itemDescription", itemDescription);
        contentValues.put("price", price);
        contentValues.put("purchased", purchased);
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Itemdata where id=?", new String[]{id});
        if (cursor.getCount()>0){
        long result = sqLiteDatabase.update("Itemdata", contentValues, "id=?", new String[] {id});
        if (result == -1){
            return false;
        }else {
            return true;
        }
        }else {
            return false;
        }
    }

    public Boolean markAsPurchased(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("purchased", 1);
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Itemdata where id=?", new String[]{id});
        if (cursor.getCount()>0){
        long result = sqLiteDatabase.update("Itemdata", contentValues, "id=?", new String[] {id});
        if (result == -1){
            return false;
        }else {
            return true;
        }
        }else {
            return false;
        }
    }
    public Boolean unmarkAsPurchased(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("purchased", 0);
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Itemdata where id=?", new String[]{id});
        if (cursor.getCount()>0){
        long result = sqLiteDatabase.update("Itemdata", contentValues, "id=?", new String[] {id});
        if (result == -1){
            return false;
        }else {
            return true;
        }
        }else {
            return false;
        }
    }

    public Boolean deleteitemdata(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", "deleted");
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Itemdata where id=?", new String[]{id});
        if (cursor.getCount()>0){
        long result = sqLiteDatabase.update("Itemdata", contentValues,"id=?", new String[] {id});
        if (result == -1){
            return false;
        }else {
            return true;
        }
        }else {
            return false;
        }
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String tableName = "Userdata";
        String columnName = "email";

        String query = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }


    public Cursor getdata(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String[]  selectionArgs = {String.valueOf(id)};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Itemdata WHERE status = 'active' AND userId=?", selectionArgs);
//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Itemdata", null);
        return cursor;
    }
    public Cursor getItemData(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", "active");
        sqLiteDatabase.update("Itemdata", contentValues,"id=?", new String[] {id});
        String[] selectionArgs = {id};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Itemdata WHERE id=?", selectionArgs);
//      Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Itemdata", null);
        return cursor;
    }

    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        // Define the columns you want to retrieve
        String[] columns = {"id", "name", "email"};

        // Define the selection criteria (WHERE clause)
        String selection = "email = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query("Userdata", columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Retrieve user data from the cursor
                int userId = cursor.getInt(0);
                String name = cursor.getString(1);
                String userEmail = cursor.getString(2);

                // Create a User object with the retrieved data
                user = new User(userId, name, userEmail);
            }

            cursor.close();
        }

        db.close();

        return user;
    }

}

