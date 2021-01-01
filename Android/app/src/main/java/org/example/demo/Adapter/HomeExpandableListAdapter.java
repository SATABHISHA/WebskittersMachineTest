package org.example.demo.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.example.demo.Home.HomeActivity;
import org.example.demo.Model.KeyData;
import org.example.demo.Model.ObjectTitle;
import org.example.demo.R;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;

    ArrayList<ObjectTitle> objectTitleArrayList;
    private HashMap<ObjectTitle, ArrayList<KeyData>> keyDataArrayList;

    public HomeExpandableListAdapter(HomeActivity context, ArrayList<ObjectTitle> objectTitleArrayList,
                                     HashMap<ObjectTitle, ArrayList<KeyData>> keyDataArrayList) {
        this.context = (Context) context;
        this.objectTitleArrayList = objectTitleArrayList;
        this.keyDataArrayList = keyDataArrayList;
    }


    public int getGroupCount() {
        return objectTitleArrayList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.keyDataArrayList.get(this.objectTitleArrayList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.objectTitleArrayList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.keyDataArrayList.get(this.objectTitleArrayList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
//        String headerTitle = (String) getGroup(i);
        final ObjectTitle objectTitle = (ObjectTitle) getGroup(i);

        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = infalInflater.inflate(R.layout.listview_row_group, null);

        TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
        tv_title.setText(objectTitle.getContent_obj());

        return view;
    }



    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
//        final String childText = (String) getChild(i, i1);
        final KeyData keydata = (KeyData) getChild(i,i1);
        final ObjectTitle objectTitle = (ObjectTitle) getGroup(i);
        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = infalInflater.inflate(R.layout.listview_row, null);

        TextView tv_body;

        tv_body = (TextView)view.findViewById(R.id.tv_body);
        tv_body.setText(keydata.getKey_result());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
