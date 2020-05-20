package com.zut.controller.manager;

import com.sun.org.apache.regexp.internal.RE;
import com.zut.controller.util.FTPUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping("/img")
public class ImageController {

    private static String host = "123.57.141.90";
    private static int port = 21;
    private static String user = "ftpuser1";
    private static String password = "123456";
    private static String basePath = "/home/ftpuser1/logistics/images";
    private static String saveFile = "D:/test/saveftp";


    @RequestMapping(value = "upload/{path}", method = RequestMethod.POST)
    public Result upload(@RequestParam("uploadIMG") MultipartFile file, @PathVariable String path){
        basePath = basePath;  //根据不同用户放置在不同目录
        String img_url = "";
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString().substring(0,10);  //存放于ftp服务器的文件名
        fileName=fileName+originalFilename.substring(originalFilename.lastIndexOf("."));  //获得文件扩展名
        String filePath = path + new SimpleDateFormat("/yyyy/MM/dd").format(new Date());
        HashMap<String, Object> map = new HashMap<>();
        try {
            boolean result = FTPUtil.uploadFile(host,port,user,password,basePath,filePath,fileName,file.getInputStream());
            if (!result){
                return new Result(StatusCode.REMOTEERROR,"图片上传失败");
            }else {
                img_url = "http://"+host+"/"+filePath+"/"+fileName; //用于返回给前端的图片路径
//                map.put("img_url",img_url);
                return new Result(StatusCode.OK,"图片上传成功", img_url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new Result(StatusCode.OK,"图片上传成功", img_url);
    }


    public static void main(String[] args) {
        Date now = new Date();
        String year = new SimpleDateFormat("yyyy").format(now);
        String month = new SimpleDateFormat("MM").format(now);
        String day = new SimpleDateFormat("dd").format(now);

        System.out.println(year+month+day);

        System.out.println(new SimpleDateFormat("/yyyy/MM/dd").format(now));
    }





}
