INSERT INTO contract (contract_typology,contract_id,account_id,contract_amendment,contract_date,contract_state,contract_title,contract_type,created_at,end_date,starting_date,status,structure_contract,
					  updated_at,fse,client_id,contract_status,duration_unit,global_amount,mission_duration,monthly_amount,validator_id,hourly_rate,rupture_date,work_time,commission,comment) VALUES 
					  ('cape','dce6d0df-8d4f-4612-890a-79503dd67f8c','f99337eb-ff45-487a-a20d-f186ba71e99c', null, NOW(), 'NOT_STARTED', 'CAPE Monsieur du Test', 'CONTRACT', NOW() - INTERVAL '30 days', NOW() + INTERVAL '2 years',NOW() + INTERVAL '30 days', true, 'AUXIME',
					  null, true, null, null, null,null,null,null,null,null,null,null,null,null);

INSERT INTO contract (contract_typology,contract_id,account_id,contract_amendment,contract_date,contract_state,contract_title,contract_type,created_at,end_date,starting_date,status,structure_contract,
					  updated_at,fse,client_id,contract_status,duration_unit,global_amount,mission_duration,monthly_amount,validator_id,hourly_rate,rupture_date,work_time,commission,comment) VALUES 
					  ('commercial_contract','f4772f45-6acb-45d7-9220-7cd6c4a8ac04','f99337eb-ff45-487a-a20d-f186ba71e99c', null, NOW(), 'CANCELED', 'Contrat Test-Client du coin', 'CONTRACT', NOW() - INTERVAL '30 days', NOW() + INTERVAL '2 years',NOW() - INTERVAL '15 days', true, 'AUXIME',
					  NOW() - INTERVAL '15 days', false, '5b1a9d99-d0e7-49ca-a99e-82c867656fdc', 'VALIDATED', 'MONTHS',12000,12,1000,'d2d12d0a-75ce-4051-9e22-95a00ede8138',null,null,null,null,null);
					  
INSERT INTO contract (contract_typology,contract_id,account_id,contract_amendment,contract_date,contract_state,contract_title,contract_type,created_at,end_date,starting_date,status,structure_contract,
					  updated_at,fse,client_id,contract_status,duration_unit,global_amount,mission_duration,monthly_amount,validator_id,rupture_date,hourly_rate,work_time,commission,comment) VALUES 
					  ('permanent_contract','de72bff9-23aa-428d-91e9-c695c823ec7f','f99337eb-ff45-487a-a20d-f186ba71e99c', null, NOW(), 'ACTIVE', 'CDI Monsieur IMBERT', 'CONTRACT', NOW() - INTERVAL '30 days', NOW() + INTERVAL '2 years',NOW() - INTERVAL '45 days', true, 'COELIS',
					  NOW() - INTERVAL '20 days', false, null, null, null,null,null,null,null,NOW() + INTERVAL '30 days',20,151.67,10,null);
					  
INSERT INTO contract (contract_typology,contract_id,account_id,contract_amendment,contract_date,contract_state,contract_title,contract_type,created_at,end_date,starting_date,status,structure_contract,
					updated_at,fse,client_id,contract_status,duration_unit,global_amount,mission_duration,monthly_amount,validator_id,rupture_date,hourly_rate,work_time,commission,comment) VALUES 
					('temporary_contract','46492afb-7f38-454a-90d8-cf284610e158','f99337eb-ff45-487a-a20d-f186ba71e99c', null, NOW(), 'ACTIVE', 'CDD Monsieur CHARLES', 'CONTRACT', NOW() - INTERVAL '30 days', NOW() + INTERVAL '2 years',NOW() - INTERVAL '60 days', true, 'COELIS',
					NOW() - INTERVAL '20 days', false, null, null, null,null,null,null,null,NOW() + INTERVAL '30 days',20,151.67,10,null);
					
INSERT INTO contract (contract_typology,contract_id,account_id,contract_amendment,contract_date,contract_state,contract_title,contract_type,created_at,end_date,starting_date,status,structure_contract,
					  updated_at,fse,client_id,contract_status,duration_unit,global_amount,mission_duration,monthly_amount,validator_id,hourly_rate,rupture_date,work_time,commission,comment) VALUES 
					  ('portage_convention','3b2d1af5-faf5-4a0f-8e3d-4dcd70ab4ae1','f99337eb-ff45-487a-a20d-f186ba71e99c', null, NOW(), 'INACTIVE', 'Convention de Portage Monsieur CHARLES', 'CONTRACT', NOW()- INTERVAL '30 days', NOW()+ INTERVAL '2 years',NOW() + INTERVAL '60 days', true, 'COELIS',
					  null, false, null, null, null,null,null,null,null,null,null,null,10,null);
INSERT INTO contract (contract_typology,contract_id,account_id,contract_amendment,contract_date,contract_state,contract_title,contract_type,created_at,end_date,starting_date,status,structure_contract,
					  updated_at,fse,client_id,contract_status,duration_unit,global_amount,mission_duration,monthly_amount,validator_id,hourly_rate,rupture_date,work_time,commission,comment) VALUES 
					  ('cape','696271ec-19ca-416b-acb2-cbce1e1bfefc','f99337eb-ff45-487a-a20d-f186ba71e99c', 'dce6d0df-8d4f-4612-890a-79503dd67f8c', NOW(), 'NOT_STARTED', 'Renouvellement CAPE Monsieur du Test', 'AMENDMENT', NOW() - INTERVAL '30 days', NOW() + INTERVAL '2 years',NOW()+ INTERVAL '95 days', true, 'AUXIME',
					  null, true, null, null, null,null,null,null,null,null,null,null,null,null);

INSERT INTO contract (contract_typology,contract_id,account_id,contract_amendment,contract_date,contract_state,contract_title,contract_type,created_at,end_date,starting_date,status,structure_contract,
					  updated_at,fse,client_id,contract_status,duration_unit,global_amount,mission_duration,monthly_amount,validator_id,hourly_rate,rupture_date,work_time,commission,comment) VALUES 
					  ('commercial_contract','7cfb54f7-16c0-4b8b-b751-21496a0bb6c3','f99337eb-ff45-487a-a20d-f186ba71e99c', 'f4772f45-6acb-45d7-9220-7cd6c4a8ac04', NOW(), 'CANCELED', 'Contrat Test-Client du coin', 'AMENDMENT', NOW() - INTERVAL '30 days', NOW() + INTERVAL '2 years',NOW() - INTERVAL '15 days', true, 'AUXIME',
					  NOW() - INTERVAL '15 days', false, '5b1a9d99-d0e7-49ca-a99e-82c867656fdc', 'VALIDATED', 'MONTHS',12000,12,1000,'d2d12d0a-75ce-4051-9e22-95a00ede8138',null,null,null,null,null);

INSERT INTO contract (contract_typology,contract_id,account_id,contract_amendment,contract_date,contract_state,contract_title,contract_type,created_at,end_date,starting_date,status,structure_contract,
					  updated_at,fse,client_id,contract_status,duration_unit,global_amount,mission_duration,monthly_amount,validator_id,rupture_date,hourly_rate,work_time,commission,comment) VALUES 
					  ('permanent_contract','0448a987-2e1a-4375-a4ea-8edc9b14d795','f99337eb-ff45-487a-a20d-f186ba71e99c', null, NOW(), 'ACTIVE', 'CDI Monsieur IMBERT', 'AMENDMENT', NOW() - INTERVAL '30 days', NOW() + INTERVAL '2 years',NOW() - INTERVAL '45 days', true, 'COELIS',
					  NOW() - INTERVAL '20 days', false, null, null, null,null,null,null,null,NOW() + INTERVAL '30 days',20,151.67,10,null);

INSERT INTO rates (rate_id,created_at,rate,type_rate,updated_at) VALUES ('4874684b-8f55-4f6b-8f38-e2398d129166', NOW()- INTERVAL '30 days', 9, 'ACTIVITY', null );
INSERT INTO rates (rate_id,created_at,rate,type_rate,updated_at) VALUES ('d901a167-4ae2-42c7-96dd-de4e301af916', NOW()- INTERVAL '30 days', 14, 'QUALIOPI', null );
INSERT INTO rates (rate_id,created_at,rate,type_rate,updated_at) VALUES ('8c0c7ac0-3707-47b5-a825-b06d125c5a04', NOW()- INTERVAL '30 days', 10, 'ACTIVITY', null );
INSERT INTO rates (rate_id,created_at,rate,type_rate,updated_at) VALUES ('7a672266-f0f9-4af2-9eac-435c826e3ccb', NOW()- INTERVAL '30 days', 12, 'QUALIOPI', null );
INSERT INTO contract_rates(cape_contract_id, rates) VALUES ('dce6d0df-8d4f-4612-890a-79503dd67f8c', '4874684b-8f55-4f6b-8f38-e2398d129166'), ('dce6d0df-8d4f-4612-890a-79503dd67f8c', 'd901a167-4ae2-42c7-96dd-de4e301af916');
INSERT INTO contract_rates(cape_contract_id, rates) VALUES ('696271ec-19ca-416b-acb2-cbce1e1bfefc', '8c0c7ac0-3707-47b5-a825-b06d125c5a04'), ('696271ec-19ca-416b-acb2-cbce1e1bfefc', '7a672266-f0f9-4af2-9eac-435c826e3ccb');