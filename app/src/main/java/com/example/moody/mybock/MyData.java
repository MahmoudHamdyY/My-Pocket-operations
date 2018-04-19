package com.example.moody.mybock;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyData {
    Context context;
    private MyDataHelp MDH;
    private SQLiteDatabase db;
    private String DB_NAME= "data";
    private int DB_VERSION = 1;
    long dd , s ,ddG , sG ,TT,TG,DDT,DDG;


    public MyData(Context context){
        this.context = context;
        MDH = new MyDataHelp(context,DB_NAME , null , DB_VERSION);

    }

    public void open(){
        db = MDH.getWritableDatabase();
    }

    public String insertTData(long Tnum, String Date, String Tname, String Tnots)throws SQLException{
        String E = "";
        try {
            ContentValues cv = new ContentValues();
            cv.put("Tnum", Tnum);
            cv.put("Tname", Tname);
            cv.put("Tnots", Tnots);
            cv.put("Tdate " , Date);

            db.insert("info", null, cv);
        }catch(SQLException e)
        {
            e.printStackTrace();
            E = e.toString();
        }
        return E;
    }

    public String insertGData(long Gnum, String Date, String Gname, String Gnots)throws SQLException{
        String E = "";
        try {
            ContentValues cv = new ContentValues();
            cv.put("Gnum", Gnum);
            cv.put("gname", Gname);
            cv.put("Gnots", Gnots);
            cv.put("Gdate " , Date);

            db.insert("infoG", null, cv);
        }catch(SQLException e)
        {
            e.printStackTrace();
            E = e.toString();
        }
        return E;
    }

    //---------------------------------------------------------------
    //IN :
    public ArrayList<String> SAD(){
        ArrayList<String> AD = new ArrayList<String>();
        Cursor cursor = db.query("info", null,null,null,null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                AD.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return AD;
    }

    public ArrayList<String> SAN(){
        ArrayList<String> AD = new ArrayList<String>();
        Cursor cursor = db.query("info", null,null,null,null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                AD.add(cursor.getString(2));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return AD;
    }

    public ArrayList<String> VAN(){
        ArrayList<String> AD = new ArrayList<String>();
        Cursor cursor = db.query("info", null,null,null,null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                AD.add(cursor.getString(1));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return AD;
    }
    public long getSum(String r_c){
        String KW = r_c.toString();
        String selection = "Tname " + "like ? ";
        Cursor cursor = db.query("info", null,selection,new String[]{"%"+KW+"%"},null,null,null);
        if(cursor !=null && cursor.moveToFirst()){
            do{
                dd = Long.parseLong(cursor.getString(1).toString());
                s = s + dd;
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return s ;
    }


    public ArrayList<String> getDb2(String r_c ){
        ArrayList<String> dd = new ArrayList<>();
        String KW = r_c.toString();
        String selection = "Tname " + "like ? ";
        Cursor cursor = db.query("info", null,selection,new String[]{"%"+KW+"%"},null,null,null);
        if(cursor !=null && cursor.moveToFirst()){
            do{
                dd.add(cursor.getString(0)+"\n " + "From :  " +cursor.getString(2)+"\n "+"price : " + cursor.getString(1)+" \n "+"Notes : " + cursor.getString(3));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return dd ;
    }
   /* public String getDb(String r_c ){
        String dd = new String();
        String KW = r_c.toString();
        String selection = "Tdate " + "like ? ";

        Cursor cursor = db.query("info", null,selection,new String[]{"%"+KW+"%"},null,null,null);
        if(cursor !=null && cursor.moveToFirst()){
            do{
                dd =(cursor.getString(0)+"\n " + "From :  " +cursor.getString(2)+"\n "+"price : " + cursor.getString(1)+" \n "+"Nots : " + cursor.getString(3));
            }
            while (cursor.moveToNext());
        }
        return dd ;
    }*/public String getDb(String r_c ){
       String dd = new String();
       String KW = r_c.toString();
       String selection = "Tdate " + "like ? ";

       Cursor cursor = db.query("info", null,selection,new String[]{"%"+KW+"%"},null,null,null);
       if(cursor !=null && cursor.moveToFirst()){
           do{
               dd =("Notes : " + cursor.getString(3));
           }
           while (cursor.moveToNext());
       }
       cursor.close();
       return dd ;
   }

    public void del(String r_c){
        db.delete("info", "Tdate = ?", new String[]{r_c + ""});


    }
    public void del2(String r_c){
        db.delete("info", "Tname = ?", new String[]{r_c + ""});


    }
    //----------------------------------------------------------------------------------------------
    //OUT :
    public ArrayList<String> SADG(){
        ArrayList<String> AD = new ArrayList<String>();
        Cursor cursor = db.query("infoG", null,null,null,null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                AD.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return AD;
    }


    public ArrayList<String> SANG(){
        ArrayList<String> AD = new ArrayList<String>();
        Cursor cursor = db.query("infoG", null,null,null,null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                AD.add(cursor.getString(2));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return AD;
    }
    public ArrayList<String> VANG(){
        ArrayList<String> AD = new ArrayList<String>();
        Cursor cursor = db.query("infoG", null,null,null,null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                AD.add(cursor.getString(1));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return AD;
    }
    public long getSumG(String r_c){
        String KW = r_c.toString();
        String selection = "Gname " + "like ? ";

        Cursor cursor = db.query("infoG", null,selection,new String[]{"%"+KW+"%"},null,null,null);
        //String selection = cursor.getString(0).toString()+" : " + cursor.getString(2).toString() + "like ?";
        //new String[]{"%"+KW+"%"},null ,null , null);
        //Cursor cursor1 = db.query( "info" ,null,selection,new String[]{"%"+KW+"%"}
        if(cursor !=null && cursor.moveToFirst()){
            do{
                ddG = Long.parseLong(cursor.getString(1).toString());
                sG = sG + ddG;
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return sG ;
    }


    public ArrayList<String> getDb2G(String r_c ){
        ArrayList<String> dd = new ArrayList<>();
        String KW = r_c.toString();
        String selection = "Gname " + "like ? ";
        Cursor cursor = db.query("infoG", null,selection,new String[]{"%"+KW+"%"},null,null,null);
        if(cursor !=null && cursor.moveToFirst()){
            do{
                dd.add(cursor.getString(0)+"\n " + "From :  " +cursor.getString(2)+"\n "+"price : " + cursor.getString(1)+" \n "+"Notes : " + cursor.getString(3));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return dd ;
    }
    /*public String getDbG(String r_c ){
        String dd = new String();
        String KW = r_c.toString();
        String selection = "Gdate " + "like ? ";

        Cursor cursor = db.query("infoG", null,selection,new String[]{"%"+KW+"%"},null,null,null);
        if(cursor !=null && cursor.moveToFirst()){
            do{
                dd =(cursor.getString(0)+"\n " + "From :  " +cursor.getString(2)+"\n "+"price : " + cursor.getString(1)+" \n "+"Nots : " + cursor.getString(3));
            }
            while (cursor.moveToNext());
        }
        return dd ;
    }*/
    public String getDbG(String r_c ){
        String dd = new String();
        String KW = r_c.toString();
        String selection = "Gdate " + "like ? ";

        Cursor cursor = db.query("infoG", null,selection,new String[]{"%"+KW+"%"},null,null,null);
        if(cursor !=null && cursor.moveToFirst()){
            do{
                dd =("Notes : " + cursor.getString(3));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return dd ;
    }

    public void delG(String r_c){
        db.delete("infoG", "Gdate = ?", new String[]{r_c + ""});


    }
    public void del2G(String r_c){
        db.delete("infoG", "Gname = ?", new String[]{r_c + ""});


    }

    public void restdata(){
        db.delete("info",null,null);
        db.delete("infoG",null,null);
    }

    //----------------------------------------------------
    // total
    public long totalSumT(){
        Cursor cursor = db.query("info", null,null,null,null,null,null);
        if(cursor !=null && cursor.moveToFirst()){
            do{
                DDT = Long.parseLong(cursor.getString(1).toString());
                TT = TT + DDT;
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return TT ;
    }

    public long totalSumG(){

        Cursor cursor = db.query("infoG", null,null,null,null,null,null);
        if(cursor !=null && cursor.moveToFirst()){
            do{
                DDG = Long.parseLong(cursor.getString(1).toString());
                TG = TG + DDG;
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return TG ;
    }

    public ArrayList<String> names(){
        ArrayList<String> dd = new ArrayList<>();
        Cursor cursor = db.query("infoG", null,null,null,null,null,null);
        if(cursor !=null && cursor.moveToFirst()){
            do{
                dd.add(cursor.getString(2));
            }
            while (cursor.moveToNext());
        }
        cursor = db.query("info", null,null,null,null,null,null);
        if(cursor !=null && cursor.moveToFirst()){
            do{
                dd.add(cursor.getString(2));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return dd ;
    }




    private static class MyDataHelp extends SQLiteOpenHelper {
        public final static String TABLE_INF = "info";
        public final static String TABLE_INFG = "infoG";
        public final static String COL_TNUM = "Tnum";
        public final static String COL_TFROM = "Tname";
        public final static String COL_TNOTS = "Tnots";
        public final static String COL_TDATE = "Tdate";
        public final static String COL_GNUM = "Gnum";
        public final static String COL_GTO = "Gname";
        public final static String COL_GNOTS = "Gnots";
        public final static String COL_GDATE = "Gdate";



        public MyDataHelp(Context context , String name , SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db)throws SQLException{
            try {
                db.execSQL("create table "+TABLE_INF+"("+COL_TDATE+" varchar(50) ,"+COL_TNUM+" integer, "+COL_TFROM+" varchar(50) , "+COL_TNOTS+" varchar(500)  ); ");
                //-------------------------------------
                db.execSQL("create table "+TABLE_INFG+"("+COL_GDATE+" varchar(50) ,"+COL_GNUM+" integer, "+COL_GTO+" varchar(50) , "+COL_GNOTS+" varchar(500)  ); ");

                //varchar(50) UNIQUE
            }catch(SQLException e)
            {
                e.printStackTrace();
            }

        }



        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXIST info;");
            db.execSQL("DROP TABLE IF EXIST infoG;");
            onCreate(db);
        }


    }
}