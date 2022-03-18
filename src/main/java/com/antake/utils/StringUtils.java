package com.antake.utils;

import cn.hutool.core.io.FileUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.lionsoul.ip2region.*;
import org.springframework.util.FileCopyUtils;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.util.Calendar;
import java.util.Date;

/**功能描述：字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * @Auther:http://www.xueden.cn
 * @Date:2020/3/23
 * @Description:cn.xueden.common.utils
 * @version:1.0
 */
@Slf4j
public class StringUtils extends org.apache.commons.lang3.StringUtils{
    private static final String IPCONFIGDIR= "/ip2region/ip2region.db";
    private static final char SEPARATOR = '_';

    private static final String UNKNOWN = "unknown";

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 获取ip地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if  (localhost.equals(ip))  {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }



    public static String getBrowser(HttpServletRequest request){
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    /**
     * 获得当天是周几
     */
    public static String getWeekDay(){
        /*String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};*/
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return weekDays[w];
    }
    /**
     * 获取发起请求的操作系统名称
     */
    public static String getOsName(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        return operatingSystem.getName();
    }
    /**
    * @Description: 获取ip归属地
    * @Param: [ip]
    * @return: java.lang.String
    * @Author: MiZiMi
    * @Date: 2020/5/11 11:49
    */
    public static String getIpArea(String ip) {
        if (Util.isIpAddress(ip)){
            try {
                String dbPath = StringUtils.class.getResource(IPCONFIGDIR).getPath();
                File file = new File(URLDecoder.decode(dbPath,"UTF-8"));
                //linux没有文件路径映射，如果直接读取就会出错，报io异常
                if (!file.exists()) {
                    String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
                    dbPath = tmpDir + "ip.db";
                    file = new File(dbPath);
                    if (!file.exists()){
                        FileUtils.copyInputStreamToFile(StringUtils.class.getClassLoader().getResourceAsStream("classpath:ip2region/ip2region.db"), file);
                    }
                }
                if (file.exists()){
                    DbConfig config = new DbConfig();
                    DbSearcher searcher = new DbSearcher(config, file.getPath());
                    DataBlock dataBlock = searcher.btreeSearch(ip);
                    String region = dataBlock.getRegion();
                    if (region!=null) {
                        return region;
                    }
                }
            } catch (DbMakerConfigException e) {
                log.error("getIpArea...创建dbconfig失败...{}",e);
            } catch (FileNotFoundException e) {
                log.error("getIpArea...没有找到IPCONFIGDIR...{}",e);
            } catch (IOException e) {
                log.error("getIpArea...查找ip IO异常...{}",e);
            }
        }
        return "";
    }

    public static void main(String[] args) {
        getIpArea("222.209.35.36");
    }
}
