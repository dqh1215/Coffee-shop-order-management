CREATE TABLE users (
                       id                CHAR(36)     NOT NULL PRIMARY KEY,
                       email             VARCHAR(100) NOT NULL UNIQUE,
                       phone_number      VARCHAR(20),
                       password_hash     VARCHAR(255) NOT NULL,
                       full_name         VARCHAR(100),
                       role              VARCHAR(20)  NOT NULL DEFAULT 'CUSTOMER',
                       created_at        DATETIME     NOT NULL,
                       created_by        VARCHAR(100) NOT NULL,
                       last_modified_at  DATETIME,
                       last_modified_by  VARCHAR(100),
                       is_active         BOOLEAN      NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE categories (
                            id                CHAR(36)     NOT NULL PRIMARY KEY,
                            name              VARCHAR(100) NOT NULL UNIQUE,
                            created_at        DATETIME     NOT NULL,
                            created_by        VARCHAR(100) NOT NULL,
                            last_modified_at  DATETIME,
                            last_modified_by  VARCHAR(100),
                            is_active         BOOLEAN      NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE menu_items (
                            id                CHAR(36)      NOT NULL PRIMARY KEY,
                            category_id       CHAR(36),
                            name              VARCHAR(150)  NOT NULL,
                            description       VARCHAR(500),
                            price             DECIMAL(10,2) NOT NULL,
                            available         BOOLEAN       NOT NULL DEFAULT TRUE,
                            created_at        DATETIME      NOT NULL,
                            created_by        VARCHAR(100)  NOT NULL,
                            last_modified_at  DATETIME,
                            last_modified_by  VARCHAR(100),
                            is_active         BOOLEAN       NOT NULL DEFAULT TRUE,
                            CONSTRAINT fk_menu_items_category FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE vouchers (
                          id                CHAR(36)      NOT NULL PRIMARY KEY,
                          code              VARCHAR(30)   NOT NULL UNIQUE,
                          discount_percent  INT           NOT NULL,
                          expired_at        DATETIME,
                          created_at        DATETIME      NOT NULL,
                          created_by        VARCHAR(100)  NOT NULL,
                          last_modified_at  DATETIME,
                          last_modified_by  VARCHAR(100),
                          is_active         BOOLEAN       NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE orders (
                        id                CHAR(36)      NOT NULL PRIMARY KEY,
                        user_id           CHAR(36)      NOT NULL,
                        voucher_id        CHAR(36),
                        status            VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
                        subtotal          DECIMAL(10,2) NOT NULL DEFAULT 0,
                        discount_amount   DECIMAL(10,2) NOT NULL DEFAULT 0,
                        tax_amount        DECIMAL(10,2) NOT NULL DEFAULT 0,
                        total_amount      DECIMAL(10,2) NOT NULL DEFAULT 0,
                        note              VARCHAR(255),
                        cancel_reason     VARCHAR(255),
                        created_at        DATETIME      NOT NULL,
                        created_by        VARCHAR(100)  NOT NULL,
                        last_modified_at  DATETIME,
                        last_modified_by  VARCHAR(100),
                        is_active         BOOLEAN       NOT NULL DEFAULT TRUE,
                        CONSTRAINT fk_orders_user    FOREIGN KEY (user_id)    REFERENCES users(id),
                        CONSTRAINT fk_orders_voucher FOREIGN KEY (voucher_id) REFERENCES vouchers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE order_items (
                             id                CHAR(36)      NOT NULL PRIMARY KEY,
                             order_id          CHAR(36)      NOT NULL,
                             menu_item_id      CHAR(36)      NOT NULL,
                             item_name         VARCHAR(150)  NOT NULL,
                             unit_price        DECIMAL(10,2) NOT NULL,
                             quantity          INT           NOT NULL,
                             line_total        DECIMAL(10,2) NOT NULL,
                             created_at        DATETIME      NOT NULL,
                             created_by        VARCHAR(100)  NOT NULL,
                             last_modified_at  DATETIME,
                             last_modified_by  VARCHAR(100),
                             is_active         BOOLEAN       NOT NULL DEFAULT TRUE,
                             CONSTRAINT fk_order_items_order     FOREIGN KEY (order_id)     REFERENCES orders(id),
                             CONSTRAINT fk_order_items_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO categories (id, name, created_at, created_by) VALUES
                                                              ('f78bf2e1-2e86-44a9-bd30-f49545384808', 'Coffee',    NOW(), 'SYSTEM'),
                                                              ('db7bd876-aab8-4935-95cb-fc1fe2c0d32d', 'Tea',       NOW(), 'SYSTEM'),
                                                              ('7ce6527f-e578-476a-8850-860bcb5e2947', 'Cake', NOW(), 'SYSTEM');

INSERT INTO menu_items (id, category_id, name, description, price, created_at, created_by) VALUES
                                                                                               ('913ea87e-d553-408e-b031-478d1cf8f949', 'f78bf2e1-2e86-44a9-bd30-f49545384808', 'Espresso',     'Espresso coffee',  25000, NOW(), 'SYSTEM'),
                                                                                               ('57a8881d-6ff4-4535-9e96-169faea48a75', 'f78bf2e1-2e86-44a9-bd30-f49545384808', 'Bac xiu',     'Vietnamese coffee with milk',   29000, NOW(), 'SYSTEM'),
                                                                                               ('0e7c6c63-fd1c-4e67-af5a-582fd329ac31', 'db7bd876-aab8-4935-95cb-fc1fe2c0d32d', 'Peach Tea', 'Cold peach tea',          35000, NOW(), 'SYSTEM'),
                                                                                               ('9afaccfd-fea2-42f1-99a9-1ec687a741a3', '7ce6527f-e578-476a-8850-860bcb5e2947', 'Croissant', 'French cake',      32000, NOW(), 'SYSTEM');

INSERT INTO vouchers (id, code, discount_percent, created_at, created_by) VALUES
    ('a6964d1b-2d42-4989-997b-3fc696ba17ad', 'WELCOME10', 10, NOW(), 'SYSTEM');