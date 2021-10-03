package framework.core.support;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileUtils {

    public static List<File> getFiles(String path) {
        List<File> filesList = new ArrayList<>();
        final File file = new File(path);
        if (file.isDirectory()) {
            recurrence(filesList, file);
        } else {
            filesList.add(file);
        }
        return filesList;
    }

    private static void recurrence(List<File> filesList, File f) {
        File[] list = f.listFiles();
        assert list != null;
        for (File file : list) {
            if (file.isDirectory()) {
                recurrence(filesList, file);
            } else {
                filesList.add(file);
            }
        }
    }
}
