DO @TIPO := :tipo;
DO @DATA1 := IF(@TIPO = 'R', 20170701, 20200101);
DO @DATA2 := IF(@TIPO = 'R', 20170701, 20200518);

DROP TEMPORARY TABLE IF EXISTS T2;
CREATE TEMPORARY TABLE T2 (
  PRIMARY KEY (storeno, ordno)
)
SELECT pxa.storeno,
       pxa.pdvno,
       pxa.eordno                                       AS ordno,
       pxa.time,
       pxanf.fre_amt,
       MAX(IF(pxa.cfo IN (5922, 6922), pxa.date, NULL)) AS data_venda,
       MAX(IF(pxa.cfo IN (5922, 6922), pxa.nfno, NULL)) AS nfno_venda,
       MAX(IF(pxa.cfo IN (5922, 6922), pxa.nfse, NULL)) AS nfse_venda,
       MAX(IF(pxa.cfo IN (5922, 6922), pxa.amt, NULL))  AS valor_venda,
       MAX(IF(pxa.cfo IN (5117, 6117), pxa.date, NULL)) AS data_entrega,
       MAX(IF(pxa.cfo IN (5117, 6117), pxa.nfno, NULL)) AS nfno_entrega,
       MAX(IF(pxa.cfo IN (5117, 6117), pxa.nfse, NULL)) AS nfse_entrega,
       MAX(IF(pxa.cfo IN (5117, 6117), pxa.amt, NULL))  AS valor_entrega
FROM sqlpdv.pxa
  LEFT JOIN sqlpdv.pxanf
	      ON (pxa.xano = pxanf.xano AND pxa.storeno = pxanf.storeno AND pxa.pdvno = pxanf.pdvno)
WHERE (pxa.storeno IN (1, 2, 3, 4, 5, 6))
  AND (pxa.date >= @DATA2)
  AND pxa.cfo IN (5922, 6922, 5117, 6117)
GROUP BY pxa.storeno, pxa.eordno;

SELECT EO.storeno                                                             AS loja,
       S.name                                                                 AS nomeLoja,
       S.sname                                                                AS siglaLoja,
       EO.ordno                                                               AS pedido,
       MID(EO.rmkMontagem, 1, 1)                                              AS marca,
       CAST(P.date AS DATE)                                                   AS data,
       CAST(IF(EO.dataEntrega = 0, NULL, EO.dataEntrega) AS DATE)             AS dataEntrega,
       EO.pdvno                                                               AS pdvno,
       SEC_TO_TIME(P.time)                                                    AS hora,

       IFNULL(cast(T2.nfno_venda AS CHAR), '')                                AS nfnoFat,
       IFNULL(T2.nfse_venda, '')                                              AS nfseFat,
       if(T2.data_venda = 0, NULL, cast(T2.data_venda AS DATE))               AS dataFat,
       sec_to_time(nff2.auxLong4)                                             AS horaFat,
       (nff.grossamt / 100)                                                   AS valorFat,

       IFNULL(cast(T2.nfno_entrega AS CHAR), '')                              AS nfnoEnt,
       IFNULL(T2.nfse_entrega, '')                                            AS nfseEnt,
       if(T2.data_entrega = 0, NULL, cast(T2.data_entrega AS DATE))           AS dataEnt,
       sec_to_time(nfe2.auxLong4)                                             AS horaEnt,
       (EO.amount / 100)                                                      AS valorEnt,

       IFNULL(E.no, 0)                                                        AS vendno,
       cast(CONCAT(E.no, '-', E.sname) AS CHAR)                               AS vendedor,
       ifnull(C.no, 0)                                                        AS custno,
       cast(CONCAT(LPAD(C.no * 1, 6, '0'), '-', C.name) AS CHAR)              AS cliente,
       cast(CONCAT('(', IFNULL(CA.ddd, LEFT(C.ddd, 3)), ')',
		   IFNULL(CA.tel, LEFT(C.tel, 10))) AS CHAR)                  AS foneCliente,
       cast(RPAD(IFNULL(CA.city, C.city1), 20, ' ') AS CHAR)                  AS cidade,
       cast(IFNULL(CA.state, C.state1) AS CHAR)                               AS estado,
       C.add1                                                                 AS endereco,
       C.nei1                                                                 AS bairro,
       cast(RPAD(IFNULL(CA.addr, C.add1), 60, ' ') AS CHAR)                   AS enderecoEntrega,
       RPAD(IFNULL(CA.nei, C.nei1), 25, ' ')                                  AS bairroEntrega,
       IFNULL(T2.fre_amt, 0) / 100                                            AS frete,
       EO.amount / 100                                                        AS valor,
       IF(eoprdf.bits & POW(2, 1), 'R', 'E')                                  AS status,
       IFNULL(A.name, '')                                                     AS area,
       IFNULL(R.name, '')                                                     AS rota,
       IF(LEFT(OBS.remarks__480, 2) = 'EF ', LEFT(OBS.remarks__480, 11), ' ') AS obs,
       A.no                                                                   AS codArea,
       EO.userno                                                              AS userno,
       IFNULL(U.name, '')                                                     AS username,
       CAST(IF(EO.l14 = 0, NULL, EO.l14) AS DATE)                             AS dataPrint,
       sec_to_time(IF(EO.l13 = 0, NULL, EO.l13))                              AS horaPrint,
       RPAD(IFNULL(MID(O.remarks__480, 1, 80), ' '), 80, ' ')                 AS obs1,
       RPAD(IFNULL(MID(O.remarks__480, 81, 80), ' '), 80, ' ')                AS obs2,
       RPAD(IFNULL(MID(O.remarks__480, 161, 80), ' '), 80, ' ')               AS obs3,
       RPAD(IFNULL(MID(O.remarks__480, 241, 80), ' '), 80, ' ')               AS obs4,
       RPAD(IFNULL(MID(O.remarks__480, 241, 80), ' '), 80, ' ')               AS obs5,
       RPAD(IFNULL(MID(O.remarks__480, 241, 80), ' '), 80, ' ')               AS obs6,
       RPAD(IFNULL(MID(O.remarks__480, 241, 80), ' '), 80, ' ')               AS obs7,
       IF(eoprdf.bits & POW(2, 1), 'R', 'E')                                  AS tipo
FROM sqldados.eord           AS EO
  INNER JOIN sqldados.store  AS S
	       ON S.no = EO.storeno
  INNER JOIN T2
	       ON (T2.storeno = EO.storeno AND T2.ordno = EO.ordno)
  LEFT JOIN  sqldados.eordrk AS O
	       ON (O.storeno = EO.storeno AND O.ordno = EO.ordno)

  LEFT JOIN  sqldados.ctadd  AS CA
	       ON (EO.custno = CA.custno AND CA.seqno = EO.custno_addno)
  LEFT JOIN  sqldados.users  AS U
	       ON U.no = EO.userno
  LEFT JOIN  sqldados.custp  AS C
	       ON (C.no = EO.custno)
  LEFT JOIN  sqldados.emp    AS E
	       ON (E.no = EO.empno)
  LEFT JOIN  sqldados.eoprdf
	       ON (eoprdf.storeno = EO.storeno AND eoprdf.ordno = EO.ordno)
  LEFT JOIN  sqldados.paym
	       ON (paym.no = EO.paymno)
  LEFT JOIN  sqlpdv.pxa      AS P USE INDEX (e1)
	       ON (EO.storeno = P.storeno AND EO.ordno = P.eordno AND EO.nfno_futura = P.nfno AND
		   EO.nfse_futura = P.nfse)
  LEFT JOIN  sqldados.ctadd  AS AD
	       ON (C.no = AD.custno AND AD.seqno = EO.custno_addno)
  LEFT JOIN  sqldados.route  AS R
	       ON (AD.routeno = R.no)
  LEFT JOIN  sqldados.area   AS A
	       ON (A.no = R.areano)
  LEFT JOIN  sqldados.nf     AS nff
	       ON (T2.nfno_venda = nff.nfno AND T2.nfse_venda = nff.nfse AND
		   T2.storeno = nff.storeno)
  LEFT JOIN  sqldados.nf2    AS nff2
	       ON nff.storeno = nff2.storeno AND nff.pdvno = nff2.pdvno AND nff.xano = nff2.xano
  LEFT JOIN  sqldados.nf     AS nfe
	       ON (T2.nfno_entrega = nfe.nfno AND T2.nfse_entrega = nfe.nfse AND
		   T2.storeno = nfe.storeno)
  LEFT JOIN  sqldados.nf2    AS nfe2
	       ON nfe.storeno = nfe2.storeno AND nfe.pdvno = nfe2.pdvno AND nfe.xano = nfe2.xano
  LEFT JOIN  sqldados.eordrk AS OBS
	       ON (OBS.storeno = EO.storeno AND OBS.ordno = EO.ordno)
WHERE (EO.storeno IN (1, 2, 3, 4, 5, 6))
  AND (((@TIPO = 'R') AND (eoprdf.bits & POW(2, 1))) OR
       ((@TIPO = 'E') AND (NOT eoprdf.bits & POW(2, 1))))
  AND EO.status NOT IN (3, 5)
  AND (EO.date >= @DATA1)
  AND (nff.status <> 1 OR nff.status IS NULL)
GROUP BY EO.storeno, EO.ordno