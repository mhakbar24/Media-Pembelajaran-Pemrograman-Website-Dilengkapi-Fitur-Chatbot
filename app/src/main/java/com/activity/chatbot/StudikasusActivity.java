package com.activity.chatbot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class StudikasusActivity extends AppCompatActivity {
 EditText name, kelas, answer;
 Button kirim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studikasus);
        name = findViewById(R.id.name);
        kelas = findViewById(R.id.kelas);
        answer = findViewById(R.id.answer);
        kirim = findViewById(R.id.kirim);

        WebView webView =findViewById(R.id.video);
        String link = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/T96JxYveqI8?si=G9sUYHztK4xrQQnN\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView.loadData(link, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToSheet();
            }
        });



    }

    private void addItemToSheet() {
        final ProgressDialog dialog = ProgressDialog.show(StudikasusActivity.this,"Adding Item","Pleass Wait..");
        final String Name = name.getText().toString().trim();
        final String Kelas = kelas.getText().toString().trim();
        final String Answer = answer.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwvFHVdniqgAiKhiUE6Om9tq8iAk2UwgOyYqBzaj0lbbm2dmAqQwuLwE7YEawBY7G1jyA/exec", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(StudikasusActivity.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> parmas = new HashMap<>();
                parmas.put("action", "create");
                parmas.put("name", Name);
                parmas.put("kelas", Kelas);
                parmas.put("answer", Answer);
                return parmas;
            }
        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(StudikasusActivity.this);
        queue.add(stringRequest);


    }
}