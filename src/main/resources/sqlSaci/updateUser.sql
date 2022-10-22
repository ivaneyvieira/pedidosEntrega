DO @NO := (SELECT MAX(no)
	   FROM sqldados.users
	   WHERE login = :login);

UPDATE sqldados.users
SET auxLong1 = :loja,
    email    = :impressoraTermica
WHERE no = @NO;

INSERT INTO sqldados.userApp(userno, appName, bitAcesso)
VALUES (@NO, 'pedidosEntrega', :bitAcesso)
ON DUPLICATE KEY UPDATE bitAcesso = :bitAcesso