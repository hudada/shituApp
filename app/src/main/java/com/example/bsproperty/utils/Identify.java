package com.example.bsproperty.utils;

import java.net.URLEncoder;

/**
* 人脸查找——识别
*/
public class Identify {

    public static String identify(String path,String token) {
        String url = "https://aip.baidubce.com/rest/2.0/face/v2/identify";
        try {
            String filePath = path;
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");


            String param = "group_id=" +
                    "ldh" +
                    "&user_top_num=" + "4" +
                    "&face_top_num=" + "1" + "&image=" + imgParam;

            String accessToken = token;

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}