package cn.bsd.learn.transfer.files;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.netease.async.library.EventBus;
import com.netease.async.library.Subscribe;
import com.netease.async.library.service.WebService;
import com.netease.async.library.util.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bsd.learn.transfer.files.adapter.TransferAdapter;
import cn.bsd.learn.transfer.files.bean.FileInfo;
import cn.bsd.learn.transfer.files.utils.Utils;

public class FileListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<FileInfo> list = new ArrayList<>();
    TransferAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        recyclerView = findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = findViewById(R.id.content_main);

        WebService.start(this);
        EventBus.getDefault().register(this);
        initRecyclerView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("netease >>> ", "onDestroy");
        WebService.stop(this);
        EventBus.getDefault().unregister(this);
    }

    // 显示确认对话框
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示:");
        builder.setMessage("确定全部删除吗？");
        builder.setPositiveButton("确定", (dialog, which) -> Utils.deleteAll());
        builder.show();
    }

    void initRecyclerView() {
        adapter = new TransferAdapter(this, list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        EventBus.getDefault().post(Constants.RxBusEventType.LOAD_BOOK_LIST);

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(() ->
                EventBus.getDefault().post(Constants.RxBusEventType.LOAD_BOOK_LIST));
    }

    // 获取apk信息
    private void handleApk(String path, long length, List<FileInfo> list) {
        FileInfo infoModel = new FileInfo();
        infoModel.setName(Utils.getFileName(path));
        infoModel.setPath(path);
        infoModel.setSize(Utils.getFileSize(length));
        if (list == null)
            this.list.add(infoModel);
        else
            list.add(infoModel);
    }

    @Subscribe()
    public void loadAppList(String str) {
        if (!str.equals(Constants.RxBusEventType.LOAD_BOOK_LIST)) return;
        Log.e("netease >>> ", "loadAppList:" + Thread.currentThread().getName());
        List<FileInfo> listArr = new ArrayList<>();
        File dir = Constants.DIR;
        if (dir.exists() && dir.isDirectory()) {
            File[] fileNames = dir.listFiles();
            if (fileNames != null) {
                for (File fileName : fileNames) {
                    handleApk(fileName.getAbsolutePath(), fileName.length(), listArr);
                }
            }
        }
        runOnUiThread(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
            list.clear();
            list.addAll(listArr);
            adapter.notifyDataSetChanged();
        });
    }

}
