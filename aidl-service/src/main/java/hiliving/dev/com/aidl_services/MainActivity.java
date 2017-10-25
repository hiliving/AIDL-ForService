package hiliving.dev.com.aidl_services;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,Iview {
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
    private RecordPresenter recordPresenter;


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

        initRecordPresenter();
    }

    private void initRecordPresenter() {
        recordPresenter = new RecordPresenter(this,this);
        recordPresenter.bindServ();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btbind:
                //启动独立进程
                recordPresenter.bindServ();
                break;
            case R.id.start:
                //调用独立进程内方法
                recordPresenter.startRecord();
                break;
            case R.id.stop:
                //调用独立进程内方法
                recordPresenter.stopRecord();
                break;
        }
    }

    /**
     * 一定要解除注册，避免内存泄漏
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        recordPresenter.destroy();
    }

    @Override
    public void updateStatus() {

    }

    @Override
    public void updateTitle() {
        textView.setTextColor(Color.GREEN);
        textView.setText("新进程状态：运行中");
    }

    @Override
    public void updateResult(String path) {
        txmsg.setText("收到进程的String返回信息：文件路径为" + path);
    }

    @Override
    public void updateContent(IRecord record) {
        txparcemsg.setText("收到进程的Parceble返回信息：文件路径为" + record.getPath());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        recordPresenter.stopRecord(requestCode, resultCode, data);
    }
}
