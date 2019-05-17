package com.example.reposearch;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtil {
    private static final String GITHUB_BASE_URL = "https://api.github.com";
    private static final String SEARCH_REPOS = "/search/repositories?q=";

    public static URL generateURL(String request)
    {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL + SEARCH_REPOS + '"' + request + '"');

        URL requestURL = null;

        try {
            requestURL = new URL(builtUri.toString());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return requestURL;
    }

    public static String getRepositoriesFromURL(URL url) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String result = null;

        try
        {
            InputStream inStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inStream);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext())
                result = scanner.next();
        }
        finally
        {
            urlConnection.disconnect();
        }

        return result;
    };

}
