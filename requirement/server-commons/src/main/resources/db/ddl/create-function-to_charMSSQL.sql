CREATE FUNCTION DBSINGULAR.TO_CHAR(@DATE_VAL DATETIME, @FORMAT VARCHAR(20))
RETURNS VARCHAR(17)
AS
BEGIN
    DECLARE @ret VARCHAR(17);
    IF(@FORMAT = 'DD/MM/YYYY HH24:MI')
          SELECT @ret = convert(varchar(10),@DATE_VAL,103) + ' ' + cast(DATEPART(HOUR,@DATE_VAL) as varchar(2)) + ':' + cast(DATEPART(MINUTE,@DATE_VAL)    as varchar(2))
    ELSE IF (@FORMAT = 'dd/MM/yyyy')
          SELECT @ret = convert(varchar(10),@DATE_VAL,103)
    RETURN @ret;
END;