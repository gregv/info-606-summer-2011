
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

desc resource_view;

-- RUN THIS AS INFO606
@01_setup_info606;


----------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------