-- Inserción de películas variadas para probar el catálogo
INSERT INTO
    peliculas (
        titulo,
        director,
        anio,
        precio_alquiler,
        precio_compra,
        total_inventario,
        unidades_vendidas
    )
VALUES (
        'Inception',
        'Christopher Nolan',
        2010,
        3.50,
        12.00,
        15,
        0
    ),
    (
        'The Matrix',
        'Lana Wachowski',
        1999,
        2.99,
        9.50,
        20,
        5
    ),
    (
        'Interstellar',
        'Christopher Nolan',
        2014,
        3.99,
        14.00,
        10,
        2
    ),
    (
        'Pulp Fiction',
        'Quentin Tarantino',
        1994,
        2.50,
        8.00,
        12,
        8
    ),
    (
        'Blade Runner 2049',
        'Denis Villeneuve',
        2017,
        4.50,
        18.00,
        5,
        1
    ),
    (
        'Spirited Away',
        'Hayao Miyazaki',
        2001,
        3.00,
        15.00,
        8,
        3
    ),
    (
        'The Godfather',
        'Francis Ford Coppola',
        1972,
        2.00,
        10.00,
        0,
        10
    );