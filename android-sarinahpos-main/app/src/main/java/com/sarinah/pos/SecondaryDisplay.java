package com.sarinah.pos;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;



import java.io.InputStream;
import java.net.URL;

public class SecondaryDisplay extends Presentation {

    public WebView myWebView;
    String domain = "http://stagging.sarinahportal.co.id/";
//    String domain = "http://sarinahportal.co.id:8070/";
    CookieSyncManager cookieSyncManager;
    CookieManager cookieManager;
    ImageView iV;

    public SecondaryDisplay(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondary_display);

        myWebView = (WebView)findViewById(R.id.myWebview2);
        iV = (ImageView) findViewById(R.id.imageView);
        String imgURL  = "http://stagging.sarinahportal.co.id/web/image/3632";
        new DownLoadImageTask(iV).execute(imgURL);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);


        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebContentsDebuggingEnabled(true);

        cookieSyncManager = CookieSyncManager.createInstance(this.getContext());
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
    }

    void showCustomerDisplay(String session){
        iV.setVisibility(View.GONE);
        cookieManager.setCookie(
                domain,
                "session_id = "+session+"  ; Domain=stagging.sarinahportal.co.id"
        );
        cookieSyncManager.sync();
        myWebView.loadUrl(domain + "web/customer_display");
    }
    void hideCustomerDisplay() {
        iV.bringToFront();
        iV.setVisibility(View.VISIBLE);
        cookieManager.removeSessionCookie();

    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}