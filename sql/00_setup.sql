
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


@01_setup_info606;


select count(*) from Student;
select count(*) from Advisor;
select count(*) from Course;
select count(*) from Schedule;

----------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------
select * from user_indexes where table_name = 'ADVISOR';

select extract(OBJECT_VALUE, '/Student' ).getStringVal() From Student;
select extract(OBJECT_VALUE, '/Advisor' ).getStringVal() From Advisor;
select extract(OBJECT_VALUE, '/' ).getStringVal() From Course;
select extract(OBJECT_VALUE, '/' ).getStringVal() From Schedule;
SELECT extract(OBJECT_VALUE, '/Course[courseId = "INFO442"]').getStringVal() FROM Course ;-- WHERE (existsNode(OBJECT_VALUE,'/Course[starts-with(courseId, "CSC") ]') = 1);



select to_xml(xmldata.Student) from dual;
select XMLDATA."name" FROM Advisor;

SELECT OBJECT_VALUE FROM ADVISOR;
SELECT extract(OBJECT_VALUE, '/').getStringVal() FROM STUDENT WHERE (existsNode(OBJECT_VALUE,'/Student[advisor = 773201]') = 1);
	


select * from advisor;

set autotrace on;
Select 
a.OBJECT_VALUE
--extract(a.OBJECT_VALUE, '/Advisor/employeeId/text()' ).getStringVal() as empId
--extract(s.OBJECT_VALUE, '/Student/studentId/text()' ).getStringVal() as studentId
From Advisor a, Student s WHERE s.XMLDATA."advisor" = a.XMLDATA."employeeId"; --ORDER BY a.XMLDATA."employeeId";
set autotrace off;


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
