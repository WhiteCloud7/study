package com.WhiteCloud.SpringBootTest.Utils.LucenceTest;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;

public class Indexer {
    private IndexWriter indexWriter;
    //这里参数是存放索引的目录不是文件目录
    public Indexer(String indexDir) throws IOException {
        //建立索引目录
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        //实现标准分词器，会自动去掉空格啊，is a the等单词
        Analyzer analyzer = new StandardAnalyzer();
        //将分词器放到写索引配置中
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        //实例化写索引对象
        indexWriter = new IndexWriter(dir,indexWriterConfig);
    }
    //这是获取该目录下所有索引的方法
    public int indexAll(String dir) throws Exception {
        //获取目录下所以文件
        File[] files = new File(dir).listFiles();
        if (null != files) {
            for (File file : files) {
                //调用下面的indexFile方法，对每个文件进行索引
                indexFile(file);
            }
        }
        //返回索引的文件数
        return indexWriter.numDocs();
    }

    private void indexFile(File file) throws Exception {
        System.out.println("索引文件的路径：" + file.getCanonicalPath());
        //调用下面的getDocument方法，获取该文件的document
        Document doc = getDocument(file);
        //将doc添加到索引中
        indexWriter.addDocument(doc);
    }

    private Document getDocument(File file) throws IOException {
        Document document = new Document();
        //开始添加字段，先添加内容
        document.add(new TextField("contents", new FileReader(file)));
        //添加文件名，并把这个字段存到索引文件里
        document.add(new TextField("fileName", file.getName(), Field.Store.YES));
        //添加文件路径
        document.add(new TextField("fullPath", file.getCanonicalPath(), Field.Store.YES));
        return document;
    }
    //测试主类
    public static void main(String[] args) {
        //索引保存到的路径
        String indexDir = "D:\\GitHub\\study\\SpringBootTest\\src\\main\\java\\com\\WhiteCloud\\SpringBootTest\\Utils\\LucenceTest\\EnglishIndex";
        //需要索引的文件数据存放的目录
        String dataDir = "D:\\GitHub\\study\\SpringBootTest\\src\\main\\java\\com\\WhiteCloud\\SpringBootTest\\Utils\\LucenceTest\\EnglishArticle";
        Indexer indexer = null;
        int indexedNum = 0;
        //记录索引开始时间
        long startTime = System.currentTimeMillis();
        try {
            // 开始构建索引
            indexer = new Indexer(indexDir);
            indexedNum = indexer.indexAll(dataDir);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != indexer) {
                    indexer.indexWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //记录索引结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("索引耗时" + (endTime - startTime) + "毫秒");
        System.out.println("共索引了" + indexedNum + "个文件");
    }
}
