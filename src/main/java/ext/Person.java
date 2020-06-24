package ext;

public class Person {
    private Long id;
    private String name;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println(" Person 被回收了");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
