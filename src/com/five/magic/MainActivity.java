package com.five.magic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * WebView 加载路由器设置首页， 设置相应的点击坐标，运行次数
 * 
 * Step:
 * 1. 配置路由器地址
 * 2. 配置路由器 断开/连接/刷新 的坐标
 * 3. 将连接地址填到URL栏，并点击右上角菜单进行保存
 * 4. 输入运行次数，点击START
 *
 */
public class MainActivity extends ActionBarActivity {

	private SharedPreferences mPrefs;
	private WebView mNetwork;
	private EditText mLoopCount;
	private EditText mLocationX, mLocationY, mLocationY1;
	private Button mReset;
	private Button mStart;
	private TextView mCurrentLoopView;
	private EditText mURLText1, mURLText2, mURLText3;
	private ImageView mClear1, mClear2, mClear3;
	private CheckBox mMobileBox;

	private String mX, mY, mY1;
	private String mURL1, mURL2, mURL3;
	private String mURL;
	private int mMaxLoop;
	private int mCurrentLoop;
	private int mOneLoopCount;
	private boolean mPageLoaded;
	private boolean mAirPlaneOn = false;
	private boolean oneLoopRunning = false;
	private ArrayList<String> mUrlList = new ArrayList<String>();

	private static final int OPERATION_CLICK_CLOSE = 10;
	private static final int OPERATION_CLICK_OPEN = 20;
	private static final int START_ET_ACTIVITY = 30;
	private static final int NET_REFRESH = 40;
    private static final int AIRPLANE_STEP_1 = 50;
    private static final int AIRPLANE_STEP_2 = 60;
	private static final int UPDATE_PROGRESS = 100;

	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
            case AIRPLANE_STEP_1:                
                setAirplaneMode1((Integer) msg.obj);
                break;
            case AIRPLANE_STEP_2:
                setAirplaneMode2((Integer) msg.obj);
                if (mAirPlaneOn) {
                    operationClick(OPERATION_CLICK_OPEN, 3);
                }
                break;
			case OPERATION_CLICK_CLOSE:
			    if (mMobileBox.isChecked()) {
			        mAirPlaneOn = true;
			        operationClick(AIRPLANE_STEP_1, 1, 1);
			        operationClick(AIRPLANE_STEP_2, 1, 3);  
			    } else {
			        doClick("Network close", mX + " " + mY); // 488 1120
			        operationClick(OPERATION_CLICK_OPEN, 5);
			    }
				break;
			case OPERATION_CLICK_OPEN:
	            if (mMobileBox.isChecked()) {
	                mAirPlaneOn = false;
	                operationClick(AIRPLANE_STEP_1, 0, 1);
	                operationClick(AIRPLANE_STEP_2, 0, 3);
	                operationClick(START_ET_ACTIVITY, 8);
	            } else {
	                doClick("Network open", mX + " " + mY); // 488 1120
	                operationClick(NET_REFRESH, 5);
	            }
				break;
			case NET_REFRESH:
	            doClick("Network refresh", mX + " " + mY1); // 993 1120
	            operationClick(START_ET_ACTIVITY, 3);
				break;
			case START_ET_ACTIVITY:
			    oneLoopRunning = true;
				startEtActivity();
				break;
			}
			return false;
		}
	});
	
    public void doCmds(List<String> cmds) throws Exception {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(process.getOutputStream());
        for (String tmpCmd : cmds) {
                os.writeBytes(tmpCmd+"\n");
        }
        os.writeBytes("exit\n");
        os.flush();
        os.close();
        process.waitFor();
        process.destroy();
    }
    
    private void setAirplaneMode1(int flag) {
        List<String> cmds = new ArrayList<String>();
        cmds.clear();
        cmds.add("settings put global airplane_mode_on " + flag);  
        try {
            doCmds(cmds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setAirplaneMode2(int flag) {
        List<String> cmds = new ArrayList<String>();
        cmds.clear();
        cmds.add("/system/bin/am broadcast -a android.intent.action.AIRPLANE_MODE --ez state " + (flag == 1 ? "true" : "false"));  
        try {
            doCmds(cmds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private void doClick(String action, String position) {
		Log.e("CQW", "doClick: action = " + action + ", input tap " + position);
		try {
			String keyCommand = "input tap " + position;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(keyCommand);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void operationClick(int action, int secends) {
		Message msg = Message.obtain();
		msg.what = action;
		mHandler.sendMessageDelayed(msg, secends * 1000);
	}
	
	   private void operationClick(int action, int flag,int secends) {
	        Message msg = Message.obtain();
	        msg.what = action;
	        msg.obj = flag;
	        mHandler.sendMessageDelayed(msg, secends * 1000);
	    }

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPrefs = getSharedPreferences("test", Context.MODE_PRIVATE);
		mX = mPrefs.getString("location_x", "");
		mY = mPrefs.getString("location_y", "");
		mY1 = mPrefs.getString("location_y1", "");

		mLoopCount = (EditText) findViewById(R.id.loop_count);
		mLoopCount.setText("1"); // default value

		mLocationX = (EditText) findViewById(R.id.location_x);
		mLocationY = (EditText) findViewById(R.id.location_y);
		mLocationY1 = (EditText) findViewById(R.id.location_y1);
		// 设置默认坐标
		if (mX.equals("") || mY.equals("")) {
		    mX = "488";
		    mY = "993";
		    mY1 = "1120";
		}
		mLocationX.setText(mX);
		mLocationY.setText(mY);
		mLocationY1.setText(mY1);

		mCurrentLoopView = (TextView) findViewById(R.id.current_loop);
		mMobileBox = (CheckBox) findViewById(R.id.use_mobile);

		mStart = (Button) findViewById(R.id.start);
		mStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String loop = mLoopCount.getText().toString();
				mMaxLoop = Integer.parseInt(loop);
				mCurrentLoop = 1;
				oneLoopRunning = true;
				updateCurrentLoop();

				mOneLoopCount = 0;
				mURL = mUrlList.get(mOneLoopCount);
				operationClick(OPERATION_CLICK_CLOSE, 1);
				mStart.setClickable(false);
				Log.e("CQW", "--------Start running-------");
			}
		});

		mReset = (Button) findViewById(R.id.reset);
		mReset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mCurrentLoop = 0;
				mLoopCount.setText("");
				mStart.setClickable(true);
			}
		});

		mURLText1 = (EditText) findViewById(R.id.url1_text);
		mURLText2 = (EditText) findViewById(R.id.url2_text);
		mURLText3 = (EditText) findViewById(R.id.url3_text);
		String url_text1 = mURLText1.getText().toString();
		String url_text2 = mURLText2.getText().toString();
		String url_text3 = mURLText3.getText().toString();
		loadURL("url1", mURLText1, url_text1);
		loadURL("url2", mURLText2, url_text2);
		loadURL("url3", mURLText3, url_text3);
		
        mUrlList.clear();
        loadUrlList();

		mClear1 = (ImageView) findViewById(R.id.clear_input1);
		mClear1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mURLText1.setText("");
			}
		});
		mClear2 = (ImageView) findViewById(R.id.clear_input2);
		mClear2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mURLText2.setText("");
			}
		});
		mClear3 = (ImageView) findViewById(R.id.clear_input3);
		mClear3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mURLText3.setText("");
			}
		});

		mNetwork = (WebView) findViewById(R.id.network);
		// 路由器地址: tplogin.cn 或  192.168.1.254
		mNetwork.loadUrl("http://tplogin.cn"); 
		mNetwork.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		mNetwork.setInitialScale(75);
		WebSettings settings = mNetwork.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE); 

		mNetwork.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				Message msg = new Message();
				msg.what = UPDATE_PROGRESS;
				msg.obj = newProgress;
				mHandler.sendMessage(msg);

				if (newProgress == 100) {
					mPageLoaded = true;
				} else {
					mPageLoaded = false;
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (oneLoopRunning) {
			if (mOneLoopCount < mUrlList.size()) {
				mURL = mUrlList.get(mOneLoopCount);
				operationClick(START_ET_ACTIVITY, 3);
			} else {
			    mOneLoopCount = 0;
			    oneLoopRunning = false;
				mCurrentLoop++;
				if (mCurrentLoop <= mMaxLoop && mCurrentLoop != 0) {
				    Log.e("CQW", "onResume OPERATION_CLICK_CLOSE mCurrentLoop = " + mCurrentLoop);
					if (mPageLoaded) {
						operationClick(OPERATION_CLICK_CLOSE, 1);
					} else {
						operationClick(OPERATION_CLICK_CLOSE, 3);
					}
					updateCurrentLoop();					
				} else {
				    // 运行结束
				    mStart.setClickable(true);
				    Log.e("CQW", "-------- END -------");
				}
			}
		}
	}

	private void startEtActivity() {
		try {
            Log.e("CQW", "mOneLoopCount = " + mOneLoopCount);
            mURL = mUrlList.get(mOneLoopCount);
			mOneLoopCount++;
			ComponentName componentName = new ComponentName("com.example.et3",
			        "com.example.et3.MainActivity");
			Intent intent = new Intent();
			intent.setComponent(componentName);
			intent.putExtra("URL", mURL);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(MainActivity.this, "App not found", Toast.LENGTH_SHORT)
			        .show();
		}
	}

	private void updateCurrentLoop() {
		mCurrentLoopView.setText("Current: " + mCurrentLoop);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			String x = mLocationX.getText().toString();
			String y = mLocationY.getText().toString();
			String y1 = mLocationY1.getText().toString();
			if (x != null && !x.equals("") && y != null && !y.equals("")
			        && y1 != null && !y1.equals("")) {
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("location_x", x);
				editor.putString("location_y", y);
				editor.putString("location_y1", y1);
				editor.commit();
			} else {
				Toast.makeText(this, "Please input X/Y", Toast.LENGTH_SHORT)
				        .show();
			}

			String url1 = mURLText1.getText().toString();
			String url2 = mURLText2.getText().toString();
			String url3 = mURLText3.getText().toString();
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putString("url1", url1);
			editor.putString("url2", url2);
			editor.putString("url3", url3);
			editor.commit();
			
			mUrlList.clear();
			loadUrlList();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void loadURL(String urlkey, EditText editText, String urlText) {
		mPrefs = getSharedPreferences("test", Context.MODE_PRIVATE);
		String url = mPrefs.getString(urlkey, "");
		if (urlText == null || urlText.equals("")) {
			if (url != null && !url.equals("")) {
				editText.setText(url);
			}
		} else {
			if (url == null || url.equals("")) {
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString(urlkey, urlText);
				editor.commit();
			}
		}
	}

    private void loadUrlList() {
        mURL1 = mURLText1.getText().toString();
        mURL2 = mURLText2.getText().toString();
        mURL3 = mURLText3.getText().toString();
        if (!mURL1.equals("")) {
            mUrlList.add(mURL1);
        }
        if (!mURL2.equals("")) {
            mUrlList.add(mURL2);
        }
        if (!mURL3.equals("")) {
            mUrlList.add(mURL3);
        }
    }
	
    private String formatURL(String url) {
        String u = "";
        String u1 = url.substring(0, 85);
        String u2 = url.substring(125, url.length());
        u = u1 + "param=" + u2;
        return u;
    }
}
