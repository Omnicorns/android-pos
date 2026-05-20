package com.sarinah.pos;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Color;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class CustomerDisplayPresentation extends Presentation {

    private static final String TAG = "DUAL_DISPLAY";

    private WebView webView;
    private String currentUrl;
    private final String host;
    private final boolean devMode;

    public CustomerDisplayPresentation(Context context, Display display, String host, boolean devMode) {
        super(context, display);
        this.host = host;
        this.devMode = devMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout root = new FrameLayout(getContext());
        root.setBackgroundColor(Color.BLACK);
        setContentView(root);

        webView = new WebView(getContext());
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);

        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setSupportZoom(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setInitialScale(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            s.setMixedContentMode(devMode
                    ? WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                    : WebSettings.MIXED_CONTENT_NEVER_ALLOW);
            try {
                CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
            } catch (Throwable ignored) {}
        }

        final String injectFit =
                "(function(){"
                        + "try{"
                        + "  var existing=document.querySelector('meta[name=viewport]');"
                        + "  if(existing) existing.remove();"
                        + "  var m=document.createElement('meta');"
                        + "  m.name='viewport';"
                        + "  m.content='width=1280';"
                        + "  document.head.appendChild(m);"

                        + "  if(!document.getElementById('sarinah-fit-fix')){"
                        + "    var css=document.createElement('style');"
                        + "    css.id='sarinah-fit-fix';"
                        + "    css.textContent='"
                        + "      *{box-sizing:border-box!important;}"
                        + "      html,body{margin:0!important;padding:0!important;"
                        + "        width:100%!important;height:100%!important;"
                        + "        overflow-x:hidden!important;}"

                        // Rating container - pastikan visible & clickable
                        + "      [class*=rating],form[class*=rating],.o_website_rating,"
                        + "      .o_rating_popup,.o_rating_form,.o_portal_rating{"
                        + "        pointer-events:auto!important;"
                        + "        position:relative!important;"
                        + "        z-index:9998!important;"
                        + "        overflow:visible!important;"
                        + "        display:flex!important;"
                        + "        justify-content:center!important;"
                        + "        gap:8px!important;"
                        + "        padding:10px 20px!important;"
                        + "      }"

                        // Rating stars - besar, jelas, dan clickable
                        + "      .fa-star,.fa-star-o,.star-rating,.o_rating,"
                        + "      [class*=rating] .fa,[class*=rating] span,"
                        + "      [class*=rating] i,[class*=rating] label{"
                        + "        pointer-events:auto!important;"
                        + "        position:relative!important;"
                        + "        z-index:9999!important;"
                        + "        cursor:pointer!important;"
                        + "        min-width:40px!important;"
                        + "        min-height:40px!important;"
                        + "        font-size:32px!important;"
                        + "        line-height:40px!important;"
                        + "        text-align:center!important;"
                        + "        display:inline-block!important;"
                        + "        -webkit-tap-highlight-color:rgba(255,165,0,0.3)!important;"
                        + "        touch-action:manipulation!important;"
                        + "      }"

                        // Hapus semua overlay / pseudo element yang mungkin block click
                        + "      [class*=rating]::before,[class*=rating]::after,"
                        + "      [class*=rating] *::before,[class*=rating] *::after{"
                        + "        pointer-events:none!important;"
                        + "      }"

                        + "    ';"
                        + "    document.head.appendChild(css);"
                        + "  }"

                        // ===== FIT SCALING =====
                        // Gunakan transform:scale bukan body.style.zoom
                        // karena zoom di Android WebView bikin hit-test area geser
                        + "  var body=document.body;"
                        + "  var html=document.documentElement;"
                        + "  var contentH=Math.max(body.scrollHeight,html.scrollHeight);"
                        + "  var contentW=Math.max(body.scrollWidth,html.scrollWidth);"
                        + "  var viewH=window.innerHeight;"
                        + "  var viewW=window.innerWidth;"
                        + "  if(contentW>10 && contentH>10){"
                        + "    var scaleX=viewW/contentW;"
                        + "    var scaleY=viewH/contentH;"
                        + "    var scale=Math.min(scaleX,scaleY,1);"

                        // Reset zoom lama kalau ada
                        + "    body.style.zoom='';"

                        // Pakai transform scale - hit area tetap akurat
                        + "    body.style.transformOrigin='top left';"
                        + "    body.style.transform='scale('+scale+')';"

                        // Set width agar content tidak wrap ulang setelah scale
                        + "    body.style.width=(100/scale)+'%';"
                        + "    body.style.height=(100/scale)+'%';"
                        + "    body.style.overflow='hidden';"

                        + "    console.log('sarinah-fit: transform scale='+scale.toFixed(3)"
                        + "      +' content='+contentW+'x'+contentH"
                        + "      +' view='+viewW+'x'+viewH);"
                        + "  }"

                        // ===== STAR CLICK INTERCEPTOR (fallback) =====
                        // Tangkap click manual di area bintang, dispatch ulang ke elemen yg benar
                        + "  if(!window._sarinahStarFixApplied){"
                        + "    window._sarinahStarFixApplied=true;"
                        + "    document.addEventListener('click',function(e){"
                        + "      var t=e.target;"
                        + "      if(t && (t.classList.contains('fa-star')"
                        + "        ||t.classList.contains('fa-star-o')"
                        + "        ||t.closest('[class*=rating]'))){"
                        + "        console.log('sarinah-star-click: tag='+t.tagName"
                        + "          +' class='+t.className"
                        + "          +' x='+e.clientX+' y='+e.clientY);"
                        + "      }"
                        + "    },true);"

                        // Touch event fallback - beberapa WebView butuh ini
                        + "    document.addEventListener('touchend',function(e){"
                        + "      if(!e.target) return;"
                        + "      var t=e.target;"
                        + "      var isRating=t.classList.contains('fa-star')"
                        + "        ||t.classList.contains('fa-star-o')"
                        + "        ||t.closest('[class*=rating]');"
                        + "      if(isRating){"
                        + "        console.log('sarinah-star-touch: dispatching click on '+t.tagName);"
                        + "        var touch=e.changedTouches[0];"
                        + "        if(touch){"
                        + "          var clickEvt=new MouseEvent('click',{"
                        + "            bubbles:true,cancelable:true,"
                        + "            clientX:touch.clientX,clientY:touch.clientY"
                        + "          });"
                        + "          t.dispatchEvent(clickEvt);"
                        + "        }"
                        + "      }"
                        + "    },{passive:false});"
                        + "  }"

                        // Debug log posisi bintang
                        + "  var stars=document.querySelectorAll('.fa-star,.fa-star-o,[class*=star]');"
                        + "  if(stars.length>0){"
                        + "    for(var i=0;i<stars.length;i++){"
                        + "      var rect=stars[i].getBoundingClientRect();"
                        + "      console.log('sarinah-star['+i+']: x='+rect.x.toFixed(0)"
                        + "        +' y='+rect.y.toFixed(0)"
                        + "        +' w='+rect.width.toFixed(0)"
                        + "        +' h='+rect.height.toFixed(0)"
                        + "        +' class='+stars[i].className);"
                        + "    }"
                        + "    var last=stars[stars.length-1];"
                        + "    var rect=last.getBoundingClientRect();"
                        + "    var el=document.elementFromPoint(rect.x+rect.width/2,rect.y+rect.height/2);"
                        + "    console.log('sarinah-star: elementFromPoint on last star = '+("
                        + "      el?el.tagName+'.'+el.className:'null'));"
                        + "  }"

                        + "}catch(e){console.log('injectFit error: '+e.message)}"
                        + "})();";

        final String repeatedInject =
                "(function(){"
                        + "  var count=0;"
                        + "  var iv=setInterval(function(){"
                        + "    count++;"
                        + "    " + injectFit
                        + "    if(count>=10) clearInterval(iv);"
                        + "  },500);"
                        + "})();";

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (devMode) handler.proceed();
                else handler.cancel();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "Secondary page loaded: " + url);
                view.evaluateJavascript(injectFit, null);
                view.evaluateJavascript(repeatedInject, null);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                if (devMode && cm != null) {
                    Log.d(TAG, "SECONDARY JS: " + cm.message());
                }
                return super.onConsoleMessage(cm);
            }
        });

        root.addView(webView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        String defaultUrl = "https://" + host + "/web/image/3632";
        webView.loadUrl(defaultUrl);

        Log.d(TAG, "CustomerDisplay created on secondary screen: "
                + getDisplay().getName() + " ("
                + getDisplay().getWidth() + "x" + getDisplay().getHeight() + ")");
    }

    public void loadUrl(String url) {
        if (webView != null && url != null) {
            currentUrl = url;
            webView.post(() -> webView.loadUrl(url));
            Log.d(TAG, "Secondary loading: " + url);
        }
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void reload() {
        if (webView != null) webView.post(() -> webView.reload());
    }

    @Override
    public void dismiss() {
        try {
            if (webView != null) {
                webView.stopLoading();
                webView.destroy();
                webView = null;
            }
        } catch (Exception ignored) {}
        super.dismiss();
    }
}