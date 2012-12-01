<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container">
			<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</a>
			<a class="brand" href="<c:url value="/home" />">requestr</a>
			<div class="nav-collapse collapse">
				<ul class="nav">
					<li <c:if test="${param.pageId eq 'home'}">class="active"</c:if>><a href="<c:url value="/home" />">Home</a></li>
					<li <c:if test="${param.pageId eq 'json'}">class="active"</c:if>><a href="<c:url value="/send/json" />">JSON Defined Request</a></li>
					<li <c:if test="${param.pageId eq 'components'}">class="active"</c:if>><a href="<c:url value="/send/components" />">Components Defined Request</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>
