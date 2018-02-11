package com.example.mcb.genesisapp.Repository.SQLite;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by mcb on 11.02.18.
 */

public class BasicSQLContracts {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public BasicSQLContracts() {}

    private static final String TEXT_TYPE = " TEXT ";
    // private static final String IMAGE_TYPE = " BLOB ";
    private static final String COMMA_SEP = ",";
    private static final String SEMICOLON_SEP=";";
    private static final String NOT_NULL = " NOT NULL ";
    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";
    private static final String DROP_INDEX_IF_EXISTS = "DROP INDEX IF EXISTS ";
    private static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";
    private static final String INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY ";
    private static final String INTEGER = " INTEGER ";
    private static final String DEFAULT_CURRENT_TIME = " DEFAULT CURRENT_TIMESTAMP ";

    public static void createTables(SQLiteDatabase db) {
        //   db.execSQL(PrimitiveAttributes.SQL_CREATE);
        db.execSQL(BasicTokenTable.SQL_CREATE);

        // Create Indices
        db.execSQL(BasicTokenTable.CREATE_INDEX_NAME_COLUMN);
    }

    public static void dropTables(SQLiteDatabase db) {

        //Drop Indices
        db.execSQL(DROP_INDEX_IF_EXISTS + BasicTokenTable.INDEX_NAME_NAME_COLUMN + SEMICOLON_SEP);
        //DROP Tables
        db.execSQL(DROP_TABLE_IF_EXISTS + BasicTokenTable.TABLE_NAME + SEMICOLON_SEP);
    }

    public static abstract class BasicTokenTable implements BaseColumns {
        public static final String TABLE_NAME = "BasicTokenTable";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_SYMBOL="Symbol";
        public static final String COLUMN_NAME_DECIMALS = "Decimals";
        public static final String COLUMN_NAME_GENERAL_SUPPLY ="GeneralSupply";
        public static final String COLUMN_NAME_GENESIS_SUPPLY= "GenesisSupply";
        public static final String COLUMN_NAME_CREATION_DATE = "CreationDate";

        public static final String INDEX_NAME_NAME_COLUMN = "nameIndex";


        public static final int idColumn =0;
        public static final int nameColumng=1;
        public static final int symbolColumn=2;
        public static final int decimalsColumn=3;
        public static final int totalSupplyColumn=4;
        public static final int genesisSupplyColumn =5;
        public static final int creationDateColumn =6;

        // create statement
        public static final String SQL_CREATE =
                CREATE_TABLE_IF_NOT_EXISTS + TABLE_NAME + " (" +
                        _ID + INTEGER_PRIMARY_KEY + NOT_NULL + COMMA_SEP +
                        COLUMN_NAME_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                        COLUMN_NAME_SYMBOL + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                        COLUMN_NAME_DECIMALS + INTEGER + NOT_NULL + COMMA_SEP +
                        COLUMN_NAME_GENERAL_SUPPLY + INTEGER + NOT_NULL + COMMA_SEP +
                        COLUMN_NAME_GENESIS_SUPPLY + INTEGER + NOT_NULL + COMMA_SEP +
                        COLUMN_NAME_CREATION_DATE + TEXT_TYPE + DEFAULT_CURRENT_TIME  +
                        " );";
        // all column names - handy for querries
        public static String[] allColumns = {_ID, COLUMN_NAME_NAME, COLUMN_NAME_SYMBOL,
                COLUMN_NAME_DECIMALS, COLUMN_NAME_GENERAL_SUPPLY,
                COLUMN_NAME_GENESIS_SUPPLY,  COLUMN_NAME_CREATION_DATE};

        /*
         * Create and destroy index
         */
        public static String CREATE_INDEX_NAME_COLUMN = " CREATE INDEX IF NOT EXISTS "+ INDEX_NAME_NAME_COLUMN +" ON "
                + TABLE_NAME + " ("+COLUMN_NAME_NAME+" ASC )";
        public static String DROP_INDEX_NAME_COLUMN = "DROP INDEX "+ INDEX_NAME_NAME_COLUMN;



    }



}
