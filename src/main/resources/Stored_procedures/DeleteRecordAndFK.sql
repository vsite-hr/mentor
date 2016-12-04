CREATE OR REPLACE FUNCTION deleteRecordAndFK(IN tableName regclass, IN deleteId UUID)
RETURNS json AS $$

DECLARE
recordToDelete integer DEFAULT 0;
returnDeletedRecord record;
PK_ColumnName varchar;
FK_TableNames varchar[]; 
FK_ColumnNames varchar[];
FK_IsNullable varchar[];
BEGIN
	-- Select FK tableName, columnName, isNulllable as arrays and PK columnName
	EXECUTE 'SELECT array(SELECT table_constraints.table_name::varchar
                          FROM information_schema.columns,
                               information_schema.table_constraints, 
                               information_schema.key_column_usage, 
                               information_schema.constraint_column_usage
                          WHERE table_constraints.constraint_name = key_column_usage.constraint_name
                          AND constraint_column_usage.constraint_name = table_constraints.constraint_name
                          AND columns.table_name = table_constraints.table_name
                          AND columns.column_name = key_column_usage.column_name 
                          AND table_constraints.constraint_type = ''FOREIGN KEY''
                          AND constraint_column_usage.table_name = '|| quote_literal(tableName) ||
                          'ORDER BY key_column_usage.column_name
                          ),
                    array(SELECT key_column_usage.column_name::varchar
                          FROM information_schema.columns,
                               information_schema.table_constraints, 
                               information_schema.key_column_usage, 
                               information_schema.constraint_column_usage
                          WHERE table_constraints.constraint_name = key_column_usage.constraint_name
                          AND constraint_column_usage.constraint_name = table_constraints.constraint_name
                          AND columns.table_name = table_constraints.table_name
                          AND columns.column_name = key_column_usage.column_name
                          AND table_constraints.constraint_type = ''FOREIGN KEY''
                          AND constraint_column_usage.table_name = '|| quote_literal(tableName) ||
                          'ORDER BY key_column_usage.column_name
                          ),
                    array(SELECT columns.is_nullable::varchar
                          FROM information_schema.columns,
                               information_schema.table_constraints, 
                               information_schema.key_column_usage, 
                               information_schema.constraint_column_usage
                          WHERE table_constraints.constraint_name = key_column_usage.constraint_name
                          AND constraint_column_usage.constraint_name = table_constraints.constraint_name
                          AND columns.table_name = table_constraints.table_name
                          AND columns.column_name = key_column_usage.column_name
                          AND table_constraints.constraint_type = ''FOREIGN KEY''
                          AND constraint_column_usage.table_name = '|| quote_literal(tableName) ||
                          'ORDER BY key_column_usage.column_name
                          ),
                    key_column_usage.column_name 
                    FROM information_schema.key_column_usage,
                  	     information_schema.table_constraints
                    WHERE table_constraints.table_name = '|| quote_literal(tableName) ||'
                    AND key_column_usage.table_name = table_constraints.table_name
                    AND table_constraints.constraint_type = ''PRIMARY KEY'''
             INTO FK_TableNames, FK_ColumnNames, FK_IsNullable, PK_ColumnName;
             
    -- Check if this record exist, if not trow exception
	EXECUTE 'SELECT COUNT(*) FROM ' 
    		|| tableName 
            ||' WHERE '|| quote_ident(PK_ColumnName) 
        	||' = '|| quote_literal(deleteId)
    INTO recordToDelete;
      
   IF  recordToDelete = 0 THEN
    	RAISE EXCEPTION 'ID does not exist: %', deleteId
        	  USING HINT = 'Please provide other ID';
    END IF;
    
    -- Loop through FK_relation tables, if not nullable then delete if yes then set to null
    FOR i IN 1..array_length(FK_ColumnNames, 1) LOOP

		IF (FK_IsNullable[i] = 'NO')
        THEN EXECUTE 'DELETE FROM ' || quote_ident(FK_TableNames[i]) 
                        ||' WHERE '|| quote_ident(FK_ColumnNames[i]) 
                        || ' = ' || quote_literal(deleteId);
		ELSE
        	 EXECUTE 'UPDATE ' || quote_ident(FK_TableNames[i]) 
            			||' SET '|| quote_ident(FK_ColumnNames[i])
                        ||' = NULL' 
                        ||' WHERE '|| quote_ident(FK_ColumnNames[i]) 
                        ||' = '|| quote_literal(deleteId);
        END IF;
	END LOOP;
    
    -- Select record that will be passed back before it's deleted 
    EXECUTE 'SELECT * FROM ' 
    		|| tableName 
            ||' WHERE '|| quote_ident(PK_ColumnName) 
            ||' = '|| quote_literal(deleteId) 
    INTO returnDeletedRecord;
  
    -- Delete record
	EXECUTE 'DELETE FROM '
    		|| tableName 
            ||' WHERE '|| quote_ident(PK_ColumnName) 
			|| ' = ' || quote_literal(deleteId);
                 	
    RETURN row_to_json(returnDeletedRecord);
END;
$$ LANGUAGE plpgsql;