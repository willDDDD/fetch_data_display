package com.example.fetch_assignment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements JsonFetcher.DataFetchedListener {

    private RecyclerView recyclerView;
    private ItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemsAdapter();
        recyclerView.setAdapter(adapter);

        JsonFetcher.fetchData(this);

        setupSearchView();
    }

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        Button btnShowAll = findViewById(R.id.btnShowAll);

        // Set a listener to perform search operation.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query);

                // Hide the keyboard after the search is performed
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // "Show All" button resets the filter.
        btnShowAll.setOnClickListener(v -> adapter.filterData(-1));
    }

    //Filters content based on the list ID
    private void filterData(String query) {
        try {
            int listId = Integer.parseInt(query);
            adapter.filterData(listId);
        } catch (NumberFormatException e) {
            adapter.filterData(-1);
        }
    }

    @Override
    //Callback method when data fetch is complete
    public void onDataFetched(List<Item> items) {
        runOnUiThread(() -> adapter.setItems(items));
    }
}
