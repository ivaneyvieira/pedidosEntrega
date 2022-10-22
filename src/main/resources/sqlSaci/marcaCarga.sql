UPDATE sqldados.eord
SET eord.rmkMontagem = TRIM(CONCAT(RPAD(MID(eord.rmkMontagem, 1, 3), 3, ' '), RPAD(:marca, 1, ' '),
				   LPAD(:entrega, '0', 8), MID(eord.rmkMontagem, 13, 50)))
WHERE ordno = :ordno
  AND storeno = :storeno