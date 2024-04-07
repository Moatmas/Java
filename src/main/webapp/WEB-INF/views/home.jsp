<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

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
        Dashboard
      </h1>
    </section>

    <section class="content">
      <div class="row">
        <div class="col-md-3 col-sm-6 col-xs-12">
          <div class="info-box">
            <span class="info-box-icon bg-aqua"><i class="fa fa-user"></i></span>

            <div class="info-box-content">
              <span class="info-box-text">Utilisateurs</span>
              <span class="info-box-number">${totalClients}</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-sm-6 col-xs-12">
          <div class="info-box">
            <span class="info-box-icon bg-red"><i class="fa fa-car"></i></span>

            <div class="info-box-content">
              <span class="info-box-text">Voitures</span>
              <span class="info-box-number">${totalVehicles}</span>
            </div>
          </div>
        </div>
        <div class="clearfix visible-sm-block"></div>

        <div class="col-md-3 col-sm-6 col-xs-12">
          <div class="info-box">
            <span class="info-box-icon bg-green"><i class="fa fa-pencil"></i></span>

            <div class="info-box-content">
              <span class="info-box-text">Reservations</span>
              <span class="info-box-number">${totalReservation}</span>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>

   <jsp:include page='/WEB-INF/views/common/footer.jsp'></jsp:include>


</div>
   <%@ include file="/WEB-INF/views/common/js_imports.jsp" %>

</body>
</html>