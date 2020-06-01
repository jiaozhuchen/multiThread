package atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 测试AtomicStampedReference的使用，可以看到当期望的版本号和实际版本号不一致时，是无法更新成功的
 */
public class AtomicStampedReferenceTest {

    public static void main(String[] args) {
        String oldString = "oldString";
        String newString = "newString";

        AtomicStampedReference<String> reference = new AtomicStampedReference<>(oldString, 1);
        reference.compareAndSet(oldString, newString, reference.getStamp(), reference.getStamp() + 1);

        System.out.println("reference 值：" + reference.getReference());


        boolean flag = reference.attemptStamp(newString, reference.getStamp() + 1);
        System.out.println("版本号是否更新成功: " + flag);
        System.out.println("版本号值：" + reference.getStamp());

        boolean updateFlag = reference.compareAndSet(newString, oldString, 4, reference.getStamp() + 1);
        System.out.println("reference 值：" + reference.getReference());
        System.out.println("reference是否更新成功：" + updateFlag);
    }
}
