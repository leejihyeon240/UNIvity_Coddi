package com.example.univity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder> {

    private ArrayList<String> startlistD = null ;
    private ArrayList<String> endlistD = null ;
    private ArrayList<String> steplistD = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView caldb_time_start ;
        TextView caldb_time_end ;
        TextView caldb_step ;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            caldb_time_start = itemView.findViewById(R.id.caldb_time_start) ;
            caldb_time_end = itemView.findViewById(R.id.caldb_time_end) ;
            caldb_step = itemView.findViewById(R.id.caldb_step) ;
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    SimpleTextAdapter(ArrayList<String> startlist,ArrayList<String> endlist,ArrayList<String> steplist) {
        startlistD = startlist ;
        endlistD = endlist ;
        steplistD = steplist ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public SimpleTextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false) ;
        SimpleTextAdapter.ViewHolder vh = new SimpleTextAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(SimpleTextAdapter.ViewHolder holder, int position) {
        String text1 = startlistD.get(position) ;
        holder.caldb_time_start.setText(text1) ;

        String text2 = endlistD.get(position) ;
        holder.caldb_time_end.setText(text2) ;

        String text3 = steplistD.get(position) ;
        holder.caldb_step.setText(text3) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return startlistD.size() ;
    }
}