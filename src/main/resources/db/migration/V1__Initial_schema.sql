CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';

CREATE EXTENSION IF NOT EXISTS chkpass WITH SCHEMA public;
COMMENT ON EXTENSION chkpass IS 'data type for auto-encrypted passwords';

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;
COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


SET search_path = public, pg_catalog;

CREATE TYPE markup_type AS ENUM (
    'None',
    'Markdown'
);

ALTER TYPE markup_type OWNER TO postgres;

CREATE TYPE unit_type AS ENUM (
    'Text',
    'Video',
    'Audio',
    'Image',
    'Quiz'
);
ALTER TYPE unit_type OWNER TO postgres;

SET default_tablespace = '';
SET default_with_oids = false;

CREATE TABLE course_lectures (
    course_id uuid NOT NULL,
    lecture_id uuid NOT NULL,
    lecture_ordinal integer NOT NULL
);
ALTER TABLE course_lectures OWNER TO postgres;

CREATE TABLE courses (
    course_id uuid DEFAULT uuid_generate_v1() NOT NULL,
    course_title text NOT NULL,
    course_description text NOT NULL,
    author_id uuid NOT NULL
);
ALTER TABLE courses OWNER TO postgres;

CREATE TABLE lecture_units (
    lecture_id uuid NOT NULL,
    unit_id uuid NOT NULL,
    unit_ordinal integer NOT NULL
);
ALTER TABLE lecture_units OWNER TO postgres;

CREATE TABLE lectures (
    lecture_id uuid DEFAULT uuid_generate_v1() NOT NULL,
    lecture_title text NOT NULL,
    lecture_description text NOT NULL,
    author_id uuid NOT NULL
);
ALTER TABLE lectures OWNER TO postgres;

CREATE TABLE units (
    unit_id uuid DEFAULT uuid_generate_v1() NOT NULL,
    unit_type unit_type NOT NULL,
    unit_title text NOT NULL,
    author_id uuid NOT NULL
);
ALTER TABLE units OWNER TO postgres;

CREATE TABLE units_text (
    unit_markup_type markup_type NOT NULL,
    unit_markup text NOT NULL
) INHERITS (units);
ALTER TABLE units_text OWNER TO postgres;

CREATE TABLE users (
    user_id uuid DEFAULT uuid_generate_v1() NOT NULL,
    user_email text NOT NULL,
    user_password chkpass NOT NULL,
    user_name text NOT NULL
);
ALTER TABLE users OWNER TO postgres;

ALTER TABLE ONLY units_text ALTER COLUMN unit_id SET DEFAULT uuid_generate_v1();

ALTER TABLE ONLY course_lectures ADD CONSTRAINT pk_course_lectures PRIMARY KEY (course_id, lecture_id);

ALTER TABLE ONLY courses ADD CONSTRAINT pk_courses PRIMARY KEY (course_id);

ALTER TABLE ONLY lecture_units ADD CONSTRAINT pk_lecture_units PRIMARY KEY (lecture_id, unit_id);

ALTER TABLE ONLY lectures ADD CONSTRAINT pk_lectures PRIMARY KEY (lecture_id);

ALTER TABLE ONLY units ADD CONSTRAINT pk_units PRIMARY KEY (unit_id);

ALTER TABLE ONLY units_text ADD CONSTRAINT pk_units_text PRIMARY KEY (unit_id);

ALTER TABLE ONLY users ADD CONSTRAINT pk_users PRIMARY KEY (user_id);

ALTER TABLE ONLY course_lectures ADD CONSTRAINT uniq_course_lectures_lecture_ordinal UNIQUE (course_id, lecture_id, lecture_ordinal);

ALTER TABLE ONLY lecture_units ADD CONSTRAINT uniq_lecture_units_unit_ordinal UNIQUE (lecture_id, unit_id, unit_ordinal);

ALTER TABLE ONLY users ADD CONSTRAINT uniq_users_email UNIQUE (user_email);

ALTER TABLE ONLY course_lectures ADD CONSTRAINT fk_course_lectures_course_id FOREIGN KEY (course_id) REFERENCES courses(course_id);

ALTER TABLE ONLY course_lectures ADD CONSTRAINT fk_course_lectures_lecture_id FOREIGN KEY (lecture_id) REFERENCES lectures(lecture_id);

ALTER TABLE ONLY courses ADD CONSTRAINT fk_courses_author_id FOREIGN KEY (author_id) REFERENCES users(user_id);

ALTER TABLE ONLY lecture_units ADD CONSTRAINT fk_lecture_units_lecture_id FOREIGN KEY (lecture_id) REFERENCES lectures(lecture_id);

ALTER TABLE ONLY lectures ADD CONSTRAINT fk_lectures_author_id FOREIGN KEY (author_id) REFERENCES users(user_id);

ALTER TABLE ONLY units ADD CONSTRAINT fk_units_author_id FOREIGN KEY (author_id) REFERENCES users(user_id);

ALTER TABLE ONLY units_text ADD CONSTRAINT fk_units_text_author_id FOREIGN KEY (author_id) REFERENCES users(user_id);

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;

REVOKE ALL ON TABLE course_lectures FROM PUBLIC;
REVOKE ALL ON TABLE course_lectures FROM postgres;
GRANT ALL ON TABLE course_lectures TO postgres;

REVOKE ALL ON TABLE courses FROM PUBLIC;
REVOKE ALL ON TABLE courses FROM postgres;
GRANT ALL ON TABLE courses TO postgres;
GRANT ALL ON TABLE courses TO mentor;

REVOKE ALL ON TABLE lecture_units FROM PUBLIC;
REVOKE ALL ON TABLE lecture_units FROM postgres;
GRANT ALL ON TABLE lecture_units TO postgres;
GRANT ALL ON TABLE lecture_units TO mentor;

REVOKE ALL ON TABLE lectures FROM PUBLIC;
REVOKE ALL ON TABLE lectures FROM postgres;
GRANT ALL ON TABLE lectures TO postgres;
GRANT ALL ON TABLE lectures TO mentor;

REVOKE ALL ON TABLE units FROM PUBLIC;
REVOKE ALL ON TABLE units FROM postgres;
GRANT ALL ON TABLE units TO postgres;
GRANT ALL ON TABLE units TO mentor;

REVOKE ALL ON TABLE units_text FROM PUBLIC;
REVOKE ALL ON TABLE units_text FROM postgres;
GRANT ALL ON TABLE units_text TO postgres;
GRANT ALL ON TABLE units_text TO mentor;

REVOKE ALL ON TABLE users FROM PUBLIC;
REVOKE ALL ON TABLE users FROM postgres;
GRANT ALL ON TABLE users TO postgres;
GRANT ALL ON TABLE users TO mentor;

