package com.chrono.speedtimeapp.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.chrono.speedtimeapp.R;

public class WebActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        webView = findViewById(R.id.webview);
        initializeWebView();

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            webView.loadUrl("http://192.168.1.145:3000/");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    private void initializeWebView() {
        WebView.setWebContentsDebuggingEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webView.setScrollContainer(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("web1", "web1: " + url);

                // Verificar si la URL contiene la dirección específica
                if (url.contains("http://192.168.1.145:3000/chrono")) {
                    Log.d("web", url);
                    // Obtener los valores de userId y trackId de la URL
                    Uri uri = Uri.parse(url);
                    String userId = uri.getQueryParameter("userId");
                    String trackId = uri.getQueryParameter("trackId");

                    // Abrir la actividad "CronoActivity" con los parámetros
                    Intent intent = new Intent(WebActivity.this, ChronoActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("trackId", trackId);
                    startActivity(intent);
                }
                Log.d("web2", "web2: " + url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("web", "Error al cargar la página: " + error);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // No hacer nada aquí para mantener el estado de la actividad
    }
}




