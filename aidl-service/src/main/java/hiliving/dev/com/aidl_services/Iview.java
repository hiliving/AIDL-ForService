package hiliving.dev.com.aidl_services;

/**
 * Created by Helloworld on 2017/10/25.
 */

public interface Iview {
    void updateStatus();

    void updateTitle();

    void updateResult(String path);

    void updateContent(IRecord record);
}
