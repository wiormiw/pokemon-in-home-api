-- Insert Roles
INSERT INTO roles (id, name) VALUES
  (gen_random_uuid(), 'ADMIN'),
  (gen_random_uuid(), 'PLAYER')
ON CONFLICT DO NOTHING;

-- Insert Pokémon Types
INSERT INTO types (id, name) VALUES
  (gen_random_uuid(), 'Fire'),
  (gen_random_uuid(), 'Water'),
  (gen_random_uuid(), 'Grass'),
  (gen_random_uuid(), 'Electric'),
  (gen_random_uuid(), 'Ice'),
  (gen_random_uuid(), 'Fighting'),
  (gen_random_uuid(), 'Poison'),
  (gen_random_uuid(), 'Ground'),
  (gen_random_uuid(), 'Flying'),
  (gen_random_uuid(), 'Psychic'),
  (gen_random_uuid(), 'Bug'),
  (gen_random_uuid(), 'Rock'),
  (gen_random_uuid(), 'Ghost'),
  (gen_random_uuid(), 'Dragon'),
  (gen_random_uuid(), 'Dark'),
  (gen_random_uuid(), 'Steel'),
  (gen_random_uuid(), 'Fairy')
ON CONFLICT DO NOTHING;

-- Insert Sample Pokémon
INSERT INTO pokemon (id, name, catch_rate) VALUES
  (gen_random_uuid(), 'Pikachu', 0.75),
  (gen_random_uuid(), 'Charmander', 0.7),
  (gen_random_uuid(), 'Squirtle', 0.8),
  (gen_random_uuid(), 'Bulbasaur', 0.85)
ON CONFLICT DO NOTHING;

-- Associate Pokémon with Types
INSERT INTO pokemon_types (pokemon_id, type_id)
SELECT p.id, t.id FROM pokemon p, types t WHERE p.name = 'Pikachu' AND t.name = 'Electric';

INSERT INTO pokemon_types (pokemon_id, type_id)
SELECT p.id, t.id FROM pokemon p, types t WHERE p.name = 'Charmander' AND t.name = 'Fire';

INSERT INTO pokemon_types (pokemon_id, type_id)
SELECT p.id, t.id FROM pokemon p, types t WHERE p.name = 'Squirtle' AND t.name = 'Water';

INSERT INTO pokemon_types (pokemon_id, type_id)
SELECT p.id, t.id FROM pokemon p, types t WHERE p.name = 'Bulbasaur' AND t.name IN ('Grass', 'Poison');
