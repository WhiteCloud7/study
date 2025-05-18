package com.WhiteCloud.SpringBootTest.Utils.LucenceTest;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

class Searcher {
    public static void search(String searchDir,String str) throws IOException, ParseException {
        //获取搜素目录，即索引目录
        Directory dir = FSDirectory.open(Paths.get(searchDir));
        //用IndexReader读取索引
        IndexReader indexReader = DirectoryReader.open(dir);
        //构建IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //标准分词器
        Analyzer analyzer = new StandardAnalyzer();
        //查询解析器
        QueryParser queryParser = new QueryParser("contents",analyzer);
        //通过解析要查询的String，获取查询对象，str为传进来的待查的字符串
        Query query = queryParser.parse(str);

        long startTime = System.currentTimeMillis();
        //开始搜素,这里查询前十条记录
        TopDocs docs = indexSearcher.search(query,10);
        long endTime = System.currentTimeMillis();
        long searchTime = endTime - startTime;
        System.out.println("匹配" + str + "共耗时" + searchTime + "毫秒");
        System.out.println("查询到" + docs.totalHits + "条记录");

        //取出每条查询结果
        for(ScoreDoc scoreDoc : docs.scoreDocs) {
            //scoreDoc.doc相当于docID,根据这个docID来获取文档
            Document doc = indexSearcher.doc(scoreDoc.doc);
            //fullPath是刚刚建立索引的时候我们定义的一个字段，表示路径。也可以取其他的内容，只要我们在建立索引时有定义即可。
            System.out.println(doc.get("fullPath"));
        }
        indexReader.close();
    }

    //测试主类
    public static void main(String[] args) {
        String indexDir = "D:\\GitHub\\study\\SpringBootTest\\src\\main\\java\\com\\WhiteCloud\\SpringBootTest\\Utils\\LucenceTest\\EnglishIndex";
        //查询这个字符串
        String str = "你";
        try {
            search(indexDir, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
