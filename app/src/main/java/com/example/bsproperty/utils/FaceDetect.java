package com.example.bsproperty.utils;

import java.net.URLEncoder;

/**
 * 人脸探测
 */
public class FaceDetect {

    public static String detect(String path, String token) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v1/detect";
        try {
            // 本地文件路径
            String filePath = path;
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "max_face_num=" + 5 + "&face_fields=" + "age,beauty,expression,faceshape,gender,glasses,landmark,race,qualities" + "&image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = token;

            String result = HttpUtil.post(url, accessToken, param);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}