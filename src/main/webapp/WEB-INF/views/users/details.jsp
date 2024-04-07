<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/views/common/head.jsp"%>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

    <div class="content-wrapper">
        <section class="content">

            <div class="row">
                <div class="col-md-3">

                    <div class="box box-primary">
                        <div class="box-body box-profile">
                            <h3 class="profile-username text-center"> ${client.prenom} ${client.nom} (${client.email})</h3>

                            <ul class="list-group list-group-unbordered">
                                <li class="list-group-item">
                                    <b>Réservation(s)</b> <a class="pull-right">${nbReservations}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Voiture(s)</b> <a class="pull-right">${nbVehicles}</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-9">
                    <div class="nav-tabs-custom">
                        <ul class="nav nav-tabs">
                            <li class="active"><a href="#rents" data-toggle="tab">Réservations</a></li>
                            <li><a href="#cars" data-toggle="tab">Voitures</a></li>
                        </ul>
                        <div class="tab-content">
                            <div class="active tab-pane" id="rents">
                                <div class="box-body no-padding">
                                    <table class="table table-striped">
                                        <tr>
                                            <th style="width: 10px">#</th>
                                            <th>Voiture</th>
                                            <th>Date de début</th>
                                            <th>Date de fin</th>
                                        </tr>
                                        <c:forEach items="${reservations}" var="reservation" varStatus="loop">
                                            <tr>
                                                <td>${reservation.ID}</td>
                                                <td>${reservation.vehicleName}</td>
                                                <td>${reservation.debut}</td>
                                                <td>${reservation.fin}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>
                            <div class="tab-pane" id="cars">
                                <div class="box-body no-padding">
                                    <table class="table table-striped">
                                        <tr>
                                            <th style="width: 10px">#</th>
                                            <th>Modèle</th>
                                            <th>Constructeur</th>
                                            <th>Nombre de places</th>
                                        </tr>
                                        <c:forEach items="${vehicles}" var="vehicle" varStatus="loop">
                                            <tr>
                                                <td>${vehicle.ID}.</td>
                                                <td>${vehicle.modele}</td>
                                                <td>${vehicle.constructeur}</td>
                                                <td>${vehicle.nb_place}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>
                        </div>
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
