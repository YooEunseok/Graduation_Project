package com.example.gp0905;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ReadMailActivity extends AppCompatActivity {

    private WebView mWebView;
    private long backKeyPressedTime = 0;
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_mail);

        // 웹뷰 셋팅
        mWebView = (WebView) findViewById(R.id.webView);//xml 자바코드 연결
        mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용

        mWebView.loadUrl("https://mail.google.com/");//웹뷰 실행
        mWebView.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
        mWebView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용
    }

     @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//뒤로가기 버튼 이벤트

         if (System.currentTimeMillis() > backKeyPressedTime + 3000) {//뒤로가기가 한번 눌리면
             if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {//웹뷰에서 뒤로가기 버튼을 누르면 뒤로가짐
                 backKeyPressedTime = System.currentTimeMillis();

                 mWebView.goBack();//이전 웹뷰로 이동
                 toast = Toast.makeText(this, "이전 페이지로 이동합니다.", Toast.LENGTH_SHORT);
                 toast.show();
                 return true;

             }
         }
         if (System.currentTimeMillis() <= backKeyPressedTime + 3000) { //3초안에 두번이상 눌리면
             finish();//액티비티 종료
             toast.cancel();//토스트 메시지 종료
         }

        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL",url);
            view.loadUrl(url);
            return true;
        }
    }


}