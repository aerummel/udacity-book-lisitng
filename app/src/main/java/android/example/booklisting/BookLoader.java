package android.example.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class BookLoader extends AsyncTaskLoader {
    /** Tag for log messages */
    private static final String LOG_TAG = BookLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /** search term */
    private String mSearch;

    /**
     * Constructs a new {@link BookLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public BookLoader(Context context, String url, String search) {
        super(context);
        mUrl = url;
        mSearch = search;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v(LOG_TAG, "BookLoader called onStartLoading.");
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Book> loadInBackground() {

        Log.v(LOG_TAG, "Background thread is running for: " + mSearch);

        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Book> books = QueryUtils.fetchBookData(mUrl, mSearch);
        return books;

    }
}
