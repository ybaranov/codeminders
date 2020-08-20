package file_scanner.main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import file_scanner.process.RootProcessor;

public class MainRunner {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Application should have only one argument which is the file or catalog path.");
			return;
		}
		
		Path rootPath = Paths.get(args[0]);
		File root = rootPath.toFile();
		if (!root.exists()) {
			System.out.println("Root file doesn't exist.");
			return;
		}
		new RootProcessor().processRoot(rootPath, root);
	}
	
}
