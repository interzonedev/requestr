<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE html
	PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>Requestr Application - ${param.title}</title>

		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />

		<link rel="icon" href="<c:url value="/assets/img/favicon.png" />" type="image/png" />

		<jsp:include page="../frag/commonHeadCss.jsp" />

		<c:if test="${not empty param.cssIncludes}">
			<jsp:include page="${param.cssIncludes}" />
		</c:if>

		<jsp:include page="../frag/commonHeadJsIncludes.jsp" />

		<c:if test="${not empty param.headJsIncludes}">
			<jsp:include page="${param.headJsIncludes}" />
		</c:if>
	</head>

	<body>
		<div id="bodyContainer">
			<div id="topContainer">
				<jsp:include page="../frag/top.jsp" />
			</div>

			<div id="contentContainer">
				<div class="pageHeader">
					Requestr Application - ${param.pageHeader}
				</div>
				<jsp:include page="${param.bodyContent}" />
			</div>

			<div id="bottomContainer">
				<jsp:include page="../frag/bottom.jsp" />
			</div>
		</div>

		<jsp:include page="../frag/commonBodyJsIncludes.jsp" />

		<c:if test="${not empty param.bodyJsIncludes}">
			<jsp:include page="${param.bodyJsIncludes}" />
		</c:if>

		<jsp:include page="../frag/commonBodyJsSetUp.jsp" />

		<c:if test="${not empty param.bodyJsSetUp}">
			<jsp:include page="${param.bodyJsSetUp}" />
		</c:if>
	</body>
</html>
