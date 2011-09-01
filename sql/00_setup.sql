
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

------------------------------------------------------------------------------------------------------------
-- ADVISOR
--
 BEGIN
       dbms_xmlschema.deleteSchema
               --( schemaurl =>  'http://www.info606.org/advisor/Advisor.xsd'
               ( schemaurl =>  'Advisor.xsd'
               , delete_option => dbms_xmlschema.DELETE_CASCADE_FORCE
               );
    END;
/ 

begin
--dbms_xmlschema.registerSchema('http://www.info606.org/advisor/Advisor.xsd', 
dbms_xmlschema.registerSchema('Advisor.xsd', 
xdburitype('/public/Advisor.xsd'), 
 local=>true,
 gentypes=>true,
 genbean=>false,
 gentables=>false,
 owner=>'info606');
 end;
 /

DROP TABLE ADVISOR cascade constraints purge;

CREATE TABLE ADVISOR of XMLTYPE
 --XMLSCHEMA "http://www.info606.org/advisor/Advisor.xsd"
 XMLSCHEMA "Advisor.xsd"
  ELEMENT "Advisor";


ALTER TABLE Advisor ADD CONSTRAINT advisor_pk UNIQUE (XMLDATA."employeeId");
  



------------------------------------------------------------------------------------------------------------
-- COURSE
--
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
dbms_xmlschema.registeruri('Course.xsd', '/public/Course.xsd', 
 local=>true,
 gentypes=>true,
 genbean=>false,
 gentables=>false,
 owner=>'info606');
 end;
 /
 
DROP TABLE Course cascade constraints purge;

CREATE TABLE Course of XMLTYPE
 --XMLSCHEMA "http://www.info606.org/course/Course.xsd"
 XMLSCHEMA "Course.xsd"
  ELEMENT "Course";


ALTER TABLE Course ADD CONSTRAINT course_pk UNIQUE (XMLDATA."coursePrefix",XMLDATA."crn" );



------------------------------------------------------------------------------------------------------------
--
-- SCHEDULE
--
 BEGIN
       dbms_xmlschema.deleteSchema
               -- ( schemaurl =>  'http://www.info606.org/schedule/Schedule.xsd'
               ( schemaurl =>  'Schedule.xsd'
               , delete_option => dbms_xmlschema.DELETE_CASCADE_FORCE
               );
    END;
/ 

 begin
-- dbms_xmlschema.registeruri('http://www.info606.org/schedule/Schedule.xsd', '/public/Schedule.xsd', 
dbms_xmlschema.registeruri('Schedule.xsd', '/public/Schedule.xsd', 
 local=>true,
 gentypes=>true,
 genbean=>false,
 gentables=>false,
 owner=>'info606');
 end;
 /
 
DROP TABLE Schedule cascade constraints purge;

CREATE TABLE Schedule of XMLTYPE
 --XMLSCHEMA "http://www.info606.org/schedule/Schedule.xsd"
 XMLSCHEMA "Schedule.xsd"
  ELEMENT "Schedule";


ALTER TABLE Schedule ADD CONSTRAINT schedule_pk UNIQUE (XMLDATA."scheduleId");

 
------------------------------------------------------------------------------------------------------------
-- STUDENT
--

  BEGIN
       dbms_xmlschema.deleteSchema
               -- ( schemaurl =>  'http://www.info606.org/student/Student.xsd'
               ( schemaurl =>  'Student.xsd'
               , delete_option => dbms_xmlschema.DELETE_CASCADE_FORCE
               );
    END;
/ 

 begin
-- dbms_xmlschema.registeruri('http://www.info606.org/student/Student.xsd', '/public/Student.xsd', 
dbms_xmlschema.registeruri('Student.xsd', '/public/Student.xsd', 
 local=>true,
 gentypes=>true,
 genbean=>false,
 gentables=>false,
 owner=>'info606');
 end;
 /

DROP TABLE Student cascade constraints purge;

CREATE TABLE Student of XMLTYPE
 -- XMLSCHEMA "http://www.info606.org/student/Student.xsd"
 XMLSCHEMA "Student.xsd"
  ELEMENT "Student";
  
  
ALTER TABLE Student ADD CONSTRAINT student_pk UNIQUE (XMLDATA."studentId" );
ALTER TABLE Student ADD CONSTRAINT studentAdvisorFK FOREIGN KEY (XMLDATA."advisor") REFERENCES Advisor (XMLDATA."employeeId") ENABLE;




drop index student_gpa_inx;
create index student_gpa_inx on Student(extractValue(object_value,'//GPA') );

drop index student_program_inx;
create index student_program_inx on Student(extractValue(object_value,'/Student/program') );

drop index student_textindex_ix;
CREATE INDEX student_textindex_ix ON student (OBJECT_VALUE) INDEXTYPE IS CTXSYS.CONTEXT;




----------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------
  select * from user_indexes where table_name = 'ADVISOR';

select extract(OBJECT_VALUE, '/Student' ).getStringVal() From Student;
select extract(OBJECT_VALUE, '/Advisor' ).getStringVal() From Advisor;
select to_xml(xmldata.Student) from dual;
select XMLDATA."name" FROM Advisor;

select * from advisor;

Select extract(s.OBJECT_VALUE, '/Student/studentId/text()' ).getStringVal(), 
extract(s.OBJECT_VALUE, '/Student/advisor/text()' ).getStringVal(), 
extract(a.OBJECT_VALUE, '/Advisor/employeeId/text()' ).getStringVal() 
From Advisor a, Student s WHERE s.XMLDATA."advisor" = a.XMLDATA."employeeId";



--
-- QUERIES
-- 


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

SELECT count(*) FROM STUDENT WHERE (existsNode(OBJECT_VALUE, '/Student[program = "Civil Engineering"]') = 1 AND existsNode(OBJECT_VALUE,'/Student[GPA >0.9 and GPA <2.5]') = 1 );

select count(*) from student;


DELETE FROM STUDENT WHERE existsNode(OBJECT_VALUE, "'"/Student[studentId = 4982719]"'") = 1;
