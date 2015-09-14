package com.example.oem.oweme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 28/06/15.
 */
public class DebtDatabase extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "debtdb.db";
    public static final String CONTACTS_TABLE = "contacts1";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_AMOUNT = "amount";
    public static final String CONTACTS_COLUMN_AMOUNT_LIST = "amount_list";


    public DebtDatabase(Context context){
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CONTACTS_TABLE +
                "(" + CONTACTS_COLUMN_ID + " INTEGER PRIMARY KEY," +
                CONTACTS_COLUMN_NAME + " TEXT," +
                CONTACTS_COLUMN_AMOUNT + " INTEGER," +
                CONTACTS_COLUMN_AMOUNT_LIST + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            // Wipe older tables if existed
            db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE);
            // Create tables again
            onCreate(db);
        }
    }

    public void insertContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CONTACTS_COLUMN_NAME, contact.getName());
        cv.put(CONTACTS_COLUMN_AMOUNT, contact.getAmount());
        cv.put(CONTACTS_COLUMN_AMOUNT_LIST, contact.getAmount_list());
        long rowId = db.insertOrThrow(CONTACTS_TABLE, null, cv);
        db.close();
    }

    public List<Contact> getAllContacts(){
        List<Contact> contactArrayList= new ArrayList<Contact>();

        String query = "SELECT * FROM " + CONTACTS_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(query, null);

        if(c.moveToFirst()){
            do{
                Contact contact = new Contact();
                contact.setId(c.getInt(0));
                contact.setName(c.getString(1));
                contact.setAmount(c.getInt(2));
                contactArrayList.add(contact);
            } while(c.moveToNext());
        }
        if (c != null)
            c.close();

        return contactArrayList;
    }

    public Contact getContact(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +CONTACTS_TABLE + " where id="+id+"", null );


            cursor.moveToFirst();
            Contact contact = new Contact(cursor.getString(1), Double.parseDouble(cursor.getString(2)),
                    cursor.getString(3));
            cursor.close();
            return contact;


    }

    public void editContactById(long id, double amount, String OweOrPay){
        double newAmount;
        SQLiteDatabase db = this.getWritableDatabase();
        Contact contact = getContact(id);
        double currentAmount = contact.getAmount();

        switch (OweOrPay){
            case "TheyOweMe":
                newAmount = currentAmount + amount;
                contact.setAmount(newAmount);
                break;
            case "IOweThem":
                newAmount = currentAmount - amount;
                contact.setAmount(newAmount);
                break;
        }

        String id_filter = CONTACTS_COLUMN_ID + "=" + id;
        ContentValues args = new ContentValues();
        args.put(CONTACTS_COLUMN_AMOUNT, contact.getAmount());
        args.put(CONTACTS_COLUMN_AMOUNT_LIST, contact.getAmount_list());
        db.update(CONTACTS_TABLE, args, id_filter, null);

    }

    public void editContactByPosition(int position, double amount, String TheyOwe){
        String query = "SELECT * FROM " + CONTACTS_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(query, null);
        int position_count = 0;

        if(c.moveToFirst()){
            do{
                if(position_count == position){
                    editContactById(c.getInt(0), amount, TheyOwe);
                }
                position_count++;
            } while(c.moveToNext());
        }
        c.close();
    }

    public void deleteContactById(long id) {

        SQLiteDatabase db = this.getWritableDatabase();
        Contact contactToDelete = getContact(id);
        //return db.delete(CONTACTS_TABLE, CONTACTS_COLUMN_ID + " = ?",
         //       new String[] {String.valueOf(contactToDelete.getId()) }) > 0;
        db.delete(CONTACTS_TABLE, CONTACTS_COLUMN_ID + "='" + id + "'", null);
    }

    public void deleteContactByPosition(int position){
        String query = "SELECT * FROM " + CONTACTS_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(query, null);
        int position_count = 0;

        if(c.moveToFirst()){
            do{
                if(position_count == position){
                    deleteContactById(c.getInt(0));
                }
                position_count++;
            } while(c.moveToNext());
        }
        c.close();
    }

    public void deleteAll(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(CONTACTS_TABLE, null, null);
    }


}
