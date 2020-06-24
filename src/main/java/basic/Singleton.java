package basic;

/**
 * DCL + volatile 实现单例
 */
public class Singleton {

    /**
     * 为什么使用volatile 防止指令重排，因为Java新建对象分为三步
     *  1。在内存中开辟一块地址
     *  2。对象初始化
     *  3。将指针指向这块内存地址
     */
    private static volatile Singleton singleton;
    private Singleton() {
    }

    public static Singleton getInstance() {
        if(singleton == null) {
            synchronized (Singleton.class) {
                if(singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
