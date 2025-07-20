-- Clean data.sql file with correct table structure
-- Insert 180 dummy services following aaa-bbb-ccc format
INSERT INTO services (name) VALUES
-- Authentication Services
('auth-login-service'), ('auth-logout-service'), ('auth-token-service'), ('auth-session-service'), ('auth-oauth-service'),
('auth-ldap-service'), ('auth-saml-service'), ('auth-jwt-service'), ('auth-mfa-service'), ('auth-sso-service'),

-- Payment Services  
('pay-gateway-service'), ('pay-processor-service'), ('pay-validation-service'), ('pay-refund-service'), ('pay-settlement-service'),
('pay-currency-service'), ('pay-fraud-service'), ('pay-billing-service'), ('pay-invoice-service'), ('pay-subscription-service'),

-- User Management Services
('user-profile-service'), ('user-auth-service'), ('user-register-service'), ('user-update-service'), ('user-delete-service'),
('user-search-service'), ('user-verify-service'), ('user-suspend-service'), ('user-restore-service'), ('user-migrate-service'),

-- Notification Services
('notif-email-service'), ('notif-sms-service'), ('notif-push-service'), ('notif-template-service'), ('notif-queue-service'),
('notif-delivery-service'), ('notif-schedule-service'), ('notif-history-service'), ('notif-preference-service'), ('notif-analytics-service'),

-- Data Services
('data-sync-service'), ('data-backup-service'), ('data-restore-service'), ('data-migration-service'), ('data-validation-service'),
('data-transform-service'), ('data-analytics-service'), ('data-export-service'), ('data-import-service'), ('data-archive-service'),

-- API Services
('api-gateway-service'), ('api-rate-service'), ('api-monitor-service'), ('api-version-service'), ('api-security-service'),
('api-cache-service'), ('api-proxy-service'), ('api-transform-service'), ('api-validate-service'), ('api-document-service'),

-- Security Services
('sec-encrypt-service'), ('sec-decrypt-service'), ('sec-hash-service'), ('sec-verify-service'), ('sec-audit-service'),
('sec-policy-service'), ('sec-compliance-service'), ('sec-scan-service'), ('sec-monitor-service'), ('sec-alert-service'),

-- File Services
('file-upload-service'), ('file-download-service'), ('file-storage-service'), ('file-compress-service'), ('file-convert-service'),
('file-preview-service'), ('file-search-service'), ('file-share-service'), ('file-backup-service'), ('file-sync-service'),

-- Reporting Services
('report-generate-service'), ('report-schedule-service'), ('report-export-service'), ('report-template-service'), ('report-data-service'),
('report-chart-service'), ('report-dashboard-service'), ('report-alert-service'), ('report-archive-service'), ('report-share-service'),

-- Workflow Services
('flow-orchestrate-service'), ('flow-execute-service'), ('flow-monitor-service'), ('flow-schedule-service'), ('flow-retry-service'),
('flow-approval-service'), ('flow-escalate-service'), ('flow-audit-service'), ('flow-template-service'), ('flow-optimize-service'),

-- Integration Services
('int-connector-service'), ('int-transform-service'), ('int-route-service'), ('int-queue-service'), ('int-batch-service'),
('int-stream-service'), ('int-sync-service'), ('int-validate-service'), ('int-monitor-service'), ('int-error-service'),

-- Configuration Services
('config-manage-service'), ('config-deploy-service'), ('config-validate-service'), ('config-backup-service'), ('config-restore-service'),
('config-version-service'), ('config-encrypt-service'), ('config-audit-service'), ('config-template-service'), ('config-sync-service'),

-- Monitoring Services
('mon-health-service'), ('mon-metrics-service'), ('mon-logs-service'), ('mon-trace-service'), ('mon-alert-service'),
('mon-dashboard-service'), ('mon-report-service'), ('mon-analytics-service'), ('mon-forecast-service'), ('mon-optimize-service'),

-- Cache Services
('cache-redis-service'), ('cache-memory-service'), ('cache-cluster-service'), ('cache-invalidate-service'), ('cache-preload-service'),
('cache-stats-service'), ('cache-backup-service'), ('cache-restore-service'), ('cache-optimize-service'), ('cache-monitor-service'),

-- Search Services
('search-index-service'), ('search-query-service'), ('search-suggest-service'), ('search-filter-service'), ('search-rank-service'),
('search-analytics-service'), ('search-optimize-service'), ('search-update-service'), ('search-backup-service'), ('search-restore-service'),

-- Communication Services
('comm-chat-service'), ('comm-video-service'), ('comm-audio-service'), ('comm-message-service'), ('comm-broadcast-service'),
('comm-conference-service'), ('comm-record-service'), ('comm-translate-service'), ('comm-moderate-service'), ('comm-archive-service'),

-- Content Services
('content-create-service'), ('content-edit-service'), ('content-publish-service'), ('content-review-service'), ('content-archive-service'),
('content-search-service'), ('content-tag-service'), ('content-version-service'), ('content-backup-service'), ('content-sync-service'),

-- Location Services
('loc-geocode-service'), ('loc-map-service'), ('loc-route-service'), ('loc-track-service'), ('loc-geofence-service'),
('loc-distance-service'), ('loc-weather-service'), ('loc-timezone-service'), ('loc-address-service'), ('loc-coordinate-service'),

-- Machine Learning Services
('ml-train-service'), ('ml-predict-service'), ('ml-evaluate-service'), ('ml-deploy-service'), ('ml-monitor-service'),
('ml-optimize-service'), ('ml-data-service'), ('ml-feature-service'), ('ml-model-service'), ('ml-pipeline-service');

-- Insert users with role-based permissions
INSERT INTO users (username, password, role) VALUES 
('superadmin1', 'admin123', 'superadmin'),
('admin1', 'admin123', 'admin'),
('admin2', 'admin123', 'admin'), 
('dev1', 'dev123', 'developer'),
('dev2', 'dev123', 'developer'),
('dev3', 'dev123', 'developer');

-- Insert releases with name and description only
INSERT INTO releases (name, description) VALUES 
('2025-01', 'Initial release for January 2025'),
('2025-02', 'February 2025 feature release'),
('2025-03', 'March 2025 quarterly release'),
('2025-04', 'April 2025 spring release'),
('2025-05', 'May 2025 enhancement release'),
('2025-06', 'June 2025 mid-year release'),
('2025-07', 'July 2025 summer release'),
('2025-08', 'August 2025 maintenance release'),
('2025-09', 'September 2025 quarterly release'),
('2025-10', 'October 2025 fall release'),
('2025-11', 'November 2025 pre-holiday release'),
('2025-12', 'December 2025 year-end release'),
('2026-01', 'January 2026 major release'),
('2026-02', 'February 2026 feature release'),
('2026-03', 'March 2026 quarterly release'),
('2026-04', 'April 2026 spring release'),
('2026-05', 'May 2026 enhancement release'),
('2026-06', 'June 2026 mid-year release');

-- Insert deployment entries 1-30 (ALL environments exclude PROD - as per requirement)
-- Using proper column structure matching Deployment entity
INSERT INTO deployment (serial_number, csi_id, service, request_id, release_branch, release, status, created_by, team, date_requested, date_modified, is_config, config_request_id, rlm_id_uat1, rlm_id_uat2, rlm_id_uat3, rlm_id_perf1, rlm_id_perf2, rlm_id_prod1, rlm_id_prod2, production_ready) VALUES 

-- Entries 1-10 (Earlier dates)
('MSDR0000001', '172033', 'auth-login-service', 'jenkins-REQ001', 'main', '2025-01', 'Completed', 'dev1', 'Phoenix', '2024-01-15 09:30:00', '2024-01-20 14:45:00', false, NULL, 'RLM-UAT1-001', 'RLM-UAT2-001', 'RLM-UAT3-001', 'RLM-PERF1-001', 'RLM-PERF2-001', NULL, NULL, false),

('MSDR0000002', '172223', 'pay-gateway-service', 'jenkins-REQ002', 'feature/payment-gateway', '2025-01', 'In Progress', 'dev2', 'Avengers', '2024-01-22 11:15:00', '2024-01-25 16:20:00', true, 'jenkins-CONF002', 'RLM-UAT1-002', 'RLM-UAT2-002', NULL, 'RLM-PERF1-002', NULL, NULL, NULL, false),

('MSDR0000003', '169608', 'user-profile-service', 'jenkins-REQ003', 'main', '2025-02', 'Pending', 'dev3', 'Transformers', '2024-02-01 08:45:00', '2024-02-03 12:30:00', false, NULL, 'RLM-UAT1-003', NULL, NULL, 'RLM-PERF1-003', 'RLM-PERF2-003', NULL, NULL, false),

('MSDR0000004', '172033', 'notif-email-service', 'jenkins-REQ004', 'hotfix/notification', '2025-02', 'Open', 'dev1', 'X-Men', '2024-02-10 14:20:00', '2024-02-10 14:20:00', true, 'jenkins-CONF004', 'RLM-UAT1-004', 'RLM-UAT2-004', NULL, NULL, NULL, NULL, NULL, false),

('MSDR0000005', '172223', 'data-sync-service', 'jenkins-REQ005', 'main', '2025-03', 'Completed', 'dev2', 'Justice League', '2024-02-18 10:30:00', '2024-02-25 16:45:00', false, NULL, 'RLM-UAT1-005', 'RLM-UAT2-005', 'RLM-UAT3-005', 'RLM-PERF1-005', 'RLM-PERF2-005', NULL, NULL, false),

('MSDR0000006', '169608', 'api-gateway-service', 'jenkins-REQ006', 'feature/api-v2', '2025-03', 'In Progress', 'dev3', 'Guardians', '2024-03-01 09:15:00', '2024-03-05 11:30:00', false, NULL, 'RLM-UAT1-006', 'RLM-UAT2-006', NULL, 'RLM-PERF1-006', NULL, NULL, NULL, false),

('MSDR0000007', '172033', 'sec-encrypt-service', 'jenkins-REQ007', 'main', '2025-04', 'Pending', 'dev1', 'Fantastic Four', '2024-03-12 13:45:00', '2024-03-15 09:20:00', true, 'jenkins-CONF007', 'RLM-UAT1-007', NULL, NULL, NULL, NULL, NULL, NULL, false),

('MSDR0000008', '172223', 'file-upload-service', 'jenkins-REQ008', 'release/v2.1', '2025-04', 'Open', 'dev2', 'Teen Titans', '2024-03-20 15:10:00', '2024-03-20 15:10:00', false, NULL, 'RLM-UAT1-008', 'RLM-UAT2-008', NULL, 'RLM-PERF1-008', 'RLM-PERF2-008', NULL, NULL, false),

('MSDR0000009', '169608', 'report-generate-service', 'jenkins-REQ009', 'main', '2025-05', 'Completed', 'dev3', 'Power Rangers', '2024-03-28 08:30:00', '2024-04-02 14:15:00', false, NULL, 'RLM-UAT1-009', 'RLM-UAT2-009', 'RLM-UAT3-009', 'RLM-PERF1-009', 'RLM-PERF2-009', NULL, NULL, false),

('MSDR0000010', '172033', 'flow-orchestrate-service', 'jenkins-REQ010', 'feature/workflow', '2025-05', 'In Progress', 'dev1', 'Ninja Turtles', '2024-04-05 12:00:00', '2024-04-08 16:30:00', true, 'jenkins-CONF010', 'RLM-UAT1-010', 'RLM-UAT2-010', NULL, NULL, NULL, NULL, NULL, false),

-- Entries 11-20 (Mid dates)
('MSDR0000011', '172223', 'int-connector-service', 'jenkins-REQ011', 'main', '2025-06', 'Pending', 'dev2', 'Avengers', '2024-04-12 10:45:00', '2024-04-15 13:20:00', false, NULL, 'RLM-UAT1-011', NULL, NULL, 'RLM-PERF1-011', NULL, NULL, NULL, false),

('MSDR0000012', '169608', 'config-manage-service', 'jenkins-REQ012', 'hotfix/config', '2025-06', 'Open', 'dev3', 'X-Men', '2024-04-20 14:15:00', '2024-04-20 14:15:00', true, 'jenkins-CONF012', 'RLM-UAT1-012', 'RLM-UAT2-012', NULL, 'RLM-PERF1-012', 'RLM-PERF2-012', NULL, NULL, false),

('MSDR0000013', '172033', 'mon-health-service', 'jenkins-REQ013', 'main', '2025-07', 'Completed', 'dev1', 'Justice League', '2024-04-25 09:20:00', '2024-05-01 15:40:00', false, NULL, 'RLM-UAT1-013', 'RLM-UAT2-013', 'RLM-UAT3-013', 'RLM-PERF1-013', 'RLM-PERF2-013', NULL, NULL, false),

('MSDR0000014', '172223', 'cache-redis-service', 'jenkins-REQ014', 'feature/caching', '2025-07', 'In Progress', 'dev2', 'Guardians', '2024-05-03 11:30:00', '2024-05-06 14:45:00', false, NULL, 'RLM-UAT1-014', 'RLM-UAT2-014', NULL, 'RLM-PERF1-014', NULL, NULL, NULL, false),

('MSDR0000015', '169608', 'search-index-service', 'jenkins-REQ015', 'main', '2025-08', 'Pending', 'dev3', 'Fantastic Four', '2024-05-10 13:15:00', '2024-05-12 10:25:00', true, 'jenkins-CONF015', 'RLM-UAT1-015', NULL, NULL, NULL, NULL, NULL, NULL, false),

('MSDR0000016', '172033', 'comm-chat-service', 'jenkins-REQ016', 'release/v3.0', '2025-08', 'Open', 'dev1', 'Teen Titans', '2024-05-18 15:40:00', '2024-05-18 15:40:00', false, NULL, 'RLM-UAT1-016', 'RLM-UAT2-016', NULL, 'RLM-PERF1-016', 'RLM-PERF2-016', NULL, NULL, false),

('MSDR0000017', '172223', 'content-create-service', 'jenkins-REQ017', 'main', '2025-09', 'Completed', 'dev2', 'Power Rangers', '2024-05-22 08:50:00', '2024-05-28 12:10:00', false, NULL, 'RLM-UAT1-017', 'RLM-UAT2-017', 'RLM-UAT3-017', 'RLM-PERF1-017', 'RLM-PERF2-017', NULL, NULL, false),

('MSDR0000018', '169608', 'loc-geocode-service', 'jenkins-REQ018', 'feature/location', '2025-09', 'In Progress', 'dev3', 'Ninja Turtles', '2024-06-01 10:20:00', '2024-06-03 16:35:00', true, 'jenkins-CONF018', 'RLM-UAT1-018', 'RLM-UAT2-018', NULL, NULL, NULL, NULL, NULL, false),

('MSDR0000019', '172033', 'ml-train-service', 'jenkins-REQ019', 'main', '2025-10', 'Pending', 'dev1', 'Avengers', '2024-06-08 12:45:00', '2024-06-10 09:15:00', false, NULL, 'RLM-UAT1-019', NULL, NULL, 'RLM-PERF1-019', 'RLM-PERF2-019', NULL, NULL, false),

('MSDR0000020', '172223', 'pay-processor-service', 'jenkins-REQ020', 'hotfix/payment', '2025-10', 'Open', 'dev2', 'X-Men', '2024-06-15 14:30:00', '2024-06-15 14:30:00', false, NULL, 'RLM-UAT1-020', 'RLM-UAT2-020', NULL, 'RLM-PERF1-020', 'RLM-PERF2-020', NULL, NULL, false),

-- Entries 21-30 (Latest dates) - Updated with new release format
('MSDR0000021', '172033', 'auth-sso-service', 'jenkins-REQ021', 'feature/sso-integration', '2025-11', 'In Progress', 'dev2', 'Avengers', '2024-06-28 09:45:00', '2024-06-30 16:15:00', false, NULL, 'RLM-UAT1-021', 'RLM-UAT2-021', NULL, 'RLM-PERF1-021', 'RLM-PERF2-021', NULL, NULL, false),

('MSDR0000022', '169608', 'file-storage-service', 'jenkins-REQ022', 'main', '2025-11', 'Pending', 'dev2', 'Transformers', '2024-07-01 14:55:00', '2024-07-03 11:40:00', true, 'jenkins-CONF022', 'RLM-UAT1-022', NULL, NULL, 'RLM-PERF1-022', NULL, NULL, NULL, false),

('MSDR0000023', '172223', 'search-index-service', 'jenkins-REQ023', 'release/search-v2', '2025-12', 'Completed', 'dev1', 'Crusaders', '2024-07-05 10:35:00', '2024-07-07 15:20:00', false, NULL, 'RLM-UAT1-023', 'RLM-UAT2-023', 'RLM-UAT3-023', 'RLM-PERF1-023', 'RLM-PERF2-023', NULL, NULL, false),

('MSDR0000024', '172033', 'pay-gateway-service', 'jenkins-REQ024', 'main', '2025-12', 'Open', 'dev1', 'Phoenix', '2024-07-08 12:10:00', '2024-07-08 12:10:00', true, 'jenkins-CONF024', 'RLM-UAT1-024', NULL, NULL, NULL, NULL, NULL, NULL, false),

('MSDR0000025', '169608', 'user-profile-service', 'jenkins-REQ025', 'feature/profile-v3', '2026-01', 'In Progress', 'dev3', 'Hyper Care', '2024-07-10 16:25:00', '2024-07-12 14:50:00', false, NULL, 'RLM-UAT1-025', 'RLM-UAT2-025', NULL, 'RLM-PERF1-025', 'RLM-PERF2-025', NULL, NULL, false),

('MSDR0000026', '172223', 'data-analytics-service', 'jenkins-REQ026', 'main', '2026-01', 'Completed', 'dev3', 'CRUD', '2024-07-14 08:15:00', '2024-07-15 17:30:00', false, NULL, 'RLM-UAT1-026', 'RLM-UAT2-026', 'RLM-UAT3-026', 'RLM-PERF1-026', 'RLM-PERF2-026', NULL, NULL, false),

('MSDR0000027', '172033', 'notif-push-service', 'jenkins-REQ027', 'hotfix/notifications', '2026-02', 'Pending', 'dev2', 'Avengers', '2024-07-15 11:40:00', '2024-07-16 13:20:00', true, 'jenkins-CONF027', 'RLM-UAT1-027', NULL, NULL, NULL, NULL, NULL, NULL, false),

('MSDR0000028', '169608', 'cache-redis-service', 'jenkins-REQ028', 'main', '2026-02', 'Completed', 'dev1', 'Transformers', '2024-07-16 09:20:00', '2024-07-17 16:45:00', false, NULL, 'RLM-UAT1-028', 'RLM-UAT2-028', 'RLM-UAT3-028', 'RLM-PERF1-028', 'RLM-PERF2-028', NULL, NULL, false),

('MSDR0000029', '172223', 'api-security-service', 'jenkins-REQ029', 'feature/security-v2', '2026-03', 'In Progress', 'dev2', 'Crusaders', '2024-07-17 14:30:00', '2024-07-18 10:15:00', true, 'jenkins-CONF029', 'RLM-UAT1-029', 'RLM-UAT2-029', NULL, 'RLM-PERF1-029', NULL, NULL, NULL, false),

('MSDR0000030', '172033', 'ml-pipeline-service', 'jenkins-REQ030', 'main', '2026-03', 'Open', 'dev1', 'Phoenix', '2024-07-18 15:00:00', '2024-07-18 15:00:00', false, NULL, 'RLM-UAT1-030', 'RLM-UAT2-030', 'RLM-UAT3-030', 'RLM-PERF1-030', 'RLM-PERF2-030', NULL, NULL, false);
