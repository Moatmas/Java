<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
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
                Voitures
            </h1>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="box">
                        <form class="form-horizontal" method="post" action="<%= (request.getAttribute("vehicle") != null) ? "/rentmanager/cars/modifier" : "/rentmanager/cars/create" %>">
                            <% if (request.getAttribute("vehicle") != null) { %>
                            <input type="hidden" name="vehicleId" value="${vehicle.ID}" />
                            <% if (session.getAttribute("error") != null) { %>
                            <div class="alert alert-danger" role="alert">
                                <%= session.getAttribute("error") %>
                            </div>
                            <% session.removeAttribute("error"); %>
                            <% } %>
                            <% } %>
                            <div class="box-body">
                                <div class="form-group">
                                    <label for="manufacturer" class="col-sm-2 control-label">Marque</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="manufacturer" name="manufacturer" placeholder="Marque" required <% if (request.getAttribute("vehicle") != null) { %> value="${ vehicle.constructeur}"<% } %>>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="modele" class="col-sm-2 control-label">Modele</label>

                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="modele" name="modele" placeholder="Modele" required <% if (request.getAttribute("vehicle") != null) { %> value="${ vehicle.modele}"<% } %>>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="seats" class="col-sm-2 control-label">Nombre de places</label>

                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="seats" name="seats" placeholder="Nombre de places" required <% if (request.getAttribute("vehicle") != null) { %> value="${ vehicle.nb_place}"<% } %>>
                                        <% if (request.getAttribute("error_place") != null) { %>
                                        <span class="text-danger"><%= request.getAttribute("error_place") %></span>
                                        <% } %>
                                    </div>
                                </div>
                            </div>
                            <div class="box-footer">
                                <button type="submit" class="btn btn-info pull-right"><%= (request.getAttribute("vehicle") != null) ? "Modifier" : "Ajouter" %></button>
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
</body>
</html>