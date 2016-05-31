package com.five.bill;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.five.bill.bean.Bill;
import com.five.bill.view.BillSectionedAdapter;
import com.five.bill.view.PinnedHeaderListView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
    
    private TextView mDetail, mReportForms, mAccount;

    private PinnedHeaderListView mDetailListView;
    // private BillListAdapter mAdapter;
    private List<Bill> mDetailList = new ArrayList<Bill>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mTitleTextView.setText("账号概要");
        mLeftTextView.setVisibility(View.INVISIBLE);
        mLeftImageView.setVisibility(View.INVISIBLE);
        mRightTextView.setText("详情");
        
        mRightTextView.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CardListActivity.class);
                startActivity(intent);                
            }
        });
        
        initContentView();
    }
    
    private void initContentView() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.activity_main, null);
//        mScrollContentView = (LinearLayout) contentView
//                .findViewById(R.id.header_view);
        mMainContentView.addView(contentView);
        
        mDetail = (TextView) contentView.findViewById(R.id.detail);
        mReportForms = (TextView) contentView.findViewById(R.id.reprot_forms);
        mAccount = (TextView) contentView.findViewById(R.id.account);
        mDetail.setOnClickListener(tabClickListener);
        mReportForms.setOnClickListener(tabClickListener);
        mAccount.setOnClickListener(tabClickListener);
        
        loadData();
        
        mDetailListView = (PinnedHeaderListView) contentView.findViewById(R.id.bill_listview);
//      mAdapter = new BillListAdapter(this, mBillList);
//      mBillListView.setAdapter(mAdapter);
//      mBillListView.setOnScrollListener(mAdapter);
//      mBillListView.setPinnedHeaderView(getLayoutInflater().inflate(R.layout.bill_list_header_layout, mBillListView, false));
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout header1 = (LinearLayout) inflator.inflate(R.layout.list_item, null);
//        ((TextView) header1.findViewById(R.id.textItem)).setText("HEADER 1");
//        LinearLayout header2 = (LinearLayout) inflator.inflate(R.layout.list_item, null);
//        ((TextView) header2.findViewById(R.id.textItem)).setText("HEADER 2");
        LinearLayout footer = (LinearLayout) inflator.inflate(R.layout.list_item, null);
        ((TextView) footer.findViewById(R.id.textItem)).setText("FOOTER");
//        mBillListView.addHeaderView(header1);
//        mBillListView.addHeaderView(header2);
        mDetailListView.addFooterView(footer);
        BillSectionedAdapter sectionedAdapter = new BillSectionedAdapter(this, mDetailList);
        mDetailListView.setAdapter(sectionedAdapter);
    }
    
    private void loadData() {
        for (int i = 0; i < 5; i++) {
            Bill bill = new Bill();
            bill.setBill_date(new Date());
            bill.setBill_type(i);
            bill.setCard_id(i);
            bill.setDescription("adkjd-" + i);
            bill.setSection(2);
            mDetailList.add(bill);
        }
       for (int i = 0; i < 2; i++) {
            Bill bill = new Bill();
            bill.setBill_date(new Date());
            bill.setBill_type(i);
            bill.setCard_id(i);
            bill.setDescription("adkjd-" + i);
            bill.setSection(1);
            mDetailList.add(bill);
        }
    }
    
    View.OnClickListener tabClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            case R.id.detail:
                break;
            case R.id.reprot_forms:
                break;
            case R.id.account:
                break;
            default:
                break;
            }
            updateTabButton(view);
        }
    };
    
    private void updateTabButton(View view) {
        boolean tabDetail = false;
        boolean tabReportForms = false;
        boolean tabAccount = false;
        if (view.getId() == R.id.detail) {
            tabDetail = true;
        } else if (view.getId() == R.id.reprot_forms) {
            tabReportForms = true;
        } else if (view.getId() == R.id.account) {
            tabAccount = true;
        }

        mDetail.setTextColor(tabDetail ? Color.WHITE : getResources().getColor(
                R.color.detail_tab_bg));
        mDetail.setBackground(tabDetail ? getResources().getDrawable(
                R.drawable.tab_button_bg_1) : getResources().getDrawable(
                R.drawable.transparent));
        mReportForms.setTextColor(tabReportForms ? Color.WHITE : getResources()
                .getColor(R.color.detail_tab_bg));
        mReportForms.setBackgroundColor(tabReportForms ? getResources().getColor(
                R.color.detail_tab_bg) : Color.TRANSPARENT);
        mAccount.setTextColor(tabAccount ? Color.WHITE : getResources()
                .getColor(R.color.detail_tab_bg));
        mAccount.setBackground(tabAccount ? getResources().getDrawable(
                R.drawable.tab_button_bg_2) : getResources().getDrawable(
                R.drawable.transparent));
    }

}
