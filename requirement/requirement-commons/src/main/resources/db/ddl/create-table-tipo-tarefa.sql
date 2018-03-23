-- ==============================================================
-- /* Table: TB_TIPO_TAREFA                                        */
-- /*==============================================================*/
CREATE TABLE DBSINGULAR.TB_TIPO_TAREFA (
  CO_TIPO_TAREFA BIGINT      NOT NULL,
  DS_TIPO_TAREFA VARCHAR(100) NOT NULL,
  CONSTRAINT PK_TIPO_TAREFA PRIMARY KEY (CO_TIPO_TAREFA),
  CONSTRAINT AK_AK_TIPO_TAREFA_TB_TIPO_ UNIQUE (DS_TIPO_TAREFA)
);