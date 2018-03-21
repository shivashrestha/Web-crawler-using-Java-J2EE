<%@ page language="java" pageEncoding="UTF-8" session="true"%>
<%@ page import="java.util.*"%>
<%
   Object query = request.getAttribute("query");
   int pageNum = (Integer) request.getAttribute("pageNum");
   List<ArrayList<String>> result = (List<ArrayList<String>>) request
   .getAttribute("resultList");
   int maxPages = ((int) result.size() / 10) + 1;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title><%=query.toString()%> - Search</title>
<link rel="stylesheet" href="../css/styleResult.css" type="text/css" />
<!--[if !IE 7]>
   <style type="text/css">
      #wrap {display:table;height:100%}
   </style>
<![endif]-->

<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.min.js"></script>
<link rel="stylesheet" href="../fancybox/jquery.fancybox.css"
	type="text/css" media="screen" />
<script type="text/javascript" src="../fancybox/jquery.fancybox.pack.js"></script>
<script src="../js/jquery.lazyload.min.js" type="text/javascript"></script>

<script>
$(document).ready(function() {
	   $(".fancybox").fancybox({
	      openEffect  : 'none',
	      closeEffect : 'none',
	      closeBtn    : false,
	      loop        : false,
	      fitToView   : true
	   });
	});
</script>

<script>$("img.lazy").lazyload();</script>

</head>
<body style="background: #f1f1f1;">
	<div class="top-bar-black">
		<form name="ImageSearchTop" method="POST"
			action="/Project_khwopa_7/web/search" style="display: inline">
			<input type="hidden" name="page" value="1"> <input
				type="hidden" value="<%=query.toString()%>" name="search_field"
				id="search_field"> <a
				href="javascript:document.ImageSearchTop.submit()">Search</a>
		</form>
		<a href="javascript: void(0)" style="color: #fff">Images</a>
	</div>
	<div id="wrap">
		<div id="main">
			<div class=topbar id=TopBar>
				<a href="/Project_khwopa_7"> <img src="../img/logoMini.png"
					alt="Search" width="156" height="38" border="0">
				</a>
				<form class="form-wrapper cf" method="post" name="search_frm"
					action="/Project_khwopa_7/image/search"
					onsubmit="if (document.getElementById('search_field').value.length < 1) return false;">
					<input type="text" value="<%=query.toString()%>"
						name="search_field" id="search_field" required> <input
						type="hidden" name="page" value="1">
					<button type="submit" name="submit" value="Search">
						<img src="../img/search.png">
					</button>
				</form>
			</div>

			<div class="stub">
				<form name="imageSearch" method="POST"
					action="/Project_khwopa_7/web/search"
					style="display: inline; margin-left: 160px; margin-right: -170px;">
					<input type="hidden" name="page" value="1"> <input
						type="hidden" value="<%=query.toString()%>" name="search_field"
						id="search_field"> <a
						href="javascript:document.imageSearch.submit()"
						class="stub-regular" style="margin-left: 15px;">Web</a>
				</form>
				<a href="javascript: void(0)" class="stub-focus">Images</a>
			</div>


			<div class="results image-results">
				<%
      // If there are no results found
         if (result != null)
         {
            if (result.size() == 0)
            {
      %>
				<h5>
					Your search - <b><%=query.toString()%></b> - did not match any
					images. <br>
					<br> Suggestions: <br>
					<br> &bull; Make sure all words are spelled correctly.<br>
					&bull; Try different keywords.<br> &bull; Try more general
					keywords.<br> &bull; Try fewer keywords.
				</h5>

				<%
         }
            else
            {
               // Display all images
               for (int i = 0; i < result.size(); i++)
               {
               // Use lazy load to speed up image scrolling
      %>
				<a class="fancybox lazy" rel="gallery1"
					href="<%=result.get(i).get(0)%>" title=""> <img
					src="<%=result.get(i).get(0)%>" alt=""
					style="max-height: 200px; width: auto; max-width: 50%; min-width: auto;">
				</a>
				<%
         }
            }
         }
      %>
			</div>
		</div>
	</div>

</body>

<footer>
	<div id="footer">
		<div class="footer image-footer">
			<p> 4S &copy; WebCrawler 2017 All Rights Reserved.</p>
		</div>
	</div>
</footer>
</html>