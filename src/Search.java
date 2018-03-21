import java.io.*;
import java.sql.*;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Search
 */
@WebServlet(description = "Search servlet", urlPatterns =
{ "/web/search" })
public class Search extends HttpServlet
{
   private static final long serialVersionUID = 1L;
   private Connection connection;
   private Properties props;

   protected void doPost(HttpServletRequest request,
         HttpServletResponse response) throws ServletException, IOException
   {
      request.getSession(true);
      response.setContentType("text/html");
      response.getWriter();
      
      
      int maxRank = 0;
      int pageNum = 0;
      List<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
      String search = new String();

      readProperties();
      try
      {
         // Get the search query and page number from the request
         search = request.getParameter("search_field");
         pageNum = Integer.parseInt(request.getParameter("page"));

         // Open database connection
         openConnection();

         // Split the search query by words
         String[] words = search.toLowerCase().split("\\s+");
         new ArrayList<List<Integer>>();
         List<ResultURL> result = new ArrayList<ResultURL>();
         
         // Create a crawler with the opened connection
         Crawler crawler = new Crawler(connection);

         // Get the urlList of each word
         for (String word : words)
         {
            // Check to see if the search query word is in the DB
            if (crawler.wordInDB(word, Crawler.TABLE_URLS))
            {
            	System.out.println("there exists a word="+word);
               // Get the urlList corresponding to the word
               List<String> list = crawler.getURLListFromDB(word, Crawler.TABLE_URLS);
               //System.out.println("search list="+list);
               if(list!=null){
                   // Retrieve information from DB for each urlID
                   for (String urlid : list )
                   {
                	   int id=Integer.parseInt(urlid);
                   	System.out.println("urlid="+urlid);
                      PreparedStatement pstmt = connection
                            .prepareStatement("SELECT * FROM urls WHERE urlid = ?");
                      pstmt.setInt(1,id);
                      ResultSet r = pstmt.executeQuery();
                      r.next();
                      int rank = r.getInt(3);
                      if (rank > maxRank)
                         maxRank = rank;
                         System.out.println("maxRank:"+maxRank);
                      
                      
                      String url = r.getString(2);
                      if(url.contains("#")) // skip url containing fragment identifiers
                         continue;
                      
                      // Add data to the result list
                      result.add(new ResultURL(id, url, rank, r
                            .getString(4), r.getString(5), words));
                      pstmt.close();
                   }

            	   
               }   	   
            	   
               }
               
               //List<Integer> temp = new ArrayList<Integer>();
               //System.out.println("temp="+temp);
        
               // Create a list of integers from the list
//               for (String idStr : ((String) list).split(","))
//               {
//              temp.add(Integer.parseInt(idStr));
//                  
//               }
//
//              urlIDs.add(temp);
                             
               
            
            
         }
         

//         if (!urlIDs.isEmpty())
//         {
//            // Get the intersection of the urlIDs
//            List<Integer> intersection = urlIDs.get(0);
//            System.out.println("intersection="+intersection);
//            
//            for (List<Integer> list : urlIDs)            
//             intersection.retainAll(list);
//            

//            // Retrieve information from DB for each urlID
//            for (int urlid : list )
//            {
//            	System.out.println("urlid="+urlid);
//               PreparedStatement pstmt = connection
//                     .prepareStatement("SELECT * FROM urls WHERE urlid = ?");
//               pstmt.setInt(1, urlid);
//               ResultSet r = pstmt.executeQuery();
//               r.next();
//               int rank = r.getInt(3);
//               if (rank > maxRank)
//                  maxRank = rank;
//               
//               String url = r.getString(2);
//               if(url.contains("#")) // skip url containing fragment identifiers
//                  continue;
//               
//               // Add data to the result list
//               result.add(new ResultURL(urlid, url, rank, r
//                     .getString(4), r.getString(5), words));
//               pstmt.close();
//            }

            /* 
             * Update rank if the word is found in the URL's title. For every
             * word found in the title, increment the rank by one
             * 
             */
            for (ResultURL obj : result)
            {
               int count = 1;
               for (String word : words)
               {
                  String title = obj.getTitle().toLowerCase();

                  if (title.contains(word.toLowerCase()))
                  {
                     // Bold the word in the title
                     int start = title.indexOf(word);
                     int end = start + word.length();
                     String titleBold = obj.getTitle().substring(0, start) + "<b>"
                           + obj.getTitle().substring(start, end) + "</b>"
                           + obj.getTitle().substring(end, title.length());

                     /* 
                      * Update page rank. Bring element to the top of the list
                      * by adding the maxRank then adding the found word counter
                      * 
                      */
                     obj.setRank(obj.getRank() + maxRank + count);
                     System.out.println("setrank:"+obj.getRank() + maxRank + count);

                     // Update to new title
                     obj.setTitle(titleBold);
                     count++;
                 //    System.out.println("count:"+count);
                  }
               }
            }

            // Sort the result collection by rank from high to low
            Collections.sort(result);

            // Convert object to String representations to be used in the jsp
            for (ResultURL obj : result)
            {
               ArrayList<String> temp = new ArrayList<String>();
               temp.add(obj.getUrl());
               temp.add(obj.getTitle());
               temp.add(obj.getDescription());
               temp.add(obj.getRank() + "");
               temp.add(obj.getUrlid() + "");
             //  System.out.println("url name:"+obj.getTitle());
               System.out.println("add rank:"+obj.getRank()+"");

               resultList.add(temp);
            }
         } catch (Exception e)
      {
             // TODO Auto-generated catch block
             e.printStackTrace();
          }

         /* 
          * Add resultList, original search query, and the update page number 
          * to the request
          */
         request.setAttribute("query", search);
         request.setAttribute("resultList", resultList);
         request.setAttribute("pageNum", pageNum);

         // Forward to results page
         String nextJSP = "/results.jsp";
         RequestDispatcher dispatcher = getServletContext()
               .getRequestDispatcher(nextJSP);
         dispatcher.forward(request, response);
         try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }

//      catch (Exception e)
//      {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      }

   //}

   /**
    * Open the properties file to retrieve information from it
    * @throws IOException
    */
   public void readProperties() throws IOException
   {
      props = new Properties();
      props.load(getServletContext().getResourceAsStream(
            "/WEB-INF/database.properties"));

   }

   /**
    * Create a connection to the DB from the properties file
    * @throws SQLException
    * @throws IOException
    * @throws ClassNotFoundException
    */
   public void openConnection() throws SQLException, IOException,
         ClassNotFoundException
   {

      String drivers = props.getProperty("jdbc.drivers");
      if (drivers != null)
         System.setProperty("jdbc.drivers", drivers);

      String url = props.getProperty("jdbc.url");
      String username = props.getProperty("jdbc.username");
      String password = props.getProperty("jdbc.password");
      String driver = "com.mysql.jdbc.Driver";

      Class.forName(driver);
      connection = DriverManager.getConnection(url, username, password);
      System.out.println("connection success");
   }
   /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
	//	request.getRequestDispatcher("/").forward(request,response);
	}

}
