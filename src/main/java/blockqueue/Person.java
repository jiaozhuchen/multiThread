package blockqueue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person implements Comparable {
    private String name;
    private Integer age;

    //重写compare方法 按年龄大小排序
    @Override
    public int compareTo(Object o) {
        if(!(o instanceof Person)) {
            throw new IllegalArgumentException();
        }
        return this.age.compareTo(((Person) o).getAge());
    }
}
