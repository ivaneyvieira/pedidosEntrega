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
SELECT storenoNfr      AS storeno,
       pdvnoNfr        AS pdvno,
       xanoNfr         AS xano,
       MID(MAX(CONCAT(LPAD(A.date, 10, '0'), LPAD(A.time, 10, '0'), LPAD(A.auxShort4, 10, '0'))), 1,
	   10) * 1     AS date,
       MID(MAX(CONCAT(LPAD(A.date, 10, '0'), LPAD(A.time, 10, '0'), LPAD(A.auxShort4, 10, '0'))),
	   11, 10) * 1 AS time,
       MID(MAX(CONCAT(LPAD(A.date, 10, '0'), LPAD(A.time, 10, '0'), LPAD(A.auxShort4, 10, '0'))),
	   21, 10) * 1 AS empno
FROM sqldados.awnfrh        AS A
  INNER JOIN sqldados.awnfr AS C
	       USING (storeno, cargano, storenoNfr, pdvnoNfr, xanoNfr)
WHERE A.date BETWEEN @DI AND @DF
  AND A.status IN (1, 13)
  AND A.auxShort4 > 0
GROUP BY storeno, pdvno, xano;

DROP TABLE IF EXISTS T_NOTAS;
CREATE TEMPORARY TABLE T_NOTAS (
  KEY (storeno, pdvno, xano),
  PRIMARY KEY (storenoEnt, nfnoEnt, nfseEnt)
)
SELECT DISTINCT A.cargano    AS cargano,
		A.storenoNfr AS storeno,
		A.pdvnoNfr   AS pdvno,
		A.xanoNfr    AS xano,
		A.ordno      AS ordno,
		A.nfno       AS nfnoFat,
		A.nfse       AS nfseFat,
		F.nfStoreno  AS storenoEnt,
		F.nfNfno     AS nfnoEnt,
		F.nfNfse     AS nfseEnt,
		C.empno
FROM sqldados.awnfr          AS A
  INNER JOIN T_CARGA         AS C
	       ON A.storenoNfr = C.storeno AND A.pdvnoNfr = C.pdvno AND A.xanoNfr = C.xano
  INNER JOIN sqldados.eord   AS O
	       ON O.storeno = A.storenoNfr AND O.ordno = A.ordno
  INNER JOIN sqldados.eoprdf AS F
	       ON (F.storeno = O.storeno AND F.ordno = O.ordno)
WHERE F.nfNfno <> 0
GROUP BY storenoEnt, nfnoEnt, nfseEnt;

DROP TABLE IF EXISTS T_METRICAS;
CREATE TEMPORARY TABLE T_METRICAS
SELECT C.storeno,
       C.pdvno,
       C.xano,
       COUNT(DISTINCT C.xano)                                           AS qtdEnt,
       ROUND(SUM(if(P.groupno = 010000, X.qtty, 0.000)), 2)             AS pisoCxs,
       ROUND(SUM(if(P.groupno = 010000, (X.qtty) * P.weight, 0.00)), 2) AS pisoPeso,
       ROUND(SUM((X.price / 100) * (X.qtty)), 2)                        AS valor,
       N.grossamt / 100                                                 AS valorNota,
       N.fre_amt / 100                                                  AS valorFrete,
       C.empno
FROM T_NOTAS                AS C
  INNER JOIN sqldados.nf    AS N
	       ON C.storenoEnt = N.storeno AND C.nfnoEnt = N.nfno AND C.nfseEnt = N.nfse
  INNER JOIN sqldados.xaprd AS X
	       ON X.storeno = N.storeno AND X.pdvno = N.pdvno AND X.xano = N.xano
  INNER JOIN sqldados.prd   AS P
	       ON P.no = X.prdno
GROUP BY C.storeno, C.pdvno, C.xano;

DROP TABLE IF EXISTS T_MESTRE;
CREATE TEMPORARY TABLE T_MESTRE
SELECT storenoNfr,
       pdvnoNfr,
       xanoNfr,
       A.status,
       placa,
       M.storeno,
       M.pdvno,
       M.xano,
       qtdEnt,
       pisoCxs,
       pisoPeso,
       valor,
       valorFrete,
       valorNota,
       E.empno,
       sname,
       name,
       funcao,
       funcaoName
FROM sqldados.awnfrh        AS A
  INNER JOIN sqldados.awnfr AS C
	       USING (storeno, cargano, storenoNfr, pdvnoNfr, xanoNfr)
  INNER JOIN T_METRICAS     AS M
	       ON A.storenoNfr = M.storeno AND A.pdvnoNfr = M.pdvno AND A.xanoNfr = M.xano
  INNER JOIN T_CARGA        AS CG
	       ON A.storenoNfr = CG.storeno AND A.pdvnoNfr = CG.pdvno AND C.xanoNfr = CG.xano AND
		  A.date = CG.date AND A.time = CG.time
  INNER JOIN T_EMP          AS E
	       ON E.empno = CG.empno;

SELECT funcaoName,
       sname               AS nome,
       empno               AS empno,
       SUM(ROUND(qtdEnt))  AS qtdEnt,
       SUM(ROUND(pisoCxs)) AS pisoCxs,
       SUM(pisoPeso)       AS pisoPeso,
       SUM(valor)          AS valor,
       SUM(valorNota)      AS valorNota,
       SUM(valorFrete)     AS valorFrete
FROM T_MESTRE
GROUP BY empno


