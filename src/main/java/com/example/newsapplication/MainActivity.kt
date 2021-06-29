package com.example.newsapplication

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException;
import org.json.JSONObject;


class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewListAdapter(this)
        recyclerView.adapter = mAdapter
    }
    private fun fetchData() {
        val url = "https://newsapi.org/v2/everything?q=tesla&from=2021-05-29&sortBy=publishedAt&apiKey=0c418f17711d41c688c29b23c45de73f/0c418f17711d41c688c29b23c45de73f"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
               { response ->
                    val newsJsonArray =JSONArray("articles")
                    val newsArray = ArrayList<News>()
                    for (i in 0 until newsJsonArray.length())
                    {
                        val newsJSONObject = newsJsonArray.getJSONObject(i)
                        val news = News(
                                newsJSONObject.getString("title"),
                                newsJSONObject.getString("author"),
                                newsJSONObject.getString("url"),
                                newsJSONObject.getString("urlToImage")
                        )
                        newsArray.add(news)
                    }
                    mAdapter.updateNews(newsArray)
                }
            ,{
                Toast.makeText(this, "Error",Toast.LENGTH_SHORT ).show()
            })

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {

        val builder = CustomTabsIntent.Builder();
        val customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }
}