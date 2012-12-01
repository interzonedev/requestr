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
		<label for="url" class="inline wider">URL</label>
		<form:input path="url" id="url" />
		<form:errors path="url" cssClass="help-inline" />
	</div>

	<div class="control-group <form:errors path="method">error</form:errors>">
		<label for="method" class="inline wider">Method</label>
		<form:select id="method" path="method" items="${requestMethods}" />
		<form:errors path="method" cssClass="help-inline" />
	</div>

	<div class="control-group">
		<fieldset>
			<legend>Parameters</legend>
			<div>
				<label for="parameterName1" class="inline">Name</label>
				<input type="text" id="parameterName1">
				<span class="help-inline"></span>
				<label for="parameterValue1" class="inline">Value</label>
				<input type="text" id="parameterValue1">
				<span class="help-inline"></span>
				<i class="icon-minus-sign"></i>
			</div>
			<div><i class="icon-plus-sign"></i> Add another parameter</div>
		</fieldset>
	</div>

	<div class="control-group">
		<fieldset>
			<legend>Headers</legend>
			<div>
				<label for="headerName1" class="inline">Name</label>
				<input type="text" id="headerName1">
				<span class="help-inline"></span>
				<label for="headerValue1" class="inline">Value</label>
				<input type="text" id="headerValue1">
				<span class="help-inline"></span>
			</div>
		</fieldset>
	</div>

	<div class="control-group">
		<input type="submit" value="Submit" class="btn" />
	</div>

</form:form>
