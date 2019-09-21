package com.iyuba.core.util;

import android.content.Context;

import com.iyuba.core.listener.ActionFinishCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ct
 *         <p>
 *         功能：将assets目录下指定文件夹及子文件夹和文件拷贝到sd卡
 */
public class CopyFileToSD {
    private Context mContext;
    private ActionFinishCallBack finish;
    private String targetDir;
    private String assetFileName;

    public CopyFileToSD(Context context, String assetDir, String dir,
                        ActionFinishCallBack oflistener) {
        mContext = context;
        this.finish = oflistener;
        this.targetDir = dir;
        CopyAssets(assetDir, dir);
    }

    public void CopyAssets(String assetDir, String dir) {
        String[] files;
        File mWorkingPath = new File(dir);
        if (!mWorkingPath.exists()) {
            mWorkingPath.mkdirs();
        }
        try {
            files = mContext.getResources().getAssets().list(assetDir);
        } catch (IOException e) {
            return;
        }

        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i];
                if (!fileName.contains("")) {
                    if (0 == assetDir.length()) {
                        CopyAssets(fileName, dir + "/" + fileName + "/");
                    } else {
                        CopyAssets(assetDir + "/" + fileName, dir + "/"
                                + fileName + "/");
                    }
                    continue;
                }
                assetFileName = fileName;
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists())
                    outFile.delete();
                InputStream in = null;
                if (0 != assetDir.length())
                    in = mContext.getAssets().open(assetDir + "/" + fileName);
                else
                    in = mContext.getAssets().open(fileName);
                OutputStream out = new FileOutputStream(outFile);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024 * 8];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{
            File[] file = new File(targetDir).listFiles();
            for (File f : file) {
                if (targetDir.equals(dir) && f.getName().equals(assetFileName)) {
                    finish.onReceived();
                    break;
                }
            }
        }catch (Exception e){
            finish.onReceived();
            e.printStackTrace();
        }

    }
}
