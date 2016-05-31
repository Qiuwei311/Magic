package com.five.bill.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.five.bill.R;
import com.five.bill.bean.Bill;
import com.five.bill.utils.DateUtils;

public class BillSectionedAdapter extends SectionedBaseAdapter {
    
    private Context mContext;
    private List<Bill> mDataList = null;
    private ArrayList<String> mSections = new ArrayList<String>();
    private Map<String, ArrayList<Bill>> mSectionMap = new HashMap<String, ArrayList<Bill>>();
    private String[] sectionStrings = {"本月", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
    
    public BillSectionedAdapter(Context context, List<Bill> list) {
        mContext = context;
        mDataList = list;
        formatSectionList(list);
    }

    @Override
    public Bill getItem(int section, int position) {
        return mSectionMap.get(String.valueOf(section)).get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return mSectionMap.size();
    }

    @Override
    public int getCountForSection(int section) {
        if (mSectionMap.get(String.valueOf(section)) != null) {
            return mSectionMap.get(String.valueOf(section)).size();
        }
        return 0;
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.list_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.textItem)).setText(DateUtils.dateToString("MM-dd", getItem(section, position).getBill_date()));
        return layout;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.textItem)).setText(sectionStrings[section]);
        return layout;
    }
    
    private void formatSectionList(List<Bill> list) {
        for (int i = 1; i < 13; i++) {
            mSectionMap.put(String.valueOf(i), new ArrayList<Bill>());
        }
        
        for (Bill bill : list) {
            mSectionMap.get(String.valueOf(bill.getSection())).add(bill);
        }
    }

}
