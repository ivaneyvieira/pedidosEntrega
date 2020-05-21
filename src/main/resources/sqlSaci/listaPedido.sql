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
       MAX(IF(pxa.cfo IN (5117), pxa.date, NULL))       AS data_entrega,
       MAX(IF(pxa.cfo IN (5117), pxa.nfno, NULL))       AS nfno_entrega,
       MAX(IF(pxa.cfo IN (5117), pxa.nfse, NULL))       AS nfse_entrega,
       MAX(IF(pxa.cfo IN (5117), pxa.amt, NULL))        AS valor_entrega
FROM sqlpdv.pxa
  LEFT JOIN sqlpdv.pxanf
	      ON (pxa.xano = pxanf.xano AND pxa.storeno = pxanf.storeno AND pxa.pdvno = pxanf.pdvno)
WHERE (pxa.storeno IN (1, 2, 3, 4, 5, 6))
  AND (pxa.date >= 20200518)
  AND pxa.cfo IN (5922, 6922, 5117, 6117)
GROUP BY pxa.storeno, pxa.eordno;

SELECT eord.storeno                                                                 AS loja,
       eord.ordno                                                                   AS pedido,
       MID(eord.rmkMontagem, 1, 1)                                                  AS marca,
       CAST(pxa.date AS DATE)                                                       AS data,
       SEC_TO_TIME(pxa.time)                                                        AS hora,

       IFNULL(cast(T2.nfno_venda AS CHAR), '')                                      AS nfnoFat,
       IFNULL(T2.nfse_venda, '')                                                    AS nfseFat,
       if(T2.data_venda = 0, NULL, cast(T2.data_venda AS DATE))                     AS dataFat,
       sec_to_time(nff2.auxLong4)                                                   AS horaFat,

       IFNULL(cast(T2.nfno_entrega AS CHAR), '')                                    AS nfnoEnt,
       IFNULL(T2.nfse_entrega, '')                                                  AS nfseEnt,
       if(T2.data_entrega = 0, NULL, cast(T2.data_entrega AS DATE))                 AS dataEnt,
       sec_to_time(nfe2.auxLong4)                                                   AS horaEnt,

       IFNULL(emp.no, 0)                                                            AS vendno,
       ifnull(custp.no, 0)                                                          AS custno,
       IFNULL(T2.fre_amt, 0) / 100                                                  AS frete,
       eord.amount / 100                                                            AS valor,
       IF(eoprdf.bits & POW(2, 1), 'R', 'E')                                        AS status,
       IFNULL(area.name, '')                                                        AS area,
       IFNULL(route.name, '')                                                       AS rota,
       IF(LEFT(eordrk.remarks__480, 2) = 'EF ', LEFT(eordrk.remarks__480, 11), ' ') AS obs,
       area.no                                                                      AS codArea,
       eord.userno                                                                  AS userno,
       IFNULL(U.name, '')                                                           AS username
FROM sqldados.eord
  INNER JOIN T2
	       ON (T2.storeno = eord.storeno AND T2.ordno = eord.ordno)
  LEFT JOIN  sqldados.users AS U
	       ON U.no = eord.userno
  LEFT JOIN  sqldados.custp
	       ON (custp.no = eord.custno)
  LEFT JOIN  sqldados.emp
	       ON (emp.no = eord.empno)
  LEFT JOIN  sqldados.eoprdf
	       ON (eoprdf.storeno = eord.storeno AND eoprdf.ordno = eord.ordno)
  LEFT JOIN  sqldados.paym
	       ON (paym.no = eord.paymno)
  LEFT JOIN  sqlpdv.pxa USE INDEX (e1)
	       ON (eord.storeno = pxa.storeno AND eord.ordno = pxa.eordno AND
		   eord.nfno_futura = pxa.nfno AND eord.nfse_futura = pxa.nfse)
  LEFT JOIN  sqldados.ctadd
	       ON (custp.no = ctadd.custno AND ctadd.seqno = eord.custno_addno)
  LEFT JOIN  sqldados.route
	       ON (ctadd.routeno = route.no)
  LEFT JOIN  sqldados.area
	       ON (area.no = route.areano)
  LEFT JOIN  sqldados.nf    AS nff
	       ON (T2.nfno_venda = nff.nfno AND T2.nfse_venda = nff.nfse AND
		   T2.storeno = nff.storeno)
  LEFT JOIN  sqldados.nf2   AS nff2
	       ON nff.storeno = nff2.storeno AND nff.pdvno = nff2.pdvno AND nff.xano = nff2.xano
  LEFT JOIN  sqldados.nf    AS nfe
	       ON (T2.nfno_entrega = nfe.nfno AND T2.nfse_entrega = nfe.nfse AND
		   T2.storeno = nfe.storeno)
  LEFT JOIN  sqldados.nf2   AS nfe2
	       ON nfe.storeno = nfe2.storeno AND nfe.pdvno = nfe2.pdvno AND nfe.xano = nfe2.xano
  LEFT JOIN  sqldados.eordrk
	       ON (eordrk.storeno = eord.storeno AND eordrk.ordno = eord.ordno)
WHERE (eord.storeno IN (1, 2, 3, 4, 5, 6))
  AND ((NOT eoprdf.bits & POW(2, 1)))
  AND eord.status NOT IN (3, 5)
  AND (eord.date >= 20200518)
GROUP BY eord.storeno, eord.ordno