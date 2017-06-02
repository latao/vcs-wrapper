package com.teclick.tools.vcs.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Nelson on 2017-05-26.
 * Zip
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class Zip {

    public static void unzip(File zipFile, File target) throws IOException {
        try (FileInputStream in = new FileInputStream(zipFile)) {
            unzip(in, target, true);
        }
    }

    public static void unzip(File zipFile, File target, boolean includeBaseFolder) throws IOException {
        try (FileInputStream in = new FileInputStream(zipFile)) {
            unzip(in, target, includeBaseFolder);
        }
    }

    public static void unzip(InputStream in, File folder, boolean includeBaseFolder) throws IOException {

        byte[] buffer = new byte[4096];

        try {
            //create output directory is not exists
            if (!folder.exists()) {
                folder.mkdirs();
            }

            //get the zip content
            try (ZipInputStream zis = new ZipInputStream(in)) {
                //get the zipped file list entry
                ZipEntry ze = zis.getNextEntry();
                String baseName = ze.getName();

                while (ze != null) {
                    String fileName = ze.getName();
                    File newFile;
                    if (includeBaseFolder) {
                        newFile = new File(folder + File.separator + fileName);
                    } else {
                        fileName = fileName.replace(baseName, "");
                        newFile = new File(folder + File.separator + fileName);
                    }
                    if (ze.isDirectory()) {
                        newFile.mkdir();
                        ze = zis.getNextEntry();
                        continue;
                    }

                    try(FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    ze = zis.getNextEntry();
                }

                zis.closeEntry();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
