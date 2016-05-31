package com.five.bill;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.five.bill.bean.Bill;
import com.five.bill.bean.CardInfo;
import com.five.bill.view.PinnedHeaderListView;
//import com.five.bill.view.BillListAdapter;
import com.five.bill.view.BillSectionedAdapter;

public class CardDetailsActivity extends BaseActivity {
    
    private CardInfo mCardInfo;
    private PinnedHeaderListView mBillListView;
//    private BillListAdapter mAdapter;
    private List<Bill> mBillList = new ArrayList<Bill>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTitleTextView.setText("信用卡详情");
		mLeftTextView.setText("返回");
		mLeftTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}
		});
		mRightTextView.setText("管理");
		mRightTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(CardDetailsActivity.this,
				        CardManagementActivity.class);
				intent.putExtra("card_info", mCardInfo);
				startActivity(intent);
			}
		});
		
		mCardInfo = (CardInfo) getIntent().getSerializableExtra("card_info");
		
		initContentView();
	}

	private void initContentView() {
		View contentView = LayoutInflater.from(this).inflate(
		        R.layout.activity_card_details, null);
		TextView bankName = (TextView) contentView.findViewById(R.id.bank_name);
		bankName.setText(mCardInfo.getBank_name());
	    TextView cardNumber = (TextView) contentView.findViewById(R.id.card_number);
	    cardNumber.setText(mCardInfo.getCard_number());
		// LinearLayout contentLayout = (LinearLayout)
		// contentView.findViewById(R.id.card_details_layout);
		mMainContentView.addView(contentView);
		
		loadData();
		
		mBillListView = (PinnedHeaderListView) contentView.findViewById(R.id.bill_listview);
//		mAdapter = new BillListAdapter(this, mBillList);
//		mBillListView.setAdapter(mAdapter);
//		mBillListView.setOnScrollListener(mAdapter);
//		mBillListView.setPinnedHeaderView(getLayoutInflater().inflate(R.layout.bill_list_header_layout, mBillListView, false));
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout header1 = (LinearLayout) inflator.inflate(R.layout.list_item, null);
//        ((TextView) header1.findViewById(R.id.textItem)).setText("HEADER 1");
//        LinearLayout header2 = (LinearLayout) inflator.inflate(R.layout.list_item, null);
//        ((TextView) header2.findViewById(R.id.textItem)).setText("HEADER 2");
        LinearLayout footer = (LinearLayout) inflator.inflate(R.layout.list_item, null);
        ((TextView) footer.findViewById(R.id.textItem)).setText("FOOTER");
//        mBillListView.addHeaderView(header1);
//        mBillListView.addHeaderView(header2);
        mBillListView.addFooterView(footer);
        BillSectionedAdapter sectionedAdapter = new BillSectionedAdapter(this, mBillList);
        mBillListView.setAdapter(sectionedAdapter);
	}
	
	private void loadData() {
	    for (int i = 0; i < 5; i++) {
	        Bill bill = new Bill();
	        bill.setBill_date(new Date());
	        bill.setBill_type(i);
	        bill.setCard_id(i);
	        bill.setDescription("adkjd-" + i);
	        bill.setSection(2);
	        mBillList.add(bill);
	    }
       for (int i = 0; i < 2; i++) {
            Bill bill = new Bill();
            bill.setBill_date(new Date());
            bill.setBill_type(i);
            bill.setCard_id(i);
            bill.setDescription("adkjd-" + i);
            bill.setSection(1);
            mBillList.add(bill);
        }
	}
}
