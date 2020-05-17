DELETE
FROM ords
WHERE storeno = :storeno AND
      no BETWEEN :numeroI AND :numeroF;
DELETE
FROM orddlv
WHERE storeno = :storeno AND
      ordno BETWEEN :numeroI AND :numeroF;
DELETE
FROM oprd
WHERE storeno = :storeno AND
      ordno BETWEEN :numeroI AND :numeroF;
DELETE
FROM oprdxf
WHERE storeno = :storeno AND
      ordno BETWEEN :numeroI AND :numeroF;