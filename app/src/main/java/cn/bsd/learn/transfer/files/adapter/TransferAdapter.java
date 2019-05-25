package cn.bsd.learn.transfer.files.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.bsd.learn.transfer.files.R;
import cn.bsd.learn.transfer.files.bean.FileInfo;

public class TransferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<FileInfo> list;

    public TransferAdapter(Context context, List<FileInfo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.adapter_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder holder = (MyViewHolder) viewHolder;
        FileInfo infoModel = list.get(position);
        holder.mTvAppName.setText(infoModel.getName());
        holder.mTvAppSize.setText(infoModel.getSize());
        holder.mTvAppPath.setText(infoModel.getPath());
    }

    @Override
    public int getItemCount() {
        return list.size() > 0 ? list.size() : 0;

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTvAppName;
        TextView mTvAppSize;
        TextView mTvAppPath;

        MyViewHolder(View view) {
            super(view);
            mTvAppName = view.findViewById(R.id.tv_name);
            mTvAppSize = view.findViewById(R.id.tv_size);
            mTvAppPath = view.findViewById(R.id.tv_path);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0) {
            return 1;
        }
        return super.getItemViewType(position);
    }
}
