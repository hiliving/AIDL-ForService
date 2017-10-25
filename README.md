### 单应用中基于AIDL的进程间通信示例

#### 平常看到网上各种AIDL的教程大都是做了连个app来演示，一个服务端，一个客户端，这里我演示了如何在一个APP内发起新的进程，同时与其他页面通信。

##### AS内新建AIDL代码非常简单。关键步骤如下：
* 新建ALDL文件  
* 新建服务
* 配置接口监听
* 绑定服务，启动新进程

### ！需要注意的是，AIDL文件不能自动import外部文件，需要手动填写你引用到的类文件。
效果图如下：

 ![aidl-service](https://github.com/hiliving/AIDL-ForService/blob/master/screen.gif)