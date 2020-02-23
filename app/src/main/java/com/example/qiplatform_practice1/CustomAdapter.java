package com.example.qiplatform_practice1;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Dictionary> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView sensorid;
        protected TextView macaddress;



        public CustomViewHolder(View view) {
            super(view);
            this.sensorid = (TextView) view.findViewById(R.id.sensorid_txt);
            this.macaddress = (TextView) view.findViewById(R.id.mac_txt);
//            this.korean = (TextView) view.findViewById(R.id.korean_listitem);


        }


    }


    public CustomAdapter(ArrayList<Dictionary> list) {
        this.mList = list;
    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.sensorid.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.macaddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
//        viewholder.korean.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.sensorid.setGravity(Gravity.CENTER);
        viewholder.macaddress.setGravity(Gravity.CENTER);
//        viewholder.korean.setGravity(Gravity.CENTER);



        viewholder.sensorid.setText(mList.get(position).getSensorid());
        viewholder.macaddress.setText(mList.get(position).getMacaddress());
//        viewholder.korean.setText(mList.get(position).getKorean());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}