<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/views/common/head.jsp" %>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                Reservations
            </h1>
        </section>

        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="box">
                        <form class="form-horizontal" method="post" action="<%= (request.getAttribute("reservation") != null) ? "/rentmanager/rents/modifier" : "/rentmanager/rents/create" %>">
                            <% if (request.getAttribute("reservation") != null) { %>
                            <input type="hidden" name="reservationId" value="${reservation.ID}" />
                            <% if (session.getAttribute("error_date") != null) { %>
                            <div class="alert alert-danger" role="alert">
                                <%= session.getAttribute("error_date") %>
                            </div>
                            <% session.removeAttribute("error_date"); %>
                            <% } %>
                            <% } %>
                            <div class="box-body">
                                <% if (request.getAttribute("reservation") == null) { %>
                                <div class="form-group">
                                    <label for="car" class="col-sm-2 control-label">Voiture</label>

                                    <div class="col-sm-10">
                                        <select class="form-control" id="car" name="car">
                                            <c:if test="${not empty cars}">
                                                <c:forEach items="${cars}" var="car">
                                                    <option value="${car.ID}">${car.constructeur} ${car.modele}</option>
                                                </c:forEach>
                                            </c:if>

                                        </select>

                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="client" class="col-sm-2 control-label">Client</label>

                                    <div class="col-sm-10">
                                        <select class="form-control" id="client" name="client">
                                            <c:if test="${not empty clients}">
                                                <c:forEach items="${clients}" var="client">
                                                    <option value="${client.ID}">${client.nom} ${client.prenom}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                    </div>
                                </div>
                                <% } %>
                                <div class="form-group">
                                    <label for="begin" class="col-sm-2 control-label">Date de début</label>

                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="begin" name="begin" required
                                               data-inputmask="'alias': 'dd/mm/yyyy'" data-mask>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="end" class="col-sm-2 control-label">Date de fin</label>

                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="end" name="end" required
                                               data-inputmask="'alias': 'dd/mm/yyyy'" data-mask>
                                        <% if (request.getAttribute("error_date") != null) { %>
                                        <span class="text-danger"><%= request.getAttribute("error_date") %></span>
                                        <% } %>
                                    </div>
                                </div>
                            </div>
                            <div class="box-footer">
                                <button type="submit" class="btn btn-info pull-right"><%= (request.getAttribute("reservation") != null) ? "Modifier" : "Ajouter" %></button>
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
