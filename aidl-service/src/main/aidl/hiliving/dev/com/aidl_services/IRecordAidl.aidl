// IRecordAidl.aidl
package hiliving.dev.com.aidl_services;

// Declare any non-default types here with import statements
import hiliving.dev.com.aidl_services.IRecordListener;
interface IRecordAidl {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
   void startRecord();
   void stopRecord();
   void registListener(IRecordListener listener);
   void unregistListener(IRecordListener listener);
}
