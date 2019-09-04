package org.wlld.web.WebIo;

import org.wlld.web.server.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadConfig {
    private static ReadConfig config = new ReadConfig();
    private long nowKey = 0L;
    private int time = 0;

    private ReadConfig() {
    }


    public static ReadConfig getConfig() {
        return config;
    }

    public void readConfig() throws IOException {//读取同级目录下的配置文件
        String filePath = System.getProperty("user.dir") + "/config.txt";
        InputStream in = new BufferedInputStream(new FileInputStream(filePath));
        int i = 0;
        List<Byte> data = new ArrayList<>();
        int write = 0;
        boolean isData = false;//是否进入过换行符
        while ((i = in.read()) > -1) {
            if (i != 32) {
                if (i != 10 && i != 13) {
                    if (write > 0) {
                        data.add((byte) i);
                    }
                    getKey(i);
                   // System.out.println("nowKey==" + nowKey + ",end==" + Config.port + ",i==" + i);
                    if (nowKey == Config.port) {//端口号
                        isData = false;
                        write = write | 1;
                    } else if (nowKey == Config.host) {//包的全路径地址
                        isData = false;
                        write = write | 2;
                    }
                } else if (!isData) {
                    nowKey = 0;
                    time = 0;
                    isData = true;
                    if (write == 1) {//端口号
                        Config.Server_PORT = Integer.parseInt(getData(data));
                        //System.out.println("duan kou ==" + Config.Server_PORT);
                        write = 0;
                        data.clear();
                    }
                }
            }
        }
        Config.Web_Url = getData(data)+"\\";
    }

    public String getData(List<Byte> br) throws UnsupportedEncodingException {
        byte[] b = new byte[br.size()];
        for (int i = 0; i < br.size(); i++) {
            b[i] = br.get(i);
        }
        return new String(b, "UTF-8");
    }

    public void getKey(int be) {
        if (time > 4) {
            nowKey = 0;
            time = 0;
        }
        nowKey = nowKey << 8L | be;
        time++;
    }

    public long initPort() {
        byte[] ports = "port=".getBytes();
        long port = 0L;
        for (int i = 0; i < ports.length; i++) {
            int r = ports[i] & 0xff;
            port = port << 8L | (ports[i] & 0xff);
        }
        return port;
    }

    public long initHost() {
        byte[] hosts = "host=".getBytes();
        long host = 0L;
        for (int i = 0; i < hosts.length; i++) {
            host = host << 8L | (hosts[i] & 0xff);
        }
        return host;
    }
}
