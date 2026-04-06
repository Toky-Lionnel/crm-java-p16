-- =====================================================
-- DONNEES DE TEST POUR LA BASE CRM
-- A executer apres sql/reset_database.sql
-- Toutes les valeurs inserees sont renseignees
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;

-- 1. USERS
INSERT INTO `users` (`id`, `email`, `password`, `hire_date`, `created_at`, `updated_at`, `username`, `status`, `token`, `is_password_set`) VALUES
(1, 'admin@crm.test', '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', '2026-01-10 09:00:00', '2026-04-06 10:00:00', '2026-04-06 10:00:00', 'admin', 'active', 'token_user_01', 1),
(2, 'manager1@crm.test', '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', '2026-01-12 09:00:00', '2026-04-06 10:00:00', '2026-04-06 10:00:00', 'manager1', 'active', 'token_user_02', 1),
(3, 'employee1@crm.test', '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', '2026-01-14 09:00:00', '2026-04-06 10:00:00', '2026-04-06 10:00:00', 'employee1', 'active', 'token_user_03', 1),
(4, 'employee2@crm.test', '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', '2026-01-16 09:00:00', '2026-04-06 10:00:00', '2026-04-06 10:00:00', 'employee2', 'active', 'token_user_04', 1),
(5, 'employee3@crm.test', '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', '2026-01-18 09:00:00', '2026-04-06 10:00:00', '2026-04-06 10:00:00', 'employee3', 'active', 'token_user_05', 1);

-- 2. USER PROFILES
INSERT INTO `user_profile` (`id`, `first_name`, `last_name`, `phone`, `department`, `salary`, `status`, `oauth_user_image_link`, `user_image`, `bio`, `youtube`, `twitter`, `facebook`, `user_id`, `country`, `position`, `address`) VALUES
(1, 'Alex', 'Martin', '+33 6 10 10 10 10', 'Sales', 52000.00, 'active', 'https://img.crm.test/u1.png', 0x746573745f696d6167655f31, 'Sales lead for enterprise accounts', 'https://youtube.com/alexmartin', 'https://x.com/alexmartin', 'https://facebook.com/alexmartin', 1, 'France', 'Sales Director', '10 rue du Test, 75001 Paris'),
(2, 'Sarah', 'Leroy', '+33 6 20 20 20 20', 'Sales', 46000.00, 'active', 'https://img.crm.test/u2.png', 0x746573745f696d6167655f32, 'Manages the sales pipeline', 'https://youtube.com/sarahleroy', 'https://x.com/sarahleroy', 'https://facebook.com/sarahleroy', 2, 'France', 'Sales Manager', '20 avenue du Test, 69001 Lyon'),
(3, 'Nina', 'Petit', '+33 6 30 30 30 30', 'Support', 41000.00, 'active', 'https://img.crm.test/u3.png', 0x746573745f696d6167655f33, 'Handles support escalation', 'https://youtube.com/ninapetit', 'https://x.com/ninapetit', 'https://facebook.com/ninapetit', 3, 'France', 'Support Lead', '30 boulevard du Test, 13001 Marseille'),
(4, 'Louis', 'Bernard', '+33 6 40 40 40 40', 'Support', 38000.00, 'active', 'https://img.crm.test/u4.png', 0x746573745f696d6167655f34, 'Support agent for customer tickets', 'https://youtube.com/louisbernard', 'https://x.com/louisbernard', 'https://facebook.com/louisbernard', 4, 'France', 'Support Agent', '40 place du Test, 33000 Bordeaux'),
(5, 'Maya', 'Durand', '+33 6 50 50 50 50', 'Operations', 40000.00, 'active', 'https://img.crm.test/u5.png', 0x746573745f696d6167655f35, 'Coordinates internal operations', 'https://youtube.com/mayadurand', 'https://x.com/mayadurand', 'https://facebook.com/mayadurand', 5, 'France', 'Operations Specialist', '50 rue du Test, 44000 Nantes');

-- 3. OAUTH USERS
INSERT INTO `oauth_users` (`id`, `user_id`, `access_token`, `access_token_issued_at`, `access_token_expiration`, `refresh_token`, `refresh_token_issued_at`, `refresh_token_expiration`, `granted_scopes`, `email`) VALUES
(1, 1, 'access_token_01', '2026-04-06 08:00:00', '2026-04-06 18:00:00', 'refresh_token_01', '2026-04-06 08:00:00', '2026-05-06 08:00:00', 'read,write', 'admin@crm.test'),
(2, 2, 'access_token_02', '2026-04-06 08:10:00', '2026-04-06 18:10:00', 'refresh_token_02', '2026-04-06 08:10:00', '2026-05-06 08:10:00', 'read,write', 'manager1@crm.test'),
(3, 3, 'access_token_03', '2026-04-06 08:20:00', '2026-04-06 18:20:00', 'refresh_token_03', '2026-04-06 08:20:00', '2026-05-06 08:20:00', 'read', 'employee1@crm.test'),
(4, 4, 'access_token_04', '2026-04-06 08:30:00', '2026-04-06 18:30:00', 'refresh_token_04', '2026-04-06 08:30:00', '2026-05-06 08:30:00', 'read', 'employee2@crm.test'),
(5, 5, 'access_token_05', '2026-04-06 08:40:00', '2026-04-06 18:40:00', 'refresh_token_05', '2026-04-06 08:40:00', '2026-05-06 08:40:00', 'read', 'employee3@crm.test');

-- 4. USER ROLES
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 2),
(5, 3);

-- 5. EMPLOYEES
INSERT INTO `employee` (`id`, `username`, `first_name`, `last_name`, `email`, `password`, `provider`) VALUES
(1, 'admin', 'Alex', 'Martin', 'admin@crm.test', 'passhash_admin', 'local'),
(2, 'manager1', 'Sarah', 'Leroy', 'manager1@crm.test', 'passhash_manager1', 'local'),
(3, 'employee1', 'Nina', 'Petit', 'employee1@crm.test', 'passhash_employee1', 'local'),
(4, 'employee2', 'Louis', 'Bernard', 'employee2@crm.test', 'passhash_employee2', 'local'),
(5, 'employee3', 'Maya', 'Durand', 'employee3@crm.test', 'passhash_employee3', 'local');

-- 6. CUSTOMER LOGIN INFO
INSERT INTO `customer_login_info` (`id`, `password`, `username`, `token`, `password_set`) VALUES
(1, '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', 'client01', 'token_cust_01', 1),
(2, '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', 'client02', 'token_cust_02', 1),
(3, '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', 'client03', 'token_cust_03', 1),
(4, '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', 'client04', 'token_cust_04', 1),
(5, '$2a$10$Fr9kCsPf7WrdCd85kk7wmINKsknvgyPdNWawo4nEspSYDNmx4Lu', 'client05', 'token_cust_05', 1);

-- 7. CUSTOMERS
INSERT INTO `customer` (`customer_id`, `name`, `phone`, `address`, `city`, `state`, `country`, `user_id`, `description`, `position`, `twitter`, `facebook`, `youtube`, `created_at`, `email`, `profile_id`) VALUES
(1, 'Client One', '+33 6 11 11 11 11', '1 rue Client', 'Paris', 'Ile-de-France', 'France', 1, 'Technology services company', 'CEO', 'https://x.com/clientone', 'https://facebook.com/clientone', 'https://youtube.com/clientone', '2026-04-06 10:00:00', 'client01@crm.test', 1),
(2, 'Client Two', '+33 6 22 22 22 22', '2 avenue Client', 'Lyon', 'Auvergne-Rhone-Alpes', 'France', 2, 'Marketing consultancy', 'Marketing Director', 'https://x.com/clienttwo', 'https://facebook.com/clienttwo', 'https://youtube.com/clienttwo', '2026-04-06 10:00:00', 'client02@crm.test', 2),
(3, 'Client Three', '+33 6 33 33 33 33', '3 boulevard Client', 'Marseille', 'Provence-Alpes-Cote d Azur', 'France', 3, 'Real estate agency', 'Owner', 'https://x.com/clientthree', 'https://facebook.com/clientthree', 'https://youtube.com/clientthree', '2026-04-06 10:00:00', 'client03@crm.test', 3),
(4, 'Client Four', '+33 6 44 44 44 44', '4 place Client', 'Bordeaux', 'Nouvelle-Aquitaine', 'France', 4, 'Hospitality business', 'Manager', 'https://x.com/clientfour', 'https://facebook.com/clientfour', 'https://youtube.com/clientfour', '2026-04-06 10:00:00', 'client04@crm.test', 4),
(5, 'Client Five', '+33 6 55 55 55 55', '5 rue Client', 'Nantes', 'Pays de la Loire', 'France', 5, 'Logistics company', 'Operations Lead', 'https://x.com/clientfive', 'https://facebook.com/clientfive', 'https://youtube.com/clientfive', '2026-04-06 10:00:00', 'client05@crm.test', 5);

-- 8. EMAIL TEMPLATES
INSERT INTO `email_template` (`template_id`, `name`, `content`, `user_id`, `json_design`, `created_at`) VALUES
(1, 'Lead Status Update', '<h2>Lead Status Update</h2><p>Hello {{customer_name}},</p><p>Your lead {{lead_name}} is now {{status}}.</p>', 1, '{"header":"blue","footer":"standard"}', '2026-04-06 10:00:00'),
(2, 'Meeting Scheduled', '<h2>Meeting Scheduled</h2><p>Hello {{customer_name}},</p><p>A meeting is scheduled for {{meeting_date}}.</p>', 1, '{"header":"green","footer":"standard"}', '2026-04-06 10:00:00'),
(3, 'Contract Created', '<h2>Contract Created</h2><p>Hello {{customer_name}},</p><p>Contract {{contract_subject}} is ready.</p>', 2, '{"header":"gold","footer":"standard"}', '2026-04-06 10:00:00'),
(4, 'Ticket Created', '<h2>Ticket Created</h2><p>Hello {{customer_name}},</p><p>Your ticket {{ticket_id}} has been created.</p>', 3, '{"header":"orange","footer":"support"}', '2026-04-06 10:00:00'),
(5, 'Welcome Message', '<h2>Welcome</h2><p>Hello {{customer_name}}, welcome to our CRM test environment.</p>', 4, '{"header":"teal","footer":"standard"}', '2026-04-06 10:00:00');

-- 9. LEADS
INSERT INTO `trigger_lead` (`lead_id`, `customer_id`, `user_id`, `name`, `phone`, `employee_id`, `status`, `meeting_id`, `google_drive`, `google_drive_folder_id`, `created_at`) VALUES
(1, 1, 1, 'Cloud Migration', '0611111111', 3, 'Qualified', 'meet_test_001', 1, 'drive_folder_test_001', '2026-04-06 10:00:00'),
(2, 2, 1, 'Digital Campaign', '0622222222', 4, 'Contacted', 'meet_test_002', 1, 'drive_folder_test_002', '2026-04-06 10:00:00'),
(3, 3, 2, 'Website Redesign', '0633333333', 3, 'New', 'meet_test_003', 0, 'drive_folder_test_003', '2026-04-06 10:00:00'),
(4, 4, 2, 'Restaurant Upgrade', '0644444444', 4, 'Qualified', 'meet_test_004', 1, 'drive_folder_test_004', '2026-04-06 10:00:00'),
(5, 5, 1, 'Fleet Management Software', '0655555555', 5, 'Contacted', 'meet_test_005', 1, 'drive_folder_test_005', '2026-04-06 10:00:00');

-- 10. LEAD ACTIONS
INSERT INTO `lead_action` (`id`, `lead_id`, `action`, `date_time`) VALUES
(1, 1, 'Lead created', '2026-04-06 10:05:00'),
(2, 1, 'Call completed', '2026-04-06 11:00:00'),
(3, 2, 'Lead created', '2026-04-06 10:10:00'),
(4, 3, 'Email sent', '2026-04-06 10:15:00'),
(5, 4, 'Meeting scheduled', '2026-04-06 10:20:00');

-- 11. CONTRACTS
INSERT INTO `trigger_contract` (`contract_id`, `subject`, `status`, `description`, `start_date`, `end_date`, `amount`, `google_drive`, `google_drive_folder_id`, `lead_id`, `user_id`, `customer_id`, `created_at`) VALUES
(1, 'Cloud Migration Contract', 'Active', 'Cloud migration service for enterprise client', '2026-04-15', '2026-10-15', 25000, 1, 'contract_folder_test_001', 1, 1, 1, '2026-04-06 10:00:00'),
(2, 'Digital Campaign Contract', 'Active', 'Marketing campaign for six months', '2026-04-20', '2026-10-20', 18000, 1, 'contract_folder_test_002', 2, 1, 2, '2026-04-06 10:00:00'),
(3, 'Website Redesign Contract', 'Pending', 'Redesign and modernization of the company website', '2026-05-01', '2026-09-30', 30000, 1, 'contract_folder_test_003', 3, 2, 3, '2026-04-06 10:00:00'),
(4, 'Restaurant Upgrade Contract', 'Draft', 'Upgrade project for restaurant operations', '2026-05-10', '2026-11-10', 22000, 1, 'contract_folder_test_004', 4, 2, 4, '2026-04-06 10:00:00'),
(5, 'Fleet Management Contract', 'Active', 'Fleet software deployment with support', '2026-04-25', '2026-10-25', 28000, 1, 'contract_folder_test_005', 5, 1, 5, '2026-04-06 10:00:00');

-- 12. TICKETS
INSERT INTO `trigger_ticket` (`ticket_id`, `subject`, `description`, `status`, `priority`, `customer_id`, `manager_id`, `employee_id`, `created_at`) VALUES
(1, 'Login issue', 'Customer cannot access the portal', 'Open', 'High', 1, 1, 3, '2026-04-06 10:30:00'),
(2, 'UI bug', 'Validation button is not responsive', 'In Progress', 'Medium', 2, 1, 4, '2026-04-06 10:35:00'),
(3, 'Training request', 'Customer wants training for new features', 'Open', 'Low', 3, 2, 3, '2026-04-06 10:40:00'),
(4, 'Performance issue', 'System is slow during peak hours', 'Resolved', 'High', 4, 2, 4, '2026-04-06 10:45:00'),
(5, 'Export error', 'Data export fails on every attempt', 'Open', 'Medium', 5, 1, 5, '2026-04-06 10:50:00');

-- 13. LEAD SETTINGS
INSERT INTO `lead_settings` (`id`, `status`, `meeting`, `phone`, `name`, `user_id`, `status_email_template`, `phone_email_template`, `meeting_email_template`, `name_email_template`, `customer_id`) VALUES
(1, 1, 1, 1, 1, 1, 1, 2, 2, 5, 1),
(2, 1, 1, 1, 1, 2, 1, 2, 2, 5, 2),
(3, 1, 1, 1, 1, 3, 1, 2, 2, 5, 3),
(4, 1, 1, 1, 1, 4, 1, 2, 2, 5, 4),
(5, 1, 1, 1, 1, 5, 1, 2, 2, 5, 5);

-- 14. CONTRACT SETTINGS
INSERT INTO `contract_settings` (`id`, `amount`, `subject`, `description`, `end_date`, `start_date`, `status`, `user_id`, `status_email_template`, `amount_email_template`, `subject_email_template`, `description_email_template`, `start_email_template`, `end_email_template`, `customer_id`) VALUES
(1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 1),
(2, 1, 1, 1, 1, 1, 1, 2, 3, 3, 3, 3, 3, 3, 2),
(3, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3),
(4, 1, 1, 1, 1, 1, 1, 4, 3, 3, 3, 3, 3, 3, 4),
(5, 1, 1, 1, 1, 1, 1, 5, 3, 3, 3, 3, 3, 3, 5);

-- 15. TICKET SETTINGS
INSERT INTO `ticket_settings` (`id`, `priority`, `subject`, `description`, `status`, `user_id`, `status_email_template`, `subject_email_template`, `priority_email_template`, `description_email_template`, `customer_id`) VALUES
(1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 1),
(2, 1, 1, 1, 1, 2, 4, 4, 4, 4, 2),
(3, 1, 1, 1, 1, 3, 4, 4, 4, 4, 3),
(4, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4),
(5, 1, 1, 1, 1, 5, 4, 4, 4, 4, 5);

-- 16. FILES
INSERT INTO `file` (`file_id`, `file_name`, `file_data`, `file_type`, `lead_id`, `contract_id`) VALUES
(1, 'cloud_scope.pdf', 0x5044465f434C4F55445F31, 'application/pdf', 1, 1),
(2, 'campaign_plan.pdf', 0x5044465f434C4F55445F32, 'application/pdf', 2, 2),
(3, 'website_mockup.pdf', 0x5044465f434C4F55445F33, 'application/pdf', 3, 3),
(4, 'restaurant_upgrade.pdf', 0x5044465f434C4F55445F34, 'application/pdf', 4, 4),
(5, 'fleet_solution.pdf', 0x5044465f434C4F55445F35, 'application/pdf', 5, 5);

-- 17. GOOGLE DRIVE FILES
INSERT INTO `google_drive_file` (`id`, `drive_file_id`, `drive_folder_id`, `lead_id`, `contract_id`) VALUES
(1, 'drive_file_test_001', 'drive_folder_test_001', 1, 1),
(2, 'drive_file_test_002', 'drive_folder_test_002', 2, 2),
(3, 'drive_file_test_003', 'drive_folder_test_003', 3, 3),
(4, 'drive_file_test_004', 'drive_folder_test_004', 4, 4),
(5, 'drive_file_test_005', 'drive_folder_test_005', 5, 5);

SET FOREIGN_KEY_CHECKS = 1;
