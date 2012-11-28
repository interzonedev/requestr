<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url var="formAction" value="/send/components" scope="page" />

<div class="section">
	<form:form modelAttribute="componentsForm" action="${formAction}" method="post">

		<div class="globalErrorsContainer">
			<form:errors cssClass="formError" />
		</div>

		<div>
			<label for="url">URL</label>
			<form:input path="url" id="url" cssClass="inputField" />
			<form:errors path="url" cssClass="formError" />
		</div>

		<div>
			<label for="method">Method</label>
			<form:input path="method" id="method" cssClass="inputField" />
			<form:errors path="method" cssClass="formError" />
		</div>

		<div class="buttons">
			<input type="submit" value="Submit" />
		</div>
	</form:form>

</div>
