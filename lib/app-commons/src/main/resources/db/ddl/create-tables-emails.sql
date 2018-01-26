CREATE SCHEMA if not exists DBSINGULAR;

/*==============================================================*/
/* Table: TB_EMAIL                                              */
/*==============================================================*/
CREATE TABLE DBSINGULAR.TB_EMAIL
(
   CO_EMAIL             INTEGER              IDENTITY,
   TX_RESPONDER_PARA    VARCHAR(200),
   TX_ASSUNTO           VARCHAR(200)         NOT NULL,
   TX_CONTEUDO          CLOB                 NOT NULL,
   DT_CRIACAO           DATETIME             NOT NULL,
   CONSTRAINT PK_EMAIL PRIMARY KEY (CO_EMAIL)
);

/*==============================================================*/
/* Table: TB_EMAIL_ARQUIVO                                      */
/*==============================================================*/
CREATE TABLE DBSINGULAR.TB_EMAIL_ARQUIVO
(
   CO_EMAIL             INTEGER              NOT NULL,
   CO_ARQUIVO           INTEGER              NOT NULL,
   CONSTRAINT PK_EMAIL_ARQUIVO PRIMARY KEY (CO_EMAIL, CO_ARQUIVO)
);

/*==============================================================*/
/* Table: TB_DESTINATARIO_EMAIL                                 */
/*==============================================================*/
CREATE TABLE DBSINGULAR.TB_DESTINATARIO_EMAIL
(
   CO_DESTINATARIO_EMAIL INTEGER             IDENTITY,
   CO_EMAIL             INTEGER              NOT NULL,
   TX_ENDERECO          VARCHAR(100)          NOT NULL,
   TP_ENVIO             CHAR(3)              NOT NULL
      CONSTRAINT CKC_TP_ENVIO_TB_DESTI CHECK (TP_ENVIO IN ('To','Cc','Bcc')),
   DT_ENVIO             DATETIME,
   CONSTRAINT PK_DESTINATARIO_EMAIL PRIMARY KEY (CO_DESTINATARIO_EMAIL)
);