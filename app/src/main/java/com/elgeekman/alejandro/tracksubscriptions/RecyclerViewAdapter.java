package com.elgeekman.alejandro.tracksubscriptions;

/**
 * Created by alejandro on 17/11/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alejandro on 17/11/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elgeekman.alejandro.tracksubscriptions.R;
import com.elgeekman.alejandro.tracksubscriptions.StudentDetails;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<StudentDetails> MainImageUploadInfoList;

    public RecyclerViewAdapter(Context context, List<StudentDetails> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        StudentDetails studentDetails = MainImageUploadInfoList.get(position);

        holder.StudentNameTextView.setText(studentDetails.getStudentName());

        holder.StudentNumberTextView.setText(studentDetails.getStudentPhoneNumber());

        holder.MonedaTextView.setText(studentDetails.getMoneda());

        holder.FechaTextView.setText(studentDetails.getFecha());

        holder.ColorTextView.setText(studentDetails.getColor());

        holder.rtl.setBackgroundColor(Color.parseColor(studentDetails.getColor()));

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView StudentNameTextView;
        public TextView StudentNumberTextView;
        public TextView MonedaTextView;
        public TextView FechaTextView;
        public TextView ColorTextView;

        public RelativeLayout rtl;

        public ViewHolder(View itemView) {

            super(itemView);

            StudentNameTextView = (TextView) itemView.findViewById(R.id.ShowStudentNameTextView);

            StudentNumberTextView = (TextView) itemView.findViewById(R.id.ShowStudentNumberTextView);

            MonedaTextView = (TextView) itemView.findViewById(R.id.txt_moneda);

            FechaTextView = (TextView) itemView.findViewById(R.id.txt_fecha);

            ColorTextView = (TextView) itemView.findViewById(R.id.txt_color);

            rtl = (RelativeLayout) itemView.findViewById(R.id.rtl);

        }
    }
}
