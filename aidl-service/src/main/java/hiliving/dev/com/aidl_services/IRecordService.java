package hiliving.dev.com.aidl_services;

import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Binder;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Helloworld on 2017/10/25.
 */

public class IRecordService extends Service {
    private String TAG = "RecordService";
    private IRecordListener iRecordListener;
    private boolean running;
    private MediaProjection mediaProjection;
    private MediaRecorder mediaRecorder;
    private VirtualDisplay virtualDisplay;
    private MediaProjectionManager projectionManager;
    private int width = 720;
    private int height = 1080;
    private int dpi;
    private String fileName;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class RecordBinder extends Binder {
        public IRecordService getRecordService() {
            return IRecordService.this;
        }
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

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread serviceThread = new HandlerThread("service_thread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        serviceThread.start();
        running = false;
        mediaRecorder = new MediaRecorder();


    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("uuuuuuu","kkkkkkkkkkkk");
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        return START_STICKY;
    }

    public void setMediaProject(MediaProjection project) {
        mediaProjection = project;
    }

    public boolean isRunning() {
        return running;
    }

    public void setConfig(int width, int height, int dpi) {
        this.width = width;
        this.height = height;
        this.dpi = dpi;
    }

    public boolean startRecord() {
        if (mediaProjection == null || running) {
            return false;
        }

        initRecorder();
        createVirtualDisplay();
        mediaRecorder.start();
        running = true;
        return true;
    }
    public boolean stopRecord() {
        if (!running) {
            return false;
        }
        running = false;
        mediaRecorder.stop();
        mediaRecorder.reset();
        virtualDisplay.release();
        mediaProjection.stop();

        return true;
    }
    private void initRecorder() {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        fileName = System.currentTimeMillis()+"";
        mediaRecorder.setOutputFile(getsaveDirectory() + fileName + ".mp4");
        mediaRecorder.setVideoSize(width, height);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
        mediaRecorder.setVideoFrameRate(30);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getsaveDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ScreenRecord" + "/";

            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }


            return rootDir;
        } else {
            return null;
        }
    }
    private void createVirtualDisplay() {
        virtualDisplay = mediaProjection.createVirtualDisplay("MainScreen", width, height, dpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(), null, null);
    }
    public String getFileName() {
        return fileName;
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
