<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url var="formAction" value="/send/components" scope="page" />

<form:form id="componentsForm" modelAttribute="componentsForm" action="${formAction}" method="post" class="form-horizontal">
	<form:hidden path="parameterValues" id="parameterValues" />
	<form:hidden path="headerValues" id="headerValues" />

	<form:errors>
		<div class="control-group error">
			<form:errors cssClass="help-inline" />
		</div>
	</form:errors>

	<div class="control-group <form:errors path="url">error</form:errors>">
		<label for="url" class="inline wider">URL</label>
		<form:input path="url" id="url" placeholder="URL" />
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
			<div id="parametersContainer"></div>
			<div id="addParameterTrigger" class="link"><i class="icon-plus-sign"></i> Add parameter</div>
		</fieldset>
	</div>

	<div class="control-group">
		<fieldset>
			<legend>Headers</legend>
			<div id="headersContainer"></div>
			<div id="addHeaderTrigger" class="link"><i class="icon-plus-sign"></i> Add header</div>
		</fieldset>
	</div>

	<div class="control-group spacer">
		<input type="submit" value="Submit" class="btn" />
	</div>

	<div id="nameValuePairTemplate" class="htmlTemplate">
		<div class="nameValuePairContainer control-nameValuePairContainer form-inline control-group">
			<span>
				<label for="name{{count}}" class="inline">Name</label>
				<input type="text" id="name{{count}}" class="control-nameInput" value="{{name}}" placeholder="Name">
				<span class="formError control-formError"><span class="help-inline">Invalid Name</span></span>
			</span>
			<span>
				<label for="value{{count}}" class="inline">Value</label>
				<input type="text" id="value{{count}}" class="control-valueInput" value="{{value}}" placeholder="Value">
				<span class="formError control-formError"><span class="help-inline">Invalid Value</span></span>
			</span>
			<i class="icon-minus-sign link control-deleteNameValuePair"></i>
		</div>
	</div>
</form:form>
