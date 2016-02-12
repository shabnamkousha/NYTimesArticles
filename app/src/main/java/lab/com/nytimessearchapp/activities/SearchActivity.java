package lab.com.nytimessearchapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.AsynchronousCloseException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import lab.com.nytimessearchapp.Article;
import lab.com.nytimessearchapp.ArticleArrayAdapter;
import lab.com.nytimessearchapp.R;

public class SearchActivity extends AppCompatActivity {
    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    ArrayList<Article> articls;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupViews();
    }

    public void setupViews(){
        etQuery= (EditText) findViewById(R.id.etQuery);
        gvResults= (GridView) findViewById(R.id.gvResults);
        btnSearch=(Button) findViewById(R.id.btnSearch);
        articls= new ArrayList<>();
        adapter= new ArticleArrayAdapter(this, articls);
        gvResults.setAdapter(adapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),ArticleActivity.class);
                Article article=articls.get(position);
                i.putExtra("url", article.getWebUrl());
                startActivity(i)  ;
            }
        });
    }

    public void onArticleSearch(View view) {
        String query=etQuery.getText().toString();
        //Toast.makeText(this, "Result:" + query, Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();

        String url="http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params=new RequestParams();
        params.put("api-key","44df8b918e4cde4c7dcfbd6473346627:7:74373173");
        params.put("page","0");
        params.put("q",query);

        client.get(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray articleJsonResults=null;
                try {
                    articleJsonResults=response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJsonArray(articleJsonResults));

                    Log.d("DEBUG", articls.toString());
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
