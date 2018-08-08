package basic;

import java.io.File;

public class CommonTool {
	
	/**
     * Make sure the directory path end up with 'File.seperator'
     * Only when the input dir equals "", it does not end with 'File.seperator'
     * @param inputDir
     * @return
     */
    public static String formatDirWithSep(String inputDir) {
        String outputDir = inputDir;
        if (inputDir.equals("")) {
            return outputDir;
        }
        if (inputDir.lastIndexOf(File.separator) != inputDir.length() - 1) {
            outputDir = inputDir + File.separator;
        }
        return outputDir;
    }

}
