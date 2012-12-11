<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="request" value="${response.request}" scope="page" />

<div class="page-header">
	<h2>Request Results</h2>
</div>

<div class="well">
	<p class="lead">Generating Request</p>

	<dl class="dl-horizontal">
		<dt>URL</dt>
		<dd>${request.url}</dd>

		<dt>Method</dt>
		<dd>${request.method}</dd>

		<dt>Headers</dt>
		<c:choose>
			<c:when test="${not empty request.headers}">
				<dd>
					<ul class="unstyled">
						<c:forEach items="${request.headers}" var="requestHeaderItem">
							<c:set var="requestHeaderName" value="${requestHeaderItem.key}" scope="page" />
							<c:set var="requestHeaderValue" value="${requestHeaderItem.value}" scope="page" />
							<li>${requestHeaderName} = ${requestHeaderValue}</li>
						</c:forEach>
					</ul>
				</dd>
			</c:when>
			<c:otherwise>
				<dd>No request headers</dd>
			</c:otherwise>
		</c:choose>

		<dt>Parameters</dt>
		<c:choose>
			<c:when test="${not empty request.parameters}">
				<dd>
					<ul class="unstyled">
						<c:forEach items="${request.parameters}" var="requestParameterItem">
							<c:set var="requestParameterName" value="${requestParameterItem.key}" scope="page" />
							<c:set var="requestParameterValue" value="${requestParameterItem.value}" scope="page" />
							<li>${requestParameterName} = ${requestParameterValue}</li>
						</c:forEach>
					</ul>
				</dd>
			</c:when>
			<c:otherwise>
				<dd>No request parameters</dd>
			</c:otherwise>
		</c:choose>
	</dl>
</div>

<div class="well">
	<p class="lead">Response</p>
	
	<dl class="dl-horizontal">
		<dt>Status</dt>
		<dd>${response.status}</dd>

		<dt>Content Type</dt>
		<dd>${response.contentType}</dd>

		<dt>Content Length</dt>
		<dd>${response.contentLength}</dd>

		<dt>Headers</dt>
		<c:choose>
			<c:when test="${not empty response.headers}">
				<dd>
					<ul class="unstyled">
						<c:forEach items="${response.headers}" var="responseHeaderItem">
							<c:set var="responseHeaderName" value="${responseHeaderItem.key}" scope="page" />
							<c:set var="responseHeaderValue" value="${responseHeaderItem.value}" scope="page" />
							<li>${responseHeaderName} = ${responseHeaderValue}</li>
						</c:forEach>
					</ul>
				</dd>
			</c:when>
			<c:otherwise>
				<dd>No response headers</dd>
			</c:otherwise>
		</c:choose>

		<dt>Cookies</dt>
		<c:choose>
			<c:when test="${not empty response.cookies}">
				<dd>
					<ul class="unstyled">
						<c:forEach items="${response.cookies}" var="cookieItem">
							<c:set var="cookieName" value="${cookieItem.key}" scope="page" />
							<c:set var="cookie" value="${cookieItem.value}" scope="page" />
							<c:set var="cookieValue" value="${cookie.value}" scope="page" />
							<li>${cookieName} = ${cookieValue}</li>
						</c:forEach>
					</ul>
				</dd>
			</c:when>
			<c:otherwise>
				<dd>No cookies in response</dd>
			</c:otherwise>
		</c:choose>

		<dt>Content</dt>
		<dd><c:out value="${response.content}" /></dd>
	</dl>
</div>
