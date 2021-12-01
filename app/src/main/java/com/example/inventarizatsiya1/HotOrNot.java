package com.example.inventarizatsiya1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class HotOrNot {


    public static final String KEY_ROWID="_id";
    public static final String KEY_EtScanner1="EtScanner1";
    public static final String KEY_EtDetalNomeri="EtDetalNomeri";
    public static final String KEY_EtDetalSoni="EtDetalSoni";
    public static final String KEY_EtAdres="EtAdres";
    public static final String KEY_EtEO="EtEO";
    public static final String KEY_EtIzox="EtIzox";
    public static final String KEY_SCAN_DATA="scan_data";
    public static final String KEY_SCAN_TIME="scan_time";
    public static final String DATABASE_NAME="Inventarizatsiya";
    public static final String DATABASE_TABLE="inventarlar";
    public static final int DATABASE_VERSION=4;

    private DbHelper ourHelper;
    private Context ourContext;
    private SQLiteDatabase ourDatabase;

    public long createEntry(String  EtScanner1,String  EtDetalNomeri,String  EtDetalSoni,String EtAdres,String EtEO,String EtIzox,String scan_data,String scan_time) {
        ContentValues cv=new ContentValues();
        cv.put(KEY_EtScanner1,EtScanner1);
        cv.put(KEY_EtDetalNomeri,EtDetalNomeri);
        cv.put(KEY_EtDetalSoni,EtDetalSoni);
        cv.put(KEY_EtAdres,EtAdres);
        cv.put(KEY_EtEO,EtEO);
        cv.put(KEY_EtIzox,EtIzox);
        cv.put(KEY_SCAN_DATA,scan_data);
        cv.put(KEY_SCAN_TIME,scan_time);
        return ourDatabase.insert(DATABASE_TABLE,null,cv);
    }


    public boolean getForExcel(String EexcelFileName  ) {

        String[] columns=new String[]{KEY_ROWID,KEY_EtScanner1,KEY_EtDetalNomeri,KEY_EtDetalSoni,KEY_EtAdres,KEY_EtEO,KEY_EtIzox,KEY_SCAN_DATA,KEY_SCAN_TIME};
        Cursor c=ourDatabase.query(DATABASE_TABLE,columns,null,null,null,null,null);

//////////////////////////////////////////////////////
        File sd = Environment.getExternalStorageDirectory();
        String csvFile = EexcelFileName;

        File directory = new File(sd.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        try {


            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("userList", 0);
            // column and row
            sheet.addCell(new Label(0, 0, "EtScanner1"));
            sheet.addCell(new Label(1, 0, "EtDetalNomeri"));
            sheet.addCell(new Label(2, 0, "EtDetalSoni"));
            sheet.addCell(new Label(3, 0, "Adres"));
            sheet.addCell(new Label(4, 0, "EO"));
            sheet.addCell(new Label(5, 0, "EtIzox"));
            sheet.addCell(new Label(6, 0, "Scan_data"));
            sheet.addCell(new Label(7, 0, "Scan_time"));


            if (c.moveToFirst()) {


                do {
                    String EtScanner1 = c.getString(1);
                    String EtDetalNomeri = c.getString(2);
                    String EtDetalSoni = c.getString(3);
                    String EtAdres = c.getString(4);
                    String EtEO = c.getString(5);
                    String EtIzox = c.getString(6);
                    String scan_data = c.getString(7);
                    String scan_time = c.getString(8);
                    int i = c.getPosition() + 1;
                    sheet.addCell(new Label(0, i, EtScanner1));
                    sheet.addCell(new Label(1, i, EtDetalNomeri));
                    sheet.addCell(new Label(2, i, EtDetalSoni));
                    sheet.addCell(new Label(3, i, EtAdres));
                    sheet.addCell(new Label(4, i, EtEO));
                    sheet.addCell(new Label(5, i, EtIzox));
                    sheet.addCell(new Label(6, i, scan_data));
                    sheet.addCell(new Label(7, i, scan_time));

                } while (c.moveToNext());
            }

            c.close();
            workbook.write();
            workbook.close();
            Toast.makeText(ourContext.getApplicationContext(),
                    "Data Exported in a Excel Sheet ("+csvFile.toString()+") ", Toast.LENGTH_SHORT).show();
            return true;

        } catch(IOException e){
            e.printStackTrace();
            return false;
        } catch (WriteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList getDataForListView() {

        String[] columns=new String[]{KEY_ROWID,KEY_EtDetalNomeri,KEY_EtDetalSoni,KEY_EtAdres,KEY_EtEO,KEY_EtIzox,KEY_SCAN_DATA,KEY_SCAN_TIME};
        Cursor c=ourDatabase.query(DATABASE_TABLE,columns,null,null,null,null,KEY_ROWID);

        String result="";

        int iRow=c.getColumnIndex(KEY_ROWID);
        int iEtDetalNomeri=c.getColumnIndex(KEY_EtDetalNomeri);
        int iEtDetalSoni=c.getColumnIndex(KEY_EtDetalSoni);
        int iEtEtAdres=c.getColumnIndex(KEY_EtAdres);
        int iEtEO=c.getColumnIndex(KEY_EtEO);
        int iEtIzox=c.getColumnIndex(KEY_EtIzox);
        int iScan_Data=c.getColumnIndex(KEY_SCAN_DATA);
        int iScan_Time=c.getColumnIndex(KEY_SCAN_TIME);

        ArrayList<String> malumot=new ArrayList<>();
        for(c.moveToLast();!c.isBeforeFirst();c.moveToPrevious())
        {
            //result="ID: "+c.getString(iRow)+":\n Detal Nomeri: "+c.getString(iEtDetalNomeri)+"\n Detal Soni: "+c.getString(iEtDetalSoni)+"\n Datal soni jami: "+c.getString(iEtDetalSoniJami)+"\n Adres: "+c.getString(iEtEtAdres)+"\n EO: "+c.getString(iEtEO)+"\n Vaqti: "+c.getString(iScan_Data)+" "+c.getString(iScan_Time);
            result=c.getString(iRow)+": "+c.getString(iEtDetalNomeri)+": "+c.getString(iEtDetalSoni)+": "+c.getString(iEtEtAdres)+": "+c.getString(iEtEO)+": "+c.getString(iEtIzox)+": "+c.getString(iScan_Data)+" "+c.getString(iScan_Time);
            malumot.add(result);
        }
        c.close();
        return malumot;
    }

    public String getData() {

        String[] columns=new String[]{KEY_ROWID,KEY_EtScanner1,KEY_EtDetalNomeri,KEY_SCAN_DATA,KEY_SCAN_TIME};
        Cursor c=ourDatabase.query(DATABASE_TABLE,columns,null,null,null,null,KEY_ROWID);

        String result="";

        int iRow=c.getColumnIndex(KEY_ROWID);
        int i_EtScanner1=c.getColumnIndex(KEY_EtScanner1);
        int iEtDetalNomeri=c.getColumnIndex(KEY_EtDetalNomeri);
        int iScan_Data=c.getColumnIndex(KEY_SCAN_DATA);
        int iScan_Time=c.getColumnIndex(KEY_SCAN_TIME);

        for(c.moveToLast();!c.isBeforeFirst();c.moveToPrevious())
        {
            result=result+c.getString(iRow)+" "+c.getString(i_EtScanner1)+" "+c.getString(iEtDetalNomeri)+" "+c.getString(iScan_Data)+" "+c.getString(iScan_Time)+"\n";
        }
        return result;
    }

    public String getName(long l) {
        String[] columns=new String[]{KEY_ROWID,KEY_EtScanner1,KEY_EtDetalNomeri,KEY_SCAN_DATA,KEY_SCAN_TIME};
        Cursor c=ourDatabase.query(DATABASE_TABLE,columns,KEY_ROWID+"="+l,null,null,null,null);

        if(c!=null)
        {
            c.moveToFirst();
            String name=c.getString(1);
            return name;
        }
        return null;
    }

    public String getHotness() {
        String[] columns=new String[]{KEY_ROWID,KEY_EtScanner1,KEY_EtDetalNomeri,KEY_SCAN_DATA,KEY_SCAN_TIME};
        Cursor c=ourDatabase.query(DATABASE_TABLE,columns,KEY_ROWID+">0",null,null,null,null);

        if(c!=null)
        {
            c.moveToLast();
            String hotness=c.getString(2);
            return hotness;
        }
        return "Yangi Kriting";
    }

    public void updateEntry(long lRow, String mEtScanner1, String mEtDetalNomeri, String mScan_Data, String mScan_Time) {
        ContentValues cvUpdate=new ContentValues();
        cvUpdate.put(KEY_EtScanner1,mEtScanner1);
        cvUpdate.put(KEY_EtDetalNomeri,mEtDetalNomeri);
        cvUpdate.put(KEY_SCAN_DATA,mScan_Data);
        cvUpdate.put(KEY_SCAN_DATA,mScan_Time);
        ourDatabase.update(DATABASE_TABLE,cvUpdate,KEY_ROWID+"="+lRow,null);
    }

    public void deleteItemID(String deleteItemID) {
        ourDatabase.delete(DATABASE_TABLE,KEY_ROWID+"="+deleteItemID,null);
    }

    public void deleteEntry() {
        ourDatabase.delete(DATABASE_TABLE,KEY_ROWID+">0",null);
    }

//    public void createEntry(String etScanner1, String etDetalNomeri, String etDetalSoni, String etAdres, String etEO, String etIzox, String scan_data, String scan_time)
//    {
//    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context contex)
        {
            super(contex,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " +DATABASE_TABLE + " ("+
                    KEY_ROWID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_EtScanner1+" TEXT NOT NULL, " +
                    KEY_EtDetalNomeri+" TEXT, " +
                    KEY_EtDetalSoni+" TEXT, " +
                    KEY_EtAdres+" TEXT, " +
                    KEY_EtEO+" TEXT, " +
                    KEY_EtIzox+" TEXT, " +
                    KEY_SCAN_DATA+" TEXT NOT NULL, " +
                    KEY_SCAN_TIME+ " TEXT NOT NULL);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE);
            onCreate(db);
        }
    }

    public HotOrNot(Context c)
    {
        ourContext=c;
    }

    public HotOrNot open() throws SQLException
    {
        ourHelper=new DbHelper(ourContext);
        ourDatabase=ourHelper.getWritableDatabase();

        return this;
    }
    public void close()
    {
        ourHelper.close();
    }
}
