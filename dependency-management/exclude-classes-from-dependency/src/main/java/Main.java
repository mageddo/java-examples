import org.apache.commons.lang3.StringUtils;

public class Main {
	public static void main(String[] args) {
		if(args.length == 0){
			args = new String[]{"Hello World!!!"};
		}
		System.out.println(StringUtils.upperCase(args[0]));
	}
}
