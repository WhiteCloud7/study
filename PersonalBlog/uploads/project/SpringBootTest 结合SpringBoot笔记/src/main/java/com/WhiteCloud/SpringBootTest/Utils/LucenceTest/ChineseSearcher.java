package com.WhiteCloud.SpringBootTest.Utils.LucenceTest;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
@Component
public class ChineseSearcher {
    public List searcher(String indexDir, String str) throws IOException, ParseException, InvalidTokenOffsetsException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        IndexReader indexReader = DirectoryReader.open(dir);
        Analyzer analyzer = new SmartChineseAnalyzer();
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        QueryParser parser = new QueryParser("test1",analyzer);
        Query query = parser.parse(str);

        long startTime = System.currentTimeMillis();
        TopDocs topDocs = indexSearcher.search(query,10);
        long endTime = System.currentTimeMillis();
        long searchTime = endTime - startTime;
        System.out.println("匹配" + str + "共耗时" + searchTime + "毫秒");
        System.out.println("查询到" + topDocs.totalHits + "条记录");

        //如果不指定参数的话，默认是加粗，即<b><b/>
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color=red>","</font></b>");
        //根据查询对象计算得分，会初始化一个查询结果最高的得分
        QueryScorer scorer = new QueryScorer(query);
        //根据这个得分计算出一个片段
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        //将这个片段中的关键字用上面初始化好的高亮格式高亮
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        //设置一下要显示的片段
        highlighter.setTextFragmenter(fragmenter);

        //取出每条查询结果
        List<String> list = new ArrayList<>();
        for(ScoreDoc scoreDoc : topDocs.scoreDocs) {
            //scoreDoc.doc相当于docID,根据这个docID来获取文档
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("test2:"+doc.get("city"));
            System.out.println("test1:"+doc.get("desc"));
            String desc = doc.get("test1");

            //显示高亮
            if(desc != null) {
                TokenStream tokenStream = analyzer.tokenStream("test1", new StringReader(desc));
                String summary = highlighter.getBestFragment(tokenStream, desc);
                System.out.println("高亮后的test1:"+summary);
                list.add(summary);
            }
        }
        indexReader.close();
        return list;
    }
}
