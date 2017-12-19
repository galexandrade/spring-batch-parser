DROP TABLE IF EXISTS access;

CREATE TABLE `access` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `ip_address` VARCHAR(15) NOT NULL,
  `dt_access` DATETIME NOT NULL,
  `request` VARCHAR(200) NOT NULL,
  `status` INT NOT NULL,
  `user_agent` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `IDX_DATE` (`dt_access` ASC));
