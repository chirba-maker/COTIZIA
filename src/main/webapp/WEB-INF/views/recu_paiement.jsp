<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <title>Reçu de Paiement - ${paiement.reference}</title>
            <style>
                body {
                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                    color: #333;
                    margin: 0;
                    padding: 40px;
                }

                .receipt-container {
                    max-width: 800px;
                    margin: auto;
                    border: 1px solid #eee;
                    padding: 30px;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
                }

                .header {
                    display: flex;
                    justify-content: space-between;
                    border-bottom: 2px solid #008080;
                    padding-bottom: 20px;
                    margin-bottom: 30px;
                }

                .logo {
                    font-size: 24px;
                    font-weight: bold;
                    color: #008080;
                }

                .receipt-title {
                    font-size: 28px;
                    font-weight: bold;
                    text-align: center;
                    margin-bottom: 40px;
                    text-transform: uppercase;
                    color: #444;
                }

                .info-section {
                    display: flex;
                    justify-content: space-between;
                    margin-bottom: 40px;
                }

                .info-box h4 {
                    margin: 0 0 10px 0;
                    color: #008080;
                    text-transform: uppercase;
                    font-size: 12px;
                }

                .info-box p {
                    margin: 2px 0;
                    font-size: 14px;
                }

                .details-table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-bottom: 40px;
                }

                .details-table th {
                    background-color: #f9f9f9;
                    text-align: left;
                    padding: 12px;
                    border-bottom: 2px solid #eee;
                    font-size: 14px;
                }

                .details-table td {
                    padding: 12px;
                    border-bottom: 1px solid #eee;
                    font-size: 14px;
                }

                .total-row td {
                    font-weight: bold;
                    font-size: 18px;
                    color: #008080;
                    border-top: 2px solid #eee;
                }

                .footer {
                    text-align: center;
                    font-size: 12px;
                    color: #888;
                    margin-top: 50px;
                    border-top: 1px solid #eee;
                    padding-top: 20px;
                }

                @media print {
                    .no-print {
                        display: none;
                    }

                    body {
                        padding: 0;
                    }

                    .receipt-container {
                        border: none;
                        box-shadow: none;
                    }
                }

                .btn-print {
                    background: #008080;
                    color: white;
                    border: none;
                    padding: 10px 20px;
                    border-radius: 5px;
                    cursor: pointer;
                    font-weight: bold;
                }
            </style>
        </head>

        <body>
            <div class="no-print" style="text-align: right; margin-bottom: 20px;">
                <button class="btn-print" onclick="window.print()">Imprimer le Reçu</button>
                <button class="btn-print" style="background: #666;" onclick="window.close()">Fermer</button>
            </div>

            <div class="receipt-container">
                <div class="header">
                    <div class="logo">COTIZIA</div>
                    <div style="text-align: right;">
                        <p style="margin: 0; font-weight: bold;">Réf: ${paiement.reference}</p>
                        <p style="margin: 0;">Date: ${paiement.datePaiement}</p>
                    </div>
                </div>

                <div class="receipt-title">Reçu de Paiement</div>

                <div class="info-section">
                    <div class="info-box">
                        <h4>Adhérent</h4>
                        <p><strong>${paiement.echeance.participant.adherent.nomComplet}</strong></p>
                        <p>ID: ${paiement.echeance.participant.adherent.numeroIdentification}</p>
                        <p>Tél: ${paiement.echeance.participant.adherent.telephone}</p>
                    </div>
                    <div class="info-box" style="text-align: right;">
                        <h4>Cycle de Cotisation</h4>
                        <p>${paiement.echeance.cycle.libelle}</p>
                        <p>Tour N°: ${paiement.echeance.numeroTour}</p>
                        <p>Mode: ${paiement.modePaiement}</p>
                    </div>
                </div>

                <table class="details-table">
                    <thead>
                        <tr>
                            <th>Description</th>
                            <th style="text-align: right;">Montant</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Cotisation pour l'échéance du ${paiement.echeance.dateEcheance}</td>
                            <td style="text-align: right;">${paiement.montant} GNF</td>
                        </tr>
                        <tr class="total-row">
                            <td>TOTAL VERSÉ</td>
                            <td style="text-align: right;">${paiement.montant} GNF</td>
                        </tr>
                    </tbody>
                </table>

                <div style="margin-top: 40px;">
                    <p style="font-style: italic; font-size: 14px;">Arrêté le présent reçu à la somme de :
                        <strong>${paiement.montant} GNF</strong></p>
                </div>

                <div style="display: flex; justify-content: space-between; margin-top: 60px;">
                    <div style="text-align: center; width: 40%;">
                        <p style="text-decoration: underline; margin-bottom: 60px;">Signature du Collecteur</p>
                        <p><strong>${paiement.echeance.cycle.collecteur.nomComplet}</strong></p>
                    </div>
                    <div style="text-align: center; width: 40%;">
                        <p style="text-decoration: underline; margin-bottom: 60px;">Signature de l'Adhérent</p>
                        <p><strong>${paiement.echeance.participant.adherent.nomComplet}</strong></p>
                    </div>
                </div>

                <div class="footer">
                    <p>Cotizia - Système de Gestion de Cotisation</p>
                    <p>Généré le <%= new java.util.Date() %>
                    </p>
                </div>
            </div>
        </body>

        </html>