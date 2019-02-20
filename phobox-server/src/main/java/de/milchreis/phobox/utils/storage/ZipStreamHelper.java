package de.milchreis.phobox.utils.storage;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipStreamHelper {

    public void compressToStream(List<File> files, OutputStream out) throws IOException {

        Closeable res = out;

        try {
            ZipOutputStream zout = new ZipOutputStream(out);
            res = zout;
            for (File kid : files) {
                zout.putNextEntry(new ZipEntry(kid.getName()));
                writeFileInZip(zout, kid);
            }
        } finally {
            res.close();
        }
    }

    public void compressToStream(File directory, OutputStream out) throws IOException {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<File>();
        queue.push(directory);
        Closeable res = out;
        try {
            ZipOutputStream zout = new ZipOutputStream(out);
            res = zout;
            while (!queue.isEmpty()) {
                directory = queue.pop();
                for (File kid : directory.listFiles()) {
                    String name = base.relativize(kid.toURI()).getPath();
                    if (kid.isDirectory()) {
                        queue.push(kid);
                        name = name.endsWith("/") ? name : name + "/";
                        zout.putNextEntry(new ZipEntry(name));
                    } else {
                        zout.putNextEntry(new ZipEntry(name));

                        writeFileInZip(zout, kid);
                    }
                }
            }
        } finally {
            res.close();
        }
    }

    private void writeFileInZip(ZipOutputStream zout, File kid) throws IOException {
        InputStream in = new FileInputStream(kid);
        try {
            byte[] buffer = new byte[1024];
            while (true) {
                int readCount = in.read(buffer);
                if (readCount < 0) {
                    break;
                }
                zout.write(buffer, 0, readCount);
            }
        } finally {
            in.close();
        }
        zout.closeEntry();
    }
}
