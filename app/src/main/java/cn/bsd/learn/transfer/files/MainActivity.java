package cn.bsd.learn.transfer.files;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.netease.async.library.service.WifiConnectChangedReceiver;
import com.netease.async.library.util.Constants;
import com.netease.async.library.util.WifiUtils;

public class MainActivity extends AppCompatActivity {

    private TextView ipTv;
    private WifiConnectChangedReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipTv = findViewById(R.id.ip);
        receiver = new WifiConnectChangedReceiver();

        registerReceiver();
        checkWifiState(WifiUtils.getWifiConnectState(this));
    }

    // 检查网络，显示wifi连接的详细ip + port
    void checkWifiState(NetworkInfo.State state) {
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            if (state == NetworkInfo.State.CONNECTED) {
                String ip = WifiUtils.getWifiIp(this);
                if (!TextUtils.isEmpty(ip)) {
                    ipTv.setText(String.format(getString(R.string.http_address), ip, Constants.HTTP_PORT));
                }
            }
        }
    }

    // 注册广播监听
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(receiver, intentFilter);
    }

    // 解除广播注册
    private void unregisterReceiver() {
        unregisterReceiver(receiver);
    }

    public void jump(View view) {
        if (requestPermission()) {
            startActivity(new Intent(this, FileListActivity.class));
        }
    }

    String[] perms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    // 运行时权限申请
    private boolean requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean needapply = false;
            for (String perm : perms) {
                int chechpermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                        perm);
                if (chechpermission != PackageManager.PERMISSION_GRANTED) {
                    needapply = true;
                }
            }
            if (needapply) {
                ActivityCompat.requestPermissions(this, perms, 1);
            }
            return !needapply;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }
}
