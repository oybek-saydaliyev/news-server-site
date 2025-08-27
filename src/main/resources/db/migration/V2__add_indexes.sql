CREATE UNIQUE INDEX idx_users_username ON users (username);
CREATE UNIQUE INDEX idx_users_email ON users (email);

CREATE INDEX idx_users_is_active ON users (is_active);

CREATE INDEX idx_category_name ON category (name);
CREATE INDEX idx_category_is_active ON category (is_active);


CREATE INDEX idx_news_category_id ON news (category_id);
CREATE INDEX idx_news_status ON news (status);
CREATE INDEX idx_news_title ON news (title);