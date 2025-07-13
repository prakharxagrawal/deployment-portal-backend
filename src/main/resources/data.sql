INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin');
INSERT INTO users (username, password, role) VALUES ('user', 'user123', 'user');
INSERT INTO releases (name) VALUES ('Release 1.0');
INSERT INTO releases (name) VALUES ('Release 2.0');
INSERT INTO deployment (serial_number, csi_id, release_branch, release, status, created_by)
VALUES ('MSDR0000001', '172033', 'main', 'Release 1.0', 'Open', 'admin');

-- Insert services for deployment id 1
INSERT INTO deployment_services (deployment_id, services) VALUES (1, 'Service1');
INSERT INTO deployment_services (deployment_id, services) VALUES (1, 'Service2');

-- Insert request_ids for deployment id 1
INSERT INTO deployment_request_ids (deployment_id, request_ids) VALUES (1, 'REQ001');
INSERT INTO deployment_request_ids (deployment_id, request_ids) VALUES (1, 'REQ002');

-- Insert environments for deployment id 1
INSERT INTO deployment_environments (deployment_id, environments) VALUES (1, 'UAT1');
INSERT INTO deployment_environments (deployment_id, environments) VALUES (1, 'DEV1');