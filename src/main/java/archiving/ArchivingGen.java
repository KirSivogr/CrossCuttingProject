package archiving;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ArchivingGen {
    public ArchivingGen() {}
    public static void zipFile(String fileName , String directoryName) throws java.io.IOException {
        try(ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(directoryName));
            FileInputStream fis = new FileInputStream(fileName);)
        {
            ZipEntry entry1=new ZipEntry(fileName.substring(fileName.lastIndexOf("\\") + 1));
            zout.putNextEntry(entry1);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            zout.write(buffer);
            zout.closeEntry();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    public static void unzipFile(String fileName, String directoryName) throws java.io.IOException {
        ZipInputStream zin = new ZipInputStream(new FileInputStream(fileName));
        ZipEntry entry;
        while((entry = zin.getNextEntry()) != null) {
            FileOutputStream fout = new FileOutputStream(directoryName);
            for (int c = zin.read(); c != -1; c = zin.read()) {
                fout.write(c);
            }
            fout.flush();
            zin.closeEntry();
            fout.close();
        }
    }
}
