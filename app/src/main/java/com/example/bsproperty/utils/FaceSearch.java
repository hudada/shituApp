package com.example.bsproperty.utils;

import java.net.URLEncoder;

/**
* 人脸搜索
*/
public class FaceSearch {

    public static String search(String path,String token) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
        try {
            // 本地文件路径
            String filePath = path;
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam +
                    "&image_type=" + "BASE64" +
                    "&group_id_list=" + "ldh" +
                    "&max_user_num=" + 1;

            String accessToken = token;

            String result = HttpUtil.post(url, accessToken, param);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}