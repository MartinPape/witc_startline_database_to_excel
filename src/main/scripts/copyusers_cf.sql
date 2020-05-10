DROP TABLE IF EXISTS startline_users_cf_old;
RENAME TABLE startline_users_cf TO startline_users_cf_old;
CREATE TABLE startline_users_cf LIKE wp_axsw_cf_form_entries;
INSERT startline_users_cf SELECT * from wp_axsw_cf_form_entries WHERE form_id="CF590590e59c7be";
DELETE FROM startline_users_cf WHERE NOT status="active";
ALTER TABLE startline_users_cf DROP COLUMN form_id;
#ALTER TABLE startline_users_cf DROP COLUMN active;
#ALTER TABLE startline_users_cf DROP COLUMN user_id;
#email
ALTER TABLE startline_users_cf ADD COLUMN email varchar(1000);
UPDATE startline_users_cf INNER JOIN wp_axsw_cf_form_entry_values ON startline_users_cf.id = wp_axsw_cf_form_entry_values.entry_id SET startline_users_cf.email = wp_axsw_cf_form_entry_values.value WHERE wp_axsw_cf_form_entry_values.slug="email";
#firstname, lastname
ALTER TABLE startline_users_cf ADD COLUMN firstname varchar(256);
ALTER TABLE startline_users_cf ADD COLUMN lastname varchar(256);
UPDATE startline_users_cf INNER JOIN wp_axsw_cf_form_entry_values ON startline_users_cf.id = wp_axsw_cf_form_entry_values.entry_id SET startline_users_cf.firstname = wp_axsw_cf_form_entry_values.value WHERE wp_axsw_cf_form_entry_values.slug="first_name";
UPDATE startline_users_cf INNER JOIN wp_axsw_cf_form_entry_values ON startline_users_cf.id = wp_axsw_cf_form_entry_values.entry_id SET startline_users_cf.lastname = wp_axsw_cf_form_entry_values.value WHERE wp_axsw_cf_form_entry_values.slug="last_name";
#membership level
ALTER TABLE startline_users_cf ADD COLUMN membership_level varchar(20);
UPDATE startline_users_cf INNER JOIN wp_axsw_cf_form_entry_values ON startline_users_cf.id = wp_axsw_cf_form_entry_values.entry_id SET startline_users_cf.membership_level = wp_axsw_cf_form_entry_values.value WHERE wp_axsw_cf_form_entry_values.slug="membership_level";
#nationality
ALTER TABLE startline_users_cf ADD COLUMN nationality varchar(20);
UPDATE startline_users_cf SET nationality = "";
UPDATE startline_users_cf INNER JOIN wp_axsw_cf_form_entry_values ON startline_users_cf.id = wp_axsw_cf_form_entry_values.entry_id SET startline_users_cf.nationality = wp_axsw_cf_form_entry_values.value WHERE wp_axsw_cf_form_entry_values.slug="nationality";
#birthday
ALTER TABLE startline_users_cf ADD COLUMN birthday varchar(100);
UPDATE startline_users_cf SET birthday = "";
UPDATE startline_users_cf INNER JOIN wp_axsw_cf_form_entry_values ON startline_users_cf.id = wp_axsw_cf_form_entry_values.entry_id SET startline_users_cf.birthday = wp_axsw_cf_form_entry_values.value WHERE wp_axsw_cf_form_entry_values.slug="date_of_birth";
#volunteering
ALTER TABLE startline_users_cf ADD COLUMN volunteering varchar(1000);
UPDATE startline_users_cf SET volunteering = "";
UPDATE startline_users_cf SET startline_users_cf.volunteering = 
	(SELECT GROUP_CONCAT(value SEPARATOR ', ') FROM `wp_axsw_cf_form_entry_values` WHERE entry_id = startline_users_cf.id AND slug LIKE "volunteering.%");
#drop unused columns
ALTER TABLE startline_users_cf DROP COLUMN user_id;
