CREATE TABLE users (
                       id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username          VARCHAR(50)  NOT NULL UNIQUE,
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
                            id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name              VARCHAR(100) NOT NULL UNIQUE,
                            created_at        DATETIME     NOT NULL,
                            created_by        VARCHAR(100) NOT NULL,
                            last_modified_at  DATETIME,
                            last_modified_by  VARCHAR(100),
                            is_active         BOOLEAN      NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE menu_items (
                            id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                            category_id       BIGINT,
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
                          id                BIGINT AUTO_INCREMENT PRIMARY KEY,
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
                        id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id           BIGINT        NOT NULL,
                        voucher_id        BIGINT,
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
                             id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id          BIGINT        NOT NULL,
                             menu_item_id      BIGINT        NOT NULL,
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

INSERT INTO categories (name, created_at, created_by) VALUES
                                                          ('Cà phê',     NOW(), 'SYSTEM'),
                                                          ('Trà',        NOW(), 'SYSTEM'),
                                                          ('Bánh ngọt',  NOW(), 'SYSTEM');

INSERT INTO menu_items (category_id, name, description, price, created_at, created_by) VALUES
                                                                                           (1, 'Cà phê đen',     'Cà phê phin truyền thống',  25000, NOW(), 'SYSTEM'),
                                                                                           (1, 'Cà phê sữa',     'Cà phê phin kèm sữa đặc',   29000, NOW(), 'SYSTEM'),
                                                                                           (2, 'Trà đào cam sả', 'Trà đào mát lạnh',          35000, NOW(), 'SYSTEM'),
                                                                                           (3, 'Bánh croissant', 'Bánh sừng bò bơ Pháp',      32000, NOW(), 'SYSTEM');

INSERT INTO vouchers (code, discount_percent, created_at, created_by) VALUES
    ('WELCOME10', 10, NOW(), 'SYSTEM');