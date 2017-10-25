package hiliving.dev.com.aidl_services;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.txparcemsg)
    TextView txparcemsg;
    private String TAG = "MainActivity";
    @BindView(R.id.btbind)
    Button btbind;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.txmsg)
    TextView txmsg;
    @BindView(R.id.start)
    Button start;
    @BindView(R.id.stop)
    Button stop;
    private IRecordAidl mRemoteIRecordManager;
    /**
     * 服务回调本地方法
     */
    private IRecordListener listener = new IRecordListener.Stub() {

        @Override
        public void onReciveListener(String path) throws RemoteException {
            //返回字符串
            txmsg.setText("收到进程的String返回信息：文件路径为" + path);
        }

        @Override
        public void onRecordStatusListener(IRecord record) throws RemoteException {
            //返回序列化的对象
            txparcemsg.setText("收到进程的Parceble返回信息：文件路径为" + record.getPath());
        }
    };
    private boolean ifRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        btbind.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btbind:
                //启动独立进程
                bindServices();
                break;
            case R.id.start:
                //调用独立进程内方法
                if (ifRunning){
                    try {
                        mRemoteIRecordManager.startRecord();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(this, "请先启动服务进程", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.stop:
                //调用独立进程内方法
                if (ifRunning){
                    try {
                        mRemoteIRecordManager.stopRecord();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(this, "请先启动服务进程", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void bindServices() {
        Intent intent = new Intent(MainActivity.this, IRecordService.class);
        ifRunning = bindService(intent, connection, BIND_AUTO_CREATE);
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IRecordAidl iRecordAidl = IRecordAidl.Stub.asInterface(service);
            mRemoteIRecordManager = iRecordAidl;

            try {
                mRemoteIRecordManager.registListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "绑定成功");
            textView.setTextColor(Color.GREEN);
            textView.setText("新进程状态：运行中");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteIRecordManager = null;
            Log.d(TAG, "绑定断开");

        }
    };

    /**
     * 一定要在这解除注册，避免内存泄漏
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRemoteIRecordManager != null && mRemoteIRecordManager.asBinder().isBinderAlive()) {
            try {
                Log.e(TAG, "解除注册");
                mRemoteIRecordManager.unregistListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        try {
            unbindService(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "绑定断开Destroy");
    }
}
