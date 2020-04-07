package com.iyuba.wordtest.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.iyuba.wordtest.entity.CetRootWord;

import java.util.List;

;

@Dao
public interface CetDAO {
//    @Query("select * from NewTypeAnswer where TestTime = :TestTime and Section =:Section and Sound =:Sound")
//    List<NewTypeAnswer> getAllFromAnswer(String TestTime, String Section, String Sound) ;
//
//    @Query("select * from NewTypeExplain where TestTime = :TestTime and Section =:Section")
//    List<NewTypeExplain> getAllFromExplain(String TestTime, String Section) ;
//
//
//    @Query("select * from NewTypeText where TestTime = :TestTime and Section =:Section")
//    List<NewTypeText> getAllFromText(String TestTime, String Section) ;
//
//    @Query("select Sentence from NewTypeText where Sentence like :search limit 1")
//    List<String> findFromText(String search) ;
//
//    @Query("select * from NewTypeText where TestTime = :TestTime and Section =:Section and Sound = :Sound")
//    List<NewTypeText> getAllFromText(String TestTime, String Section, String Sound) ;
//
//    @Query("select * from NewTypeText where TestTime = :TestTime and Section = :Section and Number =:Number and NumberIndex = :NumberIndex")
//    List<NewTypeText> getTextByIDandNumber(String TestTime, String Section, String Number, String NumberIndex) ;
//
//
//    @Query("select * from NewTypeExplain where TestTime = :TestTime and Section =:Section and Sound =:Sound")
//    List<NewTypeExplain> getSelectFromExplain(String TestTime, String Section, String Sound) ;
//
//
//    @Query("select * from CetRootWord where groupflg = :groupflag limit 3")
//    List<CetRootWord> getRootWord(int groupflag) ;
//
//
//    @Query("select * from ExamYear")
//    List<ExamYear> getExamYears() ;
//
//    @Query("select * from ExamData where TestTime = :TestTime  and Section = :Section")
//    List<ExamData> getExamData(String TestTime, String Section) ;
//
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    long[]  insertAll(NewTypeAnswer... As);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    long[]  insertAllText(NewTypeText... As);
//
//
//
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    long[]  insertAllExplain(NewTypeExplain... Explains);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    long[]  insertExamData(ExamData... examDatas);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    long[]  insertExamYear(ExamYear... examYears);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    long[]  insertExamYear(List<ExamYear> examYears);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[]  insertWord(CetRootWord... word);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[]  insertWord(List<CetRootWord> words);


}
