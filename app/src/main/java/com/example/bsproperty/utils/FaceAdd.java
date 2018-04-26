package com.example.bsproperty.utils;

import java.net.URLEncoder;

/**
 * 人脸注册
 */
public class FaceAdd {

    public static String add(String token,String uid, String info,
                             String gid, String... path) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v2/faceset/user/add";
        try {
            StringBuffer buffer = new StringBuffer();
            for (String s : path) {
                String filePath = s;
                byte[] imgData = FileUtil.readFileByBytes(filePath);
                String imgStr = Base64Util.encode(imgData);
                String imgParam = URLEncoder.encode(imgStr, "UTF-8");
                buffer.append(imgParam);
                buffer.append(",");
            }

            String imgs = buffer.substring(0, buffer.length() - 1);

            String param = "uid=" + uid +
                    "&user_info=" + info +
                    "&group_id=" + gid +
                    "&images=" + imgs;

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