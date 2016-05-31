package com.five.bill.view;

import com.five.bill.R;
import com.five.bill.bean.CardInfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomCardItemView extends RelativeLayout {
    
    private ImageView mIcon;
    private TextView mBankName;
    private TextView mUnsettledBill;

    public CustomCardItemView(Context context) {
        super(context);
    }
    public CustomCardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public CustomCardItemView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public CustomCardItemView(Context context, int iconId, CardInfo info) {
        super(context);
        init(context, iconId, info);
    }
    
    public void setFeatureIcon(int resId) {
        mIcon.setImageResource(resId);
    }
    
    public void setBankName(String text) {
        mBankName.setText(text);
    }
    
    public void init(Context context, int iconId, CardInfo info) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.costom_card_item, null);
        mIcon = (ImageView) view.findViewById(R.id.bank_icon);
        mBankName = (TextView) view.findViewById(R.id.bank_name);
        mUnsettledBill = (TextView) view.findViewById(R.id.unsettled_bills);
        
        mIcon.setImageResource(iconId);
        mBankName.setText(info.getBank_name());
        mUnsettledBill.setText(Double.toString(info.getUnsettled_bills()));        
    }

}
