package org.gif;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoGifExample {
   public static void main(String[] args) throws IOException {
      MongoClient mc = null;
      try {
         mc = new MongoClient();

         DBCollection col = mc.getDB("gifs").getCollection("gif");
         

         String originalGif = "cat-filing-nails.gif";

         BasicDBObject dbo = new BasicDBObject("file", originalGif);

         InputStream is = Files.newInputStream(Paths
               .get(originalGif));

         List<Integer> gifBytes = new ArrayList<Integer>();
         int b = -1;
         while ((b = is.read()) != -1) {
            gifBytes.add(b);
         }
         
         dbo.append("gif", gifBytes);
         
         col.insert(dbo);
         
         DBObject findOne = col.findOne(new BasicDBObject("file",originalGif));
         
         
         @SuppressWarnings("unchecked")
         List<Integer> gifOut = (List<Integer>) findOne.get("gif");
         
         OutputStream os = Files.newOutputStream(Paths.get("new-cat.gif"));
         
         for(int i : gifOut) {
            os.write(i);
         }
         
      } finally {
         if (null != mc) {
            mc.close();
         }
      }
   }
}
