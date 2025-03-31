package com.example.intelclassone;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_web_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


         webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript

        // Open links inside WebView instead of the browser
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String url = request.getUrl().toString();
                    if (url.contains("youtube.com") || url.contains("youtu.be")) {
                        // Prevent YouTube links from opening in a new page
                        return true; // Cancel the request
                    }
                }
                return false; // Allow WebView to load the page
            }

            @SuppressWarnings("deprecation") // For older versions
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("youtube.com") || url.contains("youtu.be")) {
                    return true; // Cancel YouTube link opening
                }
                return false;
            }
        });

        // Load a website
        webView.loadUrl("https://firozmohamed2.github.io/intelproject2/videogit.html"); // Change to your desired URL
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack(); // Go to previous page in WebView
        } else {
            // If no previous page, go to MainActivity
            Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish(); // Close current activity
        }
    }
}