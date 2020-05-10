package com.martinpape.startline;

import jxl.Workbook;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySqlToExcel {

    public static Map<String, String> COUNTRY_TO_CODE = new HashMap<String, String>();
    public static String ADD_ON_FIELD_ID = "fld_8919745";

    public PreparedStatement getAddOnStatement;

    static {
        COUNTRY_TO_CODE.put("Afghanistan", "af");
        COUNTRY_TO_CODE.put("Åland Islands", "ax");
        COUNTRY_TO_CODE.put("Albania", "al");
        COUNTRY_TO_CODE.put("Algeria", "dz");
        COUNTRY_TO_CODE.put("American Samoa", "as");
        COUNTRY_TO_CODE.put("Andorra", "ad");
        COUNTRY_TO_CODE.put("Angola", "ao");
        COUNTRY_TO_CODE.put("Anguilla", "ai");
        COUNTRY_TO_CODE.put("Antarctica", "aq");
        COUNTRY_TO_CODE.put("Antigua and Barbuda", "ag");
        COUNTRY_TO_CODE.put("Argentina", "ar");
        COUNTRY_TO_CODE.put("Armenia", "am");
        COUNTRY_TO_CODE.put("Aruba", "aw");
        COUNTRY_TO_CODE.put("Australia", "au");
        COUNTRY_TO_CODE.put("Austria", "at");
        COUNTRY_TO_CODE.put("Azerbaijan", "az");
        COUNTRY_TO_CODE.put("Bahamas", "bs");
        COUNTRY_TO_CODE.put("Bahrain", "bh");
        COUNTRY_TO_CODE.put("Bangladesh", "bd");
        COUNTRY_TO_CODE.put("Barbados", "bb");
        COUNTRY_TO_CODE.put("Belarus", "by");
        COUNTRY_TO_CODE.put("Belgium", "be");
        COUNTRY_TO_CODE.put("Belize", "bz");
        COUNTRY_TO_CODE.put("Benin", "bj");
        COUNTRY_TO_CODE.put("Bermuda", "bm");
        COUNTRY_TO_CODE.put("Bhutan", "bt");
        COUNTRY_TO_CODE.put("Bolivia, Plurinational State of", "bo");
        COUNTRY_TO_CODE.put("Bonaire, Sint Eustatius and Saba", "bq");
        COUNTRY_TO_CODE.put("Bosnia and Herzegovina", "ba");
        COUNTRY_TO_CODE.put("Botswana", "bw");
        COUNTRY_TO_CODE.put("Bouvet Island", "bv");
        COUNTRY_TO_CODE.put("Brazil", "br");
        COUNTRY_TO_CODE.put("British Indian Ocean Territory", "io");
        COUNTRY_TO_CODE.put("Brunei Darussalam", "bn");
        COUNTRY_TO_CODE.put("Bulgaria", "bg");
        COUNTRY_TO_CODE.put("Burkina Faso", "bf");
        COUNTRY_TO_CODE.put("Burundi", "bi");
        COUNTRY_TO_CODE.put("Cambodia", "kh");
        COUNTRY_TO_CODE.put("Cameroon", "cm");
        COUNTRY_TO_CODE.put("Canada", "ca");
        COUNTRY_TO_CODE.put("Cape Verde", "cv");
        COUNTRY_TO_CODE.put("Cayman Islands", "ky");
        COUNTRY_TO_CODE.put("Central African Republic", "cf");
        COUNTRY_TO_CODE.put("Chad", "td");
        COUNTRY_TO_CODE.put("Chile", "cl");
        COUNTRY_TO_CODE.put("China", "cn");
        COUNTRY_TO_CODE.put("Christmas Island", "cx");
        COUNTRY_TO_CODE.put("Cocos (Keeling) Islands", "cc");
        COUNTRY_TO_CODE.put("Colombia", "co");
        COUNTRY_TO_CODE.put("Comoros", "km");
        COUNTRY_TO_CODE.put("Congo", "cg");
        COUNTRY_TO_CODE.put("Congo, the Democratic Republic of the", "cd");
        COUNTRY_TO_CODE.put("Cook Islands", "ck");
        COUNTRY_TO_CODE.put("Costa Rica", "cr");
        COUNTRY_TO_CODE.put("Côte d'Ivoire", "ci");
        COUNTRY_TO_CODE.put("Croatia", "hr");
        COUNTRY_TO_CODE.put("Cuba", "cu");
        COUNTRY_TO_CODE.put("Curaçao", "cw");
        COUNTRY_TO_CODE.put("Cyprus", "cy");
        COUNTRY_TO_CODE.put("Czech Republic", "cz");
        COUNTRY_TO_CODE.put("Denmark", "dk");
        COUNTRY_TO_CODE.put("Djibouti", "dj");
        COUNTRY_TO_CODE.put("Dominica", "dm");
        COUNTRY_TO_CODE.put("Dominican Republic", "do");
        COUNTRY_TO_CODE.put("Ecuador", "ec");
        COUNTRY_TO_CODE.put("Egypt", "eg");
        COUNTRY_TO_CODE.put("El Salvador", "sv");
        COUNTRY_TO_CODE.put("Equatorial Guinea", "gq");
        COUNTRY_TO_CODE.put("Eritrea", "er");
        COUNTRY_TO_CODE.put("Estonia", "ee");
        COUNTRY_TO_CODE.put("Ethiopia", "et");
        COUNTRY_TO_CODE.put("Falkland Islands (Malvinas)", "fk");
        COUNTRY_TO_CODE.put("Faroe Islands", "fo");
        COUNTRY_TO_CODE.put("Fiji", "fj");
        COUNTRY_TO_CODE.put("Finland", "fi");
        COUNTRY_TO_CODE.put("France", "fr");
        COUNTRY_TO_CODE.put("French Guiana", "gf");
        COUNTRY_TO_CODE.put("French Polynesia", "pf");
        COUNTRY_TO_CODE.put("French Southern Territories", "tf");
        COUNTRY_TO_CODE.put("Gabon", "ga");
        COUNTRY_TO_CODE.put("Gambia", "gm");
        COUNTRY_TO_CODE.put("Georgia", "ge");
        COUNTRY_TO_CODE.put("Germany", "de");
        COUNTRY_TO_CODE.put("Ghana", "gh");
        COUNTRY_TO_CODE.put("Gibraltar", "gi");
        COUNTRY_TO_CODE.put("Greece", "gr");
        COUNTRY_TO_CODE.put("Greenland", "gl");
        COUNTRY_TO_CODE.put("Grenada", "gd");
        COUNTRY_TO_CODE.put("Guadeloupe", "gp");
        COUNTRY_TO_CODE.put("Guam", "gu");
        COUNTRY_TO_CODE.put("Guatemala", "gt");
        COUNTRY_TO_CODE.put("Guernsey", "gg");
        COUNTRY_TO_CODE.put("Guinea", "gn");
        COUNTRY_TO_CODE.put("Guinea-Bissau", "gw");
        COUNTRY_TO_CODE.put("Guyana", "gy");
        COUNTRY_TO_CODE.put("Haiti", "ht");
        COUNTRY_TO_CODE.put("Heard Island and McDonald Islands", "hm");
        COUNTRY_TO_CODE.put("Holy See (Vatican City State)", "va");
        COUNTRY_TO_CODE.put("Honduras", "hn");
        COUNTRY_TO_CODE.put("Hong Kong", "hk");
        COUNTRY_TO_CODE.put("Hungary", "hu");
        COUNTRY_TO_CODE.put("Iceland", "is");
        COUNTRY_TO_CODE.put("India", "in");
        COUNTRY_TO_CODE.put("Indonesia", "id");
        COUNTRY_TO_CODE.put("Iran, Islamic Republic of", "ir");
        COUNTRY_TO_CODE.put("Iraq", "iq");
        COUNTRY_TO_CODE.put("Ireland", "ie");
        COUNTRY_TO_CODE.put("Isle of Man", "im");
        COUNTRY_TO_CODE.put("Israel", "il");
        COUNTRY_TO_CODE.put("Italy", "it");
        COUNTRY_TO_CODE.put("Jamaica", "jm");
        COUNTRY_TO_CODE.put("Japan", "jp");
        COUNTRY_TO_CODE.put("Jersey", "je");
        COUNTRY_TO_CODE.put("Jordan", "jo");
        COUNTRY_TO_CODE.put("Kazakhstan", "kz");
        COUNTRY_TO_CODE.put("Kenya", "ke");
        COUNTRY_TO_CODE.put("Kiribati", "ki");
        COUNTRY_TO_CODE.put("Korea, Democratic People's Republic of", "kp");
        COUNTRY_TO_CODE.put("Korea, Republic of", "kr");
        COUNTRY_TO_CODE.put("Kuwait", "kw");
        COUNTRY_TO_CODE.put("Kyrgyzstan", "kg");
        COUNTRY_TO_CODE.put("Lao People's Democratic Republic", "la");
        COUNTRY_TO_CODE.put("Latvia", "lv");
        COUNTRY_TO_CODE.put("Lebanon", "lb");
        COUNTRY_TO_CODE.put("Lesotho", "ls");
        COUNTRY_TO_CODE.put("Liberia", "lr");
        COUNTRY_TO_CODE.put("Libya", "ly");
        COUNTRY_TO_CODE.put("Liechtenstein", "li");
        COUNTRY_TO_CODE.put("Lithuania", "lt");
        COUNTRY_TO_CODE.put("Luxembourg", "lu");
        COUNTRY_TO_CODE.put("Macao", "mo");
        COUNTRY_TO_CODE.put("Macedonia, the former Yugoslav Republic of", "mk");
        COUNTRY_TO_CODE.put("Madagascar", "mg");
        COUNTRY_TO_CODE.put("Malawi", "mw");
        COUNTRY_TO_CODE.put("Malaysia", "my");
        COUNTRY_TO_CODE.put("Maldives", "mv");
        COUNTRY_TO_CODE.put("Mali", "ml");
        COUNTRY_TO_CODE.put("Malta", "mt");
        COUNTRY_TO_CODE.put("Marshall Islands", "mh");
        COUNTRY_TO_CODE.put("Martinique", "mq");
        COUNTRY_TO_CODE.put("Mauritania", "mr");
        COUNTRY_TO_CODE.put("Mauritius", "mu");
        COUNTRY_TO_CODE.put("Mayotte", "yt");
        COUNTRY_TO_CODE.put("Mexico", "mx");
        COUNTRY_TO_CODE.put("Micronesia, Federated States of", "fm");
        COUNTRY_TO_CODE.put("Moldova, Republic of", "md");
        COUNTRY_TO_CODE.put("Monaco", "mc");
        COUNTRY_TO_CODE.put("Mongolia", "mn");
        COUNTRY_TO_CODE.put("Montenegro", "me");
        COUNTRY_TO_CODE.put("Montserrat", "ms");
        COUNTRY_TO_CODE.put("Morocco", "ma");
        COUNTRY_TO_CODE.put("Mozambique", "mz");
        COUNTRY_TO_CODE.put("Myanmar", "mm");
        COUNTRY_TO_CODE.put("Namibia", "na");
        COUNTRY_TO_CODE.put("Nauru", "nr");
        COUNTRY_TO_CODE.put("Nepal", "np");
        COUNTRY_TO_CODE.put("Netherlands", "nl");
        COUNTRY_TO_CODE.put("New Caledonia", "nc");
        COUNTRY_TO_CODE.put("New Zealand", "nz");
        COUNTRY_TO_CODE.put("Nicaragua", "ni");
        COUNTRY_TO_CODE.put("Niger", "ne");
        COUNTRY_TO_CODE.put("Nigeria", "ng");
        COUNTRY_TO_CODE.put("Niue", "nu");
        COUNTRY_TO_CODE.put("Norfolk Island", "nf");
        COUNTRY_TO_CODE.put("Northern Mariana Islands", "mp");
        COUNTRY_TO_CODE.put("Norway", "no");
        COUNTRY_TO_CODE.put("Oman", "om");
        COUNTRY_TO_CODE.put("Pakistan", "pk");
        COUNTRY_TO_CODE.put("Palau", "pw");
        COUNTRY_TO_CODE.put("Palestinian Territory, Occupied", "ps");
        COUNTRY_TO_CODE.put("Panama", "pa");
        COUNTRY_TO_CODE.put("Papua New Guinea", "pg");
        COUNTRY_TO_CODE.put("Paraguay", "py");
        COUNTRY_TO_CODE.put("Peru", "pe");
        COUNTRY_TO_CODE.put("Philippines", "ph");
        COUNTRY_TO_CODE.put("Pitcairn", "pn");
        COUNTRY_TO_CODE.put("Poland", "pl");
        COUNTRY_TO_CODE.put("Portugal", "pt");
        COUNTRY_TO_CODE.put("Puerto Rico", "pr");
        COUNTRY_TO_CODE.put("Qatar", "qa");
        COUNTRY_TO_CODE.put("Réunion", "re");
        COUNTRY_TO_CODE.put("Romania", "ro");
        COUNTRY_TO_CODE.put("Russian Federation", "ru");
        COUNTRY_TO_CODE.put("Rwanda", "rw");
        COUNTRY_TO_CODE.put("Saint Barthélemy", "bl");
        COUNTRY_TO_CODE.put("Saint Helena, Ascension and Tristan da Cunha", "sh");
        COUNTRY_TO_CODE.put("Saint Kitts and Nevis", "kn");
        COUNTRY_TO_CODE.put("Saint Lucia", "lc");
        COUNTRY_TO_CODE.put("Saint Martin (French part)", "mf");
        COUNTRY_TO_CODE.put("Saint Pierre and Miquelon", "pm");
        COUNTRY_TO_CODE.put("Saint Vincent and the Grenadines", "vc");
        COUNTRY_TO_CODE.put("Samoa", "ws");
        COUNTRY_TO_CODE.put("San Marino", "sm");
        COUNTRY_TO_CODE.put("Sao Tome and Principe", "st");
        COUNTRY_TO_CODE.put("Saudi Arabia", "sa");
        COUNTRY_TO_CODE.put("Senegal", "sn");
        COUNTRY_TO_CODE.put("Serbia", "rs");
        COUNTRY_TO_CODE.put("Seychelles", "sc");
        COUNTRY_TO_CODE.put("Sierra Leone", "sl");
        COUNTRY_TO_CODE.put("Singapore", "sg");
        COUNTRY_TO_CODE.put("Sint Maarten (Dutch part)", "sx");
        COUNTRY_TO_CODE.put("Slovakia", "sk");
        COUNTRY_TO_CODE.put("Slovenia", "si");
        COUNTRY_TO_CODE.put("Solomon Islands", "sb");
        COUNTRY_TO_CODE.put("Somalia", "so");
        COUNTRY_TO_CODE.put("South Africa", "za");
        COUNTRY_TO_CODE.put("South Georgia and the South Sandwich Islands", "gs");
        COUNTRY_TO_CODE.put("South Sudan", "ss");
        COUNTRY_TO_CODE.put("Spain", "es");
        COUNTRY_TO_CODE.put("Sri Lanka", "lk");
        COUNTRY_TO_CODE.put("Sudan", "sd");
        COUNTRY_TO_CODE.put("Suriname", "sr");
        COUNTRY_TO_CODE.put("Svalbard and Jan Mayen", "sj");
        COUNTRY_TO_CODE.put("Swaziland", "sz");
        COUNTRY_TO_CODE.put("Sweden", "se");
        COUNTRY_TO_CODE.put("Switzerland", "ch");
        COUNTRY_TO_CODE.put("Syrian Arab Republic", "sy");
        COUNTRY_TO_CODE.put("Taiwan, Province of China", "tw");
        COUNTRY_TO_CODE.put("Tajikistan", "tj");
        COUNTRY_TO_CODE.put("Tanzania, United Republic of", "tz");
        COUNTRY_TO_CODE.put("Thailand", "th");
        COUNTRY_TO_CODE.put("Timor-Leste", "tl");
        COUNTRY_TO_CODE.put("Togo", "tg");
        COUNTRY_TO_CODE.put("Tokelau", "tk");
        COUNTRY_TO_CODE.put("Tonga", "to");
        COUNTRY_TO_CODE.put("Trinidad and Tobago", "tt");
        COUNTRY_TO_CODE.put("Tunisia", "tn");
        COUNTRY_TO_CODE.put("Turkey", "tr");
        COUNTRY_TO_CODE.put("Turkmenistan", "tm");
        COUNTRY_TO_CODE.put("Turks and Caicos Islands", "tc");
        COUNTRY_TO_CODE.put("Tuvalu", "tv");
        COUNTRY_TO_CODE.put("Uganda", "ug");
        COUNTRY_TO_CODE.put("Ukraine", "ua");
        COUNTRY_TO_CODE.put("United Arab Emirates", "ae");
        COUNTRY_TO_CODE.put("United Kingdom", "gb");
        COUNTRY_TO_CODE.put("United Kingdom", "uk");
        COUNTRY_TO_CODE.put("United States", "us");
        COUNTRY_TO_CODE.put("United States Minor Outlying Islands", "um");
        COUNTRY_TO_CODE.put("Uruguay", "uy");
        COUNTRY_TO_CODE.put("Uzbekistan", "uz");
        COUNTRY_TO_CODE.put("Vanuatu", "vu");
        COUNTRY_TO_CODE.put("Venezuela, Bolivarian Republic of", "ve");
        COUNTRY_TO_CODE.put("Viet Nam", "vn");
        COUNTRY_TO_CODE.put("Virgin Islands, British", "vg");
        COUNTRY_TO_CODE.put("Virgin Islands, U.S.", "vi");
        COUNTRY_TO_CODE.put("Wallis and Futuna", "wf");
        COUNTRY_TO_CODE.put("Western Sahara", "eh");
        COUNTRY_TO_CODE.put("Yemen", "ye");
        COUNTRY_TO_CODE.put("Zambia", "zm");
        COUNTRY_TO_CODE.put("Zimbabwe", "zw");
    }

    private String getQuotedContent(final String input) {
        if (input == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("(\".*?\")");
        Matcher m = pattern.matcher(input);
        if (m.find()) {
            return m.group(1).replace("\"", "");
        }
        return null;

    }

    private Integer parseDate(final String input, final WritableSheet sheet, Integer column, int row) throws RowsExceededException, WriteException {
        //a:1:{s:4:"date";a:3:{s:2:"mm";s:2:"09";s:2:"dd";s:2:"17";s:2:"yy";s:4:"1957";}}
        String month = null;
        String day = null;
        String year = null;
        StringTokenizer st = new StringTokenizer(input, ";");
        while (st.hasMoreTokens()) {
            String token = getQuotedContent(st.nextToken());
            System.out.println(token);
            if ("mm".equals(token)) {
                month = getQuotedContent(st.nextToken());
            } else if ("dd".equals(token)) {
                day = getQuotedContent(st.nextToken());
            } else if ("yy".equals(token)) {
                year = getQuotedContent(st.nextToken());
            }
        }
        if (month != null && day != null && year != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
            Date date = new Date();
            try {
                date = sdf.parse(year + "/" + month + "/" + day);
            } catch (Exception ex) {
                System.err.println("Date failure: " + ex.getMessage());
            }
            sheet.addCell(new DateTime(column, row, date));
        }
        return column;
    }

    private Integer parseVolunteering(final String input, final WritableSheet sheet, Integer column, int row) throws RowsExceededException, WriteException {
        //a:4:{i:0;s:17:"physical_training";i:1;s:31:"workshop_developmental Training";i:2;s:9:"mentoring";i:3;s:14:"event_planning";}
        List<String> types = new ArrayList<String>();
        if (input != null) {
            StringTokenizer st = new StringTokenizer(input, ";");
            while (st.hasMoreTokens()) {
                String token = getQuotedContent(st.nextToken());
                if (!StringUtils.isEmpty(token)) {
                    types.add(token);
                }
            }
        }
        sheet.addCell(new Label(column, row, StringUtils.join(types, ",")));
        return column;
    }

    private Integer parseAddress(final String input, final WritableSheet sheet, Integer column, int row) throws RowsExceededException, WriteException {
        //a:4:{s:7:"address";s:23:"Marszałkowska 10/16/13";s:4:"city";s:6:"Warsaw";s:3:"zip";s:6:"00-590";s:7:"country";s:6:"Poland";}
        //a:4:{s:7:"address";s:0:"";s:4:"city";s:0:"";s:3:"zip";s:0:"";s:7:"country";s:6:"Poland";}
        String street = null;
        String city = null;
        String zip = null;
        String country = null;
        StringTokenizer st = new StringTokenizer(input, ";");
        while (st.hasMoreTokens()) {
            String token = getQuotedContent(st.nextToken());
            System.out.println(token);
            if ("address".equals(token)) {
                street = getQuotedContent(st.nextToken());
            } else if ("city".equals(token)) {
                city = getQuotedContent(st.nextToken());
            } else if ("zip".equals(token)) {
                zip = getQuotedContent(st.nextToken());
            } else if ("country".equals(token)) {
                country = getQuotedContent(st.nextToken());
            }
        }
        sheet.addCell(new Label(column, row, street));
        column++;
        sheet.addCell(new Label(column, row, zip));
        column++;
        sheet.addCell(new Label(column, row, city));
        column++;
        sheet.addCell(new Label(column, row, country));
        System.out.println("Parsed address to: " + street + " " + zip + " " + city + " " + country);
        return column;
    }

    private Integer parseNationality(final String input, final WritableSheet sheet, Integer column, int row)
            throws RowsExceededException, WriteException {
        // pl, us, ro...
        String country = "";
        if (!StringUtils.isEmpty(input)) {
            for (String key : COUNTRY_TO_CODE.keySet()) {
                String code = COUNTRY_TO_CODE.get(key).toLowerCase();
                if (code.equals(input.toLowerCase())) {
                    country = key;
                    break;
                }
            }
        }
        sheet.addCell(new Label(column, row, country));
        return column;
    }

    public String filterAddOns(String input) {
    	if (StringUtils.isEmpty(input)) {
    		return null;
		} else if (input.startsWith("{") && input.endsWith("}")) {
    		return null;
		} else {
    		return input;
		}
	}

    public String readAddOns(int id, String fieldId) throws SQLException {
    	List<String> addOnes = new ArrayList<String>();
        ResultSet resultSet = null;
        try {
            this.getAddOnStatement.setInt(1, id);
			this.getAddOnStatement.setString(2, fieldId);
            resultSet = this.getAddOnStatement.executeQuery();
            while (resultSet.next()) {
                String value = filterAddOns(resultSet.getString("value"));
                if (value != null) {
					System.out.println("Got value: " + value + " for addon id: " + id);
					addOnes.add(value);
				}
            }
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {

            }
        }
        return StringUtils.join(addOnes, ", ");
    }

    public void readDataBase(String user, String password, File output) {
        Connection databaseConnection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            databaseConnection = DriverManager
                    .getConnection("jdbc:mysql://localhost/startline?user="+user+"&password="+password);
            this.getAddOnStatement = databaseConnection.prepareStatement("SELECT value FROM wp_axsw_cf_form_entry_values WHERE entry_id=? AND field_id=?");
            statement = databaseConnection.createStatement();
            resultSet = statement.executeQuery("select * from startline_users_cf");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            WritableWorkbook workbook = Workbook.createWorkbook(output);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            int column = 0;
            int row = 0;
            for (int i = 1; i < columnCount + 1; i++) {
                String columnName = rsmd.getColumnName(i);
                if ("address".equals(columnName)) {
                    sheet.addCell(new Label(column, row, "Street"));
                    column++;
                    sheet.addCell(new Label(column, row, "ZIP"));
                    column++;
                    sheet.addCell(new Label(column, row, "City"));
                    column++;
                    sheet.addCell(new Label(column, row, "Country"));
                } else {
                    sheet.addCell(new Label(column, row, columnName));
                }
                column++;
            }
			sheet.addCell(new Label(column, row, "AddOns"));
            row++;
            while (resultSet.next()) {
                column = 0;
                int id = -1;
                for (int i = 1; i < columnCount + 1; i++) {
                    int type = rsmd.getColumnType(i);
                    String columnName = rsmd.getColumnName(i);
                    if ("id".equals(columnName)) {
                        id = resultSet.getInt(i);
                    } else if ("address".equals(columnName)) {
                        column = parseAddress(resultSet.getString(i), sheet, column, row);
//					} else if ("birthday".equals(columnName)) {
//						column = parseDate(resultSet.getString(i), sheet, column, row);
                    } else if ("nationality".equals(columnName)) {
                        column = parseNationality(resultSet.getString(i), sheet, column, row);
//					} else if ("volunteering".equals(columnName)) {
//						column = parseVolunteering(resultSet.getString(i), sheet, column, row);
                    } else if (type == 12) {
                        //varchar/string
                        String value = resultSet.getString(i);
                        System.out.println(columnName + " = " + value);
                        Label label = new Label(column, row, value);
                        sheet.addCell(label);
                    } else if (type == 93) {
                        //timestamp
                        Timestamp timestamp = resultSet.getTimestamp(i);
                        Date date = new Date(timestamp.getTime());
                        System.out.println(columnName + " = " + date);
                        DateTime label = new DateTime(column, row, date);
                        sheet.addCell(label);
                    } else if (type == -5) {
                        //bigint
                        int value = resultSet.getInt(i);
                        System.out.println(columnName + " = " + value);
                        jxl.write.Number label = new jxl.write.Number(column, row, value);
                        sheet.addCell(label);
                    } else {
                        //http://alvinalexander.com/java/edu/pj/jdbc/recipes/ResultSet-ColumnType.shtml
                        System.err.println("Type: " + type + " " + columnName + " = " + resultSet.getString(i));
                    }
                    column++;
                }
                System.out.println();
                if (id > -1) {
					String addOns = readAddOns(id, ADD_ON_FIELD_ID);
					sheet.addCell(new Label(column, row, addOns));
                }
                row++;
            }
            workbook.write();
            workbook.close();
            System.out.println("Wrote Excel output with: " + workbook.getSheets()[0].getRows() + " rows to: " + output.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error: " + ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (statement != null) {
                    statement.close();
                }

                if (databaseConnection != null) {
                    databaseConnection.close();
                }
            } catch (Exception e) {

            }
        }
    }

    public static void main(String[] args) {
    	if (args.length < 3) {
    		System.err.println("Usage: java -Xms4M -Xmx10M -jar startline-0.0.4-SNAPSHOT-jar-with-dependencies.jar <username> <password> <outputfile(Excel)>");
    		System.exit(1);
		}
    	String user = args[0];
    	String password = args[1];
		String outputFilename = args[2];
        MySqlToExcel m = new MySqlToExcel();
        File output = new File(outputFilename);
        m.readDataBase(user, password, output);
    }
}
