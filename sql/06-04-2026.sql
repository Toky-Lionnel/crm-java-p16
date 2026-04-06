
CREATE TABLE IF NOT EXISTS `budget`(
   `id` int unsigned NOT NULL AUTO_INCREMENT,
   `customer_id` INT UNSIGNED,
   `amount` DECIMAL(25,2),
   `created_at` DATETIME,
   PRIMARY KEY (`id`),
   KEY `customer_id` (`customer_id`),
   constraint `fk_budget_customer` foreign key (`customer_id`) references `customer` (`customer_id`) on delete cascade
) ENGINE=InnoDB;

ALTER TABLE `trigger_ticket` ADD COLUMN `amount` DECIMAL(25,2);
ALTER TABLE `trigger_lead` ADD COLUMN `amount` DECIMAL (25,2);

