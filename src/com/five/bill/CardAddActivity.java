package com.five.bill;

import com.five.bill.bean.CardInfo;
import com.five.bill.view.BottomDialog;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CardAddActivity extends BaseActivity {
    
    private LinearLayout mStatementDateLayout; // 账单日
    private LinearLayout mPaymentDueDateLayout; // 还款日
    
    private ToggleButton mPaymentNotity;
    private LinearLayout mPaymentNotitySettings; // 还款提醒日
    
    private BottomDialog mDialog;
    
    private EditText mCardNumber;
    private EditText mCardBank;
    private EditText mCardName;
    private TextView mStatementDate;
    private TextView mPaymentDueDate;
    private TextView mPaymentNotityDate;
    
    private Button mConfirm;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTitleTextView.setText("添加信用卡");
		mLeftTextView.setText("返回");
		mLeftTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}
		});
		mRightTextView.setVisibility(View.INVISIBLE);
		
		mDialog = new BottomDialog(this, R.style.DialogBottomStyle);
		
		initContentView();
	}

	private void initContentView() {
		View contentView = LayoutInflater.from(this).inflate(
		        R.layout.activity_card_add, null);
		mMainContentView.addView(contentView);
		
        mPaymentNotity = (ToggleButton) contentView
                .findViewById(R.id.auto_notify);
        mPaymentNotity
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                            boolean isChecked) {
                        mPaymentNotitySettings.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    }
                });
        
        mPaymentNotitySettings = (LinearLayout) contentView
                .findViewById(R.id.payment_notify_settings);
        
        mStatementDateLayout = (LinearLayout) contentView
                .findViewById(R.id.statement_date_layout);
        
        mPaymentDueDateLayout = (LinearLayout) contentView
                .findViewById(R.id.payment_due_date_layout);
        mPaymentDueDateLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                LayoutInflater inflater = LayoutInflater.from(CardAddActivity.this);
//                View v = inflater.inflate(R.layout.bottom_dialog_layout, null);
//                Dialog dialog = new Dialog(CardAddActivity.this, R.style.DialogBottomStyle);
//                dialog.setContentView(v);
//                dialog.setCanceledOnTouchOutside(true);
//                Window window = dialog.getWindow();
//                WindowManager.LayoutParams wmlp = window.getAttributes();
//                wmlp.gravity = Gravity.BOTTOM;
//                wmlp.width = LayoutParams.MATCH_PARENT;
//                window.setAttributes(wmlp);
//                window.setWindowAnimations(R.style.GlobalDialogAnimation);
//                dialog.show();
                mDialog.show();
            }
        });
        
        //payment_notify_settings
        
        mCardNumber = (EditText) contentView.findViewById(R.id.card_number);
        mCardBank = (EditText) contentView.findViewById(R.id.bank);
        mCardName = (EditText) contentView.findViewById(R.id.my_name);
        mStatementDate = (TextView) contentView.findViewById(R.id.statement_date);
        mPaymentDueDate = (TextView) contentView.findViewById(R.id.payment_due_date);
        mPaymentNotityDate = (TextView) contentView.findViewById(R.id.payment_notify_date);
        
        mConfirm = (Button) contentView.findViewById(R.id.confirm);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                CardInfo info = new CardInfo();
                info.setCard_id(1);
                info.setBank_name(mCardBank.getText().toString());
                info.setCard_number(mCardNumber.getText().toString());                
                info.setStatement_date(mStatementDate.getText().toString());
                info.setPayment_date(mPaymentDueDate.getText().toString());
                info.setNeed_notity(mPaymentNotity.isChecked());
                if (mPaymentNotity.isChecked()) {
                    info.setNotity_date(mPaymentNotityDate.getText().toString());
                }
                info.setCurrent_debt(0);
                info.setSettled_bills(0);
                info.setUnsettled_bills(0);
                
                Uri retUri = cardInsert(info);
                if (retUri != null) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });        
    }

    private Uri cardInsert(CardInfo info) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("card_id", info.getCard_id());
        contentValues.put("bank_name", info.getBank_name());
        contentValues.put("card_number", info.getCard_number());
        contentValues.put("current_debt", 0);
        contentValues.put("need_notity", info.isNeed_notity() ? 1 : 0);
        contentValues.put("payment_date", info.getPayment_date());
        contentValues.put("settled_bills", 0);
        contentValues.put("unsettled_bills", 0);
        Uri insertUri = getResolver().insert(CARD_INFO_URI, contentValues);
        
        return insertUri;
    }
}
