package com.ljq.server;

import com.ljq.http.MyRequest;
import com.ljq.http.MyResponse;
import com.ljq.servlet.MyServlet;
import com.ljq.util.ServletConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


//将url分发给对应的servlet处理
public class ServletUtil {
    private static Logger logger = LoggerFactory.getLogger(ServletUtil.class);

    /**
     * 最长匹配原则，比如/test/post 匹配/test/post而不会匹配/test, 而单只有/匹配时，匹配到/
     * @param url
     * @return
     */
    public static String getServlet(String url){
        String res = null;
        int max_len = 0;
        int tmp_len = Integer.MAX_VALUE;
        for(Map.Entry e: ServletConfig.servletUrlMap.entrySet()){
            String tmp = e.getKey().toString();
            int len = 0;
            int i = 0;
            int j = 0;
            while (i < url.length() && j < tmp.length() && url.charAt(i) == tmp.charAt(j)){
                i++;
                j++;
                len++;
            }
            if(len > max_len){
                max_len = len;
                res = tmp;
            }else if (len == max_len){
                if(tmp.length() < tmp_len){
                    tmp_len = tmp.length();
                    res = tmp;
                }
            }
        }
        return res;
    }
    public static void dispatch(MyRequest request, MyResponse responsee)  {
        String url = request.getUrl();
        String res = getServlet(url);
        logger.info(url+"匹配的servlet地址为 "+res);
        String clazz = ServletConfig.servletUrlMap.get(res);
        Class<MyServlet> myServletClass = null;
        try {
            //加载
            myServletClass = (Class<MyServlet>) Class.forName(clazz);
            //创建
            MyServlet myServlet = myServletClass.newInstance();
            //初始化
            myServlet.init();
            //处理请求
            myServlet.service(request, responsee);
            //卸载
            myServlet.destory();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
