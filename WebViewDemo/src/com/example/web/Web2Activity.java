/****************************************************************************
Copyright (c) 2008-2010 Ricardo Quesada
Copyright (c) 2010-2012 cocos2d-x.org
Copyright (c) 2011      Zynga Inc.
Copyright (c) 2013-2014 Chukong Technologies Inc.
 
http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
package com.example.web;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class Web2Activity extends Activity {
	static Web2Activity m_appActivity  = null;
	WebView m_webView;
    ImageView m_imageView;
    FrameLayout m_webLayout;
    LinearLayout m_topLayout;
    Button m_backButton;
    ProgressBar m_progressBar;
    String m_url;
    String url = "http://www.baidu.com/";
    
    protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		m_appActivity=this;
        
		DisplayMetrics dm = new DisplayMetrics( );
		getWindowManager( ).getDefaultDisplay( ).getMetrics( dm );
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		
        //初始化一个空布局
        m_webLayout = new FrameLayout(this);
        FrameLayout.LayoutParams lytp = new FrameLayout.LayoutParams(screenWidth,screenHeight-100);
        lytp .gravity = Gravity.CENTER;
        addContentView(m_webLayout, lytp);
        
        openWebview(url);
	}
    
    //返回实例
    public static Web2Activity getInstance() {
        Log.v("AppActivity","getInstance");
    	return m_appActivity;
    }
    public void openWebview(String url) {
    	Log.v("AppActivity", "openWebView");
    	Log.v("AppActivity", url);
    	m_url = url;
    	this.runOnUiThread(new Runnable() {//在主线程里添加别的控件
            @SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
			public void run() {   
            	//初始化webView
                m_webView = new WebView(m_appActivity);
                m_webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                //设置webView能够执行javascript脚本
                m_webView.getSettings().setJavaScriptEnabled(true);            
                //设置可以支持缩放
                m_webView.getSettings().setSupportZoom(true);//设置出现缩放工具
                m_webView.getSettings().setBuiltInZoomControls(true);
                                
                //载入URL
                m_webView.loadUrl(m_url);
                //使页面获得焦点
                m_webView.requestFocus();

                m_webView.setWebViewClient(new WebViewClient(){       
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {   
                    	//如果自身加载新链接,不做外部跳转
                        if(url.indexOf("tel:")<0){
                            view.loadUrl(url); 
                        }
                        return true;       
                    } 
                    
                    @Override
                    public void onPageFinished(WebView view, String url) {
                    	// TODO Auto-generated method stub
                    	super.onPageFinished(view, url);
                    	
                    	//页面下载完毕,却不代表页面渲染完毕显示出来  
                        //WebChromeClient中progress==100时也是一样  
                        if (m_webView.getContentHeight() != 0) {  
                                //这个时候网页才显示  
                        }  
                    }                  
                    
                });
                
				m_webView.setWebChromeClient(new WebChromeClient() {
					@Override
					public void onProgressChanged(WebView view, int newProgress) {
						// TODO Auto-generated method stub
						super.onProgressChanged(view, newProgress);

						if (newProgress == 0) {
							m_progressBar.setVisibility(View.VISIBLE);
						}
						m_progressBar.setProgress(newProgress);
						m_progressBar.postInvalidate();
						if (newProgress == 100) {
							m_progressBar.setVisibility(View.INVISIBLE);
						}
					}
                });
                
                
                
                //背景图
                m_imageView = new ImageView(m_appActivity);
                m_imageView.setImageResource(R.drawable.webview_bg);
                m_imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                //初始化线性布局里面加按钮和webView
                m_topLayout = new LinearLayout(m_appActivity);      
                m_topLayout.setOrientation(LinearLayout.VERTICAL);
                //初始化返回按钮
                m_backButton = new Button(m_appActivity);
                m_backButton.setBackgroundResource(R.drawable.button_bg);
                m_backButton.setText("back");
                LinearLayout.LayoutParams lypt=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lypt.gravity=Gravity.RIGHT;
                m_backButton.setLayoutParams(lypt);            
                m_backButton.setOnClickListener(new OnClickListener() {                    
                    public void onClick(View v) {
                        removeWebView();
                    }
                });
                
                //初始化进度条
                m_progressBar = new ProgressBar(m_appActivity,null,android.R.attr.progressBarStyleHorizontal);
                m_progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_style));
                //把image加到主布局里
                m_webLayout.addView(m_imageView);
                //把webView加入到线性布局
                m_topLayout.addView(m_backButton);
                m_topLayout.addView(m_progressBar);
                m_topLayout.addView(m_webView);                
                //再把线性布局加入到主布局
                m_webLayout.addView(m_topLayout);
            }
        });
    }
	//移除webView
    public void removeWebView() {              
    	m_webLayout.removeView(m_imageView);
        m_imageView.destroyDrawingCache();
        
        m_webLayout.removeView(m_topLayout);
        m_topLayout.destroyDrawingCache();
                
        m_topLayout.removeView(m_webView);
        m_webView.destroy();
                
        m_topLayout.removeView(m_backButton);
        m_backButton.destroyDrawingCache();
        
        Web2Activity.this.finish();
    }
    
	//重写return键
    public boolean onKeyDown(int keyCoder,KeyEvent event)
    {
    	//如果网页能回退则后退，如果不能后退移除WebView
    	if(m_webView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
            m_webView.goBack();
        }else{
            removeWebView();
        }
        return false;      
    }


	@Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	DisplayMetrics dm = new DisplayMetrics( );
		getWindowManager( ).getDefaultDisplay( ).getMetrics( dm );
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		
        //初始化一个空布局
        m_webLayout = new FrameLayout(this);
        FrameLayout.LayoutParams lytp = new FrameLayout.LayoutParams(screenWidth,screenHeight-100);
        lytp .gravity = Gravity.CENTER;
        addContentView(m_webLayout, lytp);
    }


}
