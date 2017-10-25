package hiliving.dev.com.aidl_services;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by Helloworld on 2017/10/25.
 */

public class RecordPresenter {

    private static final int RECORD_REQUEST_CODE  = 101;
    private IRecordAidl mRemoteIRecordManager;
    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private Context context;
    private String TAG = "MainActivity";
    private boolean ifRunning= false;
//    private IRecordService recordService;
    private Iview iview;
    public RecordPresenter(Context context,Iview iview) {
        this.context = context;
        this.iview = iview;
    }

    public void bindServ() {
        Intent intent = new Intent(context, IRecordService.class);
        ifRunning = context.bindService(intent, connection, BIND_AUTO_CREATE);
    }
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IRecordAidl iRecordAidl = IRecordAidl.Stub.asInterface(service);
            mRemoteIRecordManager = iRecordAidl;

           /* IRecordService.RecordBinder binder = (IRecordService.RecordBinder) service;
            recordService = binder.getRecordService();*/

            try {
                mRemoteIRecordManager.registListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "绑定成功");
            iview.updateTitle();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteIRecordManager = null;
            Log.d(TAG, "绑定断开");

        }
    };
    /**
     * 服务回调本地方法
     */
    private IRecordListener listener = new IRecordListener.Stub() {

        @Override
        public void onReciveListener(String path) throws RemoteException {
            //返回字符串
           iview.updateResult(path);
        }

        @Override
        public void onRecordStatusListener(IRecord record) throws RemoteException {
            //返回序列化的对象
            iview.updateContent(record);
        }
    };

    public void startRecord() {
        if (ifRunning){
            try {
                mRemoteIRecordManager.startRecord();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
          /*  Intent captureIntent = projectionManager.createScreenCaptureIntent();
            ((Activity)context).startActivityForResult(captureIntent, RECORD_REQUEST_CODE);*/
        }else {
            Toast.makeText(context, "请先启动服务进程", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 调用独立进程的方法
     */
    public void stopRecord() {
        if (ifRunning){
            try {
                mRemoteIRecordManager.stopRecord();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(context, "请先启动服务进程", Toast.LENGTH_SHORT).show();
        }
    }

    public void destroy() {
        if (mRemoteIRecordManager != null && mRemoteIRecordManager.asBinder().isBinderAlive()) {
            try {
                mRemoteIRecordManager.unregistListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        try {
            context.unbindService(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "解除注册,绑定已断开");
    }

    public void stopRecord(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
//            recordService.setMediaProject(mediaProjection);
//            recordService.startRecord();
        }
    }
}
