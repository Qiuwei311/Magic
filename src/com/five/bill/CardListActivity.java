package com.five.bill;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.five.bill.bean.CardInfo;

public class CardListActivity extends BaseActivity {

	private TextView mAddCard;
	protected LinearLayout mScrollContentView;
	private List<CardInfo> mCardLists = new ArrayList<CardInfo>();
	
	private static final int REQUEST_ADD_CODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mMainContentView.setBackgroundColor(Color.parseColor("#273753"));
		mLeftImageView.setOnClickListener(clickListener);
		mRightImageView.setOnClickListener(clickListener);
		mLeftTextView.setOnClickListener(clickListener);

		initContentView();
	}

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.add_card:
				addNewCard();
				break;
			case R.id.left_icon:
			case R.id.left_action:
				finish();
				break;
			case R.id.right_action:
				// startActivity();
				break;
			default:
				break;
			}
		}
	};

	private void addNewCard() {
		Intent intent = new Intent();
		intent.setClass(CardListActivity.this, CardAddActivity.class);
		startActivityForResult(intent, REQUEST_ADD_CODE);
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mScrollContentView.removeAllViews();
            loadCardListFormDB();
            initCardListViews(mCardLists);
        }
    }
	
	private void loadCardListFormDB() {
        Cursor cursor = getContentResolver().query(CARD_INFO_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            CardInfo info = new CardInfo();
            info.setCard_id(cursor.getInt(cursor.getColumnIndex("card_id")));
            info.setBank_name(cursor.getString(cursor.getColumnIndex("bank_name")));
            info.setCard_number(cursor.getString(cursor.getColumnIndex("card_number")));
            info.setCurrent_debt(cursor.getInt(cursor.getColumnIndex("current_debt")));
            info.setPayment_date(cursor.getString(cursor.getColumnIndex("payment_date")));
            info.setSettled_bills(cursor.getInt(cursor.getColumnIndex("settled_bills")));
            info.setUnsettled_bills(cursor.getInt(cursor.getColumnIndex("unsettled_bills")));
            
            mCardLists.add(info);
        }
        cursor.close();
	}

	private void initCardListViews(List<CardInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            View child = createCardView(list.get(i), i);
            mScrollContentView.addView(child, 0);
        }
    }

	private void initContentView() {
		View contentView = LayoutInflater.from(this).inflate(
		        R.layout.activity_card_list, null);
		mScrollContentView = (LinearLayout) contentView
		        .findViewById(R.id.scroll_content);
		mMainContentView.addView(contentView);

		mAddCard = (TextView) contentView.findViewById(R.id.add_card);
		mAddCard.setOnClickListener(clickListener);

		loadCardListFormDB();
		initCardListViews(mCardLists);
	}

	@SuppressLint("InflateParams")
	private View createCardView(final CardInfo info, final int index) {
		View view = LayoutInflater.from(this).inflate(
		        R.layout.costom_card_item, null);

		((TextView) view.findViewById(R.id.bank_name)).setText(info
		        .getBank_name());
		String fullNumber = info.getCard_number();
		String number = "* * * *  * * * *  * * * *  " + fullNumber.substring(fullNumber.length() - 4, fullNumber.length());
	    ((TextView) view.findViewById(R.id.card_number)).setText(number);
        ((TextView) view.findViewById(R.id.payment_date)).setText(info
                .getPayment_date());    
		((TextView) view.findViewById(R.id.current_debt)).setText(Double
		        .toString(info.getSettled_bills()));
		((TextView) view.findViewById(R.id.unsettled_bills)).setText("未出："
		        + info.getUnsettled_bills());
		((TextView) view.findViewById(R.id.settled_bills)).setText("已出："
		        + info.getSettled_bills());
//		RelativeLayout detailLayout = (RelativeLayout) view
//		        .findViewById(R.id.detail_layout);
//		detailLayout.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//				Intent intent = new Intent();
//				intent.setClass(CardListActivity.this,
//				        CardDetailsActivity.class);
//				startActivity(intent);
//			}
//		});

		LinearLayout.LayoutParams params = new LayoutParams(
		        LayoutParams.MATCH_PARENT, 320);
		params.leftMargin = 16;
		params.rightMargin = 16;
		params.topMargin = 16;
		view.setLayoutParams(params);
		view.setTag(index);
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(CardListActivity.this,
				        CardDetailsActivity.class);
				intent.putExtra("card_info", mCardLists.get(index));
				startActivity(intent);
			}
		});
		return view;
	}
}
