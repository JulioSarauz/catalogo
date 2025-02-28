INSERT INTO categories (name) VALUES ('Programación');
INSERT INTO categories (name) VALUES ('Ficción');
INSERT INTO categories (name) VALUES ('Historia');

INSERT INTO books (title, author, isbn, publication_date, rating, price, stock, is_visible, category_id)
VALUES ('Spring Boot en Acción', 'Craig Walls', '9781617292545', '2016-11-28', 4.5, 29.99, 100, TRUE, 1);

INSERT INTO books (title, author, isbn, publication_date, rating, price, stock, is_visible, category_id)
VALUES ('Java: The Complete Reference', 'Herbert Schildt', '9781260440232', '2018-12-10', 4.8, 45.00, 150, TRUE, 1);

INSERT INTO books (title, author, isbn, publication_date, rating, price, stock, is_visible, category_id)
VALUES ('Cien años de soledad', 'Gabriel García Márquez', '9780307474728', '1967-05-30', 4.9, 19.99, 200, TRUE, 2);

INSERT INTO books (title, author, isbn, publication_date, rating, price, stock, is_visible, category_id)
VALUES ('Don Quijote de la Mancha', 'Miguel de Cervantes', '9788420412149', '1605-01-16', 4.7, 25.00, 80, TRUE, 2);

INSERT INTO books (title, author, isbn, publication_date, rating, price, stock, is_visible, category_id)
VALUES ('Sapiens: De animales a dioses', 'Yuval Noah Harari', '9780062316097', '2011-02-04', 4.8, 22.50, 120, TRUE, 3);

INSERT INTO books (title, author, isbn, publication_date, rating, price, stock, is_visible, category_id)
VALUES ('Breve historia del tiempo', 'Stephen Hawking', '9780553380163', '1988-04-01', 4.6, 18.99, 90, FALSE, 3);
