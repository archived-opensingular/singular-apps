/*Não existe uma entidade para a tabela ATOR.
  A View é gerada altomaticamente pela classe SingularSchemaExport
*/

/*==============================================================*/
/* Table: TB_ATOR                                               */
/*==============================================================*/
CREATE TABLE DBSINGULAR.TB_ATOR (
   CO_ATOR  BIGINT       IDENTITY,
   CO_USUARIO           VARCHAR(60)          NOT NULL,
  CONSTRAINT PK_ATOR PRIMARY KEY (CO_ATOR)
);

/*==============================================================*/
/* Table: TB_TIPO_TAREFA                                        */
/*==============================================================*/
CREATE TABLE DBSINGULAR.TB_TIPO_TAREFA (
  CO_TIPO_TAREFA BIGINT       IDENTITY,
  DS_TIPO_TAREFA VARCHAR(100) NOT NULL,
  CONSTRAINT PK_TIPO_TAREFA PRIMARY KEY (CO_TIPO_TAREFA),
  CONSTRAINT AK_AK_TIPO_TAREFA_TB_TIPO_ UNIQUE (DS_TIPO_TAREFA)
);


create view DBSINGULAR.VW_ATOR AS
SELECT A.CO_ATOR, A.CO_USUARIO as CO_USUARIO, A.CO_USUARIO as NO_ATOR, ' ' as DS_EMAIL
FROM DBSINGULAR.TB_ATOR A;