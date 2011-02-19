<html>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
	<head>
	<link href="../styles.css" rel="stylesheet" type="text/css" />
	<title><h:outputText value="login" /></title>
	</head>
	<body>
	<h:form id="loginForm">
		<h1><h:outputText value="#{msgs.welcome}" /></h1>
		<h1><h:outputText value="#{msgs.welcome2}" /></h1>
		<h:message for="loginForm" errorClass="error" showSummary="true" />

		<h:panelGrid columns="3">
			<h:outputText value="#{msgs.name}" />
			<h:inputText id="userName" value="#{userbean.userName}"
				required="true" />
			<h:message for="userName" errorClass="error"/>
			<h:outputText value="#{msgs.password}" />
			<h:inputSecret id="password" value="#{userbean.password}"
				required="true" />
			<h:message for="password" errorClass="error"/>
		</h:panelGrid>

		<h:commandButton value="#{msgs.createAccount}" action="#{userbean.createAccount}" />

		<h:commandButton value="#{msgs.login}" action="#{userbean.login}" />
	</h:form>
	</body>
</f:view>
</html>
