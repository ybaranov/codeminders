package file_scanner.process;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import file_scanner.model.PrintedLine;

public class RootProcessor {
	
	public void processRoot(Path rootPath, File root) {
		if (root.isFile()) {
			PrintedLine pl = new PrintedLine(rootPath, 0);
			try {
				pl.setLineNumber(Files.lines(pl.getPath()).count());
			} catch (IOException ex) {
				ex.printStackTrace();
				throw new IllegalStateException(ex);
			}
			System.out.println(pl);
		} else if (root.isDirectory()) {
			List<PrintedLine> lines = getTopLines(rootPath);
			lines.forEach(this::buildChildren);
			lines.forEach(this::calcCatalogTotals);
			lines.stream().forEach(System.out::println);
		}
	}
	
	private List<PrintedLine> getTopLines(Path rootPath) {
		Stream<PrintedLine> lines;
		try {
			lines = getChildLinesStream(rootPath, 0);
			return lines.collect(Collectors.toList());
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		}
	}
	
	private void buildChildren(PrintedLine pl) {
		Stream<PrintedLine> childLines;
		try {
			if (pl.isFile()) {
				pl.setLineNumber(Files.lines(pl.getPath()).count());
			}
			if (pl.isDirectory()) {
				childLines = getChildLinesStream(pl.getPath(), pl.getSpacePrefixCount());
				pl.setChildren(childLines.collect(Collectors.toList()));
				pl.getChildren().stream().forEach(pl0 -> buildChildren(pl0));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		}
	}
	
	private void calcCatalogTotals(PrintedLine pl) {
		if (pl.isDirectory()) {
			pl.getChildren().stream().forEach(pl0 -> calcCatalogTotals(pl0));
			pl.setLineNumber(pl.getChildren()
					.stream().map(pl0 -> pl0.getLineNumber()).reduce(0l, Long::sum));
		}
	}
	
	private Stream<PrintedLine> getChildLinesStream(Path path, int spacePrefixCount) throws IOException {
	    return Files.list(path)
	    		.map(path0 -> new PrintedLine(path0, spacePrefixCount + 1))
	    		.sorted((pl0, pl1) -> {
					if ((pl0.isDirectory() && pl1.isDirectory()) || (pl0.isFile() && pl1.isFile())) {
						return pl0.getName().compareTo(pl1.getName());
					} else {
						return pl1.isDirectory().compareTo(pl0.isDirectory());
					}
				});
	}
}
