package org.wlld.web.WebIo;

import org.wlld.web.server.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Web {
    private  Map<String, byte[]> urlMap = WebUrl.getWebUrl().getRes();

    public  void getAllFileName(String path, ArrayList<String> listFileName) {
        File file = new File(path);
        File[] files = file.listFiles();
        String[] names = file.list();
        if (names != null) {
            String[] completNames = new String[names.length];
            for (int i = 0; i < names.length; i++) {
                completNames[i] = path + names[i];
            }
            listFileName.addAll(Arrays.asList(completNames));
        }
        for (File a : files) {
            if (a.isDirectory()) {//如果文件夹下有子文件夹，获取子文件夹下的所有文件全路径。
                getAllFileName(a.getAbsolutePath() + "\\", listFileName);
            }
        }
    }

    public void start() throws IOException {
        read();
    }

    public  void read() throws IOException {
        ArrayList<String> listFileName = new ArrayList<String>();
        String path = Config.Web_Url;
        getAllFileName(path, listFileName);
        for (String name : listFileName) {
            File f = new File(name);
            if (!f.isDirectory()) {
                InputStream is = new FileInputStream(f);
                byte[] br = new byte[is.available()];
                int t = 0;
                int b = 0;
                while ((t = is.read()) > -1) {
                    br[b] = (byte) t;
                    b++;
                }
                 name = name.replace("\\","/");
                 name =name.substring(path.length()-1);
                 urlMap.put(name,br);
            }
        }
    }
}
