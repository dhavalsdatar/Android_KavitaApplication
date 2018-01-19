package personal.dhaval.kavita.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ApplicationUtils {

    private ApplicationUtils() {
        // private constructor
    }

    public static List<String> getFileNames() {
        List<String> fileNames = new ArrayList();
        File file = new File(ApplicationConstants.kavitaFolder);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : file.listFiles()) {
                    String filename = FilenameUtils.getBaseName(f.getName());
                    fileNames.add(filename);
                }
            }
        }
        return fileNames;
    }

    public static void emailContent(Context context, String filename) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "कविता : " + filename);
        intent.putExtra(Intent.EXTRA_TEXT, readFileContent(filename));

        context.startActivity(Intent.createChooser(intent, "Share " + filename + " by Email"));
    }

    public static void whatsappContent(Context context, String filename) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "*" + filename + "*" + System.lineSeparator() + readFileContent(filename));
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        context.startActivity(sendIntent);
    }

    public static void createFolder() {
        File directory = new File(ApplicationConstants.kavitaFolder);
        directory.mkdirs();
    }

    public static void clearFolder() {
        try {
            File directory = new File(ApplicationConstants.kavitaFolder);
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                for (File f : files) {
                    f.delete();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in clean directory");
        }
    }

    public static String readFileContent(String fileName) {
        String filePath = ApplicationConstants.kavitaFolder + fileName + ApplicationConstants.fileExtension;
        File file = new File(filePath);
        StringBuilder output = new StringBuilder();
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String content = fileScanner.nextLine();
                output.append(content + System.lineSeparator());
            }
        } catch (Exception e) {
            Log.d("readFileContent", "No file found : " + filePath);
        }
        Log.d("readFileContent", output.toString());
        return output.toString();
    }

    public static void deleteFile(String filename) {
        String filePath = ApplicationConstants.kavitaFolder + filename + ApplicationConstants.fileExtension;
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                Log.d("deleteFile", "File deleted");
            } else {
                Log.d("deleteFile", "File could not be deleted");
            }
        }
    }

    public static String getFileDate(String fileName) {
        String filePath = ApplicationConstants.kavitaFolder + fileName + ApplicationConstants.fileExtension;
        File file = new File(filePath);
        if (file.exists()) {
            try {
                String lastModifiedDate = new SimpleDateFormat("dd-MMM-yyyy").format(
                        new Date(file.lastModified())
                );
                return lastModifiedDate;
            } catch (Exception e) {
                Log.d("getFileDate", "Exception! " + e);
                return "";
            }
        } else {
            Log.d("getFileDate", "File not exists");
            return "";
        }
    }

    public static String getCurrentTimestamp() {
        return new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(
                new Date());
    }
}
