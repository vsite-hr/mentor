CREATE OR REPLACE FUNCTION deleteRecordAndFK(IN tableName regclass, IN columnName varchar(30), IN deleteId UUID)
RETURNS json AS $$

DECLARE
fkCount integer;
recordToDelete integer DEFAULT 0;
deletedRecord record;
FK_TableNames varchar[]; 
BEGIN
	-- Check if this record exist, if not return error
	EXECUTE 'SELECT COUNT(*)
    		FROM ' || tableName ||
    		' WHERE '|| quote_ident(columnName) 
        	||' = '|| quote_literal(deleteId)
    INTO recordToDelete;
      
    IF  recordToDelete = 0 THEN
    	RAISE EXCEPTION 'Unknown ID: %', deleteId
        	  USING HINT = 'Please provide regular ID';
    END IF;

	-- Number of ForeignKey constraints
	SELECT count(*)
	FROM information_schema.table_constraints tc
	WHERE LOWER(tc.constraint_type) = LOWER('foreign key')
	AND tc.constraint_name LIKE ('%' || columnName)
    INTO fkCount;

    -- Select names of FK tables into array
    SELECT array(SELECT table_name::varchar 
                 FROM information_schema.table_constraints
                 WHERE constraint_name LIKE ('%' || columnName)) 
    INTO FK_TableNames;  
    
    -- Loop through FK_relation tables
    FOR i IN 1..fkCount LOOP
    	EXECUTE 'DELETE FROM '|| quote_ident(FK_TableNames[i]) ||
                ' WHERE '|| quote_ident(columnName) || ' = ' || quote_literal(deleteId);
	END LOOP;
    
    -- Select record that will be passed back before it's deleted 
    EXECUTE 'SELECT * FROM ' || tableName ||
    		' WHERE '|| quote_ident(columnName) 
            ||' = '|| quote_literal(deleteId) 
    INTO deletedRecord;
  
    -- Delete record
	EXECUTE 'DELETE FROM '|| tableName ||
            ' WHERE '|| quote_ident(columnName) 
			|| ' = ' || quote_literal(deleteId);
                 	
    RETURN row_to_json(deletedRecord);
END;
$$ LANGUAGE plpgsql;