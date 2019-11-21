package pojo;

import com.mageddo.processor.Immutable;

@Immutable
public class Fruit {

    private String name;

    public Fruit(final String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
