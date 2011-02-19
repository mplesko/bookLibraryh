<html>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
	<head>
	<link href="../styles.css" rel="stylesheet" type="text/css" />
	<title><h:outputText value="add a book" /></title>
	</head>
	<body>
	<h:form id="addbookForm">
		<h1><h:outputText value="#{msgs.addBook}" /></h1>
		<h:message for="addbookForm" errorClass="error" showSummary="true" />

		<h:panelGrid columns="3">
			<h:outputText value="#{msgs.title}" />
			<h:inputText id="title" value="#{mainbean.addBookTitle}" required="true" />
			<h:message for="title" errorClass="error" />
			<h:outputText value="#{msgs.authorFirstName}" />
			<h:inputText id="authorFirstName" value="#{mainbean.addAuthorFirstName}"
				required="true" />
			<h:message for="authorFirstName" errorClass="error" />
			<h:outputText value="#{msgs.authorLastName}" />
			<h:inputText id="authorLastName" value="#{mainbean.addAuthorLastName}"
				required="true" />
			<h:message for="authorLastName" errorClass="error" />
		</h:panelGrid>

		<h:commandButton value="#{msgs.addBook}" action="#{mainbean.addBook}" />
		<h:commandButton value="#{msgs.cancel}" action="#{mainbean.cancel}"
			immediate="true" />
	</h:form>
	</body>
</f:view>
</html>
