package com.carlos.service;

import com.carlos.domain.Book2;
import com.carlos.domain.BookEntity;
import com.carlos.es.IndexCore;
import com.carlos.repository.BookRepository2;
import com.carlos.utils.URLFecterUtils;
import com.carlos.utils.UUIDGeneratorUtils;
import net.sf.json.JSONObject;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Carlos on 2017/3/23.
 */
@Service("bookService2")
public class BookService2 {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    @Autowired
    private BookRepository2 bookRepository2;
    @Autowired
    private IndexCore indexCore;
    @Value("${es.indexName}")
    private String indexName;
    @Value("${es.typeName}")
    private String typeName;

    public void getBookInfo(){
        try{
            //初始化一个httpclient
            HttpClient client = new DefaultHttpClient();
            //我们要爬取的一个地址，这里可以从数据库中抽取数据，然后利用循环，可以爬取一个URL队列
            String url="http://search.jd.com/Search?keyword=Python&enc=utf-8&book=y&wq=Python&pvid=33xo9lni.p4a1qb";
            //抓取的数据
            List<BookEntity> bookdatas= URLFecterUtils.URLParser(client, url);
            Map<String,String> bookMap = new HashMap<String,String>();
            String bookInfo = "";
            if(bookdatas!=null&&bookdatas.size()!=0){
                logger.info("-----开始处理抓取的数据22-----");
                for (BookEntity bookEntity:bookdatas) {
                    Book2 book2 = new Book2();
                    book2.setBookID(bookEntity.getBookID());
                    book2.setBookName(bookEntity.getBookName());
                    book2.setBookPrice(bookEntity.getBookPrice());
                    //数据入库
                    bookRepository2.saveAndFlush(book2);
                    bookInfo = JSONObject.fromObject(book2).toString();
                    bookMap.put(new UUIDGeneratorUtils().uuidGenerate(),bookInfo);
//                logger.info("bookID:"+book.getBookID()+"\t"+"bookPrice:"+book.getBookPrice()+"\t"+"bookName:"+book.getBookName());
                }
                logger.info("-----数据写入ES开始22-----");
                indexCore.indexBulkInsert(indexName,typeName,bookMap);
                logger.info("-----数据写入ES结束22-----");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
