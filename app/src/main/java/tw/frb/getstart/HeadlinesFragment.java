package tw.frb.getstart;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HeadlinesFragment extends ListFragment {

    private String[] list = {"Article1", "Article2"};
    private ArrayAdapter<String> listAdapter;

    OnHeadlineSelectedListener mCallback;

    FeedReaderContract.FeedReaderDbHelper mDbHelper;

    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }

    public HeadlinesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);

        setListAdapter(listAdapter);

        mDbHelper = new FeedReaderContract.FeedReaderDbHelper(getActivity());

        return inflater.inflate(R.layout.fragment_headline, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnHeadlineSelectedListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

//        insertToDatabase();
//        selectFromDatabase();
//        deleteFromDatabase();
//        updateDatabase();

//        viewWebPage();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_position), position);
        editor.commit();

        mCallback.onArticleSelected(position);
    }


    private void insertToDatabase() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID, 1);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "title");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "subtitle");

        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, "null", values);
        Log.d("SQLite", "newRowId: " + String.valueOf(newRowId));
    }

    private void selectFromDatabase() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
            FeedReaderContract.FeedEntry._ID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE
        };

        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME, // The table to query
                projection, // The columns to return
                null,       // The columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                null        // The sort order
        );

        c.moveToFirst();
        long itemId = c.getLong(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
        String itemTitle = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
        Log.d("SQLite", "itemId: " + String.valueOf(itemId) + " itemTitle: " + itemTitle);
    }

    private void deleteFromDatabase() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(1) };

        int count = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        Log.d("SQLite", "delete: " + String.valueOf(count));
    }

    private void updateDatabase() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "updated title");

        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(1) };

        int count = db.update(
            FeedReaderContract.FeedEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        );

        Log.d("SQLite", "update: " + String.valueOf(count));
    }

    private void viewWebPage() {
        Uri webpage = Uri.parse("http://www.google.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

        PackageManager packageManager = getActivity().getPackageManager();
        List activities = packageManager.queryIntentActivities(webIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
//            startActivity(webIntent);
            showChooser(webIntent);
        }
    }

    private void showChooser(Intent intent) {
        Intent chooser = Intent.createChooser(intent, "View Web Page with");

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
}
