
package file_scanner.model;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrintedLine {
	
	private static String PREFIX = "    ";
	
	private final Path path;
	private final String name;
	private Boolean isFile;
	private Boolean isDirectory;
	private Long lineNumber = 0l;
	private List<PrintedLine> children = new LinkedList<>();
	private int spacePrefixCount = 0;

	public PrintedLine(Path path, int spacePrefixCount) {
		this.path = path;
		final File file = path.toFile();
		this.name = file.getName();
		this.isDirectory = file.isDirectory();
		this.isFile = file.isFile();
		this.spacePrefixCount = spacePrefixCount;
	}

	public Long getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name + " : " + lineNumber);
		if (this.isDirectory) {
			for(PrintedLine pl : children) {
				sb.append("\r\n");
				sb.append(Stream.generate(() -> PREFIX).limit(spacePrefixCount).collect(Collectors.joining()));
				sb.append(pl.toString());
			}
		}
		return sb.toString();
	}

	public Boolean isFile() {
		return isFile;
	}

	public void setFile(Boolean isFile) {
		this.isFile = isFile;
	}

	public Boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(Boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public List<PrintedLine> getChildren() {
		return children;
	}

	public void setChildren(List<PrintedLine> children) {
		this.children = children;
	}

	public Path getPath() {
		return path;
	}

	public int getSpacePrefixCount() {
		return spacePrefixCount;
	}
	
	
}
