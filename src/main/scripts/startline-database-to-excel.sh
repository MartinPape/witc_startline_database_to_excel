#!/bin/bash
# Created by: Martin Pape
DB_LOGIN=$1
DB_PASSWORD=$2
FTP_LOGIN=$3
FTP_PASSWORD=$4
BASE_DIR=/home/martin/startline
EXCEL_FILENAME=startline.xls
rm $BASE_DIR/$EXCEL_FILENAME
cd $BASE_DIR
ncftpget -R -u $FTP_LOGIN -p $FTP_PASSWORD ftp://www.startlinefoundation.org/startlinefoundation/wp-content/backup-db
rm -f $BASE_DIR/backup-db/.htaccess
rm -f $BASE_DIR/backup-db/index.php
find $BASE_DIR/backup-db/* -mtime +60 -exec rm {} \;
cd $BASE_DIR/backup-db
LASTEST_ARCHIVE=`ls -t *.gz | head -1`
echo "Latest archive is: $LASTEST_ARCHIVE"
cd ..
rm -rf $BASE_DIR/import/*
cp $BASE_DIR/backup-db/$LASTEST_ARCHIVE ./import
echo "Unzipping (1) database dump: $LASTEST_ARCHIVE"
gzip -d $BASE_DIR/import/$LASTEST_ARCHIVE
cd import
LASTEST_SQL=`ls -r *.sql | head -1`
cd ..
echo "Importing into database from: $LASTEST_SQL"
sed -i -e 's/utf8mb4_unicode_520_ci/utf8mb4_unicode_ci/g' $BASE_DIR/import/$LASTEST_SQL
mysql -v --user=$DB_LOGIN --password=$DB_PASSWORD startline < $BASE_DIR/import/$LASTEST_SQL
echo "Copying users"
mysql -v --user=$DB_LOGIN --password=$DB_PASSWORD startline < $BASE_DIR/copyusers_cf.sql
echo "Creating Excel"
java -Xms4M -Xmx10M -jar startline-0.0.4-SNAPSHOT-jar-with-dependencies.jar $DB_LOGIN $DB_PASSWORD $EXCEL_FILENAME
echo "Sending emails"
#add a line for every email-address here
mutt -s "Startline users" xxx.xxx@gmail.com -a $EXCEL_FILENAME < $BASE_DIR/email-body.text
