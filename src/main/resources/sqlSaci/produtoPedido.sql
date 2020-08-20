SELECT CAST(LPAD(E.prdno * 1, 6, '0') AS CHAR) AS codigo,
       TRIM(MID(P.name, 1, 37))                AS descricao,
       E.grade                                 AS grade,
       P.mfno_ref                              AS refFab,
       IFNULL(B.barcode, P.barcode)            AS barcode,
       ROUND(E.qtty / 1000)                    AS qtd,
       P.weight                                AS peso,
       E.price / 100                           AS vlUnit,
       ROUND(E.qtty / 1000) * (E.price / 100)  AS vlTotal,
       IFNULL(L.localizacao, '')               AS localizacao
FROM sqldados.eoprd          AS E
  INNER JOIN sqldados.prd    AS P
	       ON P.no = E.prdno
  LEFT JOIN  sqldados.prdbar AS B
	       USING (prdno, grade)
  LEFT JOIN  sqldados.prdloc AS L
	       ON L.prdno = E.prdno AND L.grade = E.grade AND L.storeno = 4
WHERE E.storeno = :storeno
  AND E.ordno = :ordno
GROUP BY E.storeno, E.ordno, E.prdno, E.grade
ORDER BY E.storeno, E.ordno, E.prdno, E.grade
