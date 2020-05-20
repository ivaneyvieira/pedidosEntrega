UPDATE sqldados.eord
SET eord.rmkMontagem = CONCAT(:marca, mid(eord.rmkMontagem, length(:marca) + 1, 50))
WHERE ordno = :ordno
  AND storeno = :storeno