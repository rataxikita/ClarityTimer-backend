-- Script SQL para verificar los personajes en la base de datos
-- Ejecuta estos comandos en MySQL para diagnosticar el problema

-- 1. Ver cuántos personajes hay en total
SELECT COUNT(*) as total_personajes FROM personaje_sanrio;

-- 2. Ver todos los personajes con su estado
SELECT 
    id,
    nombre,
    precio_puntos,
    disponible,
    es_default,
    orden_tienda,
    rareza
FROM personaje_sanrio
ORDER BY orden_tienda;

-- 3. Ver cuántos personajes están disponibles
SELECT COUNT(*) as disponibles FROM personaje_sanrio WHERE disponible = true;

-- 4. Ver cuántos personajes NO están disponibles
SELECT COUNT(*) as no_disponibles FROM personaje_sanrio WHERE disponible = false;

-- 5. Ver los personajes que NO están disponibles (para saber cuáles faltan)
SELECT 
    nombre,
    disponible,
    es_default
FROM personaje_sanrio
WHERE disponible = false
ORDER BY nombre;

-- 6. Verificar que existan las categorías
SELECT COUNT(*) as total_categorias FROM categoria_personaje;

-- 7. Ver todas las categorías
SELECT id, nombre FROM categoria_personaje;

