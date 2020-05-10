DROP TABLE IF EXISTS startline_users_old;
RENAME TABLE startline_users TO startline_users_old;
#DROP TABLE IF EXISTS startline_users;
CREATE TABLE startline_users LIKE wp_axsw_users;
INSERT startline_users SELECT * from wp_axsw_users;
ALTER TABLE startline_users DROP COLUMN user_pass;
ALTER TABLE startline_users DROP COLUMN user_url;
ALTER TABLE startline_users DROP COLUMN user_activation_key;
ALTER TABLE startline_users DROP COLUMN user_status;
ALTER TABLE startline_users DROP COLUMN user_nicename;
ALTER TABLE startline_users DROP COLUMN verified;
DELETE FROM startline_users WHERE user_login = 'admin';
#firstname, lastname
ALTER TABLE startline_users ADD COLUMN firstname varchar(256);
ALTER TABLE startline_users ADD COLUMN lastname varchar(256);
UPDATE startline_users INNER JOIN wp_axsw_usermeta ON startline_users.ID = wp_axsw_usermeta.user_id SET startline_users.firstname = wp_axsw_usermeta.meta_value WHERE wp_axsw_usermeta.meta_key="first_name";
UPDATE startline_users INNER JOIN wp_axsw_usermeta ON startline_users.ID = wp_axsw_usermeta.user_id SET startline_users.lastname = wp_axsw_usermeta.meta_value WHERE wp_axsw_usermeta.meta_key="last_name";
#membership level
ALTER TABLE startline_users ADD COLUMN membership_level varchar(20);
UPDATE startline_users INNER JOIN wp_axsw_usermeta ON startline_users.ID = wp_axsw_usermeta.user_id SET startline_users.membership_level = wp_axsw_usermeta.meta_value WHERE wp_axsw_usermeta.meta_key="pie_dropdown_7";
#unverified
ALTER TABLE startline_users ADD COLUMN verified varchar(20);
UPDATE startline_users INNER JOIN wp_axsw_usermeta ON startline_users.ID = wp_axsw_usermeta.user_id SET startline_users.verified = wp_axsw_usermeta.meta_value WHERE wp_axsw_usermeta.meta_key="active";
#nationality
ALTER TABLE startline_users ADD COLUMN nationality varchar(20);
UPDATE startline_users SET nationality = "";
UPDATE startline_users INNER JOIN wp_axsw_usermeta ON startline_users.ID = wp_axsw_usermeta.user_id SET startline_users.nationality = wp_axsw_usermeta.meta_value WHERE wp_axsw_usermeta.meta_key="pie_dropdown_24";
#birthday
ALTER TABLE startline_users ADD COLUMN birthday varchar(100);
UPDATE startline_users SET birthday = "";
UPDATE startline_users INNER JOIN wp_axsw_usermeta ON startline_users.ID = wp_axsw_usermeta.user_id SET startline_users.birthday = wp_axsw_usermeta.meta_value WHERE wp_axsw_usermeta.meta_key="pie_date_3";
#volunteering
ALTER TABLE startline_users ADD COLUMN volunteering varchar(1000);
UPDATE startline_users SET volunteering = "";
UPDATE startline_users INNER JOIN wp_axsw_usermeta ON startline_users.ID = wp_axsw_usermeta.user_id SET startline_users.volunteering = wp_axsw_usermeta.meta_value WHERE wp_axsw_usermeta.meta_key="pie_multiselect_25";
#membership_level_old
ALTER TABLE startline_users ADD COLUMN membership_level_old varchar(20);
UPDATE startline_users INNER JOIN startline_users_old ON startline_users.ID = startline_users_old.ID SET startline_users.membership_level_old = startline_users_old.membership_level;
#changed
ALTER TABLE startline_users ADD COLUMN has_changed varchar(20);
UPDATE startline_users SET has_changed = "no";
UPDATE startline_users SET has_changed = "yes" where membership_level != membership_level_old;
