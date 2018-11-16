package com.iyuba.trainingcamp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.utils
 * @class describe
 * @time 2018/7/21 14:52
 * @change
 * @chang time
 * @class describe
 */
public class FileUtils {

    public static List<String> ReadTxtFile(String strFilePath)
    {
        LogUtils.d("TestFile", strFilePath);

        String path = strFilePath;
        List<String> newList=new ArrayList<String>();
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory())
        {
            LogUtils.d("TestFile", "The File doesn't not exist.");
        }
        else
        {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while (( line = buffreader.readLine()) != null) {
                        newList.add(line+"\n");
                    }
                    instream.close();
                }
            }
            catch (java.io.FileNotFoundException e)
            {
                LogUtils.d("TestFile", "The File doesn't not exist.");
            }
            catch (IOException e)
            {
                LogUtils.d("TestFile", e.getMessage());
            }
        }
        return newList;
    }

}
