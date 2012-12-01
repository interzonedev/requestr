<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url var="formAction" value="/send/json" scope="page" />

<div class="section">
	<form:form modelAttribute="jsonForm" action="${formAction}" method="post">

		<div class="globalErrorsContainer">
			<form:errors cssClass="formError" />
		</div>

		<div>
			<label for="input">JSON Input</label>
			<form:textarea path="input" id="input" cssClass="inputField" cols="100" rows="20" />
			<form:errors path="input" cssClass="formError" />
		</div>

		<div class="buttons">
			<input type="submit" value="Submit" />
		</div>
	</form:form>

</div>
