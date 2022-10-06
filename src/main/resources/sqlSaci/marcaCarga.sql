UPDATE sqldados.eord
SET eord.rmkMontagem = TRIM(CONCAT(LPAD(eord.rmkMontagem, 3, ' '), LPAD(:marca, 1, ' '),
				   MID(eord.rmkMontagem, 4, 50)))
WHERE ordno = :ordno
  AND storeno = :storeno