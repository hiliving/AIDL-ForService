package hiliving.dev.com.aidl_services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Helloworld on 2017/10/25.
 */

public class IRecordService extends Service {
    private String TAG = "RecordService";
    private IRecordListener iRecordListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    private IBinder mBinder = new IRecordAidl.Stub(){
        @Override
        public void startRecord() throws RemoteException {
            startRecords();
        }

        @Override
        public void stopRecord() throws RemoteException {
            stopRecords();
        }

        @Override
        public void registListener(IRecordListener listener) throws RemoteException {
           iRecordListener = listener;
        }

        @Override
        public void unregistListener(IRecordListener listener) throws RemoteException {
            iRecordListener = null;
        }
    };

    /**
     * 停止录制的操作
     */
    private void stopRecords() {
        if (iRecordListener!=null){
            try {
                IRecord iRecord = new IRecord(Parcel.obtain());
                iRecord.setPath("Sdcard/parcel.mp4");
                iRecordListener.onRecordStatusListener(iRecord);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开始录制的操作
     */
    private void startRecords() {
       if (iRecordListener!=null){
           try {
               iRecordListener.onReciveListener("sdcard/text.mp4");
           } catch (RemoteException e) {
               e.printStackTrace();
           }
       }
    }


}
