package com.linkedpipes.etl.test.dataunit;

import com.linkedpipes.etl.dataunit.core.files.FilesDataUnit;
import com.linkedpipes.etl.dataunit.core.files.WritableFilesDataUnit;
import com.linkedpipes.etl.executor.api.v1.LpException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class TestFilesDataUnit
        implements FilesDataUnit, WritableFilesDataUnit, Checkable {

    private static final Logger LOG =
            LoggerFactory.getLogger(TestFilesDataUnit.class);

    private final File directory;

    public TestFilesDataUnit(File directory) {
        this.directory = directory;
    }

    @Override
    public File createFile(String fileName) throws LpException {
        final File file = new File(directory, fileName);
        if (file.exists()) {
            throw new LpException("File already exists!");
        }
        return file;
    }

    @Override
    public File getWriteDirectory() {
        return directory;
    }

    @Override
    public Collection<File> getReadDirectories() {
        return Arrays.asList(directory);
    }

    @Override
    public long size() {
        return listFiles(directory).size();
    }

    @Override
    public Iterator<Entry> iterator() {
        final Iterator<File> files = listFiles(directory).iterator();
        return new Iterator<Entry>() {
            @Override
            public boolean hasNext() {
                return files.hasNext();
            }

            @Override
            public Entry next() {
                final File file = files.next();
                return new Entry() {

                    @Override
                    public File toFile() {
                        return file;
                    }

                    @Override
                    public String getFileName() {
                        return directory.toPath().relativize(
                                file.toPath()).toString();
                    }
                };
            }
        };
    }

    private static List<File> listFiles(File rootDirectory) {
        final List<File> files = new LinkedList<>();
        try {
            Files.walkFileTree(rootDirectory.toPath(),
                    new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file,
                                BasicFileAttributes attributes)
                                throws IOException {
                            files.add(file.toFile());
                            return super.visitFile(file, attributes);
                        }
                    });
        } catch (IOException ex) {
            throw new RuntimeException("Can't iterate files.", ex);
        }
        return files;
    }

    @Override
    public boolean checkContent(File expectedContent) {
        boolean match = true;
        List<File> actual = listFiles(directory);
        List<File> expected = listFiles(expectedContent);
        if (actual.size() != expected.size()) {
            match = false;
            LOG.error("File count does not match actual: {} expected: {}",
                    actual.size(), expected.size());
        }
        for (File actualFile : actual) {
            String fileName = directory.toPath().relativize(
                    actualFile.toPath()).toString();
            File expectedFile = new File(expectedContent, fileName);
            if (!expectedFile.exists()) {
                LOG.error("Missing file: {}");
                match = false;
            }
            //
            try {
                if (!FileUtils.contentEquals(actualFile, expectedFile)) {
                    match = false;
                    LOG.error("Content does not match for: {}", fileName);
                }
            } catch (IOException ex) {
                match = false;
                LOG.error("Can't compare files: {} {}",
                        actualFile, expectedFile);
            }
        }
        return match;
    }

}
