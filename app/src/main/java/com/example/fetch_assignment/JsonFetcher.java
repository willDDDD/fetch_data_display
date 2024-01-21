package com.example.fetch_assignment;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JsonFetcher {

    //asynchronous operation to fetch data
    public static void fetchData(final DataFetchedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    List<Item> items = parseJsonData(result.toString());
                    listener.onDataFetched(items);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //Parses the JSON string
    private static List<Item> parseJsonData(String jsonData) {
        List<Item> items = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.optString("name");
                if (name != null && !name.isEmpty()) {
                    int listId = jsonObject.getInt("listId");
                    int id = jsonObject.getInt("id");
                    items.add(new Item(listId, id, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                int listIdCompare = Integer.compare(o1.getListId(), o2.getListId());
                if (listIdCompare == 0) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
                return listIdCompare;
            }
        });
        return items;
    }

    public interface DataFetchedListener {
        void onDataFetched(List<Item> items);
    }
}
