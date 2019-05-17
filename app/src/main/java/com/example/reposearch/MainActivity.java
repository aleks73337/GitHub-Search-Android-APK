package com.example.reposearch;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText searchField;

    class GitQueryTask extends AsyncTask<URL, Void, String> implements rvReposAdapter.OnNoteListner
    {
        private WeakReference<Context> context;
        ArrayList<repoPair<String, Intent>> repositories;

        GitQueryTask(Context mContext)
        {
            context = new WeakReference<>(mContext);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String repos = null;
            try {
                repos = NetworkUtil.getRepositoriesFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return repos;
        }

        @Override
        protected void onPostExecute(String reposStr)
        {
            JsonParser parser = new JsonParser();
            JsonArray repos = (JsonArray)parser.parse(reposStr).getAsJsonObject().get("items");
            repositories = new ArrayList<repoPair<String,Intent>>();

            for (JsonElement repo : repos)
            {
                String name = repo.getAsJsonObject().get("name").toString();
                String link = repo.getAsJsonObject().get("html_url").toString().trim();
                link = link.replace("\"", "");
                Intent intent = new Intent().setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(link).normalizeScheme();
                Log.d("DEBUG",uri.toString());
                intent.setData(uri);


                repositories.add(new repoPair<String, Intent>(name, intent));
            }

            RecyclerView rvRepos = (RecyclerView) findViewById(R.id.rvRepos);

            rvReposAdapter adapter = new rvReposAdapter(repositories, this);
            rvRepos.setAdapter(adapter);
            rvRepos.setLayoutManager(new LinearLayoutManager(context.get()));

        }


        @Override
        public void onNoteClick(int parent) {
            Intent intent = repositories.get(parent).getIntent();
            Log.d("INTENT:",intent.getDataString());
            try {
                startActivity(intent);
            }
            catch (android.content.ActivityNotFoundException e) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Can't open link",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.END, 0, 0);
                toast.show();
                }
            }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.searchButton);
        searchField = findViewById(R.id.searchField);

        View.OnClickListener searchButtonListner = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String request = searchField.getText().toString();
                if (!request.isEmpty())
                {
                    URL requestURL = NetworkUtil.generateURL(request);

                    if (requestURL == null)
                    {
                        return;
                    }
                    new GitQueryTask(getApplicationContext()).execute(requestURL);
                }
            };
        };
        searchButton.setOnClickListener(searchButtonListner);
    }
}
