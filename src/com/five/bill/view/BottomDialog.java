package com.five.bill.view;

import com.five.bill.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;

public class BottomDialog extends Dialog {
    
    public BottomDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected BottomDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }
    
    public BottomDialog(Context context) {
        super(context);
        init(context);
    }
    
    private void init(Context context) {
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.bottom_dialog_layout, null);
        setContentView(contentView);
        setCancelable(true); //默认
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wmlp = getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        wmlp.width = LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(wmlp);
        getWindow().setWindowAnimations(R.style.GlobalDialogAnimation);
    }

}
