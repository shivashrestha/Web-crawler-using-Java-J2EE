<%@ page language="java" pageEncoding="UTF-8" session="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Search index</title>
<link rel="stylesheet" href="css/style.css" type="text/css" />

</head>
<body onLoad="document.forms.search_frm.search_field.focus()">

	<div class="top-bar-black">
		<a href="javascript: void(0)" style="color: #fff">Search</a> <a
			href="#" style="margin-left: 10px;">Images</a>
	</div>

	<div class="home">
		<img src="img/logo.png" alt="Search" width="300" height="147"
			border="0">
		<form class="form-wrapper cf" method="post" name="search_frm"
			action="web/search" id="search_frm"
			onsubmit="if (document.getElementById('search_field').value.length < 1) return false;">
			<input type="text" placeholder="Search here..." name="search_field"
				id="search_field" required> <input type="hidden" name="page"
				value="1">
				
			<button type="submit" name="submit" value="Search">Search</button>
		</form>
	</div>

</body>
</html>