package prob.procon.dmcc2016.myapplication;

/**
 * Created by Maeda Hiromu on 2016/09/29.
 */

public class DatabaseManager {

    public static String TABLE_NAME = "sql_makers";
    public static String FIELD_ID = "_id";
    public static String FIELD_MOUNT_NAME = "mount_name";
    public static String FIELD_DATE = "date";
    public static String FIELD_INFO_TYPE = "info_type";
    public static String FIELD_USER_ID = "user_id";
    public static String FIELD_COMMENT = "comment";
    public static String FIELD_GRAPH = "graph";

    static final String DB_NAME = "sqlite_maker";
    static final int DB_VERSION = 1;

    public static String CREATE_TABLE_SQL = "CREATE TABLE "+TABLE_NAME+" ( "+FIELD_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FIELD_MOUNT_NAME+" TEXT NOT NULL, "
            +FIELD_DATE+" TEXT NOT NULL, "+FIELD_INFO_TYPE+" INTEGER, "+FIELD_USER_ID+" TEXT NOT NULL, "+FIELD_COMMENT+" TEXT, "
            +FIELD_GRAPH+" TEXT);";
}
