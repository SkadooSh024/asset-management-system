-- ============================================================
-- DATABASE: asset_management
-- PURPOSE : Hệ thống quản lý tài sản, cấp phát và bảo trì/sửa chữa thiết bị
-- TARGET  : MySQL 8.x / MariaDB 10.4+
-- CHARSET : utf8mb4
-- ============================================================

SET NAMES utf8mb4;

-- ============================================================
-- 1. MASTER DATA
-- ============================================================

CREATE TABLE `roles` (
  `role_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_code` VARCHAR(50) NOT NULL,
  `role_name` VARCHAR(100) NOT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `uk_roles_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `departments` (
  `department_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `parent_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `department_code` VARCHAR(50) NOT NULL,
  `department_name` VARCHAR(150) NOT NULL,
  `manager_name` VARCHAR(100) DEFAULT NULL,
  `phone` VARCHAR(20) DEFAULT NULL,
  `email` VARCHAR(120) DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `is_active` TINYINT(1) NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`department_id`),
  UNIQUE KEY `uk_departments_department_code` (`department_code`),
  CONSTRAINT `fk_departments_parent_department`
    FOREIGN KEY (`parent_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `users` (
  `user_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_id` BIGINT UNSIGNED NOT NULL,
  `department_id` BIGINT UNSIGNED DEFAULT NULL,
  `username` VARCHAR(50) NOT NULL,
  `full_name` VARCHAR(150) NOT NULL,
  `email` VARCHAR(120) NOT NULL,
  `phone` VARCHAR(20) DEFAULT NULL,
  `job_title` VARCHAR(100) DEFAULT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `status` ENUM('ACTIVE','INACTIVE','LOCKED') NOT NULL DEFAULT 'ACTIVE',
  `last_login_at` DATETIME DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_users_username` (`username`),
  UNIQUE KEY `uk_users_email` (`email`),
  KEY `idx_users_role_id` (`role_id`),
  KEY `idx_users_department_id` (`department_id`),
  CONSTRAINT `fk_users_role`
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_users_department`
    FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `asset_categories` (
  `category_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `parent_category_id` BIGINT UNSIGNED DEFAULT NULL,
  `category_code` VARCHAR(50) NOT NULL,
  `category_name` VARCHAR(150) NOT NULL,
  `default_warranty_months` INT DEFAULT NULL,
  `default_maintenance_cycle_days` INT DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `is_active` TINYINT(1) NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `uk_asset_categories_category_code` (`category_code`),
  CONSTRAINT `fk_asset_categories_parent_category`
    FOREIGN KEY (`parent_category_id`) REFERENCES `asset_categories` (`category_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `asset_statuses` (
  `status_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `status_code` VARCHAR(50) NOT NULL,
  `status_name` VARCHAR(100) NOT NULL,
  `status_group` ENUM('STORAGE','USAGE','MAINTENANCE','END_OF_LIFE','EXCEPTION') NOT NULL,
  `is_allocatable` TINYINT(1) NOT NULL DEFAULT 0,
  `sort_order` INT NOT NULL DEFAULT 0,
  `description` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`status_id`),
  UNIQUE KEY `uk_asset_statuses_status_code` (`status_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 2. CORE ASSET TABLE
-- ============================================================

CREATE TABLE `assets` (
  `asset_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `asset_code` VARCHAR(50) NOT NULL,
  `asset_name` VARCHAR(200) NOT NULL,
  `category_id` BIGINT UNSIGNED NOT NULL,
  `current_status_id` BIGINT UNSIGNED NOT NULL,
  `owning_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `current_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `assigned_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `brand` VARCHAR(100) DEFAULT NULL,
  `model` VARCHAR(100) DEFAULT NULL,
  `serial_number` VARCHAR(100) DEFAULT NULL,
  `asset_tag` VARCHAR(100) DEFAULT NULL,
  `purchase_date` DATE DEFAULT NULL,
  `warranty_expiry_date` DATE DEFAULT NULL,
  `purchase_cost` DECIMAL(18,2) DEFAULT 0.00,
  `specification_text` LONGTEXT DEFAULT NULL,
  `notes` TEXT DEFAULT NULL,
  `image_url` VARCHAR(255) DEFAULT NULL,
  `is_active` TINYINT(1) NOT NULL DEFAULT 1,
  `created_by` BIGINT UNSIGNED DEFAULT NULL,
  `updated_by` BIGINT UNSIGNED DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`asset_id`),
  UNIQUE KEY `uk_assets_asset_code` (`asset_code`),
  UNIQUE KEY `uk_assets_serial_number` (`serial_number`),
  UNIQUE KEY `uk_assets_asset_tag` (`asset_tag`),
  KEY `idx_assets_category_id` (`category_id`),
  KEY `idx_assets_current_status_id` (`current_status_id`),
  KEY `idx_assets_owning_department_id` (`owning_department_id`),
  KEY `idx_assets_current_department_id` (`current_department_id`),
  KEY `idx_assets_assigned_user_id` (`assigned_user_id`),
  CONSTRAINT `fk_assets_category`
    FOREIGN KEY (`category_id`) REFERENCES `asset_categories` (`category_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_assets_current_status`
    FOREIGN KEY (`current_status_id`) REFERENCES `asset_statuses` (`status_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_assets_owning_department`
    FOREIGN KEY (`owning_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_assets_current_department`
    FOREIGN KEY (`current_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_assets_assigned_user`
    FOREIGN KEY (`assigned_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_assets_created_by`
    FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_assets_updated_by`
    FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 3. OPERATIONAL TRANSACTION TABLES
-- ============================================================

CREATE TABLE `assignment_forms` (
  `assignment_form_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `form_code` VARCHAR(50) NOT NULL,
  `assignment_date` DATE NOT NULL,
  `source_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `target_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `target_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `issued_by_user_id` BIGINT UNSIGNED NOT NULL,
  `approved_by_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `status` ENUM('DRAFT','CONFIRMED','CANCELED','COMPLETED') NOT NULL DEFAULT 'DRAFT',
  `reason` VARCHAR(255) DEFAULT NULL,
  `note` TEXT DEFAULT NULL,
  `created_by` BIGINT UNSIGNED DEFAULT NULL,
  `updated_by` BIGINT UNSIGNED DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`assignment_form_id`),
  UNIQUE KEY `uk_assignment_forms_form_code` (`form_code`),
  KEY `idx_assignment_forms_target_department_id` (`target_department_id`),
  KEY `idx_assignment_forms_target_user_id` (`target_user_id`),
  CONSTRAINT `fk_assignment_forms_source_department`
    FOREIGN KEY (`source_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_assignment_forms_target_department`
    FOREIGN KEY (`target_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_assignment_forms_target_user`
    FOREIGN KEY (`target_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_assignment_forms_issued_by_user`
    FOREIGN KEY (`issued_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_assignment_forms_approved_by_user`
    FOREIGN KEY (`approved_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_assignment_forms_created_by`
    FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_assignment_forms_updated_by`
    FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `assignment_form_details` (
  `assignment_form_detail_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `assignment_form_id` BIGINT UNSIGNED NOT NULL,
  `asset_id` BIGINT UNSIGNED NOT NULL,
  `expected_return_date` DATE DEFAULT NULL,
  `note` TEXT DEFAULT NULL,
  PRIMARY KEY (`assignment_form_detail_id`),
  UNIQUE KEY `uk_assignment_form_asset` (`assignment_form_id`, `asset_id`),
  KEY `idx_assignment_form_details_asset_id` (`asset_id`),
  CONSTRAINT `fk_assignment_form_details_form`
    FOREIGN KEY (`assignment_form_id`) REFERENCES `assignment_forms` (`assignment_form_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_assignment_form_details_asset`
    FOREIGN KEY (`asset_id`) REFERENCES `assets` (`asset_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `return_forms` (
  `return_form_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `form_code` VARCHAR(50) NOT NULL,
  `return_date` DATE NOT NULL,
  `source_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `source_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `received_by_user_id` BIGINT UNSIGNED NOT NULL,
  `target_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `status` ENUM('DRAFT','CONFIRMED','CANCELED','COMPLETED') NOT NULL DEFAULT 'DRAFT',
  `reason` VARCHAR(255) DEFAULT NULL,
  `note` TEXT DEFAULT NULL,
  `created_by` BIGINT UNSIGNED DEFAULT NULL,
  `updated_by` BIGINT UNSIGNED DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`return_form_id`),
  UNIQUE KEY `uk_return_forms_form_code` (`form_code`),
  CONSTRAINT `fk_return_forms_source_department`
    FOREIGN KEY (`source_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_return_forms_source_user`
    FOREIGN KEY (`source_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_return_forms_received_by_user`
    FOREIGN KEY (`received_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_return_forms_target_department`
    FOREIGN KEY (`target_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_return_forms_created_by`
    FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_return_forms_updated_by`
    FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `return_form_details` (
  `return_form_detail_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `return_form_id` BIGINT UNSIGNED NOT NULL,
  `asset_id` BIGINT UNSIGNED NOT NULL,
  `status_after_return_id` BIGINT UNSIGNED DEFAULT NULL,
  `note` TEXT DEFAULT NULL,
  PRIMARY KEY (`return_form_detail_id`),
  UNIQUE KEY `uk_return_form_asset` (`return_form_id`, `asset_id`),
  KEY `idx_return_form_details_asset_id` (`asset_id`),
  CONSTRAINT `fk_return_form_details_form`
    FOREIGN KEY (`return_form_id`) REFERENCES `return_forms` (`return_form_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_return_form_details_asset`
    FOREIGN KEY (`asset_id`) REFERENCES `assets` (`asset_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_return_form_details_status_after_return`
    FOREIGN KEY (`status_after_return_id`) REFERENCES `asset_statuses` (`status_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `transfer_forms` (
  `transfer_form_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `form_code` VARCHAR(50) NOT NULL,
  `requested_date` DATE NOT NULL,
  `transfer_date` DATE DEFAULT NULL,
  `from_department_id` BIGINT UNSIGNED NOT NULL,
  `to_department_id` BIGINT UNSIGNED NOT NULL,
  `target_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `requested_by_user_id` BIGINT UNSIGNED NOT NULL,
  `approved_by_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `received_by_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `status` ENUM('DRAFT','APPROVED','IN_TRANSIT','COMPLETED','CANCELED') NOT NULL DEFAULT 'DRAFT',
  `reason` VARCHAR(255) DEFAULT NULL,
  `note` TEXT DEFAULT NULL,
  `created_by` BIGINT UNSIGNED DEFAULT NULL,
  `updated_by` BIGINT UNSIGNED DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`transfer_form_id`),
  UNIQUE KEY `uk_transfer_forms_form_code` (`form_code`),
  CONSTRAINT `fk_transfer_forms_from_department`
    FOREIGN KEY (`from_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_transfer_forms_to_department`
    FOREIGN KEY (`to_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_transfer_forms_target_user`
    FOREIGN KEY (`target_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_transfer_forms_requested_by_user`
    FOREIGN KEY (`requested_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_transfer_forms_approved_by_user`
    FOREIGN KEY (`approved_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_transfer_forms_received_by_user`
    FOREIGN KEY (`received_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_transfer_forms_created_by`
    FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_transfer_forms_updated_by`
    FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `transfer_form_details` (
  `transfer_form_detail_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `transfer_form_id` BIGINT UNSIGNED NOT NULL,
  `asset_id` BIGINT UNSIGNED NOT NULL,
  `note` TEXT DEFAULT NULL,
  PRIMARY KEY (`transfer_form_detail_id`),
  UNIQUE KEY `uk_transfer_form_asset` (`transfer_form_id`, `asset_id`),
  KEY `idx_transfer_form_details_asset_id` (`asset_id`),
  CONSTRAINT `fk_transfer_form_details_form`
    FOREIGN KEY (`transfer_form_id`) REFERENCES `transfer_forms` (`transfer_form_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_transfer_form_details_asset`
    FOREIGN KEY (`asset_id`) REFERENCES `assets` (`asset_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `incident_reports` (
  `incident_report_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `report_code` VARCHAR(50) NOT NULL,
  `asset_id` BIGINT UNSIGNED NOT NULL,
  `reported_by_user_id` BIGINT UNSIGNED NOT NULL,
  `assigned_to_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `report_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `severity` ENUM('LOW','MEDIUM','HIGH','CRITICAL') NOT NULL DEFAULT 'MEDIUM',
  `issue_title` VARCHAR(255) NOT NULL,
  `issue_description` TEXT NOT NULL,
  `status` ENUM('OPEN','IN_REVIEW','CONVERTED_TO_TICKET','RESOLVED','REJECTED','CANCELED') NOT NULL DEFAULT 'OPEN',
  `resolution_note` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`incident_report_id`),
  UNIQUE KEY `uk_incident_reports_report_code` (`report_code`),
  KEY `idx_incident_reports_asset_id` (`asset_id`),
  KEY `idx_incident_reports_reported_by_user_id` (`reported_by_user_id`),
  CONSTRAINT `fk_incident_reports_asset`
    FOREIGN KEY (`asset_id`) REFERENCES `assets` (`asset_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_incident_reports_reported_by_user`
    FOREIGN KEY (`reported_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_incident_reports_assigned_to_user`
    FOREIGN KEY (`assigned_to_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `maintenance_tickets` (
  `maintenance_ticket_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ticket_code` VARCHAR(50) NOT NULL,
  `incident_report_id` BIGINT UNSIGNED DEFAULT NULL,
  `asset_id` BIGINT UNSIGNED NOT NULL,
  `opened_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `assigned_to_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `status` ENUM('OPEN','ASSIGNED','IN_PROGRESS','WAITING_PARTS','OUTSOURCED','COMPLETED','CANCELED') NOT NULL DEFAULT 'OPEN',
  `priority` ENUM('LOW','MEDIUM','HIGH','URGENT') NOT NULL DEFAULT 'MEDIUM',
  `maintenance_type` ENUM('PREVENTIVE','CORRECTIVE','INSPECTION','OTHER') NOT NULL DEFAULT 'CORRECTIVE',
  `problem_description` TEXT NOT NULL,
  `external_service_name` VARCHAR(150) DEFAULT NULL,
  `estimated_cost` DECIMAL(18,2) DEFAULT 0.00,
  `actual_cost` DECIMAL(18,2) DEFAULT 0.00,
  `completed_date` DATETIME DEFAULT NULL,
  `result_summary` TEXT DEFAULT NULL,
  `created_by` BIGINT UNSIGNED DEFAULT NULL,
  `updated_by` BIGINT UNSIGNED DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`maintenance_ticket_id`),
  UNIQUE KEY `uk_maintenance_tickets_ticket_code` (`ticket_code`),
  KEY `idx_maintenance_tickets_asset_id` (`asset_id`),
  CONSTRAINT `fk_maintenance_tickets_incident_report`
    FOREIGN KEY (`incident_report_id`) REFERENCES `incident_reports` (`incident_report_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_maintenance_tickets_asset`
    FOREIGN KEY (`asset_id`) REFERENCES `assets` (`asset_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_maintenance_tickets_assigned_to_user`
    FOREIGN KEY (`assigned_to_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_maintenance_tickets_created_by`
    FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_maintenance_tickets_updated_by`
    FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `maintenance_updates` (
  `maintenance_update_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `maintenance_ticket_id` BIGINT UNSIGNED NOT NULL,
  `updated_by_user_id` BIGINT UNSIGNED NOT NULL,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_status` VARCHAR(50) DEFAULT NULL,
  `update_note` TEXT NOT NULL,
  `next_action_date` DATE DEFAULT NULL,
  PRIMARY KEY (`maintenance_update_id`),
  KEY `idx_maintenance_updates_ticket_id` (`maintenance_ticket_id`),
  CONSTRAINT `fk_maintenance_updates_ticket`
    FOREIGN KEY (`maintenance_ticket_id`) REFERENCES `maintenance_tickets` (`maintenance_ticket_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_maintenance_updates_updated_by_user`
    FOREIGN KEY (`updated_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `inventory_sessions` (
  `inventory_session_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `session_code` VARCHAR(50) NOT NULL,
  `session_name` VARCHAR(150) NOT NULL,
  `scope_type` ENUM('ALL','DEPARTMENT') NOT NULL DEFAULT 'ALL',
  `department_id` BIGINT UNSIGNED DEFAULT NULL,
  `started_by_user_id` BIGINT UNSIGNED NOT NULL,
  `start_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_time` DATETIME DEFAULT NULL,
  `status` ENUM('DRAFT','IN_PROGRESS','COMPLETED','CANCELED') NOT NULL DEFAULT 'DRAFT',
  `summary_note` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`inventory_session_id`),
  UNIQUE KEY `uk_inventory_sessions_session_code` (`session_code`),
  CONSTRAINT `fk_inventory_sessions_department`
    FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_sessions_started_by_user`
    FOREIGN KEY (`started_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `inventory_session_results` (
  `inventory_result_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `inventory_session_id` BIGINT UNSIGNED NOT NULL,
  `asset_id` BIGINT UNSIGNED NOT NULL,
  `actual_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `actual_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `actual_status_id` BIGINT UNSIGNED DEFAULT NULL,
  `result_type` ENUM('MATCH','MISSING','EXTRA','STATUS_MISMATCH','USER_MISMATCH','DEPARTMENT_MISMATCH') NOT NULL,
  `checked_by_user_id` BIGINT UNSIGNED NOT NULL,
  `checked_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `discrepancy_note` TEXT DEFAULT NULL,
  PRIMARY KEY (`inventory_result_id`),
  UNIQUE KEY `uk_inventory_result_asset` (`inventory_session_id`, `asset_id`),
  CONSTRAINT `fk_inventory_results_session`
    FOREIGN KEY (`inventory_session_id`) REFERENCES `inventory_sessions` (`inventory_session_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_results_asset`
    FOREIGN KEY (`asset_id`) REFERENCES `assets` (`asset_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_results_actual_department`
    FOREIGN KEY (`actual_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_results_actual_user`
    FOREIGN KEY (`actual_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_results_actual_status`
    FOREIGN KEY (`actual_status_id`) REFERENCES `asset_statuses` (`status_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_results_checked_by_user`
    FOREIGN KEY (`checked_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 4. HISTORY TABLE
-- ============================================================

CREATE TABLE `asset_histories` (
  `asset_history_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `asset_id` BIGINT UNSIGNED NOT NULL,
  `action_type` VARCHAR(50) NOT NULL,
  `reference_type` VARCHAR(50) DEFAULT NULL,
  `reference_id` BIGINT UNSIGNED DEFAULT NULL,
  `from_status_id` BIGINT UNSIGNED DEFAULT NULL,
  `to_status_id` BIGINT UNSIGNED DEFAULT NULL,
  `from_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `to_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `from_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `to_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `action_by_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `action_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` TEXT DEFAULT NULL,
  PRIMARY KEY (`asset_history_id`),
  KEY `idx_asset_histories_asset_time` (`asset_id`, `action_time`),
  CONSTRAINT `fk_asset_histories_asset`
    FOREIGN KEY (`asset_id`) REFERENCES `assets` (`asset_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_asset_histories_from_status`
    FOREIGN KEY (`from_status_id`) REFERENCES `asset_statuses` (`status_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asset_histories_to_status`
    FOREIGN KEY (`to_status_id`) REFERENCES `asset_statuses` (`status_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asset_histories_from_department`
    FOREIGN KEY (`from_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asset_histories_to_department`
    FOREIGN KEY (`to_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asset_histories_from_user`
    FOREIGN KEY (`from_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asset_histories_to_user`
    FOREIGN KEY (`to_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asset_histories_action_by_user`
    FOREIGN KEY (`action_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 5. SEED DATA CƠ BẢN
-- ============================================================

INSERT INTO `roles` (`role_id`, `role_code`, `role_name`, `description`) VALUES
(1, 'ADMIN', 'Admin', 'Quản trị hệ thống và có thể thao tác toàn bộ nghiệp vụ'),
(2, 'ASSET_STAFF', 'Nhân viên tài sản', 'Vận hành tài sản, cấp phát, thu hồi, điều chuyển, kiểm kê, bảo trì'),
(3, 'MANAGER', 'Quản lý', 'Xem dashboard, báo cáo, lịch sử và theo dõi tài sản theo đơn vị'),
(4, 'END_USER', 'Người nhận thiết bị', 'Người dùng cuối nhận tài sản và gửi báo hỏng');

INSERT INTO `departments` (`department_id`, `parent_department_id`, `department_code`, `department_name`, `manager_name`, `phone`, `email`, `description`) VALUES
(1, NULL, 'IT', 'Phòng Công nghệ thông tin', 'Nguyễn Minh Khôi', '02873001111', 'it@demo.local', 'Quản lý hệ thống CNTT và tài sản số'),
(2, NULL, 'ADMIN', 'Phòng Hành chính', 'Trần Hữu Đức', '02873002222', 'admin@demo.local', 'Phụ trách hành chính, kho và điều phối tài sản'),
(3, NULL, 'LAB', 'Phòng Thực hành', 'Lê Ngọc An', '02873003333', 'lab@demo.local', 'Quản lý phòng lab và thiết bị phục vụ thực hành'),
(4, NULL, 'TRAINING', 'Phòng Đào tạo', 'Phạm Minh Thu', '02873004444', 'training@demo.local', 'Sử dụng thiết bị phục vụ đào tạo và hội họp');

-- Mật khẩu mẫu:
-- admin         / admin123
-- asset_admin   / manager123
-- lab_manager   / staff123
-- training_user / staff123
INSERT INTO `users` (`user_id`, `role_id`, `department_id`, `username`, `full_name`, `email`, `phone`, `job_title`, `password_hash`, `status`) VALUES
(1, 1, 1, 'admin',         'Quản trị hệ thống',   'admin@demo.local',         '0901000001', 'System Administrator', '$2y$12$aq3iUerXk0mz63GSn/ck0u.JPdDpOnZtVuKMDNVOYyUCaTBlAbOLS', 'ACTIVE'),
(2, 2, 1, 'asset_admin',   'Nguyễn Minh Khôi',   'asset.admin@demo.local',   '0901000002', 'Asset Administrator',  '$2y$12$/7EM5PNhGbyutss5EAtlieluHvjOFqMMhwgCvwvIOO/wQg6M1IbAC', 'ACTIVE'),
(3, 3, 3, 'lab_manager',   'Lê Ngọc An',         'lab.manager@demo.local',   '0901000004', 'Lab Manager',          '$2y$12$7Mxbeb1Xt3wOCmshcBJAD.T2PDnardCwiHxE9KXswWpuQ5veo3sg.', 'ACTIVE'),
(4, 4, 4, 'training_user', 'Phạm Minh Thu',      'training.user@demo.local', '0901000005', 'Training Staff',       '$2y$12$7Mxbeb1Xt3wOCmshcBJAD.T2PDnardCwiHxE9KXswWpuQ5veo3sg.', 'ACTIVE');

INSERT INTO `asset_categories` (`category_id`, `parent_category_id`, `category_code`, `category_name`, `default_warranty_months`, `default_maintenance_cycle_days`, `description`) VALUES
(1, NULL, 'LAPTOP', 'Laptop', 24, 180, 'Máy tính xách tay phục vụ làm việc và đào tạo'),
(2, NULL, 'PROJECTOR', 'Máy chiếu', 24, 180, 'Thiết bị trình chiếu trong lớp học và phòng họp'),
(3, NULL, 'PRINTER', 'Máy in', 12, 90, 'Thiết bị in ấn văn phòng'),
(4, NULL, 'MONITOR', 'Màn hình', 24, 180, 'Màn hình rời cho máy trạm');

INSERT INTO `asset_statuses` (`status_id`, `status_code`, `status_name`, `status_group`, `is_allocatable`, `sort_order`, `description`) VALUES
(1, 'READY', 'Sẵn sàng cấp phát', 'STORAGE', 1, 1, 'Tài sản đang sẵn sàng để cấp phát'),
(2, 'IN_USE', 'Đang sử dụng', 'USAGE', 0, 2, 'Tài sản đang được cấp phát cho người dùng hoặc phòng ban'),
(3, 'WAITING_MAINTENANCE', 'Hỏng chờ xử lý', 'EXCEPTION', 0, 3, 'Tài sản có sự cố và đang chờ mở phiếu xử lý'),
(4, 'IN_MAINTENANCE', 'Đang bảo trì', 'MAINTENANCE', 0, 4, 'Tài sản đang được sửa chữa hoặc bảo trì'),
(5, 'RETIRED', 'Thanh lý', 'END_OF_LIFE', 0, 5, 'Tài sản đã ngừng sử dụng'),
(6, 'LOST', 'Mất hoặc không xác định', 'EXCEPTION', 0, 6, 'Tài sản bị mất hoặc chưa xác định được vị trí');

INSERT INTO `assets` (`asset_id`, `asset_code`, `asset_name`, `category_id`, `current_status_id`, `owning_department_id`, `current_department_id`, `assigned_user_id`, `brand`, `model`, `serial_number`, `asset_tag`, `purchase_date`, `warranty_expiry_date`, `purchase_cost`, `specification_text`, `notes`, `image_url`, `created_by`, `updated_by`) VALUES
(1, 'AST-LT-001', 'Laptop Dell Latitude 5440', 1, 2, 1, 4, 4, 'Dell', 'Latitude 5440', 'DL5440-0001', 'TAG-0001', '2025-09-01', '2027-09-01', 23500000.00, 'Core i5 / RAM 16GB / SSD 512GB', 'Đang cấp cho phòng đào tạo', NULL, 1, 1),
(2, 'AST-PJ-001', 'Máy chiếu Epson EB-X06', 2, 1, 2, 2, NULL, 'Epson', 'EB-X06', 'EPX06-0001', 'TAG-0002', '2025-10-10', '2027-10-10', 12500000.00, 'Máy chiếu phòng họp', 'Đang ở trạng thái sẵn sàng cấp phát', NULL, 1, 1),
(3, 'AST-PR-001', 'Máy in HP LaserJet M211', 3, 4, 2, 1, NULL, 'HP', 'LaserJet M211', 'HPM211-0001', 'TAG-0003', '2025-08-20', '2026-08-20', 4500000.00, 'Máy in đơn sắc văn phòng', 'Đang bảo trì', NULL, 1, 1);

INSERT INTO `asset_histories` (`asset_history_id`, `asset_id`, `action_type`, `reference_type`, `reference_id`, `from_status_id`, `to_status_id`, `from_department_id`, `to_department_id`, `from_user_id`, `to_user_id`, `action_by_user_id`, `action_time`, `description`) VALUES
(1, 1, 'ASSIGNMENT', 'assignment_forms', 1, 1, 2, 1, 4, NULL, 4, 2, '2026-04-01 09:00:00', 'Cấp phát laptop cho người dùng phòng đào tạo'),
(2, 3, 'MAINTENANCE', 'maintenance_tickets', 1, 3, 4, 2, 1, NULL, NULL, 2, '2026-04-03 14:30:00', 'Mở phiếu bảo trì cho máy in do lỗi kẹt giấy liên tục');

SET FOREIGN_KEY_CHECKS = 1;
