package me.ferdithedev.overblock.util;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class FileUtil {

    public static boolean copy(File source, File dest) throws IOException {
        if(source.isDirectory()) {
            if(!dest.exists()) {
                dest.mkdir();
            }

            String[] files = source.list();
            if(files == null) return false;
            for(String file : files) {
                File newSource = new File(source,file);
                File newDest = new File(dest, file);
                if(!copy(newSource,newDest)) return false;
            }
        } else {
            try {
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(dest);

                byte[] buffer = new byte[1024];

                int length;

                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }

                in.close();
                out.close();
            } catch (FileNotFoundException ignored) {
                return false;
            }
        }
        return true;
    }

    public static void delete(File file) {
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            if(files == null) return;
            for(File child : files) {
                delete(child);
            }
        }

        file.delete();
    }

    public static YamlConfiguration getConfigOfFile(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
}
