<%@ page import="com.azry.sps.console.server.ApplicationTimestamp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=9" >
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/app.css?t=<%=ApplicationTimestamp.getStartupTime()%>" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/font-awesome/css/font-awesome.min.css?t=<%=ApplicationTimestamp.getStartupTime()%>">

	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<link type="text/css" rel="stylesheet" href="css/reset.css" />
	<title>SPS Project</title>
</head>
<body>
	<script type="text/javascript" src="js/version.nocache.js"></script>
	<script type="text/javascript" src="sps/sps.nocache.js"></script>

</body>
</html>