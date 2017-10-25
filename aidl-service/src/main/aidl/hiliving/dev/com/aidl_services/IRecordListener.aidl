// IRecordListener.aidl
package hiliving.dev.com.aidl_services;

// Declare any non-default types here with import statements
import hiliving.dev.com.aidl_services.IRecord;
interface IRecordListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
   void onReciveListener(String path);

   void onRecordStatusListener(in IRecord record);
}
