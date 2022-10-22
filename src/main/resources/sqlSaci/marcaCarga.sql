UPDATE sqldados.eord
SET eord.rmkMontagem = RTRIM(CONCAT(RPAD(MID(eord.rmkMontagem, 1, 3), 3, ' '), RPAD(:marca, 1, ' '),
				   LPAD(:entrega, 8, '0'), MID(eord.rmkMontagem, 13, 50)))
WHERE ordno = :ordno
  AND storeno = :storeno