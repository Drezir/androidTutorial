package app.android.adam.androidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.ArrayList;
import java.util.List;

import app.android.adam.androidapp.shopitems.ShopItem;

public class Repository {

    private final SQLiteDatabase sqLiteDatabase;

    public Repository(Context context) {
        ProductsRepository productsRepository = new ProductsRepository(context);
        sqLiteDatabase = productsRepository.getWritableDatabase();
    }

    public void insertData(ShopItem shopItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductsRepository.COLUMN_NAME, shopItem.getName());
        contentValues.put(ProductsRepository.COLUMN_BOUGHT, shopItem.isBought() ? 1 : 0);
        sqLiteDatabase.insert(ProductsRepository.TABLE_NAME, null, contentValues);
    }
    public void update(ShopItem shopItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductsRepository.COLUMN_NAME, shopItem.getName());
        contentValues.put(ProductsRepository.COLUMN_BOUGHT, shopItem.isBought() ? 1 : 0);
        sqLiteDatabase.update(
                ProductsRepository.TABLE_NAME,
                contentValues,
                String.format("%s = ?", ProductsRepository.COLUMN_ID),
                new String[]{Long.toString(shopItem.getId())}
        );
    }
    public void delete(ShopItem shopItem) {
        sqLiteDatabase.delete(
                ProductsRepository.TABLE_NAME,
                String.format("%s = ?", ProductsRepository.COLUMN_ID),
                new String[]{Long.toString(shopItem.getId())}
        );
    }
    public List<ShopItem> getAllItems() {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(ProductsRepository.TABLE_NAME);

        Cursor cursor = sqLiteQueryBuilder.query(
                sqLiteDatabase,
                new String[] {ProductsRepository.COLUMN_ID, ProductsRepository.COLUMN_NAME, ProductsRepository.COLUMN_BOUGHT},
                null,
                null,
                null,
                null,
                ProductsRepository.COLUMN_NAME
                );

        List<ShopItem> shopItems = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(ProductsRepository.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(ProductsRepository.COLUMN_NAME));
                boolean bought = cursor.getInt(cursor.getColumnIndex(ProductsRepository.COLUMN_BOUGHT)) == 1;
                ShopItem shopItem = new ShopItem(name, bought);
                shopItem.setId(id);
                shopItems.add(shopItem);
            }
        }
        return shopItems;
    }

    private class ProductsRepository extends SQLiteOpenHelper {

        private static final String TABLE_NAME = "products";
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_NAME = "name";
        private static final String COLUMN_BOUGHT = "bought";
        private static final String DB_NAME = "Database";
        private final String CREATE_DB = String.format(
                "create table if not exists %s (" +
                "%s integer primary key autoincrement, " +
                "%s text unique," +
                "%s integer default 0 )", TABLE_NAME, COLUMN_ID, COLUMN_NAME, COLUMN_BOUGHT);

        private ProductsRepository(Context context) {
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists products");
            onCreate(db);
        }

    }
}
