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
        private const val TABLE_DATAFIELDS = "DataFieldsTable"
        private const val TABLE_DATAFIELDSENTRY = "DataFieldsEntryTable"
        private const val J_KEY_ID = "journal_id"
        private const val J_KEY_TITLE = "title"
        private const val J_KEY_DESCRIPTION = "description"
        private const val J_KEY_LABELS = "labels" // shouldn't be stored as string, but...
        private const val J_KEY_DATA_FIELDS = "data_fields"
        private const val L_KEY_ID = "labels_id"
        private const val L_KEY_NAME = "name"
        private const val L_KEY_COLOR = "color"
        private const val D_KEY_ID = "df_id"
        private const val D_KEY_ALWAYS = "df_always"
        private const val D_KEY_NAME = "df_name"
        private const val D_KEY_TYPE= "df_type"
        private const val DE_KEY_ID = "de_id"
        private const val DE_KEY_DF = "de_df_id"
        private const val DE_KEY_VALUE = "de_value"
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
        val CREATE_DATA_FIELD_TABLE = ("CREATE TABLE " + TABLE_DATAFIELDS + "("
                + D_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + D_KEY_NAME + " TEXT,"
                + D_KEY_ALWAYS+" TEXT,"
                + D_KEY_TYPE + " TEXT)")
        db?.execSQL(CREATE_DATA_FIELD_TABLE)
        val CREATE_DATA_FIELD_ENTRY_TABLE = ("CREATE TABLE " + TABLE_DATAFIELDSENTRY + "("
                + DE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + DE_KEY_DF + " INTEGER,"
                + DE_KEY_VALUE + " TEXT" + ")")
        db?.execSQL(CREATE_DATA_FIELD_ENTRY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNALS)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_DATAFIELDS)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_DATAFIELDSENTRY)
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

    fun addDataField(dataField: DataFieldData):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(D_KEY_NAME, dataField.name)
        contentValues.put(D_KEY_ALWAYS, dataField.alwaysAdd)
        contentValues.put(D_KEY_TYPE, dataField.type)

        val id = db.insert(TABLE_DATAFIELDS, null, contentValues)
        db.close()
        dataField.dfId = id.toInt()

        return id
    }

    fun addDataFieldEntry(dataField: DataFieldData):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DE_KEY_VALUE, dataField.value)
        contentValues.put(DE_KEY_DF, dataField.dfId)

        val id = db.insert(TABLE_DATAFIELDSENTRY, null, contentValues)
        db.close()
        dataField.dbId = id.toInt()

        return id
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

    fun viewDataFieldEntries():List<DataFieldData>{
        val lList:ArrayList<DataFieldData> = ArrayList<DataFieldData>()
        val selectQuery = "SELECT  * FROM $TABLE_DATAFIELDS"
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
        var always: String
        var type: String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(D_KEY_ID))
                always = cursor.getString(cursor.getColumnIndex(D_KEY_ALWAYS))
                name = cursor.getString(cursor.getColumnIndex(D_KEY_NAME))
                type = cursor.getString(cursor.getColumnIndex(D_KEY_TYPE))
                val emp= DataFieldData(always, name, type, null, null, id)
                lList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lList
    }

    /*private fun getDataFieldById(id:Int):DataFieldData? {
        if (id == -1)
            return null
        var db = this.readableDatabase
        val query:String = "SELECT * from "+ TABLE_DATAFIELDS +" where "+ L_KEY_ID+"="+id
        var c = db.rawQuery(query,null);
        if (c != null && c.moveToFirst()) {
            val id = c.getInt(c.getColumnIndex(D_KEY_ID))
            val name = c.getString(c.getColumnIndex(D_KEY_NAME))
            val type = c.getString(c.getColumnIndex(D_KEY_TYPE))
            val value = c.getString(c.getColumnIndex(D_KEY_VALUE))
            c.close()

            return DataFieldData(name, type, value, id)
        }
        return null
    }*/

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