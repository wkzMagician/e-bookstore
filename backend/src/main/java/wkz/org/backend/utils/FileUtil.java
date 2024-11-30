package wkz.org.backend.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static String saveImage(String cover, Long bookId) {
        // 保存在resources/static/bookImage/目录下
        // 现在cover是一个base64字符串，需要将其转换为图片文件
        // 保存的文件名为bookId.jpg
        try {
            // Check if cover string contains a data URL scheme and remove it
            if (cover.startsWith("data:image/jpeg;base64,")) {
                cover = cover.substring("data:image/jpeg;base64,".length());
            }else if (cover.startsWith("data:image/png;base64,")) {
                cover = cover.substring("data:image/png;base64,".length());
            }else if (cover.startsWith("data:image/jpg;base64,")) {
                cover = cover.substring("data:image/jpg;base64,".length());
            }



            // Decode the base64 string to byte array
            byte[] decodedImg = Base64.getDecoder().decode(cover.getBytes("UTF-8"));

            // Define the path of the image file
            String imgPath = "src/main/resources/static/bookImage/" + bookId + ".jpg";

            // Write the byte array to a file
            Files.write(Paths.get(imgPath), decodedImg, StandardOpenOption.CREATE);

            // 只返回bookImage/xxx.jpg
            return "bookImage/" + bookId + ".jpg";
        } catch (Exception e) {
            logger.error("Error saving image", e);
        }

        return null;
    }

    public static String saveImage(String cover) {
        // 保存在resources/static/bookImage/目录下
        // 现在cover是一个base64字符串，需要将其转换为图片文件
        // 保存的文件名为时间戳.jpg
        try {
            // Check if cover string contains a data URL scheme and remove it
            if (cover.startsWith("data:image/jpeg;base64,")) {
                cover = cover.substring("data:image/jpeg;base64,".length());
            }else if (cover.startsWith("data:image/png;base64,")) {
                cover = cover.substring("data:image/png;base64,".length());
            }else if (cover.startsWith("data:image/jpg;base64,")) {
                cover = cover.substring("data:image/jpg;base64,".length());
            }else {
                return null;
            }


            // Decode the base64 string to byte array
            byte[] decodedImg = Base64.getDecoder().decode(cover.getBytes("UTF-8"));

            // Define the path of the image file
            String imgPath = "src/main/resources/static/bookImage/" + System.currentTimeMillis() + ".jpg";

            // Write the byte array to a file
            Files.write(Paths.get(imgPath), decodedImg, StandardOpenOption.CREATE);

            // 只返回bookImage/xxx.jpg
            return imgPath.substring(imgPath.indexOf("bookImage"));
        } catch (Exception e) {
            logger.error("Error saving image", e);
        }

        return null;
    }
}
