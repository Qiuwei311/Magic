package com.five.bill;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class StatusBarUtils {
    
    /**
     * 在{@link Activity#setContentView}之后调用
     * 
     * @param activity
     *            要实现的沉浸式状态栏的Activity
     * @param titleViewGroup
     *            头部控件的ViewGroup,若为null,整个界面将和状态栏重叠
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void initAfterSetContentView(Activity activity,
            View titleViewGroup) {
        if (activity == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (titleViewGroup == null)
            return;
        // 设置头部控件ViewGroup的PaddingTop,防止界面与状态栏重叠
        int statusBarHeight = getStatusBarHeight(activity);
        titleViewGroup.setPadding(0, statusBarHeight, 0, 0);
        
        //setTranslucentStatus(activity, true);
        //compat(activity, COLOR_DEFAULT);

    }
    
    /**
     * 获取状态栏高度
     * 
     * @param context
     * @return
     */
    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    
    @TargetApi(19)   
    private static void setTranslucentStatus(Activity activity, boolean on) {  
        Window win = activity.getWindow();  
        WindowManager.LayoutParams winParams = win.getAttributes();  
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;  
        if (on) {  
            winParams.flags |= bits;  
        } else {  
            winParams.flags &= ~bits;  
        }  
        win.setAttributes(winParams);  
    }
    
    private static final int INVALID_VAL = -1;  
    private static final int COLOR_DEFAULT = Color.parseColor("#3E444F");  
    public static void compat(Activity activity, int statusColor) {
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //    if (statusColor != INVALID_VAL) {
        //        activity.getWindow().setStatusBarColor(statusColor);
        //    }
        //    return;
        //}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //&& Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity
                    .findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL) {
                color = statusColor;
            }
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }

    }
}
