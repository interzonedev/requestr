<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url var="formAction" value="/send/json" scope="page" />

<div class="section">
	<form:form modelAttribute="jsonForm" action="${formAction}" method="post">

		<form:errors>
			<div class="control-group error">
				<form:errors cssClass="help-inline" />
			</div>
		</form:errors>

		<div <form:errors path="input">class="control-group error"</form:errors>>
			<label for="input">JSON Input</label>
			<form:textarea path="input" id="input" cols="50" rows="10" />
			<form:errors path="input"><p><form:errors path="input" cssClass="help-inline" /></p></form:errors>
		</div>

		<input type="submit" value="Submit" class="btn" />
	</form:form>
</div>
