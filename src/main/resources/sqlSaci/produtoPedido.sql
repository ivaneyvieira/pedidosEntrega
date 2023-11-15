DO @TIPO := :tipo;

SELECT CAST(LPAD(E.prdno * 1, 6, '0') AS CHAR) AS codigo,
       TRIM(MID(P.name, 1, 37))                AS descricao,
       E.grade                                 AS grade,
       P.mfno_ref                              AS refFab,
       IFNULL(B.barcode, P.barcode)            AS barcode,
       ROUND(E.qtty / 1000)                    AS qtd,
       P.weight                                AS peso,
       IFNULL(X.price, E.price) / 100          AS vlUnit,
       ROUND(E.qtty / 1000) * (E.price / 100)  AS vlTotal,
       IFNULL(L.localizacao, '')               AS localizacao,
       IFNULL(A.form_label, '')                AS rotulo
FROM sqldados.eoprd AS E
       INNER JOIN sqldados.eord AS EO
                  USING (storeno, ordno)
       INNER JOIN sqldados.prd AS P
                  ON P.no = E.prdno
       LEFT JOIN sqldados.prdbar AS B
                 USING (prdno, grade)
       LEFT JOIN sqldados.prdalq AS A
                 ON A.prdno = P.no
       LEFT JOIN sqldados.prdloc AS L
                 ON L.prdno = E.prdno AND L.grade = E.grade AND L.storeno = 4
       LEFT JOIN sqldados.eoprdf AS O
                 ON (O.storeno = E.storeno AND O.ordno = E.ordno AND O.prdno = E.prdno AND
                     O.grade = E.grade)
       LEFT JOIN sqldados.xaprd AS X
                 ON X.storeno = EO.storeno AND X.nfno = EO.nfno AND X.nfse = EO.nfse AND
                    X.prdno = E.prdno AND X.grade = E.grade
WHERE E.storeno = :storeno
  AND E.ordno = :ordno
  AND ((@TIPO = 'R') AND (O.bits & POW(2, 1)) OR (@TIPO = 'E') AND (NOT O.bits & POW(2, 1)) OR
       (EO.storeno = 4 AND EO.empno = 440))
GROUP BY E.storeno, E.ordno, E.prdno, E.grade
