package viasistemasweb.com.tulio.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Frank on 25/05/2015.
 */
public class DataBaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tulio";

    // Login table name
    private static final String TABLE_LOGIN = "login";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CPF = "cpf";
    private static final String KEY_TIPO_USUARIO = "tipo_usuario";
    private static final String KEY_TURMA = "turma";


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CPF + " TEXT,"
                + KEY_TIPO_USUARIO + " TEXT,"
                + KEY_TURMA + " INTEGER)";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String cpf, String tipoUsuario, Integer turmaId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CPF, cpf); // cpf
        values.put(KEY_TIPO_USUARIO, tipoUsuario); // tipo do usuário
        values.put(KEY_TURMA, turmaId); // ID da turma do usuário


        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("cpf", cursor.getString(1));
            user.put("tipo_usuario", cursor.getString(2));
            user.put("turma", cursor.getString(3));
        }
        cursor.close();
        db.close();

        HashMap<String, String>teste = user;

        String testestr = user.get("cpf");
        String testestr2 = user.get("tipo_usuario");
        String testestr3 = user.get("turma");

        // return user
        return user;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    public boolean getUserFromCpf(String cpf){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_LOGIN, new String[]{KEY_ID, KEY_CPF, KEY_TIPO_USUARIO, KEY_TURMA}, "cpf=?", new String[]{cpf}, null, null, null);
        if (c.getCount() > 0)
        {
            c.moveToFirst();
            String userCpf = c.getString(Integer.parseInt(KEY_CPF));
            Log.i("cpf do banco", userCpf);
            return true;
        }
        return false;
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

}
