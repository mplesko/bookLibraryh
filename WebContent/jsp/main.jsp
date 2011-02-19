<html>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
	<head>
	<link href="../styles.css" rel="stylesheet" type="text/css" />
	<title><h:outputText value="main" /></title>
	</head>
	<body>
	<h:form id="mainForm">
		<h1><h:outputText value="#{msgs.welcome1}#{userbean.userName}!" /></h1>
		<h1><h:outputText value="#{msgs.welcome2}" /></h1>
		<h:message for="mainForm" errorClass="error" showSummary="true" />
		<br>
		<h:dataTable value="#{mainbean.bookBeans}" var="book" border="10"
			cellpadding="5" cellspacing="3">
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{msgs.delete}" />
				</f:facet>
				<h:selectBooleanCheckbox value="#{book.markedForDeletion}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{msgs.title}" />
				</f:facet>
				<h:commandLink value="#{book.title}" action="#{mainbean.edit}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{msgs.author}" />
				</f:facet>
				<h:outputText value="#{book.authorName}" />
			</h:column>
		</h:dataTable>
		</br>
		<h:commandButton value="#{msgs.logout}" action="#{mainbean.logout}" />
		<h:commandButton value="#{msgs.updatebooks}"
			action="#{mainbean.updateBooks}" disabled="#{not mainbean.haveBooksToUpdate}" />
		<h:commandButton value="#{msgs.addBooks}"
			action="#{mainbean.addBooks}" />
	</h:form>
	</body>
</f:view>
</html>
