package com.WhiteCloud.SpringBootTest.Utils.LucenceTest;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class ChineseIndexer {
    private IndexWriter indexWriter;

    public ChineseIndexer(String indexDir) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        Analyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(dir,indexWriterConfig);
    }

    public int indexAll(String[] testSet) throws IOException {
        getIndex(testSet);
        return indexWriter.numDocs();
    }

    private void getIndex(String[] testSet) throws IOException {
        Document document = new Document();
        document.add(new TextField("test1",testSet[0],Field.Store.YES));
        document.add(new TextField("test2",testSet[1],Field.Store.YES));
        document.add(new TextField("test2",testSet[2],Field.Store.YES));
        indexWriter.addDocument(document);
    }

    public static void main(String[] args) throws IOException {
        String indexDir = "D:\\GitHub\\study\\SpringBootTest\\src\\main\\java\\com\\WhiteCloud\\SpringBootTest\\Utils\\LucenceTest\\ChineseIndex";
        String test1 = "关羽（162年—220年）[1]，字云长，本字长生，别称美髯公，河东郡解县（今山西运城）人，东汉末年名将，被后世崇为“武圣”，与“文圣”孔子齐名[2]。";
        String test2 = "关羽早期跟随刘备辗转各地。赤壁之战后被刘备任命为襄阳太守，刘备入益州，关羽留守荆州。219年，关羽围襄樊，曹操派于禁前来增援，关羽擒获于禁，斩杀庞德，威震华夏，后曹操派徐晃前来增援，东吴吕蒙又偷袭荆州，关羽腹背受敌，被潘璋部擒获，被杀于临沮。蜀汉后主刘禅追谥其为“壮缪侯”[3]。\n";
        String test3 = "关羽，本字长生，后改字云长，早年因犯事逃离家乡流落至幽州涿郡[6]。到了中平元年（184年），黄巾起义爆发，刘备在涿县组织起了一支义勇军。关羽、张飞都加入其中，随刘备辗转各地，参与扑灭黄巾军的战争，刘备、关羽、张飞三人的感情好到连睡觉也要睡在一起。刘备担任平原相时，任命关羽、张飞为别部司马，分统部曲。随刘备出席活动时，关羽和张飞则侍立刘备左右，保护刘备周全[6]。\n";
        String[] testSet = {test1,test2,test3};
        ChineseIndexer chineseIndexer = new ChineseIndexer(indexDir);
        long startTime = System.currentTimeMillis();
        int count = chineseIndexer.indexAll(testSet);
        long endTime = System.currentTimeMillis();
        System.out.println("共索引了"+count+"文件，耗时"+(endTime-startTime)+"ms");
        chineseIndexer.indexWriter.close();
    }
}
