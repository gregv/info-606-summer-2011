
------------------ DO THIS PART AS SYSTEM/SYS --------------

-- UNLOCK XDB User
alter user xdb account unlock;
alter user xdb identified by xdbpwd;

-- SETUP FTP Port
call DBMS_XDB.setFTPPort(2121);

-- Create user
drop user info606;
create user info606 identified by pass DEFAULT TABLESPACE users TEMPORARY TABLESPACE temp;

grant connect to info606;
grant create any directory to info606;
grant resource to info606;
grant alter session to info606;
grant create view to info606;
grant create any table to info606;
grant create materialized view to info606;
grant ctxapp to info606;
grant SELECT_CATALOG_ROLE TO info606;
grant SELECT_ANY_DICTIONARY to info606;

desc resource_view;


----------------------------------------------------
------------- DO THIS PART AS INFO606 --------------

--
-- FTP XSDs to /public before doing the steps below
-- ftp://xdb@127.0.0.1 using password xdbpwd
--


-- Register schemas

--
-- ADVISOR
--
 BEGIN
       dbms_xmlschema.deleteSchema
               ( schemaurl =>  'http://www.info606.org/advisor/Advisor.xsd'
               , delete_option => dbms_xmlschema.DELETE_CASCADE_FORCE
               );
    END;
/ 

begin
dbms_xmlschema.registerSchema('http://www.info606.org/advisor/Advisor.xsd', 
xdburitype('/public/Advisor.xsd'), 
 local=>true,
 gentypes=>true,
 genbean=>false,
 gentables=>true,
 owner=>'info606');
 end;
 /

--
-- COURSE
--
 BEGIN
       dbms_xmlschema.deleteSchema
               ( schemaurl =>  'http://www.info606.org/course/Course.xsd'
               , delete_option => dbms_xmlschema.DELETE_CASCADE_FORCE
               );
    END;
/ 


begin
dbms_xmlschema.registeruri('http://www.info606.org/course/Course.xsd', '/public/Course.xsd', 
 local=>true,
 gentypes=>true,
 genbean=>false,
 gentables=>true,
 owner=>'info606');
 end;
 /
 


--
-- STUDENT
--

  BEGIN
       dbms_xmlschema.deleteSchema
               ( schemaurl =>  'http://www.info606.org/student/Student.xsd'
               , delete_option => dbms_xmlschema.DELETE_CASCADE_FORCE
               );
    END;
/ 

 begin
dbms_xmlschema.registeruri('http://www.info606.org/student/Student.xsd', '/public/Student.xsd', 
 local=>true,
 gentypes=>true,
 genbean=>false,
 gentables=>false,
 owner=>'info606');
 end;
 /

DROP TABLE Student cascade constraints purge;

CREATE TABLE Student of XMLTYPE
 XMLSCHEMA "http://www.info606.org/student/Student.xsd"
  ELEMENT "Student";
  
create unique index student_pk on Student(extractValue(object_value,'/Student/studentId') );
drop index student_pk;
SELECT * FROM user_indexes where table_name = 'STUDENT';

create index student_gpa_inx on Student(extractValue(object_value,'//GPA') );
drop index student_gpa_inx;


SELECT OBJECT_VALUE FROM STUDENT WHERE (existsNode(OBJECT_VALUE,'/Student[GPA >0.9 and GPA <1.5]') = 1);




--
-- SCHEDULE
--
 BEGIN
       dbms_xmlschema.deleteSchema
               ( schemaurl =>  'http://www.info606.org/schedule/Schedule.xsd'
               , delete_option => dbms_xmlschema.DELETE_CASCADE_FORCE
               );
    END;
/ 

 begin
dbms_xmlschema.registeruri('http://www.info606.org/schedule/Schedule.xsd', '/public/Schedule.xsd', 
 local=>true,
 gentypes=>true,
 genbean=>false,
 gentables=>true,
 owner=>'info606');
 end;
 /
 
 
-- Create tables
 
DROP TABLE Schedule cascade constraints purge;

CREATE TABLE Schedule of XMLTYPE
 XMLSCHEMA "http://www.info606.org/schedule/Schedule.xsd"
  ELEMENT "registration";
  

  

DROP TABLE Course cascade constraints purge;

CREATE TABLE Course of XMLTYPE
 XMLSCHEMA "http://www.info606.org/course/Course.xsd"
  ELEMENT "courseInfo";
  
  
DROP TABLE ADVISOR cascade constraints purge;

CREATE TABLE ADVISOR of XMLTYPE
 XMLSCHEMA "http://www.info606.org/advisor/Advisor.xsd"
  ELEMENT "advisor";
