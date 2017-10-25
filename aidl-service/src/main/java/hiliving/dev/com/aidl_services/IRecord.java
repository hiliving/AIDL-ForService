package hiliving.dev.com.aidl_services;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Helloworld on 2017/10/25.
 */

public class IRecord implements Parcelable{
    private String path;

    protected IRecord(Parcel in) {
        path = in.readString();
    }

    public static final Creator<IRecord> CREATOR = new Creator<IRecord>() {
        @Override
        public IRecord createFromParcel(Parcel in) {
            return new IRecord(in);
        }

        @Override
        public IRecord[] newArray(int size) {
            return new IRecord[size];
        }
    };

    public IRecord(String s) {
       setPath(s);
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
    }
}
