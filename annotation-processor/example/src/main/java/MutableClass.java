import com.mageddo.processor.Immutable;

@Immutable
public class MutableClass {

    private String name;

    public MutableClass( final String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
//        new MutableClass("x").name = "x";
//        System.out.println("hi");
    }
}
