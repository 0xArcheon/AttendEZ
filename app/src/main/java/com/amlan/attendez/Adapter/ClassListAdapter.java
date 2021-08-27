package com.amlan.attendez.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.amlan.attendez.Firebase.Class_Names;
import com.amlan.attendez.R;
import com.amlan.attendez.realm.Students_List;
import com.amlan.attendez.viewholders.ViewHolder;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.MyViewHolder> {

    ArrayList<com.amlan.attendez.Firebase.Class_Names> mList;
    private final Activity mActivity;
    /* RealmResults<Class_Names> mList;
      private final Activity mActivity;
    Realm realm;
    RealmChangeListener realmChangeListener; */

    public ClassListAdapter(ArrayList<Class_Names> mList, Activity mActivity) {
        this.mList = mList;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Class_Names mClass = mList.get(position);
        holder.ClassName.setText(mClass.getName_subject());
        holder.CourseName.setText(mClass.getName_class());
        holder.StudentCount.setText("Students : " + mList.size());
        final String bg_position = mClass.getPosition_bg();

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ClassName, CourseName, StudentCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ClassName = itemView.findViewById(R.id.subjectName_adapter);
            CourseName = itemView.findViewById(R.id.className_adapter);
            StudentCount = itemView.findViewById(R.id.totalStudents_adapter);

        }
    }

    /*
    public ClassListAdapter(RealmResults<Class_Names> list, Activity context) {

        super(context, list, true);
        Realm realm = Realm.getDefaultInstance();
        mActivity = context;
        mList = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_adapter, parent, false);
        return new ViewHolder(itemView, mActivity, mList);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Class_Names temp = getItem(position);

        Realm.init(mActivity);
        realm = Realm.getDefaultInstance();
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                long count = realm.where(Students_List.class)
                        .equalTo("class_id", temp.getId())
                        .count();
                holder.total_students.setText("Students : " + count);
            }
        };
        realm.addChangeListener(realmChangeListener);

        long count = realm.where(Students_List.class)
                .equalTo("class_id", temp.getId())
                .count();
        holder.total_students.setText("Students : " + count);
        holder.class_name.setText(temp.getName_class());
        holder.subject_name.setText(temp.getName_subject());
        switch (temp.getPosition_bg()) {
            case "0":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_paleblue);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_1);
                break;
            case "1":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_green);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_2);
                break;
            case "2":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_yellow);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_3);
                break;
            case "3":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_palegreen);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_4);
                break;
            case "4":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_paleorange);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_5);
                break;
            case "5":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_white);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_6);
                holder.subject_name.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.text_color_secondary));
                holder.class_name.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.text_color_secondary));
                holder.total_students.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.text_color_secondary));
                break;
        }

    } */
}
