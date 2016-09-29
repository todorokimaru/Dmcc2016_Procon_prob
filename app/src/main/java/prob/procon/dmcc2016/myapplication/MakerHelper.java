package prob.procon.dmcc2016.myapplication;

/**
 * Created by Maeda Hiromu on 2016/09/26.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MakerHelper extends SQLiteOpenHelper{

    private static MakerHelper sSingleton = null;

    public static synchronized MakerHelper getInstance(Context context) {
        if (sSingleton == null) {
            sSingleton = new MakerHelper(context);
        }
        return sSingleton;
    }

    private MakerHelper(Context context) {
        super(context, DatabaseManager.DB_NAME, null, DatabaseManager.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
