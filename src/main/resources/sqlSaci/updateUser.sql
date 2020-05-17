UPDATE sqldados.abastecimentoLoc AS L INNER JOIN sqldados.users AS U USING (no)
SET bits2 = :bitAcesso
WHERE login = :login