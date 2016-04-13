package com.example.web;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

public class WebActivity extends Activity {
	
	Button m_btn_back;
	WebView m_webView;
	ProgressBar m_progressBar;
//	String url = "http://www.baidu.com/";
	String url = "http://218.104.194.2:8090";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web);
		
		
		m_btn_back = (Button) findViewById(R.id.btn_back);
		m_progressBar = (ProgressBar) findViewById(R.id.progressbar);
		m_webView = (WebView) findViewById(R.id.myweb);
		
		m_btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		m_webView = (WebView) findViewById(R.id.myweb);
		
		openWebView(url);
	}
	
	
	
	public void openWebView(final String url){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				//设置webView能够执行javascript脚本
		        m_webView.getSettings().setJavaScriptEnabled(true);            
		        //设置可以支持缩放
		        m_webView.getSettings().setSupportZoom(true);//设置出现缩放工具
		        m_webView.getSettings().setBuiltInZoomControls(true);
		        
		        //设置可以自动加载图片
		        m_webView.getSettings().setLoadsImagesAutomatically(true);
		        
		        //载入URL
		        m_webView.loadUrl(url);
		        //使页面获得焦点
		        m_webView.requestFocus();

		        m_webView.setWebViewClient(new WebViewClient(){       
		            public boolean shouldOverrideUrlLoading(WebView view, String url) {   
		            	//如果自身加载新链接,不做外部跳转
		                if(url.indexOf("tel:")<0){
		                    view.loadUrl(url); 
		                }                
		                return true;       
		                //外部跳转,使用系统浏览器打开
//		                Intent in = new Intent (Intent.ACTION_VIEW , Uri.parse(url)); startActivity(in); return true; 
		            } 
		            
		            @Override
		            public void onPageFinished(WebView view, String url) {
		            	// TODO Auto-generated method stub
		            	super.onPageFinished(view, url);
		            	
		            	Log.i("ss", "页面加载完毕");
		            	m_progressBar.setVisibility(View.GONE);
		            	m_progressBar.postInvalidate();
		            } 
		            
		            @Override
		            public void onReceivedError(WebView view, int errorCode,
		            		String description, String failingUrl) {
		            	super.onReceivedError(view, errorCode, description, failingUrl);
		            	Log.e("sss", "加载页面报错");
		            }
		            
		        });
		        
		        m_webView.setWebChromeClient(new WebChromeClient() {
					@Override
					public void onProgressChanged(WebView view, int newProgress) {
						// TODO Auto-generated method stub
						super.onProgressChanged(view, newProgress);

//						setProgress(newProgress);
						
						setMyProgress(newProgress);
						
					}
		        });
			}
		});
	}
	
	
	private void setMyProgress(int progress){
		if (progress!=100) {
			m_progressBar.setVisibility(View.VISIBLE);
		}else {
			m_progressBar.setVisibility(View.GONE);
		}
		
		m_progressBar.setProgress(progress);
		m_progressBar.postInvalidate();
	}
	

	//重写return
    public boolean onKeyDown(int keyCoder,KeyEvent event)
    {
    	//如果网页能回退则后退
    	if(m_webView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
    		m_webView.goBack();
        }else {
			WebActivity.this.finish();
		}
        return false;      
    }
	

}
