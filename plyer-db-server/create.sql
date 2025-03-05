


-- user table
create table user_user (
	id SERIAL primary key,
	user_name VARCHAR(20) not null,
	user_email VARCHAR(200) not null,
	user_password text not null,
	is_master boolean not null default false, --true: admin
	is_active boolean not null default true, --휴먼 유저?
	is_del boolean not null default false, --유저 삭제
	created_at TIMESTAMPTZ default now(),
	updated_at TIMESTAMPTZ default now()
);
CREATE INDEX idx_user_user_user_email ON user_user(user_email);

create table category (
	id SERIAL primary key, --category id
	category_name VARCHAR(50) UNIQUE not null --category name
);


-- user role
create table user_role (
	id SERIAL primary key,
	user_id BIGINT not null,
	user_role_type int not null, --1: update, 2: read, 3: update, 4: delete
	creator_id BIGINT not null,
	created_at TIMESTAMPTZ default now(),
	updator_id BIGINT not null,
	updated_at TIMESTAMPTZ default now(),
	FOREIGN KEY (user_id) REFERENCES user_user(id) ON UPDATE CASCADE
);
CREATE INDEX idx_user_role_user_id ON user_role(user_id);
CREATE INDEX idx_user_role_creator_id ON user_role(creator_id);
CREATE INDEX idx_user_role_updator_id ON user_role(updator_id);

 select * from image_imagehub;
-- image hub system table
create table image_imagehub (
	id SERIAL primary key,
	IMAGE_ORIGIN_NAME text not null,
	image_name text not null,
	image_ext VARCHAR(10) not null,
	image_size BIGINT not null,
	image_description text,
	image_content_type text not null,
    image_path TEXT NOT NULL,
    thumbnail_path TEXT,
	is_del boolean not null default false,
	creator_id BIGINT not null,
	created_at TIMESTAMPTZ default now(),
	updator_id BIGINT not null,
	updated_at TIMESTAMPTZ default now(),
    FOREIGN KEY (creator_id) REFERENCES user_user(id) ON UPDATE CASCADE,
    FOREIGN KEY (updator_id) REFERENCES user_user(id) ON UPDATE CASCADE
);
CREATE INDEX idx_image_imagehub_creator_id ON image_imagehub(creator_id);

-- image category table
create table image_category (
	id SERIAL primary key,
	image_id BIGINT not null,
	category_id int not null,
	is_del boolean not null default false,
	creator_id BIGINT not null,
	created_at TIMESTAMPTZ default now(),
	updator_id BIGINT not null,
	updated_at TIMESTAMPTZ default now(),
    FOREIGN KEY (image_id) REFERENCES image_imagehub(id) ON UPDATE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category(id) ON UPDATE CASCADE
);
CREATE INDEX idx_image_category_image_id ON image_category(image_id);
CREATE INDEX idx_image_category_category_id ON image_category(category_id);
CREATE UNIQUE INDEX idx_unique_active_image_category ON image_category(image_id, category_id) WHERE is_del = false; --카테고리 중복 할당 방지
