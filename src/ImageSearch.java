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
@WebServlet(description = "Image search servlet", urlPatterns =
{ "/image/search" })
public class ImageSearch extends HttpServlet
{
   private static final long serialVersionUID = 1L;
   private Connection connection;
   private Properties props;

   protected void doPost(HttpServletRequest request,
         HttpServletResponse response) throws ServletException, IOException
   {
      HttpSession session = request.getSession(true);
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      
      
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
         List<List<Integer>> urlIDs = new ArrayList<List<Integer>>();
         List<ResultURL> result = new ArrayList<ResultURL>();
         
         // Create a crawler with the opened connection
         Crawler crawler = new Crawler(connection);

         // Get the urlList of each word
         for (String word : words)
         {
            // Check to see if the search query word is in the DB
            if (crawler.wordInDB(word, Crawler.TABLE_IMGWORD))
            {
               // Get the urlList corresponding to the word
               List<String> list = crawler.getURLListFromDB(word, Crawler.TABLE_IMGWORD);
               List<Integer> temp = new ArrayList<Integer>();

               // Create a list of integers from the list
//               for (String idStr : list.split(","))
//               {
//                  temp.add(Integer.parseInt(idStr));
//               }
//
//               urlIDs.add(temp);
            }
         }

         if (!urlIDs.isEmpty())
         {
            // Get the intersection of the urlIDs
            List<Integer> intersection = urlIDs.get(0);
            for (List<Integer> list : urlIDs)
               intersection.retainAll(list);

            // Retrieve information from DB for each urlID
            for (int urlid : intersection)
            {
               PreparedStatement pstmt = connection
                     .prepareStatement("SELECT * FROM images WHERE urlid = ?");
               pstmt.setInt(1, urlid);
               ResultSet r = pstmt.executeQuery();
               r.next();
               int rank = r.getInt(3);
               if (rank > maxRank)
                  maxRank = rank;
               
               String url = r.getString(2);
               if(url.contains("#")) // skip url containing fragment identifiers
                  continue;
               
               // Add data to the result list
               result.add(new ResultURL(urlid, url, rank));
               pstmt.close();
            }

            // Sort the result collection by rank from high to low
            Collections.sort(result);

            // Convert object to String representations
            for (ResultURL obj : result)
            {
               ArrayList<String> temp = new ArrayList<String>();
               temp.add(obj.getUrl());
               temp.add(obj.getRank() + "");
               temp.add(obj.getUrlid() + "");

               resultList.add(temp);
            }
         }

         /* 
          * Add resultList, original search query, and the update page number 
          * to the request
          */
         request.setAttribute("query", search);
         request.setAttribute("resultList", resultList);
         request.setAttribute("pageNum", pageNum);

         // Forward to image results page
         String nextJSP = "/imageResults.jsp";
         RequestDispatcher dispatcher = getServletContext()
               .getRequestDispatcher(nextJSP);
         dispatcher.forward(request, response);
         connection.close();
      }

      catch (Exception e)
      {
         e.printStackTrace();
      }

   }

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
   }
}
