package newDB;



import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class CetRootWord implements Serializable {

    @PrimaryKey
    @NonNull
    public String word;

    public int groupflg ;

    public String sound ;

    public String pron ;

    public String def ;

    public String grouptitle ;

    public String root ;

    public int remembered ;

    public int stage ;

    public int flag ; //是否标记为收藏
    public int wrong ; //是否做错    1正确 2错误



    public String sentence ;

    public String sentenceCN ;


    public String sentencePron;
}
