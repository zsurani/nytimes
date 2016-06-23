package com.example.zsurani.nytsearch1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.zsurani.nytsearch1.Article;
import com.example.zsurani.nytsearch1.ArticleArrayAdapter;
import com.example.zsurani.nytsearch1.EndlessScrollListener;
import com.example.zsurani.nytsearch1.FilterActivity;
import com.example.zsurani.nytsearch1.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    //EditText etQuery;
    GridView gvResults;
    //Button btnSearch;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    String date;
    String order;
    String n_values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
        setupFiltersListener();

        GridView lvItems = (GridView) findViewById(R.id.gvResults);
        // Attach the listener to the AdapterView onCreate
        lvItems.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    public void customLoadMoreDataFromApi(int offset) {

        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        // gv.addAll(offset);
        adapter.notifyDataSetChanged();
    }

    public void setupViews() {
        //etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
//        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(position);
                i.putExtra("article", article);
                startActivity(i);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(String query) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key", "447a938cd4b5488fa13bc371599396ee");
        params.put("page", 0);

        //Toast.makeText(this, date, Toast.LENGTH_SHORT).show();

        if (date == null)
        {
        } else if (date.length() > 4) {
            params.put("begin_date", date);
        }

        if (order == null)
        {
        } else if (order.equals("none")) {
        } else if (date.length() > 4) {
            params.put("sort", order);
        }

        if (n_values == null)
        {
        } else if (n_values.length() > 4) {
            params.put("news_desk", n_values);
        }

        Toast.makeText(this, n_values, Toast.LENGTH_SHORT).show();

        params.put("q", query);

        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try{
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    private final int REQUEST_CODE = 20;

    private void setupFiltersListener() {
        Button button = (Button) findViewById(R.id.btnSearch);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SearchActivity.this, FilterActivity.class);
                startActivityForResult(i, REQUEST_CODE); // brings up the second activity

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            date = data.getExtras().getString("date");

            order = data.getExtras().getString("ordering");

            n_values = data.getExtras().getString("checkboxes");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // added because of search view

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                onArticleSearch(query);

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

        //getMenuInflater().inflate(R.menu.menu_search, menu);
        //return true;
    }
}
