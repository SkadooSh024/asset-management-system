-- ============================================================
-- DATABASE: asset_management_portfolio
-- PURPOSE : Hệ thống quản lý tài sản, cấp phát và bảo trì/sửa chữa thiết bị
-- TARGET  : MySQL 8.x / MariaDB 10.4+ / WAMP / XAMPP / phpMyAdmin
-- CHARSET : utf8mb4

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

CREATE TABLE `locations` (
  `location_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `parent_location_id` BIGINT UNSIGNED DEFAULT NULL,
  `department_id` BIGINT UNSIGNED DEFAULT NULL,
  `location_code` VARCHAR(50) NOT NULL,
  `location_name` VARCHAR(150) NOT NULL,
  `location_type` ENUM('WAREHOUSE','ROOM','LAB','OFFICE','WORKSHOP','TEMP_STORAGE','OTHER') NOT NULL DEFAULT 'OTHER',
  `building_name` VARCHAR(100) DEFAULT NULL,
  `floor_name` VARCHAR(50) DEFAULT NULL,
  `room_number` VARCHAR(50) DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `is_active` TINYINT(1) NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`location_id`),
  UNIQUE KEY `uk_locations_location_code` (`location_code`),
  KEY `idx_locations_department_id` (`department_id`),
  CONSTRAINT `fk_locations_parent_location`
    FOREIGN KEY (`parent_location_id`) REFERENCES `locations` (`location_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_locations_department`
    FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`)
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
  `avatar_url` VARCHAR(255) DEFAULT NULL,
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

CREATE TABLE `vendors` (
  `vendor_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `vendor_code` VARCHAR(50) NOT NULL,
  `vendor_name` VARCHAR(150) NOT NULL,
  `vendor_type` ENUM('SUPPLIER','SERVICE_PROVIDER','BOTH') NOT NULL DEFAULT 'SUPPLIER',
  `contact_person` VARCHAR(100) DEFAULT NULL,
  `phone` VARCHAR(20) DEFAULT NULL,
  `email` VARCHAR(120) DEFAULT NULL,
  `address_line` VARCHAR(255) DEFAULT NULL,
  `tax_code` VARCHAR(50) DEFAULT NULL,
  `website_url` VARCHAR(255) DEFAULT NULL,
  `is_active` TINYINT(1) NOT NULL DEFAULT 1,
  `notes` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`vendor_id`),
  UNIQUE KEY `uk_vendors_vendor_code` (`vendor_code`)
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

CREATE TABLE `asset_conditions` (
  `condition_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `condition_code` VARCHAR(50) NOT NULL,
  `condition_name` VARCHAR(100) NOT NULL,
  `severity_level` TINYINT UNSIGNED NOT NULL DEFAULT 1,
  `description` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`condition_id`),
  UNIQUE KEY `uk_asset_conditions_condition_code` (`condition_code`)
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
  `current_condition_id` BIGINT UNSIGNED NOT NULL,
  `supplier_id` BIGINT UNSIGNED DEFAULT NULL,
  `owning_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `current_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `current_location_id` BIGINT UNSIGNED DEFAULT NULL,
  `assigned_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `brand` VARCHAR(100) DEFAULT NULL,
  `model` VARCHAR(100) DEFAULT NULL,
  `serial_number` VARCHAR(100) DEFAULT NULL,
  `asset_tag` VARCHAR(100) DEFAULT NULL,
  `purchase_date` DATE DEFAULT NULL,
  `received_date` DATE DEFAULT NULL,
  `warranty_expiry_date` DATE DEFAULT NULL,
  `purchase_cost` DECIMAL(18,2) DEFAULT 0.00,
  `residual_value` DECIMAL(18,2) DEFAULT 0.00,
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
  KEY `idx_assets_current_condition_id` (`current_condition_id`),
  KEY `idx_assets_supplier_id` (`supplier_id`),
  KEY `idx_assets_owning_department_id` (`owning_department_id`),
  KEY `idx_assets_current_department_id` (`current_department_id`),
  KEY `idx_assets_current_location_id` (`current_location_id`),
  KEY `idx_assets_assigned_user_id` (`assigned_user_id`),
  KEY `idx_assets_brand_model` (`brand`, `model`),
  CONSTRAINT `fk_assets_category`
    FOREIGN KEY (`category_id`) REFERENCES `asset_categories` (`category_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_assets_current_status`
    FOREIGN KEY (`current_status_id`) REFERENCES `asset_statuses` (`status_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_assets_current_condition`
    FOREIGN KEY (`current_condition_id`) REFERENCES `asset_conditions` (`condition_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_assets_supplier`
    FOREIGN KEY (`supplier_id`) REFERENCES `vendors` (`vendor_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_assets_owning_department`
    FOREIGN KEY (`owning_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_assets_current_department`
    FOREIGN KEY (`current_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_assets_current_location`
    FOREIGN KEY (`current_location_id`) REFERENCES `locations` (`location_id`)
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
  `source_location_id` BIGINT UNSIGNED DEFAULT NULL,
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
  CONSTRAINT `fk_assignment_forms_source_location`
    FOREIGN KEY (`source_location_id`) REFERENCES `locations` (`location_id`)
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
  `condition_before_id` BIGINT UNSIGNED DEFAULT NULL,
  `status_after_assignment_id` BIGINT UNSIGNED DEFAULT NULL,
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
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_assignment_form_details_condition_before`
    FOREIGN KEY (`condition_before_id`) REFERENCES `asset_conditions` (`condition_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_assignment_form_details_status_after_assignment`
    FOREIGN KEY (`status_after_assignment_id`) REFERENCES `asset_statuses` (`status_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `return_forms` (
  `return_form_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `form_code` VARCHAR(50) NOT NULL,
  `return_date` DATE NOT NULL,
  `source_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `source_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `received_by_user_id` BIGINT UNSIGNED NOT NULL,
  `target_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `target_location_id` BIGINT UNSIGNED DEFAULT NULL,
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
  CONSTRAINT `fk_return_forms_target_location`
    FOREIGN KEY (`target_location_id`) REFERENCES `locations` (`location_id`)
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
  `condition_after_return_id` BIGINT UNSIGNED DEFAULT NULL,
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
  CONSTRAINT `fk_return_form_details_condition_after_return`
    FOREIGN KEY (`condition_after_return_id`) REFERENCES `asset_conditions` (`condition_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
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
  `from_location_id` BIGINT UNSIGNED DEFAULT NULL,
  `to_department_id` BIGINT UNSIGNED NOT NULL,
  `to_location_id` BIGINT UNSIGNED DEFAULT NULL,
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
  CONSTRAINT `fk_transfer_forms_from_location`
    FOREIGN KEY (`from_location_id`) REFERENCES `locations` (`location_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_transfer_forms_to_department`
    FOREIGN KEY (`to_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_transfer_forms_to_location`
    FOREIGN KEY (`to_location_id`) REFERENCES `locations` (`location_id`)
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
  `condition_before_id` BIGINT UNSIGNED DEFAULT NULL,
  `condition_after_id` BIGINT UNSIGNED DEFAULT NULL,
  `note` TEXT DEFAULT NULL,
  PRIMARY KEY (`transfer_form_detail_id`),
  UNIQUE KEY `uk_transfer_form_asset` (`transfer_form_id`, `asset_id`),
  KEY `idx_transfer_form_details_asset_id` (`asset_id`),
  CONSTRAINT `fk_transfer_form_details_form`
    FOREIGN KEY (`transfer_form_id`) REFERENCES `transfer_forms` (`transfer_form_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_transfer_form_details_asset`
    FOREIGN KEY (`asset_id`) REFERENCES `assets` (`asset_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_transfer_form_details_condition_before`
    FOREIGN KEY (`condition_before_id`) REFERENCES `asset_conditions` (`condition_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_transfer_form_details_condition_after`
    FOREIGN KEY (`condition_after_id`) REFERENCES `asset_conditions` (`condition_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `maintenance_tickets` (
  `ticket_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ticket_code` VARCHAR(50) NOT NULL,
  `asset_id` BIGINT UNSIGNED NOT NULL,
  `ticket_type` ENUM('PREVENTIVE','CORRECTIVE','INSPECTION','REPAIR','WARRANTY') NOT NULL DEFAULT 'CORRECTIVE',
  `reported_by_user_id` BIGINT UNSIGNED NOT NULL,
  `assigned_to_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `service_provider_id` BIGINT UNSIGNED DEFAULT NULL,
  `priority` ENUM('LOW','MEDIUM','HIGH','CRITICAL') NOT NULL DEFAULT 'MEDIUM',
  `issue_title` VARCHAR(255) NOT NULL,
  `issue_description` TEXT NOT NULL,
  `diagnostic_result` TEXT DEFAULT NULL,
  `resolution_summary` TEXT DEFAULT NULL,
  `status` ENUM('OPEN','RECEIVED','IN_PROGRESS','WAITING_PARTS','COMPLETED','CLOSED','UNREPAIRABLE','CANCELED') NOT NULL DEFAULT 'OPEN',
  `reported_at` DATETIME NOT NULL,
  `accepted_at` DATETIME DEFAULT NULL,
  `started_at` DATETIME DEFAULT NULL,
  `expected_completion_date` DATE DEFAULT NULL,
  `completed_at` DATETIME DEFAULT NULL,
  `labor_cost` DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  `parts_cost` DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  `other_cost` DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  `total_cost` DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  `result_status_id` BIGINT UNSIGNED DEFAULT NULL,
  `result_condition_id` BIGINT UNSIGNED DEFAULT NULL,
  `is_warranty_claim` TINYINT(1) NOT NULL DEFAULT 0,
  `note` TEXT DEFAULT NULL,
  `created_by` BIGINT UNSIGNED DEFAULT NULL,
  `updated_by` BIGINT UNSIGNED DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ticket_id`),
  UNIQUE KEY `uk_maintenance_tickets_ticket_code` (`ticket_code`),
  KEY `idx_maintenance_tickets_asset_id` (`asset_id`),
  KEY `idx_maintenance_tickets_status` (`status`),
  KEY `idx_maintenance_tickets_priority` (`priority`),
  CONSTRAINT `fk_maintenance_tickets_asset`
    FOREIGN KEY (`asset_id`) REFERENCES `assets` (`asset_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_maintenance_tickets_reported_by_user`
    FOREIGN KEY (`reported_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_maintenance_tickets_assigned_to_user`
    FOREIGN KEY (`assigned_to_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_maintenance_tickets_service_provider`
    FOREIGN KEY (`service_provider_id`) REFERENCES `vendors` (`vendor_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_maintenance_tickets_result_status`
    FOREIGN KEY (`result_status_id`) REFERENCES `asset_statuses` (`status_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_maintenance_tickets_result_condition`
    FOREIGN KEY (`result_condition_id`) REFERENCES `asset_conditions` (`condition_id`)
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
  `ticket_id` BIGINT UNSIGNED NOT NULL,
  `updated_by_user_id` BIGINT UNSIGNED NOT NULL,
  `status_after_update` ENUM('OPEN','RECEIVED','IN_PROGRESS','WAITING_PARTS','COMPLETED','CLOSED','UNREPAIRABLE','CANCELED') NOT NULL,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `progress_note` TEXT NOT NULL,
  `next_action` VARCHAR(255) DEFAULT NULL,
  `expected_completion_date` DATE DEFAULT NULL,
  `attachment_url` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`maintenance_update_id`),
  KEY `idx_maintenance_updates_ticket_id` (`ticket_id`),
  CONSTRAINT `fk_maintenance_updates_ticket`
    FOREIGN KEY (`ticket_id`) REFERENCES `maintenance_tickets` (`ticket_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_maintenance_updates_updated_by_user`
    FOREIGN KEY (`updated_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `inventory_sessions` (
  `inventory_session_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `session_code` VARCHAR(50) NOT NULL,
  `session_name` VARCHAR(150) NOT NULL,
  `scope_type` ENUM('ALL','DEPARTMENT','LOCATION','CATEGORY') NOT NULL DEFAULT 'ALL',
  `department_id` BIGINT UNSIGNED DEFAULT NULL,
  `location_id` BIGINT UNSIGNED DEFAULT NULL,
  `category_id` BIGINT UNSIGNED DEFAULT NULL,
  `snapshot_at` DATETIME NOT NULL,
  `started_by_user_id` BIGINT UNSIGNED NOT NULL,
  `started_at` DATETIME NOT NULL,
  `ended_at` DATETIME DEFAULT NULL,
  `status` ENUM('DRAFT','IN_PROGRESS','COMPLETED','LOCKED','CANCELED') NOT NULL DEFAULT 'DRAFT',
  `note` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`inventory_session_id`),
  UNIQUE KEY `uk_inventory_sessions_session_code` (`session_code`),
  CONSTRAINT `fk_inventory_sessions_department`
    FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_sessions_location`
    FOREIGN KEY (`location_id`) REFERENCES `locations` (`location_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_sessions_category`
    FOREIGN KEY (`category_id`) REFERENCES `asset_categories` (`category_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_sessions_started_by_user`
    FOREIGN KEY (`started_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `inventory_session_snapshots` (
  `inventory_snapshot_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `inventory_session_id` BIGINT UNSIGNED NOT NULL,
  `asset_id` BIGINT UNSIGNED NOT NULL,
  `expected_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `expected_location_id` BIGINT UNSIGNED DEFAULT NULL,
  `expected_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `expected_status_id` BIGINT UNSIGNED DEFAULT NULL,
  `expected_condition_id` BIGINT UNSIGNED DEFAULT NULL,
  `snapshot_note` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`inventory_snapshot_id`),
  UNIQUE KEY `uk_inventory_snapshot_asset` (`inventory_session_id`, `asset_id`),
  CONSTRAINT `fk_inventory_snapshots_session`
    FOREIGN KEY (`inventory_session_id`) REFERENCES `inventory_sessions` (`inventory_session_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_snapshots_asset`
    FOREIGN KEY (`asset_id`) REFERENCES `assets` (`asset_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_snapshots_expected_department`
    FOREIGN KEY (`expected_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_snapshots_expected_location`
    FOREIGN KEY (`expected_location_id`) REFERENCES `locations` (`location_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_snapshots_expected_user`
    FOREIGN KEY (`expected_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_snapshots_expected_status`
    FOREIGN KEY (`expected_status_id`) REFERENCES `asset_statuses` (`status_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_snapshots_expected_condition`
    FOREIGN KEY (`expected_condition_id`) REFERENCES `asset_conditions` (`condition_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `inventory_session_results` (
  `inventory_result_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `inventory_session_id` BIGINT UNSIGNED NOT NULL,
  `asset_id` BIGINT UNSIGNED NOT NULL,
  `actual_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `actual_location_id` BIGINT UNSIGNED DEFAULT NULL,
  `actual_user_id` BIGINT UNSIGNED DEFAULT NULL,
  `actual_status_id` BIGINT UNSIGNED DEFAULT NULL,
  `actual_condition_id` BIGINT UNSIGNED DEFAULT NULL,
  `result_type` ENUM('MATCH','MISSING','EXCESS','MOVED','DAMAGED','STATUS_MISMATCH','USER_MISMATCH','LOCATION_MISMATCH') NOT NULL,
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
  CONSTRAINT `fk_inventory_results_actual_location`
    FOREIGN KEY (`actual_location_id`) REFERENCES `locations` (`location_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_results_actual_user`
    FOREIGN KEY (`actual_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_results_actual_status`
    FOREIGN KEY (`actual_status_id`) REFERENCES `asset_statuses` (`status_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_results_actual_condition`
    FOREIGN KEY (`actual_condition_id`) REFERENCES `asset_conditions` (`condition_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_results_checked_by_user`
    FOREIGN KEY (`checked_by_user_id`) REFERENCES `users` (`user_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 4. HISTORY / AUDIT TABLES
-- ============================================================

CREATE TABLE `asset_histories` (
  `asset_history_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `asset_id` BIGINT UNSIGNED NOT NULL,
  `action_type` VARCHAR(50) NOT NULL,
  `reference_type` VARCHAR(50) DEFAULT NULL,
  `reference_id` BIGINT UNSIGNED DEFAULT NULL,
  `from_status_id` BIGINT UNSIGNED DEFAULT NULL,
  `to_status_id` BIGINT UNSIGNED DEFAULT NULL,
  `from_condition_id` BIGINT UNSIGNED DEFAULT NULL,
  `to_condition_id` BIGINT UNSIGNED DEFAULT NULL,
  `from_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `to_department_id` BIGINT UNSIGNED DEFAULT NULL,
  `from_location_id` BIGINT UNSIGNED DEFAULT NULL,
  `to_location_id` BIGINT UNSIGNED DEFAULT NULL,
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
  CONSTRAINT `fk_asset_histories_from_condition`
    FOREIGN KEY (`from_condition_id`) REFERENCES `asset_conditions` (`condition_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asset_histories_to_condition`
    FOREIGN KEY (`to_condition_id`) REFERENCES `asset_conditions` (`condition_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asset_histories_from_department`
    FOREIGN KEY (`from_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asset_histories_to_department`
    FOREIGN KEY (`to_department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asset_histories_from_location`
    FOREIGN KEY (`from_location_id`) REFERENCES `locations` (`location_id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asset_histories_to_location`
    FOREIGN KEY (`to_location_id`) REFERENCES `locations` (`location_id`)
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

CREATE TABLE `audit_logs` (
  `audit_log_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED DEFAULT NULL,
  `module_name` VARCHAR(100) NOT NULL,
  `action_name` VARCHAR(100) NOT NULL,
  `reference_table` VARCHAR(100) DEFAULT NULL,
  `reference_id` BIGINT UNSIGNED DEFAULT NULL,
  `http_method` VARCHAR(10) DEFAULT NULL,
  `request_path` VARCHAR(255) DEFAULT NULL,
  `old_data` LONGTEXT DEFAULT NULL,
  `new_data` LONGTEXT DEFAULT NULL,
  `ip_address` VARCHAR(45) DEFAULT NULL,
  `user_agent` VARCHAR(255) DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`audit_log_id`),
  KEY `idx_audit_logs_user_module` (`user_id`, `module_name`),
  CONSTRAINT `fk_audit_logs_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 5. SEED DATA
-- ============================================================

INSERT INTO `roles` (`role_id`, `role_code`, `role_name`, `description`) VALUES
(1, 'SUPER_ADMIN', 'Super Admin', 'Toàn quyền hệ thống'),
(2, 'ASSET_ADMIN', 'Quản trị tài sản', 'Quản lý tài sản, cấp phát, thu hồi, điều chuyển'),
(3, 'TECHNICIAN', 'Kỹ thuật viên', 'Xử lý bảo trì, sửa chữa thiết bị'),
(4, 'DEPARTMENT_MANAGER', 'Quản lý đơn vị', 'Phê duyệt và theo dõi tài sản thuộc đơn vị'),
(5, 'STAFF', 'Nhân viên', 'Người dùng cuối, báo hỏng, nhận cấp phát');

INSERT INTO `departments` (`department_id`, `department_code`, `department_name`, `manager_name`, `phone`, `email`, `description`) VALUES
(1, 'IT', 'Phòng Công nghệ thông tin', 'Nguyễn Minh Khôi', '02873001111', 'it@demo.local', 'Quản lý hệ thống CNTT và tài sản số.'),
(2, 'ADMIN', 'Phòng Hành chính', 'Trần Hữu Đức', '02873002222', 'admin@demo.local', 'Phụ trách hành chính, kho và điều phối tài sản.'),
(3, 'LAB', 'Phòng Thực hành', 'Lê Ngọc An', '02873003333', 'lab@demo.local', 'Quản lý phòng lab và thiết bị phục vụ thực hành.'),
(4, 'TRAINING', 'Phòng Đào tạo', 'Phạm Minh Thu', '02873004444', 'training@demo.local', 'Sử dụng thiết bị phục vụ đào tạo và hội họp.');

INSERT INTO `locations` (`location_id`, `department_id`, `location_code`, `location_name`, `location_type`, `building_name`, `floor_name`, `room_number`, `description`) VALUES
(1, 2, 'WH-CENTRAL', 'Kho thiết bị trung tâm', 'WAREHOUSE', 'A', 'Tầng trệt', 'K1', 'Kho chính chứa tài sản sẵn sàng cấp phát'),
(2, 3, 'LAB-A1', 'Phòng Lab A1', 'LAB', 'B', 'Tầng 2', 'A1', 'Phòng thực hành chính'),
(3, 4, 'MEET-B2', 'Phòng họp B2', 'ROOM', 'A', 'Tầng 3', 'B2', 'Phòng họp và thuyết trình'),
(4, 1, 'WORKSHOP-01', 'Xưởng sửa chữa nội bộ', 'WORKSHOP', 'C', 'Tầng trệt', 'S1', 'Nơi tiếp nhận sửa chữa, bảo trì'),
(5, 1, 'TEMP-HOLD', 'Kho tạm bảo trì', 'TEMP_STORAGE', 'C', 'Tầng trệt', 'T1', 'Kho tạm giữ tài sản đang chờ xử lý');

-- Mật khẩu mẫu:
-- admin         / admin123
-- asset_admin   / manager123
-- technician    / tech123
-- lab_manager   / staff123
-- training_user / staff123
INSERT INTO `users` (`user_id`, `role_id`, `department_id`, `username`, `full_name`, `email`, `phone`, `job_title`, `password_hash`, `status`) VALUES
(1, 1, 1, 'admin',         'Quản trị hệ thống',   'admin@demo.local',        '0901000001', 'System Administrator', '$2y$12$aq3iUerXk0mz63GSn/ck0u.JPdDpOnZtVuKMDNVOYyUCaTBlAbOLS', 'ACTIVE'),
(2, 2, 1, 'asset_admin',   'Nguyễn Minh Khôi',   'asset.admin@demo.local',  '0901000002', 'Asset Administrator',  '$2y$12$/7EM5PNhGbyutss5EAtlieluHvjOFqMMhwgCvwvIOO/wQg6M1IbAC', 'ACTIVE'),
(3, 3, 1, 'technician',    'Trần Quốc Bảo',      'tech@demo.local',         '0901000003', 'Technician',           '$2y$12$ueyh6O2GbXvpt2anBrKFJOJY06U1j.Mr9bbbeSAtJoxDq8PA2QoIa', 'ACTIVE'),
(4, 4, 3, 'lab_manager',   'Lê Ngọc An',         'lab.manager@demo.local',  '0901000004', 'Lab Manager',          '$2y$12$7Mxbeb1Xt3wOCmshcBJAD.T2PDnardCwiHxE9KXswWpuQ5veo3sg.', 'ACTIVE'),
(5, 5, 4, 'training_user', 'Phạm Minh Thu',      'training.user@demo.local','0901000005', 'Training Staff',       '$2y$12$7Mxbeb1Xt3wOCmshcBJAD.T2PDnardCwiHxE9KXswWpuQ5veo3sg.', 'ACTIVE');

INSERT INTO `vendors` (`vendor_id`, `vendor_code`, `vendor_name`, `vendor_type`, `contact_person`, `phone`, `email`, `address_line`, `tax_code`, `website_url`, `notes`) VALUES
(1, 'VND-FPT',      'FPT Shop',                    'SUPPLIER',        'Nguyễn Hoài Nam', '02811112222', 'sales@fptshop.demo', '123 Nguyễn Văn Linh, Q7, TP.HCM', '0312345678', 'https://example.com', 'Nhà cung cấp laptop và thiết bị văn phòng'),
(2, 'VND-SAOVIET',  'Công ty Bảo trì Sao Việt',    'SERVICE_PROVIDER','Trần Văn Bảy',    '02833334444', 'support@saoviet.demo','45 Lê Lợi, Q1, TP.HCM',          '0309988776', 'https://example.com', 'Đơn vị sửa chữa và bảo trì máy in, máy chiếu'),
(3, 'VND-ABC',      'Công ty TNHH Thiết bị ABC',   'BOTH',            'Phạm Gia Huy',    '02855556666', 'contact@abc.demo',    '98 Điện Biên Phủ, Bình Thạnh',    '0311223344', 'https://example.com', 'Vừa bán thiết bị, vừa cung cấp dịch vụ kỹ thuật');

INSERT INTO `asset_categories` (`category_id`, `category_code`, `category_name`, `default_warranty_months`, `default_maintenance_cycle_days`, `description`) VALUES
(1, 'LAPTOP',   'Laptop',        24, 180, 'Thiết bị máy tính xách tay'),
(2, 'DESKTOP',  'Desktop',       24, 180, 'Máy tính để bàn'),
(3, 'PRINTER',  'Printer',       12,  90, 'Máy in và thiết bị in ấn'),
(4, 'PROJECTOR','Projector',     24, 120, 'Máy chiếu phục vụ đào tạo/họp'),
(5, 'NETWORK',  'Network Device',24, 180, 'Thiết bị mạng như router, switch');

INSERT INTO `asset_statuses` (`status_id`, `status_code`, `status_name`, `status_group`, `is_allocatable`, `sort_order`, `description`) VALUES
(1, 'IN_STOCK',              'Đang lưu kho',                'STORAGE',     1, 1, 'Thiết bị đang nằm trong kho và sẵn sàng xử lý tiếp'),
(2, 'READY_FOR_ASSIGNMENT',  'Sẵn sàng cấp phát',           'STORAGE',     1, 2, 'Thiết bị hợp lệ để cấp phát'),
(3, 'IN_USE',                'Đang sử dụng',                'USAGE',       0, 3, 'Thiết bị đã cấp phát và đang được sử dụng'),
(4, 'PENDING_RETURN_CHECK',  'Chờ kiểm tra sau thu hồi',    'USAGE',       0, 4, 'Thiết bị đã thu hồi nhưng cần kiểm tra lại'),
(5, 'UNDER_MAINTENANCE',     'Đang bảo trì/sửa chữa',       'MAINTENANCE', 0, 5, 'Thiết bị đang được sửa hoặc bảo trì'),
(6, 'BROKEN_PENDING',        'Hỏng chờ xử lý',              'EXCEPTION',   0, 6, 'Thiết bị hỏng và chưa vào sửa chữa'),
(7, 'LOST',                  'Mất/không xác định',          'EXCEPTION',   0, 7, 'Thiết bị thất lạc hoặc chưa xác định được vị trí'),
(8, 'LIQUIDATED',            'Thanh lý/ngừng sử dụng',      'END_OF_LIFE', 0, 8, 'Thiết bị đã thanh lý hoặc loại bỏ');

INSERT INTO `asset_conditions` (`condition_id`, `condition_code`, `condition_name`, `severity_level`, `description`) VALUES
(1, 'NEW',     'Mới',              1, 'Tình trạng như mới'),
(2, 'GOOD',    'Tốt',              2, 'Hoạt động ổn định, có thể cấp phát'),
(3, 'FAIR',    'Trung bình',       3, 'Có hao mòn nhưng vẫn sử dụng được'),
(4, 'POOR',    'Kém',              4, 'Cần kiểm tra hoặc bảo trì sớm'),
(5, 'DAMAGED', 'Hư hỏng',          5, 'Thiết bị hư hỏng hoặc không sử dụng được');

INSERT INTO `assets` (
  `asset_id`, `asset_code`, `asset_name`, `category_id`, `current_status_id`, `current_condition_id`,
  `supplier_id`, `owning_department_id`, `current_department_id`, `current_location_id`, `assigned_user_id`,
  `brand`, `model`, `serial_number`, `asset_tag`, `purchase_date`, `received_date`, `warranty_expiry_date`,
  `purchase_cost`, `residual_value`, `specification_text`, `notes`, `created_by`, `updated_by`
) VALUES
(1, 'AST-LAP-0001', 'Laptop Dell Latitude 5440', 1, 3, 2, 1, 1, 3, 2, 4, 'Dell', 'Latitude 5440', 'SN-DL5440-0001', 'TAG-0001', '2025-08-15', '2025-08-20', '2027-08-15', 24500000.00, 21000000.00, 'CPU Intel Core i5 / RAM 16GB / SSD 512GB', 'Đang cấp phát cho quản lý phòng lab', 2, 2),
(2, 'AST-LAP-0002', 'MacBook Pro 14 M3',         1, 2, 1, 3, 1, 2, 1, NULL, 'Apple', 'MacBook Pro 14', 'SN-MBP14-0002', 'TAG-0002', '2026-01-12', '2026-01-18', '2028-01-12', 39900000.00, 39000000.00, 'Apple M3 / RAM 16GB / SSD 512GB', 'Đang lưu tại kho chờ cấp phát', 2, 2),
(3, 'AST-PRN-0001', 'Máy in HP LaserJet Pro 4003',3, 5, 4, 2, 2, 1, 4, NULL, 'HP', 'LaserJet Pro 4003', 'SN-HP4003-0001', 'TAG-0003', '2024-09-10', '2024-09-15', '2025-09-10', 8900000.00, 5200000.00, 'In laser trắng đen, kết nối LAN/WiFi', 'Đang được kỹ thuật kiểm tra hiện tượng kẹt giấy', 2, 3),
(4, 'AST-PROJ-0001', 'Máy chiếu Epson EB-FH52',  4, 3, 2, 3, 4, 4, 3, 5, 'Epson', 'EB-FH52', 'SN-EPSON-0001', 'TAG-0004', '2025-06-01', '2025-06-05', '2027-06-01', 18500000.00, 16000000.00, 'Độ sáng 4000 lumen, Full HD', 'Đang sử dụng cho phòng đào tạo', 2, 2),
(5, 'AST-RTR-0001', 'Router MikroTik RB4011',    5, 2, 2, 3, 1, 1, 1, NULL, 'MikroTik', 'RB4011', 'SN-MKT-0001', 'TAG-0005', '2025-11-20', '2025-11-22', '2027-11-20', 6200000.00, 5400000.00, 'Router 10 cổng Gigabit, phù hợp mạng nội bộ', 'Thiết bị mạng dự phòng', 2, 2),
(6, 'AST-DESK-0001', 'Desktop Dell OptiPlex 7010',2, 6, 5, 1, 3, 3, 2, NULL, 'Dell', 'OptiPlex 7010', 'SN-OPT7010-0001', 'TAG-0006', '2023-03-10', '2023-03-15', '2025-03-10', 16500000.00, 7500000.00, 'CPU Intel Core i7 / RAM 16GB / SSD 512GB', 'Máy phát sinh lỗi nguồn, chờ quyết định sửa chữa', 2, 2);

INSERT INTO `assignment_forms` (`assignment_form_id`, `form_code`, `assignment_date`, `source_department_id`, `source_location_id`, `target_department_id`, `target_user_id`, `issued_by_user_id`, `approved_by_user_id`, `status`, `reason`, `note`, `created_by`, `updated_by`) VALUES
(1, 'AF-2026-0001', '2026-03-28', 2, 1, 3, 4, 2, 1, 'COMPLETED', 'Cấp laptop cho quản lý phòng lab', 'Phiếu cấp phát chính thức cho phòng thực hành', 2, 2),
(2, 'AF-2026-0002', '2026-04-02', 2, 1, 4, 5, 2, 1, 'COMPLETED', 'Cấp máy chiếu cho phòng đào tạo', 'Thiết bị phục vụ thuyết trình và họp', 2, 2);

INSERT INTO `assignment_form_details` (`assignment_form_detail_id`, `assignment_form_id`, `asset_id`, `condition_before_id`, `status_after_assignment_id`, `expected_return_date`, `note`) VALUES
(1, 1, 1, 2, 3, NULL, 'Bàn giao kèm sạc và túi xách'),
(2, 2, 4, 2, 3, NULL, 'Bàn giao kèm dây nguồn và remote');

INSERT INTO `maintenance_tickets` (
  `ticket_id`, `ticket_code`, `asset_id`, `ticket_type`, `reported_by_user_id`, `assigned_to_user_id`, `service_provider_id`,
  `priority`, `issue_title`, `issue_description`, `diagnostic_result`, `resolution_summary`, `status`, `reported_at`,
  `accepted_at`, `started_at`, `expected_completion_date`, `completed_at`, `labor_cost`, `parts_cost`, `other_cost`, `total_cost`,
  `result_status_id`, `result_condition_id`, `is_warranty_claim`, `note`, `created_by`, `updated_by`
) VALUES
(1, 'MT-2026-0001', 3, 'REPAIR', 5, 3, 2, 'HIGH', 'Máy in kẹt giấy liên tục',
 'Máy in thường xuyên báo kẹt giấy sau 2-3 lệnh in, có tiếng kêu bất thường ở cụm kéo giấy.',
 'Phát hiện cụm roller kéo giấy mòn, cảm biến giấy bám bụi, cần vệ sinh và thay roller.',
 NULL,
 'IN_PROGRESS', '2026-04-07 08:30:00', '2026-04-07 09:15:00', '2026-04-07 10:00:00', '2026-04-12', NULL,
 150000.00, 200000.00, 0.00, 350000.00, NULL, NULL, 0,
 'Ưu tiên xử lý trong tuần vì thiết bị dùng cho in hồ sơ hành chính.', 2, 3);

INSERT INTO `maintenance_updates` (`maintenance_update_id`, `ticket_id`, `updated_by_user_id`, `status_after_update`, `update_time`, `progress_note`, `next_action`, `expected_completion_date`) VALUES
(1, 1, 3, 'RECEIVED',     '2026-04-07 09:15:00', 'Đã tiếp nhận thiết bị tại xưởng sửa chữa, ghi nhận ngoại quan.', 'Tháo kiểm tra cụm kéo giấy.', '2026-04-10'),
(2, 1, 3, 'IN_PROGRESS',  '2026-04-07 11:30:00', 'Đã tháo cụm roller, vệ sinh cảm biến, phát hiện roller mòn.', 'Mua linh kiện roller thay thế.', '2026-04-12'),
(3, 1, 3, 'WAITING_PARTS','2026-04-08 15:00:00', 'Đang chờ nhà cung cấp giao roller mới.', 'Lắp roller và test in sau khi nhận hàng.', '2026-04-12');

INSERT INTO `inventory_sessions` (`inventory_session_id`, `session_code`, `session_name`, `scope_type`, `department_id`, `snapshot_at`, `started_by_user_id`, `started_at`, `ended_at`, `status`, `note`) VALUES
(1, 'IV-2026-Q2-LAB', 'Kiểm kê Quý II - Phòng Thực hành', 'DEPARTMENT', 3, '2026-04-05 08:00:00', 2, '2026-04-05 08:00:00', '2026-04-05 17:00:00', 'COMPLETED', 'Kiểm kê định kỳ tài sản tại phòng thực hành.');

INSERT INTO `inventory_session_snapshots` (`inventory_snapshot_id`, `inventory_session_id`, `asset_id`, `expected_department_id`, `expected_location_id`, `expected_user_id`, `expected_status_id`, `expected_condition_id`, `snapshot_note`) VALUES
(1, 1, 1, 3, 2, 4, 3, 2, 'Laptop đang cấp phát cho quản lý phòng lab'),
(2, 1, 6, 3, 2, NULL, 6, 5, 'Máy desktop lỗi nguồn đang chờ quyết định xử lý');

INSERT INTO `inventory_session_results` (`inventory_result_id`, `inventory_session_id`, `asset_id`, `actual_department_id`, `actual_location_id`, `actual_user_id`, `actual_status_id`, `actual_condition_id`, `result_type`, `checked_by_user_id`, `checked_at`, `discrepancy_note`) VALUES
(1, 1, 1, 3, 2, 4, 3, 2, 'MATCH',   4, '2026-04-05 09:15:00', 'Thiết bị đúng vị trí và người dùng.'),
(2, 1, 6, 3, 2, NULL, 6, 5, 'MATCH', 4, '2026-04-05 10:30:00', 'Thiết bị đúng vị trí, tình trạng hỏng chờ xử lý.');

INSERT INTO `asset_histories` (
  `asset_history_id`, `asset_id`, `action_type`, `reference_type`, `reference_id`,
  `from_status_id`, `to_status_id`, `from_condition_id`, `to_condition_id`,
  `from_department_id`, `to_department_id`, `from_location_id`, `to_location_id`,
  `from_user_id`, `to_user_id`, `action_by_user_id`, `action_time`, `description`
) VALUES
(1, 1, 'ASSET_CREATED',          'ASSET',            1, NULL, 2, NULL, 2, NULL, 2, NULL, 1, NULL, NULL, 2, '2026-03-20 09:00:00', 'Tạo mới tài sản và nhập kho ban đầu.'),
(2, 1, 'ASSIGNED',               'ASSIGNMENT_FORM',  1, 2, 3, 2, 2, 2, 3, 1, 2, NULL, 4, 2, '2026-03-28 14:00:00', 'Cấp phát laptop cho quản lý phòng lab.'),
(3, 3, 'ASSET_CREATED',          'ASSET',            3, NULL, 2, NULL, 3, NULL, 2, NULL, 1, NULL, NULL, 2, '2024-09-15 10:00:00', 'Tạo mới máy in khi tiếp nhận từ nhà cung cấp.'),
(4, 3, 'MAINTENANCE_CREATED',    'MAINTENANCE_TICKET',1, 6, 5, 4, 4, 2, 1, 1, 4, NULL, NULL, 2, '2026-04-07 08:30:00', 'Mở phiếu sửa chữa máy in do lỗi kẹt giấy.'),
(5, 4, 'ASSET_CREATED',          'ASSET',            4, NULL, 2, NULL, 2, NULL, 2, NULL, 1, NULL, NULL, 2, '2026-03-25 08:45:00', 'Nhập máy chiếu vào kho thiết bị.'),
(6, 4, 'ASSIGNED',               'ASSIGNMENT_FORM',  2, 2, 3, 2, 2, 2, 4, 1, 3, NULL, 5, 2, '2026-04-02 15:00:00', 'Cấp phát máy chiếu cho phòng đào tạo.'),
(7, 6, 'BROKEN_REPORTED',        'INVENTORY_SESSION',1, 3, 6, 4, 5, 3, 3, 2, 2, NULL, NULL, 4, '2026-04-05 10:30:00', 'Ghi nhận máy desktop hỏng trong kỳ kiểm kê.');

INSERT INTO `audit_logs` (`audit_log_id`, `user_id`, `module_name`, `action_name`, `reference_table`, `reference_id`, `http_method`, `request_path`, `old_data`, `new_data`, `ip_address`, `user_agent`, `created_at`) VALUES
(1, 2, 'ASSET', 'CREATE', 'assets', 1, 'POST', '/api/assets', NULL, '{"asset_code":"AST-LAP-0001","asset_name":"Laptop Dell Latitude 5440"}', '127.0.0.1', 'seed-script', '2026-03-20 09:00:00'),
(2, 2, 'ASSIGNMENT', 'CONFIRM', 'assignment_forms', 1, 'POST', '/api/assignment-forms/1/confirm', '{"status":"DRAFT"}', '{"status":"COMPLETED"}', '127.0.0.1', 'seed-script', '2026-03-28 14:00:00');

-- ============================================================
-- 6. HELPFUL VIEWS
-- ============================================================

CREATE OR REPLACE VIEW `vw_asset_overview` AS
SELECT
  a.asset_id,
  a.asset_code,
  a.asset_name,
  c.category_code,
  c.category_name,
  s.status_code,
  s.status_name,
  ac.condition_code,
  ac.condition_name,
  d.department_code AS current_department_code,
  d.department_name AS current_department_name,
  l.location_code AS current_location_code,
  l.location_name AS current_location_name,
  u.username AS assigned_username,
  u.full_name AS assigned_full_name,
  a.brand,
  a.model,
  a.serial_number,
  a.purchase_date,
  a.warranty_expiry_date,
  a.purchase_cost,
  a.residual_value,
  a.is_active,
  a.created_at,
  a.updated_at
FROM `assets` a
INNER JOIN `asset_categories` c ON c.category_id = a.category_id
INNER JOIN `asset_statuses` s ON s.status_id = a.current_status_id
INNER JOIN `asset_conditions` ac ON ac.condition_id = a.current_condition_id
LEFT JOIN `departments` d ON d.department_id = a.current_department_id
LEFT JOIN `locations` l ON l.location_id = a.current_location_id
LEFT JOIN `users` u ON u.user_id = a.assigned_user_id;

CREATE OR REPLACE VIEW `vw_open_maintenance_tickets` AS
SELECT
  mt.ticket_id,
  mt.ticket_code,
  a.asset_code,
  a.asset_name,
  mt.ticket_type,
  mt.priority,
  mt.status,
  mt.reported_at,
  mt.expected_completion_date,
  reporter.full_name AS reported_by,
  assignee.full_name AS assigned_to,
  sp.vendor_name AS service_provider,
  mt.total_cost
FROM `maintenance_tickets` mt
INNER JOIN `assets` a ON a.asset_id = mt.asset_id
INNER JOIN `users` reporter ON reporter.user_id = mt.reported_by_user_id
LEFT JOIN `users` assignee ON assignee.user_id = mt.assigned_to_user_id
LEFT JOIN `vendors` sp ON sp.vendor_id = mt.service_provider_id
WHERE mt.status IN ('OPEN','RECEIVED','IN_PROGRESS','WAITING_PARTS');

SET FOREIGN_KEY_CHECKS = 1;
