package sg.edu.np.mad.madpractical;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DbHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserDatabase";
    private static final String TABLE_USER = "User";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_FOLLOWED = "followed";

    private static final List<String> NAMES = Arrays.asList(
            "John",
            "Emma",
            "Michael",
            "Sophia",
            "William",
            "Olivia",
            "James",
            "Ava",
            "Benjamin",
            "Isabella"
    );

    private static final List<String> DESCRIPTIONS = Arrays.asList(
            "Lorem ipsum dolor sit amet",
            "Consectetur adipiscing elit",
            "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            "Ut enim ad minim veniam",
            "Quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat"
    );

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_FOLLOWED + " INTEGER" + ")";
        db.execSQL(CREATE_USER_TABLE);

        // Predefined list of names and descriptions
        String[] names = {
                "John", "Emily", "Michael", "Sophia", "Daniel", "Emma", "William", "Olivia", "James", "Ava",
                "Benjamin", "Isabella", "Jacob", "Mia", "Ethan", "Charlotte", "Alexander", "Amelia", "Henry", "Harper"
        };

        String[] descriptions = {
                "Lorem ipsum dolor sit amet.", "Consectetur adipiscing elit.", "Sed do eiusmod tempor incididunt.",
                "Ut labore et dolore magna aliqua.", "Ut enim ad minim veniam.", "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        };

        for (int i = 0; i < Math.min(names.length, descriptions.length); i++) {
            String name = names[i];
            String description = descriptions[i];
            boolean followed = new Random().nextBoolean();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME, name);
            values.put(KEY_DESCRIPTION, description);
            values.put(KEY_FOLLOWED, followed ? 1 : 0);

            db.insert(TABLE_USER, null, values);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    private String getRandomName() {
        Random random = new Random();
        int index = random.nextInt(NAMES.size());
        return NAMES.get(index);
    }

    private String getRandomDescription() {
        Random random = new Random();
        int index = random.nextInt(DESCRIPTIONS.size());
        return DESCRIPTIONS.get(index);
    }

    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int descriptionIndex = cursor.getColumnIndex(KEY_DESCRIPTION);
            int followedIndex = cursor.getColumnIndex(KEY_FOLLOWED);

            do {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String description = cursor.getString(descriptionIndex);
                int followed = cursor.getInt(followedIndex);

                User user = new User(id, name, description, followed == 1);
                userList.add(user);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return userList;
    }
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOLLOWED, user.isFollowed() ? 1 : 0);

        db.update(TABLE_USER, values, KEY_ID + " = ?", new String[]{String.valueOf(user.getId())});

        db.close();
    }

}
