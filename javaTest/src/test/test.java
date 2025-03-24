package test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class test {
	public static void main(String[] args) {
		Path path = Paths.get("D;//tmp...");
		System.out.println(path.normalize().toString());
	}
}
