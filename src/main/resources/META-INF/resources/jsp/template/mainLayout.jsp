<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--[if lt IE 7]>      <html lang="en-US" class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html lang="en-US" class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html lang="en-US" class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en-US" class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

        <title>Requestr Application - ${param.title}</title>

        <meta name="description"
            content="Make any kind of HTTP request with control over headers and parameters in JSON format or with individual components." />

        <meta name="viewport" content="width=device-width, initial-scale=1.0" />

        <link rel="icon" href="<c:url value="/img/favicon.png" />" type="image/png" />

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
        <jsp:include page="../frag/top.jsp" />

        <div class="container">
            <jsp:include page="${param.bodyContent}" />

            <jsp:include page="../frag/bottom.jsp" />
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
