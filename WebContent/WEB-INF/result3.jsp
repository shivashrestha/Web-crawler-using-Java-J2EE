<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page language="java" pageEncoding="UTF-8" session="true"%>
<%@ page import="java.util.*"%>

<%
   // Create global variables 
   Object query = request.getAttribute("query");
   int pageNum = (Integer) request.getAttribute("pageNum");
   List<ArrayList<String>> result = (List<ArrayList<String>>) request
   .getAttribute("resultList");
   int maxPages = ((int) result.size() / 10) + 1;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<div class="top-bar-black" id=BlackBar>
		<a href="javascript: void(0)" style="color: #fff">Search</a>
		<form name="webSearchTop" method="POST"
			action="/Project_khwopa_7/image/search" style="display: inline">
			<input type="hidden" name="page" value="1"> <input
				type="hidden" value="<%=query.toString()%>" name="search_field"
				id="search_field"> <a
				href="javascript:document.webSearchTop.submit()">Images</a>
		</form>
	</div>
	<div id="wrap">
		<div id="main">

			<div class=topbar id=TopBar>
				<a href="/Project_khwopa_7"> <img src="../img/logoMini.png"
					alt="Search" width="156" height="38" border="0">
				</a>
				<form class="form-wrapper cf" method="post" name="search_frm"
					action="/Project_khwopa_7/web/search"
					onsubmit="if (document.getElementById('search_field').value.length < 1) return false;">
					<input type="text" value="<%=query.toString()%>"
						name="search_field" id="search_field" required> <input
						type="hidden" name="page" value="1">
					<button type="submit" name="submit" value="Search">
						<img src="../img/search.png">
					</button>
				</form>
			</div>

			<div class="stub" id=Stub>
				<a href="javascript: void(0)" class="stub-focus">Web</a>
				<form name="webSearch" method="POST"
					action="/Project_khwopa_7/image/search" style="display: inline">
					<input type="hidden" name="page" value="1"> <input
						type="hidden" value="<%=query.toString()%>" name="search_field"
						id="search_field"> <a
						href="javascript:document.webSearch.submit()" class="stub-regular">Images</a>
				</form>
			</div>


			<div class=results>
				<%
		   // If no results are found
		   if (result != null)
		   {
		      if (result.size() == 0)
		      {
		%>
				<h5>
					Your search - <b><%=query.toString()%></b> - did not match any
					documents. <br>
					<br> Suggestions: <br>
					<br> &bull; Make sure all words are spelled correctly.<br>
					&bull; Try different keywords.<br> &bull; Try more general
					keywords.<br> &bull; Try fewer keywords.
				</h5>

				<%
		   }
		      // Print only 10 entries from the result list
		      else
		      {

		         for (int i = (pageNum - 1) * 10; i < pageNum * 10
		               && i < result.size(); i++)
		         {
		
		
		%>
		
				<a href="<%=result.get(i).get(0)%>" style="color: blue;" target="_blank"><%=result.get(i).get(1)%></a>
				<br>
				<h4><%=result.get(i).get(0)%></h4>
				<h3><%=result.get(i).get(2)%></h3>
				<br>
				<%
		   }
		      }
		   }
		%>
			</div>
		</div>
	</div>

</body>
</html>