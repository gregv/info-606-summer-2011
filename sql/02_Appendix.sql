--
-- VERSIONING
--
create or replace directory test_dir as 'C:\Users\Grog7\Desktop\Files';
SELECT * FROM DBA_DIRECTORIES;
SELECT XMLDIFF( 
XMLType(bfilename('TEST_DIR', 'Course.xsd'), nls_charset_id('AL32UTF8'))
,
XMLType(bfilename('TEST_DIR', 'Course.2.xsd'), nls_charset_id('AL32UTF8'))
 ) FROM DUAL;


BEGIN
DBMS_XMLSCHEMA.INPLACEEVOLVE(
  schemaURL => 'Course.xsd', 
  diffXML => XMLType(bfilename('TEST_DIR', 'Schema_v1.to.v2.xml'), nls_charset_id('AL32UTF8')), 
  flags => dbms_xmlschema.INPLACE_EVOLVE
   );
    END;
/ 

-- List dependent schemas
SELECT dxs.SCHEMA_URL, dxs.OWNER
    FROM DBA_DEPENDENCIES dd, DBA_XML_SCHEMAS dxs
    WHERE dd.REFERENCED_NAME = (SELECT INT_OBJNAME
                                  FROM DBA_XML_SCHEMAS
                                  WHERE SCHEMA_URL = 'Course.xsd'
                                    AND OWNER = 'INFO606')
      AND dxs.INT_OBJNAME = dd.NAME;    
--
--

--
-- COMPARISON TO NORMAL TABLES
--
INSERT INTO SCHEDULE_NEW(scheduleId, academicYear, term) 
(
SELECT
 extract(OBJECT_VALUE, '//scheduleId/text()' ).getStringVal(),
 extract(OBJECT_VALUE, '//academicYear/text()' ).getNumberVal(),
 extract(OBJECT_VALUE, '//term/text()' ).getStringVal()
 FROM Schedule
);

set autotrace on;
SELECT * FROM SCHEDULE_NEW WHERE academicYear BETWEEN 2002 AND 2003;
set autotrace off;

set autotrace on;
SELECT * FROM Schedule WHERE existsNode(OBJECT_VALUE,'/Schedule[academicYear >= 2002 and academicYear <=2003 ]') = 1;
set autotrace off;

drop index schedule_new_academicyearx;
create index snew_year_ix on SCHEDULE_NEW(academicYear);
create index sa_year_ix on Schedule(extractValue(object_value,'/Schedule/academicYear') );
--
--


--
-- GENERAL PURPOSE SQL
select * from user_indexes where table_name = 'COURSE';
select count(*) from Student;
select count(*) from Advisor;
select * from Course;
select count(*) from Schedule;


select extract(OBJECT_VALUE, '/Student' ).getStringVal() From Student;
select extract(OBJECT_VALUE, '/Advisor' ).getStringVal() From Advisor;
select extract(OBJECT_VALUE, '/' ).getStringVal() From Course;
select extract(OBJECT_VALUE, '/' ).getStringVal() From Schedule;


SELECT extract(OBJECT_VALUE, '/').getStringVal() FROM Course  WHERE (existsNode(OBJECT_VALUE,'/Course[starts-with(courseId, "INFO870") ]') = 1);

set autotrace on;
SELECT OBJECT_VALUE FROM COURSE WHERE (existsNode(OBJECT_VALUE,'/Course[instructor="Ted Lee"]') = 1);
set autotrace off;



set autotrace on;
SELECT OBJECT_VALUE FROM COURSE WHERE contains(object_value, 'Ted Lee INPATH (/Course/instructor)') > 0;
set autotrace off;


set autotrace on;
Select 
a.OBJECT_VALUE
--extract(a.OBJECT_VALUE, '/Advisor/employeeId/text()' ).getStringVal() as empId
--extract(s.OBJECT_VALUE, '/Student/studentId/text()' ).getStringVal() as studentId
From Advisor a, Student s WHERE s.XMLDATA."advisor" = a.XMLDATA."employeeId"; --ORDER BY a.XMLDATA."employeeId";
set autotrace off;

set autotrace on;
SELECT OBJECT_VALUE FROM STUDENT WHERE (existsNode(OBJECT_VALUE,'/Student[GPA >0.9 and GPA <1.5]') = 1);
set autotrace off;

set autotrace on;
SELECT OBJECT_VALUE FROM STUDENT WHERE (existsNode(OBJECT_VALUE, '/Student[program = "Civil Engineering"]') = 1 );
set autotrace off;

set autotrace on;
SELECT OBJECT_VALUE FROM STUDENT WHERE contains(OBJECT_VALUE, 'Civil Engineering') >0;
set autotrace off;

set autotrace on;
SELECT OBJECT_VALUE FROM STUDENT WHERE contains(object_value, 'Civil Engineering INPATH (/Student/program)') > 0;
set autotrace off;

set autotrace on;
SELECT OBJECT_VALUE FROM STUDENT WHERE 
existsNode(object_value, '/Student/program[ora:contains(text(), "Civil Engineering") > 0]','xmlns:ora="http://xmlns.oracle.com/xdb"' ) = 1;
set autotrace off;

SELECT OBJECT_VALUE FROM STUDENT WHERE contains(object_value, 'Spring 2008 INPATH (/Student/admit_term)') > 0;

SELECT OBJECT_VALUE FROM STUDENT WHERE 
existsNode(object_value, '/Student/admit_term[ora:contains(text(), "Spring 2008") > 0]','xmlns:ora="http://xmlns.oracle.com/xdb"' ) = 1;


set autotrace on;
SELECT OBJECT_VALUE FROM STUDENT WHERE (existsNode(OBJECT_VALUE, '/Student[program = "Civil Engineering"]') = 1 AND existsNode(OBJECT_VALUE,'/Student[GPA >0.9 and GPA <2.5]') = 1 );
set autotrace off;
--
--

--
-- MAINTENANCE SQL
-- Optimize text indexes
BEGIN
ctx_ddl.optimize_index('course_textindex_ix', 'FULL', 60 ); -- 60 is the max time to optimize in minutes
END;
/

--

--
-- DIFFERENT TABLE TYPES

-- CLOB
DROP TABLE Course cascade constraints purge;
CREATE TABLE Course of XMLTYPE XMLType STORE AS CLOB;
--
CREATE INDEX course_ins_inx ON Course
  (extract(OBJECT_VALUE, '/Course/instructor'))
  INDEXTYPE IS XDB.XMLIndex;



-- BINARY XML
 BEGIN
       dbms_xmlschema.deleteSchema
               -- ( schemaurl =>  'http://www.info606.org/course/Course.xsd'
               ( schemaurl =>  'Course.xsd'
               , delete_option => dbms_xmlschema.DELETE_CASCADE_FORCE
               );
    END;
/ 

begin
-- dbms_xmlschema.registeruri('http://www.info606.org/course/Course.xsd', '/public/Course.xsd', 
dbms_xmlschema.registerSchema(
 SCHEMAURL => 'Course.xsd', 
 SCHEMADOC => xdbURIType('/public/Course.xsd').getClob(), 
 local=>true,
 gentypes=>false,
 genbean=>false,
 gentables=>false,
 OPTIONS    => DBMS_XMLSCHEMA.REGISTER_BINARYXML,
 owner=>'info606');
 end;
 /
CREATE TABLE Course of XMLTYPE XMLType STORE AS BINARY XML XMLSCHEMA "Course.xsd" ELEMENT "Course";
--

--
--

--
-- SQL that won't work with 11g XE
-- 

CREATE BITMAP INDEX student_status_ix on Student (extractValue(object_value,'/Schedule/status') );
--
--