UPDATE sqldados.eord
SET eord.rmkMontagem = RTRIM(CONCAT(RPAD(MID(eord.rmkMontagem, 1, 2), 2, ' '), RPAD(:marca, 1, ' '),
				   MID(eord.rmkMontagem, 4, 50)))
WHERE ordno = :ordno
  AND storeno = :storeno