package com.amlan.attendez.Adapter;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.amlan.attendez.ClassDetail_Activity;
import com.amlan.attendez.Firebase.Class_Names;
import com.amlan.attendez.Firebase.Students_List;
import com.amlan.attendez.MainActivity;
import com.amlan.attendez.R;

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
        return new MyViewHolder(itemView,mActivity,mList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Class_Names mClass = mList.get(position);
        holder.subject_name.setText(mClass.getName_subject());
        holder.class_name.setText(mClass.getName_class());
        holder.total_students.setText("Students : " + mList.size());
        final String bg_position = mClass.getPosition_bg();

        switch (bg_position) {
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

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView class_name;
        public final TextView subject_name;
        public final TextView total_students;
        public ImageView imageView_bg;
        public RelativeLayout frameLayout;
        public CardView cardView;
        public Activity mActivity;
        ArrayList<Class_Names> mList;

        public MyViewHolder(@NonNull View itemView, Activity mActivity, ArrayList<Class_Names> mList) {
            super(itemView);
            class_name = itemView.findViewById(R.id.className_adapter);
            subject_name = itemView.findViewById(R.id.subjectName_adapter);
            imageView_bg = itemView.findViewById(R.id.imageClass_adapter);
            frameLayout = itemView.findViewById(R.id.frame_bg);
            cardView = itemView.findViewById(R.id.cardView_adapter);
            total_students = itemView.findViewById(R.id.totalStudents_adapter);;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ClassDetail_Activity.class);
                    intent.putExtra("theme", mList.get(getAdapterPosition()).getPosition_bg());
                    intent.putExtra("className", mList.get(getAdapterPosition()).getName_class());
                    intent.putExtra("subjectName", mList.get(getAdapterPosition()).getName_subject());
                    intent.putExtra("classroom_ID", mList.get(getAdapterPosition()).getCrId());
                    Pair<View, String> p1 = Pair.create((View) cardView, "ExampleTransition");
                    ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(mActivity, p1);
                    view.getContext().startActivity(intent, optionsCompat.toBundle());
                }
            });
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
