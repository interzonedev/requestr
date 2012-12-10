<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="request" value="${response.request}" scope="page" />

<div class="well">
	<div>Generating Request:</div>
	<div>
		<p>URL: ${request.url}</p>
		<p>Method: ${request.method}</p>
	</div>

	<div>
		<c:choose>
			<c:when test="${not empty request.headers}">
				<p>Headers:</p>
				<c:forEach items="${request.headers}" var="requestHeaderItem">
					<c:set var="requestHeaderName" value="${requestHeaderItem.key}" scope="page" />
					<c:set var="requestHeaderValue" value="${requestHeaderItem.value}" scope="page" />
					<p>${requestHeaderName} = ${requestHeaderValue}</p>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<p>No request headers</p>
			</c:otherwise>
		</c:choose>
	</div>

	<div>
		<c:choose>
			<c:when test="${not empty request.parameters}">
				<p>Parameters:</p>
				<c:forEach items="${request.parameters}" var="requestParameterItem">
					<c:set var="requestParameterName" value="${requestParameterItem.key}" scope="page" />
					<c:set var="requestParameterValue" value="${requestParameterItem.value}" scope="page" />
					<p>${requestParameterName} = ${requestParameterValue}</p>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<p>No request parameters</p>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<div class="well">
	<div>Response:</div>
	<div>
		<p>Status: ${response.status}</p>
		<p>Content Type: ${response.contentType}</p>
		<p>Content Length: ${response.contentLength}</p>
	</div>

	<div>
		<c:choose>
			<c:when test="${not empty response.headers}">
				<p>Headers:</p>
				<c:forEach items="${response.headers}" var="responseHeaderItem">
					<c:set var="responseHeaderName" value="${responseHeaderItem.key}" scope="page" />
					<c:set var="responseHeaderValue" value="${responseHeaderItem.value}" scope="page" />
					<p>${responseHeaderName} = ${responseHeaderValue}</p>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<p>No response headers</p>
			</c:otherwise>
		</c:choose>
	</div>

	<div>
		<c:choose>
			<c:when test="${not empty response.cookies}">
				<p>Headers:</p>
				<c:forEach items="${response.cookies}" var="cookie">
					<c:set var="cookieName" value="${cookie.name}" scope="page" />
					<c:set var="cookieValue" value="${cookie.value}" scope="page" />
					<p>${cookieName} = ${cookieValue}</p>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<p>No cookies in response</p>
			</c:otherwise>
		</c:choose>
	</div>

	<div>
		<p>Content:</p>
		<p><c:out value="${response.content}" /></p>
	</div>
</div>