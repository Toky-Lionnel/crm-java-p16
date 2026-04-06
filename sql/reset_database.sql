-- =====================================================
-- RESET COMPLET DE LA BASE CRM
-- Conserve uniquement les données de la table `roles`
-- =====================================================

-- USE `crm`;

SET FOREIGN_KEY_CHECKS = 0;

-- Tables dépendantes des utilisateurs et clients
TRUNCATE TABLE `google_drive_file`;
TRUNCATE TABLE `file`;
TRUNCATE TABLE `ticket_settings`;
TRUNCATE TABLE `contract_settings`;
TRUNCATE TABLE `lead_settings`;
TRUNCATE TABLE `lead_action`;
TRUNCATE TABLE `trigger_ticket`;
TRUNCATE TABLE `trigger_contract`;
TRUNCATE TABLE `trigger_lead`;
TRUNCATE TABLE `customer`;
TRUNCATE TABLE `customer_login_info`;
TRUNCATE TABLE `email_template`;
TRUNCATE TABLE `user_roles`;
TRUNCATE TABLE `user_profile`;
TRUNCATE TABLE `oauth_users`;
TRUNCATE TABLE `employee`;
TRUNCATE TABLE `users`;

-- La table `roles` est volontairement conservée telle quelle.

SET FOREIGN_KEY_CHECKS = 1;
