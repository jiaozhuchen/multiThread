package blockqueue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
public class DelayItem implements Delayed {
    private Integer age;
    private Long currentTime;

    public DelayItem(Integer age) {
        this.age = age;
        this.currentTime = System.currentTimeMillis();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        //延迟100毫秒
        return System.currentTimeMillis() - (currentTime + 100);
    }

    @Override
    public int compareTo(Delayed o) {
        //以年龄排序
        if (!(o instanceof DelayItem)) {
            throw new IllegalArgumentException();
        }
        return this.age.compareTo(((DelayItem) o).getAge());
    }
}
