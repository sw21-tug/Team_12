package com.team12.myopensecret

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException

class DataBaseHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MOSDatabase"
        private val TABLE_JOURNALS = "JournalsTable"
        private val J_KEY_ID = "journal_id"
        private val J_KEY_TITLE = "title"
        private val J_KEY_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_JOURNALS + "("
                + J_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + J_KEY_TITLE + " TEXT,"
                + J_KEY_DESCRIPTION + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNALS)
        onCreate(db)
    }

    private fun getLastJournalID():Long {
        var db = this.readableDatabase
        val query:String = "SELECT "+J_KEY_ID+" from "+ TABLE_JOURNALS +" order by "+ J_KEY_ID +" DESC limit 1";
        var c = db.rawQuery(query,null);
        if (c != null && c.moveToFirst()) {
            val retVal = c.getLong(0)
            c.close()
            return retVal
        }
        return -1
    }

    fun addJournalEntry(jde: JournalDataEntry):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(J_KEY_TITLE, jde.title)
        contentValues.put(J_KEY_DESCRIPTION, jde.description)

        val id = db.insert(TABLE_JOURNALS, null, contentValues)
        db.close()
        jde.dbId = id.toInt()

        return id
    }

    fun viewJournalEntries():List<JournalDataEntry>{
        val jList:ArrayList<JournalDataEntry> = ArrayList<JournalDataEntry>()
        val selectQuery = "SELECT  * FROM $TABLE_JOURNALS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var title: String
        var description: String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(J_KEY_ID))
                title = cursor.getString(cursor.getColumnIndex(J_KEY_TITLE))
                description = cursor.getString(cursor.getColumnIndex(J_KEY_DESCRIPTION))
                val emp= JournalDataEntry(title, description, ArrayList(), id)
                jList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return jList
    }

    // NOT TESTED
    fun updateEmployee(emp: JournalDataEntry):Int{
        if (emp.dbId == -1)
            return -1

        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(J_KEY_ID, emp.dbId)
        contentValues.put(J_KEY_TITLE, emp.title)
        contentValues.put(J_KEY_DESCRIPTION,emp.description)

        val success = db.update(TABLE_JOURNALS, contentValues, J_KEY_ID+"="+emp.dbId,null)

        db.close()
        return success
    }

    // NOT TESTED
    fun deleteEmployee(emp: JournalDataEntry):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(J_KEY_ID, emp.dbId)

        val success = db.delete(TABLE_JOURNALS,J_KEY_ID+"="+emp.dbId,null)

        db.close()
        return success
    }
}