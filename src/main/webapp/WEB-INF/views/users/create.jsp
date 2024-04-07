<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/views/common/head.jsp"%>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                Utilisateurs
            </h1>
        </section>

        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="box">
                        <form class="form-horizontal" method="post" action="<%= (request.getAttribute("client") != null) ? "/rentmanager/users/modifier" : "/rentmanager/users/create" %>">
                            <% if (request.getAttribute("client") != null) { %>
                            <input type="hidden" name="clientId" value="${client.ID}" />
                            <% if (session.getAttribute("error") != null) { %>
                            <div class="alert alert-danger" role="alert">
                                <%= session.getAttribute("error") %>
                            </div>
                            <% session.removeAttribute("error"); %>
                            <% } %>
                            <% } %>
                            <div class="box-body">
                                <div class="form-group">
                                    <label for="last_name" class="col-sm-2 control-label">Nom</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="last_name" name="last_name" required placeholder="Nom" <% if (request.getAttribute("client") != null) { %> value="${ client.nom}"<% } %> />
                                        <% if (request.getAttribute("error_nom") != null) { %>
                                        <span class="text-danger"><%= request.getAttribute("error_nom") %></span>
                                        <% } %>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="first_name" class="col-sm-2 control-label">Prénom</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="first_name" name="first_name" required placeholder="Prénom" <% if (request.getAttribute("client") != null) { %> value="${ client.prenom}"<% } %>/>
                                        <% if (request.getAttribute("error_prenom") != null) { %>
                                        <span class="text-danger"><%= request.getAttribute("error_prenom") %></span>
                                        <% } %>
                                    </div>
                                </div>
                                <% if (request.getAttribute("client") == null) { %>
                                <div class="form-group">
                                    <label for="email" class="col-sm-2 control-label">Email</label>
                                    <div class="col-sm-10">
                                        <input type="email" class="form-control" id="email" name="email" required placeholder="Email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$" title="Veuillez entrer une adresse e-mail valide (ex: utilisateur@exemple.com)" />
                                        <% if (request.getAttribute("error_mail") != null) { %>
                                        <span class="text-danger"><%= request.getAttribute("error_mail") %></span>
                                        <% } %>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="naissance" class="col-sm-2 control-label">Naissance</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="naissance" name="naissance" required data-inputmask="'alias': 'dd/mm/yyyy'" data-mask />
                                        <!-- Gestion des erreurs -->
                                        <% if (request.getAttribute("error_age") != null) { %>
                                        <span class="text-danger"><%= request.getAttribute("error_age") %></span>
                                        <% } %>
                                    </div>
                                </div>
                                <% } %>
                            </div>
                            <div class="box-footer">
                                <button type="submit" class="btn btn-info pull-right"><%= (request.getAttribute("client") != null) ? "Modifier" : "Ajouter" %></button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>


<%@ include file="/WEB-INF/views/common/js_imports.jsp" %>

<script src="${pageContext.request.contextPath}/resources/plugins/input-mask/jquery.inputmask.js"></script>
<script src="${pageContext.request.contextPath}/resources/plugins/input-mask/jquery.inputmask.date.extensions.js"></script>
<script src="${pageContext.request.contextPath}/resources/plugins/input-mask/jquery.inputmask.extensions.js"></script>
<script>
    $(function () {
        $('[data-mask]').inputmask()
    });
</script>

</body>
</html>
