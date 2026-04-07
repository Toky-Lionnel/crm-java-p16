
CREATE TABLE IF NOT EXISTS `parametre` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nom` VARCHAR(255) NOT NULL,
  `valeur` VARCHAR(255) NOT NULL,
  `type` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `parametre` (`nom`, `valeur`, `type`) VALUES
('taux_alerte', '15.20', 'decimal');