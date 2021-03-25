package com.team12.myopensecret

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import java.util.*
import kotlin.collections.ArrayList

class DataBaseHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MOSDatabase"
        private const val TABLE_JOURNALS = "JournalsTable"
        private const val TABLE_LABELS = "LabelsTable"
        private const val J_KEY_ID = "journal_id"
        private const val J_KEY_TITLE = "title"
        private const val J_KEY_DESCRIPTION = "description"
        private const val J_KEY_LABELS = "labels" // shouldn't be stored as string, but...
        private const val L_KEY_ID = "labels_id"
        private const val L_KEY_NAME = "name"
        private const val L_KEY_COLOR = "color"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_JOURNALS + "("
                + J_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + J_KEY_TITLE + " TEXT,"
                + J_KEY_DESCRIPTION + " TEXT," + J_KEY_LABELS + " TEXT" +")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
        val CREATE_LABELS_TABLE = ("CREATE TABLE " + TABLE_LABELS + "("
                + L_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + L_KEY_NAME + " TEXT,"
                + L_KEY_COLOR + " TEXT" + ")")
        db?.execSQL(CREATE_LABELS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNALS)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS)
        onCreate(db)
    }

    fun addJournalEntry(jde: JournalDataEntry):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(J_KEY_TITLE, jde.title)
        contentValues.put(J_KEY_DESCRIPTION, jde.description)
        var l: String = ""
        jde.labels.forEach{
            l += it.dbId.toString()+","
        }
        if (l.length > 0)
            l = l.substring(0, l.length - 1);
        contentValues.put(J_KEY_LABELS, l)

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
                var labels = ArrayList<LabelData>()
                id = cursor.getInt(cursor.getColumnIndex(J_KEY_ID))
                title = cursor.getString(cursor.getColumnIndex(J_KEY_TITLE))
                description = cursor.getString(cursor.getColumnIndex(J_KEY_DESCRIPTION))
                var labelsStr:String = cursor.getString(cursor.getColumnIndex(J_KEY_LABELS))
                val strArr = labelsStr.split(",")
                strArr.forEach{
                    if (it != "")
                        getLabelById(it.toInt())?.let { it1 -> labels.add(it1) }
                }
                val emp= JournalDataEntry(title, description, labels, id)
                jList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return jList
    }

    fun addLabelEntry(label: LabelData):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(L_KEY_NAME, label.name)
        contentValues.put(L_KEY_COLOR, label.color)

        val id = db.insert(TABLE_LABELS, null, contentValues)
        db.close()
        label.dbId = id.toInt()

        return id
    }

    fun viewLabelEntries():List<LabelData>{
        val lList:ArrayList<LabelData> = ArrayList<LabelData>()
        val selectQuery = "SELECT  * FROM $TABLE_LABELS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var name: String
        var color: String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(L_KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(L_KEY_NAME))
                color = cursor.getString(cursor.getColumnIndex(L_KEY_COLOR))
                val emp= LabelData(name, color, id)
                lList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lList
    }

    private fun getLabelById(id:Int):LabelData? {
        if (id == -1)
            return null
        var db = this.readableDatabase
        val query:String = "SELECT * from "+ TABLE_LABELS +" where "+ L_KEY_ID+"="+id
        var c = db.rawQuery(query,null);
        if (c != null && c.moveToFirst()) {
            val id = c.getInt(c.getColumnIndex(L_KEY_ID))
            val name = c.getString(c.getColumnIndex(L_KEY_NAME))
            val color = c.getString(c.getColumnIndex(L_KEY_COLOR))
            c.close()

            return LabelData(name, color, id)
        }
        return null
    }

    // NOT TESTED BUT SHOULD WORK
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

    // NOT TESTED BUT SHOULD WORK
    fun updateEmployee(jde: JournalDataEntry):Int{
        if (jde.dbId == -1)
            return -1

        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(J_KEY_ID, jde.dbId)
        contentValues.put(J_KEY_TITLE, jde.title)
        contentValues.put(J_KEY_DESCRIPTION,jde.description)

        val success = db.update(TABLE_JOURNALS, contentValues, J_KEY_ID+"="+jde.dbId,null)

        db.close()
        return success
    }

    // NOT TESTED BUT SHOULD WORK
    fun deleteEmployee(jde: JournalDataEntry):Int{
        if (jde.dbId == -1)
            return -1

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(J_KEY_ID, jde.dbId)

        val success = db.delete(TABLE_JOURNALS,J_KEY_ID+"="+jde.dbId,null)

        db.close()
        return success
    }

    // LABEL update/delete should work the same
}