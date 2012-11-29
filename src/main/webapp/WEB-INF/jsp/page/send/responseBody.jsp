<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="section">
	<p>Status: ${response.status}</p>
	<p>Content Type: ${response.contentType}</p>
	<p>Content Length: ${response.contentLength}</p>

	<div>
		<c:choose>
			<c:when test="${not empty response.headers}">
				<p>Headers:</p>
				<c:forEach items="${response.headers}" var="headerItem">
					<c:set var="headerName" value="${headerItem.key}" scope="page" />
					<c:set var="headerValue" value="${headerItem.value}" scope="page" />
					<p>${headerName} = ${headerValue}</p>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<p>No response headers</p>
			</c:otherwise>
		</c:choose>
	</div>

	<div>
		<p>Content:</p>
		<p><c:out value="${response.content}" /></p>
	</div>
</div>
