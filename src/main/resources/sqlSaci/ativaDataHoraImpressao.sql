UPDATE sqldados.eord
SET eord.l14 = IF(:data IS NULL, 0, :data),
    eord.l13 = IF(:hora IS NULL, 0, time_to_sec(:hora))
WHERE ordno = :ordno
  AND storeno = :storeno