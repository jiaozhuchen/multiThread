package ext;

import java.lang.ref.WeakReference;

/**
 * 测试弱引用的使用
 *  如果对象只有一个软引用指向它，垃圾回收时就会回收掉该对象
 *      例子中p强引用指向一个，这个是强的不会回收  但WeakReference对象有个弱引用引用Person对象，当垃圾回收时，回收Person对象
 */
public class WeakReferenceTest {

    public static void main(String[] args) {
        WeakReference<Person> p = new WeakReference<>(new Person());
        System.out.println(p.get());
        System.gc();
        System.out.println(p.get());
    }
}
