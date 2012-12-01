<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url var="formAction" value="/send/components" scope="page" />


<form:form modelAttribute="componentsForm" action="${formAction}" method="post" class="form-horizontal">

	<form:errors>
		<div class="control-group error">
			<form:errors cssClass="help-inline" />
		</div>
	</form:errors>

	<div class="control-group <form:errors path="url">error</form:errors>">
		<label for="url" class="control-label">URL</label>
		<div class="controls">
			<form:input path="url" id="url" />
			<form:errors path="url" cssClass="help-inline" />
		</div>
	</div>

	<div class="control-group <form:errors path="method">error</form:errors>">
		<label for="method" class="control-label">Method</label>
		<div class="controls">
			<form:select id="method" path="method" items="${requestMethods}" />
			<form:errors path="method" cssClass="help-inline" />
		</div>
	</div>

	<div class="control-group">
		<div class="controls">
			<input type="submit" value="Submit" class="btn" />
		</div>
	</div>

</form:form>
