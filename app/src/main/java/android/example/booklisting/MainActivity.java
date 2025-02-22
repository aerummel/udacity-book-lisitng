package android.example.booklisting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    /** URL for Google Book search query */
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?maxResults=20";

    /** final query URL */
    private String mSearchURL;

    /** search term */
    private String mSearch;

    /** Adapter for the list of Google Books */
    private BookAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /** Loading Spinner */
    private View mLoadingSpinner;

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);
        mEmptyStateTextView.setText(R.string.default_message);

        mLoadingSpinner = findViewById(R.id.loading_spinner);
        mLoadingSpinner.setVisibility(View.GONE);

        // Create a new {@link BookAdapter} of books
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView} so the list can be populated in the UI
        bookListView.setAdapter(mAdapter);
    }

    public void SearchButton(View view) {

        // hide the keyboard
        hideSoftKeyboard(this);

        // Set loading spinner to be visible and default message to gone
        mEmptyStateTextView.setVisibility(View.GONE);
        mLoadingSpinner.setVisibility(View.VISIBLE);

        // Find EditText view
        EditText searchTermView = (EditText) findViewById(R.id.search_bar);
        mSearch = searchTermView.getText().toString();
        Log.v(LOG_TAG, "Search term is: " +mSearch);
        mSearchURL = BASE_URL + "&q=" + mSearch;
        Log.v(LOG_TAG, "Search URL is: " + mSearchURL);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mLoadingSpinner.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_connection);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        // Create a new loader for the given URL
        return new BookLoader(this, mSearchURL, mSearch);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        Log.v(LOG_TAG, "onLoadFinished() was called.");

        // Set empty state text to display "No results found."
        mEmptyStateTextView.setText(R.string.no_results);

        // Set loading spinner visibility to GONE
        ProgressBar spinner = (ProgressBar) findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.GONE);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

        Log.v(LOG_TAG, "onLoaderReset() was called.");

        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}