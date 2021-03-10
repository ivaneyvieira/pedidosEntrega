DO @DI := :dateI;
DO @DF := :dateF;
DO @EMPNO := :empno;
DO @COLETA := 4499;

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
	       ON F.no = E.funcao
WHERE E.no = @EMPNO
  AND E.no <> @COLETA;

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
GROUP BY storeno, pdvno, xano
HAVING empno = @EMPNO
   AND empno <> @COLETA;

DROP TABLE IF EXISTS T_DATA_NOTA;
CREATE TEMPORARY TABLE T_DATA_NOTA (
  PRIMARY KEY (storeno, pdvno, xano)
)
SELECT storenoNfr AS storeno,
       pdvnoNfr   AS pdvno,
       xanoNfr    AS xano,
       A.date
FROM sqldados.awnfrh AS A
  INNER JOIN T_CARGA AS T
	       ON T.storeno = A.storenoNfr AND T.pdvno = A.pdvnoNfr AND T.xano = A.xanoNfr
WHERE status = 15
GROUP BY storenoNfr, pdvnoNfr, xanoNfr;

DROP TABLE IF EXISTS T_NOTAS;
CREATE TEMPORARY TABLE T_NOTAS (
  KEY (storeno, pdvno, xano),
  PRIMARY KEY (storenoEnt, nfnoEnt, nfseEnt)
)
SELECT DISTINCT A.cargano                                                   AS cargano,
		A.storenoNfr                                                AS storeno,
		A.pdvnoNfr                                                  AS pdvno,
		A.xanoNfr                                                   AS xano,
		A.ordno                                                     AS ordno,
		O.date                                                      AS datePedido,
		IF(O.empno = 440 AND O.storeno = 4, O.nfno, A.nfno)         AS nfnoFat,
		IF(O.empno = 440 AND O.storeno = 4, O.nfse, A.nfse)         AS nfseFat,
		CAST(IF(N.issuedate = 0, NULL, N.issuedate) AS DATE)        AS dateFat,
		IF(O.empno = 440 AND O.storeno = 4, O.storeno, F.nfStoreno) AS storenoEnt,
		IF(O.empno = 440 AND O.storeno = 4, O.nfno, F.nfNfno)       AS nfnoEnt,
		IF(O.empno = 440 AND O.storeno = 4, O.nfse, F.nfNfse)       AS nfseEnt,
		C.empno
FROM sqldados.awnfr          AS A
  LEFT JOIN  sqldados.nf     AS N
	       ON A.storenoNfr = N.storeno AND A.nfno = N.nfno AND A.nfse = N.nfse
  INNER JOIN T_CARGA         AS C
	       ON A.storenoNfr = C.storeno AND A.pdvnoNfr = C.pdvno AND A.xanoNfr = C.xano
  INNER JOIN sqldados.eord   AS O
	       ON O.storeno = A.storenoNfr AND O.ordno = A.ordno
  LEFT JOIN sqldados.eoprdf AS F
	       ON (F.storeno = O.storeno AND F.ordno = O.ordno)
GROUP BY storenoEnt, nfnoEnt, nfseEnt;

DROP TABLE IF EXISTS T_METRICAS;
CREATE TEMPORARY TABLE T_METRICAS
SELECT C.storeno,
       C.pdvno,
       C.xano,
       X.prdno,
       X.grade,

       C.nfnoFat                                                        AS nfnoFat,
       C.nfseFat                                                        AS nfseFat,
       C.dateFat                                                        AS dateFat,

       N.storeno                                                        AS storenoEnt,
       N.nfno                                                           AS nfnoEnt,
       N.nfse                                                           AS nfseEnt,
       CAST(N.issuedate AS DATE)                                        AS dateEnt,

       C.ordno                                                          AS numPedido,
       CAST(C.datePedido AS DATE)                                       AS datePedido,
       COUNT(DISTINCT C.xano)                                           AS qtdEnt,
       ROUND(SUM(IF(P.groupno = 010000, X.qtty, 0.000)), 2)             AS pisoCxs,
       ROUND(SUM(IF(P.groupno = 010000, (X.qtty) * P.weight, 0.00)), 2) AS pisoPeso,
       ROUND(SUM((X.price / 100) * (X.qtty)), 2)                        AS valor,
       N.grossamt / 100                                                 AS valorNota,
       N.fre_amt / 100                                                  AS valorFrete,
       C.empno
FROM T_NOTAS                AS C
  LEFT JOIN  T_DATA_NOTA    AS D
	       USING (storeno, pdvno, xano)
  INNER JOIN sqldados.nf    AS N
	       ON C.storenoEnt = N.storeno AND C.nfnoEnt = N.nfno AND C.nfseEnt = N.nfse
  INNER JOIN sqldados.xaprd AS X
	       ON X.storeno = N.storeno AND X.pdvno = N.pdvno AND X.xano = N.xano
  INNER JOIN sqldados.prd   AS P
	       ON P.no = X.prdno
WHERE IFNULL(N.issuedate, 0) BETWEEN D.date AND @DF
GROUP BY C.storeno, C.pdvno, C.xano, X.prdno, X.grade;

DROP TABLE IF EXISTS T_MESTRE;
CREATE TEMPORARY TABLE T_MESTRE
SELECT cargano,
       storenoNfr,
       pdvnoNfr,
       xanoNfr,
       A.status,
       placa,
       A.auxShort4          AS motorista,
       M.storeno,
       M.pdvno,
       M.xano,
       numPedido,
       datePedido,

       M.nfnoFat,
       M.nfseFat,
       M.dateFat,

       M.storenoEnt,
       M.dateEnt,
       M.nfnoEnt,
       M.nfseEnt,

       CAST(A.date AS DATE) AS entrega,

       prdno,
       grade,
       qtdEnt,
       pisoCxs,
       pisoPeso,
       valor,
       valorNota,
       valorFrete,
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

SELECT cargano,
       funcaoName                                                   AS funcaoName,
       sname                                                        AS nome,
       M.empno                                                      AS empno,
       storeno                                                      AS loja,
       prdno                                                        AS prdno,
       TRIM(MID(P.name, 1, 37))                                     AS descricao,
       grade                                                        AS grade,
       CAST(CONCAT(nfnoFat, '/', nfseFat) AS CHAR)                  AS notaFat,
       dateFat                                                      AS dateFat,
       CAST(CONCAT(storenoEnt, '-', nfnoEnt, '/', nfseEnt) AS CHAR) AS notaEnt,
       dateEnt                                                      AS dateEnt,
       entrega,
       numPedido,
       datePedido,
       ROUND(pisoCxs)                                               AS pisoCxs,
       pisoPeso                                                     AS pisoPeso,
       valor                                                        AS valor,
       valorNota,
       valorFrete
FROM T_MESTRE             AS M
  INNER JOIN sqldados.prd AS P
	       ON P.no = M.prdno
WHERE M.empno = @EMPNO
  AND M.empno <> @COLETA


