DO @DI := :dateI;
DO @DF := :dateF;

DROP TABLE IF EXISTS T_EMP;
CREATE TEMPORARY TABLE T_EMP (
  PRIMARY KEY (empno)
)
SELECT E.no   AS empno,
       E.sname,
       E.name,
       funcao,
       F.name AS funcaoName
FROM sqldados.emp            AS E
  INNER JOIN sqldados.empfnc AS F
	       ON F.no = E.funcao;

DROP TABLE IF EXISTS T_CARGA;
CREATE TEMPORARY TABLE T_CARGA (
  PRIMARY KEY (storeno, pdvno, xano)
)
SELECT storenoNfr AS storeno,
       pdvnoNfr   AS pdvno,
       xanoNfr    AS xano,
       CASE statusCarga
	 WHEN 0
	   THEN 'Carga Pendente'
	 WHEN 1
	   THEN 'Saida Carga (transito)'
	 WHEN 2
	   THEN 'Retorno Concluido'
	 WHEN 3
	   THEN 'Retorno Erro (nao utilizado)'
       END        AS descricaoStatus
FROM sqldados.awnfr
WHERE date BETWEEN @DI AND @DF
GROUP BY storeno, pdvno, xano;

DROP TABLE IF EXISTS T_METRICAS;
CREATE TEMPORARY TABLE T_METRICAS (
  PRIMARY KEY (storeno, pdvno, xano)
)
SELECT storeno,
       pdvno,
       xano,
       COUNT(*)                                                      AS qtdEnt,
       SUM(if(P.groupno = 010000, I.qtty / 1000, 0.000))             AS pisoCxs,
       SUM(if(P.groupno = 010000, (I.qtty / 1000) * P.weight, 0.00)) AS pisoPeso,
       SUM((I.price / 100) * (I.qtty / 1000))                        AS valor
FROM sqldados.nfr            AS N
  INNER JOIN T_CARGA
	       USING (storeno, pdvno, xano)
  INNER JOIN sqldados.nfrprd AS I
	       USING (storeno, pdvno, xano)
  INNER JOIN sqldados.prd    AS P
	       ON P.no = I.prdno
GROUP BY N.storeno, N.pdvno, N.xano;

DROP TABLE IF EXISTS T_MESTRE;
CREATE TEMPORARY TABLE T_MESTRE
SELECT storenoNfr,
       pdvnoNfr,
       xanoNfr,
       status,
       placa,
       auxShort4 AS motorista,
       M.*,
       E.*
FROM sqldados.awnfrh    AS A
  INNER JOIN T_METRICAS AS M
	       ON A.storenoNfr = M.storeno AND A.pdvnoNfr = M.pdvno AND A.xanoNfr = M.xano
  INNER JOIN T_EMP      AS E
	       ON E.empno = A.auxShort4;

SELECT funcaoName,
       sname               AS nome,
       SUM(ROUND(qtdEnt))  AS qtdEnt,
       SUM(ROUND(pisoCxs)) AS pisoCxs,
       SUM(pisoPeso)       AS pisoPeso,
       SUM(valor)          AS valor
FROM T_MESTRE
GROUP BY empno


