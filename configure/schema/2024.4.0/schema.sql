USE
`datacap`;

ALTER TABLE `datacap_dashboard`
    ADD COLUMN `version` VARCHAR(100) DEFAULT NULL;

UPDATE `datacap_dashboard`
SET `version` = '1.0'
WHERE `version` IS NULL;

UPDATE `datacap_dataset`
SET `executor` = 'LocalExecutor'
WHERE `executor` = 'Default';
