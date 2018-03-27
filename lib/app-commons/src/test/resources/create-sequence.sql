/*Esse Script é um default, provavelmente deverá ser alterado para suportar o ator especifico do cliente.*/

/*==============================================================*/
/* Table: TB_ATOR                                               */
/*==============================================================*/
CREATE TABLE DBSINGULAR.TB_ATOR (
   CO_ATOR  BIGINT       IDENTITY,
   CO_USUARIO           VARCHAR(60)          NOT NULL,
  CONSTRAINT PK_ATOR PRIMARY KEY (CO_ATOR)
);

/*==============================================================*/
/* View: VW_ATOR                                    */
/*==============================================================*/
create view DBSINGULAR.VW_ATOR AS
SELECT A.CO_ATOR, A.CO_USUARIO as CO_USUARIO, A.CO_USUARIO as NO_ATOR, ' ' as DS_EMAIL
FROM DBSINGULAR.TB_ATOR A;


CREATE SEQUENCE DBSINGULAR.SQ_CO_ATOR  START WITH 1 INCREMENT BY 1;


ALTER TABLE DBSINGULAR.TB_ATOR ALTER COLUMN CO_ATOR BIGINT NOT NULL;
