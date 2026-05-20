/* 
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
*/
/**
 * Author:  Major117
 * Created: 24 févr. 2026
 */

-- DROP DATABASE IF EXISTS gestion_cotisation;

-- CREATE DATABASE gestion_cotisation CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- USE gestion_cotisation;

CREATE TABLE utilisateur (
    id_utilisateur INT NOT NULL AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    role ENUM(
        'admin',
        'collecteur',
        'consultant'
    ) NOT NULL DEFAULT 'consultant',
    actif TINYINT(1) NOT NULL DEFAULT 1,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_utilisateur),
    INDEX idx_login (login),
    INDEX idx_role (role)
) ENGINE = InnoDB;

CREATE TABLE collecteur (
    id_collecteur INT NOT NULL AUTO_INCREMENT,
    id_utilisateur INT NOT NULL,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(150) NULL UNIQUE,
    zone_collecte VARCHAR(150) NULL,
    statut ENUM(
        'actif',
        'suspendu',
        'inactif'
    ) NOT NULL DEFAULT 'actif',
    date_enregistrement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_collecteur),
    CONSTRAINT fk_collecteur_utilisateur FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id_utilisateur) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_collecteur_statut (statut)
) ENGINE = InnoDB;

CREATE TABLE adherent (
    id_adherent INT NOT NULL AUTO_INCREMENT,
    numero_identification VARCHAR(50) NOT NULL UNIQUE,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE NOT NULL,
    adresse TEXT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(150) NULL UNIQUE,
    profession VARCHAR(100) NULL,
    employeur VARCHAR(150) NULL,
    revenus_estimes DECIMAL(12, 2) NULL,
    statut ENUM('actif', 'suspendu', 'radie') NOT NULL DEFAULT 'actif',
    date_adhesion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_adherent),
    INDEX idx_adherent_statut (statut),
    INDEX idx_adherent_nom (nom, prenom)
) ENGINE = InnoDB;

CREATE TABLE cycle (
    id_cycle INT NOT NULL AUTO_INCREMENT,
    id_collecteur INT NOT NULL,
    libelle VARCHAR(150) NOT NULL,
    description TEXT NULL,
    montant_cotisation DECIMAL(12, 2) NOT NULL,
    frequence ENUM(
        'hebdomadaire',
        'bimensuelle',
        'mensuelle'
    ) NOT NULL DEFAULT 'mensuelle',
    date_debut DATE NOT NULL,
    nombre_tours INT NOT NULL,
    statut ENUM(
        'cree',
        'ouvert',
        'actif',
        'suspendu',
        'termine',
        'annule',
        'cloture'
    ) NOT NULL DEFAULT 'cree',
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    support_demande TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id_cycle),
    CONSTRAINT fk_cycle_collecteur FOREIGN KEY (id_collecteur) REFERENCES collecteur (id_collecteur) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_cycle_statut (statut),
    INDEX idx_cycle_collecteur (id_collecteur)
) ENGINE = InnoDB;

CREATE TABLE participant (
    id_participant INT NOT NULL AUTO_INCREMENT,
    id_cycle INT NOT NULL,
    id_adherent INT NOT NULL,
    numero_ordre INT NOT NULL,
    statut ENUM(
        'inscrit',
        'actif',
        'retire',
        'suspendu'
    ) NOT NULL DEFAULT 'inscrit',
    montant_recu DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    date_inscription TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_participant),
    CONSTRAINT fk_participant_cycle FOREIGN KEY (id_cycle) REFERENCES cycle (id_cycle) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_participant_adherent FOREIGN KEY (id_adherent) REFERENCES adherent (id_adherent) ON DELETE RESTRICT ON UPDATE CASCADE,
    UNIQUE KEY uq_participant_cycle_adherent (id_cycle, id_adherent),
    UNIQUE KEY uq_participant_ordre (id_cycle, numero_ordre),
    INDEX idx_participant_statut (statut)
) ENGINE = InnoDB;

CREATE TABLE echeance (
    id_echeance INT NOT NULL AUTO_INCREMENT,
    id_participant INT NOT NULL,
    id_cycle INT NOT NULL,
    numero_tour INT NOT NULL,
    date_echeance DATE NOT NULL,
    montant_du DECIMAL(12, 2) NOT NULL,
    montant_paye DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    statut ENUM(
        'en_attente',
        'paye',
        'partiel',
        'impaye'
    ) NOT NULL DEFAULT 'en_attente',
    date_paiement TIMESTAMP NULL,
    commentaire TEXT NULL,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_echeance),
    CONSTRAINT fk_echeance_participant FOREIGN KEY (id_participant) REFERENCES participant (id_participant) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_echeance_cycle FOREIGN KEY (id_cycle) REFERENCES cycle (id_cycle) ON DELETE RESTRICT ON UPDATE CASCADE,
    UNIQUE KEY uq_echeance_participant_tour (id_participant, numero_tour),
    INDEX idx_echeance_statut (statut),
    INDEX idx_echeance_date (date_echeance),
    INDEX idx_echeance_cycle (id_cycle)
) ENGINE = InnoDB;

CREATE TABLE paiement (
    id_paiement INT NOT NULL AUTO_INCREMENT,
    id_echeance INT NOT NULL,
    id_utilisateur INT NOT NULL,
    montant DECIMAL(12, 2) NOT NULL,
    mode_paiement ENUM(
        'especes',
        'mobile_money',
        'virement',
        'cheque'
    ) NOT NULL DEFAULT 'especes',
    reference VARCHAR(100) NULL,
    note TEXT NULL,
    date_paiement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_paiement),
    CONSTRAINT fk_paiement_echeance FOREIGN KEY (id_echeance) REFERENCES echeance (id_echeance) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_paiement_utilisateur FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id_utilisateur) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_paiement_echeance (id_echeance),
    INDEX idx_paiement_date (date_paiement)
) ENGINE = InnoDB;

CREATE TABLE notification (
    id_notification INT NOT NULL AUTO_INCREMENT,
    id_utilisateur INT NOT NULL,
    type ENUM(
        'echeance_proche',
        'impaye',
        'systeme',
        'info'
    ) NOT NULL DEFAULT 'info',
    titre VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    lu TINYINT(1) NOT NULL DEFAULT 0,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_lecture TIMESTAMP NULL,
    PRIMARY KEY (id_notification),
    CONSTRAINT fk_notification_utilisateur FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id_utilisateur) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_notification_utilisateur (id_utilisateur),
    INDEX idx_notification_lu (lu)
) ENGINE = InnoDB;

CREATE TABLE mouchard (
    id_mouchard INT NOT NULL AUTO_INCREMENT,
    id_utilisateur INT NULL,
    action VARCHAR(100) NOT NULL,
    entite VARCHAR(50) NOT NULL,
    id_entite INT NULL,
    detail_avant JSON NULL,
    detail_apres JSON NULL,
    adresse_ip VARCHAR(45) NULL,
    user_agent VARCHAR(255) NULL,
    date_action TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_mouchard),
    CONSTRAINT fk_mouchard_utilisateur FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id_utilisateur) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_mouchard_utilisateur (id_utilisateur),
    INDEX idx_mouchard_entite (entite, id_entite),
    INDEX idx_mouchard_date (date_action)
) ENGINE = InnoDB;

CREATE OR REPLACE VIEW v_echeances_cycle AS
SELECT
    c.id_cycle,
    c.libelle AS cycle_libelle,
    c.montant_cotisation,
    COUNT(e.id_echeance) AS total_echeances,
    SUM(
        CASE
            WHEN e.statut = 'paye' THEN 1
            ELSE 0
        END
    ) AS nb_payees,
    SUM(
        CASE
            WHEN e.statut = 'partiel' THEN 1
            ELSE 0
        END
    ) AS nb_partielles,
    SUM(
        CASE
            WHEN e.statut = 'impaye' THEN 1
            ELSE 0
        END
    ) AS nb_impayees,
    SUM(
        CASE
            WHEN e.statut = 'en_attente' THEN 1
            ELSE 0
        END
    ) AS nb_en_attente,
    COALESCE(SUM(e.montant_paye), 0) AS total_collecte,
    COALESCE(
        SUM(e.montant_du - e.montant_paye),
        0
    ) AS total_restant
FROM
    cycle c
    LEFT JOIN participant p ON p.id_cycle = c.id_cycle
    LEFT JOIN echeance e ON e.id_participant = p.id_participant
GROUP BY
    c.id_cycle,
    c.libelle,
    c.montant_cotisation;

CREATE OR REPLACE VIEW v_solde_participant AS
SELECT
    p.id_participant,
    p.id_cycle,
    p.numero_ordre,
    p.statut AS statut_participant,
    a.id_adherent,
    CONCAT(a.nom, ' ', a.prenom) AS adherent_nom_complet,
    a.telephone,
    c.libelle AS cycle_libelle,
    c.montant_cotisation,
    COUNT(e.id_echeance) AS nb_echeances,
    COALESCE(SUM(e.montant_du), 0) AS total_du,
    COALESCE(SUM(e.montant_paye), 0) AS total_paye,
    COALESCE(
        SUM(e.montant_du - e.montant_paye),
        0
    ) AS solde_restant,
    SUM(
        CASE
            WHEN e.statut = 'impaye' THEN 1
            ELSE 0
        END
    ) AS nb_impayees
FROM
    participant p
    JOIN adherent a ON a.id_adherent = p.id_adherent
    JOIN cycle c ON c.id_cycle = p.id_cycle
    LEFT JOIN echeance e ON e.id_participant = p.id_participant
GROUP BY
    p.id_participant,
    p.id_cycle,
    p.numero_ordre,
    p.statut,
    a.id_adherent,
    a.nom,
    a.prenom,
    a.telephone,
    c.libelle,
    c.montant_cotisation;

CREATE OR REPLACE VIEW v_activite_recente AS
SELECT m.id_mouchard, m.date_action, m.action, m.entite, m.id_entite, m.adresse_ip, CONCAT(u.nom, ' ', u.prenom) AS utilisateur, u.role
FROM mouchard m
    LEFT JOIN utilisateur u ON u.id_utilisateur = m.id_utilisateur
WHERE
    m.date_action >= DATE_SUB(NOW(), INTERVAL 30 DAY)
ORDER BY m.date_action DESC;

INSERT INTO
    utilisateur (
        nom,
        prenom,
        login,
        email,
        mot_de_passe,
        role
    )
VALUES (
        'Admin',
        'Système',
        'admin',
        'admin@cotisation.local',
        'admin',
        'admin'
    );

INSERT INTO
    utilisateur (
        nom,
        prenom,
        login,
        email,
        mot_de_passe,
        role
    )
VALUES (
        'Diallo',
        'Mamadou',
        'mdiallo',
        'mdiallo@cotisation.local',
        'pass123',
        'collecteur'
    ),
    (
        'Bah',
        'Fatoumata',
        'fbah',
        'fbah@cotisation.local',
        'pass123',
        'collecteur'
    ),
    (
        'Barry',
        'Ibrahima',
        'ibarry',
        'ibarry@cotisation.local',
        'pass123',
        'consultant'
    );

INSERT INTO
    collecteur (
        id_utilisateur,
        nom,
        prenom,
        telephone,
        email,
        zone_collecte,
        statut
    )
VALUES (
        2,
        'Diallo',
        'Mamadou',
        '+224 620 00 01 01',
        'mdiallo@gmail.com',
        'Conakry - Matam',
        'actif'
    ),
    (
        3,
        'Bah',
        'Fatoumata',
        '+224 620 00 02 02',
        'fbah@gmail.com',
        'Conakry - Ratoma',
        'actif'
    );

INSERT INTO
    adherent (
        numero_identification,
        nom,
        prenom,
        date_naissance,
        telephone,
        email,
        profession,
        statut
    )
VALUES (
        'CIN-001',
        'Camara',
        'Aliou',
        '1985-03-12',
        '+224 621 11 11 01',
        'aliou@mail.com',
        'Commerçant',
        'actif'
    ),
    (
        'CIN-002',
        'Sylla',
        'Aminata',
        '1990-07-25',
        '+224 621 22 22 02',
        'aminata@mail.com',
        'Enseignante',
        'actif'
    ),
    (
        'CIN-003',
        'Kouyaté',
        'Sékou',
        '1978-11-03',
        '+224 621 33 33 03',
        'sekou@mail.com',
        'Fonctionnaire',
        'actif'
    ),
    (
        'CIN-004',
        'Traoré',
        'Mariama',
        '1995-01-18',
        '+224 621 44 44 04',
        'mariama@mail.com',
        'Infirmière',
        'actif'
    ),
    (
        'CIN-005',
        'Baldé',
        'Oumar',
        '1982-09-07',
        '+224 621 55 55 05',
        'oumar@mail.com',
        'Chauffeur',
        'actif'
    ),
    (
        'CIN-006',
        'Condé',
        'Kadiatou',
        '1988-05-30',
        '+224 621 66 66 06',
        'kadiatou@mail.com',
        'Commerçante',
        'actif'
    );

INSERT INTO
    cycle (
        id_collecteur,
        libelle,
        description,
        montant_cotisation,
        frequence,
        date_debut,
        nombre_tours,
        statut
    )
VALUES (
        1,
        'Tontine Matam Janvier 2026',
        'Cycle mensuel du groupe de Matam',
        50000.00,
        'mensuelle',
        '2026-01-01',
        6,
        'actif'
    ),
    (
        1,
        'Tontine Hebdo Matam',
        'Cycle hebdomadaire petit commerce',
        10000.00,
        'hebdomadaire',
        '2026-02-01',
        8,
        'actif'
    ),
    (
        2,
        'Épargne Ratoma Mars 2026',
        'Cycle mensuel du groupe Ratoma',
        75000.00,
        'mensuelle',
        '2026-03-01',
        5,
        'actif'
    );

INSERT INTO
    participant (
        id_cycle,
        id_adherent,
        numero_ordre,
        statut
    )
VALUES (1, 1, 1, 'actif'),
    (1, 2, 2, 'actif'),
    (1, 3, 3, 'actif'),
    (1, 4, 4, 'actif'),
    (1, 5, 5, 'actif'),
    (1, 6, 6, 'actif');

INSERT INTO
    participant (
        id_cycle,
        id_adherent,
        numero_ordre,
        statut
    )
VALUES (2, 1, 1, 'actif'),
    (2, 2, 2, 'actif'),
    (2, 3, 3, 'actif'),
    (2, 4, 4, 'actif');

INSERT INTO
    participant (
        id_cycle,
        id_adherent,
        numero_ordre,
        statut
    )
VALUES (3, 2, 1, 'actif'),
    (3, 3, 2, 'actif'),
    (3, 4, 3, 'actif'),
    (3, 5, 4, 'actif'),
    (3, 6, 5, 'actif');

INSERT INTO
    echeance (
        id_participant,
        id_cycle,
        numero_tour,
        date_echeance,
        montant_du,
        montant_paye,
        statut
    )
VALUES (
        1,
        1,
        1,
        '2026-01-31',
        50000.00,
        50000.00,
        'paye'
    ),
    (
        1,
        1,
        2,
        '2026-02-28',
        50000.00,
        50000.00,
        'paye'
    ),
    (
        1,
        1,
        3,
        '2026-03-31',
        50000.00,
        30000.00,
        'partiel'
    ),
    (
        1,
        1,
        4,
        '2026-04-30',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        1,
        1,
        5,
        '2026-05-31',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        1,
        1,
        6,
        '2026-06-30',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        2,
        1,
        1,
        '2026-01-31',
        50000.00,
        50000.00,
        'paye'
    ),
    (
        2,
        1,
        2,
        '2026-02-28',
        50000.00,
        50000.00,
        'paye'
    ),
    (
        2,
        1,
        3,
        '2026-03-31',
        50000.00,
        50000.00,
        'paye'
    ),
    (
        2,
        1,
        4,
        '2026-04-30',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        2,
        1,
        5,
        '2026-05-31',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        2,
        1,
        6,
        '2026-06-30',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        3,
        1,
        1,
        '2026-01-31',
        50000.00,
        50000.00,
        'paye'
    ),
    (
        3,
        1,
        2,
        '2026-02-28',
        50000.00,
        0.00,
        'impaye'
    ),
    (
        3,
        1,
        3,
        '2026-03-31',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        3,
        1,
        4,
        '2026-04-30',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        3,
        1,
        5,
        '2026-05-31',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        3,
        1,
        6,
        '2026-06-30',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        4,
        1,
        1,
        '2026-01-31',
        50000.00,
        50000.00,
        'paye'
    ),
    (
        4,
        1,
        2,
        '2026-02-28',
        50000.00,
        50000.00,
        'paye'
    ),
    (
        4,
        1,
        3,
        '2026-03-31',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        4,
        1,
        4,
        '2026-04-30',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        4,
        1,
        5,
        '2026-05-31',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        4,
        1,
        6,
        '2026-06-30',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        5,
        1,
        1,
        '2026-01-31',
        50000.00,
        50000.00,
        'paye'
    ),
    (
        5,
        1,
        2,
        '2026-02-28',
        50000.00,
        50000.00,
        'paye'
    ),
    (
        5,
        1,
        3,
        '2026-03-31',
        50000.00,
        50000.00,
        'paye'
    ),
    (
        5,
        1,
        4,
        '2026-04-30',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        5,
        1,
        5,
        '2026-05-31',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        5,
        1,
        6,
        '2026-06-30',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        6,
        1,
        1,
        '2026-01-31',
        50000.00,
        0.00,
        'impaye'
    ),
    (
        6,
        1,
        2,
        '2026-02-28',
        50000.00,
        0.00,
        'impaye'
    ),
    (
        6,
        1,
        3,
        '2026-03-31',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        6,
        1,
        4,
        '2026-04-30',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        6,
        1,
        5,
        '2026-05-31',
        50000.00,
        0.00,
        'en_attente'
    ),
    (
        6,
        1,
        6,
        '2026-06-30',
        50000.00,
        0.00,
        'en_attente'
    );

INSERT INTO
    paiement (
        id_echeance,
        id_utilisateur,
        montant,
        mode_paiement,
        reference
    )
VALUES (
        1,
        2,
        50000.00,
        'especes',
        NULL
    ),
    (
        2,
        2,
        50000.00,
        'mobile_money',
        'MM-2026-0001'
    ),
    (
        3,
        2,
        30000.00,
        'especes',
        NULL
    ),
    (
        7,
        2,
        50000.00,
        'especes',
        NULL
    ),
    (
        8,
        2,
        50000.00,
        'especes',
        NULL
    ),
    (
        9,
        2,
        50000.00,
        'mobile_money',
        'MM-2026-0002'
    ),
    (
        13,
        2,
        50000.00,
        'especes',
        NULL
    ),
    (
        19,
        2,
        50000.00,
        'especes',
        NULL
    ),
    (
        20,
        2,
        50000.00,
        'mobile_money',
        'MM-2026-0003'
    ),
    (
        25,
        2,
        50000.00,
        'especes',
        NULL
    ),
    (
        26,
        2,
        50000.00,
        'especes',
        NULL
    ),
    (
        27,
        2,
        50000.00,
        'especes',
        NULL
    );

INSERT INTO
    mouchard (
        id_utilisateur,
        action,
        entite,
        id_entite,
        adresse_ip
    )
VALUES (
        1,
        'CREATE_UTILISATEUR',
        'utilisateur',
        2,
        '192.168.1.1'
    ),
    (
        1,
        'CREATE_UTILISATEUR',
        'utilisateur',
        3,
        '192.168.1.1'
    ),
    (
        2,
        'CREATE_CYCLE',
        'cycle',
        1,
        '192.168.1.10'
    ),
    (
        2,
        'CREATE_CYCLE',
        'cycle',
        2,
        '192.168.1.10'
    ),
    (
        2,
        'ENREGISTRER_PAIEMENT',
        'paiement',
        1,
        '192.168.1.10'
    ),
    (
        2,
        'ENREGISTRER_PAIEMENT',
        'paiement',
        2,
        '192.168.1.10'
    ),
    (
        3,
        'CREATE_CYCLE',
        'cycle',
        3,
        '192.168.1.20'
    );

INSERT INTO
    notification (
        id_utilisateur,
        type,
        titre,
        message
    )
VALUES (
        2,
        'impaye',
        'Impayé détecté',
        'Kouyaté Sékou n a pas payé l échéance du 28/02/2026'
    ),
    (
        2,
        'impaye',
        'Impayé détecté',
        'Condé Kadiatou a 2 échéances impayées'
    ),
    (
        2,
        'echeance_proche',
        'Échéance dans 5 jours',
        'Tour 4 du cycle Tontine Matam arrive à échéance le 30/04/2026'
    ),
    (
        1,
        'systeme',
        'Nouveau collecteur',
        'Le collecteur Bah Fatoumata a été enregistré avec succès'
    );

SELECT 'TABLES CRÉÉES' AS info;

SHOW TABLES;

SELECT 'RÉSUMÉ DES DONNÉES' AS info;

SELECT 'utilisateur' AS table_name, COUNT(*) AS nb_lignes
FROM utilisateur
UNION ALL
SELECT 'collecteur', COUNT(*)
FROM collecteur
UNION ALL
SELECT 'adherent', COUNT(*)
FROM adherent
UNION ALL
SELECT 'cycle', COUNT(*)
FROM cycle
UNION ALL
SELECT 'participant', COUNT(*)
FROM participant
UNION ALL
SELECT 'echeance', COUNT(*)
FROM echeance
UNION ALL
SELECT 'paiement', COUNT(*)
FROM paiement
UNION ALL
SELECT 'mouchard', COUNT(*)
FROM mouchard
UNION ALL
SELECT 'notification', COUNT(*)
FROM notification;

SELECT 'VUE : SOLDE PARTICIPANTS (Cycle 1)' AS info;

SELECT
    adherent_nom_complet,
    numero_ordre,
    total_du,
    total_paye,
    solde_restant,
    nb_impayees
FROM v_solde_participant
WHERE
    id_cycle = 1
ORDER BY numero_ordre;

ALTER TABLE utilisateur ADD COLUMN photo LONGTEXT NULL;