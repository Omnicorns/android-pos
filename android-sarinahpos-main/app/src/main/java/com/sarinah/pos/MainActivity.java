package com.sarinah.pos;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import com.caysn.autoreplyprint.AutoReplyPrint;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // ============== KONFIG ==============
    private static final String HOST = "dev.sarinahportal.co.id";   // tanpa https://
    private static final boolean DEV_MODE = true;
    private static final boolean DEV_ALLOW_MIXED = true;            // ijinkan http (dev)
    private static final boolean DEV_ACCEPT_3P_COOKIES = true;      // third-party cookies (dev)
    private static final boolean DEV_VERBOSE_JS_LOG = true;

    private static final String[] DEV_SUFFIXES = { "dev.sarinahportal.co.id" };

    private String initialPosUrl() {
        return "https://" + HOST + "/web#action=426&model=pos.config&view_type=kanban&cids=1&bids=false&menu_id=270";
    }

    // ==== CETAK: lebar printer (576=80mm, 384=58mm) + padding aman ====
    private static final int PRINTER_MAX_WIDTH_DOTS = 576;
    private static final int SAFE_BOTTOM_PAD_DOTS  = 32;

    // ============== UI ==============
    private FrameLayout root;
    private LinearLayout topBar;
    private Button btnReload, btnExit;
    private TextView statusView;
    private FrameLayout primaryContainer;
    private TextView offlineOverlay;
    private ProgressBar progressBar;
    private WebView primary;

    // ============== SCAN STATE ==============
    // Gunakan flag untuk aktifkan HID/broadcast HANYA di halaman POS
    private volatile boolean scannerActive = false;

    // ============== SCAN (HID) ==============
    // Perbaikan utama: capture karakter PERTAMA, debounce lebih longgar, dan CONSUME event saat burst
    private final StringBuilder scanBuffer = new StringBuilder();
    private long lastKeystroke = 0L;
    private final Handler scanHandler = new Handler(Looper.getMainLooper());
    private final Runnable scanFinalizeTask = this::runFinalizeNow;

    // Lebihkan toleransi default supaya tidak cepat finalize di tengah scan
    private static final int SCAN_BURST_GAP_MS = 35;         // antar key (HID)
    private static final int SCAN_FINALIZE_TIMEOUT_MS = 150; // idle sebelum finalize

    // De-dupe antar sumber (HID vs Broadcast)
    private volatile long lastScanTs = 0L;
    private volatile String lastScanCode = null;

    // ============== SCAN (Broadcast) ==============
    private BroadcastReceiver scanReceiver;
    private IntentFilter scanFilter;

    // ============== FILE CHOOSER ==============
    private ValueCallback<Uri[]> mUMA;
    private String mCM;
    private static final int FCR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView.setWebContentsDebuggingEnabled(true);
        buildUi();
        buildScanBroadcast();

        primary = makeWebView();
        primaryContainer.addView(primary, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        statusView.setText(HOST.contains("dev") ? "DEV • Memuat…" : "Memuat…");
        primary.loadUrl(initialPosUrl());
    }

    private void buildUi() {
        root = new FrameLayout(this);
        root.setBackgroundColor(Color.BLACK);
        setContentView(root);

        topBar = new LinearLayout(this);
        topBar.setOrientation(LinearLayout.HORIZONTAL);
        topBar.setPadding(dp(12), dp(12), dp(12), dp(12));
        topBar.setBackgroundColor(Color.parseColor("#111827"));
        topBar.setGravity(Gravity.CENTER_VERTICAL);

        btnReload = new Button(this);
        btnReload.setText("Reload");
        btnReload.setOnClickListener(v -> {
            statusView.setText("Memuat…");
            showOffline(false);
            if (primary != null) primary.reload();
        });
        btnReload.setOnLongClickListener(v -> {
            try {
                if (primary != null) {
                    primary.clearCache(true);
                    primary.clearFormData();
                    CookieManager cm = CookieManager.getInstance();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) cm.removeAllCookies(val -> {});
                    else cm.removeAllCookie();
                    cm.flush();
                    String nuke =
                            "(async()=>{try{"
                                    + "if('caches' in window){let ks=await caches.keys();for(const k of ks){await caches.delete(k)}}"
                                    + "if('serviceWorker' in navigator){let regs=await navigator.serviceWorker.getRegistrations();for(const r of regs){await r.unregister()}}"
                                    + "if(window.indexedDB){try{let list=(indexedDB.databases?await indexedDB.databases():[]);"
                                    + "for(const d of list){if(d&&d.name){try{indexedDB.deleteDatabase(d.name)}catch(e){}}}}catch(e){}}"
                                    + "}catch(e){}; location.reload(true)})()";
                    primary.evaluateJavascript(nuke, null);
                }
            } catch (Exception ignored) {}
            return true;
        });

        btnExit = new Button(this);
        btnExit.setText("Exit");
        btnExit.setOnClickListener(v -> confirmAndExitSecure());

        statusView = new TextView(this);
        statusView.setTextColor(Color.WHITE);
        statusView.setPadding(dp(12), 0, 0, 0);

        topBar.addView(btnReload);
        topBar.addView(btnExit);
        LinearLayout.LayoutParams lpStatus = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        topBar.addView(statusView, lpStatus);

        primaryContainer = new FrameLayout(this);
        primaryContainer.setBackgroundColor(Color.BLACK);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.addView(topBar, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        content.addView(primaryContainer, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        root.addView(content, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        offlineOverlay = new TextView(this);
        offlineOverlay.setText("Koneksi terputus!!\n1. Periksa Wi-Fi\n2. Power Modem\n3. Kuota\n4. Hubungi Operation");
        offlineOverlay.setBackgroundColor(0xCCFF3B30);
        offlineOverlay.setTextColor(Color.WHITE);
        offlineOverlay.setTextSize(16f);
        offlineOverlay.setGravity(Gravity.CENTER);
        offlineOverlay.setVisibility(View.GONE);
        root.addView(offlineOverlay, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(dp(48), dp(48));
        lp.gravity = Gravity.CENTER;
        progressBar.setVisibility(View.GONE);
        root.addView(progressBar, lp);
    }

    // ============== WebView ==============
    private WebView makeWebView() {
        WebView wv = new WebView(this);
        WebSettings s = wv.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setSupportMultipleWindows(false);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) s.setSafeBrowsingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            s.setMixedContentMode(DEV_MODE && DEV_ALLOW_MIXED
                    ? WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                    : WebSettings.MIXED_CONTENT_NEVER_ALLOW);
            if (DEV_MODE && DEV_ACCEPT_3P_COOKIES) {
                try { CookieManager.getInstance().setAcceptThirdPartyCookies(wv, true); } catch (Throwable ignored) {}
            }
        }

        wv.setVerticalScrollBarEnabled(true);
        wv.setHorizontalScrollBarEnabled(false);

        // JS bridge (print + anti-CORS)
        wv.addJavascriptInterface(new WebAppInterface(), "Android");

        wv.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onConsoleMessage(ConsoleMessage cm) {
                if (DEV_VERBOSE_JS_LOG && cm != null) Log.d("WVConsole", "PRIMARY: " + cm.message());
                return super.onConsoleMessage(cm);
            }
            @Override public void onPermissionRequest(final PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) request.grant(request.getResources());
            }
            @Override public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (mUMA != null) mUMA.onReceiveValue(null);
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try { photoFile = createImageFile(); takePictureIntent.putExtra("PhotoPath", mCM); }
                    catch (Exception ex) { Log.e("Webview", "Image file creation failed", ex); }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else takePictureIntent = null;
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray = takePictureIntent != null ? new Intent[]{takePictureIntent} : new Intent[0];
                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Pilih Berkas");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);
                return true;
            }
        });

        wv.setWebViewClient(new WebViewClient() {
            private boolean isDevHost(@Nullable Uri u) {
                if (u == null || u.getHost() == null) return false;
                String host = u.getHost().toLowerCase();
                for (String suf : DEV_SUFFIXES) { String s2 = suf.toLowerCase(); if (host.equals(s2) || host.endsWith("." + s2)) return true; }
                return false;
            }

            @Override public void onReceivedError(WebView view, WebResourceRequest req, android.webkit.WebResourceError err) {
                if (req == null || req.isForMainFrame()) { showOffline(true); statusView.setText("OFFLINE"); }
            }
            @Override public void onReceivedHttpError(WebView view, WebResourceRequest req, WebResourceResponse err) {
                if (req == null || req.isForMainFrame()) { showOffline(true); statusView.setText("OFFLINE"); }
            }
            @Override public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                boolean allow = DEV_MODE && (isDevHost(Uri.parse(view.getUrl()==null?"":view.getUrl())))
                        || (error!=null && isDevHost(Uri.parse(error.getUrl()==null?"":error.getUrl())));
                if (allow) handler.proceed();
                else { handler.cancel(); showOffline(true); statusView.setText("OFFLINE"); Toast.makeText(view.getContext(), "SSL error", Toast.LENGTH_SHORT).show(); }
            }
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) { return !isAllowed(request.getUrl()); }
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) { try { return !isAllowed(Uri.parse(url)); } catch (Exception e) { return true; } }

            @Override public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                if (DEV_VERBOSE_JS_LOG) {
                    String hook = "(function(){if(window.__andErrHooked)return;window.__andErrHooked=true;"
                            + "window.addEventListener('error',e=>console.log('[JS-ERR]',(e&&e.message)||e));"
                            + "window.addEventListener('unhandledrejection',e=>console.log('[JS-PROMISE]',e&&e.reason));})();";
                    view.evaluateJavascript(hook,null);
                }
            }
            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                showOffline(false); statusView.setText("Online"); progressBar.setVisibility(View.GONE);
                view.requestFocus(); view.requestFocusFromTouch();
                // Aktifkan scanner hanya di halaman POS (biar tidak “nembak” di tempat lain)
                scannerActive = (url != null && url.contains("/pos/web"));
            }
        });

        return wv;
    }

    private boolean isAllowed(Uri u) {
        if (u == null || u.getHost() == null) return false;
        String host = u.getHost().toLowerCase(); String allowed = HOST.toLowerCase();
        return host.equals(allowed) || host.endsWith("." + allowed);
    }

    private void showOffline(boolean show) {
        offlineOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
        btnReload.setEnabled(!show);
        if (primary != null) primary.setEnabled(!show);
    }

    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && primary != null) { primary.requestFocus(); primary.requestFocusFromTouch(); }
    }

    // ============== Exit Aman ==============
    private void confirmAndExitSecure() {
        int kode = Calendar.getInstance().get(Calendar.SECOND);
        new AlertDialog.Builder(this)
                .setTitle("Peringatan")
                .setMessage("Pastikan POS Session sudah Close.\nData transaksi yang belum 'close session' dapat Hilang!")
                .setPositiveButton("Lanjut", (d, w) -> askCodeAndExit(kode))
                .setNegativeButton("Batal", null)
                .show();
    }
    private void askCodeAndExit(int kode) {
        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setHint("Masukkan kode: " + kode);
        new AlertDialog.Builder(this)
                .setTitle("Verifikasi")
                .setView(input)
                .setPositiveButton("OK", (d, w) -> {
                    Integer entered = null;
                    try { entered = Integer.parseInt(input.getText().toString().trim()); } catch (Exception ignored) {}
                    if (entered != null && entered == kode) { safeCleanupAndExit(); }
                    else { Toast.makeText(this, "Kode salah.", Toast.LENGTH_SHORT).show(); }
                })
                .setNegativeButton("Batal", null)
                .show();
    }
    private void safeCleanupAndExit() {
        try {
            CookieManager cm = CookieManager.getInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) cm.removeAllCookies(val -> {});
            else cm.removeAllCookie();
            cm.flush();
        } catch (Exception ignored) {}
        try { if (primary != null) primary.destroy(); } catch (Exception ignored) {}
        finishAffinity();
        new Handler(Looper.getMainLooper()).postDelayed(() -> System.exit(0), 150);
    }

    // ============== SCANNER (HID) ==============
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Proses HID hanya saat halaman POS aktif
        if (!scannerActive) return super.dispatchKeyEvent(event);

        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            int keyCode = event.getKeyCode();

            // Abaikan modifier
            if (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT || keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT
                    || keyCode == KeyEvent.KEYCODE_ALT_LEFT || keyCode == KeyEvent.KEYCODE_ALT_RIGHT
                    || keyCode == KeyEvent.KEYCODE_CTRL_LEFT || keyCode == KeyEvent.KEYCODE_CTRL_RIGHT
                    || keyCode == KeyEvent.KEYCODE_META_LEFT || keyCode == KeyEvent.KEYCODE_META_RIGHT) {
                return super.dispatchKeyEvent(event);
            }

            long now = System.currentTimeMillis();
            boolean burst = (now - lastKeystroke) <= SCAN_BURST_GAP_MS;

            // ENTER mengakhiri burst
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (scanBuffer.length() > 0) runFinalizeNow();
                return true; // consume enter dari scanner
            }

            int uc = event.getUnicodeChar();
            boolean hasModifier = event.isShiftPressed() || event.isAltPressed() || event.isCtrlPressed() || event.isMetaPressed();

            // **Perbaikan utama**: tangkap KARAKTER PERTAMA juga (mulai buffer)
            if (uc != 0 && !hasModifier) {
                scanBuffer.append((char) uc);
                lastKeystroke = now;
                scheduleScanFinalize();
                return true; // consume supaya tidak bocor ke WebView
            }

            if (uc != 0) lastKeystroke = now;
        }
        return super.dispatchKeyEvent(event);
    }

    private void scheduleScanFinalize() {
        scanHandler.removeCallbacks(scanFinalizeTask);
        scanHandler.postDelayed(scanFinalizeTask, SCAN_FINALIZE_TIMEOUT_MS);
    }
    private void runFinalizeNow() {
        scanHandler.removeCallbacks(scanFinalizeTask);
        if (scanBuffer.length() == 0) return;
        String code = scanBuffer.toString();
        scanBuffer.setLength(0);

        // Bersihkan & finalize
        String cleaned = code.replaceAll("\\p{Cntrl}", "");
        String finalCode = cleaned.isEmpty() ? code : cleaned;

        // De-dupe vs broadcast
        long now = System.currentTimeMillis();
        if (isDuplicateScan(finalCode, now)) return;
        lastScanCode = finalCode;
        lastScanTs = now;

        handleScannedCode(finalCode);
    }

    private boolean isDuplicateScan(String code, long now) {
        return code != null && code.equals(lastScanCode) && (now - lastScanTs) <= 400;
    }

    // ============== SCANNER (Broadcast) ==============
    private void buildScanBroadcast() {
        scanFilter = new IntentFilter();

        // Sunmi
        scanFilter.addAction("com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED");
        scanFilter.addAction("com.sunmi.peripheral.scanner.ACTION_DATA_CODE_RECEIVED");
        scanFilter.addAction("com.sunmi.scanner.ACTION_SCAN_SUCCESS");

        // Newland / OEM
        scanFilter.addAction("nlscan.action.SCANNER_RESULT");

        // Zebra DataWedge
        scanFilter.addAction("com.symbol.datawedge.data");
        scanFilter.addAction("com.symbol.datawedge.api.RESULT_ACTION");

        // Generic / OEM lain
        scanFilter.addAction("android.intent.action.DECODE_DATA");          // ← perbaikan (case benar)
        scanFilter.addAction("android.intent.action.SCANRESULT");
        scanFilter.addAction("com.android.server.scannerservice.broadcast");
        scanFilter.addAction("com.qs.scanner.SCAN");

        scanReceiver = new BroadcastReceiver() {
            @Override public void onReceive(Context ctx, Intent i) {
                if (primary == null || !scannerActive) return;

                // Debug extras (boleh dimatikan kalau sudah stabil)
                try {
                    if (i != null && i.getExtras()!=null) {
                        for (String k : i.getExtras().keySet()) {
                            Log.d("SCANDBG","act="+i.getAction()+" extra "+k+"="+i.getExtras().get(k));
                        }
                    } else {
                        Log.d("SCANDBG","act="+(i==null?null:i.getAction())+" (no extras)");
                    }
                } catch (Exception ignore){}

                String code = extractBarcodeFromIntent(i);
                if (code == null) return;
                code = code.replaceAll("\\p{Cntrl}", "");
                if (code.isEmpty()) return;

                long now = System.currentTimeMillis();
                if (isDuplicateScan(code, now)) return; // de-dupe HID vs broadcast
                lastScanCode = code;
                lastScanTs = now;

                handleScannedCode(code);
            }
        };
    }

    private static final String[] EXTRA_STRING_KEYS = new String[]{
            // umum
            "data","text",Intent.EXTRA_TEXT,"value","scanvalue",
            // Sunmi
            "barcode_string",
            // Newland/ALPS
            "SCAN_BARCODE1","barcode","barocode",
            // variasi lain
            "scannerdata","scan_data","scan_code","code",
            // Zebra
            "com.symbol.datawedge.data_string"
    };

    private String extractBarcodeFromIntent(Intent i) {
        if (i == null) return null;

        // 1) string langsung
        for (String k : EXTRA_STRING_KEYS) {
            String v = i.getStringExtra(k);
            if (v != null && !(v=v.trim()).isEmpty()) return v;
        }

        // 2) DataWedge: ArrayList<byte[]> di "decode_data"
        try {
            Object blob = i.getExtras() == null ? null : i.getExtras().get("com.symbol.datawedge.decode_data");
            if (blob instanceof java.util.ArrayList) {
                java.util.ArrayList<?> arr = (java.util.ArrayList<?>) blob;
                if (!arr.isEmpty() && arr.get(0) instanceof byte[]) {
                    return new String((byte[]) arr.get(0), java.nio.charset.StandardCharsets.UTF_8).trim();
                }
            }
        } catch (Exception ignored) {}

        // 3) variasi byte[] vendor
        try {
            byte[] b;
            if ((b = i.getByteArrayExtra("dataBytes")) != null) return new String(b, java.nio.charset.StandardCharsets.UTF_8).trim();
            if ((b = i.getByteArrayExtra("barocode_bytes")) != null) return new String(b, java.nio.charset.StandardCharsets.UTF_8).trim();
            if ((b = i.getByteArrayExtra("barcodeBytes")) != null) return new String(b, java.nio.charset.StandardCharsets.UTF_8).trim();
        } catch (Exception ignored) {}

        return null;
    }

    private void handleScannedCode(String code) {
        if (code == null) return;

        // buang karakter kontrol dan escape supaya aman di JS
        String clean = code.replaceAll("\\p{Cntrl}", "");
        String esc = clean.replace("\\", "\\\\").replace("'", "\\'");

        String js =
                "(function(b){try{"
                        + "console.log('Inject barcode:', b);"

                        // ==== PLAN A: cari input yang relevan ====
                        + "var findBox=function(){"
                        + "  var c=document.querySelectorAll('input,textarea');"
                        + "  for(var i=0;i<c.length;i++){var el=c[i];"
                        + "    var ph=(el.getAttribute('placeholder')||'')+'';"
                        + "    var ar=(el.getAttribute('aria-label')||'')+'';"
                        + "    var cn=(el.className||'')+'';"
                        + "    if(/search|cari|barcode|scan/i.test(ph+ar+cn) && el.offsetParent!==null){"
                        + "      return el;"
                        + "    }"
                        + "  }"
                        + "  return null;"
                        + "};"
                        + "var box=findBox();"
                        + "if(box){"
                        + "  box.focus();"
                        + "  box.value=b;"
                        + "  box.dispatchEvent(new Event('input',{bubbles:true}));"
                        + "  var e1=new KeyboardEvent('keydown',{key:'Enter',code:'Enter',bubbles:true});"
                        + "  var e2=new KeyboardEvent('keyup',{key:'Enter',code:'Enter',bubbles:true});"
                        + "  document.dispatchEvent(e1);"
                        + "  document.dispatchEvent(e2);"
                        + "  try {"
                        + "    document.getElementsByClassName('button proces_search')[0].click();"
                        + "  } catch(e) {"
                        + "    console.log('Tombol proces_search tidak ditemukan:', e);"
                        + "  }"
                        + "}else{"

                        // ==== PLAN B: fallback generic ====
                        + "  const type=(el,txt)=>{"
                        + "    el.focus();"
                        + "    el.value='';"
                        + "    el.dispatchEvent(new Event('input',{bubbles:true}));"
                        + "    for(const ch of txt){"
                        + "      el.value+=ch;"
                        + "      el.dispatchEvent(new Event('input',{bubbles:true}));"
                        + "    }"
                        + "  };"
                        + "  const clickById=(id)=>{"
                        + "    const x=document.getElementById(id);"
                        + "    if(x){x.click();return true}return false;"
                        + "  };"
                        + "  var input=document.getElementById('input_barcode_mobile')"
                        + "           ||document.querySelector('input[type=search],input[type=tel],input[type=number],input[type=text]');"
                        + "  if(input){"
                        + "    type(input,b);"
                        + "    clickById('procces_barcode_mobile');"
                        + "  }else{"
                        + "    const fire=(t,o)=>document.dispatchEvent(new KeyboardEvent(t,Object.assign({bubbles:true,cancelable:true},o||{})));"
                        + "    for(const ch of b){"
                        + "      const k=String(ch);"
                        + "      fire('keydown',{key:k});"
                        + "      fire('keypress',{key:k});"
                        + "      fire('keyup',{key:k});"
                        + "    }"
                        + "    fire('keydown',{key:'Enter',code:'Enter'});"
                        + "    fire('keyup',{key:'Enter',code:'Enter'});"
                        + "  }"
                        + "}"
                        + "}catch(e){console.log('androidScan-error',e)}})('"+esc+"');";

        if (primary != null) primary.evaluateJavascript(js, null);
    }


    // ============== Lifecycle (register receiver) ==============
    @Override protected void onStart() {
        super.onStart();
        if (primary != null) { primary.requestFocus(); primary.requestFocusFromTouch(); }
        if (scanReceiver != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    registerReceiver(scanReceiver, scanFilter, Context.RECEIVER_EXPORTED);
                } else {
                    registerReceiver(scanReceiver, scanFilter);
                }
            } catch (Exception ignored) {}
        }
    }
    @Override protected void onStop() {
        try { if (scanReceiver != null) unregisterReceiver(scanReceiver); } catch (Exception ignored) {}
        scanHandler.removeCallbacksAndMessages(null); // bersihkan debounce yang tertunda
        super.onStop();
    }

    // ============== FILE CHOOSER ==============
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    if (mUMA == null) return;
                    if (intent == null) {
                        if (mCM != null) results = new Uri[]{Uri.parse(mCM)};
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            if (mUMA != null) mUMA.onReceiveValue(results);
            mUMA = null;
        }
    }

    private File createImageFile() throws java.io.IOException {
        String imageFileName = "img_tmp_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    // ============== PRINTING ==============
    private void doPhotoPrint(Bitmap bitmap) {
        try {
            PrintHelper ph = new PrintHelper(this);
            ph.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            ph.setColorMode(PrintHelper.COLOR_MODE_MONOCHROME);
            ph.setOrientation(PrintHelper.ORIENTATION_PORTRAIT);
            ph.printBitmap("receipt.jpg - print", bitmap);
            showMessageOnUiThread("Print via Android Print (FIT)");
        } catch (Throwable t) {
            showMessageOnUiThread("PrintHelper error: " + t.getMessage());
        }
    }

    private void showMessageOnUiThread(final String msg) { runOnUiThread(() -> Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show()); }

    private Pointer openPort() {
        Pointer h = Pointer.NULL;
        String[] listUsbPort = AutoReplyPrint.CP_Port_EnumUsb_Helper.EnumUsb();
        if (listUsbPort != null) {
            for (String usbPort : listUsbPort) {
                if (usbPort.contains("0x4B43") || usbPort.contains("0x0FE6")) { h = AutoReplyPrint.INSTANCE.CP_Port_OpenUsb(usbPort, 0); break; }
            }
        }
        if (h == Pointer.NULL) showMessageOnUiThread("OpenPort Failed");
        return h;
    }

    private boolean queryPrintResult(Pointer h) {
        boolean result = AutoReplyPrint.INSTANCE.CP_Pos_QueryPrintResult(h, 30000);
        showMessageOnUiThread(result ? "Print Success" : "Print Failed");
        if (!result) {
            LongByReference err = new LongByReference();
            LongByReference info = new LongByReference();
            LongByReference ts = new LongByReference();
            if (AutoReplyPrint.INSTANCE.CP_Printer_GetPrinterStatusInfo(h, err, info, ts)) {
                AutoReplyPrint.CP_PrinterStatus st = new AutoReplyPrint.CP_PrinterStatus(err.getValue(), info.getValue());
                String es = String.format("Printer Error: 0x%04X", err.getValue() & 0xffff);
                if (st.ERROR_NOPAPER()) es += " [NO PAPER]";
                if (st.ERROR_COVERUP()) es += " [COVER OPEN]";
                showMessageOnUiThread(es);
            }
        }
        return result;
    }

    private void doDirectPrint(Bitmap source){
        Bitmap bitmap = prepareForThermal(source, PRINTER_MAX_WIDTH_DOTS, SAFE_BOTTOM_PAD_DOTS);
        Pointer h = openPort();
        if (h != Pointer.NULL) {
            try {
                AutoReplyPrint.INSTANCE.CP_Printer_ClearPrinterBuffer(h);
                AutoReplyPrint.CP_Pos_PrintRasterImageFromData_Helper.PrintRasterImageFromBitmap(
                        h, bitmap.getWidth(), bitmap.getHeight(), bitmap,
                        AutoReplyPrint.CP_ImageBinarizationMethod_Thresholding,
                        AutoReplyPrint.CP_ImageCompressionMethod_None);
                AutoReplyPrint.INSTANCE.CP_Pos_FeedAndHalfCutPaper(h);
                boolean ok = queryPrintResult(h);
                if (!ok) { doPhotoPrint(bitmap); }
            } finally {
                AutoReplyPrint.INSTANCE.CP_Port_Close(h);
            }
        } else { doPhotoPrint(bitmap); }
    }

    private void doKickDrawer(){
        Pointer h = openPort();
        if (h != Pointer.NULL) {
            AutoReplyPrint.INSTANCE.CP_Pos_KickOutDrawer(h, 0, 100, 100);
            AutoReplyPrint.INSTANCE.CP_Pos_KickOutDrawer(h, 1, 100, 100);
            AutoReplyPrint.INSTANCE.CP_Pos_KickOutDrawer(h, 0, 50, 250);
            AutoReplyPrint.INSTANCE.CP_Port_Close(h);
        }
    }

    // ==== Util scale lama (biarkan tersedia) ====
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        int bitmapWidth = bitmap.getWidth(); int bitmapHeight = bitmap.getHeight();
        float scaleWidth = (float) w / bitmapWidth; float scaleHeight = (float) h / bitmapHeight;
        Matrix matrix = new Matrix(); matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
    }
    public static Bitmap resizeImageToWidth(Bitmap bitmap, int w) {
        int ww = floorTo8(Math.min(bitmap.getWidth(), w));
        int h = Math.round(ww * (bitmap.getHeight() / (float) bitmap.getWidth()));
        return resizeImage(bitmap, ww, h);
    }

    // ==== Helper cetak thermal ====
    private static int floorTo8(int v) { return (v / 8) * 8; }

    private static Bitmap prepareForThermal(Bitmap src, int maxDots, int bottomPadDots) {
        Bitmap in = (src.getConfig() == Bitmap.Config.ARGB_8888) ? src : src.copy(Bitmap.Config.ARGB_8888, false);
        int targetW = floorTo8(Math.min(in.getWidth(), maxDots));
        int targetH = Math.round(in.getHeight() * (targetW / (float) in.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(in, targetW, targetH, true);

        Bitmap out = Bitmap.createBitmap(targetW, targetH + bottomPadDots, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(out);
        c.drawColor(Color.WHITE);
        c.drawBitmap(scaled, 0, 0, null);
        if (scaled != in) scaled.recycle();
        return out;
    }

    // ============== JS Bridge (print + anti-CORS) ==============
    public class WebAppInterface {
        @JavascriptInterface public void printImg(String base64, String is_mobile) {
            try {
                byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                if (decodedImage == null) { Toast.makeText(MainActivity.this, "Gagal decode gambar.", Toast.LENGTH_SHORT).show(); return; }

                Bitmap ready = prepareForThermal(decodedImage, PRINTER_MAX_WIDTH_DOTS, SAFE_BOTTOM_PAD_DOTS);

                if ("True".equalsIgnoreCase(is_mobile)) {
                    doPhotoPrint(ready);
                } else {
                    doKickDrawer();
                    doDirectPrint(ready);
                }
            } catch (Exception e) {
                Log.e("printImg", "Error:", e);
                Toast.makeText(MainActivity.this, "Print error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        @JavascriptInterface public void kickDrawer() { doKickDrawer(); }

        // ========= Anti-CORS: POST/PUT/DELETE via native =========
        @JavascriptInterface
        public void postJson(String url, String headersJson, String body) {
            new Thread(() -> {
                int code = -1; String resp = "";
                try {
                    HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
                    c.setConnectTimeout(15000);
                    c.setReadTimeout(30000);
                    c.setRequestMethod("POST");
                    c.setDoOutput(true);

                    if (headersJson != null && headersJson.trim().length() > 0) {
                        try {
                            JSONObject h = new JSONObject(headersJson);
                            for (java.util.Iterator<String> it = h.keys(); it.hasNext(); ) {
                                String k = it.next();
                                String v = h.isNull(k) ? "" : h.optString(k, "");
                                if (k != null) c.setRequestProperty(k, v);
                            }
                        } catch (Exception ignore) {}
                    }
                    if (c.getRequestProperty("Content-Type") == null)
                        c.setRequestProperty("Content-Type", "application/json");

                    byte[] bytes = body == null ? new byte[0] : body.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                    OutputStream os = c.getOutputStream();
                    os.write(bytes); os.flush(); os.close();

                    code = c.getResponseCode();
                    InputStream is = (code >= 200 && code < 300) ? c.getInputStream() : c.getErrorStream();
                    if (is != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buf = new byte[4096]; int n;
                        while ((n = is.read(buf)) > 0) baos.write(buf,0,n);
                        resp = baos.toString("UTF-8");
                        is.close();
                    }
                    c.disconnect();
                } catch (Exception e) {
                    Log.e("CORS", "postJson error", e);
                    resp = "{\"error\":\""+e.getMessage()+"\"}";
                }
                final int fcode = code; final String fbody = resp;
                runOnUiThread(() -> {
                    if (primary != null) {
                        String js = "window.AndroidReceive && window.AndroidReceive("
                                + "{status:"+fcode+",body:"+jsonQuote(fbody)+"}"
                                + ");";
                        primary.evaluateJavascript(js, null);
                    }
                });
            }).start();
        }
    }

    private String jsonQuote(String s){
        if (s == null) return "null";
        String q = s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\r","\\r");
        return "\""+q+"\"";
    }

    private int dp(int v) { return (int) (getResources().getDisplayMetrics().density * v); }
}
