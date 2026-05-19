<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Notifications - Cotizia</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
            <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
                rel="stylesheet">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
        </head>

        <body>

            <%@ include file="/WEB-INF/views/fragments/sidebar.jspf" %>

                <div class="content">
                    <div class="container-fluid">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h2>Mes Notifications</h2>
                        </div>

                        <div class="row">
                            <c:forEach var="n" items="${listNotif}">
                                <div class="col-md-12 mb-3">
                                    <div
                                        class="card border-start border-4 ${n.lu ? 'border-secondary' : 'border-primary'} p-3">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <h5 class="mb-1">${n.titre}</h5>
                                            <small class="text-muted">${n.dateCreation}</small>
                                        </div>
                                        <p class="mb-0">${n.message}</p>
                                    </div>
                                </div>
                            </c:forEach>
                            <c:if test="${empty listNotif}">
                                <div class="col-md-12">
                                    <div class="card p-4 text-center">
                                        <p class="text-muted">Aucune notification.</p>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>