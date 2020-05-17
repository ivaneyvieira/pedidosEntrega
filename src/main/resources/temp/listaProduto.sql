SELECT O.prdno, O.grade, TRIM(MID(P.name, 1, 37)) AS descricao, P.mfno AS fornecedor,
       LPAD(P.clno, 6, '0') AS centrodelucro, L.localizacao AS localizacao, P.typeno AS tipo,
       ROUND(O.qtty, 2) AS qtty
FROM sqldados.oprd           AS O
  INNER JOIN sqldados.prd    AS P
               ON (O.prdno = P.no)
  LEFT JOIN  sqldados.prdloc AS L
               ON (O.prdno = L.prdno AND O.grade = L.grade)
WHERE O.storeno = :storeno AND
      O.ordno = :ordno
GROUP BY L.localizacao, prdno, grade
ORDER BY L.localizacao, prdno, grade