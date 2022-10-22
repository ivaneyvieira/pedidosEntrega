SELECT U.no,
       U.name,
       login,
       U.auxLong1             AS storeno,
       IFNULL(CAST(CONCAT(CHAR(ASCII(SUBSTRING(pswd, 1, 1)) + ASCII('e') - ASCII('j')),
			  CHAR(ASCII(SUBSTRING(pswd, 2, 1)) + ASCII('a') - ASCII('h')),
			  CHAR(ASCII(SUBSTRING(pswd, 3, 1)) + ASCII('c') - ASCII('k')),
			  CHAR(ASCII(SUBSTRING(pswd, 4, 1)) + ASCII(' ') - ASCII(' ')),
			  CHAR(ASCII(SUBSTRING(pswd, 5, 1)) + ASCII(' ') - ASCII('B')),
			  CHAR(ASCII(SUBSTRING(pswd, 6, 1)) + ASCII(' ') - ASCII(')')),
			  CHAR(ASCII(SUBSTRING(pswd, 7, 1)) + ASCII(' ') - ASCII(')')),
			  CHAR(ASCII(SUBSTRING(pswd, 8, 1)) + ASCII(' ') - ASCII('-'))) AS CHAR),
	      '')             AS senha,
       IFNULL(A.bitAcesso, 0) AS bitAcesso,
       U.prntno               AS prntno,
       P.name                 AS impressora,
       U.email                AS impressoraTermica
FROM sqldados.users          AS U
  LEFT JOIN sqldados.prntr   AS P
	      ON P.no = U.prntno
  LEFT JOIN sqldados.userApp AS A
	      ON A.userno = U.no AND A.appName = 'pedidosEntrega'
WHERE login = :login
   OR :login = 'TODOS'