-- Active: 1751743068514@@127.0.0.1@3306@crm
-- =====================================================
-- INSERTION DE DONNÉES COHÉRENTES POUR LE CRM
-- =====================================================

-- 1. AJOUT D'AUTRES UTILISATEURS (MANAGERS ET EMPLOYÉS)
-- =====================================================
INSERT INTO `users` (`id`, `email`, `password`, `hire_date`, `created_at`, `updated_at`, `username`, `status`, `token`, `is_password_set`) VALUES
(53, 'sophie.martin@crm.com', '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', '2024-01-15 09:00:00', NOW(), NOW(), 'sophie.martin', 'active', NULL, 1),
(54, 'thomas.bernard@crm.com', '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', '2024-02-01 09:00:00', NOW(), NOW(), 'thomas.bernard', 'active', NULL, 1),
(55, 'emma.petit@crm.com', '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', '2024-03-10 09:00:00', NOW(), NOW(), 'emma.petit', 'active', NULL, 1),
(56, 'lucas.dubois@crm.com', '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', '2024-04-05 09:00:00', NOW(), NOW(), 'lucas.dubois', 'active', NULL, 1),
(57, 'chloe.leroy@crm.com', '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', '2024-05-20 09:00:00', NOW(), NOW(), 'chloe.leroy', 'active', NULL, 1),
(58, 'hugo.mercier@crm.com', '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', '2024-06-15 09:00:00', NOW(), NOW(), 'hugo.mercier', 'active', NULL, 1),
(59, 'julie.renaud@crm.com', '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', '2024-07-01 09:00:00', NOW(), NOW(), 'julie.renaud', 'active', NULL, 1),
(60, 'maxime.girard@crm.com', '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', '2024-08-15 09:00:00', NOW(), NOW(), 'maxime.girard', 'active', NULL, 1);

-- 2. PROFILS UTILISATEURS
-- =====================================================
INSERT INTO `user_profile` (`id`, `first_name`, `last_name`, `phone`, `department`, `salary`, `status`, `user_id`, `country`, `position`, `address`) VALUES
(34, 'Sophie', 'Martin', '+33 6 12 34 56 78', 'Sales', 45000.00, 'active', 53, 'France', 'Sales Manager', '15 rue des Lilas, 75001 Paris'),
(35, 'Thomas', 'Bernard', '+33 6 23 45 67 89', 'Sales', 38000.00, 'active', 54, 'France', 'Senior Sales Rep', '22 avenue Victor Hugo, 69002 Lyon'),
(36, 'Emma', 'Petit', '+33 6 34 56 78 90', 'Support', 35000.00, 'active', 55, 'France', 'Support Manager', '8 place de la République, 13001 Marseille'),
(37, 'Lucas', 'Dubois', '+33 6 45 67 89 01', 'Support', 32000.00, 'active', 56, 'France', 'Support Agent', '45 rue Nationale, 59000 Lille'),
(38, 'Chloe', 'Leroy', '+33 6 56 78 90 12', 'Sales', 34000.00, 'active', 57, 'France', 'Sales Rep', '12 cours Mirabeau, 13100 Aix-en-Provence'),
(39, 'Hugo', 'Mercier', '+33 6 67 89 01 23', 'Sales', 36000.00, 'active', 58, 'France', 'Sales Rep', '78 rue de la République, 33000 Bordeaux'),
(40, 'Julie', 'Renaud', '+33 6 78 90 12 34', 'Support', 33000.00, 'active', 59, 'France', 'Support Agent', '3 quai de la Fontaine, 84000 Avignon'),
(41, 'Maxime', 'Girard', '+33 6 89 01 23 45', 'Sales', 37000.00, 'active', 60, 'France', 'Senior Sales Rep', '56 rue des Remparts, 44000 Nantes');

-- 3. ASSIGNATION DES RÔLES
-- =====================================================
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(53, 1), -- Sophie Martin - Manager
(54, 2), -- Thomas Bernard - Employee
(55, 1), -- Emma Petit - Manager
(56, 2), -- Lucas Dubois - Employee
(57, 2), -- Chloe Leroy - Employee
(58, 2), -- Hugo Mercier - Employee
(59, 2), -- Julie Renaud - Employee
(60, 2); -- Maxime Girard - Employee

-- 4. INFORMATIONS DE CONNEXION CLIENTS
-- =====================================================
INSERT INTO `customer_login_info` (`id`, `password`, `username`, `token`, `password_set`) VALUES
(19, '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', 'jean.dupont', 'token_cust_001_', 1),
(20, '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', 'marie.lambert', 'token_cust_002_', 1),
(21, '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', 'pierre.martin', 'token_cust_003_', 1),
(22, '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', 'isabelle.renaud', 'token_cust_004_', 1),
(23, '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', 'nicolas.garcia', 'token_cust_005_', 1),
(24, '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', 'sarah.bernard', 'token_cust_006_', 1),
(25, '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', 'antoine.robert', 'token_cust_007_', 1),
(26, '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', 'laurence.moreau', 'token_cust_008_', 1);

-- 5. CLIENTS
-- =====================================================
INSERT INTO `customer` (`customer_id`, `name`, `phone`, `address`, `city`, `state`, `country`, `user_id`, `description`, `position`, `created_at`, `email`, `profile_id`) VALUES
(43, 'Jean Dupont', '+33 6 11 22 33 44', '25 rue des Fleurs', 'Paris', 'Île-de-France', 'France', NULL, 'Entreprise de services informatiques', 'CEO', NOW(), 'jean.dupont@techsolutions.fr', 19),
(44, 'Marie Lambert', '+33 6 22 33 44 55', '18 avenue des Champs', 'Lyon', 'Auvergne-Rhône-Alpes', 'France', NULL, 'Cabinet de conseil en marketing', 'Directrice Marketing', NOW(), 'marie.lambert@marketingconseil.fr', 20),
(45, 'Pierre Martin', '+33 6 33 44 55 66', '42 boulevard Gambetta', 'Marseille', 'Provence-Alpes-Côte d\'Azur', 'France', NULL, 'Agence immobilière', 'Gérant', NOW(), 'pierre.martin@immomartin.fr', 21),
(46, 'Isabelle Renaud', '+33 6 44 55 66 77', '7 rue Victor Hugo', 'Bordeaux', 'Nouvelle-Aquitaine', 'France', NULL, 'Restaurant gastronomique', 'Propriétaire', NOW(), 'isabelle.renaud@restaurantlestoiles.fr', 22),
(47, 'Nicolas Garcia', '+33 6 55 66 77 88', '15 place du Commerce', 'Lille', 'Hauts-de-France', 'France', NULL, 'Startup e-commerce', 'CTO', NOW(), 'nicolas.garcia@shopinnov.fr', 23),
(48, 'Sarah Bernard', '+33 6 66 77 88 99', '89 rue de la Paix', 'Strasbourg', 'Grand Est', 'France', NULL, 'Agence de voyage', 'Directrice', NOW(), 'sarah.bernard@voyagesavenir.fr', 24),
(49, 'Antoine Robert', '+33 6 77 88 99 00', '34 cours de la République', 'Nantes', 'Pays de la Loire', 'France', NULL, 'Société de transport', 'Directeur Logistique', NOW(), 'antoine.robert@transportexpress.fr', 25),
(50, 'Laurence Moreau', '+33 6 88 99 00 11', '56 rue de la Liberté', 'Toulouse', 'Occitanie', 'France', NULL, 'Cabinet d\'avocats', 'Avocate Associée', NOW(), 'laurence.moreau@avocats-conseils.fr', 26);

-- 6. TEMPLATES EMAIL
-- =====================================================
INSERT INTO `email_template` (`template_id`, `name`, `content`, `user_id`, `json_design`, `created_at`) VALUES
(35, 'Lead Status Update', '<h2>Lead Status Update</h2><p>Dear {{customer_name}},</p><p>Your lead {{lead_name}} has been updated to status: {{status}}.</p><p>Best regards,<br>CRM Team</p>', 52, '{"header":"blue","footer":"standard"}', NOW()),
(36, 'Meeting Scheduled', '<h2>Meeting Confirmation</h2><p>Hello {{customer_name}},</p><p>A meeting has been scheduled for {{meeting_date}} regarding {{lead_name}}.</p><p>Meeting ID: {{meeting_id}}</p>', 52, '{"header":"green","footer":"standard"}', NOW()),
(37, 'Contract Created', '<h2>Contract Notification</h2><p>Dear {{customer_name}},</p><p>A new contract has been created: {{contract_subject}}.</p><p>Amount: {{amount}} €<br>Start Date: {{start_date}}<br>End Date: {{end_date}}</p>', 52, '{"header":"gold","footer":"standard"}', NOW()),
(38, 'Ticket Created', '<h2>Support Ticket Created</h2><p>Dear {{customer_name}},</p><p>Your ticket #{{ticket_id}} has been created with priority: {{priority}}.</p><p>Subject: {{subject}}<br>Description: {{description}}</p>', 52, '{"header":"orange","footer":"support"}', NOW());

-- 7. LEADS
-- =====================================================
INSERT INTO `trigger_lead` (`lead_id`, `customer_id`, `user_id`, `name`, `phone`, `employee_id`, `status`, `meeting_id`, `google_drive`, `google_drive_folder_id`, `created_at`) VALUES
(56, 43, 53, 'Installation Solution Cloud', '0611223344', 54, 'Qualified', 'meet_' . MD5(RAND()), 1, 'drive_folder_001', NOW()),
(57, 44, 53, 'Campagne Marketing Digital', '0622334455', 57, 'Contacted', 'meet_' . MD5(RAND()), 1, 'drive_folder_002', NOW()),
(58, 45, 54, 'Refonte Site Web', '0633445566', 58, 'New', 'meet_' . MD5(RAND()), 0, NULL, NOW()),
(59, 46, 55, 'Rénovation Restaurant', '0644556677', 56, 'Qualified', 'meet_' . MD5(RAND()), 1, 'drive_folder_003', NOW()),
(60, 47, 57, 'Plateforme E-commerce', '0655667788', 60, 'Contacted', 'meet_' . MD5(RAND()), 1, 'drive_folder_004', NOW()),
(61, 48, 55, 'Système de Réservation', '0666778899', 59, 'New', 'meet_' . MD5(RAND()), 0, NULL, NOW()),
(62, 49, 58, 'Logiciel de Gestion de Flotte', '0677889900', 60, 'Qualified', 'meet_' . MD5(RAND()), 1, 'drive_folder_005', NOW()),
(63, 50, 59, 'CRM Juridique', '0688990011', 56, 'Contacted', 'meet_' . MD5(RAND()), 1, 'drive_folder_006', NOW());

-- 8. ACTIONS SUR LES LEADS
-- =====================================================
INSERT INTO `lead_action` (`id`, `lead_id`, `action`, `date_time`) VALUES
(13, 56, 'Lead created', NOW()),
(14, 56, 'Initial contact made', DATE_ADD(NOW(), INTERVAL 1 DAY)),
(15, 56, 'Meeting scheduled', DATE_ADD(NOW(), INTERVAL 2 DAY)),
(16, 57, 'Lead created', NOW()),
(17, 57, 'Email sent', DATE_ADD(NOW(), INTERVAL 1 DAY)),
(18, 58, 'Lead created', NOW()),
(19, 59, 'Lead created', NOW()),
(20, 59, 'Phone call', DATE_ADD(NOW(), INTERVAL 1 DAY)),
(21, 60, 'Lead created', NOW()),
(22, 61, 'Lead created', NOW()),
(23, 62, 'Lead created', NOW()),
(24, 62, 'Proposal sent', DATE_ADD(NOW(), INTERVAL 3 DAY)),
(25, 63, 'Lead created', NOW());

-- 9. CONTRATS
-- =====================================================
INSERT INTO `trigger_contract` (`contract_id`, `subject`, `status`, `description`, `start_date`, `end_date`, `amount`, `google_drive`, `google_drive_folder_id`, `lead_id`, `user_id`, `customer_id`, `created_at`) VALUES
(19, 'Installation Solution Cloud - Contrat', 'Active', 'Installation complète de l\'infrastructure cloud avec support 24/7', '2026-04-15', '2026-10-15', 25000, 1, 'contract_folder_001', 56, 53, 43, NOW()),
(20, 'Campagne Marketing Digital - Contrat', 'Active', 'Campagne de marketing digital sur 6 mois', '2026-04-01', '2026-09-30', 15000, 1, 'contract_folder_002', 57, 53, 44, NOW()),
(21, 'Rénovation Restaurant - Contrat', 'Pending', 'Rénovation complète et mise aux normes', '2026-05-01', '2026-08-31', 45000, 1, 'contract_folder_003', 59, 55, 46, NOW()),
(22, 'Plateforme E-commerce - Contrat', 'Draft', 'Développement plateforme e-commerce avec paiement intégré', '2026-05-15', '2026-11-15', 35000, 1, 'contract_folder_004', 60, 57, 47, NOW()),
(23, 'Logiciel de Gestion de Flotte - Contrat', 'Active', 'Solution de gestion de flotte avec GPS', '2026-04-10', '2026-10-10', 28000, 1, 'contract_folder_005', 62, 58, 49, NOW()),
(24, 'CRM Juridique - Contrat', 'Draft', 'Développement CRM spécialisé pour cabinets d\'avocats', '2026-05-20', '2026-11-20', 42000, 1, 'contract_folder_006', 63, 59, 50, NOW());

-- 10. TICKETS SUPPORT
-- =====================================================
INSERT INTO `trigger_ticket` (`ticket_id`, `subject`, `description`, `status`, `priority`, `customer_id`, `manager_id`, `employee_id`, `created_at`) VALUES
(47, 'Problème d\'accès à la plateforme', 'Impossible de se connecter à la plateforme depuis ce matin', 'Open', 'High', 43, 55, 56, NOW()),
(48, 'Bug sur l\'interface utilisateur', 'Le bouton de validation ne fonctionne pas correctement', 'In Progress', 'Medium', 44, 55, 59, NOW()),
(49, 'Demande de formation', 'Besoin d\'une formation pour les nouvelles fonctionnalités', 'Open', 'Low', 45, 55, 56, NOW()),
(50, 'Problème de performance', 'Le système est très lent en période de pointe', 'Resolved', 'High', 46, 55, 59, DATE_ADD(NOW(), INTERVAL -3 DAY)),
(51, 'Erreur lors de l\'export', 'L\'export des données échoue systématiquement', 'In Progress', 'Medium', 47, 55, 56, NOW()),
(52, 'Demande de fonctionnalité', 'Ajouter une fonctionnalité de reporting avancé', 'Open', 'Medium', 48, 55, 59, NOW()),
(53, 'Problème de facturation', 'Facture du mois dernier non reçue', 'Open', 'High', 49, 55, 56, NOW()),
(54, 'Mise à jour de compte', 'Besoin de modifier les informations du compte', 'Closed', 'Low', 50, 55, 59, DATE_ADD(NOW(), INTERVAL -5 DAY));

-- 11. SETTINGS POUR LES NOTIFICATIONS
-- =====================================================
-- Lead Settings
INSERT INTO `lead_settings` (`id`, `status`, `meeting`, `phone`, `name`, `user_id`, `status_email_template`, `phone_email_template`, `meeting_email_template`, `name_email_template`, `customer_id`) VALUES
(3, 1, 1, 1, 1, 53, 35, 36, 36, 35, 19);

-- Contract Settings
INSERT INTO `contract_settings` (`id`, `amount`, `subject`, `description`, `end_date`, `start_date`, `status`, `user_id`, `status_email_template`, `amount_email_template`, `subject_email_template`, `description_email_template`, `start_email_template`, `end_email_template`, `customer_id`) VALUES
(4, 1, 1, 1, 1, 1, 1, 53, 37, 37, 37, 37, 37, 37, 19);

-- Ticket Settings
INSERT INTO `ticket_settings` (`id`, `priority`, `subject`, `description`, `status`, `user_id`, `status_email_template`, `subject_email_template`, `priority_email_template`, `description_email_template`, `customer_id`) VALUES
(6, 1, 1, 1, 1, 55, 38, 38, 38, 38, 19);

-- 12. FICHIERS ASSOCIÉS
-- =====================================================
-- Insertion de fichiers exemple pour les leads et contrats
INSERT INTO `file` (`file_id`, `file_name`, `file_type`, `lead_id`, `contract_id`) VALUES
(140, 'devis_cloud.pdf', 'application/pdf', 56, NULL),
(141, 'contrat_cloud.pdf', 'application/pdf', NULL, 19),
(142, 'specifications_marketing.pdf', 'application/pdf', 57, NULL),
(143, 'plan_renovation.pdf', 'application/pdf', 59, NULL);

-- 13. FICHIERS GOOGLE DRIVE
-- =====================================================
INSERT INTO `google_drive_file` (`id`, `drive_file_id`, `drive_folder_id`, `lead_id`, `contract_id`) VALUES
(52, 'drive_file_001', 'drive_folder_001', 56, NULL),
(53, 'drive_file_002', 'drive_folder_002', 57, NULL),
(54, 'drive_file_003', 'drive_folder_003', 59, NULL),
(55, 'drive_contract_001', 'contract_folder_001', NULL, 19),
(56, 'drive_contract_002', 'contract_folder_002', NULL, 20);

-- 14. MISE À JOUR DES SÉQUENCES AUTO_INCREMENT
-- =====================================================
-- Les AUTO_INCREMENT sont déjà définis, les nouvelles données utilisent les IDs suivants:
-- users: à partir de 53
-- user_profile: à partir de 34
-- customer_login_info: à partir de 19
-- customer: à partir de 43
-- email_template: à partir de 35
-- trigger_lead: à partir de 56
-- lead_action: à partir de 13
-- trigger_contract: à partir de 19
-- trigger_ticket: à partir de 47
-- lead_settings: 3
-- contract_settings: 4
-- ticket_settings: 6
-- file: à partir de 140
-- google_drive_file: à partir de 52

-- =====================================================
-- RÉSUMÉ DES DONNÉES INSÉRÉES:
-- - 8 nouveaux employés (1 manager + 7 employés)
-- - 8 nouveaux clients avec leurs comptes
-- - 8 leads
-- - 6 contrats
-- - 8 tickets support
-- - Templates email
-- - Configurations de notifications
-- - Fichiers associés
-- =====================================================