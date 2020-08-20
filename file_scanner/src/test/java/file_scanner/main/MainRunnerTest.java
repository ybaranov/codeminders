package file_scanner.main;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainRunnerTest {
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

    @Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void restoreStreams() {
	    System.setOut(originalOut);
	    System.setErr(originalErr);
	}
	
	@Test
	public void mainTest() {
		MainRunner.main(new String[] {"D:\\work\\repository\\codeminders\\file_scanner\\src\\test\\test_folder"});
	}
	
	
	@Test
	public void noArgsTest() {
		MainRunner.main(new String[] {});
		assertEquals("Message output is not as expected.", 
				"Application should have only one argument which is the file or catalog path.\r\n", outContent.toString());
	}
	
	@Test
	public void twoArgsTest() {
		MainRunner.main(new String[] {"1", "2"});
		assertEquals("Message output is not as expected.", 
				"Application should have only one argument which is the file or catalog path.\r\n", outContent.toString());
	}
	
	@Test
	public void rootFileDoesntExist() {
		MainRunner.main(new String[] {"D:\\work\\repository\\codeminders\\file_scanner\\src\\test\\test_folder\\no_file.java"});
		assertEquals("Message output is not as expected.", 
				"Root file doesn't exist.\r\n", outContent.toString());
	}
	
	@Test
	public void filePathIsCommandLinePar() {
		MainRunner.main(new String[] {"D:\\work\\repository\\codeminders\\file_scanner\\src\\test\\test_folder\\test_0.java"});
		assertEquals("Message output is not as expected.", 
				"test_0.java : 30\r\n", outContent.toString());
	}

	@Test
	public void catalogPathIsCommandLineParHasNoSubCatalog() {
		MainRunner.main(new String[] {"D:\\work\\repository\\codeminders\\file_scanner\\src\\test\\test_folder\\test_sub_folder"});
		assertEquals("Message output is not as expected.", 
				"test_1.java : 23\r\ntest_2.java : 40\r\n", outContent.toString());
	}
	
	@Test
	public void catalogPathIsCommandLineParHasSubCatalog() {
		MainRunner.main(new String[] {"D:\\work\\repository\\codeminders\\file_scanner\\src\\test\\test_folder"});
		assertEquals("Message output is not as expected.", 
				"test_sub_folder : 63\r\n    test_1.java : 23\r\n    test_2.java : 40\r\ntest_0.java : 30\r\n", outContent.toString());
	}
	
	@Test
	public void catalogPathIsCommandLineParSrcCatalog() {
		MainRunner.main(new String[] {"D:\\work\\repository\\codeminders\\file_scanner\\src"});
		assertEquals("Message output is not as expected.", 
				"main : 196\r\n" + 
				"    java : 196\r\n" + 
				"        file_scanner : 196\r\n" + 
				"            main : 26\r\n" + 
				"                MainRunner.java : 26\r\n" + 
				"            model : 90\r\n" + 
				"                PrintedLine.java : 90\r\n" + 
				"            process : 80\r\n" + 
				"                RootProcessor.java : 80\r\n" + 
				"test : 197\r\n" + 
				"    java : 104\r\n" + 
				"        file_scanner : 104\r\n" + 
				"            main : 104\r\n" + 
				"                MainRunnerTest.java : 104\r\n" + 
				"    test_folder : 93\r\n" + 
				"        test_sub_folder : 63\r\n" + 
				"            test_1.java : 23\r\n" + 
				"            test_2.java : 40\r\n" + 
				"        test_0.java : 30\r\n" + 
				"module-info.java : 2\r\n", outContent.toString());
	}
}
