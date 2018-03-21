/**
 * ResultURL Class which contains necessary information for each url element to
 * be displayed
 * 
 */
public class ResultURL implements Comparable<ResultURL>
{
   private String url, title, description;
String[] keywords;
   private int urlid, rank;

   public ResultURL(int id, String url, int rank, String title, String desc,String[] words)
   {
      this.setUrlid(id);
      this.setUrl(url);
      this.rank = rank;
      this.title = title;
      this.setDescription(desc);
      this.keywords=words;
   }

   public ResultURL(int urlid, String url, int rank)
   {
      this.setUrlid(urlid);
      this.setUrl(url);
      this.rank = rank;
   }

   /**
    * Compare function. Object is compared by URL Ranking
    */
   @Override
   public int compareTo(ResultURL arg0)
   {
      return arg0.rank - this.rank;
   }

   @Override
   public String toString()
   {
      return this.rank + "";
   }
   
   /**
    * Set and Get Methods
    */

   public String getUrl()
   {
      return url;
   }

   public String[] getKeywords() {
	return keywords;
}

public void setKeywords(String[] keywords) {
	this.keywords = keywords;
}

public void setUrl(String url)
   {
      this.url = url;
   }
   
   public String getTitle()
   {
      return title;
   }
   
   public void setTitle(String title)
   {
      this.title = title;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public int getUrlid()
   {
      return urlid;
   }

   public void setUrlid(int urlid)
   {
      this.urlid = urlid;
   }
   
   public int getRank()
   {
      return rank;
   }
   
   public void setRank(int rank)
   {
      this.rank = rank;
   }


}