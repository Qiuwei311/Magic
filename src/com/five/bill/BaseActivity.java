package com.five.bill;

import java.util.Date;

import com.five.bill.utils.DateUtils;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class BaseActivity extends ActionBarActivity {
    
    protected TextView mTitleTextView;
    protected TextView mLeftTextView;
    protected TextView mRightTextView;
    protected ImageView mLeftImageView;
    protected ImageView mRightImageView;
    
    protected LinearLayout mMainContentView;
    //protected LinearLayout mScrollContentView;
    //protected ScrollView mScrollView;
    
    private ContentResolver mResolver;
    public static final Uri CARD_INFO_URI = Uri
            .parse("content://com.five.bill.provider/cardinfo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        setContentView(R.layout.activity_base);
        View topView = findViewById(R.id.actionbar);
        StatusBarUtils.initAfterSetContentView(this, topView);
        
        mResolver = getContentResolver();
        
        mTitleTextView = (TextView) topView.findViewById(R.id.title);
        mLeftTextView = (TextView) topView.findViewById(R.id.left_action);
        mRightTextView = (TextView) topView.findViewById(R.id.right_action);
        mLeftImageView = (ImageView) topView.findViewById(R.id.left_icon);
        mRightImageView = (ImageView) topView.findViewById(R.id.right_icon);
        
        mMainContentView = (LinearLayout) findViewById(R.id.main_content);
        //mScrollContentView = (LinearLayout) findViewById(R.id.scroll_content);
        //mScrollView = (ScrollView) findViewById(R.id.scrollView);
        
        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                finish();                
            }
        });
        
    }
    
    public void resetMainContentView(View view) {
        mMainContentView.removeAllViews();
        mMainContentView.addView(view);
    }
    
//    public void resetScrollContentView(View view) {
//        mScrollContentView.removeAllViews();
//        mScrollContentView.addView(view);
//    }
    
    public ContentResolver getResolver() {
        return mResolver;
    }
    
    public Date formatDate(String dateString) {
        return DateUtils.stringToDate("yyyy-MM-dd", dateString);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
