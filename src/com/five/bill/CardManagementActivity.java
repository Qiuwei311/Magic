package com.five.bill;

import com.five.bill.bean.CardInfo;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CardManagementActivity extends BaseActivity {
    
    private ToggleButton mPaymentNotity;
    private LinearLayout mPaymentDateSettings;
    private LinearLayout mBillDateSettings;
    private TextView mDeleteButton;
    
    private CardInfo mCardInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTitleTextView.setText("信用卡管理");
		mLeftTextView.setText("返回");
		mLeftTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}
		});
		mRightTextView.setVisibility(View.INVISIBLE);
		mCardInfo = (CardInfo) getIntent().getSerializableExtra("card_info");
		initContentView();
	}

	private void initContentView() {
		View contentView = LayoutInflater.from(this).inflate(
		        R.layout.activity_card_management, null);
		mMainContentView.addView(contentView);
		
        TextView bankName = (TextView) contentView.findViewById(R.id.bank_name);
        bankName.setText(mCardInfo.getBank_name());
        TextView cardNumber = (TextView) contentView.findViewById(R.id.card_number);
        cardNumber.setText(mCardInfo.getCard_number());
		
        mPaymentNotity = (ToggleButton) contentView
                .findViewById(R.id.auto_notify);
        mPaymentNotity
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                            boolean isChecked) {
                        mPaymentDateSettings.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    }
                });
        mPaymentDateSettings = (LinearLayout) contentView
                .findViewById(R.id.payment_date_settings);
        mPaymentDateSettings.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                Toast.makeText(CardManagementActivity.this, "Show dialog to setting payment day.", Toast.LENGTH_SHORT).show();             
            }
        });
        
        mBillDateSettings = (LinearLayout) contentView
                .findViewById(R.id.bill_date_settings);
        mBillDateSettings.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                Toast.makeText(CardManagementActivity.this, "Show dialog to setting bill day.", Toast.LENGTH_SHORT).show();             
            }
        });
        
        mDeleteButton = (TextView) contentView.findViewById(R.id.delete);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                showTips();                
            }
        });
	}
	
    public void showTips() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("提醒")
                .setMessage("删除账号")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        deleteCard();
                    }

                }).setNegativeButton("取消",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create();
        alertDialog.show();
    }
    
    private void deleteCard() {
        String where = "card_number=?"; //删除的条件
        String[] selectionArgs = { mCardInfo.getCard_number() }; //删除的条件参数
        getResolver().delete(CARD_INFO_URI, where, selectionArgs);
        
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(CardManagementActivity.this, CardListActivity.class);
        startActivity(intent);
    }
}
