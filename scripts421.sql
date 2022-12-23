ALTER TABLE student
    ADD CONSTRAINT age_constraint CHECK(age > 15);

ALTER TABLE student
    ADD CONSTRAINT name_unique UNIQUE (name);

ALTER TABLE student
    ALTER name SET NOT NULL ;

ALTER TABLE faculty
    ADD CONSTRAINT name_color_unique UNIQUE (name, color);

ALTER TABLE student
    ALTER age SET DEFAULT 20;

