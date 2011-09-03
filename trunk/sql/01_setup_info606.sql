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

 
 DROP TABLE Course cascade constraints purge;


 -- STRUCTURED XML
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
 
CREATE TABLE Course of XMLTYPE
 XMLSCHEMA "Course.xsd"
  ELEMENT "Course";
--
  
  
ALTER TABLE Course DROP CONSTRAINT course_pk;
ALTER TABLE Course ADD CONSTRAINT course_pk UNIQUE (XMLDATA."courseId" );

drop index course_instructor_inx;
create index course_instructor_inx on Course(extractValue(object_value,'//instructor') );
 
drop index course_textindex_ix;
-- CREATE INDEX course_textindex_ix ON Course (OBJECT_VALUE) INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS('SYNC(EVERY "SYSDATE+1/24")');
CREATE INDEX course_textindex_ix ON Course (OBJECT_VALUE) INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS('SYNC(ON COMMIT)');
 

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

drop index student_advisor_inx;
create index student_advisor_inx on Student(extractValue(object_value,'/Student/advisor') );


drop index student_textindex_ix;
CREATE INDEX student_textindex_ix ON student (OBJECT_VALUE) INDEXTYPE IS CTXSYS.CONTEXT;