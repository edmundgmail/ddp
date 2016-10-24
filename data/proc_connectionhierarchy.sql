 CREATE DEFINER=`root`@`localhost` PROCEDURE `proc_connectionhierarchy`(
IN connname varchar(255)
)
BEGIN
	select h.* from view_hierarchy h, connectionlinkage cl, connections c
    where cl.connection_id = c.connection_id
    and (cl.start_date is null or (cl.start_date <= now()))
    and (cl.end_date is null or (cl.end_date >= now()))
    and (cl.datasource_id is null OR 
    (cl.datasource_id = h.datasource_id 
    AND (cl.dataentity_id is null OR (cl.dataentity_id=h.dataentity_id 
    AND cl.datafield_id is null or (cl.datafield_id=h.datafield_id)
    ))
    ));
END
