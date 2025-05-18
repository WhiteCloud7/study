package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test2 {
	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("article:"+1+":.+");
	    Matcher matcher = pattern.matcher("article:12:21");
	    System.out.println(matcher.matches());
	}

}
